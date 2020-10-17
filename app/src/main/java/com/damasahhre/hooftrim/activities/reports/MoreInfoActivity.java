package com.damasahhre.hooftrim.activities.reports;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;

import java.util.Objects;

public class MoreInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        State state = (State) Objects.requireNonNull(getIntent().getExtras()).get(Constants.MORE_INFO_STATE);
        assert state != null;
        switch (state) {
            case info:
                break;
            case reason:
                break;
            case injury:
                break;
            case moreInfo:
                break;
        }
    }
}