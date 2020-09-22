package com.damasahhre.hooftrim.models;

import android.content.Context;

import com.damasahhre.hooftrim.constants.FormatHelper;
import com.damasahhre.hooftrim.constants.Utilities;

import java.io.Serializable;

import static com.damasahhre.hooftrim.constants.Constants.DateSelectionMode.RANG;

public class DateContainer implements Serializable {

    private String mode;
    private MyDate startDate;
    private MyDate endDate;

    public DateContainer(String mode, MyDate startDate) {
        this.mode = mode;
        this.startDate = startDate;
    }

    public DateContainer(String mode, MyDate startDate, MyDate endDate) {
        this.mode = mode;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public MyDate getStartDate() {
        return startDate;
    }

    public void setStartDate(MyDate startDate) {
        this.startDate = startDate;
    }

    public MyDate getEndDate() {
        return endDate;
    }

    public void setEndDate(MyDate endDate) {
        this.endDate = endDate;
    }

    public static class MyDate implements Serializable {
        boolean persian;
        int day;
        int month;
        int year;

        public MyDate(boolean persian, int day, int month, int year) {
            this.persian = persian;
            this.day = day;
            this.month = month;
            this.year = year;
        }

        public String toString(Context context) {
            String out = "";
            if (persian) {
                out = FormatHelper.toPersianNumber("" + year);
            } else {
                out += year;
            }
            out = out + " " + Utilities.getMonthName(context, month);
            if (persian) {
                out = out + " " + FormatHelper.toPersianNumber("" + day);
            } else {
                out = out + " " + day;
            }
            return out;
        }
    }

    public String toString(Context context) {
        if (mode.equals(RANG)) {
            return startDate.toString(context) + " " + endDate.toString(context);
        } else {
            return startDate.toString(context);
        }
    }
}
