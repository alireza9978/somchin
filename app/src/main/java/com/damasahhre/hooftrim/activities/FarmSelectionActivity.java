package com.damasahhre.hooftrim.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.adapters.RecyclerViewAdapterFarmSimple;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.utils.AppExecutors;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FarmSelectionActivity extends AppCompatActivity {


    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_strock_selection);

        findViewById(R.id.close_image).setOnClickListener(view -> {
            finish();
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.livestroks_lists);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        MyDao dao = DataBase.getInstance(this).dao();
        AtomicReference<RecyclerViewAdapterFarmSimple> adapter = new AtomicReference<>();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Farm> farmList = dao.getAll();
            runOnUiThread(() -> {
                adapter.set(new RecyclerViewAdapterFarmSimple(farmList, context));
                recyclerView.setAdapter(adapter.get());
            });

        });


    }

    public void selectedFarm(Long id) {
        Intent intent = new Intent();
        intent.putExtra(Constants.FARM_ID, id);
        setResult(Constants.DATE_SELECTION_OK, intent);
        finish();
    }

}