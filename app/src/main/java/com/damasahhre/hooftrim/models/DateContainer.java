package com.damasahhre.hooftrim.models;

import android.content.Context;

import com.damasahhre.hooftrim.constants.FormatHelper;
import com.damasahhre.hooftrim.constants.Utilities;

import java.io.Serializable;
import java.util.Date;

import saman.zamani.persiandate.PersianDate;

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

    public static String toString(com.damasahhre.hooftrim.models.MyDate date) {
        if (date.compareTo(new com.damasahhre.hooftrim.models.MyDate(new Date())) == 0) {
            return "today";
        } else
            return "other_day";

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

    public String toStringSmall(Context context) {
        if (mode.equals(RANG)) {
            if (startDate.equals(endDate)) {
                return startDate.toStringWithoutYear(context);
            }
            return startDate.toStringWithoutYear(context) + "~" + endDate.toStringWithoutYear(context);
        } else {
            return startDate.toStringWithoutYear(context);
        }
    }

    public String toString(Context context) {
        if (mode.equals(RANG)) {
            if (startDate.equals(endDate)) {
                return startDate.toString(context);
            }
            return startDate.toString(context) + " " + endDate.toString(context);
        } else {
            return startDate.toString(context);
        }
    }

    public String toStringBeauty(Context context) {
        if (mode.equals(RANG)) {
            if (startDate.equals(endDate)) {
                return startDate.toString(context);
            }
            return "(" + startDate.toStringComa(context) + "~" + endDate.toStringComa(context) + ")";
        } else {
            return "(" + startDate.toStringComa(context) + ")";
        }
    }

    public com.damasahhre.hooftrim.models.MyDate exportStart() {
        if (startDate.persian) {
            PersianDate pdate = new PersianDate();
            int[] temp = pdate.toGregorian(startDate.year, startDate.month, startDate.day);
            return new com.damasahhre.hooftrim.models.MyDate(temp[2], temp[1], temp[0]);
        } else {
            return new com.damasahhre.hooftrim.models.MyDate(startDate.day, startDate.month, startDate.year);
        }
    }

    public com.damasahhre.hooftrim.models.MyDate exportEnd() {
        if (endDate.persian) {
            PersianDate pdate = new PersianDate();
            int[] temp = pdate.toGregorian(startDate.year, startDate.month, startDate.day);
            return new com.damasahhre.hooftrim.models.MyDate(temp[2], temp[1], temp[0]);
        } else {
            return new com.damasahhre.hooftrim.models.MyDate(endDate.day, endDate.month, endDate.year);
        }
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

        public boolean equals(MyDate obj) {
            return day == obj.day && month == obj.month && year == obj.year;
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

        public String toStringComa(Context context) {
            String out = "";
            if (persian) {
                out = FormatHelper.toPersianNumber("" + year);
            } else {
                out += year;
            }
            out = out + "," + Utilities.getMonthName(context, month);
            if (persian) {
                out = out + "," + FormatHelper.toPersianNumber("" + day);
            } else {
                out = out + "," + day;
            }
            return out;
        }

        public String toStringWithoutYear(Context context) {
            String out = Utilities.getMonthName(context, month);
            if (persian) {
                out = out + " " + FormatHelper.toPersianNumber("" + day);
            } else {
                out = out + " " + day;
            }
            return out;
        }

    }
}
