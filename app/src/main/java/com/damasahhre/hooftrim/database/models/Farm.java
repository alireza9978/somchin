package com.damasahhre.hooftrim.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * کلاس نگهدارنده اطلاعات یک گاوداری
 */
@Entity
public class Farm {

    @Expose()
    @SerializedName("front_id")
    @PrimaryKey
    private Long id;
    @Expose()
    @ColumnInfo(name = "name")
    private String name;
    @Expose()
    @SerializedName("birth_count")
    @ColumnInfo(name = "birth_count")
    private Integer birthCount;
    @Expose()
    @SerializedName("control_system")
    @ColumnInfo(name = "control_system")
    private String controlSystem;
    @Expose()
    @SerializedName("is_favorite")
    @ColumnInfo(name = "is_favorite")
    private Boolean favorite;
    @Expose(serialize = false, deserialize = false)
    @ColumnInfo(name = "sync")
    private Boolean sync;
    @Expose(serialize = false, deserialize = false)
    @ColumnInfo(name = "created")
    private Boolean created;

    public Farm(String name, Integer birthCount, String controlSystem, Boolean favorite, Boolean sync, Boolean created) {
        this.name = name;
        this.birthCount = birthCount;
        this.controlSystem = controlSystem;
        this.favorite = favorite;
        this.sync = sync;
        this.created = created;
    }

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

    public Boolean getCreated() {
        return created;
    }

    public void setCreated(Boolean created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Farm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthCount=" + birthCount +
                ", controlSystem='" + controlSystem + '\'' +
                ", favorite=" + favorite +
                ", sync=" + sync +
                '}';
    }
}
