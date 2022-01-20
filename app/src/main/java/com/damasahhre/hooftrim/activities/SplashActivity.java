package com.damasahhre.hooftrim.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.dialog.ErrorDialog;
import com.damasahhre.hooftrim.dialog.SureDialog;
import com.damasahhre.hooftrim.server.Requests;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


/**
 * صفحه‌ی اغازین برنامه
 */
public class SplashActivity extends AppCompatActivity {


    private ConstraintLayout loading_state;
    private ConstraintLayout error_state;
    private int apkVersionCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        AppCenter.start(getApplication(), "f4c019af-38a5-44af-b87a-22c2e0dc8f27",
                Analytics.class, Crashes.class);

        TextView version = findViewById(R.id.appVersionName);
        String versionText = "0.1.20";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionText = pInfo.versionName;
            apkVersionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version.setText(getString(R.string.version) + versionText);


        loading_state = findViewById(R.id.splash_loading_container);
        error_state = findViewById(R.id.offline_splash_loading_container);
        findViewById(R.id.retry).setOnClickListener(v -> checkConnection());
        findViewById(R.id.work_offline).setOnClickListener(v -> {
            if (Constants.getToken(this).equals(Constants.NO_TOKEN_B)) {
                Toast.makeText(this, R.string.login_to_work_ffline, Toast.LENGTH_LONG).show();
            } else {
                goApp();
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> checkConnection());
            }
        }, 2000);
        changeState(0);

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(updateBaseContextLocale(newBase));
    }

    public Context updateBaseContextLocale(Context context) {
        if (Constants.getDefaultLanguage(context).equals(Constants.NO_LANGUAGE)) {
            Constants.setLanguage(context, "fa");
        }
        String language = Constants.getDefaultLanguage(context);
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
        if (!Constants.isNetworkAvailable(this)) {
            changeState(1);
        } else {
            Requests.setContext(this);
            Requests.checkVersion(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    SplashActivity.this.runOnUiThread(() -> {
                        Toast.makeText(SplashActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
                        changeState(1);
                    });

                }

                @Override
                public void onResponse(Response response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            int versionCode = jsonObject.getInt("version_code");
                            boolean forceUpdate = jsonObject.getBoolean("force_update");
                            String updateUrl = jsonObject.getString("apk_url");
                            if (forceUpdate) {
                                runOnUiThread(() -> showForceUpdateDialog(updateUrl));
                            } else {
                                if (apkVersionCode < versionCode) {
                                    showUpdateDialog(updateUrl);
                                } else {
                                    runOnUiThread(() -> {
                                        if (Constants.getToken(SplashActivity.this).equals(Constants.NO_TOKEN_B)) {
                                            goLogin();
                                        } else {
                                            goApp();
                                        }
                                    });
                                }
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Requests.toastMessage(response, SplashActivity.this);
                    }
                }
            });
        }
    }

    /**
     * نمایش اپدیت اجباری
     */
    public void showForceUpdateDialog(String updateUrl) {
        ErrorDialog updateDialog = new ErrorDialog(SplashActivity.this, updateUrl);
        Objects.requireNonNull(updateDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        updateDialog.show();
    }


    /**
     * نمایش اپدیت معمولی
     */
    public void showUpdateDialog(String updateUrl) {
        runOnUiThread(() -> {
            SureDialog updateDialog = new SureDialog(this,
                    getString(R.string.new_update),
                    getString(R.string.update_message)
                    , () ->
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
                startActivity(browserIntent);
                finish();
            }
                    , () ->
            {
                if (Constants.getToken(SplashActivity.this).equals(Constants.NO_TOKEN_B)) {
                    goLogin();
                } else {
                    goApp();
                }
            }
                    , getString(R.string.update)
                    , getString(R.string.later));

            updateDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            updateDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            updateDialog.setCancelable(false);
            Objects.requireNonNull(updateDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            updateDialog.show();
        });
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