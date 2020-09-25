package com.damasahhre.hooftrim.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.damasahhre.hooftrim.R;

public class Constants {

    public static String NO_LANGUAGE = "en";
    private static String LANGUAGE_STORAGE = "someWhereInDarkness";
    private static String LANGUAGE_DATA = "someWhereInDarkness12";

    //intent to start activity data
    public static final String FARM_ID = "sdaxce";
    public static final String COW_ID = "Addssaxce";

    public static final int CHOOSE_FILE_REQUEST_CODE = 99;
    public static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 98;

    public static final int FARM_SELECTION_SEARCH_COW = 101;
    public static final int DATE_SELECTION_SEARCH_COW = 102;
    public static final int DATE_SELECTION_SEARCH_FARM = 104;

    public static final int DATE_SELECTION_REPORT_CREATE = 103;

    public static final int DATE_SELECTION_REPORT_FACTOR = 105;
    public static final int FARM_SELECTION_REPORT_FACTOR = 106;

    public static final int DATE_SELECTION_OK = 200;
    public static final int DATE_SELECTION_FAIL = 400;
    public static final String DATE_SELECTION_RESULT = "res_xc";

    public static class DateSelectionMode{
        public static String SINGLE = "asdasdngy";
        public static String RANG = "vuwasdngy";
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ir.coleo.chayi.constats.Constants.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void setImageFront(Context context, ImageView imageView){
        Configuration config = context.getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_front));
        }else{
            imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_back));
        }
    }

    public static void setImageBack(Context context, ImageView imageView){
        Configuration config = context.getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back));
        }else{
            imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_front));
        }
    }

    public static void setImageBackBorder(Context context,ImageView imageView){
        Configuration config = context.getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_next));
        }else{
            imageView.setImageDrawable(ContextCompat.getDrawable(context ,R.drawable.ic_previous));
        }
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
