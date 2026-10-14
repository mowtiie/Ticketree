package com.mowtiie.ticketree;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.materialswitch.MaterialSwitch;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText etName = findViewById(R.id.etName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        MaterialSwitch swOrganizer = findViewById(R.id.swOrganizer);

        findViewById(R.id.btnCreate).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            Data.USER_NAME = name;
            Data.USER_EMAIL = email;
            Data.USER_PASSWORD = password;
            Data.USER_IS_ORGANIZER = swOrganizer.isChecked();
            Toast.makeText(this, "Account created. Please log in.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
