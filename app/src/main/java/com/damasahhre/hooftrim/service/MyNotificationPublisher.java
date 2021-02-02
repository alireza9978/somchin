package com.damasahhre.hooftrim.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.NextReport;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.models.DateContainer;

import java.util.List;


public class MyNotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";

    public void onReceive(Context context, Intent intent) {
        MyDao dao = DataBase.getInstance(context).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            DateContainer one = DateContainer.getToday(context, false);
            List<NextReport> allNextVisit = dao.getAllNextVisitInDay(one.exportStart());
            if (!allNextVisit.isEmpty()) {
                Log.i("NOTIFICATION", "onReceive: +");
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = getNotification("visit from " + allNextVisit.size() + " cow", context);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "VISIT_REMINDER_CHANNEL", importance);
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(notificationChannel);
                }
                int id = intent.getIntExtra(NOTIFICATION_ID, 0);
                assert notificationManager != null;
                notificationManager.notify(id, notification);
            }else {
                Log.i("NOTIFICATION", "onReceive: -");
            }
        });

    }

    private Notification getNotification(String content, Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, default_notification_channel_id);
        builder.setContentTitle("Farm Visit Reminder");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }

}