package com.damasahhre.hooftrim.database.models;

import androidx.room.ColumnInfo;

public class FarmWithCowCount {

    public Long farmId;
    public String farmName;

    @ColumnInfo(name = "cow_count")
    public Integer cowCount;

}
