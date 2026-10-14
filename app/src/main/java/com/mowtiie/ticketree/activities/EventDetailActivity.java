package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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
        binding.tvFromPrice.setText(Data.money(Data.EVENT_PRICES[i]));
        binding.chipCategory.setText(Data.EVENT_CATEGORIES[i]);
        binding.chipVenue.setText(Data.EVENT_VENUES[i]);
        binding.chipAge.setText("Age " + Data.EVENT_AGE_RESTRICTIONS[i]);

        StringBuilder sb = new StringBuilder();
        sb.append("Date and time: ").append(Data.EVENT_DATES[i]).append("\n");
        sb.append("Location: ").append(Data.EVENT_LOCATIONS[i]).append("\n");
        sb.append("Organizer: ").append(Data.EVENT_ORGANIZERS[i]).append("\n");
        sb.append("Contact: ").append(Data.EVENT_ORG_CONTACTS[i]).append("\n");
        sb.append("About the organizer: ").append(Data.EVENT_ORG_ABOUT[i]).append("\n");
        sb.append("Starting price: ").append(Data.money(Data.EVENT_PRICES[i])).append("\n\n");
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
            Intent intent = new Intent(this, TicketSelectActivity.class);
            intent.putExtra("event", i);
            startActivity(intent);
        });
    }
}
