package com.dev.tlucontactkotlin.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dev.tlucontactkotlin.MainActivity
import com.google.android.material.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.dev.tlucontactkotlin.R


class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var edtLayoutEmail: TextInputLayout
    private lateinit var edtLayoutPassword: TextInputLayout
    private lateinit var btnLogin: Button
    private lateinit var txtRegister: TextView
    private lateinit var progressBar: ProgressBar

    private var isLoading = false

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        auth = FirebaseAuth.getInstance()

        edtEmail = view.findViewById(R.id.edt_email)
        edtPassword = view.findViewById(R.id.edt_password)
        edtLayoutEmail = view.findViewById(R.id.edt_layout_email)
        edtLayoutPassword = view.findViewById(R.id.edt_layout_pw)
        btnLogin = view.findViewById(R.id.btn_login)
        txtRegister = view.findViewById(R.id.txt_register)
        progressBar = activity?.findViewById<ProgressBar>(R.id.prg_loading)!!


        btnLogin.setOnClickListener { loginUser() }
        txtRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.authContainer, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }


    private fun setLoadingState(loading: Boolean) {
        isLoading = loading
        btnLogin.isEnabled = !loading
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }


    private fun loginUser() {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        // Reset lỗi trước khi kiểm tra
        edtLayoutEmail.error = null
        edtLayoutPassword.error = null

        var isValid = true

        // Kiểm tra email
        if (email.isEmpty()) {
            edtLayoutEmail.error = "Email không được để trống"
            isValid = false
        }

        // Kiểm tra mật khẩu
        if (password.isEmpty()) {
            edtLayoutPassword.error = "Mật khẩu không được để trống"
            isValid = false
        }

        if (!isValid) return
        setLoadingState(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        Toast.makeText(
                            requireContext(),
                            "Đăng nhập thành công!",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()
                        setLoadingState(false)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Vui lòng xác nhận email trước khi đăng nhập!",
                            Toast.LENGTH_LONG
                        ).show()
                        auth.signOut()
                        setLoadingState(false)
                    }
                } else {
                    setLoadingState(false)
                    edtLayoutPassword.error = "Email hoặc mật khẩu không đúng"
                }
            }

    }

}