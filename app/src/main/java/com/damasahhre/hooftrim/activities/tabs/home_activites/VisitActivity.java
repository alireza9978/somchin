package com.damasahhre.hooftrim.activities.tabs.home_activites;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.adapters.RecyclerViewAdapterAllNextVisit;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.NextReport;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.models.MyDate;

import java.util.Date;
import java.util.List;

public class VisitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());
        Constants.setImageBack(this, back);

        RecyclerView listView = findViewById(R.id.all_visit_lists);

        MyDao dao = DataBase.getInstance(this).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<NextReport> reports = dao.getAllNextVisit(new MyDate(new Date()));
            RecyclerViewAdapterAllNextVisit mAdapter = new RecyclerViewAdapterAllNextVisit(reports, this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            listView.setLayoutManager(layoutManager);
            listView.setAdapter(mAdapter);
        });

    }
}