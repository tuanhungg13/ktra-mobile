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
import com.project.tlucontact.UnitDetailsActivity;
import com.project.tlucontact.model.Unit;

import java.util.List;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.UnitViewHolder> {
    private List<Unit> listUnits;
    private Context context;

    public UnitAdapter(List<Unit> listUnits, Context context) {
        this.listUnits = listUnits;
        this.context = context;
    }

    @NonNull
    @Override
    public UnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unit, parent, false);
        return new UnitViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UnitViewHolder holder, int position) {
        holder.bind(listUnits.get(position));

        // Bắt sự kiện khi click vào một đơn vị
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UnitDetailsActivity.class);
            intent.putExtra("unit", listUnits.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listUnits.size();
    }

    static class UnitViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgLogo;
        private TextView txtName;
        private TextView txtPhone;

        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.img_logo_unit);
            txtName = itemView.findViewById(R.id.txt_unit_name);
            txtPhone = itemView.findViewById(R.id.txt_unit_phone);
        }

        public void bind(Unit unit) {
            imgLogo.setImageResource(unit.getLogoUnit());
            txtName.setText(unit.getName());
            txtPhone.setText(unit.getPhone());
        }
    }
}
