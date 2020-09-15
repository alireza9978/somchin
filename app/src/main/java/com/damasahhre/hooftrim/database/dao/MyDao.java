package com.damasahhre.hooftrim.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.damasahhre.hooftrim.database.models.Cow;
import com.damasahhre.hooftrim.database.models.CowWithReports;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.models.FarmWithCows;
import com.damasahhre.hooftrim.database.models.NextReport;
import com.damasahhre.hooftrim.database.models.Report;

import java.util.Date;
import java.util.List;

@Dao
public interface MyDao {

    @Transaction
    @Query("SELECT * FROM Farm ")
    public List<FarmWithCows> getFarmWithCows();

    @Transaction
    @Query("SELECT * FROM Report WHERE Report.cow_id == :cowId")
    public List<CowWithReports> getCowsWithReport(Integer cowId);

    @Transaction
    @Query("SELECT * FROM Cow WHERE Cow.farm_id == :farmId")
    public List<FarmWithCows> getFarmWithCows(Integer farmId);

    @Transaction
    @Query("SELECT Report.next_visit_date AS nextVisitDate, Farm.name AS farmName, Cow.number AS cowNumber" +
            " FROM Cow, Report, Farm" +
            " WHERE next_visit_date > :now")
    List<NextReport> getAllNextVisit(Date now);

    @Query("SELECT * FROM Farm")
    List<Farm> getAll();

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
