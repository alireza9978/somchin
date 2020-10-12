package com.damasahhre.hooftrim.activities.reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.badoualy.stepperindicator.StepperIndicator;
import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.reports.fragments.CowInfoFragment;
import com.damasahhre.hooftrim.activities.reports.fragments.CowInjuryFragment;
import com.damasahhre.hooftrim.activities.reports.fragments.CowReasonFragment;
import com.damasahhre.hooftrim.activities.reports.fragments.MoreInfoFragment;
import com.damasahhre.hooftrim.adapters.TabAdapterReport;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.Cow;
import com.damasahhre.hooftrim.database.models.Report;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.models.CheckBoxManager;
import com.damasahhre.hooftrim.models.DateContainer;
import com.damasahhre.hooftrim.ui_element.MyViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import static com.damasahhre.hooftrim.constants.Constants.DATE_SELECTION_REPORT_CREATE;
import static com.damasahhre.hooftrim.constants.Constants.DATE_SELECTION_REPORT_CREATE_END;
import static com.damasahhre.hooftrim.constants.Constants.DATE_SELECTION_RESULT;

public class AddReportActivity extends AppCompatActivity {

    private State state;
    private Cow cow;
    private TabAdapterReport adapter;
    private TabLayout tabLayout;
    private StepperIndicator stepperIndicator;
    private int fingerNumber;
    private DateContainer one;
    private DateContainer two;
    private int farmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        Bundle bundle = Objects.requireNonNull(getIntent().getExtras());
        int id = bundle.getInt(Constants.COW_ID, -1);
        if (id == -1) {
            farmId = bundle.getInt(Constants.FARM_ID);
        }

        tabLayout = new TabLayout(this);
        state = State.info;
        adapter = new TabAdapterReport(this, getSupportFragmentManager());
        adapter.addFragment(new CowInfoFragment());
        adapter.addFragment(new CowReasonFragment());
        adapter.addFragment(new CowInjuryFragment());
        adapter.addFragment(new MoreInfoFragment());

        MyViewPager viewPager = findViewById(R.id.pager_id);
        viewPager.setAdapter(adapter);
        viewPager.setEnableSwipe(false);
        tabLayout.setupWithViewPager(viewPager);

        stepperIndicator = findViewById(R.id.state_indicator);
        ImageView moreInfo = findViewById(R.id.info_image);
        ImageView exit = findViewById(R.id.close_image);

        exit.setOnClickListener(view -> finish());
        moreInfo.setOnClickListener(view -> {
            Intent intent = new Intent(this, MoreInfoActivity.class);
            intent.putExtra(Constants.MORE_INFO_STATE, state);
            startActivity(intent);
        });

        MyDao dao = DataBase.getInstance(this).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (id != -1) {
                cow = dao.getCow(id);
                if (cow != null)
                    ((CowInfoFragment) adapter.getItem(0)).setCowNumber(cow.getNumber());
            } else {
                cow = null;
            }
        });
    }

    private String TAG = "AddReport";
    private void addCowAndReport() {
        MyDao dao = DataBase.getInstance(this).dao();
        if (cow != null) {
            Log.i(TAG, "addCowAndReport: cow_id = " + cow.getId());
            AppExecutors.getInstance().diskIO().execute(() -> {
                Report report = new Report();
                report.visit = one.exportStart();
                if (two != null) {
                    report.nextVisit = two.exportStart();
                }else{
                    report.nextVisit = null;
                }
                report.legAreaNumber = ((CowInjuryFragment) adapter.getItem(2)).getSelected();
                report.fingerNumber = this.fingerNumber;
                report.description = ((MoreInfoFragment) adapter.getItem(3)).getMoreInfo();
                report.cowId = cow.getId();
                CheckBoxManager.getCheckBoxManager().setBooleans(report);
                dao.insert(report);
                runOnUiThread(this::finish);

            });
        } else {
            AppExecutors.getInstance().diskIO().execute(() -> {

                int cowNumber = ((CowInfoFragment) adapter.getItem(0)).getNumber();
                Cow cow = dao.getCow(cowNumber,farmId);
                if (cow == null) {
                    cow = new Cow(cowNumber, false, farmId);
                    dao.insert(cow);
                }
                cow = dao.getCow(cowNumber,farmId);
                Report report = new Report();
                report.visit = one.exportStart();
                if (two != null) {
                    report.nextVisit = two.exportStart();
                }else{
                    report.nextVisit = null;
                }
                report.legAreaNumber = ((CowInjuryFragment) adapter.getItem(2)).getSelected();
                report.fingerNumber = this.fingerNumber;
                report.description = ((MoreInfoFragment) adapter.getItem(3)).getMoreInfo();
                report.cowId = cow.getId();
                CheckBoxManager.getCheckBoxManager().setBooleans(report);
                dao.insert(report);
                runOnUiThread(this::finish);
            });
        }
    }

    public void next() {
        switch (state) {
            case info:
                state = State.reason;
                break;
            case reason:
                state = State.injury;
                break;
            case injury:
                state = State.moreInfo;
                break;
            case moreInfo:
                addCowAndReport();
                return;
        }
        tabLayout.selectTab(tabLayout.getTabAt(State.getNumber(state)));
        stepperIndicator.setCurrentStep(State.getNumber(state));
    }

    public void back() {
        switch (state) {
            case info:
                break;
            case reason:
                state = State.info;
                break;
            case injury:
                state = State.reason;
                break;
            case moreInfo:
                state = State.injury;
                break;
        }
        tabLayout.selectTab(tabLayout.getTabAt(State.getNumber(state)));
        stepperIndicator.setCurrentStep(State.getNumber(state));
    }

    public void setFingerNumber(int fingerNumber) {
        this.fingerNumber = fingerNumber;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case DATE_SELECTION_REPORT_CREATE: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    DateContainer container = (DateContainer) Objects.requireNonNull(data.getExtras()).get(DATE_SELECTION_RESULT);
                    assert container != null;
                    one = container;
                    Log.i(TAG, "onActivityResult: " + one.exportStart());
                    ((CowInfoFragment) adapter.getItem(0)).setDate(container.toString(this));
                }
                break;
            }
            case DATE_SELECTION_REPORT_CREATE_END: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    DateContainer container = (DateContainer) Objects.requireNonNull(data.getExtras()).get(DATE_SELECTION_RESULT);
                    assert container != null;
                    two = container;
                    Log.i(TAG, "onActivityResult: " + two.exportStart());
                    ((MoreInfoFragment) adapter.getItem(3)).setDate(container.toString(this));
                }
                break;
            }
            default: {
                break;
            }
        }
    }

}