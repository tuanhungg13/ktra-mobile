package com.project.tlucontact.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tlucontact.R;
import com.project.tlucontact.StaffDetailsActivity;
import com.project.tlucontact.UnitDetailsActivity;
import com.project.tlucontact.model.Staff;
import com.project.tlucontact.model.Unit;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {
    private List<Staff> listStaff;
    private Context context;

    public StaffAdapter(List<Staff> listStaff, Context context) {
        this.listStaff = listStaff;
        this.context = context;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff, parent, false);
        return new StaffViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        holder.bind(listStaff.get(position));

        // Bắt sự kiện khi click vào một đơn vị
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StaffDetailsActivity.class);
            intent.putExtra("staff", listStaff.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listStaff.size();
    }

    static class StaffViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar;
        private TextView txtName;
        private TextView txtPhone;
        private TextView txtPosition;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_logo_staff);
            txtName = itemView.findViewById(R.id.txt_staff_name);
            txtPhone = itemView.findViewById(R.id.txt_staff_phone);
            txtPosition = itemView.findViewById(R.id.txt_staff_position);
        }

        public void bind(Staff staff) {
            imgAvatar.setImageResource(staff.getAvatar());
            txtName.setText(staff.getFullName());
            txtPhone.setText(staff.getPhone());
            txtPosition.setText(staff.getPosition());
        }
    }


}
