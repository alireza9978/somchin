package com.damasahhre.hooftrim.activities.tabs.report_activites;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.damasahhre.hooftrim.database.models.Report;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.models.MyDate;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

import saman.zamani.persiandate.PersianDate;

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
import static com.damasahhre.hooftrim.R.string.nine;
import static com.damasahhre.hooftrim.R.string.old_next_visit;
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
        button.setOnClickListener(view1 -> showFileChooser());


        return view;
    }

    public void showFileChooser() {
        if (Constants.checkPermissionRead(requireContext())) {
            return;
        }

        Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("application/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, Constants.CHOOSE_FILE_REQUEST_CODE);
    }

    public void importFile(Intent intent) {


        try {
            if (intent == null || intent.getData() == null) {
                return;
            }
            Uri fileUri = intent.getData();
            String fileName = null;
            InputStream stream;
            try {
                if (fileUri == null) {
                    Toast.makeText(requireContext(), "no file selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                stream = requireContext().getContentResolver().openInputStream(fileUri);

                if (Objects.equals(fileUri.getScheme(), "file")) {
                    fileName = fileUri.getLastPathSegment();
                } else {
                    try (Cursor cursor = requireContext().getContentResolver().query(fileUri, new String[]{
                            MediaStore.Images.ImageColumns.DISPLAY_NAME
                    }, null, null, null)) {
                        if (cursor != null && cursor.moveToFirst()) {
                            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                            Log.d("IMPORTS", "name is " + fileName);
                        }
                    }
                }

            } catch (FileNotFoundException e) {
                Toast.makeText(requireContext(), "file not found", Toast.LENGTH_SHORT).show();
                return;
            }

            assert stream != null;
            assert fileName != null;
            Sheet datatypeSheet = null;
            String farmName = "imported";
            if (fileName.endsWith(".xls")) {
                POIFSFileSystem myFileSystem = new POIFSFileSystem(stream);
                HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
                datatypeSheet = myWorkBook.getSheetAt(0);
                farmName = fileName.substring(0, fileName.length() - 4);
            } else if (fileName.endsWith(".xlsx")) {
                OPCPackage opcPackage = OPCPackage.open(stream);
                XSSFWorkbook myWorkBook = new XSSFWorkbook(opcPackage);
                datatypeSheet = myWorkBook.getSheetAt(0);
                farmName = fileName.substring(0, fileName.length() - 5);
            }


            Integer[] headers = {R.string.cow_number, R.string.day, R.string.month, R.string.year,
                    reason_1, reason_2, reason_3,
                    reason_6, reason_7, reason_9, reason_8, reason_4,
                    reason_5, reason_10, zero, one, two, three, four, five, six, seven, eight, nine,
                    ten, eleven, twelve, more_info_reason_1, more_info_reason_2, more_info_reason_7,
                    more_info_reason_5, more_info_reason_6, more_info_reason_4,
                    old_next_visit, more_info, more_info_reason_3};

            //read headers
            int count = 0;
            assert datatypeSheet != null;
            for (Cell cell : datatypeSheet.getRow(0)) {
                if (!cell.getStringCellValue().equals(getString(headers[count]))) {
                    Toast.makeText(requireContext(), "expected : " + getString(headers[count])
                            + " find : " + cell.getStringCellValue(), Toast.LENGTH_LONG).show();
                    return;
                }
                count++;
            }
            MyDao dao = DataBase.getInstance(requireContext()).dao();
            Sheet finalDatatypeSheet = datatypeSheet;
            String finalFarmName = farmName;
            AppExecutors.getInstance().diskIO().execute(() -> {
                Farm farm = new Farm(finalFarmName, 0, "", Boolean.FALSE, Boolean.TRUE);
                farm.setId(dao.insertGetId(farm));

                HashSet<Integer> cowNumbers = new HashSet<>();
                ArrayList<Cow> cows = new ArrayList<>();
                ArrayList<Report> reports = new ArrayList<>();

                Iterator<Row> rows = finalDatatypeSheet.iterator();
                rows.next();
                while (rows.hasNext()) {
                    Row row = rows.next();
                    Report report = new Report();
                    report.cowId = Long.parseLong(row.getCell(0).getStringCellValue());
                    cowNumbers.add(report.cowId.intValue());
                    if (Constants.getDefaultLanguage(requireContext()).equals("fa")) {
                        PersianDate pdate = new PersianDate();
                        int[] dateArray = pdate.toGregorian(Integer.parseInt(row.getCell(3).getStringCellValue()),
                                Integer.parseInt(row.getCell(2).getStringCellValue()),
                                Integer.parseInt(row.getCell(1).getStringCellValue()));
                        report.visit = new MyDate(dateArray[2], dateArray[1], dateArray[0]);
                    } else {
                        report.visit = new MyDate(Integer.parseInt(row.getCell(1).getStringCellValue()),
                                Integer.parseInt(row.getCell(2).getStringCellValue()),
                                Integer.parseInt(row.getCell(3).getStringCellValue()));
                    }
                    for (int i = 4; i < 14; i++) {
                        Cell cell = row.getCell(i);
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            String star = cell.getStringCellValue();
                            if (star != null && !star.isEmpty() && star.equals("*")) {
                                switch (i) {
                                    case 4:
                                        report.referenceCauseHundredDays = true;
                                        break;
                                    case 5:
                                        report.referenceCauseDryness = true;
                                        break;
                                    case 6:
                                        report.referenceCauseLagged = true;
                                        break;
                                    case 7:
                                        report.referenceCauseNewLimp = true;
                                        break;
                                    case 8:
                                        report.referenceCauseLimpVisit = true;
                                        break;
                                    case 9:
                                        report.referenceCauseHighScore = true;
                                        break;
                                    case 10:
                                        report.referenceCauseReferential = true;
                                        break;
                                    case 11:
                                        report.referenceCauseLongHoof = true;
                                        break;
                                    case 12:
                                        report.referenceCauseHeifer = true;
                                        break;
                                    case 13:
                                        report.referenceCauseGroupHoofTrim = true;
                                        break;
                                }
                            } else {
                                switch (i) {
                                    case 4:
                                        report.referenceCauseHundredDays = false;
                                        break;
                                    case 5:
                                        report.referenceCauseDryness = false;
                                        break;
                                    case 6:
                                        report.referenceCauseLagged = false;
                                        break;
                                    case 7:
                                        report.referenceCauseNewLimp = false;
                                        break;
                                    case 8:
                                        report.referenceCauseLimpVisit = false;
                                        break;
                                    case 9:
                                        report.referenceCauseHighScore = false;
                                        break;
                                    case 10:
                                        report.referenceCauseReferential = false;
                                        break;
                                    case 11:
                                        report.referenceCauseLongHoof = false;
                                        break;
                                    case 12:
                                        report.referenceCauseHeifer = false;
                                        break;
                                    case 13:
                                        report.referenceCauseGroupHoofTrim = false;
                                        break;
                                }
                            }
                        } else {
                            switch (i) {
                                case 4:
                                    report.referenceCauseHundredDays = false;
                                    break;
                                case 5:
                                    report.referenceCauseDryness = false;
                                    break;
                                case 6:
                                    report.referenceCauseLagged = false;
                                    break;
                                case 7:
                                    report.referenceCauseNewLimp = false;
                                    break;
                                case 8:
                                    report.referenceCauseLimpVisit = false;
                                    break;
                                case 9:
                                    report.referenceCauseHighScore = false;
                                    break;
                                case 10:
                                    report.referenceCauseReferential = false;
                                    break;
                                case 11:
                                    report.referenceCauseLongHoof = false;
                                    break;
                                case 12:
                                    report.referenceCauseHeifer = false;
                                    break;
                                case 13:
                                    report.referenceCauseGroupHoofTrim = false;
                                    break;
                            }
                        }
                    }
                    for (int i = 14; i < 27; i++) {
                        Cell cell = row.getCell(i);
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            String value = cell.getStringCellValue();
                            if (value != null && !value.isEmpty()) {
                                report.fingerNumber = Integer.parseInt(value);
                                report.legAreaNumber = i - 14;
                                report.rightSide = report.fingerNumber % 2 == 0;
                                break;
                            }
                        }
                    }
                    for (int i = 27; i < 33; i++) {
                        Cell cell = row.getCell(i);
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            String star = cell.getStringCellValue();
                            if (star != null && !star.isEmpty() && star.equals("*")) {
                                switch (i) {
                                    case 27:
                                        report.otherInfoWound = true;
                                        break;
                                    case 28:
                                        report.otherInfoEcchymosis = true;
                                        break;
                                    case 29:
                                        report.otherInfoHoofTrim = true;
                                        break;
                                    case 30:
                                        report.otherInfoGel = true;
                                        break;
                                    case 31:
                                        report.otherInfoBoarding = true;
                                        break;
                                    case 32:
                                        report.otherInfoNoInjury = true;
                                        break;
                                }
                            } else {
                                switch (i) {
                                    case 27:
                                        report.otherInfoWound = false;
                                        break;
                                    case 28:
                                        report.otherInfoEcchymosis = false;
                                        break;
                                    case 29:
                                        report.otherInfoHoofTrim = false;
                                        break;
                                    case 30:
                                        report.otherInfoGel = false;
                                        break;
                                    case 31:
                                        report.otherInfoBoarding = false;
                                        break;
                                    case 32:
                                        report.otherInfoNoInjury = false;
                                        break;
                                }
                            }
                        } else {
                            switch (i) {
                                case 27:
                                    report.otherInfoWound = false;
                                    break;
                                case 28:
                                    report.otherInfoEcchymosis = false;
                                    break;
                                case 29:
                                    report.otherInfoHoofTrim = false;
                                    break;
                                case 30:
                                    report.otherInfoGel = false;
                                    break;
                                case 31:
                                    report.otherInfoBoarding = false;
                                    break;
                                case 32:
                                    report.otherInfoNoInjury = false;
                                    break;
                            }
                        }
                    }
                    Cell nextVisitCell = row.getCell(33);
                    if (nextVisitCell.getCellType() == Cell.CELL_TYPE_STRING) {
                        if (!nextVisitCell.getStringCellValue().isEmpty()) {
                            String[] date = nextVisitCell.getStringCellValue().split("/");
                            if (Constants.getDefaultLanguage(requireContext()).equals("fa")) {
                                PersianDate pdate = new PersianDate();
                                int[] dateArray = pdate.toGregorian(Integer.parseInt(date[0]),
                                        Integer.parseInt(date[1]),
                                        Integer.parseInt(date[2]));
                                report.nextVisit = new MyDate(dateArray[2], dateArray[1], dateArray[0]);
                            } else {
                                report.nextVisit = new MyDate(Integer.parseInt(date[2]),
                                        Integer.parseInt(date[1]),
                                        Integer.parseInt(date[0]));
                            }
                        }
                    }
                    Cell moreInfo = row.getCell(34);
                    if (nextVisitCell.getCellType() == Cell.CELL_TYPE_STRING) {
                        report.description = moreInfo.getStringCellValue();
                    }
                    Cell cell = row.getCell(35);
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        String star = cell.getStringCellValue();
                        report.otherInfoRecovered = star != null && !star.isEmpty() && star.equals("*");
                    }
                    reports.add(report);
                }
                for (Integer cowNumber : cowNumbers) {
                    cows.add(new Cow(cowNumber, false, farm.getId(), true));
                }
                for (Cow cow : cows) {
                    cow.setId(dao.insertGetId(cow));
                }
                main:
                for (Report report : reports) {
                    for (Cow cow : cows) {
                        if (report.cowId.equals((long) cow.getNumber())) {
                            report.cowId = cow.getId();
                            dao.insert(report);
                            continue main;
                        }
                    }
                }

                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "imported", Toast.LENGTH_LONG).show());
            });

        } catch (Exception e) {
            Toast.makeText(requireContext(), "reading error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }
}