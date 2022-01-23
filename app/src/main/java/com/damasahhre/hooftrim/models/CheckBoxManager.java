package com.damasahhre.hooftrim.models;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.database.models.Report;

import java.util.ArrayList;

/**
 * مدیریت کننده اطلاعات مربوط به اپشن‌های ورودی در هنگام ثبت گزارش
 */
public class CheckBoxManager {

    private static CheckBoxManager checkBoxManager;
    private final ArrayList<CheckBoxItem> reasons;
    private final ArrayList<CheckBoxItem> moreInfo;
    private final ArrayList<CheckBoxItem> dialog;

    private CheckBoxManager() {
        dialog = new ArrayList<>();
        dialog.add(new CheckBoxItem(R.string.reason_4));
        dialog.add(new CheckBoxItem(R.string.reason_5));
        dialog.add(new CheckBoxItem(R.string.more_info_reason_1));
        dialog.add(new CheckBoxItem(R.string.more_info_reason_2));
        dialog.add(new CheckBoxItem(R.string.more_info_reason_5));
        dialog.add(new CheckBoxItem(R.string.more_info_reason_6));
        dialog.add(new CheckBoxItem(R.string.more_info_reason_3));

        moreInfo = new ArrayList<>();
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_4));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_7));

        reasons = new ArrayList<>();
        reasons.add(new CheckBoxItem(R.string.reason_1));
        reasons.add(new CheckBoxItem(R.string.reason_2));
        reasons.add(new CheckBoxItem(R.string.reason_3));
        reasons.add(new CheckBoxItem(R.string.reason_6));
        reasons.add(new CheckBoxItem(R.string.reason_7));
        reasons.add(new CheckBoxItem(R.string.reason_8));
        reasons.add(new CheckBoxItem(R.string.reason_9));
        reasons.add(new CheckBoxItem(R.string.reason_10));
        reasons.add(new CheckBoxItem(R.string.reason_11));

        reasons.get(0).add(reasons.get(1));//100 roze -> khoshki
        reasons.get(0).add(reasons.get(2));//100 roze -> aghab mande
        reasons.get(1).add(reasons.get(0));//khoshki -> 100
        reasons.get(1).add(reasons.get(2));//khoshki -> aghab mande
        reasons.get(2).add(reasons.get(0));//aghab mande -> 100
        reasons.get(2).add(reasons.get(1));//aghab mande -> khoshki

        dialog.get(0).add(dialog.get(1));//new langesh -> visit langesh
        dialog.get(1).add(dialog.get(0));//visit langesh -> new langesh

        dialog.get(1).add(moreInfo.get(0));//visit langesh -> no injury

        dialog.get(2).add(dialog.get(3));//zakhm -> khon morde
        dialog.get(2).add(dialog.get(6));//zakhm -> behbod yafte
        dialog.get(2).add(moreInfo.get(0));//zakhm -> no injury

        dialog.get(3).add(dialog.get(2));//khon morde -> zakhm
        dialog.get(3).add(dialog.get(4));//khon morde -> jel band
        dialog.get(3).add(moreInfo.get(0));//khon morde -> no injury

        dialog.get(6).add(dialog.get(2));//healed -> wound
        dialog.get(6).add(dialog.get(4));//healed -> jel
        dialog.get(6).add(dialog.get(5));//healed -> bed

        moreInfo.get(0).add(dialog.get(2));//no injury -> wound
        moreInfo.get(0).add(dialog.get(3));//no injury -> khon morde
        moreInfo.get(0).add(dialog.get(4));//no injury -> jel
        moreInfo.get(0).add(dialog.get(5));//no injury -> bed

    }

    public static CheckBoxManager getCheckBoxManager() {
        if (checkBoxManager == null) {
            checkBoxManager = new CheckBoxManager();
        }
        return checkBoxManager;
    }

    public void reset() {
        for (CheckBoxItem item : moreInfo) {
            item.setCheck(false);
            item.setActive(true);
        }
        for (CheckBoxItem item : reasons) {
            item.setCheck(false);
            item.setActive(true);
        }
        for (CheckBoxItem item : dialog) {
            item.setCheck(false);
            item.setActive(true);
        }
    }

    public void resetFastSmall() {
        for (CheckBoxItem item : dialog) {
            item.setCheck(false);
            item.setActive(true);
        }
    }

    public void resetFast() {
//        for (CheckBoxItem item : reasons) {
//            item.setCheck(false);
//            item.setActive(true);
//        }
        for (CheckBoxItem item : dialog) {
            item.setCheck(false);
            item.setActive(true);
        }
    }

    public boolean moreInfoSelected() {
//        در این حالت باید سمچینی انتخاب شود
        return !(dialog.get(2).isCheck() || dialog.get(3).isCheck() || moreInfo.get(1).isCheck());
    }

    public boolean moreInfoFineCow() {
        return dialog.get(6).isCheck();
    }

    public boolean conditionOne() {
//        در صورتی که گزینه ی لنگش جدید یا بازدید علامت می خورد حتما باید یکی از گزینه های زخم یا خونمردگی علامت بخورد
        if (dialog.get(0).isCheck() || dialog.get(1).isCheck()) {
            return dialog.get(2).isCheck() || dialog.get(3).isCheck();
        }
        return true;
    }

    public boolean moreInfoCondition() {
        if (reasons.get(0).isCheck() || reasons.get(1).isCheck() || reasons.get(2).isCheck() || reasons.get(7).isCheck()) {
            return !moreInfo.get(1).isCheck();
        }
        return false;
    }

    public boolean dialogSelected() {
        for (CheckBoxItem item : dialog) {
            if (item.isCheck() && item.isActive()) {
                return true;
            }
        }
        return false;
    }

    public boolean reasonSelected() {
        for (CheckBoxItem item : reasons) {
            if (item.isCheck() && item.isActive()) {
                return true;
            }
        }
        return false;
    }

    public void setBooleansFromReport(Report report) {
        reset();
        reasons.get(0).setCheck(report.referenceCauseHundredDays);
        reasons.get(1).setCheck(report.referenceCauseDryness);
        reasons.get(2).setCheck(report.referenceCauseLagged);
        dialog.get(0).setCheck(report.referenceCauseNewLimp);
        dialog.get(1).setCheck(report.referenceCauseLimpVisit);
        reasons.get(3).setCheck(report.referenceCauseHighScore);
        reasons.get(4).setCheck(report.referenceCauseReferential);
        reasons.get(5).setCheck(report.referenceCauseLongHoof);
        reasons.get(6).setCheck(report.referenceCauseHeifer);
        reasons.get(7).setCheck(report.referenceCauseGroupHoofTrim);
        reasons.get(8).setCheck(report.referenceCauseOther);

        dialog.get(2).setCheck(report.otherInfoWound);
        dialog.get(3).setCheck(report.otherInfoEcchymosis);
        dialog.get(6).setCheck(report.otherInfoRecovered);
        moreInfo.get(0).setCheck(report.otherInfoNoInjury);
        dialog.get(4).setCheck(report.otherInfoGel);
        dialog.get(5).setCheck(report.otherInfoBoarding);
        moreInfo.get(1).setCheck(report.otherInfoHoofTrim);
        for (CheckBoxItem item : reasons) {
            if (item.isCheck()) {
                item.disableOther();
            }
        }
        for (CheckBoxItem item : dialog) {
            if (item.isCheck()) {
                item.disableOther();
            }
        }
        for (CheckBoxItem item : moreInfo) {
            if (item.isCheck()) {
                item.disableOther();
            }
        }
    }

    public void setBooleansOnReport(Report report) {
        report.referenceCauseHundredDays = reasons.get(0).isCheck();
        report.referenceCauseDryness = reasons.get(1).isCheck();
        report.referenceCauseLagged = reasons.get(2).isCheck();
        report.referenceCauseNewLimp = dialog.get(0).isCheck();
        report.referenceCauseLimpVisit = dialog.get(1).isCheck();
        report.referenceCauseHighScore = reasons.get(3).isCheck();
        report.referenceCauseReferential = reasons.get(4).isCheck();
        report.referenceCauseLongHoof = reasons.get(5).isCheck();
        report.referenceCauseHeifer = reasons.get(6).isCheck();
        report.referenceCauseGroupHoofTrim = reasons.get(7).isCheck();
        report.referenceCauseOther = reasons.get(8).isCheck();

        report.otherInfoWound = dialog.get(2).isCheck();
        report.otherInfoEcchymosis = dialog.get(3).isCheck();
        report.otherInfoRecovered = dialog.get(6).isCheck();
        report.otherInfoNoInjury = moreInfo.get(0).isCheck();
        report.otherInfoGel = dialog.get(4).isCheck();
        report.otherInfoBoarding = dialog.get(5).isCheck();
        report.otherInfoHoofTrim = moreInfo.get(1).isCheck();
        reset();
    }

    public void setBooleansOnReportFast(Report report) {
        report.referenceCauseHundredDays = reasons.get(0).isCheck();
        report.referenceCauseDryness = reasons.get(1).isCheck();
        report.referenceCauseLagged = reasons.get(2).isCheck();
        report.referenceCauseNewLimp = dialog.get(0).isCheck();
        report.referenceCauseLimpVisit = dialog.get(1).isCheck();
        report.referenceCauseHighScore = reasons.get(3).isCheck();
        report.referenceCauseReferential = reasons.get(4).isCheck();
        report.referenceCauseLongHoof = reasons.get(5).isCheck();
        report.referenceCauseHeifer = reasons.get(6).isCheck();
        report.referenceCauseGroupHoofTrim = reasons.get(7).isCheck();
        report.referenceCauseOther = reasons.get(8).isCheck();

        report.otherInfoWound = dialog.get(2).isCheck();
        report.otherInfoEcchymosis = dialog.get(3).isCheck();
        report.otherInfoRecovered = dialog.get(6).isCheck();
        report.otherInfoNoInjury = moreInfo.get(0).isCheck();
        report.otherInfoGel = dialog.get(4).isCheck();
        report.otherInfoBoarding = dialog.get(5).isCheck();
        report.otherInfoHoofTrim = moreInfo.get(1).isCheck();
        resetFast();
    }


    public ArrayList<CheckBoxItem> getReasons() {
        return reasons;
    }

    public ArrayList<CheckBoxItem> getMoreInfo() {
        return moreInfo;
    }

    public ArrayList<CheckBoxItem> getDialog() {
        return dialog;
    }
}
