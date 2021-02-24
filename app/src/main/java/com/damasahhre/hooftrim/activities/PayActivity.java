package com.damasahhre.hooftrim.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.damasahhre.hooftrim.R;

public class PayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        TextView header = findViewById(R.id.date_title);
        TextView textView = findViewById(R.id.pay_state);
        ImageView back = findViewById(R.id.back_image);

        back.setOnClickListener(view -> finish());

        Intent in = getIntent();
        Uri data = in.getData();
        if (data != null) {
            String rdata = data.toString().replace("varchar://", "");
            Log.i("PayActivity", "onCreate: " + data);
            if (rdata.equals("122")) {
                header.setText(R.string.payment_title);
                textView.setText(R.string.fail);
                textView.setTextColor(getResources().getColor(R.color.red));
            }
            if (rdata.equals("123")) {
                header.setText(R.string.payment_title);
                textView.setText(R.string.success);
                textView.setTextColor(getResources().getColor(R.color.persian_green));
            }
            if (rdata.equals("124")) {
                header.setText(R.string.email_title);
                textView.setText(R.string.fail_mail);
                textView.setTextColor(getResources().getColor(R.color.persian_green));
            }
            if (rdata.equals("125")) {
                header.setText(R.string.email_title);
                textView.setText(R.string.success_mail);
                textView.setTextColor(getResources().getColor(R.color.persian_green));
            }
        }

    }
}