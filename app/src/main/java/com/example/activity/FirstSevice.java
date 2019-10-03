package com.example.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;


public class FirstSevice extends Service {

    private static final String LOG_TEG = "Service state info";
    private int cycles = 0;

    public static final String NOTIFIER_MSG_REPLY = "NOTIFIER_MSG_REPLY_INPUT";
    public static final String SEND_NEW_BROADCAST_MSG = "com.example.activity.SEND_NOTIFIER_REPLY";
    public static final String NOTIFIFER_ID = "NOTIF_ID";


    public IBinder binder = new customBinder();

    InnerReceiver innerReceiver = new InnerReceiver();

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
        registerReceiver(innerReceiver, new IntentFilter(SEND_NEW_BROADCAST_MSG));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TEG, "onDestroy");
        unregisterReceiver(innerReceiver);
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
        Log.d(LOG_TEG, "cycles count: " + cycles);
    }

    String returnThruBinder(String str) {
        Log.d(LOG_TEG, "new msg from activity: " + str);

        sendNotification(str);

        return "You say: " + str;
    }

    private void sendNotification(String msg) {
        String CHANNEL_ID = "MyActivityChannel";

        String channelName = "Service returner";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        String replyLabel = "Reply";
        RemoteInput remoteInput = new RemoteInput.Builder(NOTIFIER_MSG_REPLY)
                .setLabel(replyLabel)
                .build();

        Intent intent = new Intent(SEND_NEW_BROADCAST_MSG);

        intent.putExtra(FirstSevice.SEND_NEW_BROADCAST_MSG, NOTIFIER_MSG_REPLY);
        intent.putExtra(FirstSevice.NOTIFIFER_ID, 989);
//        intent.addFlags(intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 989,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_launcher_background, replyLabel, replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("New message from service")
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(action);

        notificationManager.notify(0, builder.build());
    }

    class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(FirstSevice.SEND_NEW_BROADCAST_MSG);
            if (msg.equals(FirstSevice.NOTIFIER_MSG_REPLY)) {

                Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

                if (remoteInput != null) {
                    Log.d(LOG_TEG, remoteInput.getString(FirstSevice.NOTIFIER_MSG_REPLY));

                    return;
                }
            }
        }
    }
}
