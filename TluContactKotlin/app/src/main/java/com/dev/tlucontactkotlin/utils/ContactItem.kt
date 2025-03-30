package com.dev.tlucontactkotlin.utils

import com.dev.tlucontactkotlin.models.Staff
import com.dev.tlucontactkotlin.models.Student
import com.dev.tlucontactkotlin.models.UnitContact

sealed class ContactItem {

    data class StaffItem(val staff: Staff) : ContactItem()
    data class StudentItem(val student: Student) : ContactItem()
    data class UnitItem(val unit: UnitContact) : ContactItem()
}
