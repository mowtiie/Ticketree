package com.mowtiie.ticketree.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mowtiie.ticketree.adapters.GenericAdapter;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityPaymentMethodsBinding;
import com.mowtiie.ticketree.databinding.SheetAddPaymentBinding;

public class PaymentMethodsActivity extends AppCompatActivity {

    ActivityPaymentMethodsBinding binding;
    GenericAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPaymentMethodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.empty.tvEmoji.setText("💳");
        binding.empty.tvMessage.setText("No payment methods saved");

        adapter = new GenericAdapter<>(Data.PAYMENT_METHODS, (row, method, position) -> {
            row.tvTitle.setText(method);
            row.tvSubtitle.setText(method.contains("****") ? "Credit / debit card" : "E-wallet");
            row.chipBadge.setVisibility(position == Data.DEFAULT_PAYMENT ? View.VISIBLE : View.GONE);
            row.chipBadge.setText("Default");
            row.btnAction.setVisibility(View.VISIBLE);
            row.btnAction.setText("Remove");
            row.btnAction.setOnClickListener(v -> {
                Data.PAYMENT_METHODS.remove(position);
                if (position == Data.DEFAULT_PAYMENT) Data.DEFAULT_PAYMENT = 0;
                else if (position < Data.DEFAULT_PAYMENT) Data.DEFAULT_PAYMENT--;
                adapter.notifyDataSetChanged();
                refreshEmpty();
            });
            row.getRoot().setOnClickListener(v -> {
                Data.DEFAULT_PAYMENT = position;
                adapter.notifyDataSetChanged();
                Snackbar.make(binding.getRoot(), method + " is now your default", Snackbar.LENGTH_SHORT).show();
            });
        });
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(adapter);
        refreshEmpty();

        binding.btnAdd.setOnClickListener(v -> showAddSheet());
    }

    void showAddSheet() {
        BottomSheetDialog sheet = new BottomSheetDialog(this);
        SheetAddPaymentBinding sb = SheetAddPaymentBinding.inflate(getLayoutInflater());
        sheet.setContentView(sb.getRoot());

        String[] providers = {"GCash", "Maya", "ShopeePay", "GrabPay"};
        sb.ddProvider.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, providers));
        sb.ddProvider.setText(providers[0], false);
        sb.tilMobile.setHelperText("11 digits, for example 09171234567");
        sb.tilCardNumber.setHelperText("16 digits. Only the last 4 are shown after saving");
        sb.tilExpiry.setHelperText("Month and year");
        sb.tilHolder.setHelperText("As printed on the card");

        sb.tabType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                boolean card = tab.getPosition() == 1;
                sb.walletFields.setVisibility(card ? View.GONE : View.VISIBLE);
                sb.cardFields.setVisibility(card ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        sb.btnSave.setOnClickListener(v -> {
            String label;
            if (sb.tabType.getSelectedTabPosition() == 0) {
                String mobile = sb.etMobile.getText().toString().trim();
                sb.tilMobile.setError(null);
                if (!mobile.matches("^09\\d{9}$")) {
                    sb.tilMobile.setError("Enter an 11-digit number starting with 09");
                    return;
                }
                label = sb.ddProvider.getText() + " " + mobile.substring(0, 4) + " *** " + mobile.substring(8);
                if (Data.PAYMENT_METHODS.contains(label)) {
                    sb.tilMobile.setError("This number is already saved");
                    return;
                }
            } else {
                String number = sb.etCardNumber.getText().toString().trim();
                String expiry = sb.etExpiry.getText().toString().trim();
                String holder = sb.etHolder.getText().toString().trim();
                sb.tilCardNumber.setError(null);
                sb.tilExpiry.setError(null);
                sb.tilHolder.setError(null);
                if (!number.matches("^\\d{16}$")) {
                    sb.tilCardNumber.setError("Enter all 16 digits");
                    return;
                }
                if (!expiry.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
                    sb.tilExpiry.setError("Use MM/YY");
                    return;
                }
                if (holder.length() < 3) {
                    sb.tilHolder.setError("Enter the name on the card");
                    return;
                }
                String brand = number.startsWith("4") ? "Visa" : number.startsWith("5") ? "Mastercard" : "Card";
                label = brand + " ****" + number.substring(12);
                if (Data.PAYMENT_METHODS.contains(label)) {
                    sb.tilCardNumber.setError("This card is already saved");
                    return;
                }
            }
            Data.PAYMENT_METHODS.add(label);
            if (Data.PAYMENT_METHODS.size() == 1) Data.DEFAULT_PAYMENT = 0;
            adapter.notifyDataSetChanged();
            refreshEmpty();
            sheet.dismiss();
            Snackbar.make(binding.getRoot(), label + " added", Snackbar.LENGTH_SHORT).show();
        });

        sheet.show();
    }

    void refreshEmpty() {
        binding.empty.getRoot().setVisibility(Data.PAYMENT_METHODS.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
