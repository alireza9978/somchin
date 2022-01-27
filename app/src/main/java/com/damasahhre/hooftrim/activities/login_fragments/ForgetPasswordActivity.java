package com.damasahhre.hooftrim.activities.login_fragments;

import android.os.Bundle;
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

/**
 * صفحه فراموشی پسورد
 */
public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ImageView logo = findViewById(R.id.login_logo);
        ImageView close = findViewById(R.id.close_image);
        close.setOnClickListener(view -> finish());
        Constants.setImageBack(this, close);
        ForgetPasswordActivity activity = this;
        EditText email = findViewById(R.id.email_input);

        findViewById(R.id.submit).setOnClickListener(v -> {
            String emailText = email.getText().toString();
            if (emailText.isEmpty()) {
                Toast.makeText(this, getString(R.string.check_fields), Toast.LENGTH_SHORT).show();
                return;
            }
            hideKeyboard();
            Requests.forgetPassword(emailText, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(() -> Toast.makeText(activity, R.string.request_error, Toast.LENGTH_LONG).show());
                }

                @Override
                public void onResponse(Response response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), R.string.forget_sended, Toast.LENGTH_LONG).show();
                        });
                    } else {
                        Requests.toastMessage(response, activity);
                    }
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

    public void hideKeyboard() {
        Constants.hideKeyboard(this, findViewById(R.id.root).getWindowToken());
    }


}