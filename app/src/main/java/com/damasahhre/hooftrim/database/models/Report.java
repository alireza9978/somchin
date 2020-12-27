package com.damasahhre.hooftrim.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.models.MyDate;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Entity
public class Report {
    @Expose()
    @SerializedName("front_id")
    @PrimaryKey
    public Long id;

    @Expose(serialize = false, deserialize = false)
    @ColumnInfo(name = "sync", defaultValue = "TRUE")
    public Boolean sync = true;
    @Expose(serialize = false, deserialize = false)
    @ColumnInfo(name = "created", defaultValue = "TRUE")
    public Boolean created = true;

    @Expose()
    @SerializedName("visit_date")
    @ColumnInfo(name = "visit_date")
    public MyDate visit;
    @Expose()
    @SerializedName("next_visit_date")
    @ColumnInfo(name = "next_visit_date")
    public MyDate nextVisit;

    @Expose()
    @SerializedName("leg_area_number")
    @ColumnInfo(name = "leg_area_number")
    public Integer legAreaNumber;
    @Expose()
    @SerializedName("finger_number")
    @ColumnInfo(name = "finger_number")
    public Integer fingerNumber;
    @Expose()
    @SerializedName("right_side")
    @ColumnInfo(name = "right_side")
    public Boolean rightSide;

    @Expose()
    @SerializedName("other_info_wound")
    @ColumnInfo(name = "other_info_wound")
    public Boolean otherInfoWound;
    @Expose()
    @SerializedName("other_info_gel")
    @ColumnInfo(name = "other_info_gel")
    public Boolean otherInfoGel;
    @Expose()
    @SerializedName("other_info_boarding")
    @ColumnInfo(name = "other_info_boarding")
    public Boolean otherInfoBoarding;
    @Expose()
    @SerializedName("other_info_ecchymosis")
    @ColumnInfo(name = "other_info_ecchymosis")
    public Boolean otherInfoEcchymosis;
    @Expose()
    @SerializedName("other_info_no_injury")
    @ColumnInfo(name = "other_info_no_injury")
    public Boolean otherInfoNoInjury;
    @Expose()
    @SerializedName("other_info_recovered")
    @ColumnInfo(name = "other_info_recovered")
    public Boolean otherInfoRecovered;
    @Expose()
    @SerializedName("other_info_hoof_trim")
    @ColumnInfo(name = "other_info_hoof_trim")
    public Boolean otherInfoHoofTrim;
    @Expose()
    @SerializedName("reference_cause_hundred_days")
    @ColumnInfo(name = "reference_cause_hundred_days")
    public Boolean referenceCauseHundredDays;
    @Expose()
    @SerializedName("reference_cause_dryness")
    @ColumnInfo(name = "reference_cause_dryness")
    public Boolean referenceCauseDryness;
    @Expose()
    @SerializedName("reference_cause_high_score")
    @ColumnInfo(name = "reference_cause_high_score")
    public Boolean referenceCauseHighScore;
    @Expose()
    @SerializedName("reference_cause_referential")
    @ColumnInfo(name = "reference_cause_referential")
    public Boolean referenceCauseReferential;
    @Expose()
    @SerializedName("reference_cause_lagged")
    @ColumnInfo(name = "reference_cause_lagged")
    public Boolean referenceCauseLagged;
    @Expose()
    @SerializedName("reference_cause_heifer")
    @ColumnInfo(name = "reference_cause_heifer")
    public Boolean referenceCauseHeifer;
    @Expose()
    @SerializedName("reference_cause_long_hoof")
    @ColumnInfo(name = "reference_cause_long_hoof")
    public Boolean referenceCauseLongHoof;
    @Expose()
    @SerializedName("reference_cause_new_limp")
    @ColumnInfo(name = "reference_cause_new_limp")
    public Boolean referenceCauseNewLimp;
    @Expose()
    @SerializedName("reference_cause_limp_visit")
    @ColumnInfo(name = "reference_cause_limp_visit")
    public Boolean referenceCauseLimpVisit;
    @Expose()
    @SerializedName("reference_cause_group_hoof_trim")
    @ColumnInfo(name = "reference_cause_group_hoof_trim")
    public Boolean referenceCauseGroupHoofTrim;
    @Expose()
    @SerializedName("description")
    @ColumnInfo(name = "description")
    public String description;
    @SerializedName("front_cow_id")
    @Expose()
    @ColumnInfo(name = "cow_id")
    public Long cowId;

