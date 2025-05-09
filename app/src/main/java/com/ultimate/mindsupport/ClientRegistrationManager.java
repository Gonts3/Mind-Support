package com.ultimate.mindsupport;

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

public class ClientRegistrationManager {
    private static final String REGISTER_URL = "https://lamp.ms.wits.ac.za/home/s2841286/client_register.php";
    private static final OkHttpClient client = new OkHttpClient();

    public interface RegistrationCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public static void registerClient(String username, String password, String email, RegistrationCallback callback) {
        final RequestBody[] formBody = {new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("email", email)
                .build()};

        Request request = new Request.Builder()
                .url(REGISTER_URL)
                .post(formBody[0])
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String message = "";
                if (response.isSuccessful() && response.body() != null) {
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

}

