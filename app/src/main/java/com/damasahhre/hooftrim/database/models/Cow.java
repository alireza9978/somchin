package com.damasahhre.hooftrim.database.models;

import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.damasahhre.hooftrim.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * کلاس نگه دارنده اطلاعات یک گاو
 */
@Entity
public class Cow {

    @Expose()
    @SerializedName("front_id")
    @PrimaryKey
    private Long id;
    @Expose()
    @SerializedName("cow_number")
    @ColumnInfo(name = "number")
    private Integer number;
    @ColumnInfo(name = "number_string")
    private String numberString;
    @Expose()
    @SerializedName("is_favorite")
    @ColumnInfo(name = "favorite")
    private Boolean favorite;
    @Expose()
    @SerializedName("front_farm_id")
    @ColumnInfo(name = "farm_id")
    private Long farm;

    @Expose(serialize = false, deserialize = false)
    @ColumnInfo(name = "sync")
    private Boolean sync;
    @Expose(serialize = false, deserialize = false)
    @ColumnInfo(name = "created")
    private Boolean created;

    public Cow(Integer number, Boolean favorite, Long farm, Boolean sync, Boolean created) {
        this.number = number;
        this.numberString = "" + number;
        this.favorite = favorite;
        this.farm = farm;
        this.sync = sync;
        this.created = created;
    }

    public Boolean getSync() {
        return sync;
    }

    public void setSync(Boolean sync) {
        this.sync = sync;
    }

    public Boolean getCreated() {
        return created;
    }

    public void setCreated(Boolean created) {
        this.created = created;
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

