package com.damasahhre.hooftrim.activities.tabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.MainActivity;

public class MarkedActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_marked, container, false);
        view.findViewById(R.id.menu_button).setOnClickListener(v -> {
            ((MainActivity) requireActivity()).openMenu();
        });
        return view;
    }

}