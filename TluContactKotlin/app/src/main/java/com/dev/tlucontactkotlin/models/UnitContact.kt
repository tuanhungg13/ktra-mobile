package com.dev.tlucontactkotlin.models

import java.io.Serializable

data class UnitContact(
    val id: String = "",          // Document ID (mã đơn vị)
    val code: String = "",        // Mã đơn vị
    val name: String = "",        // Tên đơn vị
    val address: String = "",     // Địa chỉ
    val logoURL: String = "",     // Đường dẫn logo
    val phone: String = "",       // Số điện thoại
    val email: String = "",       // Email
    val fax: String = "",         // Fax
    val parentUnit: String? = null, // ID đơn vị cha (có thể null)
    val type: String = ""         // Loại đơn vị (Khoa, Phòng, Trung tâm,...)
) : Serializable
