package com.project.tlucontact;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tlucontact.adapter.StaffAdapter;
import com.project.tlucontact.adapter.UnitAdapter;
import com.project.tlucontact.model.Staff;

import java.util.ArrayList;
import java.util.List;

public class ListStaffActivity extends AppCompatActivity {
    private RecyclerView rcvStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_staff);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<Staff> staffList = new ArrayList<>();

        staffList.add(new Staff("STF001", "Nguyễn Văn A", "Trưởng phòng", "0123 456 111", "a@tlu.edu.vn", R.drawable.logo, "Hà Nội, Việt Nam"));
        staffList.add(new Staff("STF002", "Trần Thị B", "Nhân viên", "0123 456 222", "b@tlu.edu.vn", R.drawable.logo, "Hà Nội, Việt Nam"));

        staffList.add(new Staff("STF003", "Lê Công C", "Giảng viên", "0987 654 333", "c@tlu.edu.vn", R.drawable.logo, "TP. Hồ Chí Minh, Việt Nam"));
        staffList.add(new Staff("STF004", "Phạm Quang D", "Trưởng khoa", "0987 654 444", "d@tlu.edu.vn", R.drawable.logo, "TP. Hồ Chí Minh, Việt Nam"));

        staffList.add(new Staff("STF005", "Hoàng Minh E", "Giám đốc", "024 1234 5555", "e@tlu.edu.vn", R.drawable.logo, "Đà Nẵng, Việt Nam"));
        staffList.add(new Staff("STF006", "Đặng Thu F", "Nhân viên", "024 1234 6666", "f@tlu.edu.vn", R.drawable.logo, "Đà Nẵng, Việt Nam"));

        staffList.add(new Staff("STF007", "Võ Quốc G", "Trưởng phòng", "036 789 7777", "g@tlu.edu.vn", R.drawable.logo, "Cần Thơ, Việt Nam"));
        staffList.add(new Staff("STF008", "Bùi Thanh H", "Kế toán viên", "036 789 8888", "h@tlu.edu.vn", R.drawable.logo, "Cần Thơ, Việt Nam"));

        staffList.add(new Staff("STF009", "Dương Hữu I", "Giảng viên", "085 555 9999", "i@tlu.edu.vn", R.drawable.logo, "Hải Phòng, Việt Nam"));
        staffList.add(new Staff("STF010", "Cao Văn K", "Trưởng khoa", "085 555 0000", "k@tlu.edu.vn", R.drawable.logo, "Hải Phòng, Việt Nam"));

        staffList.add(new Staff("STF011", "Tô Văn L", "Nhân viên", "077 888 1111", "l@tlu.edu.vn", R.drawable.logo, "Huế, Việt Nam"));
        staffList.add(new Staff("STF012", "Phan Quốc M", "Giám đốc", "077 888 2222", "m@tlu.edu.vn", R.drawable.logo, "Huế, Việt Nam"));

        staffList.add(new Staff("STF013", "Hồ Chí N", "Nhân viên", "0933 222 333", "n@tlu.edu.vn", R.drawable.logo, "Nha Trang, Việt Nam"));
        staffList.add(new Staff("STF014", "Trịnh Văn O", "Trưởng phòng", "0933 222 444", "o@tlu.edu.vn", R.drawable.logo, "Nha Trang, Việt Nam"));
        rcvStaff = (RecyclerView) findViewById(R.id.rcv_staff);
        StaffAdapter staffAdapter = new StaffAdapter(staffList, this);
        rcvStaff.setAdapter(staffAdapter);
    }
}