package com.damasahhre.hooftrim.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.damasahhre.hooftrim.database.models.Cow;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.models.FarmWithCows;
import com.damasahhre.hooftrim.database.models.Report;

import java.util.List;

@Dao
public interface CowDao {

    @Transaction
    @Query("SELECT * FROM Farm")
    public List<FarmWithCows> getFarmWithCows();


    @Query("SELECT * FROM Farm")
    List<Farm> getAll();

    @Insert
    void insertReport(Report report);

    @Insert
    void insertFarm(Farm farm);

    @Insert
    void insertCow(Cow Cow);

    @Insert
    void insertAll(Cow... Cows);


}
