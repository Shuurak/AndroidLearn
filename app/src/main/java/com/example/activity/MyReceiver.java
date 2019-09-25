package com.example.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    private static final String LOG_TEG = "Receiver state info";

    public MyReceiver()
    {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        String msg = intent.getStringExtra(MainActivity.SEND_NEW_BROADCAST_MSG);

        Log.d(LOG_TEG,msg);
        Toast.makeText(context, "Received msg: "+msg, Toast.LENGTH_LONG).show();

//        throw new UnsupportedOperationException("Not yet implemented");
    }


}
