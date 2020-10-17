package com.damasahhre.hooftrim.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.damasahhre.hooftrim.database.models.Temp;
import com.damasahhre.hooftrim.models.MyDate;

@Dao
public interface InjuryDao {


    @Query("SELECT COUNT(Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == :type " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.id")
    Integer countInjury(Integer farmId, MyDate start, MyDate end, Integer type);

    @Query("SELECT COUNT(Report.id) AS count FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 0 " +
            "AND Report.reference_cause_new_limp ")
    Temp felemons(Integer farmId, MyDate start, MyDate end);


    @Query("SELECT COUNT(Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 10 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.id")
    Integer deramatit(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT COUNT(Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 4 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.id")
    Integer woundHoofBottom(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT COUNT(Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND (Report.leg_area_number == 2 OR Report.leg_area_number == 3) " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.id")
    Integer whiteLineWound(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT COUNT(Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND (Report.leg_area_number == 5 OR Report.leg_area_number == 1)" +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.id")
    Integer pangeWound(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT COUNT(Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 6 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.id")
    Integer pashneWound(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT COUNT(Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND (Report.leg_area_number == 7 OR Report.leg_area_number == 8 " +
            "OR Report.leg_area_number == 11 OR Report.leg_area_number == 12) " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.id")
    Integer wallWound(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT COUNT(Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 9 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.id")
    Integer reigenNine(Integer farmId, MyDate start, MyDate end);

}
