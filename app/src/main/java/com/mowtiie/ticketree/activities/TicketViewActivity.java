package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mowtiie.ticketree.databinding.ActivityTicketViewBinding;

public class TicketViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTicketViewBinding binding = ActivityTicketViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        String[] p = Data.TICKETS.get(getIntent().getIntExtra("ticket", 0)).split("\\|");
        binding.tvEvent.setText(p[1]);
        binding.tvInfo.setText(p[2] + "\n" + p[3] + "\n" + p[4] + ", seat " + p[5]
                + "\nOrder " + p[0] + "\nHolder: " + Data.USER_NAME);
    }
}
