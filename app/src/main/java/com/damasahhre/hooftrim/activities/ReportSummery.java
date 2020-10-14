package com.damasahhre.hooftrim.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.adapters.GridViewAdapterItemInSummery;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.MyReport;
import com.damasahhre.hooftrim.database.models.Report;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.models.DateContainer;
import com.damasahhre.hooftrim.ui_element.ExpandableHeightGridView;

import java.util.ArrayList;
import java.util.Objects;

public class ReportSummery extends AppCompatActivity {

    private ImageView outside;
    private ImageView menu;
    private ConstraintLayout menuLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_summery);

        Integer reportId = Objects.requireNonNull(getIntent().getExtras()).getInt(Constants.REPORT_ID);

        ImageView back = findViewById(R.id.back_icon);
        Constants.setImageBackBorder(this, back);
        menuLayout = findViewById(R.id.menu_layout);
        outside = findViewById(R.id.outside);
        menu = findViewById(R.id.dropdown_menu);
        TextView cowText = findViewById(R.id.cow_title);
        ExpandableHeightGridView reasonsGrid = findViewById(R.id.reason_grid);
        ExpandableHeightGridView moreInfoGrid = findViewById(R.id.more_info_grid);
        TextView area = findViewById(R.id.area_text);
        TextView lastVisit = findViewById(R.id.last_visit_text);
        TextView description = findViewById(R.id.description_text);

        GridViewAdapterItemInSummery reasonAdapter = new GridViewAdapterItemInSummery(this, new ArrayList<>());
        GridViewAdapterItemInSummery moreInfoAdapter = new GridViewAdapterItemInSummery(this, new ArrayList<>());

        MyDao dao = DataBase.getInstance(this).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            MyReport myReport = dao.myReportWithCow(reportId);
            Report report = myReport.report;
            runOnUiThread(() -> {
                cowText.setText(getString(R.string.report));
                cowText.append(" " + myReport.report.id);
                cowText.append(getString(R.string.cow_title));
                cowText.append("" + myReport.cowNumber);

                area.setText(getString(R.string.injury_area) + " " + report.legAreaNumber +
                        ", " + getString(R.string.finger) + " " + report.fingerNumber);
                DateContainer container;
                if (Constants.getDefualtlanguage(this).equals("fa")) {
                    int[] temp = report.visit.convert(this);
                    DateContainer.MyDate date = new DateContainer.MyDate(true, temp[2], temp[1], temp[0]);
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                } else {
                    DateContainer.MyDate date = new DateContainer.MyDate(false, report.visit.getYear(), report.visit.getMonth(), report.visit.getDay());
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                }
                lastVisit.setText(container.toString(this));
                description.setText(report.description);

                reasonAdapter.setItems(report.getSelectedReason());
                reasonsGrid.setAdapter(reasonAdapter);
                reasonAdapter.notifyDataSetChanged();

                moreInfoAdapter.setItems(report.getSelectedOtherInfo());
                moreInfoGrid.setAdapter(moreInfoAdapter);
                moreInfoAdapter.notifyDataSetChanged();
            });
        });

        back.setOnClickListener(view -> finish());
        menu.setOnClickListener(view -> showMenu());
        outside.setOnClickListener(view -> hideMenu());
        ConstraintLayout edit = findViewById(R.id.item_one);
        ConstraintLayout remove = findViewById(R.id.item_two);
        edit.setOnClickListener(view -> {
            hideMenu();
        });
        remove.setOnClickListener(view -> {
            hideMenu();
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