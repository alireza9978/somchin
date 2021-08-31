package com.damasahhre.hooftrim.activities.reports;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.badoualy.stepperindicator.StepperIndicator;
import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.reports.fragments.CowInfoFragment;
import com.damasahhre.hooftrim.activities.reports.fragments.CowInjuryFragment;
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

import java.util.Objects;

import static com.damasahhre.hooftrim.constants.Constants.DATE_SELECTION_REPORT_CREATE;
import static com.damasahhre.hooftrim.constants.Constants.DATE_SELECTION_REPORT_CREATE_END;
import static com.damasahhre.hooftrim.constants.Constants.DATE_SELECTION_RESULT;

/**
 * صفحه اصلی مدیریت فراین ثبت گزارش
 * دارای دو حالت ثبت و تغییر
 * حالت سریع برای پر نکردن صفحه چهارم از مراحل ثبت گزارش
 */
public class AddReportActivity extends AppCompatActivity {

    private State state;
    private Cow cow;
    private TabAdapterReport adapter;
    private StepperIndicator stepperIndicator;
    private int fingerNumber = -1;
    private DateContainer one;
    private DateContainer two;
    private Long farmId;
    private String mode;
    private Long reportId;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        Bundle bundle = Objects.requireNonNull(getIntent().getExtras());
        mode = bundle.getString(Constants.REPORT_MODE);
        assert mode != null;

        boolean persian = Constants.getDefaultLanguage(this).equals("fa");
        stepperIndicator = findViewById(R.id.state_indicator);
        if (persian) {
            stepperIndicator.setRotation(180);
            stepperIndicator.setRotationX(180);
            stepperIndicator.setShowDoneIcon(false);
        }
        viewPager = findViewById(R.id.pager_id);

