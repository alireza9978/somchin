package com.damasahhre.hooftrim.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.damasahhre.hooftrim.database.models.Cow;
import com.damasahhre.hooftrim.database.models.CowWithLastVisit;
import com.damasahhre.hooftrim.database.models.CowWithReports;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.models.FarmWithCows;
import com.damasahhre.hooftrim.database.models.FarmWithNextVisit;
import com.damasahhre.hooftrim.database.models.LastReport;
import com.damasahhre.hooftrim.database.models.NextReport;
import com.damasahhre.hooftrim.database.models.NextVisit;
import com.damasahhre.hooftrim.database.models.Report;
import com.damasahhre.hooftrim.models.MyDate;

import java.util.List;

@Dao
public interface MyDao {

    @Query("SELECT * FROM Farm ")
    public List<FarmWithCows> getFarmWithCows();

    @Query("SELECT * FROM Cow WHERE Cow.farm_id == :id")
    public List<Cow> getNextVisitCows(int id);

    @Query("SELECT * FROM Cow WHERE Cow.favorite")
    public List<Cow> getMarkedCows();

    @Query("SELECT * FROM Farm WHERE Farm.is_favorite")
    public List<Farm> getMarkedFarm();

    @Query("SELECT * FROM Report WHERE Report.cow_id == :cowId")
    public List<CowWithReports> getCowsWithReport(Integer cowId);

    @Query("SELECT * FROM Cow WHERE Cow.farm_id == :farmId")
    public List<FarmWithCows> getFarmWithCows(Integer farmId);

    @Query("SELECT Report.next_visit_date AS nextVisitDate, Farm.name AS farmName, Cow.number AS cowNumber" +
            " FROM Cow, Report, Farm" +
            " WHERE nextVisitDate >= :now AND" +
            " Cow.id == Report.cow_id AND" +
            " Farm.id == Cow.farm_id")
    List<NextReport> getAllNextVisit(MyDate now);

    @Query("SELECT Report.visit_date AS visitDate, Farm.name AS farmName, Cow.number AS cowNumber" +
            " FROM Cow, Report, Farm" +
            " WHERE visitDate == :now AND" +
            " Cow.id == Report.cow_id AND" +
            " Farm.id == Cow.farm_id")
    List<NextReport> getAllVisitInDay(MyDate now);

    @Query("SELECT Report.next_visit_date AS nextVisitDate, Farm.name AS farmName, Cow.number AS cowNumber" +
            " FROM Cow, Report, Farm" +
            " WHERE nextVisitDate == :now AND" +
            " Cow.id == Report.cow_id AND" +
            " Farm.id == Cow.farm_id")
    List<NextReport> getAllNextVisitInDay(MyDate now);

    @Query("SELECT Report.next_visit_date AS visitDate, Cow.id AS cowId, Cow.number AS cowNumber" +
            " FROM Cow, Report, Farm" +
            " WHERE visitDate >= :now AND" +
            " Report.cow_id == Cow.id AND" +
            " Cow.farm_id == Farm.id AND" +
            " Farm.id == :farmId")
    List<NextVisit> getAllNextVisitFroFarm(MyDate now, Integer farmId);

    @Query("SELECT Report.next_visit_date AS nextVisit, MAX(Report.visit_date) AS lastVisit" +
            " FROM Report" +
            " WHERE Report.cow_id == :cowId")
    LastReport getLastReport(Integer cowId);

    @Query("SELECT * FROM Farm")
    List<Farm> getAll();

    @Query("SELECT * FROM Report")
    List<Report> getAllReports();

    @Query("SELECT Cow.id AS id, Cow.number AS number, MAX(Report.visit_date) AS lastVisit " +
            " FROM Cow, Report" +
            " WHERE Cow.farm_id == :id AND" +
            " Report.cow_id == Cow.id GROUP BY Cow.id")
    List<CowWithLastVisit> getAllCowOfFarmWithLastVisit(Integer id);


    @Query("SELECT * FROM Cow WHERE Cow.farm_id == :id")
    List<Cow> getAllCowOfFarm(Integer id);

    @Query("SELECT * FROM Report WHERE Report.cow_id == :id")
    List<Report> getAllReportOfCow(Integer id);

    @Query("SELECT * FROM Farm WHERE Farm.id == :id")
    Farm getFarm(Integer id);

    @Query("SELECT *, MIN(Report.next_visit_date) AS nextVisit " +
            "FROM Farm,Cow,Report " +
            "WHERE Farm.id == :id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Report.cow_id == Cow.id")
    FarmWithNextVisit getFarmWithNextVisit(Integer id);


    @Query("SELECT * FROM Cow WHERE Cow.number == :cowNumber AND Cow.farm_id == :farmId")
    Cow getCow(Integer cowNumber, Integer farmId);

    @Query("SELECT * FROM Cow WHERE Cow.id == :id")
    Cow getCow(Integer id);

    @Query("SELECT * FROM Report WHERE Report.id == :id")
    Report getReport(Integer id);

    @Delete
    void deleteCow(Cow... cows);

    @Delete
    void deleteFarm(Farm... farms);

    @Delete
    void deleteReport(Report... reports);

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
    void insert(Cow Cow);


    @Insert
    void insertAll(Report... reports);

    @Insert
    void insertAll(Cow... cows);

    @Insert
    void insertAll(Farm... farms);


}
