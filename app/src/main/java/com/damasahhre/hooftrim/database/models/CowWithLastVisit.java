package com.damasahhre.hooftrim.database.models;

import android.content.Context;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.models.MyDate;

public class CowWithLastVisit {

    private Integer id;
    private Integer number;
    private MyDate lastVisit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public String getNumber(Context context) {
        return context.getString(R.string.cow_title) + number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public MyDate getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(MyDate lastVisit) {
        this.lastVisit = lastVisit;
    }
}
