package com.damasahhre.hooftrim.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.menu.AboutActivity;
import com.damasahhre.hooftrim.activities.menu.ContactActivity;
import com.damasahhre.hooftrim.activities.menu.ProfileActivity;
import com.damasahhre.hooftrim.adapters.TabAdapterHome;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.DeletedSyncModel;
import com.damasahhre.hooftrim.database.models.SyncModel;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.server.Requests;
import com.damasahhre.hooftrim.service.MyNotificationPublisher;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import static android.os.Build.VERSION.SDK_INT;
import static com.damasahhre.hooftrim.constants.Constants.CHOOSE_FILE_REQUEST_CODE;
import static com.damasahhre.hooftrim.constants.Constants.DATE_SELECTION_REPORT_FACTOR;
import static com.damasahhre.hooftrim.constants.Constants.DATE_SELECTION_REPORT_INJURY;
import static com.damasahhre.hooftrim.constants.Constants.FARM_SELECTION_REPORT_FACTOR;
import static com.damasahhre.hooftrim.constants.Constants.FARM_SELECTION_REPORT_INJURY;
import static com.damasahhre.hooftrim.constants.Constants.getDefaultLanguage;
import static com.damasahhre.hooftrim.constants.Constants.setPremium;

/**
 * صفحه اصلی برنامه برای مدیریت ۵ صفحه‌ی پایین برنامه
 * مدیریت ناتیفیکیشن
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabAdapterHome adapter;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;

    public static void applyFontToMenu(Menu m, Context mContext) {
        for (int i = 0; i < m.size(); i++) {
            applyFontToMenuItem(m.getItem(i), mContext);
        }
    }

    public static void applyFontToMenuItem(MenuItem mi, Context mContext) {
        if (mi.hasSubMenu())
            for (int i = 0; i < mi.getSubMenu().size(); i++) {
                applyFontToMenuItem(mi.getSubMenu().getItem(i), mContext);
            }
        Typeface font = ResourcesCompat.getFont(mContext, R.font.anjoman_medium);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());

        mNewTitle.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.hit_gray)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public void hideKeyboard() {
        Constants.hideKeyboard(this, findViewById(R.id.root).getWindowToken());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager = findViewById(R.id.pager_id);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setUserInputEnabled(false);
        NavigationView navigationView = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        tabLayout = findViewById(R.id.tab_layout_id);
        adapter = new TabAdapterHome(this, tabLayout, viewPager);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(position));
        }).attach();

        applyFontToMenu(navigationView.getMenu(), this);
        navigationView.setNavigationItemSelectedListener(this);
        tabLayout.selectTab(tabLayout.getTabAt(0));

        Activity activity = this;
        if (Constants.isNetworkAvailable(this)) {
            Requests.isPaid(Constants.getToken(this), new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(() -> Toast.makeText(activity, R.string.request_error, Toast.LENGTH_LONG).show());
                }

                @Override
                public void onResponse(Response response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            boolean isPremium = (boolean) jsonObject.get("is_premium");
                            setPremium(activity, isPremium);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Requests.toastMessage(response, activity);
                        if (response.code() == 403) {
                            Log.i("MAIN", "onResponse: invalid token");
                            MyDao dao = DataBase.getInstance(activity).dao();
                            AppExecutors.getInstance().diskIO().execute(() -> {
                                dao.deleteAllFarm();
                                dao.deleteAllCow();
                                dao.deleteAllReport();
                                dao.deleteAllOtherFarm();
                                dao.deleteAllOtherCow();
                                dao.deleteAllOtherReport();
                                runOnUiThread(() -> {
                                    Constants.setToken(activity, Constants.NO_TOKEN);
                                    Constants.setEmail(activity, Constants.NO_EMAIL);
                                    Intent intent = new Intent(activity, SplashActivity.class);
                                    startActivity(intent);
                                    finish();
                                });
                            });
                        }
                    }
                }
            });
        }

        SwitchCompat switchCompat = navigationView.getMenu().getItem(2).getActionView().findViewById(R.id.switch_id);
        if (switchCompat != null) {
            switchCompat.setChecked(Constants.getNotificationStatus(this));
            switchCompat.setClickable(false);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.OPEN_PROFILE: {
                if (resultCode == Constants.PASSWORD_CHANGED) {
                    logout();
                } else {
                    Log.i("MAIN", "onActivityResult: " + resultCode);
                }
                break;
            }
            case Constants.DATE_SELECTION_SEARCH_COW:
            case Constants.FARM_SELECTION_SEARCH_COW:
            case Constants.DATE_SELECTION_SEARCH_FARM:
                adapter.getFragment(1).onActivityResult(requestCode, resultCode, data);
                break;

            case DATE_SELECTION_REPORT_INJURY:
            case FARM_SELECTION_REPORT_INJURY:
            case DATE_SELECTION_REPORT_FACTOR:
            case FARM_SELECTION_REPORT_FACTOR: {
                adapter.getFragment(3).onActivityResult(requestCode, resultCode, data);
                break;
            }
            case 2296: {
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        // perform action when allow permission success
                    } else {
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            default: {
                adapter.getFragment(3).onActivityResult(CHOOSE_FILE_REQUEST_CODE, resultCode, data);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tabLayout.getSelectedTabPosition() == 2) {
            tabLayout.selectTab(tabLayout.getTabAt(0));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.username: {
                if (!Constants.isNetworkAvailable(this)) {
                    Toast.makeText(this, R.string.connection_required, Toast.LENGTH_SHORT).show();
                    return true;
                }
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivityForResult(intent, Constants.OPEN_PROFILE);
                return true;
            }
            case R.id.contact: {
                Intent intent = new Intent(this, ContactActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.about_us: {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.lang: {
                if (getDefaultLanguage(this).equals("fa")) {
                    Constants.setLanguage(this, "en");
                } else {
                    Constants.setLanguage(this, "fa");
                }
                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            case R.id.sync: {
                if (!Constants.isNetworkAvailable(this)) {
                    Toast.makeText(this, R.string.connection_required, Toast.LENGTH_SHORT).show();
                    return true;
                }
                Activity activity = this;
                MyDao dao = DataBase.getInstance(this).dao();
                AppExecutors.getInstance().diskIO().execute(() -> {
                    SyncModel model = new SyncModel(dao.getAllFarmToSync(), dao.getAllCowToSync(), dao.getAllReportToSync());
                    if (!model.isEmpty()) {
                        SyncModel finalModel = model;
                        Requests.update(Constants.getToken(getApplicationContext()), model, new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {

                            }

                            @Override
                            public void onResponse(Response response) {
                                if (response.isSuccessful()) {
                                    finalModel.doneUpdate();
                                    AppExecutors.getInstance().diskIO().execute(() -> {
                                        dao.doneSyncFarm(finalModel.farms);
                                        dao.doneSyncCow(finalModel.cows);
                                        dao.doneSyncReport(finalModel.reports);
                                        runOnUiThread(() -> Toast.makeText(activity, R.string.sync_done, Toast.LENGTH_LONG).show());
                                    });
                                } else {
                                    Requests.toastMessage(response, activity);
                                }
                            }
                        });
                    }

                    model = new SyncModel(dao.getAllNewFarmToSync(), dao.getAllNewCowToSync(), dao.getAllNewReportToSync());
                    if (!model.isEmpty()) {
                        SyncModel finalModel = model;
                        Requests.setContext(MainActivity.this);
                        Requests.create(Constants.getToken(getApplicationContext()), model, new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {

                            }

                            @Override
                            public void onResponse(Response response) {
                                if (response.isSuccessful()) {
                                    finalModel.doneCreate();
                                    AppExecutors.getInstance().diskIO().execute(() -> {
                                        dao.doneSyncFarm(finalModel.farms);
                                        dao.doneSyncCow(finalModel.cows);
                                        dao.doneSyncReport(finalModel.reports);
                                        runOnUiThread(() -> Toast.makeText(activity, R.string.sync_done, Toast.LENGTH_LONG).show());
                                    });
                                } else {
                                    Requests.toastMessage(response, activity);
                                }
                            }
                        });
                    }

                    DeletedSyncModel deletedSyncModel = new DeletedSyncModel(dao.getAllDeletedFarmToSync(), dao.getAllDeletedCowToSync(), dao.getAllDeletedReportToSync());
                    if (!deletedSyncModel.isEmpty()) {
                        Requests.delete(Constants.getToken(getApplicationContext()), deletedSyncModel, new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {

                            }

                            @Override
                            public void onResponse(Response response) {
                                if (response.isSuccessful()) {
                                    AppExecutors.getInstance().diskIO().execute(() -> {
                                        dao.doneDeleteFarm(deletedSyncModel.farms);
                                        dao.doneDeleteCow(deletedSyncModel.cows);
                                        dao.doneDeleteReport(deletedSyncModel.reports);
                                        runOnUiThread(() -> Toast.makeText(activity, R.string.sync_done, Toast.LENGTH_LONG).show());
                                    });
                                } else {
                                    Requests.toastMessage(response, activity);
                                }
                            }
                        });
                    }
                });
                return true;
            }
            case R.id.logout: {
                return logout();
            }
            case R.id.nav_switch: {
                SwitchCompat switchCompat = item.getActionView().findViewById(R.id.switch_id);
                if (!Constants.getPremium(this)) {
                    switchCompat.setChecked(false);
                    Toast.makeText(this, R.string.premium_require, Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    if (switchCompat.isChecked()) {
                        switchCompat.setChecked(false);
                        Constants.setNotificationStatus(this, false);
                        cancelSchedule();
                    } else {
                        switchCompat.setChecked(true);
                        Constants.setNotificationStatus(this, true);
                        scheduleNotification();
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void openMenu() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> drawerLayout.openDrawer(GravityCompat.START), 200);
    }

    private void cancelSchedule() {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, notificationIntent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

    }

    private void scheduleNotification() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();

        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static class CustomTypefaceSpan extends TypefaceSpan {

        private final Typeface newType;

        public CustomTypefaceSpan(String family, Typeface type) {
            super(family);
            newType = type;
        }

        private static void applyCustomTypeFace(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }

            paint.setTypeface(tf);
        }

        @Override
        public void updateDrawState(@NotNull TextPaint ds) {
            applyCustomTypeFace(ds, newType);
        }

        @Override
        public void updateMeasureState(@NotNull TextPaint paint) {
            applyCustomTypeFace(paint, newType);
        }
    }

    private boolean logout() {
        if (!Constants.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.connection_required, Toast.LENGTH_SHORT).show();
            return true;
        }
        Activity activity = this;
        Requests.logout(Constants.getToken(this), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(() -> Toast.makeText(activity, R.string.request_error, Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    MyDao dao = DataBase.getInstance(activity).dao();
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        dao.deleteAllFarm();
                        dao.deleteAllCow();
                        dao.deleteAllReport();
                        dao.deleteAllOtherFarm();
                        dao.deleteAllOtherCow();
                        dao.deleteAllOtherReport();
                        runOnUiThread(() -> {
                            Constants.setToken(activity, Constants.NO_TOKEN);
                            Constants.setEmail(activity, Constants.NO_EMAIL);
                            Constants.setPremium(activity, false);
                            Constants.setNotificationStatus(activity, false);
                            cancelSchedule();
                            Intent intent = new Intent(activity, SplashActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    });
                } else {
                    Requests.toastMessage(response, activity);
                }
            }
        });
        return true;
    }

}