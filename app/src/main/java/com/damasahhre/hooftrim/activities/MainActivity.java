package com.damasahhre.hooftrim.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.tabs.AddLivestockActivity;
import com.damasahhre.hooftrim.activities.tabs.HomeActivity;
import com.damasahhre.hooftrim.activities.tabs.MarkedActivity;
import com.damasahhre.hooftrim.activities.tabs.ReportsActivity;
import com.damasahhre.hooftrim.activities.tabs.SearchActivity;
import com.damasahhre.hooftrim.adapters.TabAdapterHome;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabAdapterHome adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.pager_id);
        tabLayout = findViewById(R.id.tab_layout_id);
        viewPager.setOffscreenPageLimit(4);

        adapter = new TabAdapterHome(this, getSupportFragmentManager());
        adapter.addFragment(new MarkedActivity(),
                getResources().getString(R.string.report),
                ContextCompat.getDrawable(this, R.drawable.ic_bookmark),
                ContextCompat.getDrawable(this, R.drawable.ic_bookmark_fill));
        adapter.addFragment(new ReportsActivity(),
                getResources().getString(R.string.marked),
                ContextCompat.getDrawable(this, R.drawable.ic_report),
                ContextCompat.getDrawable(this, R.drawable.ic_report_fill));
        adapter.addFragment(AddLivestockActivity.class);
        adapter.addFragment(new SearchActivity(),
                getResources().getString(R.string.search),
                ContextCompat.getDrawable(this, R.drawable.ic_search),
                ContextCompat.getDrawable(this, R.drawable.ic_search_fill));
        adapter.addFragment(new HomeActivity(),
                getResources().getString(R.string.home),
                ContextCompat.getDrawable(this, R.drawable.ic_home),
                ContextCompat.getDrawable(this, R.drawable.ic_home_fill));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        final Context context = this;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
                if (position == 2) {
                    Intent intent = new Intent(context, AddLivestockActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupTabIcons();
        highLightCurrentTab(4);
        tabLayout.selectTab(tabLayout.getTabAt(4), true);
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


}