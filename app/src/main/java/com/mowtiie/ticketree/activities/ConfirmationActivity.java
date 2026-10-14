package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.OvershootInterpolator;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.mowtiie.ticketree.databinding.ActivityConfirmationBinding;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityConfirmationBinding binding = ActivityConfirmationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvReceipt.setText(getIntent().getStringExtra("receipt"));

        binding.tvCheck.setScaleX(0f);
        binding.tvCheck.setScaleY(0f);
        binding.tvCheck.animate().scaleX(1f).scaleY(1f).setDuration(600)
                .setInterpolator(new OvershootInterpolator()).start();

        binding.btnWallet.setOnClickListener(v -> {
            goHome();
            startActivity(new Intent(this, TicketWalletActivity.class));
        });
        binding.btnHome.setOnClickListener(v -> goHome());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goHome();
            }
        });
    }

    void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
