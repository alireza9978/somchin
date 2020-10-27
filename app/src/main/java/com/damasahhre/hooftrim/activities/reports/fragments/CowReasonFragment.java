package com.damasahhre.hooftrim.activities.reports.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.reports.AddReportActivity;
import com.damasahhre.hooftrim.adapters.GridViewAdapterReasonAddReport;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.models.CheckBoxManager;


public class CowReasonFragment extends Fragment {

    private GridViewAdapterReasonAddReport adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cow_reason, container, false);
        Constants.setImageFront(requireContext(), view.findViewById(R.id.next_icon));
        Constants.setImageBack(requireContext(), view.findViewById(R.id.back_icon));

        GridView gridView = view.findViewById(R.id.reason_container);
        adapter = new GridViewAdapterReasonAddReport(requireContext(), CheckBoxManager.getCheckBoxManager().getReasons());
        gridView.setAdapter(adapter);

        view.findViewById(R.id.next_button).setOnClickListener(v -> {
            if (!CheckBoxManager.getCheckBoxManager().reasonSelected()) {
                Toast.makeText(requireContext(), R.string.toast_select_one, Toast.LENGTH_SHORT).show();
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
        adapter.notifyDataSetChanged();
    }
}