package com.damasahhre.hooftrim.activities.tabs.search_activities;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.DateSelectionActivity;
import com.damasahhre.hooftrim.activities.LiveStrockSelectionActivity;
import com.damasahhre.hooftrim.adapters.RecyclerViewAdapterSearchCow;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.models.DateContainer;

import java.util.ArrayList;

public class SearchCowFragment extends Fragment {

    private RecyclerView cowsList;
    private TextView notFound;
    private TextView farmName;
    private TextView dateText;
    private EditText cowNumber;
    private ConstraintLayout cowNumberContainer;
    private ConstraintLayout farmContainer;
    private ConstraintLayout dateContainer;
    private Button search;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapterSearchCow mAdapter;

    private int farmId = -1;
    private DateContainer date = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_cow, container, false);

        cowsList = view.findViewById(R.id.searched_cows_list);
        search = view.findViewById(R.id.submit);
        notFound = view.findViewById(R.id.not_fount_text);
        cowNumber = view.findViewById(R.id.cow_name_text);
        cowNumberContainer = view.findViewById(R.id.cow_number_container);
        farmContainer = view.findViewById(R.id.livestock_container);
        dateContainer = view.findViewById(R.id.date_container);
        farmName = view.findViewById(R.id.livestock_name_text);
        dateText = view.findViewById(R.id.date_text);

        cowsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(requireContext());
        cowsList.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapterSearchCow(new ArrayList<>(), requireContext());
        cowsList.setAdapter(mAdapter);

        search.setOnClickListener((v) -> {
            //todo search in database
            //update adapter
        });

        dateContainer.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireContext(), DateSelectionActivity.class);
            intent.setAction(Constants.DateSelectionMode.RANG);
            requireActivity().startActivityForResult(intent, Constants.DATE_SELECTION_SEARCH_COW);
        });

        farmContainer.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), LiveStrockSelectionActivity.class);
            requireActivity().startActivityForResult(intent, Constants.FARM_SELECTION_SEARCH_COW);
        });

        cowNumber.setOnFocusChangeListener((view13, b) -> {
            if (b) {
                cowNumberContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
            } else {
                if (cowNumber.getText().toString().isEmpty()) {
                    cowNumberContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
                } else {
                    cowNumberContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
                }
            }
        });

        return view;
    }

    public void setDate(DateContainer date) {
        this.date = date;
        dateText.setText(date.toString(requireContext()));
    }

    public void setFarm(int id) {
        this.farmId = id;
        MyDao dao = DataBase.getInstance(requireContext()).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            Farm farm = dao.getFarm(id);
            if (farm != null)
                requireActivity().runOnUiThread(() -> {
                    farmName.setText(farm.name);
                });
        });
    }

}