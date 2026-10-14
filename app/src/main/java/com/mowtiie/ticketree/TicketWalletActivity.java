package com.mowtiie.ticketree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TicketWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_wallet);

        ListView lv = findViewById(R.id.lvTickets);
        ArrayList<String> rows = new ArrayList<>();
        for (String t : Data.TICKETS) {
            String[] p = t.split("\\|");
            rows.add(p[1] + "\n" + p[2] + "\n" + p[3] + " - " + p[4] + " seat " + p[5] + "  (" + p[0] + ")");
        }
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows));
        if (rows.isEmpty()) findViewById(R.id.tvEmpty).setVisibility(View.VISIBLE);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, TicketViewActivity.class);
            intent.putExtra("ticket", position);
            startActivity(intent);
        });
    }
}
