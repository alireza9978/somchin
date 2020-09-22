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
import com.damasahhre.hooftrim.database.models.Cow;
import com.damasahhre.hooftrim.models.DateContainer;
import com.damasahhre.hooftrim.ui_element.MyViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import static com.damasahhre.hooftrim.constants.Constants.DATE_SELECTION_REPORT_CREATE;
import static com.damasahhre.hooftrim.constants.Constants.DATE_SELECTION_RESULT;
import static com.damasahhre.hooftrim.constants.Constants.DATE_SELECTION_SEARCH_COW;

public class AddReportActivity extends AppCompatActivity {

    private State state;
    private Cow cow;
    private TabAdapterReport adapter;
    private MyViewPager viewPager;
    private TabLayout tabLayout;
    private StepperIndicator stepperIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

//        int id = Objects.requireNonNull(getIntent().getExtras()).getInt(Constants.COW_ID);
//        MyDao dao = DataBase.getInstance(this).dao();
//        AppExecutors.getInstance().diskIO().execute(() -> {
//            cow = dao.getCow(id);
//
//        });
        tabLayout = new TabLayout(this);
        state = State.info;
        adapter = new TabAdapterReport(this, getSupportFragmentManager());
        adapter.addFragment(new CowInfoFragment());
        adapter.addFragment(new CowReasonFragment());
        adapter.addFragment(new CowInjuryFragment());
        adapter.addFragment(new MoreInfoFragment());

        viewPager = findViewById(R.id.pager_id);
        viewPager.setAdapter(adapter);
        viewPager.setEnableSwipe(false);
        tabLayout.setupWithViewPager(viewPager);

        stepperIndicator = findViewById(R.id.state_indicator);
        ImageView moreInfo = findViewById(R.id.info_image);
        ImageView exit = findViewById(R.id.close_image);

        exit.setOnClickListener(view -> finish());
        moreInfo.setOnClickListener(view -> {
//            Intent intent = new Intent()
            //todo go to more info page
        });

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
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case DATE_SELECTION_REPORT_CREATE: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    DateContainer container = (DateContainer) Objects.requireNonNull(data.getExtras()).get(DATE_SELECTION_RESULT);
                    assert container != null;
                    ((CowInfoFragment) adapter.getItem(0)).setDate(container.toString(this));
                }
                break;
            }
            case DATE_SELECTION_SEARCH_COW: {
                break;
            }
            default: {
                break;
            }
        }
    }

    private enum State {
        info, reason, injury, moreInfo;

        public static int getNumber(State state) {
            switch (state) {
                case info:
                    return 0;
                case reason:
                    return 1;
                case injury:
                    return 2;
                case moreInfo:
                    return 3;
            }
            return 0;
        }

    }


}