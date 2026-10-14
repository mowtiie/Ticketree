package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.R;
import com.mowtiie.ticketree.data.Data;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;

import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import com.mowtiie.ticketree.databinding.ActivitySeatSelectBinding;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.MaterialColors;

public class SeatSelectActivity extends AppCompatActivity {

    ActivitySeatSelectBinding binding;
    CountDownTimer timer;
    boolean forward = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySeatSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.steps.tvStepLabel.setText("Step 2 of 3 - Seats");
        binding.steps.stepBar.setProgress(2);

        Data.SEL_BOX = 0;

        binding.chipFree.setChipBackgroundColor(ColorStateList.valueOf(colorOf(0, false)));
        binding.chipFree.setTextColor(colorOf(0, true));
        binding.chipSelected.setChipBackgroundColor(ColorStateList.valueOf(colorOf(1, false)));
        binding.chipSelected.setTextColor(colorOf(1, true));
        binding.chipTaken.setChipBackgroundColor(ColorStateList.valueOf(colorOf(2, false)));
        binding.chipTaken.setTextColor(colorOf(2, true));

        binding.tvInfo.setText("Pick " + Data.SEL_QTY + " seat(s) in one box. Seats are held for "
                + Data.HOLD_SECONDS + " seconds once you pick one.");

        binding.ddBox.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Data.BOX_NAMES));
        binding.ddBox.setText(Data.BOX_NAMES[0], false);
        binding.ddBox.setOnItemClickListener((parent, view, position, id) -> {
            if (position != Data.SEL_BOX) {
                Data.releaseHold();
                stopTimer();
                Data.SEL_BOX = position;
            }
            buildGrid();
        });
        buildGrid();

        binding.btnContinue.setOnClickListener(v -> {
            if (Data.SEL_SEATS.size() != Data.SEL_QTY) {
                Snackbar.make(binding.getRoot(), "Select " + Data.SEL_QTY + " seat(s)", Snackbar.LENGTH_SHORT).show();
                return;
            }
            forward = true;
            startActivity(new Intent(this, CheckoutActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        forward = false;
    }

    void updateSelection() {
        binding.tvSelection.setText(Data.SEL_SEATS.size() + " of " + Data.SEL_QTY + " seats - "
                + Data.money(Data.TICKET_PRICES[Data.SEL_TYPE] * Data.SEL_QTY));
    }

    void buildGrid() {
        binding.gridSeats.removeAllViews();
        for (int s = 0; s < 20; s++) {
            final int seat = s;
            MaterialButton b = new MaterialButton(this);
            b.setText(String.valueOf(s + 1));
            b.setInsetTop(0);
            b.setInsetBottom(0);
            b.setPadding(0, 0, 0, 0);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
            lp.width = 0;
            lp.setMargins(4, 4, 4, 4);
            b.setLayoutParams(lp);
            paint(b, seat);
            b.setOnClickListener(v -> {
                boolean[] taken = Data.SEAT_TAKEN[Data.SEL_EVENT][Data.SEL_BOX];
                if (Data.SEL_SEATS.contains(seat)) {
                    Data.SEL_SEATS.remove(Integer.valueOf(seat));
                    taken[seat] = false;
                    if (Data.SEL_SEATS.isEmpty()) {
                        stopTimer();
                        Data.HOLD_END = 0;
                    }
                } else if (taken[seat]) {
                    return;
                } else if (Data.SEL_SEATS.size() >= Data.SEL_QTY) {
                    Snackbar.make(binding.getRoot(), "You only need " + Data.SEL_QTY + " seat(s)", Snackbar.LENGTH_SHORT).show();
                    return;
                } else {
                    Data.SEL_SEATS.add(seat);
                    taken[seat] = true;
                    if (Data.HOLD_END == 0) startHold();
                }
                paint(b, seat);
                updateSelection();
            });
            binding.gridSeats.addView(b);
        }
        updateSelection();
    }

    int colorOf(int state, boolean text) {
        View v = binding.getRoot();
        if (state == 1) {
            return text ? MaterialColors.getColor(v, com.google.android.material.R.attr.colorSurface)
                    : getColor(R.color.success);
        } else if (state == 2) {
            return MaterialColors.getColor(v, text ? com.google.android.material.R.attr.colorOutline
                    : com.google.android.material.R.attr.colorSurfaceVariant);
        }
        return MaterialColors.getColor(v, text ? com.google.android.material.R.attr.colorOnSecondaryContainer
                : com.google.android.material.R.attr.colorSecondaryContainer);
    }

    void paint(MaterialButton b, int seat) {
        boolean selected = Data.SEL_SEATS.contains(seat);
        boolean taken = Data.SEAT_TAKEN[Data.SEL_EVENT][Data.SEL_BOX][seat];
        int state = selected ? 1 : (taken ? 2 : 0);
        b.setBackgroundTintList(ColorStateList.valueOf(colorOf(state, false)));
        b.setTextColor(colorOf(state, true));
        b.setContentDescription("Seat " + (seat + 1) + ", " + (selected ? "selected" : taken ? "taken" : "free"));
    }

    void startHold() {
        Data.HOLD_END = System.currentTimeMillis() + Data.HOLD_SECONDS * 1000L;
        stopTimer();
        timer = new CountDownTimer(Data.HOLD_SECONDS * 1000L, 1000) {
            @Override
            public void onTick(long ms) {
                Data.styleTimer(binding.tvTimer, ms);
            }

            @Override
            public void onFinish() {
                if (Data.HOLD_END == 0) return;
                int event = Data.SEL_EVENT;
                Data.releaseHold();
                Toast.makeText(SeatSelectActivity.this, "Hold expired. Seats released.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SeatSelectActivity.this, EventDetailActivity.class);
                intent.putExtra("event", event);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }.start();
    }

    void stopTimer() {
        if (timer != null) timer.cancel();
        timer = null;
        binding.tvTimer.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
        if (isFinishing() && !forward && Data.HOLD_END != 0) Data.releaseHold();
    }
}
