package com.damasahhre.hooftrim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.tabs.HomeActivity;
import com.damasahhre.hooftrim.constants.Constants;

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

    private void checkConnection() {
        if (!Constants.isNetworkAvailable()) {
            changeState(1);
        } else {
            changeState(0);
        }
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
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}