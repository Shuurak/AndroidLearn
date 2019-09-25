package com.example.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TEG = "Activity state info";
    public static final String ACT_FOR_START_NEW_ACT = "com.example.activity.START_SECOND_ACT";
    public static final String SEND_NEW_BROADCAST_MSG = "com.example.activity.SEND_NEW_BROADCAST";
    public static final String ADDITIONAL_MSG = "ADDITIONAL_MSG";

    private int taskNum = 0;
    Random rand = new Random();

    public EditText textEdit;
    private MyReceiver receiver = new MyReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TEG, "onCreate");

        textEdit = findViewById(R.id.onMainTextLine);

        Button explicit = findViewById(R.id.startExplButton);
        explicit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SecondActivity.class);
                intent.putExtra(ADDITIONAL_MSG, "This activity was started by explicit call");
                startActivity(intent);
            }
        });

        Button implicit = findViewById(R.id.startIpmlButton);
      implicit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(ACT_FOR_START_NEW_ACT);
              intent.putExtra(ADDITIONAL_MSG, "This activity was started by implicit call");
              startActivity(intent);
          }
      });

        final Button startService = findViewById(R.id.startServiceButton);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(v.getContext(), FirstSevice.class));
            }
        });

        Button startIntentService = findViewById(R.id.startIntentServiceButton);
        startIntentService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyIntentService.class);
                intent.putExtra(MyIntentService.SERVICE_PARAM_TASK, "Task #" + taskNum++);
                intent.putExtra(MyIntentService.SERVICE_PARAM_TIME, rand.nextInt(8)+10);

                startService(intent);
            }
        });

        Button stopAllServices = findViewById(R.id.stopServiceButton);
        stopAllServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(v.getContext(), FirstSevice.class));
                stopService(new Intent(v.getContext(), MyIntentService.class));
            }
        });

        final Button sendBroadcastMsg = findViewById(R.id.sendBroadcastMsgButton);
        sendBroadcastMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SEND_NEW_BROADCAST_MSG);

                intent.putExtra(MainActivity.SEND_NEW_BROADCAST_MSG, textEdit.getText().toString());
                intent.addFlags(intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(MainActivity.SEND_NEW_BROADCAST_MSG));
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
