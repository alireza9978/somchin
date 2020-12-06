package com.damasahhre.hooftrim.server;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

public class Requests {

    private static final String TAG = "REQUESTS";
    private static final String BASE_URL = "http://";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void isConfirmed(String email, Callback callback){
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "is confirmed: " + object);
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/is_confirmed/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void isPaid(String email, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "is paid: " + object);
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/forget_password/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void editPassword(String email, String oldPassword, String newPassword, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
            object.put("old_password", oldPassword);
            object.put("new_password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "edit pass: " + object);
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
            object.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "forget: " + object);
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/sign_up/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void resend(String email, String password, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "resend: " + object);
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
            object.put("email", email);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "sign_up: " + object);
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
            object.put("email", email);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "login: " + object);
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + "user/login/")
                .method("POST", body)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
