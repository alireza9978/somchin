package com.damasahhre.hooftrim.activities.tabs;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.utils.AppExecutors;

import mehdi.sakout.fancybuttons.FancyButton;

public class AddLivestockActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_livestock);

        findViewById(R.id.close_image).setOnClickListener((View v) -> finish());
        EditText farmTitle = findViewById(R.id.farm_title_input);
        EditText controlSystem = findViewById(R.id.control_system_input);
        EditText birth = findViewById(R.id.something_count);
        FancyButton submit = findViewById(R.id.submit);

        submit.setOnClickListener((View v) -> {
            String title = farmTitle.getText().toString();
            String system = controlSystem.getText().toString();
            String countString = birth.getText().toString();
            if (title.isEmpty() | system.isEmpty() | countString.isEmpty()) {
                Toast.makeText(this, R.string.check_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            int count = Integer.parseInt(countString);
            Farm farm = new Farm();
            farm.favorite = false;
            farm.controlSystem = system;
            farm.birthCount = count;
            farm.name = title;
            MyDao dao = DataBase.getInstance(this).dao();
            AppExecutors.getInstance().diskIO().execute(() -> {
                dao.insert(farm);
                finish();
            });
        });


    }
}