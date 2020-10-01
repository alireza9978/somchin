package com.damasahhre.hooftrim.activities;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.adapters.GridViewAdapterCowInFarmProfile;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.Cow;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.utils.AppExecutors;

import java.util.List;
import java.util.Objects;

public class FarmProfileActivity extends AppCompatActivity {

    private TextView title;
    private TextView system;
    private TextView birthCount;
    private TextView nextVisit;
    private ImageView bookmark;
    private ImageView exit;
    private GridView cowsGridView;
    private RecyclerView nextVisitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestock_profile);

        title = findViewById(R.id.title_livestrok);
        birthCount = findViewById(R.id.count_value);
        system = findViewById(R.id.system_value);
        nextVisit = findViewById(R.id.next_visit_value);
        bookmark = findViewById(R.id.bookmark_image);
        exit = findViewById(R.id.back_icon);
        cowsGridView = findViewById(R.id.cows_grid);
        nextVisitView = findViewById(R.id.next_visit_lists);
        exit.setOnClickListener(view -> finish());
        Constants.setImageBackBorder(this, exit);

        int id = Objects.requireNonNull(getIntent().getExtras()).getInt(Constants.FARM_ID);
        MyDao dao = DataBase.getInstance(this).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
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
                nextVisit.setText("Not yet");
            });
            List<Cow> cows = dao.getAllCowOfFarm(id);
            runOnUiThread(() -> {
                GridViewAdapterCowInFarmProfile adapter = new GridViewAdapterCowInFarmProfile(this, cows, farm.id);
                cowsGridView.setAdapter(adapter);
            });
            //todo query one
            //todo query two
        });

    }
}