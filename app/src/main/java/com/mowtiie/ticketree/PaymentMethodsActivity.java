package com.mowtiie.ticketree;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

        binding.btnAdd.setOnClickListener(v -> {
            String method = binding.etMethod.getText().toString().trim();
            if (method.isEmpty()) {
                Toast.makeText(this, "Enter a payment method", Toast.LENGTH_SHORT).show();
                return;
            }
            Data.PAYMENT_METHODS.add(method);
            binding.etMethod.setText("");
            adapter.notifyDataSetChanged();
            refreshEmpty();
        });
    }

    void refreshEmpty() {
        binding.tvEmpty.setVisibility(Data.PAYMENT_METHODS.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
