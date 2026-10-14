package com.mowtiie.ticketree;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mowtiie.ticketree.databinding.ActivityNotificationsBinding;

public class NotificationsActivity extends AppCompatActivity {

    ActivityNotificationsBinding binding;
    GenericAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new GenericAdapter<>(Data.NOTIFICATIONS, (row, text, position) -> {
            row.tvTitle.setText(text);
            row.tvSubtitle.setVisibility(View.GONE);
        });
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(adapter);
        refreshEmpty();

        binding.btnSimulate.setOnClickListener(v -> {
            String event = Data.EVENT_NAMES[0];
            if (!Data.TICKETS.isEmpty()) {
                event = Data.TICKETS.get(Data.TICKETS.size() - 1).split("\\|")[1];
            }
            String reminder = "Reminder: " + event + " starts soon. Bring your QR ticket!";
            Data.NOTIFICATIONS.add(0, reminder);
            adapter.notifyDataSetChanged();
            refreshEmpty();
            Toast.makeText(this, reminder, Toast.LENGTH_LONG).show();
        });
    }

    void refreshEmpty() {
        binding.tvEmpty.setVisibility(Data.NOTIFICATIONS.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
