package com.example.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class FirstSevice extends Service {

    private static final String LOG_TEG = "Service state info";
    private int cycles = 0;

    public IBinder binder = new customBinder();

    class customBinder extends Binder {
        FirstSevice getService() {
            return FirstSevice.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        Log.d(LOG_TEG, "service onBind");
//        throw new UnsupportedOperationException("Not yet implemented");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TEG, "service onUnbind");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(LOG_TEG, "service onRebind");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TEG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TEG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TEG, "onStartCommand");
        todoFunc();

        return super.onStartCommand(intent, flags, startId);
    }

    private void todoFunc() {
        SystemClock.sleep(300);
        ++cycles;
        Log.d(LOG_TEG, "cycles count: "+cycles);
    }

    String returnThruBinder (String str) {
        Log.d(LOG_TEG, "new msg from activity: " + str);
        return "You say: "+str;
    }
}
