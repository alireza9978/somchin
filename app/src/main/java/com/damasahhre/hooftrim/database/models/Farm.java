package com.damasahhre.hooftrim.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

@Entity
public class Farm {

    public Farm(String name, Integer birthCount, String controlSystem, Boolean favorite, boolean sync) {
        this.name = name;
        this.birthCount = birthCount;
        this.controlSystem = controlSystem;
        this.favorite = favorite;
        this.sync = sync;
    }

    @PrimaryKey
    private Long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "birth_count")
    private Integer birthCount;

    @ColumnInfo(name = "control_system")
    private String controlSystem;

    @ColumnInfo(name = "is_favorite")
    private Boolean favorite;

    @ColumnInfo(name = "sync")
    private Boolean sync;

    public Boolean getSync() {
        return sync;
    }

    public void setSync(Boolean sync) {
        this.sync = sync;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthCount() {
        return birthCount;
    }

    public void setBirthCount(Integer birthCount) {
        this.birthCount = birthCount;
    }

    public String getControlSystem() {
        return controlSystem;
    }

    public void setControlSystem(String controlSystem) {
        this.controlSystem = controlSystem;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public JsonElement getJson() {
        Gson gson = new Gson();
        return gson.toJsonTree(this);
    }

}
