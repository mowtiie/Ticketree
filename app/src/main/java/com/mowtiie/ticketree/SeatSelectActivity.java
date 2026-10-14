package com.mowtiie.ticketree;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class SeatSelectActivity extends AppCompatActivity {

    Spinner spBox;
    GridLayout grid;
    TextView tvInfo, tvTimer;
    CountDownTimer timer;
    boolean forward = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_select);

        spBox = findViewById(R.id.spBox);
        grid = findViewById(R.id.gridSeats);
        tvInfo = findViewById(R.id.tvInfo);
        tvTimer = findViewById(R.id.tvTimer);

        Data.SEL_BOX = 0;
        tvInfo.setText("Pick " + Data.SEL_QTY + " seat(s) in one box. Seats are held for "
                + Data.HOLD_SECONDS + " seconds once you pick one.");

        spBox.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Data.BOX_NAMES));
        spBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != Data.SEL_BOX) {
                    Data.releaseHold();
                    stopTimer();
                    Data.SEL_BOX = position;
                }
                buildGrid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        findViewById(R.id.btnContinue).setOnClickListener(v -> {
            if (Data.SEL_SEATS.size() != Data.SEL_QTY) {
                Toast.makeText(this, "Select " + Data.SEL_QTY + " seat(s)", Toast.LENGTH_SHORT).show();
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

    void buildGrid() {
        grid.removeAllViews();
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
                    Toast.makeText(this, "You only need " + Data.SEL_QTY + " seat(s)", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Data.SEL_SEATS.add(seat);
                    taken[seat] = true;
                    if (Data.HOLD_END == 0) startHold();
                }
                paint(b, seat);
            });
            grid.addView(b);
        }
    }

    void paint(MaterialButton b, int seat) {
        boolean selected = Data.SEL_SEATS.contains(seat);
        boolean taken = Data.SEAT_TAKEN[Data.SEL_EVENT][Data.SEL_BOX][seat];
        if (selected) {
            b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2E7D32")));
            b.setTextColor(Color.WHITE);
        } else if (taken) {
            b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
            b.setTextColor(Color.WHITE);
        } else {
            b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
            b.setTextColor(Color.BLACK);
        }
    }

    void startHold() {
        Data.HOLD_END = System.currentTimeMillis() + Data.HOLD_SECONDS * 1000L;
        stopTimer();
        timer = new CountDownTimer(Data.HOLD_SECONDS * 1000L, 1000) {
            @Override
            public void onTick(long ms) {
                tvTimer.setText("Seats held for " + (ms / 1000) + "s");
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
        tvTimer.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
        if (isFinishing() && !forward && Data.HOLD_END != 0) Data.releaseHold();
    }
}
