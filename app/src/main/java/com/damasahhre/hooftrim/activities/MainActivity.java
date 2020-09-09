package com.damasahhre.hooftrim.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.menu.ContactActivity;
import com.damasahhre.hooftrim.activities.menu.ProfileActivity;
import com.damasahhre.hooftrim.activities.tabs.AddLivestockActivity;
import com.damasahhre.hooftrim.activities.tabs.HomeActivity;
import com.damasahhre.hooftrim.activities.tabs.MarkedActivity;
import com.damasahhre.hooftrim.activities.tabs.ReportsActivity;
import com.damasahhre.hooftrim.activities.tabs.SearchActivity;
import com.damasahhre.hooftrim.adapters.TabAdapterHome;
import com.damasahhre.hooftrim.ui_element.MyViewPager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabAdapterHome adapter;
    private TabLayout tabLayout;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyViewPager viewPager = findViewById(R.id.pager_id);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation);
        tabLayout = findViewById(R.id.tab_layout_id);

        viewPager.setEnableSwipe(false);
        viewPager.setOffscreenPageLimit(1);

        adapter = new TabAdapterHome(this, getSupportFragmentManager());
        adapter.addFragment(new MarkedActivity(),
                getResources().getString(R.string.marked),
                ContextCompat.getDrawable(this, R.drawable.ic_bookmark),
                ContextCompat.getDrawable(this, R.drawable.ic_bookmark_fill));
        adapter.addFragment(new ReportsActivity(),
                getResources().getString(R.string.report),
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

        applyFontToMenu(navigationView.getMenu(), this);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_us:
            case R.id.user_guid:
                return true;
            case R.id.username:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.contact:
                Intent i = new Intent(this, ContactActivity.class);
                startActivity(i);
                return true;
        }
        return false;
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
        public void updateDrawState(TextPaint ds) {
            applyCustomTypeFace(ds, newType);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyCustomTypeFace(paint, newType);
        }
    }

    public void openMenu(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

}