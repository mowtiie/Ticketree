package com.mowtiie.ticketree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ((TextView) findViewById(R.id.tvWelcome)).setText("Welcome, " + Data.USER_NAME);

        LinearLayout container = findViewById(R.id.featuredContainer);
        for (int i = 0; i < 3; i++) {
            final int index = i;
            Button b = new Button(this);
            b.setAllCaps(false);
            b.setText(Data.EVENT_NAMES[i] + "\n" + Data.EVENT_DATES[i] + " - P" + Data.EVENT_PRICES[i]);
            b.setOnClickListener(v -> {
                Intent intent = new Intent(this, EventDetailActivity.class);
                intent.putExtra("event", index);
                startActivity(intent);
            });
            container.addView(b);
        }

        findViewById(R.id.btnBrowse).setOnClickListener(v ->
                startActivity(new Intent(this, EventListActivity.class)));

        View.OnClickListener soon = v ->
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        findViewById(R.id.btnTickets).setOnClickListener(v ->
                startActivity(new Intent(this, TicketWalletActivity.class)));
        findViewById(R.id.btnHistory).setOnClickListener(soon);
        findViewById(R.id.btnNotifications).setOnClickListener(soon);
        findViewById(R.id.btnProfile).setOnClickListener(soon);
        findViewById(R.id.btnPayments).setOnClickListener(soon);
    }
}
