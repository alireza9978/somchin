package com.damasahhre.hooftrim.server;

import android.widget.Toast;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.SplashActivity;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;

import java.util.Timer;
import java.util.TimerTask;

import static com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread;

public class Downloader {

    public static void download(String updateUrl, SplashActivity activity) {

        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(activity)
                .setDownloadConcurrentLimit(3)
                .build();

        Fetch fetch = Fetch.Impl.getInstance(fetchConfiguration);

        String file = "/downloads/SomchinYar.apk";
        Request request = new Request(updateUrl, file);
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);

        fetch.enqueue(request, updatedRequest -> {
            runOnUiThread(() -> {
                Toast.makeText(activity, R.string.install, Toast.LENGTH_LONG).show();
            });

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(activity::finish);
                }
            }, 2000);
        }, error -> {
            Toast.makeText(activity, R.string.server_error, Toast.LENGTH_LONG).show();
        });
    }

}
