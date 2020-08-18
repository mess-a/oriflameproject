package com.example.android_oriflame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class signin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }

    public void signIn(View view) {
        Intent dsp = new Intent(signin.this, HomeActivity.class);
        startActivity(dsp);
    }
}
