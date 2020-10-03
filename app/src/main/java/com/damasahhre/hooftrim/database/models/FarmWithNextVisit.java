package com.damasahhre.hooftrim.database.models;

import androidx.room.Embedded;

import com.damasahhre.hooftrim.models.MyDate;

public class FarmWithNextVisit {
    @Embedded
    public Farm farm;
    public MyDate nextVisit;


}
