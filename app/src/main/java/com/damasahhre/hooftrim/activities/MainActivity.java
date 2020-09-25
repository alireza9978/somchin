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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.menu.ContactActivity;
import com.damasahhre.hooftrim.activities.menu.ProfileActivity;
import com.damasahhre.hooftrim.activities.tabs.SearchFragment;
import com.damasahhre.hooftrim.adapters.TabAdapterHome;
import com.damasahhre.hooftrim.constants.Constants;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabAdapterHome adapter;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager = findViewById(R.id.pager_id);
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
        tabLayout.selectTab(tabLayout.getTabAt(4));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.DATE_SELECTION_SEARCH_COW:
            case Constants.FARM_SELECTION_SEARCH_COW:
            case Constants.DATE_SELECTION_SEARCH_FARM:
                adapter.getFragment(3).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tabLayout.getSelectedTabPosition() == 2) {
            tabLayout.selectTab(tabLayout.getTabAt(4));
        }

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

    public void openMenu() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

}