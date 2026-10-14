package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mowtiie.ticketree.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvWelcome.setText("Welcome, " + Data.USER_NAME);

        ArrayList<Integer> featured = new ArrayList<>();
        for (int i = 0; i < Data.EVENT_NAMES.length && featured.size() < 3; i++) {
            if (Data.EVENT_PUBLISHED[i]) featured.add(i);
        }
        binding.rvFeatured.setLayoutManager(new LinearLayoutManager(this));
        binding.rvFeatured.setAdapter(new GenericAdapter<Integer>(featured, (row, event, position) -> {
            row.tvTitle.setText(Data.EVENT_NAMES[event]);
            row.tvSubtitle.setText(Data.EVENT_DATES[event] + " | " + Data.EVENT_LOCATIONS[event]
                    + " | from P" + Data.EVENT_PRICES[event]);
            row.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(this, EventDetailActivity.class);
                intent.putExtra("event", event);
                startActivity(intent);
            });
        }));

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
