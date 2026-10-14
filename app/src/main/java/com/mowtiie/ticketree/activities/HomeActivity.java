package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.annotation.OptIn;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.chip.Chip;
import com.mowtiie.ticketree.R;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    BadgeDrawable badge;

    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    @Override
    protected void onResume() {
        super.onResume();
        if (badge != null) BadgeUtils.detachBadgeDrawable(badge, binding.toolbar, R.id.action_notifications);
        badge = BadgeDrawable.create(this);
        badge.setNumber(Data.UNREAD);
        badge.setVisible(Data.UNREAD > 0);
        BadgeUtils.attachBadgeDrawable(badge, binding.toolbar, R.id.action_notifications);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvWelcome.setText(Data.greeting());

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
            if (item.getItemId() != R.id.action_notifications) return false;
            startActivity(new Intent(this, NotificationsActivity.class));
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
            lp.bottomMargin = 0;
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
        binding.cardProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
        binding.cardPayments.setOnClickListener(v ->
                startActivity(new Intent(this, PaymentMethodsActivity.class)));
    }
}
