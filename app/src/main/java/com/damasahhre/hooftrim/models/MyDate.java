package com.damasahhre.hooftrim.models;

import android.content.Context;

import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.constants.Utilities;
import com.google.gson.annotations.Expose;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import saman.zamani.persiandate.PersianDate;

/**
 * کلاس نگهدارنده‌ی اطلاعات تاریخ
 */
public class MyDate implements Serializable, Cloneable, Comparable<MyDate> {

    @Expose
    private int day;
    @Expose
    private int month;
    @Expose
    private int year;

    public MyDate(Date date) {
        day = date.getDate();
        month = date.getMonth() + 1;
        year = date.getYear() + 1900;
    }

    public MyDate(Long temp) {
        String tempDate = "" + temp;
        year = Integer.parseInt(tempDate.substring(0, 4));
        month = Integer.parseInt(tempDate.substring(4, 6));
        day = Integer.parseInt(tempDate.substring(6, 8));
    }

    public MyDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public boolean isToday() {
        return compareTo(new MyDate(new Date())) == 0;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Long toLong() {
        String temp = String.format("%04d%02d%02d", year, month, day);
        return Long.parseLong(temp);
    }

    @Override
    public int compareTo(MyDate myDate) {
        if (this.year != myDate.year) {
            return this.year - myDate.year;
        } else {
            if (this.month != myDate.month) {
                return this.month - myDate.month;
            } else {
                if (this.day != myDate.day) {
                    return this.day - myDate.day;
                } else {
                    return 0;
                }
            }
        }
    }

    @NotNull
    @Override
    public String toString() {
        return "" + year + "/" + month + "/" + day;
    }

    public String convertDay(Context context) {
        if (Constants.getDefaultLanguage(context).equals("fa")) {
            PersianDate pdate = new PersianDate();
            int[] temp = pdate.gregorian_to_jalali(year, month, day);
            return "" + temp[2];
        } else {
            return "" + day;
        }
    }

    public int[] convert(Context context) {
        if (Constants.getDefaultLanguage(context).equals("fa")) {
            PersianDate pdate = new PersianDate();
            return pdate.gregorian_to_jalali(year, month, day);
        } else {
            return new int[]{year, month, day};
        }
    }

    @NotNull
    public String toString(Context context) {
        if (Constants.getDefaultLanguage(context).equals("fa")) {
            PersianDate pdate = new PersianDate();
            int[] temp = pdate.gregorian_to_jalali(year, month, day);
            String day = String.format("%02d", temp[2]);
            String month = String.format("%02d", temp[1]);
            String year = String.format("%04d", temp[0]);
            return year + "/" + month + "/" + day;
        } else {
            String day = String.format("%02d", this.day);
            String month = String.format("%02d", this.month);
            String year = String.format("%04d", this.year);

            return year + "/" + month + "/" + day;
        }
    }

    @NotNull
    public String toStringWithoutYear(Context context) {
        if (Constants.getDefaultLanguage(context).equals("fa")) {
            PersianDate pdate = new PersianDate();
            int[] temp = pdate.gregorian_to_jalali(year, month, day);
            return Utilities.getMonthName(context, temp[1]) + " " + temp[2];
        } else {
            return Utilities.getMonthName(context, month) + " " + day;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyDate myDate = (MyDate) o;
        return day == myDate.day &&
                month == myDate.month &&
                year == myDate.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }
}
