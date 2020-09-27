package com.damasahhre.hooftrim.models;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.database.models.Report;

import java.util.ArrayList;

public class CheckBoxManager {

    private static CheckBoxManager checkBoxManager;
    private ArrayList<CheckBoxItem> reasons;
    private ArrayList<CheckBoxItem> moreInfo;

    private CheckBoxManager() {
        moreInfo = new ArrayList<>();
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_1));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_2));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_3));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_4));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_5));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_6));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_7));
        moreInfo.get(0).add(moreInfo.get(1));
        moreInfo.get(0).add(moreInfo.get(2));
        moreInfo.get(0).add(moreInfo.get(3));
        moreInfo.get(1).add(moreInfo.get(0));
        moreInfo.get(1).add(moreInfo.get(3));
        moreInfo.get(1).add(moreInfo.get(4));
        moreInfo.get(2).add(moreInfo.get(0));
        moreInfo.get(2).add(moreInfo.get(4));
        moreInfo.get(2).add(moreInfo.get(5));
        moreInfo.get(3).add(moreInfo.get(0));
        moreInfo.get(3).add(moreInfo.get(1));
        moreInfo.get(3).add(moreInfo.get(4));
        moreInfo.get(3).add(moreInfo.get(5));

        reasons = new ArrayList<>();
        reasons.add(new CheckBoxItem(R.string.reason_1));
        reasons.add(new CheckBoxItem(R.string.reason_2));
        reasons.add(new CheckBoxItem(R.string.reason_3));
        reasons.add(new CheckBoxItem(R.string.reason_4));
        reasons.add(new CheckBoxItem(R.string.reason_5));
        reasons.add(new CheckBoxItem(R.string.reason_6));
        reasons.add(new CheckBoxItem(R.string.reason_7));
        reasons.add(new CheckBoxItem(R.string.reason_8));
        reasons.add(new CheckBoxItem(R.string.reason_9));
        reasons.add(new CheckBoxItem(R.string.reason_10));
        reasons.get(0).add(reasons.get(1));
        reasons.get(0).add(reasons.get(2));
        reasons.get(1).add(reasons.get(0));
        reasons.get(1).add(reasons.get(2));
        reasons.get(2).add(reasons.get(0));
        reasons.get(2).add(reasons.get(1));
        reasons.get(3).add(reasons.get(4));
        reasons.get(4).add(reasons.get(3));

    }

    public boolean moreInfoSelected(){
        return moreInfo.get(0).isCheck() || moreInfo.get(1).isCheck() || moreInfo.get(6).isCheck();
    }

    public boolean reasonSelected() {
        for (CheckBoxItem item : reasons) {
            if (item.isCheck() && item.isActive()) {
                return true;
            }
        }
        return false;
    }

    public void setBooleans(Report report){
        //todo set all boolean
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

    public ArrayList<CheckBoxItem> getMoreInfo() {
        return moreInfo;
    }
}
