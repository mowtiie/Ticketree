package com.mowtiie.ticketree.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityVenueManagementBinding;

public class VenueManagementActivity extends AppCompatActivity {

    ActivityVenueManagementBinding binding;
    GenericAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityVenueManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.empty.getRoot().setVisibility(View.GONE);
        binding.tilVenue.setHelperText("For example Onsite, Hotel, Training Camp or Convention Center");

        adapter = new GenericAdapter<>(Data.VENUES, (row, venue, position) -> {
            int used = 0;
            for (String v : Data.EVENT_VENUES) {
                if (venue.equals(v)) used++;
            }
            final int usedBy = used;
            row.tvTitle.setText(venue);
            row.tvSubtitle.setText(usedBy == 0 ? "Not used by any event" : "Used by " + usedBy + " event(s)");
            row.btnAction.setVisibility(View.VISIBLE);
            row.btnAction.setText("Remove");
            row.btnAction.setOnClickListener(v -> {
                if (usedBy > 0) {
                    Snackbar.make(binding.getRoot(), "Change the venue of those events first", Snackbar.LENGTH_SHORT).show();
                } else if (Data.VENUES.size() == 1) {
                    Snackbar.make(binding.getRoot(), "Keep at least one venue", Snackbar.LENGTH_SHORT).show();
                } else {
                    Data.VENUES.remove(position);
                    adapter.notifyDataSetChanged();
                    Snackbar.make(binding.getRoot(), venue + " removed", Snackbar.LENGTH_LONG)
                            .setAction("Undo", undo -> {
                                Data.VENUES.add(position, venue);
                                adapter.notifyDataSetChanged();
                            }).show();
                }
            });
        });
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(adapter);

        binding.btnAdd.setOnClickListener(v -> {
            String venue = binding.etVenue.getText().toString().trim();
            binding.tilVenue.setError(null);
            if (venue.length() < 3) {
                binding.tilVenue.setError("Enter a venue name");
                return;
            }
            if (Data.VENUES.contains(venue)) {
                binding.tilVenue.setError("This venue already exists");
                return;
            }
            Data.VENUES.add(venue);
            binding.etVenue.setText("");
            adapter.notifyDataSetChanged();
        });
    }
}
