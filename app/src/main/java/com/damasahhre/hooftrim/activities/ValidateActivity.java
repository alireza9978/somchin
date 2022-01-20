package com.damasahhre.hooftrim.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.server.Requests;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

/**
 * صفحه تایید ایمیل در ابتدای برنامه
 */
public class ValidateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);
        String email = Objects.requireNonNull(getIntent().getExtras()).getString(Constants.EMAIL);
        ValidateActivity activity = this;
        findViewById(R.id.resend).setOnClickListener(view -> {
            Toast.makeText(this, R.string.wait, Toast.LENGTH_SHORT).show();

            Requests.resend(email, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            Toast.makeText(activity, R.string.resended, Toast.LENGTH_LONG).show();
                        });
                    } else {
                        Requests.toastMessage(response, activity);
                    }
                }
            });
        });

        findViewById(R.id.submit).setOnClickListener(view -> {
            Requests.isValidated(email, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            boolean confirmed = (boolean) jsonObject.get("is_confirmed");
                            if (confirmed) {
                                runOnUiThread(() -> {
                                    Toast.makeText(activity, R.string.login_pls, Toast.LENGTH_LONG).show();
                                    finish();
                                });
                            } else {
                                runOnUiThread(() -> {
                                    Toast.makeText(activity, R.string.not_confirmed, Toast.LENGTH_LONG).show();
                                });
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Requests.toastMessage(response, activity);
                    }
                }
            });
        });

    }


}