package com.damasahhre.hooftrim.database.models;

import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.damasahhre.hooftrim.R;

@Entity
public class Cow {
    @PrimaryKey
    private Long id;
    @ColumnInfo(name = "number")
    private Integer number;
    @ColumnInfo(name = "number_string")
    private String numberString;
    @ColumnInfo(name = "favorite")
    private Boolean favorite;
    @ColumnInfo(name = "farm_id")
    private Long farm;
    @ColumnInfo(name = "sync")
    private Boolean sync;

    public Boolean getSync() {
        return sync;
    }

    public void setSync(Boolean sync) {
        this.sync = sync;
    }

    public Cow(Integer number, Boolean favorite, Long farm, Boolean sync) {
        this.number = number;
        this.numberString = "" + number;
        this.favorite = favorite;
        this.farm = farm;
        this.sync = sync;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getNumber(Context context) {
        return context.getString(R.string.cow_title) + number;
    }

    public Long getFarm() {
        return farm;
    }

    public void setFarm(Long farm) {
        this.farm = farm;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getNumberString() {
        return numberString;
    }

    public void setNumberString(String numberString) {
        this.numberString = numberString;
    }

    @Override
    public String toString() {
        return "Cow{" +
                "id=" + id +
                ", number=" + number +
                ", numberString='" + numberString + '\'' +
                ", favorite=" + favorite +
                ", farm=" + farm +
                ", sync=" + sync +
                '}';
    }
}

