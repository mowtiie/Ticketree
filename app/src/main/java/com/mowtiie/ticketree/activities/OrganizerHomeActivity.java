package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
            row.tvSubtitle.setText(Data.EVENT_DATES[event] + "\n"
                    + (Data.EVENT_PUBLISHED[event] ? "Published" : "Unpublished"));
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

        binding.btnCreate.setOnClickListener(v ->
                startActivity(new Intent(this, EventFormActivity.class)));
        binding.btnDashboard.setOnClickListener(v ->
                startActivity(new Intent(this, OrganizerDashboardActivity.class)));
        binding.btnPricing.setOnClickListener(v ->
                startActivity(new Intent(this, TicketPricingActivity.class)));
        binding.btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        events.clear();
        for (int i = 0; i < Data.EVENT_NAMES.length; i++) events.add(i);
        adapter.notifyDataSetChanged();
    }
}
