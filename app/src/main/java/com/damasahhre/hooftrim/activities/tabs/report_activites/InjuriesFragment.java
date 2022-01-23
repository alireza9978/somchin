package com.damasahhre.hooftrim.activities.tabs.report_activites;

import static com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread;

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
import android.widget.Toast;

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
import com.damasahhre.hooftrim.database.models.InjureyReport;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.models.DateContainer;
import com.damasahhre.hooftrim.server.Requests;
import com.gun0912.tedpermission.PermissionListener;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Response;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2okhttp.OkHttpDownloader;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * صفحه مربوط به خروجی اکسل از جراحت‌های گاو‌ها
 */
public class InjuriesFragment extends Fragment {

    private ConstraintLayout farmLayout;
    private TextView farmText;

    private ConstraintLayout dateLayout;
    private TextView dateText;

    private long farmId = -1;
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
            if (!Constants.getPremium(requireContext())) {
                Toast.makeText(requireContext(), R.string.premium_require, Toast.LENGTH_LONG).show();
                return;
            }
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Toast.makeText(requireActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    if (date == null) {
                        Toast.makeText(requireContext(), getString(R.string.check_fields), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (farmId == -1) {
                        Toast.makeText(requireContext(), getString(R.string.check_fields), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    exportOnline();
                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    Toast.makeText(requireActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                }
            };
            Constants.checkPermission(permissionlistener);

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

    public void setFarm(long id) {
        this.farmId = id;
        MyDao dao = DataBase.getInstance(requireContext()).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            Farm farm = dao.getFarm(id);
            if (farm != null)
                requireActivity().runOnUiThread(() -> {
                    farmText.setText(farm.getName());
                    if (farmText.getText().toString().isEmpty()) {
                        farmLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
                    } else {
                        farmLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
                    }
                });
        });
    }

    public List<InjureyReport> findSame(List<InjureyReport> mainList) {
        List<InjureyReport> sameToRemove = new ArrayList<>();
        for (int i = 0; i < mainList.size() - 1; i++) {
            InjureyReport temp = mainList.get(i);
            for (int j = i + 1; j < mainList.size(); j++) {
                InjureyReport inner_temp = mainList.get(j);
                if (inner_temp.cowId.equals(temp.cowId) && temp.date.equals(inner_temp.date) &&
                        inner_temp.fingerNumber.equals(temp.fingerNumber)) {
                    sameToRemove.add(inner_temp);
                }
            }
        }
        return sameToRemove;
    }

    public void exportOnline() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(requireContext())
                .setDownloadConcurrentLimit(5)
                .setHttpDownloader(new OkHttpDownloader(okHttpClient))
                .build();

        Fetch fetch = Fetch.Impl.getInstance(fetchConfiguration);

        String file = "/downloads/test.txt";

        Requests.getInjuryFile(Constants.getToken(requireContext()), farmId, date.exportStart(), date.exportEnd(), new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {
                runOnUiThread(() -> Toast.makeText(requireActivity(), R.string.request_error, Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Response response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject object = new JSONObject(response.body().string());
                        Log.i("Injury export", "onResponse: " + object);
//                        String url = object.getString("link");
                        String url = "https://doc-0k-9s-sheets.googleusercontent.com/export/p21bsln2ga5s8q8dn5sjdoldoo/" +
                                "omctje37765cujfo3im2vacgjc/1642675835000/104846878786963225493/104846878786963225493/" +
                                "1_Z0MzQZezLl8LE7Qmn4zTOZTbG2aqY0535L0bismDyQ?format=xlsx&id=1_Z0MzQZezLl8LE7Qmn4zTOZTbG2a" +
                                "qY0535L0bismDyQ&dat=AFCstmobixu6GWoomsSKON-wBoPnc0_ZUH3dYPmb520VWa1qPMGYCCds4iNHSJx1ho4KKc-y" +
                                "-D0pmNzoFeE3OhImm_dWLentVkXOhslPQ7bMYOdK89X9wVww0rMQ9JMKHL3LUVmc7tSu96T8GirDjNqAswf-1WjFjjaF8" +
                                "R_QQVVVxAFxj7sKUqxMk0h0Hom63DKJ6ld4rCf2p2mszJOiZ-dV_Lj6xc1-l17nJSZc_XbiLon1JhZv_zaSMR6vhYLuL8" +
                                "biYEonmODeh7FPnHChfE2V9TyndF7h4t485tqTdmzCuHxvFbwaErDsIEDcxyPW7C6UWzSYuduTqOAe4fY1Ao7z7oxYMeN" +
                                "gctKvy7VYQ6hGoQTpFnKwI3noCiZWGNGW1zRVoRhtrcQWl5xY4oVnkSsW4mQLfs-PblxomVnrdKG_Yt1Mcz0OOpQYZ2kE19j" +
                                "VRZndFy-j0HAFD_HNYL9NpOEYr8xrum5wRquUob1TJR3aTBL9VoBoNIiHlTehycWDRcKaHu7VqSk7kw3J1znV6BEtq58hUmW9wn" +
                                "q9c5bw6j2pI6er8ENVoKN-6i8uwAU4D8y2_7tmPvKIFmnAfoMCV2ghn0sbX6fB1Q27BQORU92Lq4yhObzHL0MK5Nb65hZVYdttrKWL" +
                                "aIMypfO9aEmMn3Pmrm8Fw4xCLFmOKfz5P4FE4UJsGjDu9wiVIE6tz5ETptYusVQfmS-NW9XNhB5EThz449SkPP90";

                        final Request request = new com.tonyodev.fetch2.Request(url, file);
                        request.setPriority(Priority.HIGH);
                        request.setNetworkType(NetworkType.ALL);
                        fetch.enqueue(request, updatedRequest -> {
                            runOnUiThread(() -> Toast.makeText(requireActivity(), R.string.file_downloaded, Toast.LENGTH_LONG).show());
                        }, error -> {
                            //An error occurred enqueuing the request.
                        });
                    } else {
                        Requests.toastMessage(response, requireActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void export() {

        InjuryDao dao = DataBase.getInstance(requireContext()).injuryDao();
        MyDao myDao = DataBase.getInstance(requireContext()).dao();

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
        Integer[] rowsNameTwo = {
                R.string.column_name_1,
                R.string.column_name_2,
                R.string.column_name_3,
                R.string.column_name_4,
                R.string.column_name_5,
                R.string.column_name_6,
                R.string.column_name_7,
                R.string.column_name_8,
                R.string.column_name_9,
                R.string.column_name_10,
                R.string.column_name_11,
                R.string.column_name_12,
                R.string.column_name_13
        };

        AppExecutors.getInstance().diskIO().execute(() -> {
            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(dao.felemons(farmId, date.exportStart(), date.exportEnd()));
            arrayList.add(dao.deramatit(farmId, date.exportStart(), date.exportEnd()));
            List<InjureyReport> bottom = dao.woundHoofBottom(farmId, date.exportStart(), date.exportEnd());
            arrayList.add(bottom.size());
            int count = 0;
            List<InjureyReport> white = dao.whiteLineWound(farmId, date.exportStart(), date.exportEnd());
            white.removeAll(findSame(white));
            for (InjureyReport temp : white) {
                for (InjureyReport bot : bottom) {
                    if (temp.cowId.equals(bot.cowId) && temp.date.equals(bot.date) && temp.fingerNumber.equals(bot.fingerNumber)) {
                        count++;
                    }
                }
            }
            arrayList.add(white.size() - count);
            count = 0;
            List<InjureyReport> pange = dao.pangeWound(farmId, date.exportStart(), date.exportEnd());
            pange.removeAll(findSame(pange));
            for (InjureyReport temp : pange) {
                for (InjureyReport bot : bottom) {
                    if (temp.cowId.equals(bot.cowId) && temp.date.equals(bot.date) && temp.fingerNumber.equals(bot.fingerNumber)) {
                        count++;
                    }
                }
            }
            arrayList.add(pange.size() - count);

            List<InjureyReport> pashne = dao.pashneWound(farmId, date.exportStart(), date.exportEnd());

//            count = 0;
//            for (InjureyReport temp : pashne) {
//                for (InjureyReport bot : bottom) {
//                    if (temp.cowId.equals(bot.cowId) && temp.date.equals(bot.date) && temp.fingerNumber.equals(bot.fingerNumber)) {
//                        count++;
//                    }
//                }
//            }
            arrayList.add(pashne.size());

            List<InjureyReport> wallWound = dao.wallWound(farmId, date.exportStart(), date.exportEnd());
            wallWound.removeAll(findSame(wallWound));
            arrayList.add(wallWound.size());
            arrayList.add(dao.reigenNine(farmId, date.exportStart(), date.exportEnd()));

            ArrayList<Integer> secondPart = new ArrayList<>();
            double dayCount = dao.countDate(farmId, date.exportStart(), date.exportEnd());
            if (dayCount < 1) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Data error", Toast.LENGTH_SHORT).show();
                });
                return;
            }
            double box = dao.box(farmId, date.exportStart(), date.exportEnd()) / dayCount;
            secondPart.add(dao.visit(farmId, date.exportStart(), date.exportEnd()));
            secondPart.add(dao.newLimp(farmId, date.exportStart(), date.exportEnd()));
            secondPart.add(dao.sadRoze(farmId, date.exportStart(), date.exportEnd()));
            secondPart.add(dao.dryness(farmId, date.exportStart(), date.exportEnd()));
            secondPart.add(dao.delayed(farmId, date.exportStart(), date.exportEnd()));
            secondPart.add(dao.group(farmId, date.exportStart(), date.exportEnd()));
            secondPart.add(dao.longHoof(farmId, date.exportStart(), date.exportEnd()));
            secondPart.add(dao.somChini(farmId, date.exportStart(), date.exportEnd()));
            secondPart.add(dao.high(farmId, date.exportStart(), date.exportEnd()));
            secondPart.add(dao.heifer(farmId, date.exportStart(), date.exportEnd()));
            secondPart.add(dao.refrence(farmId, date.exportStart(), date.exportEnd()));
            secondPart.add(dao.boardingFactorAll(farmId, date.exportStart(), date.exportEnd()) +
                    dao.boardingFactorAllOther(farmId, date.exportStart(), date.exportEnd()));

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
                        Log.i("report", "export: " + i + " value = " + arrayList.get(i));
                    }
                }

                row = sheet.createRow(9);
                Cell temp_cell = row.createCell(0);
                temp_cell.setCellValue(getString(rowsNameTwo[0]));
                temp_cell = row.createCell(1);
                temp_cell.setCellValue(box);

                for (int i = 1; i < 13; i++) {
                    row = sheet.createRow(i + 9);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(getString(rowsNameTwo[i]));
                    cell = row.createCell(1);
                    if (secondPart.get(i - 1) == null) {
                        Log.i("report", "export 2 : " + i);
                        cell.setCellValue(0);
                    } else {
                        cell.setCellValue(secondPart.get(i - 1));
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

}