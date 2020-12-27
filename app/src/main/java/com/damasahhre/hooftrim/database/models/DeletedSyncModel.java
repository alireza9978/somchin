package com.damasahhre.hooftrim.database.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class DeletedSyncModel {

    public DeletedSyncModel(List<DeletedFarm> farms, List<DeletedCow> cows, List<DeletedReport> reports) {
        this.farms = farms;
        this.cows = cows;
        this.reports = reports;
    }

    @Expose()
    public List<DeletedFarm> farms;
    @Expose()
    public List<DeletedCow> cows;
    @Expose()
    public List<DeletedReport> reports;

    public boolean isEmpty(){
        return farms.isEmpty() && cows.isEmpty() && reports.isEmpty();
    }

}
