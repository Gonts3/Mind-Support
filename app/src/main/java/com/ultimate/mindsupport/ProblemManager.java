package com.ultimate.mindsupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProblemManager {
    private static final String ASSIGNMENT_URL = "https://lamp.ms.wits.ac.za/home/s2841286/counsellor_assignment.php";
    private static final String COUNSELLOR_PROBLEM_URL = "https://lamp.ms.wits.ac.za/home/s2841286/counsellor_problem.php";
    private static final String CLIENT_PROBLEM_URL = "https://lamp.ms.wits.ac.za/home/s2841286/client_problem.php";
    private static final OkHttpClient client = HTTPClient.getClient();

    public interface ProblemCallback {
        void onSuccess(String message);

        void onFailure(String error);
    }

    public static void AddClientProblem(String problem_id, String id, LoginManager.LoginCallback callback) {
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", id)
                .add("problem_id", problem_id)
                .build();

        Request request = new Request.Builder()
                .url(CLIENT_PROBLEM_URL)
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
                    String message = "";
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        message = jsonObject.get("message").toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    if (json.contains("success")) {
                        callback.onSuccess("Problem added successfully");
                    } else {
                        callback.onFailure(message); // or parse JSON for error message
                    }
                } else {
                    callback.onFailure("Server error");
                }
            }
        });
    }
    public static void AddCounsellorProblems(String counsellor_id, ArrayList<String>problems, LoginManager.LoginCallback callback) {
        JSONArray jsonArray = new JSONArray();
        for (String problem : problems) {
            jsonArray.put(problem);
        }
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("problems",jsonArray);
            jsonObject.put("counsellor_id",counsellor_id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(COUNSELLOR_PROBLEM_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String message = "";
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        message = jsonObject.get("message").toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    if (json.contains("success")) {
                        callback.onSuccess("Problems added successfully");
                    } else {
                        callback.onFailure(message); // or parse JSON for error message
                    }
                } else {
                    callback.onFailure("Server error");
                }
            }
        });
    }
    public static void AssignCounsellor(String counsellor_id, String client_id, LoginManager.LoginCallback callback) {
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", client_id)
                .add("counsellor_id", counsellor_id)
                .build();

        Request request = new Request.Builder()
                .url(ASSIGNMENT_URL)
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
                    String message = "";
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        message = jsonObject.get("message").toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    if (json.contains("success")) {
                        callback.onSuccess("Counsellor assigned successfully!");
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
