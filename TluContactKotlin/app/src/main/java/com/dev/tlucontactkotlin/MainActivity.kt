package com.dev.tlucontactkotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import com.dev.tlucontactkotlin.ui.auth.AuthActivity
import com.dev.tlucontactkotlin.ui.auth.ProfileActivity
import com.dev.tlucontactkotlin.ui.contact.ContactActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private lateinit var btnUnitContact: Button
    private lateinit var btnStaffContact: Button
    private lateinit var btnStudentContact: Button
    private lateinit var btnLogout: LinearLayout
    private lateinit var txtUserName: TextView
    private lateinit var btnProfile: ImageView
    private var userRole: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth = FirebaseAuth.getInstance()

        // Ánh xạ view
        btnUnitContact = findViewById(R.id.btn_unit_contact)
        btnStaffContact = findViewById(R.id.btn_staff_contact)
        btnStudentContact = findViewById(R.id.btn_students_contact)
        btnLogout = findViewById(R.id.btn_logout)
        txtUserName = findViewById(R.id.textView2)
        btnProfile = findViewById(R.id.btn_profile)

        // Kiểm tra nếu chưa đăng nhập thì chuyển sang AuthActivity
        if (auth.currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        // Lấy thông tin user hiện tại từ Firestore
        val currentUser = auth.currentUser
        currentUser?.let {
            val userId = it.uid
            FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val fullName = document.getString("fullName") ?: "Người dùng"
                        userRole = document.getString("role") ?: "student"
                        txtUserName.text = "Xin chào: $fullName"

                        // Ẩn nút nếu là sinh viên
                        if (userRole == "student") {
                            btnUnitContact.visibility = View.GONE
                            btnStaffContact.visibility = View.GONE
                        }
                    } else {
                        txtUserName.text = "Xin chào: Người dùng"
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Lỗi khi tải thông tin người dùng: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        // Xử lý sự kiện các nút
        btnUnitContact.setOnClickListener {
            openContactActivity("unit")
        }

        btnStudentContact.setOnClickListener {
            openContactActivity("student")
        }

        btnStaffContact.setOnClickListener {
            openContactActivity("staff")
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        btnProfile.setOnClickListener {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                FirebaseFirestore.getInstance().collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val userData = HashMap<String, String>()
                            document.data?.forEach { (key, value) ->
                                userData[key] = value.toString()
                            }

                            val intent = Intent(this, ProfileActivity::class.java)
                            intent.putExtra("USER_ID", userId)
                            intent.putExtra("USER_DATA", userData)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this,
                                "Không tìm thấy thông tin người dùng",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Lỗi khi tải dữ liệu: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(this, "Không có người dùng đăng nhập", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openContactActivity(contactType: String) {
        val intent = Intent(this, ContactActivity::class.java)
        intent.putExtra("CONTACT_TYPE", contactType)
        intent.putExtra("USER_ROLE", userRole)
        startActivity(intent)
    }
}
