package com.damasahhre.hooftrim.activities.menu;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;

/**
 * صفحه درباره ما
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ImageView back = findViewById(R.id.back_image);
        Constants.setImageBack(this, back);
        back.setOnClickListener(view -> finish());
    }
}