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
    private int fingerNumber = -1;
    private DateContainer one;
    private DateContainer two;
    private int farmId;
    private String mode;
    private Integer reportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        Bundle bundle = Objects.requireNonNull(getIntent().getExtras());
        mode = bundle.getString(Constants.REPORT_MODE);
        assert mode != null;

        boolean persian = Constants.getDefualtlanguage(this).equals("fa");
        stepperIndicator = findViewById(R.id.state_indicator);
        if (persian) {
            stepperIndicator.setRotation(180);
            stepperIndicator.setRotationX(180);
            stepperIndicator.setShowDoneIcon(false);
        }


        if (mode.equals(Constants.EDIT_REPORT)) {
            Log.i("ADD_REPORT", "onCreate: edit mode");
            reportId = bundle.getInt(Constants.REPORT_ID);
            MyDao dao = DataBase.getInstance(this).dao();
            AppExecutors.getInstance().diskIO().execute(() -> {
                Report report = dao.getReport(reportId);
                DateContainer container;
                DateContainer container_two;
                int[] temp = report.visit.convert(this);
                if (Constants.getDefualtlanguage(this).equals("fa")) {
                    DateContainer.MyDate date = new DateContainer.MyDate(true, temp[2], temp[1], temp[0]);
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                } else {
                    DateContainer.MyDate date = new DateContainer.MyDate(false, temp[2], temp[1], temp[0]);
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                }
                one = container;
                if (report.nextVisit != null) {
                    temp = report.nextVisit.convert(this);
                    if (Constants.getDefualtlanguage(this).equals("fa")) {
                        DateContainer.MyDate date = new DateContainer.MyDate(true, temp[2], temp[1], temp[0]);
                        container_two = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                    } else {
                        DateContainer.MyDate date = new DateContainer.MyDate(false, temp[2], temp[1], temp[0]);
                        container_two = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                    }
                    two = container_two;
                }
                cow = dao.getCow(report.cowId);
                runOnUiThread(() -> {
                    CheckBoxManager.getCheckBoxManager().setBooleansFromReport(report);
                    fingerNumber = report.fingerNumber;

                    tabLayout = new TabLayout(this);
                    state = State.info;
                    adapter = new TabAdapterReport(this, getSupportFragmentManager());
                    adapter.addFragment(new CowInfoFragment(cow.getNumber(), one.toString(this)));
                    adapter.addFragment(new CowReasonFragment());
                    adapter.addFragment(new CowInjuryFragment(report.legAreaNumber, report.rightSide));
                    if (two != null)
                        adapter.addFragment(new MoreInfoFragment(two.toString(this), report.description));
                    else
                        adapter.addFragment(new MoreInfoFragment());

                    MyViewPager viewPager = findViewById(R.id.pager_id);
                    viewPager.setAdapter(adapter);
                    viewPager.setEnableSwipe(false);
                    tabLayout.setupWithViewPager(viewPager);

                    ImageView moreInfo = findViewById(R.id.info_image);
                    ImageView exit = findViewById(R.id.close_image);

                    exit.setOnClickListener(view -> finish());
                    moreInfo.setOnClickListener(view -> {
                        Intent intent = new Intent(this, MoreInfoActivity.class);
                        intent.putExtra(Constants.MORE_INFO_STATE, state);
                        startActivity(intent);
                    });

                });


            });
        } else if (mode.equals(Constants.REPORT_CREATE)) {
            CheckBoxManager.getCheckBoxManager().reset();
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
                    runOnUiThread(() -> {
                        if (cow != null)
                            ((CowInfoFragment) adapter.getItem(0)).setCowNumber(cow.getNumber());
                    });
                } else {
                    cow = null;
                }
                runOnUiThread(() -> {
                    one = DateContainer.getToday(this, persian);
                    ((CowInfoFragment) adapter.getItem(0)).setDate(one.toString(this));
                });
            });


        }


    }

    private void addCowAndReport() {
        MyDao dao = DataBase.getInstance(this).dao();
        if (mode.equals(Constants.EDIT_REPORT)) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                Log.i("ADD_REPORT", "onCreate: 3");

                Report report = new Report();
                report.visit = one.exportStart();
                if (two != null) {
                    report.nextVisit = two.exportStart();
                } else {
                    report.nextVisit = null;
                }
                report.legAreaNumber = ((CowInjuryFragment) adapter.getItem(2)).getSelected();
                report.rightSide = ((CowInjuryFragment) adapter.getItem(2)).getRightSide();
                report.fingerNumber = this.fingerNumber;
                report.description = ((MoreInfoFragment) adapter.getItem(3)).getMoreInfo();
                report.cowId = cow.getId();
                CheckBoxManager.getCheckBoxManager().setBooleansOnReport(report);
                report.id = reportId;
                dao.update(report);
                runOnUiThread(this::finish);
            });
        } else {
            if (cow != null) {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    Log.i("ADD_REPORT", "onCreate: 1");
                    Report report = new Report();
                    report.visit = one.exportStart();
                    if (two != null) {
                        report.nextVisit = two.exportStart();
                    } else {
                        report.nextVisit = null;
                    }
                    report.legAreaNumber = ((CowInjuryFragment) adapter.getItem(2)).getSelected();
                    report.fingerNumber = this.fingerNumber;
                    report.rightSide = ((CowInjuryFragment) adapter.getItem(2)).getRightSide();
                    report.description = ((MoreInfoFragment) adapter.getItem(3)).getMoreInfo();
                    report.cowId = cow.getId();
                    CheckBoxManager.getCheckBoxManager().setBooleansOnReport(report);
                    dao.insert(report);
                    runOnUiThread(this::finish);

                });
            } else {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    Log.i("ADD_REPORT", "onCreate: 2");
                    int cowNumber = ((CowInfoFragment) adapter.getItem(0)).getNumber();
                    Cow cow = dao.getCow(cowNumber, farmId);
                    if (cow == null) {
                        cow = new Cow(cowNumber, false, farmId);
                        dao.insert(cow);
                    }
                    cow = dao.getCow(cowNumber, farmId);
                    Report report = new Report();
                    report.visit = one.exportStart();
                    if (two != null) {
                        report.nextVisit = two.exportStart();
                    } else {
                        report.nextVisit = null;
                    }
                    report.legAreaNumber = ((CowInjuryFragment) adapter.getItem(2)).getSelected();
                    report.rightSide = ((CowInjuryFragment) adapter.getItem(2)).getRightSide();
                    report.fingerNumber = this.fingerNumber;
                    report.description = ((MoreInfoFragment) adapter.getItem(3)).getMoreInfo();
                    report.cowId = cow.getId();
                    CheckBoxManager.getCheckBoxManager().setBooleansOnReport(report);
                    dao.insert(report);
                    runOnUiThread(this::finish);
                });
            }
        }
    }

    public void addCowAndReportFast() {
        Log.i("ADD_REPORT", "onCreate: fast");
        MyDao dao = DataBase.getInstance(this).dao();
        if (cow != null) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                Report report = new Report();
                report.visit = one.exportStart();
                if (two != null) {
                    report.nextVisit = two.exportStart();
                } else {
                    report.nextVisit = null;
                }
                report.legAreaNumber = ((CowInjuryFragment) adapter.getItem(2)).getSelected();
                report.fingerNumber = this.fingerNumber;
                report.rightSide = ((CowInjuryFragment) adapter.getItem(2)).getRightSide();
                report.description = ((MoreInfoFragment) adapter.getItem(3)).getMoreInfo();
                report.cowId = cow.getId();
                CheckBoxManager.getCheckBoxManager().setBooleansOnReportFast(report);
                dao.insert(report);
                runOnUiThread(() -> {
                    state = State.reason;
                    this.fingerNumber = -1;
                    ((CowInjuryFragment) adapter.getItem(2)).reset();
                    tabLayout.selectTab(tabLayout.getTabAt(State.getNumber(state)), false);
                    stepperIndicator.setCurrentStep(State.getNumber(state));
                });
            });
        } else {
            AppExecutors.getInstance().diskIO().execute(() -> {
                int cowNumber = ((CowInfoFragment) adapter.getItem(0)).getNumber();
                Cow cow = dao.getCow(cowNumber, farmId);
                if (cow == null) {
                    cow = new Cow(cowNumber, false, farmId);
                    dao.insert(cow);
                }
                cow = dao.getCow(cowNumber, farmId);
                this.cow = cow;
                Report report = new Report();
                report.visit = one.exportStart();
                if (two != null) {
                    report.nextVisit = two.exportStart();
                } else {
                    report.nextVisit = null;
                }
                report.legAreaNumber = ((CowInjuryFragment) adapter.getItem(2)).getSelected();
                report.rightSide = ((CowInjuryFragment) adapter.getItem(2)).getRightSide();
                report.fingerNumber = this.fingerNumber;
                report.description = ((MoreInfoFragment) adapter.getItem(3)).getMoreInfo();
                report.cowId = cow.getId();
                CheckBoxManager.getCheckBoxManager().setBooleansOnReportFast(report);
                dao.insert(report);
                runOnUiThread(() -> {
                    state = State.reason;
                    this.fingerNumber = -1;
                    ((CowInjuryFragment) adapter.getItem(2)).reset();
                    tabLayout.selectTab(tabLayout.getTabAt(State.getNumber(state)), false);
                    stepperIndicator.setCurrentStep(State.getNumber(state));
                });
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
        tabLayout.selectTab(tabLayout.getTabAt(State.getNumber(state)), false);
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
        tabLayout.selectTab(tabLayout.getTabAt(State.getNumber(state)), false);
        stepperIndicator.setCurrentStep(State.getNumber(state));
    }

    public void setFingerNumber(int fingerNumber) {
        this.fingerNumber = fingerNumber;
    }

    public int getFingerNumber() {
        return fingerNumber;
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