package com.damasahhre.hooftrim.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.models.Cow;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.models.FarmWithCows;
import com.damasahhre.hooftrim.database.utils.AppExecutors;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ConstraintLayout loading_state;
    private ConstraintLayout error_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        loading_state = findViewById(R.id.splash_loading_container);
        error_state = findViewById(R.id.offline_splash_loading_container);
        findViewById(R.id.retry).setOnClickListener(v -> {
            checkConnection();
        });
        findViewById(R.id.work_offline).setOnClickListener(v -> {
            goApp();
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkConnection();
            }
        }, 1000);

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(updateBaseContextLocale(newBase));
    }

    public Context updateBaseContextLocale(Context context) {
        String language = Constants.getDefualtlanguage(context);
        if (language.isEmpty()) {
            //when first time enter into app (get the device language and set it
            language = Locale.getDefault().getLanguage();
            Constants.setLanguage(context, language);
        }
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale);
            return updateResourcesLocaleLegacy(context, locale);
        }

        return updateResourcesLocaleLegacy(context, locale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    private void checkConnection() {
        goApp();
//        if (!Constants.isNetworkAvailable()) {
//            changeState(1);
//        } else {
//            goApp();
//        }
    }

    private void changeState(int state) {
        if (state == 1) {
            error_state.setVisibility(View.VISIBLE);
            loading_state.setVisibility(View.INVISIBLE);
        } else if (state == 0) {
            loading_state.setVisibility(View.VISIBLE);
            error_state.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * تغییر به صفحه ورود
     */
    public void goLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * بازکردن صفحه اصلی
     */
    public void goApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}