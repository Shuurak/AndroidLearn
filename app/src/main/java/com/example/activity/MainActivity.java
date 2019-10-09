package com.example.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TEG = "Activity state info";
    public static final String ACT_FOR_START_NEW_ACT = "com.example.activity.START_SECOND_ACT";
    public static final String SEND_NEW_BROADCAST_MSG = "com.example.activity.SEND_NEW_BROADCAST";
    public static final String SHOW_ADAPTERS_ACT = "com.example.activity.SHOW_ADAPTER_ACT";
    public static final String ADDITIONAL_MSG = "ADDITIONAL_MSG";
    public static final String MAIN_ACTIVITY_HANDLER = "MAIN_ACTIVITY_HANDLER";

    private int taskNum = 0;
    private Random rand = new Random();

    private EditText textEdit;
    private MyReceiver receiver = new MyReceiver();
    private ArrayList<String> args = new ArrayList<>();

    private ServiceConnection serConn;
    private boolean bindFlag = false;
    private FirstService servForBind;
    private FirstService.CustomBinder mBinder;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TEG, "onCreate");

        textEdit = findViewById(R.id.onMainTextLine);

        mHandler = new MyHandler(this);

        serConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(LOG_TEG, "bind status - onServiceConnected");

                mBinder = (FirstService.CustomBinder) service;

                servForBind = mBinder.getService();
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
                intent.putExtra(MAIN_ACTIVITY_HANDLER, new Messenger(mHandler));

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
            }
        });

        Button startAsyncTask = findViewById(R.id.startAsyncTaskButton);
        startAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAsyncTask().execute(textEdit.getText(), null, null);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(MainActivity.SEND_NEW_BROADCAST_MSG));
        Log.d(LOG_TEG, "onStart");
    }

    static class MyHandler extends Handler {
        WeakReference<MainActivity> mWeakReference;

        public MyHandler (MainActivity aActivity) {
            mWeakReference = new WeakReference<MainActivity>(aActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            MainActivity mainActivity = mWeakReference.get();

            if (mainActivity != null) {
                mainActivity.textEdit.setText(msg.getData().getString(FirstService.NOTIFIER_MSG_REPLY));
            }
        }
    }

    private class MyAsyncTask extends AsyncTask<CharSequence , Void, Void> {

        @Override
        protected Void doInBackground(CharSequence... aStrings) {
            for (int i = 0; i < aStrings.length; i++) {
                if (!isCancelled()) {
                    SystemClock.sleep(5000);
                    Log.d(LOG_TEG, "doInBackground: "+aStrings[i]);
                }
            }
            return null;
        }
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
        mHandler.removeCallbacksAndMessages(null);
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
