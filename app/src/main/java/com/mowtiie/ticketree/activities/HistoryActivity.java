package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityHistoryBinding;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ActivityHistoryBinding binding;
    GenericAdapter<Integer> adapter;
    ArrayList<Integer> indexes = new ArrayList<>();
    boolean showReceipts = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.empty.tvEmoji.setText("📜");
        binding.empty.btnEmpty.setVisibility(View.VISIBLE);
        binding.empty.btnEmpty.setText("Browse events");
        binding.empty.btnEmpty.setOnClickListener(v ->
                startActivity(new Intent(this, EventListActivity.class)));

        adapter = new GenericAdapter<>(indexes, (row, index, position) -> {
            if (showReceipts) {
                String receipt = Data.RECEIPTS.get(index);
                String[] lines = receipt.split("\n");
                String total = "";
                for (String line : lines) {
                    if (line.startsWith("TOTAL")) total = line;
                }
                row.tvTitle.setText(lines[0]);
                row.tvSubtitle.setText(lines[1] + "\n" + total);
            } else {
                row.tvTitle.setText(Data.ORDERS.get(index));
                row.tvSubtitle.setText("Tap to view receipt");
            }
            row.getRoot().setOnClickListener(v ->
                    new AlertDialog.Builder(this)
                            .setMessage(Data.RECEIPTS.get(index))
                            .setPositiveButton("Close", null)
                            .show());
        });
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(adapter);

        binding.tabHistory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showReceipts = tab.getPosition() == 1;
                refresh();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        refresh();
    }

    void refresh() {
        indexes.clear();
        for (int i = 0; i < Data.ORDERS.size(); i++) indexes.add(i);
        adapter.notifyDataSetChanged();
        binding.empty.tvMessage.setText(showReceipts ? "No payment receipts yet" : "No orders yet");
        binding.empty.getRoot().setVisibility(indexes.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
