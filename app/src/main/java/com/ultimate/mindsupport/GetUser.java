package com.ultimate.mindsupport;

import android.util.Log;

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

public class GetUser {

    private static final String GET_CLIENT_URL = "https://lamp.ms.wits.ac.za/home/s2841286/get_client.php";
    private static final String GET_COUNSELLOR_URL = "https://lamp.ms.wits.ac.za/home/s2841286/get_counsellor.php";
    private static final OkHttpClient client = HTTPClient.getClient();

    public interface GetUserCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }
    public static void getClient(String id, GetUserCallback callback) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id",id)
                    .build();

            Request request = new Request.Builder()
                    .url(GET_CLIENT_URL)
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
                            callback.onSuccess("Retrieved user!");


                                String client_id = "";
                                String client_username = "";
                                String problem_id = "";
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    client_id = jsonObject.get("client_id").toString();
                                    client_username = jsonObject.get("client_username").toString();
                                    problem_id = jsonObject.get("problem_id").toString();
                                    Client client = new Client(client_id,client_username,problem_id);
                                    CurrentUser.set(client);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                        }
                        else {
                            callback.onFailure(json); // or parse JSON for error message
                        }
                    } else {
                        callback.onFailure("Server error");
                    }
                }
            });
    }
    public static void GetCounsellor(String id, GetUserCallback callback) {
        RequestBody formBody = new FormBody.Builder()
                .add("id",id)
                .build();

        Request request = new Request.Builder()
                .url(GET_COUNSELLOR_URL)
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
                        callback.onSuccess("Retrieved user!");

                        String counsellor_id = "";
                        String counsellor_fname = "";
                        String  counsellor_lname = "";
                        String counsellor_reg = "";
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            counsellor_id = jsonObject.get("counsellor_id").toString();
                            counsellor_fname = jsonObject.get("counsellor_fname").toString();
                            counsellor_lname = jsonObject.get("counsellor_lname").toString();
                            counsellor_reg = jsonObject.get("counsellor_reg").toString();
                            Counsellor counsellor = new Counsellor(counsellor_id,counsellor_fname,counsellor_lname,counsellor_reg);
                            CurrentUser.set(counsellor);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    else {
                        callback.onFailure(json); // or parse JSON for error message
                    }
                } else {
                    callback.onFailure("Server error");
                }
            }
        });
    }


}


