package com.example.activity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class FirstSevice extends Service {

    private static final String LOG_TEG = "Service state info";
    private int cycles = 0;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d(LOG_TEG, "onCreate");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(LOG_TEG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d(LOG_TEG, "onStartCommand");
        todoFunc();

        return super.onStartCommand(intent, flags, startId);
    }

    void todoFunc()
    {
        SystemClock.sleep(300);
        ++cycles;
        Log.d(LOG_TEG, "cycles count: "+cycles);
    }
}
