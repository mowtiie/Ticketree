package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityTicketSelectBinding;

public class TicketSelectActivity extends AppCompatActivity {

    ActivityTicketSelectBinding binding;
    int event;
    int type = 0;
    int qty = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
        binding.ddType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows));
        binding.ddType.setText(rows[0], false);
        binding.ddType.setOnItemClickListener((parent, view, position, id) -> {
            type = position;
            qty = Data.TICKET_MIN_QTY[type];
            update();
        });

        binding.btnMinus.setOnClickListener(v -> {
            if (qty > Data.TICKET_MIN_QTY[type]) qty--;
            else Snackbar.make(binding.getRoot(), "Minimum " + qty + " ticket(s) for this type", Snackbar.LENGTH_SHORT).show();
            update();
        });
        binding.btnPlus.setOnClickListener(v -> {
            int max = Math.min(6, Data.TICKET_REMAINING[type]);
            if (qty < max) qty++;
            else Snackbar.make(binding.getRoot(), "Maximum " + max + " tickets", Snackbar.LENGTH_SHORT).show();
            update();
        });

        binding.btnContinue.setOnClickListener(v -> {
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

        qty = Data.TICKET_MIN_QTY[type];
        update();
    }

    void update() {
        binding.tvQty.setText(String.valueOf(qty));
        binding.tvRule.setText(Data.TICKET_MIN_QTY[type] > 1
                ? Data.TICKET_TYPES[type] + " tickets need at least " + Data.TICKET_MIN_QTY[type] + " seats. Maximum 6 per order."
                : "Maximum 6 tickets per order.");
        binding.tvTotal.setText("Total: " + Data.money(Data.TICKET_PRICES[type] * qty));
    }
}
