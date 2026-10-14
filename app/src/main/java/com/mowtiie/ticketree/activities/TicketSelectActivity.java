package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityTicketSelectBinding;
import com.mowtiie.ticketree.databinding.ItemTicketTypeBinding;

public class TicketSelectActivity extends AppCompatActivity {

    ActivityTicketSelectBinding binding;
    MaterialCardView[] cards;
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

        binding.steps.tvStepLabel.setText("Step 1 of 3 - Tickets");
        binding.steps.stepBar.setProgress(1);

        event = getIntent().getIntExtra("event", 0);
        binding.tvEvent.setText(Data.EVENT_NAMES[event]);

        cards = new MaterialCardView[Data.TICKET_TYPES.length];
        for (int t = 0; t < cards.length; t++) {
            final int index = t;
            ItemTicketTypeBinding card = ItemTicketTypeBinding.inflate(getLayoutInflater(), binding.llTypes, false);
            card.tvName.setText(Data.TICKET_TYPES[t]);
            card.tvPrice.setText(Data.money(Data.TICKET_PRICES[t]));
            boolean soldOut = Data.TICKET_REMAINING[t] < Data.TICKET_MIN_QTY[t];
            card.tvStock.setText(soldOut ? "Sold out"
                    : Data.TICKET_REMAINING[t] + " left"
                    + (Data.TICKET_MIN_QTY[t] > 1 ? " | minimum " + Data.TICKET_MIN_QTY[t] + " per order" : ""));
            card.getRoot().setEnabled(!soldOut);
            card.getRoot().setAlpha(soldOut ? 0.5f : 1f);
            card.getRoot().setContentDescription(Data.TICKET_TYPES[t] + ", " + Data.money(Data.TICKET_PRICES[t])
                    + (soldOut ? ", sold out" : ""));
            card.getRoot().setOnClickListener(v -> {
                type = index;
                qty = Data.TICKET_MIN_QTY[type];
                update();
            });
            cards[t] = card.getRoot();
            binding.llTypes.addView(card.getRoot());
        }

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

        for (int t = 0; t < cards.length; t++) {
            if (Data.TICKET_REMAINING[t] >= Data.TICKET_MIN_QTY[t]) {
                type = t;
                break;
            }
        }
        qty = Data.TICKET_MIN_QTY[type];
        update();
    }

    void update() {
        for (int t = 0; t < cards.length; t++) cards[t].setChecked(t == type);
        binding.tvQty.setText(String.valueOf(qty));
        binding.tvRule.setText(Data.TICKET_MIN_QTY[type] > 1
                ? Data.TICKET_TYPES[type] + " tickets need at least " + Data.TICKET_MIN_QTY[type] + " seats. Maximum 6 per order."
                : "Maximum 6 tickets per order.");
        binding.tvTotal.setText(Data.money(Data.TICKET_PRICES[type] * qty));
    }
}
