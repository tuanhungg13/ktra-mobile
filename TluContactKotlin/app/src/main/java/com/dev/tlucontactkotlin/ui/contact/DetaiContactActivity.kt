package com.dev.tlucontactkotlin.ui.contact

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat.Type
import com.bumptech.glide.Glide
import com.dev.tlucontactkotlin.R
import com.dev.tlucontactkotlin.models.Staff
import com.dev.tlucontactkotlin.models.Student
import com.dev.tlucontactkotlin.models.UnitContact

class DetailActivity : AppCompatActivity() {

    private lateinit var txtName: TextView
    private lateinit var imgLogo: ImageView
    private lateinit var phoneNumber: String
    private lateinit var email: String

    private val REQUEST_CALL_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView)) { v, insets ->
            val systemBars = insets.getInsets(Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtName = findViewById(R.id.txt_name)
        imgLogo = findViewById(R.id.img_avatar)

        val rowId = findViewById<View>(R.id.row_id)
        val rowPosition = findViewById<View>(R.id.row_position)
        val rowPhone = findViewById<View>(R.id.row_phone)
        val rowEmail = findViewById<View>(R.id.row_email)
        val rowAddress = findViewById<View>(R.id.row_address)

        rowId.findViewById<TextView>(R.id.txt_label).text = "Mã:"
        rowPhone.findViewById<TextView>(R.id.txt_label).text = "SĐT:"
        rowEmail.findViewById<TextView>(R.id.txt_label).text = "Email:"
        rowAddress.findViewById<TextView>(R.id.txt_label).text = "Địa chỉ:"

        val type = intent.getStringExtra("type")
        when (type) {
            "staff" -> {
                val staff = intent.getSerializableExtra("data") as? Staff
                staff?.let {
                    rowPosition.findViewById<TextView>(R.id.txt_label).text = "Chức vụ:"
                    txtName.text = it.fullName
                    rowId.findViewById<TextView>(R.id.txt_value).text = it.id
                    rowPosition.findViewById<TextView>(R.id.txt_value).text = it.position
                    rowPhone.findViewById<TextView>(R.id.txt_value).text = it.phone
                    rowEmail.findViewById<TextView>(R.id.txt_value).text = it.email
                    rowAddress.findViewById<TextView>(R.id.txt_value).text = it.address
                    phoneNumber = it.phone
                    email = it.email

                    Glide.with(this)
                        .load(it.photoURL)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(imgLogo)
                }
            }

            "student" -> {
                val student = intent.getSerializableExtra("data") as? Student
                student?.let {
                    rowPosition.findViewById<TextView>(R.id.txt_label).text = "Lớp:"
                    txtName.text = it.fullName
                    rowId.findViewById<TextView>(R.id.txt_value).text = it.id
                    rowPosition.findViewById<TextView>(R.id.txt_value).text = it.className
                    rowPhone.findViewById<TextView>(R.id.txt_value).text = it.phone
                    rowEmail.findViewById<TextView>(R.id.txt_value).text = it.email
                    rowAddress.findViewById<TextView>(R.id.txt_value).text = it.address
                    phoneNumber = it.phone
                    email = it.email

                    Glide.with(this)
                        .load(it.photoURL)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(imgLogo)
                }
            }

            "unit" -> {
                val unit = intent.getSerializableExtra("data") as? UnitContact
                unit?.let {
                    txtName.text = it.name
                    rowId.findViewById<TextView>(R.id.txt_value).text = it.id
                    rowPosition.visibility = View.GONE
                    rowPhone.findViewById<TextView>(R.id.txt_value).text = it.phone
                    rowEmail.findViewById<TextView>(R.id.txt_value).text = it.email
                    rowAddress.findViewById<TextView>(R.id.txt_value).text = it.address
                    imgLogo.setImageResource(R.drawable.logo)

                    phoneNumber = it.phone
                    email = it.email
                }
            }
        }

        // Xử lý click
        val imgPhone = findViewById<ImageView>(R.id.img_phone)
        val imgMessage = findViewById<ImageView>(R.id.img_message)
        val imgMail = findViewById<ImageView>(R.id.img_mail)

        imgPhone.setOnClickListener {
            makePhoneCall()
        }

        imgMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("sms:$phoneNumber")
            startActivity(intent)
        }

        imgMail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$email")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Liên hệ từ ứng dụng")
            startActivity(Intent.createChooser(intent, "Gửi email với..."))
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun makePhoneCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION
            )
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            }
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
