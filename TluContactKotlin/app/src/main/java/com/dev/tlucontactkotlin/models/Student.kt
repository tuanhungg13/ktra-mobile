package com.dev.tlucontactkotlin.models

import java.io.Serializable

data class Student(
    val id: String = "",         // Document ID (mã sinh viên)
    val studentId: String = "",  // Mã sinh viên
    val fullName: String = "",   // Họ và tên
    val photoURL: String = "",   // Ảnh đại diện
    val phone: String = "",      // Số điện thoại
    val email: String = "",      // Email
    val address: String = "",    // Địa chỉ
    val className: String = "",  // Lớp hoặc đơn vị trực thuộc cấp thấp
    val userId: String = ""      // UID từ Firebase Authentication (liên kết với `users`)
) : Serializable