package com.example.android_oriflame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void signIn(View view) {
        Intent dsp = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(dsp);
    }
}
