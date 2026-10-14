package com.mowtiie.ticketree.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityTicketViewBinding;

public class TicketViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityTicketViewBinding binding = ActivityTicketViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        String[] p = Data.TICKETS.get(getIntent().getIntExtra("ticket", 0)).split("\\|");
        String category = Data.EVENT_CATEGORIES[Data.eventIndex(p[1])];
        binding.header.setBackgroundColor(Data.categoryColor(category));
        binding.tvEmoji.setText(Data.categoryEmoji(category));
        binding.tvEvent.setText(p[1]);
        binding.tvDate.setText(p[2]);
        binding.tvType.setText(p[3]);
        binding.tvSeat.setText(p[4] + ", seat " + p[5]);
        binding.tvOrder.setText("Order " + p[0] + "\nHolder: " + Data.USER_NAME);
    }
}
