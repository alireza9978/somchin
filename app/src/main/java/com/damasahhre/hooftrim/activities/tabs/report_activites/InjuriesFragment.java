package com.damasahhre.hooftrim.activities.tabs.report_activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.DateSelectionActivity;
import com.damasahhre.hooftrim.activities.FarmSelectionActivity;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.DataBase;
import com.damasahhre.hooftrim.database.dao.InjuryDao;
import com.damasahhre.hooftrim.database.dao.MyDao;
import com.damasahhre.hooftrim.database.models.Farm;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.models.DateContainer;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class InjuriesFragment extends Fragment {

    private ConstraintLayout farmLayout;
    private TextView farmText;

    private ConstraintLayout dateLayout;
    private TextView dateText;

    private int farmId = -1;
    private DateContainer date = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_injuries, container, false);
        farmLayout = view.findViewById(R.id.livestock_container);
        dateLayout = view.findViewById(R.id.date_container);
        farmText = view.findViewById(R.id.livestock_name_text);
        dateText = view.findViewById(R.id.date_text);

        dateLayout.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireContext(), DateSelectionActivity.class);
            intent.setAction(Constants.DateSelectionMode.RANG);
            requireActivity().startActivityForResult(intent, Constants.DATE_SELECTION_REPORT_INJURY);
        });


        farmLayout.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), FarmSelectionActivity.class);
            requireActivity().startActivityForResult(intent, Constants.FARM_SELECTION_REPORT_INJURY);
        });

        Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(view13 -> {
            if (Constants.checkPermission(requireContext()))
                return;
            if (date == null) {
                return;
            }
            if (farmId == -1) {
                return;
            }
            export();
        });

        return view;
    }

    public void setDate(DateContainer date) {
        this.date = date;
        dateText.setText(date.toStringSmall(requireContext()));

        if (dateText.getText().toString().isEmpty()) {
            dateLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
        } else {
            dateLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
        }

    }

    public void setFarm(int id) {
        this.farmId = id;
        MyDao dao = DataBase.getInstance(requireContext()).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            Farm farm = dao.getFarm(id);
            if (farm != null)
                requireActivity().runOnUiThread(() -> {
                    farmText.setText(farm.name);
                    if (farmText.getText().toString().isEmpty()) {
                        farmLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
                    } else {
                        farmLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
                    }
                });
        });
    }


    public void export() {

        InjuryDao dao = DataBase.getInstance(requireContext()).injuryDao();
        AppExecutors.getInstance().diskIO().execute(() -> {

        });

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("one");

        String[] headers = {"name", "count"};
        Integer[] rowsName = {
                R.string.injury_row_0,
                R.string.injury_row_1,
                R.string.injury_row_2,
                R.string.injury_row_3,
                R.string.injury_row_4,
                R.string.injury_row_5,
                R.string.injury_row_6,
                R.string.injury_row_7
        };

        AppExecutors.getInstance().diskIO().execute(() -> {
//            ArrayList<Integer> arrayList = getInjuries(dao);
            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(dao.felemons(farmId, date.exportStart(), date.exportEnd()).count);
            arrayList.add(dao.deramatit(farmId, date.exportStart(), date.exportEnd()));
            arrayList.add(dao.woundHoofBottom(farmId, date.exportStart(), date.exportEnd()));
            arrayList.add(dao.whiteLineWound(farmId, date.exportStart(), date.exportEnd()));
            arrayList.add(dao.pangeWound(farmId, date.exportStart(), date.exportEnd()));
            arrayList.add(dao.pashneWound(farmId, date.exportStart(), date.exportEnd()));
            arrayList.add(dao.wallWound(farmId, date.exportStart(), date.exportEnd()));
            arrayList.add(dao.reigenNine(farmId, date.exportStart(), date.exportEnd()));

            requireActivity().runOnUiThread(() -> {
                //add headers
                Row row = sheet.createRow(0);
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(headers[i]);
                }
                //add reports
                for (int i = 0; i < 8; i++) {
                    row = sheet.createRow(i + 1);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(getString(rowsName[i]));
                    cell = row.createCell(1);
                    if (arrayList.get(i) == null) {
                        Log.i("report", "export: " + i);
                        cell.setCellValue(0);
                    } else {
                        cell.setCellValue(arrayList.get(i));

                    }
                }

                try {

                    String storage = Environment.getExternalStorageDirectory().toString() + String.format("/%s.xls", "report");
                    File file = new File(storage);
                    if (file.exists()) {
                        if (file.delete()) {
                            Log.i("TAG", "export: deleted ok");
                        } else {
                            Log.i("TAG", "export: deleted fuck");
                        }
                    }
                    if (file.createNewFile()) {
                        FileOutputStream out = new FileOutputStream(file);
                        workbook.write(out);
                        out.close();
                        Log.i("TAG", "export: Excel written successfully..");
                    } else {
                        Log.i("TAG", "export: Excel written fuck..");
                    }

                    Uri uri;
                    if (Build.VERSION.SDK_INT < 24) {
                        uri = Uri.fromFile(file);
                    } else {
                        uri = FileProvider.getUriForFile(requireContext(),
                                requireContext().getApplicationContext().getPackageName() + ".provider", file);
                    }

                    Intent intent = ShareCompat.IntentBuilder.from(requireActivity())
                            .setType("*/*")
                            .setStream(uri)
                            .setChooserTitle("Choose bar")
                            .createChooserIntent()
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(intent);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });


    }

    private ArrayList<Integer> getInjuries(InjuryDao dao) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(dao.felemons(farmId, date.exportStart(), date.exportEnd()).count);
        arrayList.add(dao.deramatit(farmId, date.exportStart(), date.exportEnd()));
        arrayList.add(dao.woundHoofBottom(farmId, date.exportStart(), date.exportEnd()));
        arrayList.add(dao.whiteLineWound(farmId, date.exportStart(), date.exportEnd()));
        arrayList.add(dao.pangeWound(farmId, date.exportStart(), date.exportEnd()));
        arrayList.add(dao.pashneWound(farmId, date.exportStart(), date.exportEnd()));
        arrayList.add(dao.wallWound(farmId, date.exportStart(), date.exportEnd()));
        arrayList.add(dao.reigenNine(farmId, date.exportStart(), date.exportEnd()));
        return arrayList;
    }


}