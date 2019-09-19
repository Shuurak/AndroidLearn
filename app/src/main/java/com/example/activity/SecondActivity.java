package com.example.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        TextView textView = new TextView(this);
        textView.setTextSize(35);
        textView.setPadding(30,30,30,30);
        textView.setText(intent.getStringExtra(MainActivity.ADDITIONAL_MSG));

        setContentView(textView);
    }
}
