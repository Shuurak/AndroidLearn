package com.example.activity;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.security.Timestamp;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TEG = "Activity state info";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TEG, "onCreate");
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Log.d(LOG_TEG, "onStart");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

        Log.d(LOG_TEG, "onRestart");
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Log.d(LOG_TEG, "onResume");
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        Log.d(LOG_TEG, "onPause");
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        Log.d(LOG_TEG, "onStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        Log.d(LOG_TEG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstState)
    {
        super.onSaveInstanceState(saveInstState);

        Log.d(LOG_TEG, "SaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstState)
    {
        super.onRestoreInstanceState(saveInstState);

        Log.d(LOG_TEG, "RestoreInstanceState");
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
}
