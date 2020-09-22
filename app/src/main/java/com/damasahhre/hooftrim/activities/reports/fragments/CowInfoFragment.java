package com.damasahhre.hooftrim.activities.reports.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.DateSelectionActivity;
import com.damasahhre.hooftrim.activities.reports.AddReportActivity;
import com.damasahhre.hooftrim.constants.Constants;

public class CowInfoFragment extends Fragment {

    private ConstraintLayout date_container;
    private TextView date_text;
    private String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cow_info, container, false);
        Constants.setImageFront(requireContext(), view.findViewById(R.id.next_icon));

        date_container = view.findViewById(R.id.date_container);
        ConstraintLayout number_container = view.findViewById(R.id.cow_number_container);
        EditText number_edit = view.findViewById(R.id.cow_name_text);
        date_text = view.findViewById(R.id.date_text);

        number_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    number_container.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
                } else {
                    number_container.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
                }
            }
        });
        date_container.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireContext(), DateSelectionActivity.class);
            intent.setAction(Constants.DateSelectionMode.SINGLE);
            requireActivity().startActivityForResult(intent, Constants.DATE_SELECTION_REPORT_CREATE);
        });

        ConstraintLayout button = view.findViewById(R.id.next_button);
        button.setOnClickListener(view1 -> ((AddReportActivity) requireActivity()).next());
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
    }

    public void setDate(String date) {
        if (date != null) {
            this.date = date;
            date_text.setText(date);
        }
    }


}