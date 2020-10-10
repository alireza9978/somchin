package com.damasahhre.hooftrim.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.damasahhre.hooftrim.notification.NotificationBuilder;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationBuilder.make(context);
    }

}
