package com.damasahhre.hooftrim.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class DeletedFarm {

    @SerializedName("front_id")
    @PrimaryKey
    @Expose()
    private Long id;

    public DeletedFarm(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
