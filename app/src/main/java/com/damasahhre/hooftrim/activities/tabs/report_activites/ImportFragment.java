package com.damasahhre.hooftrim.activities.tabs.report_activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.Cow;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import static com.damasahhre.hooftrim.R.string.eight;
import static com.damasahhre.hooftrim.R.string.eleven;
import static com.damasahhre.hooftrim.R.string.five;
import static com.damasahhre.hooftrim.R.string.four;
import static com.damasahhre.hooftrim.R.string.more_info;
import static com.damasahhre.hooftrim.R.string.more_info_reason_1;
import static com.damasahhre.hooftrim.R.string.more_info_reason_2;
import static com.damasahhre.hooftrim.R.string.more_info_reason_3;
import static com.damasahhre.hooftrim.R.string.more_info_reason_4;
import static com.damasahhre.hooftrim.R.string.more_info_reason_5;
import static com.damasahhre.hooftrim.R.string.more_info_reason_6;
import static com.damasahhre.hooftrim.R.string.more_info_reason_7;
import static com.damasahhre.hooftrim.R.string.next_visit;
import static com.damasahhre.hooftrim.R.string.nine;
import static com.damasahhre.hooftrim.R.string.one;
import static com.damasahhre.hooftrim.R.string.reason_1;
import static com.damasahhre.hooftrim.R.string.reason_10;
import static com.damasahhre.hooftrim.R.string.reason_2;
import static com.damasahhre.hooftrim.R.string.reason_3;
import static com.damasahhre.hooftrim.R.string.reason_4;
import static com.damasahhre.hooftrim.R.string.reason_5;
import static com.damasahhre.hooftrim.R.string.reason_6;
import static com.damasahhre.hooftrim.R.string.reason_7;
import static com.damasahhre.hooftrim.R.string.reason_8;
import static com.damasahhre.hooftrim.R.string.reason_9;
import static com.damasahhre.hooftrim.R.string.seven;
import static com.damasahhre.hooftrim.R.string.six;
import static com.damasahhre.hooftrim.R.string.ten;
import static com.damasahhre.hooftrim.R.string.three;
import static com.damasahhre.hooftrim.R.string.twelve;
import static com.damasahhre.hooftrim.R.string.two;
import static com.damasahhre.hooftrim.R.string.zero;

public class ImportFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_import, container, false);

        ConstraintLayout button = view.findViewById(R.id.import_button);
        button.setOnClickListener(view1 -> {
            showFileChooser();
        });


        return view;
    }

    public void showFileChooser() {
        Intent intent = new Intent(requireContext(), FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowFiles(true)
                .setShowImages(false)
                .enableImageCapture(false)
                .enableVideoCapture(false)
                .setShowVideos(false)
                .enableImageCapture(true)
                .setSingleClickSelection(true)
                .setSingleChoiceMode(true)
                .setSkipZeroSizeFiles(true)
                .setSuffixes("xls", "xlsx")
                .build());

        startActivityForResult(intent, Constants.CHOOSE_FILE_REQUEST_CODE);
    }

    public void importFile(Intent intent) {
        ArrayList<MediaFile> files = intent.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
        assert files != null;
        if (files.size() == 1) {
            MediaFile file = files.get(0);
            try {

                FileInputStream excelFile = new FileInputStream(new File(Environment.getExternalStorageDirectory().toString() + "/" + file.getBucketName(), file.getName()));
                Workbook workbook = new XSSFWorkbook(excelFile);
                Sheet datatypeSheet = workbook.getSheetAt(0);

                Integer[] headers = {R.string.cow_number, R.string.day, R.string.month, R.string.year,
                        reason_1, reason_2, reason_3,
                        reason_6, reason_7, reason_9, reason_8, reason_4,
                        reason_5, reason_10, zero, one, two, three, four, five, six, seven, eight, nine,
                        ten, eleven, twelve, more_info_reason_1, more_info_reason_2, more_info_reason_7,
                        more_info_reason_5, more_info_reason_6, more_info_reason_4, more_info_reason_3,
                        next_visit, more_info};

                //read headers
                int count = 0;
                for (Cell cell : datatypeSheet.getRow(0)) {
                    if (!cell.getStringCellValue().equals(getString(headers[count]))) {
                        Toast.makeText(requireContext(), "expected : " + getString(headers[count])
                                + " find : " + cell.getStringCellValue(), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                MyDao dao = DataBase.getInstance(requireContext()).dao();
                AppExecutors.getInstance().diskIO().execute(() -> {
                    Farm farm = new Farm();
                    farm.name = file.getName().substring(0, file.getName().indexOf("."));
                    farm.id = (int) dao.insertGetId(farm);

                    ArrayList<Cow> cows = new ArrayList<Cow>();
                    HashSet<Integer> cowNumbers = new HashSet<>();
                    for (Row currentRow : datatypeSheet) {
                        cowNumbers.add((int) currentRow.getCell(0).getNumericCellValue());
                    }
                    for (Integer cowNumber : cowNumbers) {
                        cows.add(new Cow(cowNumber, false, farm.id));
                    }
                    for (Cow cow : cows) {
                        cow.setId((int) dao.insertGetId(cow));
                    }
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "imported", Toast.LENGTH_LONG).show();
                    });
                });


            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(requireContext(), "no file", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}