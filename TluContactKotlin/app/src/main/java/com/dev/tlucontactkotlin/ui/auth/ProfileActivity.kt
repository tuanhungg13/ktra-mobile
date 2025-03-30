package com.dev.tlucontactkotlin.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dev.tlucontactkotlin.R
import com.dev.tlucontactkotlin.databinding.ActivityProfileBinding
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var userId: String? = null
    private var userData: HashMap<String, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Áp padding tránh overlap với status bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Lấy dữ liệu user được truyền qua Intent
        userId = intent.getStringExtra("USER_ID")
        userData = intent.getSerializableExtra("USER_DATA") as? HashMap<String, String>

        if (userData != null) {
            populateFields(userData!!)
        }

        // Sự kiện lưu dữ liệu sau chỉnh sửa
        binding.saveButton.setOnClickListener {
            updateUserData()
        }
    }

    private fun populateFields(data: HashMap<String, String>) {
        binding.fullNameEditText.setText(data["fullName"])
        binding.phoneEditText.setText(data["phone"])
        binding.emailEditText.setText(data["email"])
        binding.addressEditText.setText(data["address"])
        // ... các trường khác nếu có

        val photoUrl = data["photoURL"]
        if (!photoUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(binding.previewImage)
        }
    }

    private fun updateUserData() {
        val updatedData = hashMapOf(
            "fullName" to binding.fullNameEditText.text.toString().trim(),
            "phone" to binding.phoneEditText.text.toString().trim(),
            "email" to binding.emailEditText.text.toString().trim(),
            "address" to binding.addressEditText.text.toString().trim(),
            // ... các trường khác nếu có
        )

        if (userId == null) {
            Toast.makeText(this, "Không tìm thấy ID người dùng", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseFirestore.getInstance().collection("users")
            .document(userId!!)
            .update(updatedData as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
