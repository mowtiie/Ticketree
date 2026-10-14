package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mowtiie.ticketree.R;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityTicketWalletBinding;

public class TicketWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityTicketWalletBinding binding = ActivityTicketWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.empty.tvEmoji.setText("🎟");
        binding.empty.tvMessage.setText("No tickets yet");
        binding.empty.btnEmpty.setVisibility(View.VISIBLE);
        binding.empty.btnEmpty.setText("Browse events");
        binding.empty.btnEmpty.setOnClickListener(v ->
                startActivity(new Intent(this, EventListActivity.class)));
        binding.empty.getRoot().setVisibility(Data.TICKETS.isEmpty() ? View.VISIBLE : View.GONE);

        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(new GenericAdapter<String>(Data.TICKETS, (row, ticket, position) -> {
            String[] p = ticket.split("\\|");
            String category = Data.EVENT_CATEGORIES[Data.eventIndex(p[1])];
            row.tvIcon.setVisibility(View.VISIBLE);
            row.tvIcon.setText(Data.categoryEmoji(category));
            row.tvIcon.setBackgroundTintList(ColorStateList.valueOf(Data.categoryColor(category)));
            row.tvTitle.setText(p[1]);
            row.tvSubtitle.setText(p[2] + "\n" + p[3] + " | " + p[4] + ", seat " + p[5]);
            row.chipBadge.setVisibility(View.VISIBLE);
            row.chipBadge.setText("Valid");
            row.chipBadge.setTextColor(getColor(R.color.success));
            row.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(this, TicketViewActivity.class);
                intent.putExtra("ticket", position);
                startActivity(intent);
            });
        }));
    }
}
