package com.mowtiie.ticketree;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    CountDownTimer timer;
    boolean paying = false;
    String seatsText = "";
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        EditText etName = findViewById(R.id.etName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etAddress = findViewById(R.id.etAddress);
        Spinner spPayment = findViewById(R.id.spPayment);
        TextView tvTimer = findViewById(R.id.tvTimer);
        View progress = findViewById(R.id.progress);
        View btnPay = findViewById(R.id.btnPay);

        etName.setText(Data.USER_NAME);
        etEmail.setText(Data.USER_EMAIL);

        if (Data.PAYMENT_METHODS.isEmpty()) {
            Data.PAYMENT_METHODS.add("GCash");
            Data.PAYMENT_METHODS.add("Maya");
            Data.PAYMENT_METHODS.add("Visa ****1234");
        }
        spPayment.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Data.PAYMENT_METHODS));

        Collections.sort(Data.SEL_SEATS);
        StringBuilder sb = new StringBuilder();
        for (int s : Data.SEL_SEATS) sb.append(sb.length() > 0 ? ", " : "").append(s + 1);
        seatsText = Data.BOX_NAMES[Data.SEL_BOX] + " seat " + sb;
        total = Data.TICKET_PRICES[Data.SEL_TYPE] * Data.SEL_QTY;

        ((TextView) findViewById(R.id.tvSummary)).setText(
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
                tvTimer.setText("Seats held for " + (ms / 1000) + "s");
            }

            @Override
            public void onFinish() {
                expire();
            }
        }.start();

        btnPay.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            if (name.isEmpty() || email.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Fill in all billing fields", Toast.LENGTH_SHORT).show();
                return;
            }
            String method = spPayment.getSelectedItem().toString();
            paying = true;
            btnPay.setEnabled(false);
            progress.setVisibility(View.VISIBLE);
            tvTimer.setText("Processing payment...");
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
        Data.ORDERS.add(orderId + " - " + eventName + " - " + typeName + " x" + Data.SEL_QTY + " - P" + total);

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
        Data.NOTIFICATIONS.add("Payment received: P" + total + " via " + method);
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
