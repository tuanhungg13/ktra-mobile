package com.dev.tlucontactkotlin.models

import java.io.Serializable

data class Staff(
    val id: String = "",         // Document ID (mã cán bộ)
    val staffId: String = "",    // Mã cán bộ
    val fullName: String = "",   // Họ và tên
    val position: String = "",   // Chức vụ
    val phone: String = "",      // Số điện thoại
    val email: String = "",      // Email
    val photoURL: String = "",   // Đường dẫn ảnh đại diện
    val unitId: String = "",     // ID đơn vị trực thuộc (tham chiếu đến collection `units`)
    val userId: String = "",     // UID từ Firebase Authentication (liên kết với `users`)
    val address: String = ""     // Địa chỉ
) : Serializable