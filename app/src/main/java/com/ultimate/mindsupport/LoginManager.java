package com.ultimate.mindsupport;

import com.ultimate.mindsupport.client.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginManager {
    private static final String LOGIN_URL = "https://lamp.ms.wits.ac.za/home/s2841286/login.php";
    private static final OkHttpClient client = HTTPClient.getClient();

    public interface LoginCallback {
        void onSuccess(String message);

        void onFailure(String error);
    }


    public static void LoginUser(String email, String password, String user, LoginManager.LoginCallback callback) {

        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("user", user)
                .build();

        Request request = new Request.Builder()
                .url(LOGIN_URL)
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


                    if (json.contains("success")) {
                        callback.onSuccess("Login successful!");
                    } else {
                        callback.onFailure(json); // or parse JSON for error message
                    }
                } else {
                    callback.onFailure("Server error");
                }
            }
        });
    }
}
