package com.example.android_oriflame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class SignInActivity extends AppCompatActivity {

    EditText email, password;
    ProgressBar progress;
    FirebaseAuth fAuth;
    Button signInButton;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        this.email = findViewById(R.id.email);
        this.password = findViewById(R.id.password);
        this.progress = findViewById(R.id.progressBar);
        this.signInButton = findViewById(R.id.signin);

        // Firebase instance
        this.fAuth = FirebaseAuth.getInstance();

        // Set progress bar color
        this.progress.setProgressTintList(ColorStateList.valueOf(Color.CYAN));

        // Check if user is already logged in
        // If so, redirect them
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        this.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validation
                EditText[] arr = {email, password};

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

                fAuth.signInWithEmailAndPassword(em, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Signed In!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            Toast.makeText(SignInActivity.this, "Wrong Email or Password!", Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }

    public void signUp(View view) {
        Intent dsp = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(dsp);
    }
}
