package com.damasahhre.hooftrim.server;

import static com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.models.DeletedSyncModel;
import com.damasahhre.hooftrim.database.models.SyncModel;
import com.damasahhre.hooftrim.database.utils.AppExecutors;
import com.damasahhre.hooftrim.models.MyDate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

/**
 * کلاس برقراری ارتباط با server
 */
public class Requests {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //    private static final String BASE_URL = "http://130.185.77.250/";
    private static final String BASE_URL = "http://176.97.218.196:8000/";
    private static Context context;

    private static final JsonSerializer<MyDate> serializer = (src, typeOfSrc, context) -> new JsonPrimitive(src.toJsonString());
    private static final JsonDeserializer<MyDate> deserializer = (json, typeOfT, context) -> {
        String date = json.getAsString();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));
        return new MyDate(day, month, year);
    };

    public static void setContext(Context context) {
        Requests.context = context;
    }

    public static void toastMessage(Response response, Activity activity) {
        activity.runOnUiThread(() -> {
            if (response.code() >= 500) {
                Toast.makeText(activity, R.string.server_error, Toast.LENGTH_LONG).show();
                return;
            }
            AppExecutors.getInstance().networkIO().execute(() -> {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Iterator<String> keys = jsonObject.keys();
                    final StringBuilder message = new StringBuilder();
                    while (keys.hasNext()) {
                        String temp_key = keys.next();
                        message.append(temp_key);
                        message.append(":");
                        message.append(jsonObject.getJSONArray(temp_key).get(0));
                        message.append("\n");
                    }
                    activity.runOnUiThread(() -> Toast.makeText(activity, message.toString(), Toast.LENGTH_LONG).show());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(() -> Toast.makeText(activity, R.string.server_error, Toast.LENGTH_LONG).show());
                }
            });
        });
    }

    public static void checkVersion(Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "api/app_versions/get_latest_version/")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void isValidated(String email, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        String language = Constants.getDefaultLanguage(context);
        Request request = new Request.Builder()
                .url(BASE_URL + "user/is_active/?email=" + email)
                .method("GET", null)
                .addHeader("language", language)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void pay(String email, Activity activity) {
        String url = BASE_URL + "api/payment/request/?email=" + email;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);
    }

    public static void isPaid(String token, Callback callback) {
        refreshToken();
        OkHttpClient client = new OkHttpClient();
        String language = Constants.getDefaultLanguage(context);
        Request request = new Request.Builder()
                .url(BASE_URL + "user/is_premium/")
                .method("GET", null)
                .addHeader("language", language)
                .addHeader("Authorization", token)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void editPassword(String token, String oldPassword, String newPassword, Callback callback) {
        refreshToken();
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        String language = Constants.getDefaultLanguage(context);
        try {
            object.put("current_password", oldPassword);
            object.put("new_password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/auth/users/set_password/")
                .method("POST", body)
                .addHeader("language", language)
                .addHeader("Authorization", token)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void forgetPassword(String email, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        String language = Constants.getDefaultLanguage(context);
        try {
            object.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/auth/users/reset_password/")
                .method("POST", body)
                .addHeader("language", language)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void resend(String email, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        String language = Constants.getDefaultLanguage(context);
        try {
            object.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/auth/users/resend_activation/")
                .method("POST", body)
                .addHeader("language", language)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void signUp(String email, String password, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        String language = Constants.getDefaultLanguage(context);
        try {
            object.put("email", email);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/auth/users/")
                .method("POST", body)
                .addHeader("language", language)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void login(String email, String password, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        String language = Constants.getDefaultLanguage(context);
        try {
            object.put("email", email);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/login/")
                .method("POST", body)
                .addHeader("language", language)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getAllData(String token, Callback callback) {
        refreshToken();
        OkHttpClient client = new OkHttpClient();
        String language = Constants.getDefaultLanguage(context);
        Request request = new Request.Builder()
                .url(BASE_URL + "api/sync_data/")
                .method("GET", null)
                .addHeader("Authorization", token)
                .addHeader("language", language)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void update(String token, SyncModel model, Callback callback) {
        refreshToken();
        OkHttpClient client = new OkHttpClient();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MyDate.class, serializer);
        String language = Constants.getDefaultLanguage(context);
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json = gson.toJson(model);
        JSONObject object = null;
        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert object != null;
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "api/sync_data/")
                .method("PUT", body)
                .addHeader("Authorization", token)
                .addHeader("language", language)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void create(String token, SyncModel model, Callback callback) {
        refreshToken();
        OkHttpClient client = new OkHttpClient();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MyDate.class, serializer);
        String language = Constants.getDefaultLanguage(context);
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json = gson.toJson(model);
        JSONObject object = null;
        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert object != null;
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "api/sync_data/")
                .method("POST", body)
                .addHeader("Authorization", token)
                .addHeader("language", language)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void delete(String token, DeletedSyncModel model, Callback callback) {
        refreshToken();
        String language = Constants.getDefaultLanguage(context);
        OkHttpClient client = new OkHttpClient();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MyDate.class, serializer);
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json = gson.toJson(model);
        JSONObject object = null;
        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert object != null;
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "api/sync_data/")
                .method("DELETE", body)
                .addHeader("Authorization", token)
                .addHeader("language", language)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void refreshToken() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(() -> Toast.makeText(context, R.string.request_error, Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        Constants.setToken(context, object.getString("refresh"));
                        Constants.setToken(context, object.getString("access"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
//                    todo login again
                }
            }
        };
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            object.put("refresh", Constants.getTokenRefresh(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String language = Constants.getDefaultLanguage(context);
        RequestBody body = RequestBody.create(JSON, object.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "/user/token/refresh/")
                .method("POST", body)
                .addHeader("language", language)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void logout(String token, Callback callback) {
        refreshToken();
        OkHttpClient client = new OkHttpClient();
        String language = Constants.getDefaultLanguage(context);
        Request request = new Request.Builder()
                .url(BASE_URL + "user/logout/")
                .method("GET", null)
                .addHeader("Authorization", token)
                .addHeader("language", language)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getInjuryFile(String token, long farmId, MyDate startDate, MyDate endDate, Callback callback) {
        refreshToken();
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            object.put("farm_id", farmId);
            object.put("start_date", startDate.toJsonString());
            object.put("end_date", endDate.toJsonString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String language = Constants.getDefaultLanguage(context);
        Request request = new Request.Builder()
                .url(BASE_URL + "api//")
                .method("GET", null)
                .addHeader("Authorization", token)
                .addHeader("language", language)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static JsonDeserializer<MyDate> getDateDeserializer() {
        return deserializer;
    }
}
