package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityEventDetailBinding;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityEventDetailBinding binding = ActivityEventDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        int i = getIntent().getIntExtra("event", 0);

        binding.ivImage.setImageResource(Data.IMAGE_RES[Data.EVENT_IMAGES[i]]);
        binding.tvImage.setText(Data.EVENT_NAMES[i]);
        binding.tvFromPrice.setText(Data.money(Data.fromPrice()));
        binding.chipCategory.setText(Data.EVENT_CATEGORIES[i]);
        binding.chipCategory.setContentDescription("Show more " + Data.EVENT_CATEGORIES[i] + " events");
        binding.chipCategory.setOnClickListener(v -> {
            Intent intent = new Intent(this, EventListActivity.class);
            intent.putExtra("category", Data.EVENT_CATEGORIES[i]);
            startActivity(intent);
        });

        StringBuilder sb = new StringBuilder();
        sb.append("Date and time: ").append(Data.EVENT_DATES[i]).append("\n");
        sb.append("Location: ").append(Data.EVENT_LOCATIONS[i]).append("\n");
        sb.append("Venue: ").append(Data.EVENT_VENUES[i]).append("\n");
        sb.append("Age restriction: ").append(Data.EVENT_AGE_RESTRICTIONS[i]).append("\n");
        sb.append("Organizer: ").append(Data.EVENT_ORGANIZERS[i]).append("\n");
        sb.append("Contact: ").append(Data.EVENT_ORG_CONTACTS[i]).append("\n");
        sb.append("About the organizer: ").append(Data.EVENT_ORG_ABOUT[i]).append("\n");
        sb.append(Data.EVENT_DESCRIPTIONS[i]).append("\n\n");
        sb.append("Speaker lineup\n").append(Data.EVENT_SPEAKERS[i]).append("\n\n");
        sb.append("Ticket types\n");
        for (int t = 0; t < Data.TICKET_TYPES.length; t++) {
            sb.append(Data.TICKET_TYPES[t]).append(" - ").append(Data.money(Data.TICKET_PRICES[t]))
                    .append(" (").append(Data.TICKET_REMAINING[t]).append(" left)\n");
        }
        sb.append("\nTerms and conditions\n").append(Data.EVENT_TERMS[i]);
        binding.tvDetails.setText(sb.toString());

        binding.btnSelectTickets.setOnClickListener(v -> {
            int age = Data.ageOf(Data.USER_BIRTHDATE);
            if (!Data.USER_IS_ORGANIZER && age < Data.minAge(i)) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Age restriction")
                        .setMessage("This event is " + Data.EVENT_AGE_RESTRICTIONS[i] + ". Your profile says you are "
                                + age + ", so you cannot buy tickets for it.")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }
            Intent intent = new Intent(this, TicketSelectActivity.class);
            intent.putExtra("event", i);
            startActivity(intent);
        });
    }
}
