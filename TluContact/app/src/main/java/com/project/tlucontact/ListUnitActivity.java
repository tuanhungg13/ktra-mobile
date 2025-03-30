package com.project.tlucontact;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tlucontact.adapter.UnitAdapter;
import com.project.tlucontact.model.Unit;

import java.util.ArrayList;
import java.util.List;

public class ListUnitActivity extends AppCompatActivity {
    private RecyclerView rcvUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_unit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<Unit> units = new ArrayList<>();

        units.add(new Unit("UID001", "Phòng Hành Chính", "Tầng 1, Tòa Nhà A", R.drawable.logo, "0123 456 789", "hanhchinh@tlu.edu.vn", "Phòng"));
        units.add(new Unit("UID002", "Khoa Công Nghệ Thông Tin", "Tầng 2, Tòa Nhà B", R.drawable.logo, "0987 654 321", "cntt@tlu.edu.vn", "Khoa"));
        units.add(new Unit("UID003", "Trung Tâm Ngoại Ngữ", "Tầng 3, Tòa Nhà C", R.drawable.logo, "024 1234 5678", "ngoaingu@tlu.edu.vn", "Trung tâm"));
        units.add(new Unit("UID004", "Phòng Kế Toán", "Tầng 4, Tòa Nhà D", R.drawable.logo, "036 789 1234", "ketoan@tlu.edu.vn", "Phòng"));
        units.add(new Unit("UID005", "Khoa Điện - Điện Tử", "Tầng 5, Tòa Nhà E", R.drawable.logo, "085 555 6666", "dien@tlu.edu.vn", "Khoa"));
        units.add(new Unit("UID006", "Trung Tâm Công Nghệ Phần Mềm", "Tầng 6, Tòa Nhà F", R.drawable.logo, "077 888 9999", "software@tlu.edu.vn", "Trung tâm"));
        units.add(new Unit("UID007", "Phòng Hợp Tác Quốc Tế", "Tầng 7, Tòa Nhà G", R.drawable.logo, "0933 222 111", "htqt@tlu.edu.vn", "Phòng"));

        rcvUnits = (RecyclerView) findViewById(R.id.rcv_units);
        UnitAdapter unitAdapter = new UnitAdapter(units, this);
        rcvUnits.setAdapter(unitAdapter);
    }
}