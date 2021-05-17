package com.damasahhre.hooftrim.activities.tabs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.utils.AppExecutors;

import java.util.Objects;

/**
 * صفحه اصلی برای افزودن یک گاوداری جدید
 */
public class AddFarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_livestock);

        String mode = (String) Objects.requireNonNull(getIntent().getExtras()).get(Constants.ADD_FARM_MODE);
        if (mode == null) {
            finish();
        } else {
            findViewById(R.id.close_image).setOnClickListener((View v) -> finish());
            EditText farmTitle = findViewById(R.id.farm_title_input);
            EditText controlSystem = findViewById(R.id.control_system_input);
            EditText birth = findViewById(R.id.something_count);
            Button submit = findViewById(R.id.submit);
            if (mode.equals(Constants.FARM_CREATE)) {
                submit.setOnClickListener((View v) -> {
                    String title = farmTitle.getText().toString();
                    String system = controlSystem.getText().toString();
                    String countString = birth.getText().toString();
                    if (title.isEmpty() | system.isEmpty() | countString.isEmpty()) {
                        Toast.makeText(this, R.string.check_fields, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int count = Integer.parseInt(countString);
                    Farm farm = new Farm(title, count, system, false, true, true);
                    MyDao dao = DataBase.getInstance(this).dao();
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        dao.insert(farm);
                        finish();
                    });
                });
            }
            if (mode.equals(Constants.EDIT_FARM)) {
                long id = getIntent().getExtras().getLong(Constants.FARM_ID);
                submit.setText(getString(R.string.edit));
                MyDao dao = DataBase.getInstance(this).dao();
                AppExecutors.getInstance().diskIO().execute(() -> {
                    Farm farm = dao.getFarm(id);
                    runOnUiThread(() -> {
                        farmTitle.setText(farm.getName());
                        farmTitle.setEnabled(false);
                        controlSystem.setText(farm.getControlSystem());
                        birth.setText("" + farm.getBirthCount());
                    });
                });

                submit.setOnClickListener((View v) -> {
                    String title = farmTitle.getText().toString();
                    String system = controlSystem.getText().toString();
                    String countString = birth.getText().toString();
                    if (title.isEmpty() | system.isEmpty() | countString.isEmpty()) {
                        Toast.makeText(this, R.string.check_fields, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int count = Integer.parseInt(countString);
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        Farm farm = dao.getFarm(id);
                        farm.setControlSystem(system);
                        farm.setBirthCount(count);
                        farm.setSync(true);
                        dao.update(farm);
                        runOnUiThread(this::finish);
                    });
                });
                controlSystem.requestFocus();
            }

        }


    }
}