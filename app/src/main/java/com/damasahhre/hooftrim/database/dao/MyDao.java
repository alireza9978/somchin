package com.damasahhre.hooftrim.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.damasahhre.hooftrim.database.models.Cow;
import com.damasahhre.hooftrim.database.models.CowForMarked;
import com.damasahhre.hooftrim.database.models.CowWithLastVisit;
import com.damasahhre.hooftrim.database.models.DeletedCow;
import com.damasahhre.hooftrim.database.models.DeletedFarm;
import com.damasahhre.hooftrim.database.models.DeletedReport;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.models.FarmWithCowCount;
import com.damasahhre.hooftrim.database.models.FarmWithNextVisit;
import com.damasahhre.hooftrim.database.models.LastReport;
import com.damasahhre.hooftrim.database.models.MyReport;
import com.damasahhre.hooftrim.database.models.NextReport;
import com.damasahhre.hooftrim.database.models.NextVisit;
import com.damasahhre.hooftrim.database.models.Report;
import com.damasahhre.hooftrim.database.models.SearchFarm;
import com.damasahhre.hooftrim.models.MyDate;

import java.util.List;

/**
 * کوئری‌های استفاده شده در صفحات برنامه
 */
@Dao
public interface MyDao {

    @Query("DELETE FROM Farm")
    void deleteAllFarm();

    @Query("DELETE FROM Cow")
    void deleteAllCow();

    @Query("DELETE FROM Report")
    void deleteAllReport();

    @Query("DELETE FROM DeletedFarm")
    void deleteAllOtherFarm();

    @Query("DELETE FROM DeletedCow")
    void deleteAllOtherCow();

    @Query("DELETE FROM DeletedReport")
    void deleteAllOtherReport();


