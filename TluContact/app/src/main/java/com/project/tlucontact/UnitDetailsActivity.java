package com.project.tlucontact;

import android.Manifest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.tlucontact.model.Unit;

public class UnitDetailsActivity extends AppCompatActivity {
    private static final int REQUEST_CALL_PERMISSION = 1;
    private TextView txtUnitName, txtUnitPhone, txtUnitEmail, txtUnitAddress, txtUid;
    private ImageView imgLogo, imgPhone;

    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.unit_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgLogo = findViewById(R.id.img_logo_unit);
        txtUnitName = findViewById(R.id.txt_unit_name);
        txtUnitPhone = findViewById(R.id.txt_unit_phone);
        txtUnitEmail = findViewById(R.id.txt_unit_email);
        txtUnitAddress = findViewById(R.id.txt_unit_address);
        txtUid = findViewById(R.id.txt_unit_uid);
        imgPhone = findViewById(R.id.img_phone);

        // Nhận dữ liệu từ Intent
        Unit unit = (Unit) getIntent().getSerializableExtra("unit");
        if (unit != null) {
            imgLogo.setImageResource(unit.getLogoUnit());
            txtUnitName.setText(unit.getName());
            txtUnitPhone.setText(unit.getPhone());
            txtUnitEmail.setText(unit.getEmail());
            txtUnitAddress.setText(unit.getAddress());
        }
        imgPhone.setOnClickListener(v -> makePhoneCall());

    }

    private void makePhoneCall() {
        String phoneNumber = txtUnitPhone.getText().toString();

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        // Kiểm tra quyền CALL_PHONE trước khi thực hiện cuộc gọi
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            // Nếu chưa có quyền, yêu cầu quyền từ người dùng
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }
    }

    // Xử lý kết quả khi người dùng cấp quyền hoặc từ chối
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(); // Nếu quyền đã được cấp, gọi lại hàm gọi điện
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để thực hiện cuộc gọi!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}