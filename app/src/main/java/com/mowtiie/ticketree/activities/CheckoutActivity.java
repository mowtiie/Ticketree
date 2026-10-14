package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import com.mowtiie.ticketree.databinding.ActivityCheckoutBinding;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding;
    CountDownTimer timer;
    boolean paying = false;
    String seatsText = "";
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());


        binding.etName.setText(Data.USER_NAME);
        binding.etEmail.setText(Data.USER_EMAIL);

        if (Data.PAYMENT_METHODS.isEmpty()) {
            Data.PAYMENT_METHODS.add("GCash");
            Data.PAYMENT_METHODS.add("Maya");
            Data.PAYMENT_METHODS.add("Visa ****1234");
        }
        binding.spPayment.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Data.PAYMENT_METHODS));

        Collections.sort(Data.SEL_SEATS);
        StringBuilder sb = new StringBuilder();
        for (int s : Data.SEL_SEATS) sb.append(sb.length() > 0 ? ", " : "").append(s + 1);
        seatsText = Data.BOX_NAMES[Data.SEL_BOX] + " seat " + sb;
        total = Data.TICKET_PRICES[Data.SEL_TYPE] * Data.SEL_QTY;

        binding.tvSummary.setText(
                "Event: " + Data.EVENT_NAMES[Data.SEL_EVENT]
                        + "\nTicket type: " + Data.TICKET_TYPES[Data.SEL_TYPE]
                        + "\nSeats: " + seatsText
                        + "\nQuantity: " + Data.SEL_QTY
                        + "\nTotal: P" + total);

        long left = Data.HOLD_END - System.currentTimeMillis();
        if (left <= 0) {
            expire();
            return;
        }
        timer = new CountDownTimer(left, 1000) {
            @Override
            public void onTick(long ms) {
                Data.styleTimer(binding.tvTimer, ms);
            }

            @Override
            public void onFinish() {
                expire();
            }
        }.start();

        binding.btnPay.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String address = binding.etAddress.getText().toString().trim();
            if (name.isEmpty() || email.isEmpty() || address.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Fill in all billing fields", Snackbar.LENGTH_SHORT).show();
                return;
            }
            String method = binding.spPayment.getSelectedItem().toString();
            paying = true;
            binding.btnPay.setEnabled(false);
            binding.overlay.setVisibility(View.VISIBLE);
            if (timer != null) timer.cancel();
            Data.HOLD_END = 0;

            new Handler(Looper.getMainLooper()).postDelayed(
                    () -> finishOrder(name, email, address, method), 2000);
        });
    }

    void expire() {
        if (paying || Data.HOLD_END == 0) return;
        int event = Data.SEL_EVENT;
        Data.releaseHold();
        Toast.makeText(this, "Hold expired. Seats released.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("event", event);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void finishOrder(String name, String email, String address, String method) {
        String orderId = "TKT-" + (1001 + Data.ORDERS.size());
        String eventName = Data.EVENT_NAMES[Data.SEL_EVENT];
        String typeName = Data.TICKET_TYPES[Data.SEL_TYPE];
        String now = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.US).format(new Date());

        Data.TICKET_REMAINING[Data.SEL_TYPE] -= Data.SEL_QTY;
        for (int s : Data.SEL_SEATS) {
            Data.TICKETS.add(orderId + "|" + eventName + "|" + Data.EVENT_DATES[Data.SEL_EVENT] + "|"
                    + typeName + "|" + Data.BOX_NAMES[Data.SEL_BOX] + "|" + (s + 1));
        }
        Data.ORDERS.add(orderId + " - " + eventName + " - " + typeName + " x" + Data.SEL_QTY + " - " + Data.money(total));
        Data.ORDER_TYPES.add(Data.SEL_TYPE);
        Data.ORDER_QTYS.add(Data.SEL_QTY);
        Data.ORDER_TOTALS.add(total);
        Data.ORDER_TIMES.add(now);

        String receipt = "RECEIPT " + orderId
                + "\nDate: " + now
                + "\n\nEvent: " + eventName
                + "\nWhen: " + Data.EVENT_DATES[Data.SEL_EVENT]
                + "\nTicket: " + typeName + " x" + Data.SEL_QTY
                + "\nSeats: " + seatsText
                + "\n\nBilled to: " + name
                + "\nEmail: " + email
                + "\nAddress: " + address
                + "\nPaid with: " + method
                + "\n\nTOTAL: P" + total;
        Data.RECEIPTS.add(receipt);

        Data.NOTIFICATIONS.add("Booking confirmed: " + eventName + " (" + orderId + ")");
        Data.NOTIFICATIONS.add("Payment received: " + Data.money(total) + " via " + method);
        Data.NOTIFICATIONS.add("Tickets delivered to " + email);

        Data.SEL_SEATS.clear();
        Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra("receipt", receipt);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}
