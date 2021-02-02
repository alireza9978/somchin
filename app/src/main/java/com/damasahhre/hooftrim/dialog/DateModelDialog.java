package com.damasahhre.hooftrim.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.FarmProfileActivity;
import com.damasahhre.hooftrim.activities.reports.AddReportActivity;
import com.damasahhre.hooftrim.adapters.GridViewAdapterReasonAddReport;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.models.CheckBoxManager;


/**
 * المان دیالوگ برای گرفتن شماره انگشت
 */
public class DateModelDialog extends Dialog {


    public DateModelDialog(@NonNull final FarmProfileActivity activity) {
        super(activity);
        setContentView(R.layout.date_model_dialog_layout);

        Button single = findViewById(R.id.single_date);
        Button range = findViewById(R.id.range_date);
        Button all = findViewById(R.id.all_date);

        single.setOnClickListener(v -> {
            activity.getDate(true);
            dismiss();
        });
        range.setOnClickListener(v -> {
            activity.getDate(false);
            dismiss();
        });
        all.setOnClickListener(v -> {
            activity.export();
            dismiss();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {

    }
}
