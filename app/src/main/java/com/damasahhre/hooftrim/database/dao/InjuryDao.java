package com.damasahhre.hooftrim.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.damasahhre.hooftrim.database.models.InjureyReport;
import com.damasahhre.hooftrim.models.MyDate;

import java.util.List;

/**
 * کوئری‌‌های مربوط به جراحت گاو
 */
@Dao
public interface InjuryDao {

    @Query("SELECT COUNT(DISTINCT Report.visit_date) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end")
    Integer countDate(Long farmId, MyDate start, MyDate end);

    @Query("SELECT SUM(id_count) FROM (SELECT COUNT(DISTINCT Report.id) AS id_count FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 0 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.finger_number)")
    Integer felemons(Long farmId, MyDate start, MyDate end);

    @Query("SELECT SUM(id_count) FROM (SELECT COUNT(DISTINCT Report.id) AS id_count FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == :farmId " +
            "AND Cow.number == :cowNumber " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 0 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.finger_number)")
    Integer felemons(Long farmId, MyDate start, MyDate end, long cowNumber);


    @Query("SELECT SUM(id_count) FROM (SELECT COUNT(DISTINCT Report.id) AS id_count FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 10 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.finger_number)")
    Integer deramatit(Long farmId, MyDate start, MyDate end);

    @Query("SELECT SUM(id_count) FROM (SELECT COUNT(DISTINCT Report.id) AS id_count FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Cow.number == :cowNumber " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 10 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.finger_number)")
    Integer deramatit(Long farmId, MyDate start, MyDate end, long cowNumber);

    @Query("SELECT Report.cow_id AS cowId, Report.visit_date AS date, Report.finger_number AS fingerNumber " +
            "FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 4 " +
            "AND Report.reference_cause_new_limp ")
    List<InjureyReport> woundHoofBottom(Long farmId, MyDate start, MyDate end);

    @Query("SELECT Report.cow_id AS cowId, Report.visit_date AS date, Report.finger_number AS fingerNumber " +
            "FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND (Report.leg_area_number == 2 OR Report.leg_area_number == 3) " +
            "AND Report.reference_cause_new_limp ")
    List<InjureyReport> whiteLineWound(Long farmId, MyDate start, MyDate end);

    @Query("SELECT Report.cow_id AS cowId, Report.visit_date AS date, Report.finger_number AS fingerNumber" +
            " FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND (Report.leg_area_number == 5 OR Report.leg_area_number == 1)" +
            "AND Report.reference_cause_new_limp ")
    List<InjureyReport> pangeWound(Long farmId, MyDate start, MyDate end);

    @Query("SELECT Report.cow_id AS cowId, Report.visit_date AS date, Report.finger_number AS fingerNumber " +
            "FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 6 " +
            "AND Report.reference_cause_new_limp ")
    List<InjureyReport> pashneWound(Long farmId, MyDate start, MyDate end);

    @Query("SELECT Report.cow_id AS cowId, Report.visit_date AS date, Report.finger_number AS fingerNumber " +
            "FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND (Report.leg_area_number == 7 OR Report.leg_area_number == 8 " +
            "OR Report.leg_area_number == 11 OR Report.leg_area_number == 12) " +
            "AND Report.reference_cause_new_limp ")
    List<InjureyReport> wallWound(Long farmId, MyDate start, MyDate end);

    @Query("SELECT COUNT(DISTINCT Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 9 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.cow_id, Report.visit_date ")
    Integer reigenNine(Long farmId, MyDate start, MyDate end);

    @Query("SELECT SUM(total) FROM (SELECT 1 AS total FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "GROUP BY Report.cow_id, Report.visit_date)")
    Integer box(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT SUM(total) FROM (SELECT 1 AS total FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_limp_visit " +
            "GROUP BY Report.cow_id, Report.visit_date)")
    Integer visit(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT SUM(total) FROM (SELECT 1 AS total FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.cow_id, Report.visit_date)")
    Integer newLimp(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT COUNT(DISTINCT Report.cow_id) FROM Report, Cow, Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_hundred_days ")
    Integer sadRoze(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT COUNT(DISTINCT Report.cow_id) FROM Report, Cow, Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_dryness ")
    Integer dryness(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT COUNT(DISTINCT Report.cow_id) FROM Report, Cow, Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_lagged ")
    Integer delayed(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT COUNT(DISTINCT Report.cow_id) FROM Report, Cow, Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_group_hoof_trim ")
    Integer group(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT COUNT(DISTINCT Report.cow_id) FROM Report, Cow, Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_high_score ")
    Integer high(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT COUNT(DISTINCT Report.cow_id) FROM Report, Cow, Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_referential ")
    Integer refrence(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT COUNT(DISTINCT Report.cow_id) FROM Report, Cow, Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_heifer ")
    Integer heifer(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT COUNT(DISTINCT Report.cow_id) FROM Report, Cow, Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_long_hoof ")
    Integer longHoof(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT COUNT(DISTINCT Report.cow_id) FROM Report, Cow, Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_hoof_trim ")
    Integer somChini(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_boarding " +
            "AND (Report.leg_area_number == 1 OR Report.leg_area_number == 2 OR Report.leg_area_number == 3 OR Report.leg_area_number == 4" +
            " OR Report.leg_area_number == 5 OR Report.leg_area_number == 6 OR Report.leg_area_number == 7 OR Report.leg_area_number == 8" +
            " OR Report.leg_area_number == 12 OR Report.leg_area_number == 11) " +
            "GROUP BY Report.finger_number")
    List<Integer> boardingFactorAll(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_boarding " +
            "AND (Report.leg_area_number == 0 OR Report.leg_area_number == 10 OR Report.leg_area_number == 9)" +
            "GROUP BY Report.finger_number")
    List<Integer> boardingFactorAllOther(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Cow.number == :cowNumber " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_boarding " +
            "AND (Report.leg_area_number == 1 OR Report.leg_area_number == 2 OR Report.leg_area_number == 3 OR Report.leg_area_number == 4" +
            " OR Report.leg_area_number == 5 OR Report.leg_area_number == 6 OR Report.leg_area_number == 7 OR Report.leg_area_number == 8" +
            " OR Report.leg_area_number == 12 OR Report.leg_area_number == 11) " +
            "GROUP BY Report.finger_number")
    List<Integer> boardingFactor(Long farmId, MyDate date, MyDate endDate, Integer cowNumber);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Cow.number == :cowNumber " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_boarding " +
            "AND (Report.leg_area_number == 0 OR Report.leg_area_number == 10 OR Report.leg_area_number == 9)" +
            "GROUP BY Report.finger_number")
    List<Integer> boardingFactorOther(Long farmId, MyDate date, MyDate endDate, Integer cowNumber);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_hoof_trim ")
    List<Integer> hoofTrimFactorAll(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Cow.number == :cowNumber " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_hoof_trim ")
    List<Integer> hoofTrimFactor(Long farmId, MyDate date, MyDate endDate, Integer cowNumber);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_gel ")
    List<Integer> gelFactorAll(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Cow.number == :cowNumber " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_gel ")
    List<Integer> gelFactor(Long farmId, MyDate date, MyDate endDate, Integer cowNumber);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_limp_visit ")
    List<Integer> visitFactorAll(Long farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Cow.number == :cowNumber " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_limp_visit ")
    List<Integer> visitFactor(Long farmId, MyDate date, MyDate endDate, Integer cowNumber);

}
