package com.mowtiie.ticketree.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityEventFormBinding;

public class EventFormActivity extends AppCompatActivity {

    void pickDateTime(ActivityEventFormBinding binding, boolean editing) {
        long initial = MaterialDatePicker.todayInUtcMilliseconds();
        int hour = 9;
        int minute = 0;
        String current = binding.etDate.getText().toString().trim();
        if (current.matches("^[A-Z][a-z]{2} \\d{2}, \\d{4} \\d{1,2}:\\d{2} (AM|PM)$")) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                initial = dateFormat.parse(current.substring(0, 12)).getTime();
                String[] time = current.substring(13).split("[: ]");
                hour = Integer.parseInt(time[0]) % 12 + (time[2].equals("PM") ? 12 : 0);
                minute = Integer.parseInt(time[1]);
            } catch (Exception ignored) {
            }
        }
        final int startHour = hour;
        final int startMinute = minute;

        CalendarConstraints.Builder constraints = new CalendarConstraints.Builder();
        if (!editing) constraints.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Event date")
                .setSelection(initial)
                .setCalendarConstraints(constraints.build())
                .build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String day = dateFormat.format(new Date(selection));
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(startHour)
                    .setMinute(startMinute)
                    .setTitleText("Event time")
                    .build();
            timePicker.addOnPositiveButtonClickListener(view -> {
                int h = timePicker.getHour();
                int hour12 = h % 12 == 0 ? 12 : h % 12;
                binding.etDate.setText(day + " " + String.format(Locale.US, "%d:%02d %s",
                        hour12, timePicker.getMinute(), h < 12 ? "AM" : "PM"));
                binding.tilDate.setError(null);
            });
            timePicker.show(getSupportFragmentManager(), "time");
        });
        datePicker.show(getSupportFragmentManager(), "date");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityEventFormBinding binding = ActivityEventFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.tilName.setHelperText("Shown to customers");
        binding.tilCategory.setHelperText("For example AI Boot Camp or Toastmasters");
        binding.tilDate.setHelperText("Tap to choose the date and time");
        binding.tilLocation.setHelperText("City where the event takes place");
        binding.tilAge.setHelperText("For example 18+ or All ages");
        binding.tilDescription.setHelperText("What attendees will learn or see");
        binding.tilSpeakers.setHelperText("Separate names with commas");

        int event = getIntent().getIntExtra("event", -1);
        binding.ddVenue.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Data.VENUES));
        binding.ddVenue.setText(Data.VENUES.get(0), false);
        binding.ddImage.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Data.IMAGE_NAMES));
        binding.ddImage.setText(Data.IMAGE_NAMES[0], false);
        binding.ivPreview.setImageResource(Data.IMAGE_RES[0]);
        binding.ddImage.setOnItemClickListener((parent, view, position, id) ->
                binding.ivPreview.setImageResource(Data.IMAGE_RES[position]));
        binding.etTerms.setText(Data.TERMS);
        binding.tilTerms.setHelperText("What attendees agree to when they buy a ticket");

        binding.etDate.setOnClickListener(v -> pickDateTime(binding, event >= 0));

        if (event >= 0) {
            binding.toolbar.setTitle("Edit event");
            binding.etName.setText(Data.EVENT_NAMES[event]);
            binding.etCategory.setText(Data.EVENT_CATEGORIES[event]);
            binding.etDate.setText(Data.EVENT_DATES[event]);
            binding.etLocation.setText(Data.EVENT_LOCATIONS[event]);
            binding.etAge.setText(Data.EVENT_AGE_RESTRICTIONS[event]);
            binding.etDescription.setText(Data.EVENT_DESCRIPTIONS[event]);
            binding.etSpeakers.setText(Data.EVENT_SPEAKERS[event]);
            binding.swPublished.setChecked(Data.EVENT_PUBLISHED[event]);
            binding.etTerms.setText(Data.EVENT_TERMS[event]);
            binding.ddImage.setText(Data.IMAGE_NAMES[Data.EVENT_IMAGES[event]], false);
            binding.ivPreview.setImageResource(Data.IMAGE_RES[Data.EVENT_IMAGES[event]]);
            binding.ddVenue.setText(Data.EVENT_VENUES[event], false);
        } else {
            binding.toolbar.setTitle("New event");
        }

        binding.btnSave.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String category = binding.etCategory.getText().toString().trim();
            String date = binding.etDate.getText().toString().trim();
            String location = binding.etLocation.getText().toString().trim();
            String age = binding.etAge.getText().toString().trim();
            String description = binding.etDescription.getText().toString().trim();
            String speakers = binding.etSpeakers.getText().toString().trim();
            String terms = binding.etTerms.getText().toString().trim();
            String imageName = binding.ddImage.getText().toString();

            binding.tilName.setError(null);
            binding.tilCategory.setError(null);
            binding.tilDate.setError(null);
            binding.tilLocation.setError(null);
            binding.tilAge.setError(null);
            binding.tilDescription.setError(null);
            binding.tilSpeakers.setError(null);
            binding.tilTerms.setError(null);
            if (name.isEmpty()) {
                binding.tilName.setError("Enter the event name");
                return;
            }
            if (category.isEmpty()) {
                binding.tilCategory.setError("Enter a category");
                return;
            }
            if (!date.matches("^[A-Z][a-z]{2} \\d{2}, \\d{4} \\d{1,2}:\\d{2} (AM|PM)$")) {
                binding.tilDate.setError("Use the format Dec 20, 2026 9:00 AM");
                return;
            }
            if (location.isEmpty()) {
                binding.tilLocation.setError("Enter the city");
                return;
            }
            if (age.isEmpty()) {
                binding.tilAge.setError("Enter an age restriction");
                return;
            }
            if (description.isEmpty()) {
                binding.tilDescription.setError("Enter a short description");
                return;
            }
            if (speakers.isEmpty()) {
                binding.tilSpeakers.setError("Enter at least one speaker");
                return;
            }
            if (terms.length() < 10) {
                binding.tilTerms.setError("Enter the terms and conditions");
                return;
            }
            int i = event;
            if (i < 0) {
                Data.addEvent();
                i = Data.EVENT_NAMES.length - 1;
                Data.EVENT_ORGANIZERS[i] = Data.USER_NAME;
                Data.EVENT_ORG_CONTACTS[i] = Data.USER_PHONE.isEmpty() ? "Not provided" : Data.USER_PHONE;
                Data.EVENT_ORG_ABOUT[i] = Data.USER_ABOUT.isEmpty() ? "No description yet." : Data.USER_ABOUT;
            }
            Data.EVENT_NAMES[i] = name;
            Data.EVENT_CATEGORIES[i] = category;
            Data.EVENT_DATES[i] = date;
            Data.EVENT_LOCATIONS[i] = location;
            Data.EVENT_VENUES[i] = binding.ddVenue.getText().toString();
            Data.EVENT_AGE_RESTRICTIONS[i] = age;
            Data.EVENT_DESCRIPTIONS[i] = description;
            Data.EVENT_SPEAKERS[i] = speakers;
            Data.EVENT_TERMS[i] = terms;
            for (int k = 0; k < Data.IMAGE_NAMES.length; k++) {
                if (Data.IMAGE_NAMES[k].equals(imageName)) Data.EVENT_IMAGES[i] = k;
            }
            Data.EVENT_PUBLISHED[i] = binding.swPublished.isChecked();
            Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
