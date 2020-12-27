package com.damasahhre.hooftrim.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class DeletedReport {
    @SerializedName("front_id")
    @PrimaryKey
    @Expose()
    public Long id;

    public DeletedReport(Long id) {
        this.id = id;
    }
}
