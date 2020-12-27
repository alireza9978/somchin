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
import com.damasahhre.hooftrim.activities.LoginActivity;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.server.Requests;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_login, container, false);

        EditText password = view.findViewById(R.id.password_input);
        EditText username = view.findViewById(R.id.user_name_input);

        view.findViewById(R.id.submit).setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.check_fields), Toast.LENGTH_SHORT).show();
                return;
            }
            Requests.login(user, pass, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                    Log.i("HTTP_LOGIN", "onFailure: " + request.toString());
                }

                @Override
                public void onResponse(Response response) {
                    LoginActivity activity = (LoginActivity) requireActivity();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            Constants.setEmail(requireActivity(), user);
                            Constants.setToken(requireActivity(), (String) jsonObject.get("token"));
                            activity.syncData();
                            activity.runOnUiThread(activity::goApp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Requests.toastMessage(response, activity);
                    }
                }
            });
        });
        view.findViewById(R.id.forgot_password).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ForgetPasswordActivity.class);
            startActivity(intent);
        });

        return view;
    }

}
