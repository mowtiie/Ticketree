package com.mowtiie.ticketree;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TicketViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_view);

        String[] p = Data.TICKETS.get(getIntent().getIntExtra("ticket", 0)).split("\\|");
        ((TextView) findViewById(R.id.tvEvent)).setText(p[1]);
        ((TextView) findViewById(R.id.tvInfo)).setText(p[2] + "\n" + p[3] + "\n" + p[4] + ", seat " + p[5]
                + "\nOrder " + p[0] + "\nHolder: " + Data.USER_NAME);
    }
}
