package com.damasahhre.hooftrim.activities.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.MainActivity;
import com.damasahhre.hooftrim.activities.tabs.report_activites.FactorFragment;
import com.damasahhre.hooftrim.activities.tabs.report_activites.ImportFragment;
import com.damasahhre.hooftrim.activities.tabs.report_activites.InjeriesFragment;
import com.damasahhre.hooftrim.activities.tabs.report_activites.ReportVisitFragment;
import com.damasahhre.hooftrim.adapters.TabAdapter;
import com.damasahhre.hooftrim.adapters.TabAdapterLongText;
import com.google.android.material.tabs.TabLayout;

public class ReportsActivity extends Fragment {


    private TabAdapterLongText adapter;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reports, container, false);
        view.findViewById(R.id.menu_button).setOnClickListener(v -> {
            ((MainActivity) requireActivity()).openMenu();
        });
        ViewPager viewPager = view.findViewById(R.id.report_pager_id);
        tabLayout = view.findViewById(R.id.report_tab_layout_id);
        viewPager.setOffscreenPageLimit(3);

        adapter = new TabAdapterLongText(requireContext(), requireActivity().getSupportFragmentManager());
        adapter.addFragment(new ImportFragment(), getResources().getString(R.string.import_file));
        adapter.addFragment(new ReportVisitFragment(), getResources().getString(R.string.visits));
        adapter.addFragment(new FactorFragment(), getResources().getString(R.string.facor));
        adapter.addFragment(new InjeriesFragment(), getResources().getString(R.string.injeries));

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

        return view;
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