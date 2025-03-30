package com.dev.tlucontactkotlin.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dev.tlucontactkotlin.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var edtConfirmPassword: TextInputEditText
    private lateinit var edtName: TextInputEditText
    private lateinit var edtLayoutEmail: TextInputLayout
    private lateinit var edtLayoutPassword: TextInputLayout
    private lateinit var edtLayoutConfirmPassword: TextInputLayout
    private lateinit var edtLayoutName: TextInputLayout
    private lateinit var btnRegister: Button
    private lateinit var txtLogin: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var edtClass: TextInputEditText
    private var isLoading = false

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        auth = FirebaseAuth.getInstance()

        edtEmail = view.findViewById(R.id.edt_email)
        edtPassword = view.findViewById(R.id.edt_password)
        edtConfirmPassword = view.findViewById(R.id.edt_confirm_pw)
        edtLayoutName = view.findViewById(R.id.edt_layout_name)
        edtLayoutEmail = view.findViewById(R.id.edt_layout_email)
        edtLayoutPassword = view.findViewById(R.id.edt_layout_pw)
        edtLayoutConfirmPassword = view.findViewById(R.id.edt_layout_confirm_pw)
        edtName = view.findViewById(R.id.edt_name)
        btnRegister = view.findViewById(R.id.btn_register)
        txtLogin = view.findViewById(R.id.txt_link_login)
        progressBar = activity?.findViewById<ProgressBar>(R.id.prg_loading)!!
        edtClass = view.findViewById(R.id.edt_class)
        btnRegister.setOnClickListener { registerUser() }
        txtLogin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.authContainer, LoginFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun setLoadingState(loading: Boolean) {
        isLoading = loading
        btnRegister.isEnabled = !loading
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun registerUser() {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val confirmPassword = edtConfirmPassword.text.toString().trim()
        val fullName = edtName.text.toString().trim()
        val className = edtClass.text.toString().trim()
        if (validateInputs(email, fullName, password, confirmPassword)) {
            setLoadingState(true)

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    val uid = user.uid
                                    val db = FirebaseFirestore.getInstance()
                                    val role = getRoleFromEmail(email)

                                    val userMap = hashMapOf(
                                        "fullName" to fullName,
                                        "email" to email,
                                        "role" to role,
                                        "className" to className
                                    )

                                    db.collection("users").document(uid).set(userMap)
                                        .addOnSuccessListener {
                                            //Gán userId nếu tồn tại trong staffs hoặc students
                                            updateExistingStaffOrStudent(email, uid)

                                            Toast.makeText(
                                                requireContext(),
                                                "Vui lòng kiểm tra email để xác nhận tài khoản.",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            edtEmail.setText("")
                                            edtName.setText("")
                                            edtPassword.setText("")
                                            edtConfirmPassword.setText("")

                                            parentFragmentManager.beginTransaction()
                                                .replace(R.id.authContainer, LoginFragment())
                                                .addToBackStack(null)
                                                .commit()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("Firestore", "Lỗi lưu dữ liệu: ${e.message}")
                                            Toast.makeText(
                                                requireContext(),
                                                "Không thể lưu thông tin người dùng.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                } else {
                                    Log.e(
                                        "Email",
                                        "Lỗi khi gửi email xác thực.",
                                        emailTask.exception
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        "Gửi email xác thực thất bại.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                setLoadingState(false)
                            }
                    } else {
                        setLoadingState(false)
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            Log.e("SignUp", "Email đã được sử dụng.")
                            Toast.makeText(
                                requireContext(),
                                "Email đã tồn tại, hãy thử đăng nhập.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Log.e("SignUp", "Lỗi khi tạo tài khoản: ${task.exception?.message}")
                            Toast.makeText(
                                requireContext(),
                                "Lỗi: ${task.exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
        }
    }

    private fun updateExistingStaffOrStudent(email: String, uid: String) {
        val db = FirebaseFirestore.getInstance()
        val fullName = edtName.text.toString().trim()
        val isStudent = email.endsWith("@e.tlu.edu.vn")
        val collection = if (isStudent) "students" else "staffs"

        db.collection(collection)
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    // ✅ Đã tồn tại → cập nhật userId
                    val docId = result.documents[0].id
                    db.collection(collection).document(docId)
                        .update("userId", uid)
                        .addOnSuccessListener {
                            Log.d("Register", "Đã gán userId cho $collection.")
                        }
                } else {
                    val newData = hashMapOf(
                        "email" to email,
                        "fullName" to fullName,
                        "userId" to uid,
                        "createdAt" to System.currentTimeMillis()
                    )

                    db.collection(collection)
                        .add(newData)
                        .addOnSuccessListener {
                            Log.d("Register", "Đã tạo mới $collection và gán userId.")
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Register", "Lỗi khi tìm hoặc tạo người dùng: ${e.message}")
            }
    }

    private fun validateInputs(
        email: String,
        fullName: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        edtLayoutEmail.error = null
        edtLayoutPassword.error = null
        edtLayoutConfirmPassword.error = null
        edtLayoutName.error = null

        val passwordRegex =
            Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")

        if (email.isEmpty()) {
            edtLayoutEmail.error = "Email không được để trống"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtLayoutEmail.error = "Email không hợp lệ"
            isValid = false
        } else if (!email.endsWith("@tlu.edu.vn") && !email.endsWith("@e.tlu.edu.vn")) {
            edtLayoutEmail.error = "Email phải là @tlu.edu.vn (CBGV) hoặc @e.tlu.edu.vn (Sinh viên)"
            isValid = false
        }

        if (fullName.isEmpty()) {
            edtLayoutName.error = "Họ và tên không được để trống"
            isValid = false
        }

        if (password.isEmpty()) {
            edtLayoutPassword.error = "Mật khẩu không được để trống"
            isValid = false
        } else if (!password.matches(passwordRegex)) {
            edtLayoutPassword.error =
                "Mật khẩu cần ít nhất 8 ký tự, gồm chữ hoa, thường, số và ký tự đặc biệt."
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            edtLayoutConfirmPassword.error = "Vui lòng nhập lại mật khẩu"
            isValid = false
        } else if (confirmPassword != password) {
            edtLayoutConfirmPassword.error = "Mật khẩu xác nhận không khớp"
            isValid = false
        }

        return isValid
    }

    private fun getRoleFromEmail(email: String): String {
        return when {
            email.endsWith("@tlu.edu.vn") -> "staff"
            email.endsWith("@e.tlu.edu.vn") -> "student"
            else -> "user"
        }
    }
}
