package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.color.MaterialColors;
import com.mowtiie.ticketree.R;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityOrganizerHomeBinding;

import java.util.ArrayList;

public class OrganizerHomeActivity extends AppCompatActivity {

    ActivityOrganizerHomeBinding binding;
    GenericAdapter<Integer> adapter;
    ArrayList<Integer> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrganizerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvWelcome.setText("Organizer: " + Data.USER_NAME);

        adapter = new GenericAdapter<>(events, (row, event, position) -> {
            Data.bindEvent(row, event);
            row.tvSubtitle.setText(Data.EVENT_DATES[event] + "\nFrom " + Data.money(Data.fromPrice()));
            boolean published = Data.EVENT_PUBLISHED[event];
            row.chipBadge.setText(published ? "Published" : "Unpublished");
            row.chipBadge.setChipBackgroundColor(ColorStateList.valueOf(published
                    ? getColor(R.color.success)
                    : MaterialColors.getColor(row.getRoot(), com.google.android.material.R.attr.colorSurfaceVariant)));
            row.chipBadge.setTextColor(published
                    ? MaterialColors.getColor(row.getRoot(), com.google.android.material.R.attr.colorSurface)
                    : MaterialColors.getColor(row.getRoot(), com.google.android.material.R.attr.colorOnSurfaceVariant));
            row.btnAction.setVisibility(android.view.View.VISIBLE);
            row.btnAction.setText(Data.EVENT_PUBLISHED[event] ? "Unpublish" : "Publish");
            row.btnAction.setOnClickListener(v -> {
                Data.EVENT_PUBLISHED[event] = !Data.EVENT_PUBLISHED[event];
                adapter.notifyDataSetChanged();
            });
            row.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(this, EventFormActivity.class);
                intent.putExtra("event", event);
                startActivity(intent);
            });
        });
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(adapter);

        binding.toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_new_event) {
                startActivity(new Intent(this, EventFormActivity.class));
            } else if (id == R.id.action_dashboard) {
                startActivity(new Intent(this, OrganizerDashboardActivity.class));
            } else if (id == R.id.action_venues) {
                startActivity(new Intent(this, VenueManagementActivity.class));
            } else if (id == R.id.action_pricing) {
                startActivity(new Intent(this, TicketPricingActivity.class));
            } else if (id == R.id.action_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                return false;
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        events.clear();
        for (int i = 0; i < Data.EVENT_NAMES.length; i++) events.add(i);
        adapter.notifyDataSetChanged();
    }
}
