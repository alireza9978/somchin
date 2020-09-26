package com.damasahhre.hooftrim.activities.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.MainActivity;
import com.damasahhre.hooftrim.activities.tabs.report_activites.FactorFragment;
import com.damasahhre.hooftrim.activities.tabs.report_activites.ImportFragment;
import com.damasahhre.hooftrim.activities.tabs.report_activites.InjuriesFragment;
import com.damasahhre.hooftrim.activities.tabs.report_activites.ReportVisitFragment;
import com.damasahhre.hooftrim.adapters.TabAdapterLongText;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.models.DateContainer;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class ReportsFragment extends Fragment {


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
        viewPager.setOffscreenPageLimit(4);

        adapter = new TabAdapterLongText(requireContext(), requireActivity().getSupportFragmentManager());
        adapter.addFragment(new ImportFragment(), getResources().getString(R.string.import_file));
        adapter.addFragment(new ReportVisitFragment(), getResources().getString(R.string.visits));
        adapter.addFragment(new FactorFragment(), getResources().getString(R.string.facor));
        adapter.addFragment(new InjuriesFragment(), getResources().getString(R.string.injeries));

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case Constants.FARM_SELECTION_REPORT_FACTOR: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    int id = Objects.requireNonNull(data.getExtras()).getInt(Constants.FARM_ID);
                    ((FactorFragment) adapter.getItem(2)).setFarm(id);
                }
                break;
            }
            case Constants.DATE_SELECTION_REPORT_FACTOR: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    DateContainer container = (DateContainer) Objects.requireNonNull(data.getExtras()).get(Constants.DATE_SELECTION_RESULT);
                    assert container != null;
                    ((FactorFragment) adapter.getItem(2)).setDate(container);
                }
            }
            case Constants.FARM_SELECTION_REPORT_INJURY: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    int id = Objects.requireNonNull(data.getExtras()).getInt(Constants.FARM_ID);
                    ((FactorFragment) adapter.getItem(3)).setFarm(id);
                }
                break;
            }
            case Constants.DATE_SELECTION_REPORT_INJURY: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    DateContainer container = (DateContainer) Objects.requireNonNull(data.getExtras()).get(Constants.DATE_SELECTION_RESULT);
                    assert container != null;
                    ((FactorFragment) adapter.getItem(3)).setDate(container);
                }
            }
        }
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