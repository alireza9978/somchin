package com.damasahhre.hooftrim.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.reports.AddReportActivity;
import com.damasahhre.hooftrim.adapters.GridViewAdapterReasonAddReport;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.models.CheckBoxManager;


/**
 * المان دیالوگ برای گرفتن شماره انگشت
 */
public class SelectFingerDialog extends Dialog {


    public SelectFingerDialog(@NonNull final Context context, boolean editMode) {
        super(context);
        setContentView(R.layout.select_finger_dialog_layout);

        EditText edit = findViewById(R.id.input);
        GridView gridView = findViewById(R.id.grid);
        GridViewAdapterReasonAddReport adapter = new GridViewAdapterReasonAddReport(context, CheckBoxManager.getCheckBoxManager().getDialog());
        gridView.setAdapter(adapter);

        Button newInput = findViewById(R.id.new_input);
        if (editMode) {
            newInput.setVisibility(View.GONE);
        } else {
            newInput.setOnClickListener(view -> {
                Constants.hideKeyboard((AddReportActivity) context, findViewById(R.id.root).getWindowToken());
                int number = -1;
                if (edit.getText().toString().isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.empty_error), Toast.LENGTH_SHORT).show();
                } else {
                    number = Integer.parseInt(edit.getText().toString());
                }
                if (number >= 1 && number <= 8) {
                    if (CheckBoxManager.getCheckBoxManager().moreInfoSelected()) {
                        Toast.makeText(context, context.getString(R.string.tripel_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dismiss();
                    ((AddReportActivity) context).setFingerNumber(number);
                    ((AddReportActivity) context).addCowAndReportFast();
                } else {
                    Toast.makeText(context, "value error", Toast.LENGTH_SHORT).show();
                }
            });
        }


        Button ok = findViewById(R.id.ok);

        {
            int number = ((AddReportActivity) context).getFingerNumber();
            if (number != -1) {
                edit.setText("" + number);
            }
        }


        ok.setOnClickListener(v -> {
            Constants.hideKeyboard((AddReportActivity) context, findViewById(R.id.root).getWindowToken());
            int number = -1;
            if (edit.getText().toString().isEmpty()) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            } else {
                number = Integer.parseInt(edit.getText().toString());
            }
            if (number >= 1 && number <= 8) {
                dismiss();
                ((AddReportActivity) context).setFingerNumber(number);
            } else {
                Toast.makeText(context, "value error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public SelectFingerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SelectFingerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {

    }
}
