package com.damasahhre.hooftrim.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.adapters.GridViewAdapterCowProfile;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.Cow;
import com.damasahhre.hooftrim.database.models.DeletedCow;
import com.damasahhre.hooftrim.database.models.DeletedReport;
import com.damasahhre.hooftrim.database.models.LastReport;
import com.damasahhre.hooftrim.database.models.Report;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.dialog.SureDialog;

import java.util.List;
import java.util.Objects;

/**
 * صفحه پروفایل گاو
 * با قابلیت حذف گاو
 */
public class CowProfileActivity extends AppCompatActivity {

    private ImageView outside;
    private ConstraintLayout menuLayout;
    private TextView title;
    private TextView lastVisit;
    private TextView nextVisit;
    private ImageView bookmark;
    private ImageView menu;
    private GridView reports;
    private Context context;
    private Cow cow;
    private GridViewAdapterCowProfile adapter;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattel_profile);
        context = this;
        title = findViewById(R.id.title_livestrok);
        bookmark = findViewById(R.id.bookmark_image);
        lastVisit = findViewById(R.id.count_value);
        nextVisit = findViewById(R.id.system_value);
        menu = findViewById(R.id.dropdown_menu);
        reports = findViewById(R.id.reports_list);
        ImageView exit = findViewById(R.id.back_icon);
        menuLayout = findViewById(R.id.menu_layout);
        outside = findViewById(R.id.outside);

        menu.setOnClickListener(view -> showMenu());
        outside.setOnClickListener(view -> hideMenu());
        exit.setOnClickListener(view -> finish());
        Constants.setImageBackBorder(this, exit);

        id = Objects.requireNonNull(getIntent().getExtras()).getLong(Constants.COW_ID);
        MyDao dao = DataBase.getInstance(this).dao();

        exit.setOnClickListener((v) -> finish());
        ConstraintLayout remove = findViewById(R.id.item_two);
        remove.setOnClickListener(view -> removeDialog());

    }

    public void removeDialog() {
        MyDao dao = DataBase.getInstance(this).dao();
        SureDialog dialog = new SureDialog(CowProfileActivity.this, getString(R.string.delete_question),
                getString(R.string.delete),
                () -> {
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        Cow cow = dao.getCow(id);
                        for (Report report : dao.getAllReportOfCow(cow.getId())) {
                            if (!report.created)
                                dao.insert(new DeletedReport(report.id));
                            dao.deleteReport(report);
                        }
                        if (!cow.getCreated())
                            dao.insert(new DeletedCow(cow.getId()));
                        dao.deleteCow(cow);

                        runOnUiThread(() -> {
                            hideMenu();
                            finish();
                        });
                    });
                },
                () -> runOnUiThread(this::hideMenu)
                ,
                getString(R.string.yes),
                getString(R.string.no));
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyDao dao = DataBase.getInstance(this).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            cow = dao.getCow(id);
            List<Report> reports = dao.getAllReportOfCow(cow.getId());
            runOnUiThread(() -> {
                title.setText(cow.getNumber(context));
                if (cow.getFavorite()) {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_fill));
                } else {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark));
                }
                adapter = new GridViewAdapterCowProfile(this, reports, cow.getId());
                this.reports.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
            LastReport lastVisit = dao.getLastReport(cow.getId());

            runOnUiThread(() -> {
                if (lastVisit.nextVisit != null) {
                    this.nextVisit.setText(lastVisit.nextVisit.toString(context));
                } else {
                    this.nextVisit.setText(R.string.no_visit_short);
                }
                if (lastVisit.lastVisit != null) {
                    this.lastVisit.setText(lastVisit.lastVisit.toString(context));
                } else {
                    this.lastVisit.setText(R.string.no_visit_short);
                }
            });
        });
        bookmark.setOnClickListener(view -> {
            if (cow != null) {
                cow.setFavorite(!cow.getFavorite());
                AppExecutors.getInstance().diskIO().execute(() -> {
                    cow.setSync(true);
                    dao.update(cow);
                });
                if (cow.getFavorite()) {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_fill));
                } else {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark));
                }
            }
        });
    }

    private void showMenu() {
        outside.setVisibility(View.VISIBLE);
        menuLayout.setVisibility(View.VISIBLE);
        menu.setVisibility(View.INVISIBLE);
    }

    private void hideMenu() {
        menu.setVisibility(View.VISIBLE);
        outside.setVisibility(View.GONE);
        menuLayout.setVisibility(View.GONE);
    }

}