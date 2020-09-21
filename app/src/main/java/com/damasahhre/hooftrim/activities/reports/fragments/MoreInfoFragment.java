package com.damasahhre.hooftrim.activities.reports.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.reports.AddReportActivity;
import com.damasahhre.hooftrim.constants.Constants;

public class MoreInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_info, container, false);
//        Constants.setImageFront(requireContext(), view.findViewById(R.id.next_icon));
//        Constants.setImageBack(requireContext(), view.findViewById(R.id.back_icon));
//
//        view.findViewById(R.id.next_button).setOnClickListener(v -> {
//            ((AddReportActivity) requireActivity()).next();
//        });
//        view.findViewById(R.id.back_button).setOnClickListener(v -> {
//            ((AddReportActivity) requireActivity()).back();
//        });
        return view;
    }
}