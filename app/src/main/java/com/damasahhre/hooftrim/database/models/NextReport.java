package com.damasahhre.hooftrim.database.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.damasahhre.hooftrim.models.MyDate;

import java.util.Date;
import java.util.List;

public class NextReport {
    public MyDate nextVisitDate;
    public Integer cowNumber;
    public String farmName;
}

