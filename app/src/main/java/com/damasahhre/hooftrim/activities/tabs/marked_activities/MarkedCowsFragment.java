package com.damasahhre.hooftrim.activities.tabs.marked_activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.adapters.RecyclerViewAdapterSearchCow;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.Cow;
import com.damasahhre.hooftrim.database.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class MarkedCowsFragment extends Fragment {

    private RecyclerViewAdapterSearchCow mAdapter;
    private TextView notFound;
    private RecyclerView markedRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marked_cows, container, false);
        notFound = view.findViewById(R.id.no_marked_cow_text);
        markedRecyclerView = view.findViewById(R.id.marked_cow_list);

        markedRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        markedRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapterSearchCow(new ArrayList<>(), requireContext());
        markedRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MyDao dao = DataBase.getInstance(requireContext()).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Cow> cows = dao.getMarkedCows();
            requireActivity().runOnUiThread(() -> {
                if (cows.isEmpty()) {
                    notFound.setVisibility(View.VISIBLE);
                    markedRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    notFound.setVisibility(View.INVISIBLE);
                    markedRecyclerView.setVisibility(View.VISIBLE);
                    mAdapter.setCows(cows);
                    mAdapter.notifyDataSetChanged();
                }

            });
        });
    }
}