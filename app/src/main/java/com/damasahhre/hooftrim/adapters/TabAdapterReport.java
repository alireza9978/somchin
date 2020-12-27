package com.damasahhre.hooftrim.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.damasahhre.hooftrim.activities.reports.fragments.CowInfoFragment;
import com.damasahhre.hooftrim.activities.reports.fragments.CowInjuryFragment;
import com.damasahhre.hooftrim.activities.reports.fragments.CowReasonFragment;
import com.damasahhre.hooftrim.activities.reports.fragments.MoreInfoFragment;


/**
 * مدیریت کننده اطلاعات در لیست
 * پایین صفحه اصلی
 */
public class TabAdapterReport extends FragmentStateAdapter {

    private Fragment[] fragments = new Fragment[4];
    private int cowNumber;
    private int legAreaNumber;
    private String date;
    private String nextDate;
    private String description;
    private Boolean rightSide;
    private final boolean edit;

    public TabAdapterReport(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.edit = false;
    }

    public TabAdapterReport(@NonNull FragmentActivity fragmentActivity, int cowNumber, String date,
                            String nextDate, int legAreaNumber, Boolean rightSide, String description) {
        super(fragmentActivity);
        this.edit = true;
        this.description = description;
        this.cowNumber = cowNumber;
        this.date = date;
        this.nextDate = nextDate;
        this.legAreaNumber = legAreaNumber;
        this.rightSide = rightSide;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (fragments[position] == null)
            switch (position) {
                case 3: {
                    if (edit) {
                        fragments[3] = new MoreInfoFragment(nextDate, description);
                    } else {
                        fragments[3] = new MoreInfoFragment();
                    }
                    break;
                }
                case 2: {
                    if (edit) {
                        fragments[2] = new CowInjuryFragment(legAreaNumber, rightSide);
                    } else {
                        fragments[2] = new CowInjuryFragment();
                    }
                    break;
                }
                case 1: {
                    fragments[1] = new CowReasonFragment();
                    break;
                }
                case 0: {
                    if (edit) {
                        fragments[0] = new CowInfoFragment();
                        ((CowInfoFragment) fragments[0]).setCowInfoFragment(cowNumber, date);
                    } else {
                        fragments[0] = new CowInfoFragment();
                    }
                    break;
                }
            }
        return fragments[position];
    }

    public Fragment getFragment(int position) {
        if (fragments[position] == null) {
            return createFragment(position);
        }
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}