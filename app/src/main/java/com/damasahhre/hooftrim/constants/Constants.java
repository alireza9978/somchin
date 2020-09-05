package com.damasahhre.hooftrim.constants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constants {

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ir.coleo.chayi.constats.Constants.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
