package com.damasahhre.hooftrim.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.tabs.AddLivestockActivity;
import com.damasahhre.hooftrim.activities.tabs.HomeActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * مدیریت کننده اطلاعات در لیست
 * پایین صفحه اصلی
 */
public class TabAdapterHome extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final List<Drawable> mFragmentImageList = new ArrayList<>();
    private final List<Drawable> mFragmentSelectedImageList = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private Class<?> startClass;

    public TabAdapterHome(Context context, FragmentManager fm) {
        super(fm);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title, Drawable normal, Drawable selected) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        mFragmentImageList.add(normal);
        mFragmentSelectedImageList.add(selected);
    }

    public void addFragment(Class<?> temp) {
        startClass = temp;
        mFragmentList.add(new HomeActivity());
        mFragmentTitleList.add("title");
        mFragmentImageList.add(null);
        mFragmentSelectedImageList.add(null);
    }

    public View getTabView(int position) {
        if (position == 2) {
            return inflater.inflate(R.layout.home_tab_layout_center, null);

        }
        View view = inflater.inflate(R.layout.home_tab_layout, null);
        view.setTag(mFragmentTitleList.get(position));
        ImageView image = view.findViewById(R.id.item_image);
        image.setImageDrawable(mFragmentImageList.get(position));
        image.setColorFilter(R.color.tab_home);
        return view;
    }

    public View getSelectedTabView(int position) {
        if (position == 2) {
            return inflater.inflate(R.layout.home_tab_layout_center, null);
        }
        View view = inflater.inflate(R.layout.home_tab_layout_selected, null);
        TextView name = view.findViewById(R.id.item_name);
        name.setText(mFragmentTitleList.get(position));
        ImageView image = view.findViewById(R.id.item_image);
        image.setImageDrawable(mFragmentSelectedImageList.get(position));
        image.setColorFilter(R.color.selected_tab_home);
        return view;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


}