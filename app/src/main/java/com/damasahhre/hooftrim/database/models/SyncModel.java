package com.damasahhre.hooftrim.database.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class SyncModel {

    @Expose()
    public List<Farm> farms;
    @Expose()
    public List<Cow> cows;
    @Expose()
    public List<Report> reports;

    public SyncModel(List<Farm> farms, List<Cow> cows, List<Report> reports) {
        this.farms = farms;
        this.cows = cows;
        this.reports = reports;
    }

    public void doneCreate() {
        for (Farm farm : farms) {
            farm.setSync(false);
            farm.setCreated(false);
        }
        for (Cow farm : cows) {
            farm.setSync(false);
            farm.setCreated(false);
        }
        for (Report report : reports) {
            report.sync = false;
            report.created = false;
        }
    }

    public void doneUpdate() {
        for (Farm farm : farms) {
            farm.setSync(false);
        }
        for (Cow farm : cows) {
            farm.setSync(false);
        }
        for (Report report : reports) {
            report.sync = false;
        }
    }

    public boolean isEmpty() {
        return farms.isEmpty() && cows.isEmpty() && reports.isEmpty();
    }

}
