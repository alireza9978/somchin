package com.damasahhre.hooftrim.activities.tabs.report_activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.DateSelectionActivity;
import com.damasahhre.hooftrim.activities.FarmSelectionActivity;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.models.DateContainer;

public class InjuriesFragment extends Fragment {

    private ConstraintLayout farmLayout;
    private TextView farmText;

    private ConstraintLayout dateLayout;
    private TextView dateText;

    private int farmId = -1;
    private DateContainer date = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_injuries, container, false);
        farmLayout = view.findViewById(R.id.livestock_container);
        dateLayout = view.findViewById(R.id.date_container);
        farmText = view.findViewById(R.id.livestock_name_text);
        dateText = view.findViewById(R.id.date_text);

        dateLayout.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireContext(), DateSelectionActivity.class);
            intent.setAction(Constants.DateSelectionMode.RANG);
            requireActivity().startActivityForResult(intent, Constants.DATE_SELECTION_REPORT_INJURY);
        });


        farmLayout.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), FarmSelectionActivity.class);
            requireActivity().startActivityForResult(intent, Constants.FARM_SELECTION_REPORT_FACTOR);
        });

        Button submit = view.findViewById(R.id.submit);

        return view;
    }

    public void setDate(DateContainer date) {
        this.date = date;
        dateText.setText(date.toStringSmall(requireContext()));

        if (dateText.getText().toString().isEmpty()) {
            dateLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
        } else {
            dateLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
        }

    }

    public void setFarm(int id) {
        this.farmId = id;
        MyDao dao = DataBase.getInstance(requireContext()).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            Farm farm = dao.getFarm(id);
            if (farm != null)
                requireActivity().runOnUiThread(() -> {
                    farmText.setText(farm.name);
                    if (farmText.getText().toString().isEmpty()) {
                        farmLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
                    } else {
                        farmLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
                    }
                });
        });
    }

}