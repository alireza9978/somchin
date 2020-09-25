package com.damasahhre.hooftrim.activities.tabs.search_activities;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.DateSelectionActivity;
import com.damasahhre.hooftrim.adapters.RecyclerViewAdapterSearchFarm;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.models.DateContainer;

import java.util.ArrayList;

public class SearchFarmFragment extends Fragment {

    private RecyclerView farmsList;
    private TextView notFound;
    private TextView farmsListTitle;
    private TextView dateText;
    private ConstraintLayout dateContainer;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapterSearchFarm mAdapter;

    private DateContainer date = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_livestock, container, false);
        farmsList = view.findViewById(R.id.searched_cows_list);
        notFound = view.findViewById(R.id.not_fount_text);
        farmsListTitle = view.findViewById(R.id.searched_cows_title);
        Button search = view.findViewById(R.id.submit);
        dateText = view.findViewById(R.id.date_text);
        dateContainer = view.findViewById(R.id.date_container);

        farmsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(requireContext());
        farmsList.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapterSearchFarm(new ArrayList<>(), requireContext());
        farmsList.setAdapter(mAdapter);

        search.setOnClickListener((v) -> {
            //todo search in database
            //update adapter
        });

        dateContainer.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireContext(), DateSelectionActivity.class);
            intent.setAction(Constants.DateSelectionMode.RANG);
            requireActivity().startActivityForResult(intent, Constants.DATE_SELECTION_SEARCH_FARM);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        notSearched();
    }

    public void setDate(DateContainer date) {
        this.date = date;
        dateText.setText(date.toStringSmall(requireContext()));

        if (dateText.getText().toString().isEmpty()) {
            dateContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
        } else {
            dateContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
        }

    }


    public void notSearched() {
        farmsList.setVisibility(View.INVISIBLE);
        notFound.setVisibility(View.INVISIBLE);
        farmsListTitle.setVisibility(View.INVISIBLE);
    }

    public void found() {
        farmsList.setVisibility(View.VISIBLE);
        notFound.setVisibility(View.INVISIBLE);
        farmsListTitle.setVisibility(View.VISIBLE);
    }

    public void notFound() {
        farmsList.setVisibility(View.INVISIBLE);
        notFound.setVisibility(View.VISIBLE);
        farmsListTitle.setVisibility(View.VISIBLE);
    }

}