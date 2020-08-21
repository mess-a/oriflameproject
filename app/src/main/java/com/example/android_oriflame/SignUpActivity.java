package com.example.android_oriflame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.zip.InflaterInputStream;

public class SignUpActivity extends AppCompatActivity {

    EditText name, email, password;
    ProgressBar progress;
    FirebaseAuth fAuth;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        this.name = findViewById(R.id.name);
        this.email = findViewById(R.id.email);
        this.password = findViewById(R.id.password);
        this.progress = findViewById(R.id.progressBar);
        this.signUpButton = findViewById(R.id.signup);

        // Set progress bar color
        this.progress.setProgressTintList(ColorStateList.valueOf(Color.CYAN));

        // Firebase
        this.fAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
            finish();
        }

        // Sign Up On Click
        this.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validation
                EditText[] arr = {name, email, password};

                for (EditText editText : arr) {
                    if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                        editText.setError(editText.getHint().toString() + " is required.");
                        return;
                    }
                }

                String em = email.getText().toString().trim();
                String pw = password.getText().toString().trim();

                // Show progress
                progress.setVisibility(View.VISIBLE);

                // Create user
                fAuth.createUserWithEmailAndPassword(em, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Log user in
                        // Redirect them
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                        } else {
                            Toast.makeText(SignUpActivity.this, "Error Creating User: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }

    public void signIn(View view) {
        Intent dsp = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(dsp);
    }
}
