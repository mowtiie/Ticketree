package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.mowtiie.ticketree.R;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onResume() {
        super.onResume();
        binding.tvNotifLabel.setText("🔔\nNotifications"
                + (Data.UNREAD > 0 ? " (" + Data.UNREAD + " new)" : ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvWelcome.setText("Welcome, " + Data.USER_NAME);

        ArrayList<String> categories = new ArrayList<>();
        for (int i = 0; i < Data.EVENT_NAMES.length; i++) {
            if (Data.EVENT_PUBLISHED[i] && !categories.contains(Data.EVENT_CATEGORIES[i])) categories.add(Data.EVENT_CATEGORIES[i]);
        }
        for (String category : categories) {
            Chip chip = new Chip(this);
            chip.setText(category);
            chip.setOnClickListener(v -> {
                Intent intent = new Intent(this, EventListActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            });
            binding.chipCategories.addView(chip);
        }

        binding.toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_notifications) {
                startActivity(new Intent(this, NotificationsActivity.class));
            } else if (id == R.id.action_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else if (id == R.id.action_logout) {
                Data.USER_NAME = "";
                Data.USER_EMAIL = "";
                Data.USER_PASSWORD = "";
                Data.USER_IS_ORGANIZER = false;
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                return false;
            }
            return true;
        });

        ArrayList<Integer> featured = new ArrayList<>();
        for (int i = 0; i < Data.EVENT_NAMES.length && featured.size() < 3; i++) {
            if (Data.EVENT_PUBLISHED[i]) featured.add(i);
        }
        float density = getResources().getDisplayMetrics().density;
        binding.rvFeatured.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvFeatured.setAdapter(new GenericAdapter<Integer>(featured, (row, event, position) -> {
            Data.bindEvent(row, event);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) row.getRoot().getLayoutParams();
            lp.width = (int) (300 * density);
            lp.setMarginEnd((int) (8 * density));
            row.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(this, EventDetailActivity.class);
                intent.putExtra("event", event);
                startActivity(intent);
            });
        }));

        binding.cardBrowse.setOnClickListener(v ->
                startActivity(new Intent(this, EventListActivity.class)));
        binding.cardTickets.setOnClickListener(v ->
                startActivity(new Intent(this, TicketWalletActivity.class)));
        binding.cardHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
        binding.cardNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsActivity.class)));
        binding.cardProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
        binding.cardPayments.setOnClickListener(v ->
                startActivity(new Intent(this, PaymentMethodsActivity.class)));
    }
}
