package com.damasahhre.hooftrim.models;

import java.io.Serializable;
import java.util.Date;

public class MyDate implements Serializable, Cloneable, Comparable<MyDate> {

    private int day;
    private int month;
    private int year;

    public MyDate(Date date){
        day = date.getDay();
        month = date.getMonth();
        year = date.getYear();
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
}
