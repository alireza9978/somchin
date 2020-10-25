package com.damasahhre.hooftrim.activities.reports.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.DateSelectionActivity;
import com.damasahhre.hooftrim.activities.reports.AddReportActivity;
import com.damasahhre.hooftrim.adapters.GridViewAdapterReasonAddReport;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.models.CheckBoxManager;

public class MoreInfoFragment extends Fragment {

    private EditText moreInfo;
    private ConstraintLayout date_container;
    private TextView date_text;
    private String date;
    private String description;

    public MoreInfoFragment(String date, String description) {
        this.date = date;
        this.description = description;
    }

    public MoreInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_info, container, false);
        Constants.setImageBack(requireContext(), view.findViewById(R.id.back_icon));
        moreInfo = view.findViewById(R.id.more_info_edit);
        date_container = view.findViewById(R.id.date_container);
        date_text = view.findViewById(R.id.date_text);

        date_container.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireContext(), DateSelectionActivity.class);
            intent.setAction(Constants.DateSelectionMode.SINGLE);
            requireActivity().startActivityForResult(intent, Constants.DATE_SELECTION_REPORT_CREATE_END);
        });

        GridView gridView = view.findViewById(R.id.reason_container);
        GridViewAdapterReasonAddReport adapter = new GridViewAdapterReasonAddReport(requireContext(), CheckBoxManager.getCheckBoxManager().getMoreInfo());
        gridView.setAdapter(adapter);

        view.findViewById(R.id.next_button).setOnClickListener(v -> {
            if (CheckBoxManager.getCheckBoxManager().moreInfoSelected()) {
                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                return;
            }
            ((AddReportActivity) requireActivity()).next();
        });
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            ((AddReportActivity) requireActivity()).back();
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (date != null) {
            if (this.date.length() == 0) {
                date_container.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
            } else {
                date_container.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
            }
            date_text.setText(date);
        }
        if (description != null) {
            if (moreInfo != null) {
                moreInfo.setText(description);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        description = getMoreInfo();
    }

    public String getMoreInfo() {
        return moreInfo.getText().toString();
    }

    public void setDate(String date) {
        if (date != null) {
            this.date = date;
            date_text.setText(date);
        }
    }


}