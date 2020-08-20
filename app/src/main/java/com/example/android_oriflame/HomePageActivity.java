package com.example.android_oriflame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomePageActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    Button LogOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        this.LogOutButton = findViewById(R.id.logout);

        // Get instance
        this.fAuth = FirebaseAuth.getInstance();

        // Redirect user if not logged in
        if (fAuth.getCurrentUser() == null) {
            Toast.makeText(getApplicationContext(), "You need to be logged in to view this page.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        }

        this.LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Signout
                fAuth.signOut();
                // Redirect
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        });
    }
}
