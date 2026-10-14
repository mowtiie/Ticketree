package com.mowtiie.ticketree.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityOrganizerDashboardBinding;

import java.util.ArrayList;

public class OrganizerDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        for (int t = 0; t < types; t++) {
            totalTickets += sold[t];
            totalRevenue += revenue[t];
        }
        int orders = Data.DEMO_ORDERS + Data.ORDERS.size();

        binding.tvOverview.setText("SALES OVERVIEW"
                + "\nOrders: " + orders
                + "\nTickets sold: " + totalTickets
                + "\nRevenue: P" + totalRevenue
                + "\nAverage order: P" + (totalRevenue / orders));

        StringBuilder byType = new StringBuilder("SALES BY TICKET TYPE");
        StringBuilder inventory = new StringBuilder("TICKET INVENTORY");
        for (int t = 0; t < types; t++) {
            byType.append("\n").append(Data.TICKET_TYPES[t]).append(": ").append(sold[t])
                    .append(" sold, ").append(Data.money(revenue[t]));
            inventory.append("\n").append(Data.TICKET_TYPES[t]).append(": ")
                    .append(Data.TICKET_REMAINING[t]).append(" left of ")
                    .append(Data.TICKET_REMAINING[t] + sold[t]);
        }
        binding.tvByType.setText(byType.toString());
        binding.tvInventory.setText(inventory.toString());

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
        StringBuilder byDate = new StringBuilder("SALES BY DATE");
        for (int d = 0; d < dates.size(); d++) {
            byDate.append("\n").append(dates.get(d)).append(": ").append(counts.get(d)).append(" tickets");
        }
        if (!Data.ORDER_TIMES.isEmpty()) {
            byDate.append("\n\nLatest orders (date and time)");
            for (int o = Data.ORDER_TIMES.size() - 1; o >= 0 && o >= Data.ORDER_TIMES.size() - 5; o--) {
                byDate.append("\n").append(Data.ORDER_TIMES.get(o)).append(" - ")
                        .append(Data.ORDER_QTYS.get(o)).append(" ticket(s)");
            }
        }
        binding.tvByDate.setText(byDate.toString());
    }
}