    @Query("SELECT Cow.id AS cowId,Cow.number AS cowNumber, MAX(Report.visit_date) AS lastVisit," +
            " Farm.name AS farmName " +
            "FROM Cow,Farm,Report " +
            "WHERE Cow.number_string LIKE :id " +
            "AND Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "GROUP BY Cow.id")
    List<CowForMarked> searchCow(String id);

    @Query("SELECT Farm.id AS farmId, Farm.name AS farmName, COUNT(Cow.id) AS cowCount , " +
            "MAX(Report.visit_date) AS lastVisit  " +
            "FROM Cow,Farm,Report " +
            "WHERE Report.visit_date <= :end " +
            "AND Report.visit_date >= :start " +
            "AND Cow.farm_id == farm.id " +
            "AND Report.cow_id == Cow.id " +
            "GROUP BY Farm.id")
    List<SearchFarm> searchFarm(MyDate start, MyDate end);

    @Query("SELECT Cow.id AS cowId,Cow.number AS cowNumber, MAX(Report.visit_date) AS lastVisit, Farm.name AS farmName " +
            "FROM Cow,Farm,Report " +
            "WHERE Cow.number_string LIKE :id " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.cow_id == Cow.id " +
            "AND Cow.farm_id == farm.id " +
            "GROUP BY Cow.id")
    List<CowForMarked> searchCow(String id, MyDate start, MyDate end);

    @Query("SELECT Cow.id AS cowId,Cow.number AS cowNumber, MAX(Report.visit_date) AS lastVisit, Farm.name AS farmName " +
            "FROM Cow,Farm,Report " +
            "WHERE Cow.number_string LIKE :id " +
            "AND Report.cow_id == Cow.id " +
            "AND Cow.farm_id == farm.id " +
            "AND Farm.id == :farmId " +
            "GROUP BY Cow.id")
    List<CowForMarked> searchCow(String id, Long farmId);

    @Query("SELECT Cow.id AS cowId,Cow.number AS cowNumber, MAX(Report.visit_date) AS lastVisit, Farm.name AS farmName " +
            "FROM Cow,Farm,Report " +
            "WHERE Cow.number_string LIKE :id " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.cow_id == Cow.id " +
            "AND Cow.farm_id == farm.id " +
            "AND Farm.id == :farmId " +
            "GROUP BY Cow.id")
    List<CowForMarked> searchCow(String id, MyDate start, MyDate end, Long farmId);

    @Query("SELECT Cow.id AS cowId,Cow.number AS cowNumber," +
            " Farm.name AS farmName, MAX(Report.visit_date) AS lastVisit " +
            "FROM Cow,Farm,Report " +
            "WHERE Cow.favorite AND " +
            "Cow.farm_id == Farm.id AND " +
            "Report.cow_id == Cow.id GROUP BY Cow.id")
    List<CowForMarked> getMarkedCows();

    @Query("SELECT Farm.id AS farmId, Farm.name AS farmName, COUNT(Cow.id) AS cow_count " +
            "FROM Farm, Cow " +
            "WHERE Farm.id == Cow.farm_id GROUP BY Farm.id")
    List<FarmWithCowCount> getFarmWithCowCount();

    @Query("SELECT Farm.id AS farmId, Farm.name AS farmName, COUNT(Cow.id) AS cow_count " +
            "FROM Farm,Cow " +
            "WHERE Farm.is_favorite AND " +
            "Cow.farm_id == Farm.id GROUP BY Farm.id")
    List<FarmWithCowCount> getMarkedFarmWithCowCount();

    @Query("SELECT * " +
            "FROM Farm " +
            "WHERE Farm.is_favorite")
    List<Farm> getMarkedFarm();

    @Query("SELECT Farm.name AS farmName, Cow.number AS cowNumber, Cow.id AS cowId, MIN(Report.next_visit_date) AS nextVisitDate" +
            " FROM Cow, Report, Farm" +
            " WHERE Report.next_visit_date >= :now AND" +
            " Farm.id == Cow.farm_id AND" +
            " Cow.id == Report.cow_id GROUP BY Cow.id")
    List<NextReport> getAllNextVisit(MyDate now);

    @Query("SELECT Report.next_visit_date AS nextVisitDate, Farm.name AS farmName, Cow.number AS cowNumber, Cow.id AS cowId" +
            " FROM Cow, Report, Farm" +
            " WHERE nextVisitDate == :now AND" +
            " Cow.id == Report.cow_id AND" +
            " Farm.id == Cow.farm_id ")
    List<NextReport> getAllVisitInDay(MyDate now);

    @Query("SELECT MAX(Report.next_visit_date) AS nextVisitDate, Farm.name AS farmName, Cow.number AS cowNumber, Cow.id AS cowId" +
            " FROM Cow, Report, Farm" +
            " WHERE Report.next_visit_date >= :now AND" +
            " Cow.id == Report.cow_id AND" +
            " Farm.id == Cow.farm_id " +
            "GROUP BY Report.cow_id")
    List<NextReport> getAllVisitInDayNotification(MyDate now);

    @Query("SELECT Report.next_visit_date AS nextVisitDate, Farm.name AS farmName, Cow.number AS cowNumber,Cow.id AS cowId" +
            " FROM Cow, Report, Farm" +
            " WHERE nextVisitDate == :now AND" +
            " Cow.id == Report.cow_id AND" +
            " Farm.id == Cow.farm_id")
    List<NextReport> getAllNextVisitInDay(MyDate now);

    @Query("SELECT MAX(Report.next_visit_date) AS visitDate, Cow.id AS cowId, Cow.number AS cowNumber" +
            " FROM Cow, Report, Farm" +
            " WHERE Report.next_visit_date >= :now AND" +
            " Report.cow_id == Cow.id AND" +
            " Cow.farm_id == Farm.id AND" +
            " Farm.id == :farmId GROUP BY Cow.id")
    List<NextVisit> getAllNextVisitFroFarm(MyDate now, Long farmId);

    @Query("SELECT MIN(Report.next_visit_date) AS nextVisit, MAX(Report.visit_date) AS lastVisit" +
            " FROM Report" +
            " WHERE Report.cow_id == :cowId")
    LastReport getLastReport(Long cowId);

    @Query("SELECT * FROM Farm")
    List<Farm> getAll();

    @Query("SELECT * FROM Farm WHERE Farm.sync AND (NOT Farm.created)")
    List<Farm> getAllFarmToSync();

    @Query("SELECT * FROM Cow WHERE Cow.sync AND (NOT Cow.created)")
    List<Cow> getAllCowToSync();

    @Query("SELECT * FROM Report WHERE Report.sync AND (NOT Report.created)")
    List<Report> getAllReportToSync();

    @Update
    void doneSyncCow(List<Cow> cows);

    @Update
    void doneSyncReport(List<Report> reports);

    @Update
    void doneSyncFarm(List<Farm> farms);

    @Query("SELECT * FROM Farm WHERE Farm.sync AND Farm.created")
    List<Farm> getAllNewFarmToSync();

    @Query("SELECT * FROM Cow WHERE Cow.sync AND Cow.created")
    List<Cow> getAllNewCowToSync();

    @Query("SELECT * FROM Report WHERE Report.sync AND Report.created")
    List<Report> getAllNewReportToSync();

    @Query("SELECT * FROM DeletedFarm")
    List<DeletedFarm> getAllDeletedFarmToSync();

    @Query("SELECT * FROM DeletedCow")
    List<DeletedCow> getAllDeletedCowToSync();

    @Query("SELECT * FROM DeletedReport")
    List<DeletedReport> getAllDeletedReportToSync();

    @Delete
    void doneDeleteCow(List<DeletedCow> cows);

    @Delete
    void doneDeleteReport(List<DeletedReport> reports);

    @Delete
    void doneDeleteFarm(List<DeletedFarm> farms);


    @Query("SELECT Cow.id AS id, Cow.number AS number, MAX(Report.visit_date) AS lastVisit " +
            " FROM Cow, Report" +
            " WHERE Cow.farm_id == :id AND" +
            " Report.cow_id == Cow.id GROUP BY Cow.id")
    List<CowWithLastVisit> getAllCowOfFarmWithLastVisit(Long id);


    @Query("SELECT * FROM Cow WHERE Cow.farm_id == :id")
    List<Cow> getAllCowOfFarm(Long id);

    @Query("SELECT * FROM Report WHERE Report.cow_id == :id")
    List<Report> getAllReportOfCow(Long id);

    @Query("SELECT *, Cow.number AS cowNumber FROM Cow,Report WHERE Report.cow_id == Cow.id AND Cow.farm_id == :id")
    List<MyReport> getAllMyReportFarm(Long id);

    @Query("SELECT *, Cow.number AS cowNumber FROM Cow,Report " +
            "WHERE Report.cow_id == Cow.id AND Cow.farm_id == :id " +
            "AND Report.visit_date >= :start AND Report.visit_date < :end")
    List<MyReport> getAllMyReportFarm(Long id, MyDate start, MyDate end);

    @Query("SELECT *, Cow.number AS cowNumber FROM Cow,Report " +
            "WHERE Report.cow_id == Cow.id AND Cow.farm_id == :id AND Report.visit_date == :day")
    List<MyReport> getAllMyReportFarm(Long id, MyDate day);

    @Query("SELECT *, Cow.number AS cowNumber FROM Cow,Report WHERE Report.id == :id and Cow.id == Report.cow_id")
    MyReport myReportWithCow(Long id);

    @Query("SELECT * FROM Farm WHERE Farm.id == :id")
    Farm getFarm(Long id);

    @Query("SELECT Farm.id AS farmId, MIN(Report.next_visit_date) AS nextVisit " +
            "FROM Farm,Cow,Report " +
            "WHERE Farm.id == :id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Report.cow_id == Cow.id")
    FarmWithNextVisit getFarmWithNextVisit(Long id);


    @Query("SELECT * FROM Cow WHERE Cow.number == :cowNumber AND Cow.farm_id == :farmId")
    Cow getCow(Integer cowNumber, Long farmId);

    @Query("SELECT * FROM Cow WHERE Cow.id == :id")
    Cow getCow(Long id);

    @Query("SELECT * FROM Report WHERE Report.id == :id")
    Report getReport(Long id);

    @Delete
    void deleteCow(Cow... cows);

    @Delete
    void deleteFarm(Farm... farms);

    @Delete
    void deleteReport(Report... reports);

    @Delete
    void deleteCow(DeletedCow... cows);

    @Delete
    void deleteFarm(DeletedFarm... farms);

    @Delete
    void deleteReport(DeletedReport... reports);

    @Update
    void update(Cow cow);

    @Update
    void update(Farm farm);

    @Update
    void update(Report report);

    @Insert
    void insert(Report report);

    @Insert
    void insert(Farm farm);

    @Insert
    void insert(DeletedReport report);

    @Insert
    void insert(DeletedFarm farm);

    @Insert
    long insertGetId(Farm farm);

    @Insert
    long insertGetId(Report report);

    @Insert
    void insert(Cow Cow);

    @Insert
    void insert(DeletedCow Cow);

    @Insert
    long insertGetId(Cow Cow);

    @Insert
    void insertAll(Report... reports);

    @Insert
    void insertAll(Cow... cows);

    @Insert
    void insertAll(Farm... farms);

    @Insert
    void insertAllCows(List<Cow> cows);

    @Insert
    void insertAllFarm(List<Farm> farms);

    @Insert
    void insertAllReport(List<Report> reports);
}
