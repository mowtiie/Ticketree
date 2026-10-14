package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mowtiie.ticketree.databinding.ActivityPaymentMethodsBinding;

public class PaymentMethodsActivity extends AppCompatActivity {

    ActivityPaymentMethodsBinding binding;
    GenericAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentMethodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new GenericAdapter<>(Data.PAYMENT_METHODS, (row, method, position) -> {
            row.tvTitle.setText(method);
            row.tvSubtitle.setVisibility(View.GONE);
            row.btnAction.setVisibility(View.VISIBLE);
            row.btnAction.setText("Remove");
            row.btnAction.setOnClickListener(v -> {
                Data.PAYMENT_METHODS.remove(position);
                adapter.notifyDataSetChanged();
                refreshEmpty();
            });
        });
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(adapter);
        refreshEmpty();

        binding.tilMethod.setHelperText("For example GCash, Maya or Visa ****4321");

        binding.btnAdd.setOnClickListener(v -> {
            String method = binding.etMethod.getText().toString().trim();
            binding.tilMethod.setError(null);
            if (method.isEmpty()) {
                binding.tilMethod.setError("Enter a payment method");
                return;
            }
            if (Data.PAYMENT_METHODS.contains(method)) {
                binding.tilMethod.setError("This payment method is already saved");
                return;
            }
            Data.PAYMENT_METHODS.add(method);
            binding.etMethod.setText("");
            adapter.notifyDataSetChanged();
            refreshEmpty();
        });
    }

    void refreshEmpty() {
        binding.empty.tvEmoji.setText("💳");
        binding.empty.tvMessage.setText("No payment methods saved");
        binding.empty.getRoot().setVisibility(Data.PAYMENT_METHODS.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
