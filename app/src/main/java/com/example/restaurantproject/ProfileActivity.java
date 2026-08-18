package com.example.restaurantproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantproject.database.DatabaseHelper;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvProfileName, tvProfileEmail;
    private Button btnLogout;
    private DatabaseHelper dbHelper;

    //Update these to match whatever keys used in LoginActivity
    private static final String USER_NAME = "UserSession";
    private static final String USER_ID = "loggedInUserId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        btnLogout = findViewById(R.id.btnLogout);
        dbHelper = new DatabaseHelper(this);

        loadUserData();

        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences(USER_NAME, Context.MODE_PRIVATE);
        // Using -1 as default if no user is logged in
        int userId = prefs.getInt(USER_ID, -1);

        // Testing if Login isn't ready. Change to true to test with ID 1.
        boolean isTesting = false;
        if (isTesting) userId = 1;

        if (userId != -1) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                    new String[]{"name", "email"},
                    "id=?",
                    new String[]{String.valueOf(userId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

                tvProfileName.setText(name);
                tvProfileEmail.setText(email);
                cursor.close();
            } else {
                tvProfileName.setText("User Not Found");
                tvProfileEmail.setText("---");
            }
        } else {
            tvProfileName.setText("Guest Mode");
            tvProfileEmail.setText("Please log in");
        }
    }

    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences(USER_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // PlaceHolder for LoginActivity.java
        // Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // startActivity(intent);
        finish();
    }
}