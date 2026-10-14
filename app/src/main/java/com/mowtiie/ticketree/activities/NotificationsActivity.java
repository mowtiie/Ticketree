package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mowtiie.ticketree.databinding.ActivityNotificationsBinding;

public class NotificationsActivity extends AppCompatActivity {

    ActivityNotificationsBinding binding;
    GenericAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new GenericAdapter<>(Data.NOTIFICATIONS, (row, text, position) -> {
            row.tvTitle.setText(text);
            row.tvSubtitle.setVisibility(View.GONE);
        });
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(adapter);
        refreshEmpty();
        Data.UNREAD = 0;

        binding.btnSimulate.setOnClickListener(v -> {
            String event = Data.EVENT_NAMES[0];
            if (!Data.TICKETS.isEmpty()) {
                event = Data.TICKETS.get(Data.TICKETS.size() - 1).split("\\|")[1];
            }
            String reminder = "Reminder: " + event + " starts soon. Bring your QR ticket!";
            Data.addNotification(reminder);
            Data.UNREAD = 0;
            adapter.notifyDataSetChanged();
            refreshEmpty();
            Snackbar.make(binding.getRoot(), reminder, Snackbar.LENGTH_LONG).show();
        });
    }

    void refreshEmpty() {
        binding.empty.tvEmoji.setText("🔔");
        binding.empty.tvMessage.setText("No notifications yet");
        binding.empty.getRoot().setVisibility(Data.NOTIFICATIONS.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
