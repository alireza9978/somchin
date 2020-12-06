package com.damasahhre.hooftrim.database.models;

import android.content.Context;

import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.google.gson.Gson;

import java.util.List;

public class SyncModel {

    public List<Farm> farms;
    public List<Cow> cows;
    public List<Report> reports;

}
