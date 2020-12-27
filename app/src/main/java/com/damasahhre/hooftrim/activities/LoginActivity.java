package com.damasahhre.hooftrim.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.login_fragments.LoginFragment;
import com.damasahhre.hooftrim.activities.login_fragments.SignUpFragment;
import com.damasahhre.hooftrim.adapters.TabAdapter;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.SyncModel;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.server.Requests;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView logo = findViewById(R.id.login_logo);
        ViewPager viewPager = findViewById(R.id.pager_id);
        tabLayout = findViewById(R.id.tab_layout_id);

        adapter = new TabAdapter(this, getSupportFragmentManager());
        adapter.addFragment(new SignUpFragment(), getResources().getString(R.string.sign_in));
        adapter.addFragment(new LoginFragment(), getResources().getString(R.string.login));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setupTabIcons();
        highLightCurrentTab(0);

        tabLayout.selectTab(tabLayout.getTabAt(0), true);


        KeyboardVisibilityEvent.setEventListener(
                this,
                isOpen -> {
                    if (isOpen) {
                        logo.setVisibility(View.GONE);
                    } else {
                        logo.setVisibility(View.VISIBLE);
                    }
                });

    }

    @Override
    public void onBackPressed() {

    }

    /**
     * بازکردن صفحه اصلی
     */
    public void goApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * مقدار دهی اولیه نوار پایین
     */
    private void setupTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }
    }

    /**
     * تغییر رنگ صفحه فعال
     */
    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }

    public void syncData() {
        runOnUiThread(() -> {
            Activity activity = this;
            Toast.makeText(this, R.string.syncStarted, Toast.LENGTH_LONG).show();
            Requests.getAllData(Constants.getToken(this), new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) {
                    try {
                        if (response.isSuccessful()) {
                            JSONObject object = new JSONObject(response.body().string());
                            object.remove("message");
                            GsonBuilder builder = new GsonBuilder();
                            builder.excludeFieldsWithoutExposeAnnotation();
                            Gson gson = builder.create();
                            SyncModel model = gson.fromJson(object.toString(), SyncModel.class);
                            model.doneCreate();
                            MyDao dao = DataBase.getInstance(activity).dao();
                            AppExecutors.getInstance().diskIO().execute(() -> {
                                dao.insertAllFarm(model.farms);
                                dao.insertAllCows(model.cows);
                                dao.insertAllReport(model.reports);
                                activity.runOnUiThread(() -> Toast.makeText(activity, R.string.sync_done, Toast.LENGTH_LONG).show());
                            });
                        } else {
                            Requests.toastMessage(response, activity);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

}