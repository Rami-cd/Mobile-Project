package com.example.mobileproject.ui.Profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobileproject.R;


public class EditProfileActivity extends AppCompatActivity {

    EditText editName, editEmail;
    Button saveButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editName = findViewById(R.id.edit_profile_name);
        editEmail = findViewById(R.id.edit_profile_email);
        saveButton = findViewById(R.id.btn_save_profile);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);

        // Load saved user data
        loadUserData();

        saveButton.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadUserData() {
        String name = sharedPreferences.getString("name", "User Name");
        String email = sharedPreferences.getString("email", "user@example.com");

        editName.setText(name);
        editEmail.setText(email);
    }

    private void saveProfileChanges() {
        String newName = editName.getText().toString().trim();
        String newEmail = editEmail.getText().toString().trim();

        // Save data to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", newName);
        editor.putString("email", newEmail);
        editor.apply();

        finish();
    }
}
