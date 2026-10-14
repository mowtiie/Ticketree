package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mowtiie.ticketree.databinding.ActivityTicketWalletBinding;

public class TicketWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTicketWalletBinding binding = ActivityTicketWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Data.TICKETS.isEmpty()) binding.tvEmpty.setVisibility(View.VISIBLE);

        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(new GenericAdapter<String>(Data.TICKETS, (row, ticket, position) -> {
            String[] p = ticket.split("\\|");
            row.tvTitle.setText(p[1]);
            row.tvSubtitle.setText(p[2] + "\n" + p[3] + " - " + p[4] + " seat " + p[5] + "  (" + p[0] + ")");
            row.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(this, TicketViewActivity.class);
                intent.putExtra("ticket", position);
                startActivity(intent);
            });
        }));
    }
}
