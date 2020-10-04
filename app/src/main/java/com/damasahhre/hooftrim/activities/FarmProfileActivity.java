package com.damasahhre.hooftrim.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.adapters.GridViewAdapterCowInFarmProfile;
import com.damasahhre.hooftrim.adapters.RecyclerViewAdapterNextVisitFarmProfile;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.Cow;
import com.damasahhre.hooftrim.database.models.CowWithLastVisit;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.models.FarmWithNextVisit;
import com.damasahhre.hooftrim.database.models.NextVisit;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.models.MyDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FarmProfileActivity extends AppCompatActivity {

    private TextView title;
    private TextView visitTitle;
    private TextView system;
    private TextView birthCount;
    private TextView nextVisit;
    private ImageView bookmark;
    private GridView cowsGridView;
    private RecyclerView nextVisitView;
    private RecyclerViewAdapterNextVisitFarmProfile mAdapter;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestock_profile);

        title = findViewById(R.id.title_livestrok);
        visitTitle = findViewById(R.id.next_visit_title);
        birthCount = findViewById(R.id.count_value);
        system = findViewById(R.id.system_value);
        nextVisit = findViewById(R.id.next_visit_value);
        bookmark = findViewById(R.id.bookmark_image);
        cowsGridView = findViewById(R.id.cows_grid);
        nextVisitView = findViewById(R.id.next_visit_lists);
        ImageView exit = findViewById(R.id.back_icon);
        exit.setOnClickListener(view -> finish());
        Constants.setImageBackBorder(this, exit);

        id = Objects.requireNonNull(getIntent().getExtras()).getInt(Constants.FARM_ID);
        nextVisitView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        nextVisitView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapterNextVisitFarmProfile(new ArrayList<>(), this);
        nextVisitView.setAdapter(mAdapter);

    }

    private void hideVisit() {
        visitTitle.setVisibility(View.GONE);
        nextVisitView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyDao dao = DataBase.getInstance(this).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            FarmWithNextVisit farmWithNextVisit = dao.getFarmWithNextVisit(id);
            Farm farm = dao.getFarm(id);
            runOnUiThread(() -> {
                bookmark.setOnClickListener(view -> {
                    farm.favorite = !farm.favorite;
                    if (farm.favorite) {
                        bookmark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark_fill));
                    } else {
                        bookmark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark));
                    }
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        dao.update(farm);
                    });
                });
                if (farm.favorite) {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(FarmProfileActivity.this, R.drawable.ic_bookmark_fill));
                } else {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(FarmProfileActivity.this, R.drawable.ic_bookmark));
                }
                title.setText(farm.name);
                birthCount.setText("" + farm.birthCount);
                system.setText(farm.controlSystem);
                if (farmWithNextVisit.nextVisit != null)
                    nextVisit.setText(farmWithNextVisit.nextVisit.toStringWithoutYear(this));
                else
                    nextVisit.setText(R.string.no_visit_short);
            });
            List<CowWithLastVisit> cows = dao.getAllCowOfFarmWithLastVisit(id);
            List<Cow> allCows = dao.getAllCowOfFarm(id);
            runOnUiThread(() -> {
                main:
                for(Cow cow : allCows){
                    for (CowWithLastVisit cowWithLastVisit : cows){
                        if (cow.getId().equals(cowWithLastVisit.getId())){
                            continue main;
                        }
                    }
                    CowWithLastVisit temp = new CowWithLastVisit();
                    temp.setId(cow.getId());
                    temp.setLastVisit(null);
                    temp.setNumber(cow.getNumber());
                    cows.add(temp);
                }
                GridViewAdapterCowInFarmProfile adapter = new GridViewAdapterCowInFarmProfile(this, cows, id);
                cowsGridView.setAdapter(adapter);
            });
            List<NextVisit> list = dao.getAllNextVisitFroFarm(new MyDate(new Date()), id);
            runOnUiThread(() -> {
                if (list.isEmpty()) {
                    hideVisit();
                } else {
                    showVisit();
                    mAdapter.setNextVisits(list);
                    mAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private void showVisit() {
        visitTitle.setVisibility(View.VISIBLE);
        nextVisitView.setVisibility(View.VISIBLE);
    }

}