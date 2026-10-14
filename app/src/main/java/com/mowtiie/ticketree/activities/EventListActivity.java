package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mowtiie.ticketree.databinding.ActivityEventListBinding;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    ActivityEventListBinding binding;
    ArrayList<Integer> shown = new ArrayList<>();
    GenericAdapter<Integer> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> locations = new ArrayList<>();
        categories.add("All categories");
        dates.add("All dates");
        locations.add("All locations");
        for (int i = 0; i < Data.EVENT_NAMES.length; i++) {
            String date = Data.EVENT_DATES[i].substring(0, 12);
            if (!categories.contains(Data.EVENT_CATEGORIES[i])) categories.add(Data.EVENT_CATEGORIES[i]);
            if (!dates.contains(date)) dates.add(date);
            if (!locations.contains(Data.EVENT_LOCATIONS[i])) locations.add(Data.EVENT_LOCATIONS[i]);
        }
        binding.spCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));
        binding.spDate.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dates));
        binding.spLocation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locations));

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        binding.spCategory.setOnItemSelectedListener(filterListener);
        binding.spDate.setOnItemSelectedListener(filterListener);
        binding.spLocation.setOnItemSelectedListener(filterListener);

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

        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GenericAdapter<>(shown, (row, event, position) -> {
            row.tvTitle.setText(Data.EVENT_NAMES[event]);
            row.tvSubtitle.setText(Data.EVENT_CATEGORIES[event] + " | " + Data.EVENT_DATES[event] + " | "
                    + Data.EVENT_LOCATIONS[event] + " | P" + Data.EVENT_PRICES[event]);
            row.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(this, EventDetailActivity.class);
                intent.putExtra("event", event);
                startActivity(intent);
            });
        });
        binding.rvList.setAdapter(adapter);

        refresh();
    }

    void refresh() {
        String query = binding.etSearch.getText().toString().trim().toLowerCase();
        String category = binding.spCategory.getSelectedItem().toString();
        String date = binding.spDate.getSelectedItem().toString();
        String location = binding.spLocation.getSelectedItem().toString();

        shown.clear();
        for (int i = 0; i < Data.EVENT_NAMES.length; i++) {
            if (!query.isEmpty() && !Data.EVENT_NAMES[i].toLowerCase().contains(query)
                    && !Data.EVENT_DESCRIPTIONS[i].toLowerCase().contains(query)) continue;
            if (binding.spCategory.getSelectedItemPosition() > 0 && !Data.EVENT_CATEGORIES[i].equals(category)) continue;
            if (binding.spDate.getSelectedItemPosition() > 0 && !Data.EVENT_DATES[i].startsWith(date)) continue;
            if (binding.spLocation.getSelectedItemPosition() > 0 && !Data.EVENT_LOCATIONS[i].equals(location)) continue;
            shown.add(i);
        }
        adapter.notifyDataSetChanged();
        binding.tvEmpty.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
