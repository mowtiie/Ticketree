package com.mowtiie.ticketree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        View.OnClickListener soon = v ->
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        binding.btnHistory.setOnClickListener(soon);
        binding.btnNotifications.setOnClickListener(soon);
        binding.btnProfile.setOnClickListener(soon);
        binding.btnPayments.setOnClickListener(soon);
    }
}
