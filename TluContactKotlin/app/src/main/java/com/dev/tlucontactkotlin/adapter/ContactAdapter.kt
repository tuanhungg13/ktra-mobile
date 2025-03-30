package com.dev.tlucontactkotlin.ui.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.tlucontactkotlin.R
import com.dev.tlucontactkotlin.models.Staff
import com.dev.tlucontactkotlin.models.Student
import com.dev.tlucontactkotlin.models.UnitContact
import com.dev.tlucontactkotlin.utils.ContactItem

class ContactAdapter(
    private var contacts: List<ContactItem>,
    private val contactType: String,
    private val onItemClick: (ContactItem) -> Unit

) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_STAFF = 1
        private const val TYPE_STUDENT = 2
        private const val TYPE_UNIT = 3
    }

    override fun getItemViewType(position: Int): Int {
        return when (contacts[position]) {
            is ContactItem.StaffItem -> TYPE_STAFF
            is ContactItem.StudentItem -> TYPE_STUDENT
            is ContactItem.UnitItem -> TYPE_UNIT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_STAFF -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_staff, parent, false)
                StaffViewHolder(view, onItemClick)

            }

            TYPE_STUDENT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_student, parent, false)
                StudentViewHolder(view, onItemClick)
            }

            TYPE_UNIT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_unit, parent, false)
                UnitViewHolder(view, onItemClick)
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val contact = contacts[position]) {
            is ContactItem.StaffItem -> (holder as StaffViewHolder).bind(contact.staff)
            is ContactItem.UnitItem -> (holder as UnitViewHolder).bind(contact.unit)
            is ContactItem.StudentItem -> (holder as StudentViewHolder).bind(contact.student)
        }
    }

    override fun getItemCount(): Int = contacts.size

    fun updateContacts(newContacts: List<ContactItem>) {
        contacts = newContacts
        notifyDataSetChanged()
    }

    class StaffViewHolder(
        view: View,
        private val onItemClick: (ContactItem) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val imgAvatar: ImageView = view.findViewById(R.id.img_logo_staff)
        private val txtName: TextView = view.findViewById(R.id.txt_staff_name)
        private val txtPosition: TextView = view.findViewById(R.id.txt_staff_position)
        private val txtPhone: TextView = view.findViewById(R.id.txt_staff_phone)
        private val txtEmail: TextView = view.findViewById(R.id.txt_staff_mail)
        fun bind(staff: Staff) {
            txtName.text = staff.fullName
            txtPosition.text = staff.position
            txtPhone.text = staff.phone
            txtEmail.text = staff.email

            Glide.with(itemView.context)
                .load(staff.photoURL ?: R.drawable.logo)
                .placeholder(R.drawable.logo) // ảnh mặc định khi chưa tải xong
                .error(R.drawable.logo)
                .into(imgAvatar)
            itemView.setOnClickListener {
                onItemClick(ContactItem.StaffItem(staff))
            }
        }
    }

    class StudentViewHolder(
        view: View,
        private val onItemClick: (ContactItem) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val imgAvatar: ImageView = view.findViewById(R.id.img_avatar_std)
        private val txtFullName: TextView = view.findViewById(R.id.txt_std_name)
        private val txtClassName: TextView = view.findViewById(R.id.txt_std_className)
        private val txtPhone: TextView = view.findViewById(R.id.txt_std_phone)
        private val txtEmail: TextView = view.findViewById(R.id.txt_std_mail)

        fun bind(student: Student) {
            txtFullName.text = student.fullName
            txtClassName.text = student.className
            txtPhone.text = student.phone
            txtEmail.text = student.email
            Glide.with(itemView.context).load(student.photoURL)
                .placeholder(R.drawable.logo) // ảnh mặc định khi chưa tải xong
                .error(R.drawable.logo)
                .into(imgAvatar)
            itemView.setOnClickListener {
                onItemClick(ContactItem.StudentItem(student))
            }
        }
    }

    class UnitViewHolder(
        view: View,
        private val onItemClick: (ContactItem) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val imgLogo: ImageView = view.findViewById(R.id.img_logo_unit)
        private val txtName: TextView = view.findViewById(R.id.txt_unit_name)
        private val txtPhone: TextView = view.findViewById(R.id.txt_unit_phone)


        fun bind(unit: UnitContact) {
            txtName.text = unit.name
            txtPhone.text = unit.phone
            Glide.with(itemView.context).load(unit.logoURL)
                .placeholder(R.drawable.logo) // ảnh mặc định khi chưa tải xong
                .error(R.drawable.logo)
                .into(imgLogo)
            itemView.setOnClickListener {
                onItemClick(ContactItem.UnitItem(unit))
            }
        }
    }
}

