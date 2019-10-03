package com.example.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TEG = "Activity state info";
    public static final String ACT_FOR_START_NEW_ACT = "com.example.activity.START_SECOND_ACT";
    public static final String SEND_NEW_BROADCAST_MSG = "com.example.activity.SEND_NEW_BROADCAST";
    public static final String SHOW_ADAPTERS_ACT = "com.example.activity.SHOW_ADAPTER_ACT";
    public static final String ADDITIONAL_MSG = "ADDITIONAL_MSG";

    private int taskNum = 0;
    private Random rand = new Random();

    private EditText textEdit;
    private MyReceiver receiver = new MyReceiver();
    private ArrayList<String> args = new ArrayList<>();

    private ServiceConnection serConn;
    private boolean bindFlag = false;
    private FirstService servForBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TEG, "onCreate");

        textEdit = findViewById(R.id.onMainTextLine);

        serConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(LOG_TEG, "bind status - onServiceConnected");

                servForBind = ((FirstService.customBinder)service).getService();
                bindFlag = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TEG, "bind status - onServiceDisconnected");
                bindFlag = false;
            }
        };

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
                startService(new Intent(v.getContext(), FirstService.class));
            }
        });

        Button startIntentService = findViewById(R.id.startIntentServiceButton);
        startIntentService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyIntentService.class);
                intent.putExtra(MyIntentService.SERVICE_PARAM_TASK, "Task #" + taskNum++);
                intent.putExtra(MyIntentService.SERVICE_PARAM_TIME, rand.nextInt(8) + 10);

                startService(intent);
            }
        });

        Button stopAllServices = findViewById(R.id.stopServiceButton);
        stopAllServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(v.getContext(), FirstService.class));
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

        Button showAdapterButton = findViewById(R.id.showAdapterActButton);
        showAdapterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.add("UK");
                args.add("USA");
                args.add("Japan");
                args.add("France");
                args.add("Russia");
                args.add("China");
                args.add("Canada");
                args.add("Australia");
                args.add("Germany");

                Intent intent = new Intent(SHOW_ADAPTERS_ACT);
                intent.putExtra(ADDITIONAL_MSG, args);

                startActivity(intent);
            }
        });

        Button bindToServiceButton = findViewById(R.id.bindToServiceButton);
        bindToServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FirstService.class);

                bindService(intent, serConn, BIND_AUTO_CREATE);
            }
        });

        Button unbindFromServiceButton = findViewById(R.id.unbindFromServiceButton);
        unbindFromServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bindFlag) return;
                unbindService(serConn);
                bindFlag = false;
            }
        });

        Button sendMsgToBindedService = findViewById(R.id.sendMsgToBindedServiceButton);
        sendMsgToBindedService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bindFlag) return;
                String str = servForBind.returnThruBinder(textEdit.getText().toString());
                Log.d(LOG_TEG, str);

//                String CHANNEL_ID = "MyActivityChannel";
//                String channelName = "Service returner";
//                int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
//
//                NotificationManager notificationManager = getSystemService(NotificationManager.class);
//                notificationManager.createNotificationChannel(channel);
//
////                String replyLabel = "Reply";
////                RemoteInput remoteInput = new RemoteInput.Builder(NOTIFIER_MSG_REPLY)
////                        .setLabel(replyLabel)
////                        .build();
////
////                PendingIntent replyPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
////                            con)
//
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(v.getContext(), CHANNEL_ID)
//                        .setContentTitle("New message from service")
//                        .setContentText(str)
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                            .bigText(str))
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                notificationManager.notify(0, builder.build());

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(MainActivity.SEND_NEW_BROADCAST_MSG));
        Log.d(LOG_TEG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(LOG_TEG, "onRestart");
    }

    @Override
    protected void onResume() {
        
        super.onResume();

        Log.d(LOG_TEG, "onResume");
    }

    @Override
    protected void onPause() {
    
        super.onPause();

        Log.d(LOG_TEG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(receiver);
        Log.d(LOG_TEG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TEG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstState) {
        super.onSaveInstanceState(saveInstState);

        Log.d(LOG_TEG, "SaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstState) {
        super.onRestoreInstanceState(saveInstState);

        Log.d(LOG_TEG, "RestoreInstanceState");
    }
}
