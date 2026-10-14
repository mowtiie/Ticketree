package com.mowtiie.ticketree;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        int i = getIntent().getIntExtra("event", 0);

        ((TextView) findViewById(R.id.tvImage)).setText(Data.EVENT_NAMES[i]);

        StringBuilder sb = new StringBuilder();
        sb.append(Data.EVENT_NAMES[i]).append("\n\n");
        sb.append("Type: ").append(Data.EVENT_CATEGORIES[i]).append("\n");
        sb.append("Date and time: ").append(Data.EVENT_DATES[i]).append("\n");
        sb.append("Location: ").append(Data.EVENT_LOCATIONS[i]).append("\n");
        sb.append("Venue: ").append(Data.EVENT_VENUES[i]).append("\n");
        sb.append("Organizer: ").append(Data.EVENT_ORGANIZERS[i]).append("\n");
        sb.append("Age restriction: ").append(Data.EVENT_AGE_RESTRICTIONS[i]).append("\n");
        sb.append("Starting price: P").append(Data.EVENT_PRICES[i]).append("\n\n");
        sb.append(Data.EVENT_DESCRIPTIONS[i]).append("\n\n");
        sb.append("Ticket types\n");
        for (int t = 0; t < Data.TICKET_TYPES.length; t++) {
            sb.append(Data.TICKET_TYPES[t]).append(" - P").append(Data.TICKET_PRICES[t])
                    .append(" (").append(Data.TICKET_REMAINING[t]).append(" left)\n");
        }
        ((TextView) findViewById(R.id.tvDetails)).setText(sb.toString());

        findViewById(R.id.btnSelectTickets).setOnClickListener(v -> {
            Intent intent = new Intent(this, TicketSelectActivity.class);
            intent.putExtra("event", i);
            startActivity(intent);
        });
    }
}
