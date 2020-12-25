package com.damasahhre.hooftrim.activities.menu;

import android.os.Bundle;
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

import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ImageView back = findViewById(R.id.back_image);
        Constants.setImageBack(this, back);
        back.setOnClickListener(view -> finish());

        TextView email = findViewById(R.id.email_input);
        EditText oldPass = findViewById(R.id.old_password_input);
        EditText newPass = findViewById(R.id.new_password_input);
        EditText confirmPass = findViewById(R.id.new_password_input_confirm);
        ProfileActivity activity = this;
        if (Constants.isNetworkAvailable(this)) {
            //todo get user and set email
//            email.setText();
        } else {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_LONG).show();
            finish();
        }


        FancyButton submit = findViewById(R.id.submit);
        submit.setOnClickListener(view -> {
            if (!newPass.getText().toString().isEmpty() &&
                    newPass.getText().toString().equals(confirmPass.getText().toString())) {
                Requests.editPassword(email.getText().toString(), oldPass.getText().toString(), newPass.getText().toString(), new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(activity, R.string.request_error, Toast.LENGTH_LONG).show();
                        });
                    }

                    @Override
                    public void onResponse(Response response) {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> {
                                Toast.makeText(activity, R.string.password_changed, Toast.LENGTH_LONG).show();
                            });
                            finish();
                        } else {
                            Requests.toastMessage(response, activity);
                        }
                    }
                });
            }

        });

    }
}