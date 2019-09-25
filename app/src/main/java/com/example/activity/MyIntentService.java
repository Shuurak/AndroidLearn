package com.example.activity;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {

    private static final String LOG_TEG = "MyIntentService state info";
    public static final String SERVICE_PARAM_TIME = "timestamp";
    public static final String SERVICE_PARAM_TASK = "task";


    public MyIntentService()
    {
        super("myIntentService");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d(LOG_TEG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        int timestamp = intent.getIntExtra(SERVICE_PARAM_TIME, 0);
        String task = intent.getStringExtra(SERVICE_PARAM_TASK);


        Log.d(LOG_TEG, "Service started with task "+task);
        SystemClock.sleep(timestamp*300);
        Log.d(LOG_TEG, "Service stopped with task "+task+" work time: "+(timestamp+2));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(LOG_TEG, "onDestroy");
    }
}
