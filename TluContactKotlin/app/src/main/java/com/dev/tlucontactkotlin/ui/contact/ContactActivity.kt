package com.dev.tlucontactkotlin.ui.contact

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.tlucontactkotlin.MainActivity
import com.dev.tlucontactkotlin.R
import com.dev.tlucontactkotlin.models.Staff
import com.dev.tlucontactkotlin.models.Student
import com.dev.tlucontactkotlin.models.UnitContact
import com.dev.tlucontactkotlin.utils.ContactItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import androidx.core.view.WindowInsetsCompat.Type


class ContactActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private val db = FirebaseFirestore.getInstance()
    private val contactList = mutableListOf<ContactItem>()
    private lateinit var adapter: ContactAdapter
    private lateinit var contactType: String
    private lateinit var userRole: String
    private lateinit var btnAddContact: FloatingActionButton
    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_layout)) { v, insets ->
            val systemBars = insets.getInsets(Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        contactType = intent.getStringExtra("CONTACT_TYPE") ?: return

        supportActionBar?.apply {
            title = when (contactType) {
                "staff" -> "Danh bạ CBNV"
                "student" -> "Danh bạ sinh viên"
                "unit" -> "Danh bạ đơn vị"
                else -> "Danh bạ"
            }

            setDisplayHomeAsUpEnabled(true)
        }

        recyclerView = findViewById(R.id.rcv_contact)
        emptyText = findViewById(R.id.emptyText)
        btnAddContact = findViewById(R.id.btn_add_contact)
        progressBar = findViewById(R.id.progressBar)

        userRole = intent.getStringExtra("USER_ROLE") ?: ""

        // Ẩn nút thêm nếu không phải admin
        if (userRole != "staff") {
            btnAddContact.visibility = View.GONE
        }

        btnAddContact.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            intent.putExtra("CONTACT_TYPE", contactType)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ContactAdapter(contactList, contactType) { item ->
            val intent = Intent(this, DetailActivity::class.java)
            when (item) {
                is ContactItem.StaffItem -> {
                    intent.putExtra("type", "staff")
                    intent.putExtra("data", item.staff)
                }

                is ContactItem.StudentItem -> {
                    intent.putExtra("type", "student")
                    intent.putExtra("data", item.student)
                }

                is ContactItem.UnitItem -> {
                    intent.putExtra("type", "unit")
                    intent.putExtra("data", item.unit)
                }
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        fetchContacts(contactType)
    }

    override fun onResume() {
        super.onResume()
        fetchContacts(contactType)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun fetchContacts(contactType: String) {
        progressBar.visibility = View.VISIBLE

        val collectionName = when (contactType) {
            "staff" -> "staffs"
            "unit" -> "units"
            "student" -> "students"
            else -> {
                emptyText.text = "Loại danh bạ không hợp lệ."
                emptyText.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                return
            }
        }

        db.collection(collectionName)
            .get()
            .addOnSuccessListener { snapshots ->
                contactList.clear()

                if (!snapshots.isEmpty) {
                    for (document in snapshots) {
                        try {
                            when (contactType) {
                                "staff" -> {
                                    val staff = Staff(
                                        id = document.getString("id") ?: "Không có mã giảng viên",
                                        fullName = document.getString("fullName") ?: "Không có tên",
                                        position = document.getString("position")
                                            ?: "Không có chức vụ",
                                        phone = document.getString("phone")
                                            ?: "Không có số điện thoại",
                                        email = document.getString("email") ?: "Không có email",
                                        photoURL = document.getString("photoURL") ?: "",
                                        address = document.getString("address")
                                            ?: "Không có địa chỉ"
                                    )
                                    contactList.add(ContactItem.StaffItem(staff))
                                }

                                "unit" -> {
                                    val unit = UnitContact(
                                        id = document.getString("id") ?: "Không có mã đơn vị",
                                        name = document.getString("name") ?: "Không có tên",
                                        phone = document.getString("phone")
                                            ?: "Không có số điện thoại",
                                        logoURL = document.getString("logoURL") ?: "",
                                        address = document.getString("address")
                                            ?: "Không có địa chỉ",
                                        email = document.getString("email") ?: "Không có email"
                                    )
                                    contactList.add(ContactItem.UnitItem(unit))
                                }

                                "student" -> {
                                    val student = Student(
                                        id = document.getString("id") ?: "Không có mã sinh viên",
                                        fullName = document.getString("fullName") ?: "Không có tên",
                                        phone = document.getString("phone")
                                            ?: "Không có số điện thoại",
                                        photoURL = document.getString("photoURL") ?: "",
                                        email = document.getString("email") ?: "Không có email",
                                        className = document.getString("className") ?: "",
                                        address = document.getString("address")
                                            ?: "Không có địa chỉ"
                                    )
                                    contactList.add(ContactItem.StudentItem(student))
                                }
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }

                adapter.updateContacts(contactList)
                emptyText.visibility = if (contactList.isEmpty()) View.VISIBLE else View.GONE
                progressBar.visibility = View.GONE
            }
            .addOnFailureListener { e ->
                emptyText.text = "Lỗi khi tải danh bạ: ${e.localizedMessage}"
                emptyText.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
    }
}
