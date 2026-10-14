package com.mowtiie.ticketree.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.mowtiie.ticketree.R;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityOrganizerDashboardBinding;

import java.util.ArrayList;

public class OrganizerDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityOrganizerDashboardBinding binding = ActivityOrganizerDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        int types = Data.TICKET_TYPES.length;
        int[] sold = new int[types];
        int[] revenue = new int[types];
        int totalTickets = 0;
        int totalRevenue = 0;
        for (int t = 0; t < types; t++) {
            sold[t] = Data.DEMO_SOLD_BY_TYPE[t];
            revenue[t] = Data.DEMO_SOLD_BY_TYPE[t] * Data.TICKET_PRICES[t];
        }
        for (int o = 0; o < Data.ORDER_TYPES.size(); o++) {
            int t = Data.ORDER_TYPES.get(o);
            sold[t] += Data.ORDER_QTYS.get(o);
            revenue[t] += Data.ORDER_TOTALS.get(o);
        }
        int best = 0;
        int maxRevenue = 1;
        for (int t = 0; t < types; t++) {
            totalTickets += sold[t];
            totalRevenue += revenue[t];
            maxRevenue = Math.max(maxRevenue, revenue[t]);
        }
        int orders = Data.DEMO_ORDERS + Data.ORDERS.size();

        binding.tvRevenue.setText(Data.money(totalRevenue));
        binding.tvTickets.setText(String.valueOf(totalTickets));
        binding.tvOrders.setText(String.valueOf(orders));
        binding.tvAverage.setText(Data.money(totalRevenue / orders));

        for (int t = 0; t < types; t++) {
            addBar(binding.llByType, Data.TICKET_TYPES[t] + ": " + sold[t] + " sold, " + Data.money(revenue[t]),
                    revenue[t], maxRevenue, typeColor(t));
            addBar(binding.llInventory, Data.TICKET_TYPES[t] + ": " + Data.TICKET_REMAINING[t] + " left of "
                    + (Data.TICKET_REMAINING[t] + sold[t]), Data.TICKET_REMAINING[t], Data.TICKET_REMAINING[t] + sold[t], typeColor(t));
            if (sold[t] > sold[best]) best = t;
        }

        binding.chipBest.setText("Best seller: " + Data.TICKET_TYPES[best] + " (" + sold[best] + " sold)");
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<Integer> counts = new ArrayList<>();
        for (int d = 0; d < Data.DEMO_SALE_DATES.length; d++) {
            dates.add(Data.DEMO_SALE_DATES[d]);
            counts.add(Data.DEMO_SALE_TICKETS[d]);
        }
        for (int o = 0; o < Data.ORDER_TIMES.size(); o++) {
            String day = Data.ORDER_TIMES.get(o).substring(0, 12);
            int at = dates.indexOf(day);
            if (at < 0) {
                dates.add(day);
                counts.add(Data.ORDER_QTYS.get(o));
            } else {
                counts.set(at, counts.get(at) + Data.ORDER_QTYS.get(o));
            }
        }
        int maxCount = 1;
        for (int c : counts) maxCount = Math.max(maxCount, c);
        for (int d = 0; d < dates.size(); d++) {
            addBar(binding.llByDate, dates.get(d) + ": " + counts.get(d) + " tickets", counts.get(d), maxCount, typeColor(3));
        }

        if (Data.ORDER_TIMES.isEmpty()) {
            binding.tvLatest.setText("No new orders yet. Buy a ticket as a customer to see it here.");
        } else {
            StringBuilder latest = new StringBuilder("Latest orders");
            for (int o = Data.ORDER_TIMES.size() - 1; o >= 0 && o >= Data.ORDER_TIMES.size() - 5; o--) {
                latest.append("\n").append(Data.ORDER_TIMES.get(o)).append(" - ")
                        .append(Data.ORDER_QTYS.get(o)).append(" ticket(s)");
            }
            binding.tvLatest.setText(latest.toString());
        }
    }

    int typeColor(int t) {
        View root = getWindow().getDecorView();
        if (t == 0) return MaterialColors.getColor(root, androidx.appcompat.R.attr.colorPrimary);
        if (t == 1) return MaterialColors.getColor(root, com.google.android.material.R.attr.colorTertiary);
        if (t == 2) return MaterialColors.getColor(root, com.google.android.material.R.attr.colorSecondary);
        return getColor(R.color.success);
    }

    void addBar(LinearLayout container, String label, int value, int max, int color) {
        MaterialTextView text = new MaterialTextView(this);
        text.setText(label);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.topMargin = (int) (12 * getResources().getDisplayMetrics().density);
        container.addView(text, textParams);

        LinearProgressIndicator bar = new LinearProgressIndicator(this);
        bar.setMax(max);
        bar.setProgress(value);
        bar.setIndicatorColor(color);
        bar.setTrackCornerRadius((int) (4 * getResources().getDisplayMetrics().density));
        container.addView(bar, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
