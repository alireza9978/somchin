package com.damasahhre.hooftrim.models;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;

import com.damasahhre.hooftrim.constants.Utilities;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;

import saman.zamani.persiandate.PersianDate;

public class MyDate implements Serializable, Cloneable, Comparable<MyDate> {

    private int day;
    private int month;
    private int year;

    public MyDate(Date date) {
        day = date.getDate();
        month = date.getMonth() + 1;
        year = date.getYear() + 1900;
    }

    public MyDate(Long temp) {
        Log.i("TAG", "MyDate: " + temp);
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

    @NotNull
    public String toString(Context context) {
        Configuration config = context.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            PersianDate pdate = new PersianDate();
            int[] temp = pdate.toJalali(year, month, day);
            return "" + temp[0] + "/" + temp[1] + "/" + temp[2];
        } else {
            return "" + year + "/" + month + "/" + day;
        }
    }

    @NotNull
    public String toStringWithoutYear(Context context) {
        Configuration config = context.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            PersianDate pdate = new PersianDate();
            int[] temp = pdate.toJalali(year, month, day);
            return Utilities.getMonthName(context, temp[1]) + " " + temp[2];
        } else {
            return Utilities.getMonthName(context, month) + " " + day;
        }
    }

}
