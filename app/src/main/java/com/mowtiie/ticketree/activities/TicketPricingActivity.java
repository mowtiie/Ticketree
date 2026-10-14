package com.mowtiie.ticketree.activities;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mowtiie.ticketree.adapters.GenericAdapter;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityTicketPricingBinding;

import java.util.ArrayList;

public class TicketPricingActivity extends AppCompatActivity {

    GenericAdapter<Integer> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityTicketPricingBinding binding = ActivityTicketPricingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        ArrayList<Integer> types = new ArrayList<>();
        for (int t = 0; t < Data.TICKET_TYPES.length; t++) types.add(t);

        adapter = new GenericAdapter<>(types, (row, type, position) -> {
            row.tvTitle.setText(Data.TICKET_TYPES[type]);
            row.tvSubtitle.setText(Data.money(Data.TICKET_PRICES[type]) + " | " + Data.TICKET_REMAINING[type] + " left");
            row.btnAction.setVisibility(View.VISIBLE);
            row.btnAction.setText("Add 10 seats");
            row.btnAction.setOnClickListener(v -> {
                Data.TICKET_REMAINING[type] += 10;
                adapter.notifyDataSetChanged();
            });
            row.getRoot().setOnClickListener(v -> {
                EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setText(String.valueOf(Data.TICKET_PRICES[type]));
                new AlertDialog.Builder(this)
                        .setTitle("New price for " + Data.TICKET_TYPES[type])
                        .setView(input)
                        .setPositiveButton("Save", (d, w) -> {
                            String text = input.getText().toString().trim();
                            if (text.isEmpty()) {
                                Snackbar.make(binding.getRoot(), "Enter a price", Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                            Data.TICKET_PRICES[type] = Integer.parseInt(text);
                            adapter.notifyDataSetChanged();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        });
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(adapter);
    }
}
