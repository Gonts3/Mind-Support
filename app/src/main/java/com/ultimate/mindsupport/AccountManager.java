package com.ultimate.mindsupport;

import android.graphics.Point;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccountManager {
    public interface AccountCallback {
        void onSuccess(String message);

        void onFailure(String error);
    }

    private static final String CHANGE_COUNSELLOR_NAME_URL = "https://lamp.ms.wits.ac.za/home/s2841286/change_counsellor_names.php";
    private static final String CHANGE_USERNAME_URL = "https://lamp.ms.wits.ac.za/home/s2841286/change_client_username.php";
    private static final String DELETE_COUNSELLOR_URL = "https://lamp.ms.wits.ac.za/home/s2841286/delete_counsellor_account.php";
    private static final String DELETE_CLIENT_URL = "https://lamp.ms.wits.ac.za/home/s2841286/delete_client_account.php";
    private static final String PASSWORD_RESET_URL = "https://lamp.ms.wits.ac.za/home/s2841286/password_reset.php";

    private static final OkHttpClient client = HTTPClient.getClient();

    //changing counsellor names

    public static void ResetPassword(String email, String password, String user, LoginManager.LoginCallback callback) {
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("user", user)
                .build();

        Request request = new Request.Builder()
                .url(PASSWORD_RESET_URL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    String message = "";
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        message = jsonObject.get("message").toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    if (json.contains("success")) {
                        callback.onSuccess(message);
                    } else {
                        callback.onFailure(message); // or parse JSON for error message
                    }
                } else {
                    callback.onFailure("Server error");
                }
            }
        });


    }

    public static void ChangeCounsellorName(String counsellor_id, String fname, String lname, AccountManager.AccountCallback callback) {
        RequestBody formBody = new FormBody.Builder()
                .add("user_id", counsellor_id)
                .add("new_fname", fname)
                .add("new_lname", lname)
                .build();

        Request request = new Request.Builder()
                .url(CHANGE_COUNSELLOR_NAME_URL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    String message = "";
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        message = jsonObject.get("message").toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    if (json.contains("success")) {
                        callback.onSuccess(message);
                    } else {
                        callback.onFailure(message); // or parse JSON for error message
                    }
                } else {
                    callback.onFailure("Server error");
                }
            }
        });
    }

    public static void ChangeClientName(String counsellor_id, String username, AccountManager.AccountCallback callback) {
        RequestBody formBody = new FormBody.Builder()
                .add("id", counsellor_id)
                .add("new_username", username)
                .build();

        Request request = new Request.Builder()
                .url(CHANGE_USERNAME_URL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    String message = "";
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        message = jsonObject.get("message").toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    if (json.contains("success")) {
                        callback.onSuccess(message);
                    } else {
                        callback.onFailure(message); // or parse JSON for error message
                    }
                } else {
                    callback.onFailure("Server error");
                }
            }
        });
    }

    public static void DeleteCounsellor(String counsellor_id, AccountManager.AccountCallback callback) {
        RequestBody formBody = new FormBody.Builder()
                .add("id", counsellor_id)
                .build();

        Request request = new Request.Builder()
                .url(DELETE_COUNSELLOR_URL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    String message = "";
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        message = jsonObject.get("message").toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    if (json.contains("success")) {
                        callback.onSuccess(message);
                    } else {
                        callback.onFailure(message); // or parse JSON for error message
                    }
                } else {
                    callback.onFailure("Server error");
                }
            }
        });
    }
    public static void DeleteClient(String client_id, AccountManager.AccountCallback callback) {
        RequestBody formBody = new FormBody.Builder()
                .add("id", client_id)
                .build();

        Request request = new Request.Builder()
                .url(DELETE_CLIENT_URL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    String message = "";
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        message = jsonObject.get("message").toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    if (json.contains("success")) {
                        callback.onSuccess(message);
                    } else {
                        callback.onFailure(message); // or parse JSON for error message
                    }
                } else {
                    callback.onFailure("Server error");
                }
            }
        });
    }
}



