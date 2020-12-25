package com.damasahhre.hooftrim.activities.login_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.ValidateActivity;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.server.Requests;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class SignUpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_sign_up, container, false);

        EditText password = view.findViewById(R.id.password_input);
        EditText email = view.findViewById(R.id.email_input);

        view.findViewById(R.id.submit).setOnClickListener(v -> {
            String user = email.getText().toString();
            String pass = password.getText().toString();
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.check_fields), Toast.LENGTH_SHORT).show();
            }
            Requests.signUp(user, pass, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                    Log.i("HTTP_LOGIN", "onFailure: " + request.toString());
                }

                @Override
                public void onResponse(Response response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(requireContext(), ValidateActivity.class);
                        intent.putExtra(Constants.EMAIL, user);
                        requireActivity().startActivity(intent);
                    } else {
                        Requests.toastMessage(response, requireActivity());
                    }
                }
            });
        });

        return view;
    }
}