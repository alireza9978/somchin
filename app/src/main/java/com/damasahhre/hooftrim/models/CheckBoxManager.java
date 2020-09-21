package com.damasahhre.hooftrim.models;

import com.damasahhre.hooftrim.R;

import java.util.ArrayList;

public class CheckBoxManager {

    private static CheckBoxManager checkBoxManager;
    private ArrayList<CheckBoxItem> reasons;

    private CheckBoxManager() {
        reasons = new ArrayList<>();
        reasons.add(new CheckBoxItem(R.string.reason_1));
        reasons.add(new CheckBoxItem(R.string.reason_2));
        reasons.add(new CheckBoxItem(R.string.reason_3));
        reasons.add(new CheckBoxItem(R.string.reason_4));
        reasons.add(new CheckBoxItem(R.string.reason_5));
        reasons.add(new CheckBoxItem(R.string.reason_6));
        reasons.add(new CheckBoxItem(R.string.reason_7));
        reasons.get(0).add(reasons.get(1));
        reasons.get(0).add(reasons.get(2));
        reasons.get(0).add(reasons.get(3));
        reasons.get(1).add(reasons.get(6));
        reasons.get(2).add(reasons.get(5));
    }

    public static CheckBoxManager getCheckBoxManager() {
        if (checkBoxManager == null) {
            checkBoxManager = new CheckBoxManager();
        }
        return checkBoxManager;
    }

    public ArrayList<CheckBoxItem> getReasons() {
        return reasons;
    }
}
