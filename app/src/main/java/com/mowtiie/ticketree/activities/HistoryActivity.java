package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mowtiie.ticketree.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHistoryBinding binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        if (Data.ORDERS.isEmpty()) binding.tvEmpty.setVisibility(View.VISIBLE);

        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(new GenericAdapter<String>(Data.ORDERS, (row, order, position) -> {
            row.tvTitle.setText(order);
            row.tvSubtitle.setText("Tap to view receipt");
            row.getRoot().setOnClickListener(v ->
                    new AlertDialog.Builder(this)
                            .setMessage(Data.RECEIPTS.get(position))
                            .setPositiveButton("Close", null)
                            .show());
        }));
    }
}
