package com.mowtiie.ticketree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TicketSelectActivity extends AppCompatActivity {

    int event;
    int qty = 1;
    Spinner spType;
    TextView tvQty, tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_select);

        event = getIntent().getIntExtra("event", 0);
        spType = findViewById(R.id.spType);
        tvQty = findViewById(R.id.tvQty);
        tvTotal = findViewById(R.id.tvTotal);

        ((TextView) findViewById(R.id.tvEvent)).setText(Data.EVENT_NAMES[event]);

        String[] rows = new String[Data.TICKET_TYPES.length];
        for (int t = 0; t < rows.length; t++) {
            rows[t] = Data.TICKET_TYPES[t] + " - P" + Data.TICKET_PRICES[t]
                    + " (" + Data.TICKET_REMAINING[t] + " left)";
        }
        spType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rows));
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qty = 1;
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        findViewById(R.id.btnMinus).setOnClickListener(v -> {
            if (qty > 1) qty--;
            update();
        });
        findViewById(R.id.btnPlus).setOnClickListener(v -> {
            int max = Math.min(6, Data.TICKET_REMAINING[spType.getSelectedItemPosition()]);
            if (qty < max) qty++;
            else Toast.makeText(this, "Maximum " + max + " tickets", Toast.LENGTH_SHORT).show();
            update();
        });

        findViewById(R.id.btnContinue).setOnClickListener(v -> {
            int type = spType.getSelectedItemPosition();
            if (Data.TICKET_REMAINING[type] < qty) {
                Toast.makeText(this, "Sold out", Toast.LENGTH_SHORT).show();
                return;
            }
            Data.releaseHold();
            Data.SEL_EVENT = event;
            Data.SEL_TYPE = type;
            Data.SEL_QTY = qty;
            startActivity(new Intent(this, SeatSelectActivity.class));
        });
    }

    void update() {
        tvQty.setText(String.valueOf(qty));
        tvTotal.setText("Total: P" + (Data.TICKET_PRICES[spType.getSelectedItemPosition()] * qty));
    }
}
