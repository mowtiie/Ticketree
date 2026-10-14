package com.mowtiie.ticketree.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityEventFormBinding;

public class EventFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEventFormBinding binding = ActivityEventFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        int event = getIntent().getIntExtra("event", -1);
        binding.spVenue.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Data.VENUE_TYPES));

        if (event >= 0) {
            binding.toolbar.setTitle("Edit event");
            binding.etName.setText(Data.EVENT_NAMES[event]);
            binding.etCategory.setText(Data.EVENT_CATEGORIES[event]);
            binding.etDate.setText(Data.EVENT_DATES[event]);
            binding.etLocation.setText(Data.EVENT_LOCATIONS[event]);
            binding.etPrice.setText(String.valueOf(Data.EVENT_PRICES[event]));
            binding.etAge.setText(Data.EVENT_AGE_RESTRICTIONS[event]);
            binding.etDescription.setText(Data.EVENT_DESCRIPTIONS[event]);
            binding.swPublished.setChecked(Data.EVENT_PUBLISHED[event]);
            for (int v = 0; v < Data.VENUE_TYPES.length; v++) {
                if (Data.VENUE_TYPES[v].equals(Data.EVENT_VENUES[event])) binding.spVenue.setSelection(v);
            }
        } else {
            binding.toolbar.setTitle("New event");
        }

        binding.btnSave.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String category = binding.etCategory.getText().toString().trim();
            String date = binding.etDate.getText().toString().trim();
            String location = binding.etLocation.getText().toString().trim();
            String price = binding.etPrice.getText().toString().trim();
            String age = binding.etAge.getText().toString().trim();
            String description = binding.etDescription.getText().toString().trim();
            if (name.isEmpty() || category.isEmpty() || date.length() < 12 || location.isEmpty()
                    || price.isEmpty() || age.isEmpty() || description.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Fill in all fields (date like Dec 20, 2026 9:00 AM)", Snackbar.LENGTH_LONG).show();
                return;
            }
            int i = event;
            if (i < 0) {
                Data.addEvent();
                i = Data.EVENT_NAMES.length - 1;
                Data.EVENT_ORGANIZERS[i] = Data.USER_NAME;
            }
            Data.EVENT_NAMES[i] = name;
            Data.EVENT_CATEGORIES[i] = category;
            Data.EVENT_DATES[i] = date;
            Data.EVENT_LOCATIONS[i] = location;
            Data.EVENT_VENUES[i] = binding.spVenue.getSelectedItem().toString();
            Data.EVENT_PRICES[i] = Integer.parseInt(price);
            Data.EVENT_AGE_RESTRICTIONS[i] = age;
            Data.EVENT_DESCRIPTIONS[i] = description;
            Data.EVENT_PUBLISHED[i] = binding.swPublished.isChecked();
            Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
