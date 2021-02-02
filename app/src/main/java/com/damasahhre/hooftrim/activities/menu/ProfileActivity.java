package com.damasahhre.hooftrim.activities.menu;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import mehdi.sakout.fancybuttons.FancyButton;

public class ProfileActivity extends AppCompatActivity {

    private Button convert;
    private ProfileActivity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ImageView back = findViewById(R.id.back_image);
        Constants.setImageBack(this, back);
        back.setOnClickListener(view -> finish());

        convert = findViewById(R.id.premium);
        TextView email = findViewById(R.id.email_input);
        EditText oldPass = findViewById(R.id.old_password_input);
        EditText newPass = findViewById(R.id.new_password_input);
        EditText confirmPass = findViewById(R.id.new_password_input_confirm);
        if (Constants.isNetworkAvailable(this)) {
            email.setText(Constants.getEmail(this));
        } else {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_LONG).show();
            finish();
        }

        convert.setOnClickListener(view -> {
            if (!newPass.getText().toString().isEmpty() &&
                    newPass.getText().toString().equals(confirmPass.getText().toString())) {
                Requests.editPassword(Constants.getToken(this), oldPass.getText().toString(), newPass.getText().toString(), new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        runOnUiThread(() -> Toast.makeText(activity, R.string.request_error, Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onResponse(Response response) {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> Toast.makeText(activity, R.string.password_changed, Toast.LENGTH_LONG).show());
                            finish();
                        } else {
                            Requests.toastMessage(response, activity);
                        }
                    }
                });
            }

        });

        FancyButton submit = findViewById(R.id.submit);
        submit.setOnClickListener(view -> {
            if (!newPass.getText().toString().isEmpty() &&
                    newPass.getText().toString().equals(confirmPass.getText().toString())) {
                Requests.editPassword(Constants.getToken(this), oldPass.getText().toString(), newPass.getText().toString(), new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        runOnUiThread(() -> Toast.makeText(activity, R.string.request_error, Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onResponse(Response response) {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> Toast.makeText(activity, R.string.password_changed, Toast.LENGTH_LONG).show());
                            finish();
                        } else {
                            Requests.toastMessage(response, activity);
                        }
                    }
                });
            } else {
                Toast.makeText(activity, R.string.check_fields, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Requests.isPaid(Constants.getToken(this), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(() -> Toast.makeText(activity, R.string.request_error, Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        boolean isPremium = (boolean) jsonObject.get("is_premium");
                        if (isPremium) {
                            runOnUiThread(() -> {
                                convert.setText(R.string.premium_user);
                                convert.setClickable(false);
                            });
                        } else {
                            runOnUiThread(() -> {
                                convert.setText(R.string.convert);
                                convert.setOnClickListener(view -> Requests.pay(Constants.getEmail(activity), activity));
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
    }
}