    public ArrayList<Integer> getSelectedOtherInfo() {
        ArrayList<Integer> list = new ArrayList<>();
        if (otherInfoWound) {
            list.add(R.string.more_info_reason_1);
        }
        if (otherInfoGel) {
            list.add(R.string.more_info_reason_5);
        }
        if (otherInfoBoarding) {
            list.add(R.string.more_info_reason_6);
        }
        if (otherInfoEcchymosis) {
            list.add(R.string.more_info_reason_2);
        }
        if (otherInfoNoInjury) {
            list.add(R.string.more_info_reason_4);
        }
        if (otherInfoRecovered) {
            list.add(R.string.more_info_reason_3);
        }
        if (otherInfoHoofTrim) {
            list.add(R.string.more_info_reason_7);
        }
        return list;
    }

    public ArrayList<Integer> getSelectedReason() {
        ArrayList<Integer> list = new ArrayList<>();
        if (referenceCauseHundredDays) {
            list.add(R.string.reason_1);
        }
        if (referenceCauseDryness) {
            list.add(R.string.reason_2);
        }
        if (referenceCauseHighScore) {
            list.add(R.string.reason_6);
        }
        if (referenceCauseReferential) {
            list.add(R.string.reason_7);
        }
        if (referenceCauseLagged) {
            list.add(R.string.reason_4);
        }
        if (referenceCauseHeifer) {
            list.add(R.string.reason_9);
        }
        if (referenceCauseLongHoof) {
            list.add(R.string.reason_8);
        }
        if (referenceCauseNewLimp) {
            list.add(R.string.reason_4);
        }
        if (referenceCauseLimpVisit) {
            list.add(R.string.reason_5);
        }
        if (referenceCauseGroupHoofTrim) {
            list.add(R.string.reason_10);
        }
        return list;
    }

    @NotNull
    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", sync=" + sync +
                ", created=" + created +
                ", visit=" + visit +
                ", nextVisit=" + nextVisit +
                ", legAreaNumber=" + legAreaNumber +
                ", fingerNumber=" + fingerNumber +
                ", rightSide=" + rightSide +
                ", otherInfoWound=" + otherInfoWound +
                ", otherInfoGel=" + otherInfoGel +
                ", otherInfoBoarding=" + otherInfoBoarding +
                ", otherInfoEcchymosis=" + otherInfoEcchymosis +
                ", otherInfoNoInjury=" + otherInfoNoInjury +
                ", otherInfoRecovered=" + otherInfoRecovered +
                ", otherInfoHoofTrim=" + otherInfoHoofTrim +
                ", referenceCauseHundredDays=" + referenceCauseHundredDays +
                ", referenceCauseDryness=" + referenceCauseDryness +
                ", referenceCauseHighScore=" + referenceCauseHighScore +
                ", referenceCauseReferential=" + referenceCauseReferential +
                ", referenceCauseLagged=" + referenceCauseLagged +
                ", referenceCauseHeifer=" + referenceCauseHeifer +
                ", referenceCauseLongHoof=" + referenceCauseLongHoof +
                ", referenceCauseNewLimp=" + referenceCauseNewLimp +
                ", referenceCauseLimpVisit=" + referenceCauseLimpVisit +
                ", referenceCauseGroupHoofTrim=" + referenceCauseGroupHoofTrim +
                ", description='" + description + '\'' +
                ", cowId=" + cowId +
                '}';
    }
}
