package com.dev.tlucontactkotlin.ui.contact

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat.Type
import com.dev.tlucontactkotlin.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import java.util.*
import kotlin.concurrent.thread

class AddContactActivity : AppCompatActivity() {

    private lateinit var contactType: String
    private val db = FirebaseFirestore.getInstance()
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var previewImage: ImageView
    private lateinit var addPlaceholderImage: ImageView
    private lateinit var imageContainer: LinearLayout

    // Replace with your Cloudinary
    private val CLOUD_NAME = "dqcsoimhe"
    private val UPLOAD_PRESET = "upload_avatar_tlu"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout)) { v, insets ->
            val systemBars = insets.getInsets(Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.apply {
            title = when (intent.getStringExtra("CONTACT_TYPE")) {
                "staff" -> "Thêm Cán Bộ"
                "student" -> "Thêm Sinh Viên"
                "unit" -> "Thêm Đơn Vị"
                else -> "Thêm Liên Hệ"
            }
            setDisplayHomeAsUpEnabled(true)
        }

        contactType = intent.getStringExtra("CONTACT_TYPE") ?: return
        val container = findViewById<LinearLayout>(R.id.formContainer)
        progressBar = findViewById(R.id.progressBar)
        when (contactType) {
            "staff" -> setupStaffForm(container)
            "student" -> setupStudentForm(container)
            "unit" -> setupUnitForm(container)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            previewImage.setImageURI(selectedImageUri)
            previewImage.visibility = View.VISIBLE

            if (imageContainer.indexOfChild(addPlaceholderImage) == 0) {
                imageContainer.removeView(addPlaceholderImage)
                imageContainer.addView(addPlaceholderImage)
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun uploadImage(onUploaded: (String) -> Unit) {
        if (selectedImageUri == null) {
            onUploaded("")
            return
        }

        val inputStream: InputStream? = contentResolver.openInputStream(selectedImageUri!!)
        val fileName = getFileName(selectedImageUri!!)
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", fileName, inputStream!!.readBytes().toRequestBody())
            .addFormDataPart("upload_preset", UPLOAD_PRESET)
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/$CLOUD_NAME/image/upload")
            .post(requestBody)
            .build()

        thread {
            try {
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                val url = Regex("\"url\":\"(.*?)\"").find(responseBody!!)?.groupValues?.get(1)
                    ?.replace("\\/", "/")
                runOnUiThread { onUploaded(url ?: "") }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Upload thất bại", Toast.LENGTH_SHORT).show()
                    onUploaded("")
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String {
        var result = "image_${System.currentTimeMillis()}.jpg"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME)) ?: result
            }
        }
        return result
    }

    private fun setupStaffForm(container: LinearLayout) {
        val id = createTextInput("Mã cán bộ")
        val fullName = createTextInput("Họ tên")
        val position = createTextInput("Chức vụ")
        val phone = createTextInput("Số điện thoại")
        val email = createTextInput("Email", "textEmailAddress")
        val address = createTextInput("Địa chỉ")
        val lbAvater = TextView(this).apply {
            text = "Ảnh đại diện"
            setPadding(0, 16, 0, 0)
        }
        setupImageSelector()

        val btnSave = createButton("Lưu")
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        btnSave.setOnClickListener {
            btnSave.isEnabled = false
            progressBar.visibility = View.VISIBLE
            uploadImage { imageUrl ->
                val data = hashMapOf(
                    "id" to id.second.text.toString(),
                    "fullName" to fullName.second.text.toString(),
                    "position" to position.second.text.toString(),
                    "phone" to phone.second.text.toString(),
                    "email" to email.second.text.toString(),
                    "address" to address.second.text.toString(),
                    "photoURL" to imageUrl
                )

                db.collection("staffs").add(data)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Lỗi: ${it.localizedMessage}", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnCompleteListener {
                        progressBar.visibility = View.GONE
                        btnSave.isEnabled = true
                    }
            }
        }

        container.apply {
            addView(id.first)
            addView(fullName.first)
            addView(position.first)
            addView(phone.first)
            addView(email.first)
            addView(address.first)
            addView(lbAvater)
            addView(imageContainer)
            addView(btnSave)
        }
    }

    private fun setupStudentForm(container: LinearLayout) {
        val id = createTextInput("Mã sinh viên")
        val fullName = createTextInput("Họ tên")
        val className = createTextInput("Lớp")
        val phone = createTextInput("Số điện thoại")
        val email = createTextInput("Email", "textEmailAddress")
        val address = createTextInput("Địa chỉ")
        val lbAvater = TextView(this).apply {
            text = "Ảnh đại diện"
            setPadding(0, 16, 0, 0)
        }
        setupImageSelector()

        val btnSave = createButton("Lưu")
        btnSave.setOnClickListener {
            btnSave.isEnabled = false
            progressBar.visibility = View.VISIBLE
            uploadImage { imageUrl ->
                val data = hashMapOf(
                    "id" to id.second.text.toString(),
                    "fullName" to fullName.second.text.toString(),
                    "className" to className.second.text.toString(),
                    "phone" to phone.second.text.toString(),
                    "email" to email.second.text.toString(),
                    "address" to address.second.text.toString(),
                    "photoURL" to imageUrl
                )

                db.collection("students").add(data).addOnSuccessListener {
                    Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Lỗi: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }.addOnCompleteListener {
                    progressBar.visibility = View.GONE
                    btnSave.isEnabled = true
                }
            }
        }

        container.apply {
            addView(id.first)
            addView(fullName.first)
            addView(className.first)
            addView(phone.first)
            addView(email.first)
            addView(address.first)
            addView(lbAvater)
            addView(imageContainer)
            addView(btnSave)
        }
    }

    private fun setupUnitForm(container: LinearLayout) {
        val id = createTextInput("Mã đơn vị")
        val name = createTextInput("Tên đơn vị")
        val phone = createTextInput("Số điện thoại")
        val address = createTextInput("Địa chỉ")
        val email = createTextInput("Email", "textEmailAddress")
        val lbAvater = TextView(this).apply {
            text = "Ảnh đại diện"
            setPadding(0, 16, 0, 0)
        }
        setupImageSelector()

        val btnSave = createButton("Lưu")
        btnSave.setOnClickListener {
            btnSave.isEnabled = false
            progressBar.visibility = View.VISIBLE
            uploadImage { imageUrl ->
                val data = hashMapOf(
                    "id" to id.second.text.toString(),
                    "name" to name.second.text.toString(),
                    "phone" to phone.second.text.toString(),
                    "address" to address.second.text.toString(),
                    "email" to email.second.text.toString(),
                    "logoURL" to imageUrl
                )

                db.collection("units").add(data).addOnSuccessListener {
                    Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Lỗi: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }.addOnCompleteListener {
                    progressBar.visibility = View.GONE
                    btnSave.isEnabled = true
                }
            }
        }

        container.apply {
            addView(id.first)
            addView(name.first)
            addView(phone.first)
            addView(address.first)
            addView(email.first)
            addView(lbAvater)
            addView(imageContainer)
            addView(btnSave)
        }
    }

    private fun setupImageSelector() {
        imageContainer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 16
            }
        }

        previewImage = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(300, 300)
            visibility = View.GONE
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        addPlaceholderImage = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(300, 300).apply {
                leftMargin = 16
            }
            setImageResource(R.drawable.placeholder_add)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            setOnClickListener {
                openImagePicker()
            }
        }

        imageContainer.addView(previewImage)
        imageContainer.addView(addPlaceholderImage)
    }

    private fun createTextInput(
        hint: String,
        inputType: String = "text"
    ): Pair<TextInputLayout, TextInputEditText> {
        val layout = TextInputLayout(this)
        val editText = TextInputEditText(this).apply {
            this.hint = hint
            when (inputType) {
                "textEmailAddress" -> this.inputType =
                    android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

                else -> this.inputType = android.text.InputType.TYPE_CLASS_TEXT
            }
        }
        layout.addView(editText)
        return layout to editText
    }

    private fun createButton(text: String): Button {
        return Button(this).apply {
            this.text = text
        }
    }
}
