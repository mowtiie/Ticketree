package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import com.mowtiie.ticketree.databinding.ActivityTicketSelectBinding;

public class TicketSelectActivity extends AppCompatActivity {

    ActivityTicketSelectBinding binding;
    int event;
    int qty = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        event = getIntent().getIntExtra("event", 0);
        binding.tvEvent.setText(Data.EVENT_NAMES[event]);

        String[] rows = new String[Data.TICKET_TYPES.length];
        for (int t = 0; t < rows.length; t++) {
            rows[t] = Data.TICKET_TYPES[t] + " - " + Data.money(Data.TICKET_PRICES[t])
                    + " (" + Data.TICKET_REMAINING[t] + " left)";
        }
        binding.spType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rows));
        binding.spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qty = Data.TICKET_MIN_QTY[position];
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.btnMinus.setOnClickListener(v -> {
            if (qty > Data.TICKET_MIN_QTY[binding.spType.getSelectedItemPosition()]) qty--;
            else Snackbar.make(binding.getRoot(), "Minimum " + qty + " ticket(s) for this type", Snackbar.LENGTH_SHORT).show();
            update();
        });
        binding.btnPlus.setOnClickListener(v -> {
            int max = Math.min(6, Data.TICKET_REMAINING[binding.spType.getSelectedItemPosition()]);
            if (qty < max) qty++;
            else Snackbar.make(binding.getRoot(), "Maximum " + max + " tickets", Snackbar.LENGTH_SHORT).show();
            update();
        });

        binding.btnContinue.setOnClickListener(v -> {
            int type = binding.spType.getSelectedItemPosition();
            if (Data.TICKET_REMAINING[type] < qty) {
                Snackbar.make(binding.getRoot(), "Sold out", Snackbar.LENGTH_SHORT).show();
                return;
            }
            Data.releaseHold();
            Data.SEL_EVENT = event;
            Data.SEL_TYPE = type;
            Data.SEL_QTY = qty;
            startActivity(new Intent(this, SeatSelectActivity.class));
        });
    }

    void update() {
        binding.tvQty.setText(String.valueOf(qty));
        int type = binding.spType.getSelectedItemPosition();
        binding.tvRule.setText(Data.TICKET_MIN_QTY[type] > 1
                ? Data.TICKET_TYPES[type] + " tickets need at least " + Data.TICKET_MIN_QTY[type] + " seats. Maximum 6 per order."
                : "Maximum 6 tickets per order.");
        binding.tvTotal.setText("Total: " + Data.money(Data.TICKET_PRICES[binding.spType.getSelectedItemPosition()] * qty));
    }
}
