package com.mowtiie.ticketree;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    Spinner spCategory, spDate, spLocation;
    EditText etSearch;
    ListView lvEvents;
    ArrayList<Integer> shown = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        etSearch = findViewById(R.id.etSearch);
        spCategory = findViewById(R.id.spCategory);
        spDate = findViewById(R.id.spDate);
        spLocation = findViewById(R.id.spLocation);
        lvEvents = findViewById(R.id.lvEvents);

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
        spCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));
        spDate.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dates));
        spLocation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locations));

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spCategory.setOnItemSelectedListener(filterListener);
        spDate.setOnItemSelectedListener(filterListener);
        spLocation.setOnItemSelectedListener(filterListener);

        etSearch.addTextChangedListener(new TextWatcher() {
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

        lvEvents.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra("event", shown.get(position));
            startActivity(intent);
        });

        refresh();
    }

    void refresh() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        String category = spCategory.getSelectedItem().toString();
        String date = spDate.getSelectedItem().toString();
        String location = spLocation.getSelectedItem().toString();

        shown.clear();
        ArrayList<String> rows = new ArrayList<>();
        for (int i = 0; i < Data.EVENT_NAMES.length; i++) {
            if (!query.isEmpty() && !Data.EVENT_NAMES[i].toLowerCase().contains(query)
                    && !Data.EVENT_DESCRIPTIONS[i].toLowerCase().contains(query)) continue;
            if (spCategory.getSelectedItemPosition() > 0 && !Data.EVENT_CATEGORIES[i].equals(category)) continue;
            if (spDate.getSelectedItemPosition() > 0 && !Data.EVENT_DATES[i].startsWith(date)) continue;
            if (spLocation.getSelectedItemPosition() > 0 && !Data.EVENT_LOCATIONS[i].equals(location)) continue;
            shown.add(i);
            rows.add(Data.EVENT_NAMES[i] + "\n" + Data.EVENT_CATEGORIES[i] + " | "
                    + Data.EVENT_DATES[i] + " | " + Data.EVENT_LOCATIONS[i] + " | P" + Data.EVENT_PRICES[i]);
        }
        lvEvents.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows));
    }
}
