package com.damasahhre.hooftrim.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constants {

    public static String NO_LANGUAGE = "en";
    private static String LANGUAGE_STORAGE = "someWhereInDarkness";
    private static String LANGUAGE_DATA = "someWhereInDarkness12";


    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ir.coleo.chayi.constats.Constants.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * گرفتن کلید ارتباط با سرور
     */
    public static String getDefualtlanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LANGUAGE_STORAGE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LANGUAGE_DATA,"");
    }

    /**
     * ذخیره کلید ارطباط با سرور در حافظه
     */
    public static void setLanguage(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LANGUAGE_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE_DATA, token);
        editor.apply();
    }

}
