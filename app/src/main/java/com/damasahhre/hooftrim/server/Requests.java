package com.damasahhre.hooftrim.server;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.models.DeletedSyncModel;
import com.damasahhre.hooftrim.database.models.SyncModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * کلاس برقراری ارتباط با server
 */
public class Requests {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "REQUESTS";
    private static final String BASE_URL = "http://130.185.77.250/";
    private static Context context;

    public static void setContext(Context context) {
        Requests.context = context;
    }


    public static void toastMessage(Response response, Activity activity) {
        activity.runOnUiThread(() -> {
            if (response.code() >= 500) {
                Toast.makeText(activity, R.string.server_error, Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                String message = (String) jsonObject.get("message");
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Toast.makeText(activity, R.string.server_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void checkVersion(Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "get_version/")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void isConfirmed(String email, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            String language = Constants.getDefaultLanguage(context);
            object.put("language", language);
            object.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/is_confirmed/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void pay(String email, Activity activity) {
        String url = BASE_URL + "payment/request/" + email;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);
    }

    public static void isPaid(String token, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            String language = Constants.getDefaultLanguage(context);
            object.put("language", language);
            object.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/is_premium/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void editPassword(String token, String oldPassword, String newPassword, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            String language = Constants.getDefaultLanguage(context);
            object.put("language", language);
            object.put("token", token);
            object.put("old_password", oldPassword);
            object.put("new_password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/edit_password/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void forgetPassword(String email, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            String language = Constants.getDefaultLanguage(context);
            object.put("language", language);
            object.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/forget_password/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void resend(String email, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            String language = Constants.getDefaultLanguage(context);
            object.put("language", language);
            object.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/resend/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void signUp(String email, String password, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            String language = Constants.getDefaultLanguage(context);
            object.put("language", language);
            object.put("email", email);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/sign_up/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void login(String email, String password, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            String language = Constants.getDefaultLanguage(context);
            object.put("language", language);
            object.put("email", email);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/login/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getAllData(String token, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            String language = Constants.getDefaultLanguage(context);
            object.put("language", language);
            object.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/sync/get_all/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void update(String token, SyncModel model, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json = gson.toJson(model);
        JSONObject object = null;
        try {
            object = new JSONObject(json);
            String language = Constants.getDefaultLanguage(context);
            object.put("language", language);
            object.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert object != null;
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/sync/update_data/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void create(String token, SyncModel model, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json = gson.toJson(model);
        JSONObject object = null;
        try {
            object = new JSONObject(json);
            String language = Constants.getDefaultLanguage(context);
            object.put("language", language);
            object.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert object != null;
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/sync/create_data/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void delete(String token, DeletedSyncModel model, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json = gson.toJson(model);
        JSONObject object = null;
        try {
            object = new JSONObject(json);
            String language = Constants.getDefaultLanguage(context);
            object.put("language", language);
            object.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert object != null;
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/sync/delete_data/")
                .method("DELETE", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void logout(String token, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            String language = Constants.getDefaultLanguage(context);
            object.put("language", language);
            object.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/logout/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
