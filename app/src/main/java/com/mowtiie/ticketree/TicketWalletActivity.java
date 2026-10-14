package com.mowtiie.ticketree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.mowtiie.ticketree.databinding.ActivityTicketWalletBinding;

import java.util.ArrayList;

public class TicketWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTicketWalletBinding binding = ActivityTicketWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<String> rows = new ArrayList<>();
        for (String t : Data.TICKETS) {
            String[] p = t.split("\\|");
            rows.add(p[1] + "\n" + p[2] + "\n" + p[3] + " - " + p[4] + " seat " + p[5] + "  (" + p[0] + ")");
        }
        binding.lvTickets.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows));
        if (rows.isEmpty()) binding.tvEmpty.setVisibility(View.VISIBLE);

        binding.lvTickets.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, TicketViewActivity.class);
            intent.putExtra("ticket", position);
            startActivity(intent);
        });
    }
}
