package com.damasahhre.hooftrim.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Report {
    @PrimaryKey
    public Integer id;

    @ColumnInfo(name = "visit_date")
    public Date visit;
    @ColumnInfo(name = "next_visit_date")
    public Date nextVisit;

    @ColumnInfo(name = "leg_area_number")
    public Integer legAreaNumber;
    @ColumnInfo(name = "finger_number")
    public Integer fingerNumber;

    @ColumnInfo(name = "other_info_wound")
    public Boolean otherInfoWound;
    @ColumnInfo(name = "other_info_gel")
    public Boolean otherInfoGel;
    @ColumnInfo(name = "other_info_boarding")
    public Boolean otherInfoBoarding;
    @ColumnInfo(name = "other_info_ecchymosis")
    public Boolean otherInfoEcchymosis;
    @ColumnInfo(name = "other_info_no_injury")
    public Boolean otherInfoNoInjury;
    @ColumnInfo(name = "other_info_recovered")
    public Boolean otherInfoRecovered;
    @ColumnInfo(name = "other_info_hoof_trim")
    public Boolean otherInfoHoofTrim;

    @ColumnInfo(name = "reference_cause_hundred_days")
    public Boolean referenceCauseHundredDays;
    @ColumnInfo(name = "reference_cause_dryness")
    public Boolean referenceCauseDryness;
    @ColumnInfo(name = "reference_cause_high_score")
    public Boolean referenceCauseHighScore;
    @ColumnInfo(name = "reference_cause_referential")
    public Boolean referenceCauseReferential;
    @ColumnInfo(name = "reference_cause_lagged")
    public Boolean referenceCauseLagged;

    @ColumnInfo(name = "reference_cause_heifer")
    public Boolean referenceCauseHeifer;
    @ColumnInfo(name = "reference_cause_long_hoof")
    public Boolean referenceCauseLongHoof;
    @ColumnInfo(name = "reference_cause_new_limp")
    public Boolean referenceCauseNewLimp;
    @ColumnInfo(name = "reference_cause_limp_visit")
    public Boolean referenceCauseLimpVisit;
    @ColumnInfo(name = "reference_cause_group_hoof_trim")
    public Boolean referenceCauseGroupHoofTrim;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "cow_id")
    public Integer cowId;


}
