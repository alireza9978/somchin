package com.damasahhre.hooftrim.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.NextReport;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.models.MyDate;

import java.util.Date;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        MyDao myDao = DataBase.getInstance(context).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<NextReport> reports = myDao.getAllVisitInDay(new MyDate(new Date()));
            Log.i("Alarm", "onReceive: " + reports.size());
        });
    }

}
