package com.damasahhre.hooftrim.activities.tabs.report_activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class FactorFragment extends Fragment {

    private ConstraintLayout farmLayout;
    private TextView farmText;

    private ConstraintLayout dateLayout;
    private TextView dateText;

    private ConstraintLayout cowLayout;
    private EditText cowText;
    private ConstraintLayout priceSomLayout;
    private EditText priceSomText;
    private ConstraintLayout priceCureLayout;
    private EditText priceCureText;
    private ConstraintLayout priceBedLayout;
    private EditText priceBedText;

    private int farmId = -1;
    private DateContainer date = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_factor, container, false);

        farmLayout = view.findViewById(R.id.livestock_container);
        dateLayout = view.findViewById(R.id.date_container);
        cowLayout = view.findViewById(R.id.cow_number_container);
        farmText = view.findViewById(R.id.livestock_name_text);
        dateText = view.findViewById(R.id.date_text);
        cowText = view.findViewById(R.id.cow_name_text);
        priceSomLayout = view.findViewById(R.id.som_cut_price_container);
        priceCureLayout = view.findViewById(R.id.cure_price_container);
        priceBedLayout = view.findViewById(R.id.som_bed_price_container);
        priceSomText = view.findViewById(R.id.som_cut_price_text);
        priceCureText = view.findViewById(R.id.cure_price_text);
        priceBedText = view.findViewById(R.id.som_bed_price_text);


        dateLayout.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireContext(), DateSelectionActivity.class);
            intent.setAction(Constants.DateSelectionMode.RANG);
            requireActivity().startActivityForResult(intent, Constants.DATE_SELECTION_REPORT_FACTOR);
        });


        farmLayout.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), FarmSelectionActivity.class);
            requireActivity().startActivityForResult(intent, Constants.FARM_SELECTION_REPORT_FACTOR);
        });

        Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(view1 -> {
            if (Constants.checkPermission(requireContext())) {
                return;
            }
            MyDao dao = DataBase.getInstance(requireContext()).dao();
            AppExecutors.getInstance().diskIO().execute(() -> {

//                for (int i = 0; i < yourArraylist.size(); i++) {
//
//                    Row row = sheet.createRow(i);
//                    row.createCell(CELL_INDEX_0).setCellValue(VALUE_YOU_WANT_TO_KEEP_ON_1ST_COLUMN);
//                    row.createCell(CELL_INDEX_1).setCellValue(VALUE_YOU_WANT_TO_KEEP_ON_2ND_COLUMN);
//                }
            });


        });
        setBackgrounds();

        return view;
    }

    private void setBackgrounds() {
        setBackgroundChanger(cowText, cowLayout);
        setBackgroundChanger(priceBedText, priceBedLayout);
        setBackgroundChanger(priceCureText, priceCureLayout);
        setBackgroundChanger(priceSomText, priceSomLayout);
    }

    private void setBackgroundChanger(EditText editText, ConstraintLayout layout) {
        editText.setOnFocusChangeListener((view13, b) -> {
            if (b) {
                layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
            } else {
                if (editText.getText().toString().isEmpty()) {
                    layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
                } else {
                    layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
                }
            }
        });
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