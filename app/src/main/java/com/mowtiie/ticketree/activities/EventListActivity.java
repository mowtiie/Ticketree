package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mowtiie.ticketree.R;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityEventListBinding;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    static final String ALL_CATEGORIES = "All categories";
    static final String ALL_DATES = "All dates";
    static final String ALL_LOCATIONS = "All locations";

    ActivityEventListBinding binding;
    ArrayList<Integer> shown = new ArrayList<>();
    GenericAdapter<Integer> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEventListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> locations = new ArrayList<>();
        categories.add(ALL_CATEGORIES);
        dates.add(ALL_DATES);
        locations.add(ALL_LOCATIONS);
        for (int i = 0; i < Data.EVENT_NAMES.length; i++) {
            if (!Data.EVENT_PUBLISHED[i]) continue;
            String date = Data.EVENT_DATES[i].substring(0, Math.min(12, Data.EVENT_DATES[i].length()));
            if (!categories.contains(Data.EVENT_CATEGORIES[i])) categories.add(Data.EVENT_CATEGORIES[i]);
            if (!dates.contains(date)) dates.add(date);
            if (!locations.contains(Data.EVENT_LOCATIONS[i])) locations.add(Data.EVENT_LOCATIONS[i]);
        }
        binding.ddCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories));
        binding.ddDate.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dates));
        binding.ddLocation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locations));
        binding.ddCategory.setOnItemClickListener((parent, view, position, id) -> refresh());
        binding.ddDate.setOnItemClickListener((parent, view, position, id) -> refresh());
        binding.ddLocation.setOnItemClickListener((parent, view, position, id) -> refresh());

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refresh();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() != R.id.action_clear) return false;
            clearFilters();
            return true;
        });

        binding.empty.tvEmoji.setText("🔍");
        binding.empty.tvMessage.setText("No events match your filters");
        binding.empty.btnEmpty.setVisibility(View.VISIBLE);
        binding.empty.btnEmpty.setText("Clear filters");
        binding.empty.btnEmpty.setOnClickListener(v -> clearFilters());

        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GenericAdapter<>(shown, (row, event, position) -> {
            Data.bindEvent(row, event);
            row.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(this, EventDetailActivity.class);
                intent.putExtra("event", event);
                startActivity(intent);
            });
        });
        binding.rvList.setAdapter(adapter);

        clearFilters();
        String preset = getIntent().getStringExtra("category");
        if (preset != null) {
            binding.ddCategory.setText(preset, false);
            refresh();
        }
    }

    void clearFilters() {
        binding.etSearch.setText("");
        binding.ddCategory.setText(ALL_CATEGORIES, false);
        binding.ddDate.setText(ALL_DATES, false);
        binding.ddLocation.setText(ALL_LOCATIONS, false);
        refresh();
    }

    void refresh() {
        String query = binding.etSearch.getText().toString().trim().toLowerCase();
        String category = binding.ddCategory.getText().toString();
        String date = binding.ddDate.getText().toString();
        String location = binding.ddLocation.getText().toString();

        shown.clear();
        for (int i = 0; i < Data.EVENT_NAMES.length; i++) {
            if (!Data.EVENT_PUBLISHED[i]) continue;
            if (!query.isEmpty() && !Data.EVENT_NAMES[i].toLowerCase().contains(query)
                    && !Data.EVENT_DESCRIPTIONS[i].toLowerCase().contains(query)) continue;
            if (!category.equals(ALL_CATEGORIES) && !Data.EVENT_CATEGORIES[i].equals(category)) continue;
            if (!date.equals(ALL_DATES) && !Data.EVENT_DATES[i].startsWith(date)) continue;
            if (!location.equals(ALL_LOCATIONS) && !Data.EVENT_LOCATIONS[i].equals(location)) continue;
            shown.add(i);
        }
        adapter.notifyDataSetChanged();
        binding.empty.getRoot().setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
