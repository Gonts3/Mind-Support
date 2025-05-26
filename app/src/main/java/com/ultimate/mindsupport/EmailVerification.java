package com.ultimate.mindsupport;

import org.json.JSONArray;
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

public class EmailVerification {
    private static final String VERIFICATION_URL = "https://lamp.ms.wits.ac.za/home/s2841286/email_verification.php";
    private static final String PASSWORD_OTP_URL = "https://lamp.ms.wits.ac.za/home/s2841286/otp_password.php";
    private static final OkHttpClient client = HTTPClient.getClient();

    public interface VerificationCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }


    public static void SendPasswordOtp(String email,String user, VerificationCallback callback) {

        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("user", user)
                .build();

        Request request = new Request.Builder()
                .url(PASSWORD_OTP_URL)
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
                    String token = "no otp";
                    String message = "";
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        message = jsonObject.get("message").toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    if (json.contains("success")) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            token = jsonObject.get("token").toString();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        callback.onSuccess(token);
                    } else {
                        callback.onFailure(message); // or parse JSON for error message
                    }
                } else {
                    callback.onFailure("Server error");
                }
            }
        });
    }


    public static void SendOtp(String email, VerificationCallback callback) {

        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .build();

        Request request = new Request.Builder()
                .url(VERIFICATION_URL)
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
                    String token = "no otp";


                    if (json.contains("success")) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            token = jsonObject.get("token").toString();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        callback.onSuccess(token);
                    } else {
                        callback.onFailure(json); // or parse JSON for error message
                    }
                } else {
                    callback.onFailure("Server error");
                }
            }
        });
    }
    public static void VerifyOtp(String otp,String token, VerificationCallback callback) {

        RequestBody formBody = new FormBody.Builder()
                .add("token", token)
                .add("otp",otp)
                .build();

        Request request = new Request.Builder()
                .url(VERIFICATION_URL)
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
                        callback.onSuccess(json);
                    } else {
                        callback.onFailure(json); // or parse JSON for error message
                    }
                } else {
                    callback.onFailure("Server error");
                }
            }
        });
    }

//    public static void VerifyOtp(String token, String otp) {
//        VerifyOtp(otp, token, new VerificationCallback() {
//
//            @Override
//            public void onSuccess(String message) {
//
//            }
//
//            @Override
//            public void onFailure(String error) {
//
//            }
//        });
//    }

    }