        if (mode.equals(Constants.EDIT_REPORT)) {
            reportId = bundle.getLong(Constants.REPORT_ID);
            MyDao dao = DataBase.getInstance(this).dao();
            AppExecutors.getInstance().diskIO().execute(() -> {
                Report report = dao.getReport(reportId);
                DateContainer container;
                DateContainer container_two;
                int[] temp = report.visit.convert(this);
                if (Constants.getDefaultLanguage(this).equals("fa")) {
                    DateContainer.MyDate date = new DateContainer.MyDate(true, temp[2], temp[1], temp[0]);
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                } else {
                    DateContainer.MyDate date = new DateContainer.MyDate(false, temp[2], temp[1], temp[0]);
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                }
                one = container;
                if (report.nextVisit != null) {
                    temp = report.nextVisit.convert(this);
                    if (Constants.getDefaultLanguage(this).equals("fa")) {
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

                    state = State.info;
                    String next = null;
                    if (two != null) {
                        next = two.toString(this);
                    }
                    adapter = new TabAdapterReport(this, cow.getNumber(), one.toString(this), next, report.legAreaNumber, report.rightSide, report.description);

                    viewPager.setAdapter(adapter);
                    viewPager.setUserInputEnabled(false);

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
            long id = bundle.getLong(Constants.COW_ID, -1L);
            if (id == -1) {
                farmId = bundle.getLong(Constants.FARM_ID);
            }

            state = State.info;
            adapter = new TabAdapterReport(this);

            viewPager.setOffscreenPageLimit(2);
            viewPager.setUserInputEnabled(false);
            viewPager.setAdapter(adapter);

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
                            ((CowInfoFragment) adapter.getFragment(0)).setCowNumber(cow.getNumber());
                    });
                } else {
                    cow = null;
                }
                runOnUiThread(() -> {
                    one = DateContainer.getToday(this, persian);
                    ((CowInfoFragment) adapter.getFragment(0)).setDate(one.toString(this));
                });
            });


        }


    }

    private void addCowAndReport() {
        MyDao dao = DataBase.getInstance(this).dao();
        if (mode.equals(Constants.EDIT_REPORT)) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                Report report = new Report();
                report.created = false;
                report.visit = one.exportStart();
                if (two != null) {
                    report.nextVisit = two.exportStart();
                } else {
                    report.nextVisit = null;
                }
                report.legAreaNumber = ((CowInjuryFragment) adapter.getFragment(2)).getSelected();
                report.rightSide = ((CowInjuryFragment) adapter.getFragment(2)).getRightSide();
                report.fingerNumber = this.fingerNumber;
                report.description = ((MoreInfoFragment) adapter.getFragment(3)).getMoreInfo();
                report.cowId = cow.getId();
                CheckBoxManager.getCheckBoxManager().setBooleansOnReport(report);
                report.id = reportId;
                report.sync = true;
                dao.update(report);
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.data_added, Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        } else {
            if (cow != null) {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    Report report = new Report();
                    report.visit = one.exportStart();
                    if (two != null) {
                        report.nextVisit = two.exportStart();
                    } else {
                        report.nextVisit = null;
                    }
                    report.legAreaNumber = ((CowInjuryFragment) adapter.getFragment(2)).getSelected();
                    report.fingerNumber = this.fingerNumber;
                    report.rightSide = ((CowInjuryFragment) adapter.getFragment(2)).getRightSide();
                    report.description = ((MoreInfoFragment) adapter.getFragment(3)).getMoreInfo();
                    report.cowId = cow.getId();
                    CheckBoxManager.getCheckBoxManager().setBooleansOnReport(report);
                    dao.insert(report);
                    runOnUiThread(() -> {
                        Toast.makeText(this, R.string.data_added, Toast.LENGTH_SHORT).show();
                        finish();
                    });

                });
            } else {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    int cowNumber = ((CowInfoFragment) adapter.getFragment(0)).getNumber();
                    Cow cow = dao.getCow(cowNumber, farmId);
                    if (cow == null) {
                        cow = new Cow(cowNumber, false, farmId, true, true);
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
                    report.legAreaNumber = ((CowInjuryFragment) adapter.getFragment(2)).getSelected();
                    report.rightSide = ((CowInjuryFragment) adapter.getFragment(2)).getRightSide();
                    report.fingerNumber = this.fingerNumber;
                    report.description = ((MoreInfoFragment) adapter.getFragment(3)).getMoreInfo();
                    report.cowId = cow.getId();
                    CheckBoxManager.getCheckBoxManager().setBooleansOnReport(report);
                    dao.insert(report);
                    runOnUiThread(() -> {
                        Toast.makeText(this, R.string.data_added, Toast.LENGTH_SHORT).show();
                        finish();
                    });
                });
            }
        }
    }

    public void addCowAndReportFast() {
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
                report.legAreaNumber = ((CowInjuryFragment) adapter.getFragment(2)).getSelected();
                report.fingerNumber = this.fingerNumber;
                report.rightSide = ((CowInjuryFragment) adapter.getFragment(2)).getRightSide();
                report.description = ((MoreInfoFragment) adapter.getFragment(3)).getMoreInfo();
                report.cowId = cow.getId();
                CheckBoxManager.getCheckBoxManager().setBooleansOnReportFast(report);
                dao.insert(report);
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.data_added, Toast.LENGTH_SHORT).show();
                    state = State.injury;
                    this.fingerNumber = -1;
                    ((CowInjuryFragment) adapter.getFragment(2)).reset();
                    viewPager.setCurrentItem(State.getNumber(state));
                    stepperIndicator.setCurrentStep(State.getNumber(state));
                });
            });
        } else {
            AppExecutors.getInstance().diskIO().execute(() -> {
                int cowNumber = ((CowInfoFragment) adapter.getFragment(0)).getNumber();
                Cow cow = dao.getCow(cowNumber, farmId);
                if (cow == null) {
                    cow = new Cow(cowNumber, false, farmId, true, true);
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
                report.legAreaNumber = ((CowInjuryFragment) adapter.getFragment(2)).getSelected();
                report.rightSide = ((CowInjuryFragment) adapter.getFragment(2)).getRightSide();
                report.fingerNumber = this.fingerNumber;
                report.description = ((MoreInfoFragment) adapter.getFragment(3)).getMoreInfo();
                report.cowId = cow.getId();
                CheckBoxManager.getCheckBoxManager().setBooleansOnReportFast(report);
                dao.insert(report);
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.data_added, Toast.LENGTH_SHORT).show();
                    state = State.injury;
                    this.fingerNumber = -1;
                    ((CowInjuryFragment) adapter.getFragment(2)).reset();
                    viewPager.setCurrentItem(State.getNumber(state));
                    stepperIndicator.setCurrentStep(State.getNumber(state));
                });
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard();
    }

    public void hideKeyboard() {
        Constants.hideKeyboard(this, findViewById(R.id.root).getWindowToken());
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
                ((MoreInfoFragment) adapter.getFragment(3)).setReportDate(one.exportStart());
                break;
            case moreInfo:
                addCowAndReport();
                return;
        }
        viewPager.setCurrentItem(State.getNumber(state));
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
        viewPager.setCurrentItem(State.getNumber(state));
        stepperIndicator.setCurrentStep(State.getNumber(state));
    }

    public int getFingerNumber() {
        return fingerNumber;
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
                    ((CowInfoFragment) adapter.getFragment(0)).setDate(container.toString(this));
                }
                break;
            }
            case DATE_SELECTION_REPORT_CREATE_END: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    DateContainer container = (DateContainer) Objects.requireNonNull(data.getExtras()).get(DATE_SELECTION_RESULT);
                    assert container != null;
                    two = container;
                    ((MoreInfoFragment) adapter.getFragment(3)).setDate(container.toString(this));
                }
                break;
            }
            default: {
                break;
            }
        }
    }

}