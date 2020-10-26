package com.damasahhre.hooftrim.activities.login_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.LoginActivity;
import com.damasahhre.hooftrim.constants.Constants;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_login, container, false);

        EditText password = view.findViewById(R.id.password_input);

        view.findViewById(R.id.submit).setOnClickListener(v -> {
            ((LoginActivity) requireActivity()).goApp();

        });
        view.findViewById(R.id.forgot_password).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ForgetPasswordActivity.class);
            startActivity(intent);
        });

        return view;
    }

}
