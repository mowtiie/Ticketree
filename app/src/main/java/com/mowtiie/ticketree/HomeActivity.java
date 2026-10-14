package com.mowtiie.ticketree;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mowtiie.ticketree.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvWelcome.setText("Welcome, " + Data.USER_NAME);

        for (int i = 0; i < 3; i++) {
            final int index = i;
            Button b = new Button(this);
            b.setAllCaps(false);
            b.setText(Data.EVENT_NAMES[i] + "\n" + Data.EVENT_DATES[i] + " - P" + Data.EVENT_PRICES[i]);
            b.setOnClickListener(v -> {
                Intent intent = new Intent(this, EventDetailActivity.class);
                intent.putExtra("event", index);
                startActivity(intent);
            });
            binding.featuredContainer.addView(b);
        }

        binding.btnBrowse.setOnClickListener(v ->
                startActivity(new Intent(this, EventListActivity.class)));
        binding.btnTickets.setOnClickListener(v ->
                startActivity(new Intent(this, TicketWalletActivity.class)));

        binding.btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
        binding.btnNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsActivity.class)));
        binding.btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
        binding.btnPayments.setOnClickListener(v ->
                startActivity(new Intent(this, PaymentMethodsActivity.class)));
    }
}
