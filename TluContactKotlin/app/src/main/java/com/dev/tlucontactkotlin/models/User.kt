package com.dev.tlucontactkotlin.models

data class User(
    val id: String = "",          // UID từ Firebase Authentication
    val email: String = "",       // Email người dùng
    val role: String = "",        // "CBGV" hoặc "SV"
    val displayName: String = "", // Họ và tên
    val photoURL: String = "",    // Đường dẫn ảnh đại diện
    val phoneNumber: String = ""  // Số điện thoại
)
