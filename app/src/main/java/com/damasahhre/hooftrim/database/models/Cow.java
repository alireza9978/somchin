package com.damasahhre.hooftrim.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cow {
    @PrimaryKey
    public Integer id;
    @ColumnInfo(name = "number")
    public Integer number;
    @ColumnInfo(name = "farm_id")
    public Integer farm;
}

