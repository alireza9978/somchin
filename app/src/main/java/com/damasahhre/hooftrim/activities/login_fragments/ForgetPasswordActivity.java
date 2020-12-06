package com.damasahhre.hooftrim.activities.login_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.server.Requests;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.io.IOException;

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ImageView logo = findViewById(R.id.login_logo);
        ImageView close = findViewById(R.id.close_image);
        close.setOnClickListener(view -> finish());
        Constants.setImageBack(this, close);

        EditText email = findViewById(R.id.email_input);

        findViewById(R.id.submit).setOnClickListener(v -> {
            String emailText = email.getText().toString();
            if (emailText.isEmpty()) {
                Toast.makeText(this, getString(R.string.check_fields), Toast.LENGTH_SHORT).show();
            }
            Requests.forgetPassword(emailText, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                    Log.i("HTTP_LOGIN", "onFailure: " + request.toString());
                }

                @Override
                public void onResponse(Response response) {
                    //todo check response and finish
                    Log.i("HTTP_LOGIN", "onResponse: " + response.toString());
                }
            });
        });


        KeyboardVisibilityEvent.setEventListener(
                this,
                isOpen -> {
                    if (isOpen) {
                        logo.setVisibility(View.GONE);
                        close.setVisibility(View.GONE);
                    } else {
                        logo.setVisibility(View.VISIBLE);
                        close.setVisibility(View.VISIBLE);
                    }
                });

    }
}