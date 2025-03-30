package com.project.tlucontact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button btnUnitContact;
    private Button btnStaffContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnStaffContact = (Button) findViewById(R.id.btn_staff_contact);
        btnUnitContact = (Button) findViewById(R.id.btn_unit_contact);

        btnUnitContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listUnitIntent = new Intent(MainActivity.this, ListUnitActivity.class);
                startActivity(listUnitIntent);
            }
        });

        btnStaffContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listStaffIntent = new Intent(MainActivity.this, ListStaffActivity.class);
                startActivity(listStaffIntent);
            }
        });
    }
}