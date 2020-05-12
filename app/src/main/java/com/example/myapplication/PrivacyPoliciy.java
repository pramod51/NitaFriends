package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PrivacyPoliciy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policiy);
        getSupportActionBar().setTitle(R.string.namePrivacy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
