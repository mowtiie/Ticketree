package com.mowtiie.ticketree.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityTicketPricingBinding;
import com.mowtiie.ticketree.databinding.DialogNumberBinding;

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
            row.tvSubtitle.setText(Data.money(Data.TICKET_PRICES[type]) + " | " + Data.TICKET_REMAINING[type]
                    + " in stock\nTap the card to change the price");
            row.btnAction.setVisibility(View.VISIBLE);
            row.btnAction.setText("Edit stock");
            row.btnAction.setOnClickListener(v -> askNumber("Stock for " + Data.TICKET_TYPES[type],
                    "Number of tickets available", Data.TICKET_REMAINING[type], 0, value -> {
                        Data.TICKET_REMAINING[type] = value;
                        adapter.notifyDataSetChanged();
                    }));
            row.getRoot().setOnClickListener(v -> askNumber("Price for " + Data.TICKET_TYPES[type],
                    "Price in pesos", Data.TICKET_PRICES[type], 1, value -> {
                        Data.TICKET_PRICES[type] = value;
                        adapter.notifyDataSetChanged();
                    }));
        });
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(adapter);
    }

    interface OnNumber {
        void accept(int value);
    }

    void askNumber(String title, String hint, int current, int min, OnNumber onSave) {
        DialogNumberBinding dialogBinding = DialogNumberBinding.inflate(getLayoutInflater());
        dialogBinding.tilNumber.setHint(hint);
        dialogBinding.etNumber.setText(String.valueOf(current));

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String text = dialogBinding.etNumber.getText().toString().trim();
            if (text.isEmpty() || Integer.parseInt(text) < min) {
                dialogBinding.tilNumber.setError(min > 0 ? "Enter a number above 0" : "Enter 0 or more");
                return;
            }
            onSave.accept(Integer.parseInt(text));
            dialog.dismiss();
        });
    }
}
