package com.damasahhre.hooftrim.activities.reports;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;

import java.util.Objects;

/**
 * صفحه مربوط به اطلاعات موجود در توضیحات چهار صفحه‌ی ثبت گزارش
 * با توجه به ورودی داده مربوط نشان داده می‌شود
 */
public class MoreInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        findViewById(R.id.close_image).setOnClickListener(view -> finish());
        Constants.setImageBack(this, findViewById(R.id.close_image));
        State state = (State) Objects.requireNonNull(getIntent().getExtras()).get(Constants.MORE_INFO_STATE);
        assert state != null;

        switch (state) {
            case info:
                findViewById(R.id.one).setVisibility(View.VISIBLE);
                break;
            case reason: {
                if (Constants.getDefaultLanguage(this).equals("fa")) {
                    findViewById(R.id.two_fa).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.two).setVisibility(View.VISIBLE);
                }
                break;
            }
            case injury:
                if (Constants.getDefaultLanguage(this).equals("fa")) {
                    findViewById(R.id.three_fa).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.three).setVisibility(View.VISIBLE);
                }

                break;
            case moreInfo:
                if (Constants.getDefaultLanguage(this).equals("fa")) {
                    findViewById(R.id.four_fa).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.four).setVisibility(View.VISIBLE);
                }

                break;
        }

    }
}