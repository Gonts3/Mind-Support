package com.ultimate.mindsupport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ultimate.mindsupport.chat.AssignmentAdapter;
import com.ultimate.mindsupport.chat.LoadUser;
import com.ultimate.mindsupport.chat.UserChat;
import com.ultimate.mindsupport.client.ClientLoginActivity;
import com.ultimate.mindsupport.counsellor.CouncillorScreen;
import com.ultimate.mindsupport.counsellor.CounsellorPersonalInformation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ManageAssignmentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AssignmentAdapter adapter;
    private List<UserChat> userChatList = new ArrayList<>();
    private String lastUpdateTimestamp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_assigment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerAssigned);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new AssignmentAdapter(userChatList, new AssignmentAdapter.OnUserActionListener() {
            @Override
            public void onEndSession(UserChat user) {
                Toast.makeText(ManageAssignmentActivity.this, "Ending session for " + user.getName(), Toast.LENGTH_SHORT).show();
                // TODO: API call to end session
                String client_id = String.valueOf(user.getId());
                ProblemManager.DeleteAssignment(client_id,CurrentUser.get().getId(), new ProblemManager.ProblemCallback() {
                    @Override
                    public void onSuccess(String message) {
                        runOnUiThread(() -> {
                            Toast.makeText(ManageAssignmentActivity.this, message, Toast.LENGTH_SHORT).show();
                            userChatList.remove(user);
                            adapter.notifyDataSetChanged();
                        });
                        loadUsers();
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(ManageAssignmentActivity.this, error, Toast.LENGTH_SHORT).show();
                            userChatList.remove(user);
                            adapter.notifyDataSetChanged();
                        });
                        loadUsers();
                    }
                });






            }

            @Override
            public void onReassign(UserChat user) {
                Toast.makeText(ManageAssignmentActivity.this, "Reassigning " + user.getName(), Toast.LENGTH_SHORT).show();
                String client_id = String.valueOf(user.getId());
                // TODO: Open reassign UI or activity
                ProblemManager.ReAssignCounsellor(client_id,CurrentUser.get().getId(), new ProblemManager.ProblemCallback() {
                    @Override
                    public void onSuccess(String message) {
                        runOnUiThread(() -> {
                            Toast.makeText(ManageAssignmentActivity.this, message, Toast.LENGTH_SHORT).show();
                            userChatList.remove(user);
                            adapter.notifyDataSetChanged();
                        });
                        loadUsers();
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(ManageAssignmentActivity.this, error, Toast.LENGTH_SHORT).show();
                            userChatList.remove(user);
                            adapter.notifyDataSetChanged();
                        });
                        loadUsers();
                    }
                });


            }
        });

        recyclerView.setAdapter(adapter);
        loadUsers();
    }
    private void loadUsers() {

        String id = CurrentUser.get().getId();
        String type = "counsellor";
        String url = "https://lamp.ms.wits.ac.za/home/s2841286/chat/get_users.php?user_id=" + id + "&type="+type;
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(ManageAssignmentActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();

                try {
                    JSONObject json = new JSONObject(responseBody);
                    JSONArray users = json.getJSONArray("users");
                    List<UserChat> newUserChatList = new ArrayList<>();
                    String newestTimestamp = "";
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        String userName = user.getString("username");
                        int userId = user.getInt("id");
                        String time = user.optString("last_message_time", "");

                        String lastMsg = "No messages yet";
                        String formattedTime = "";
                        if (time != null && !time.isEmpty() && !"null".equalsIgnoreCase(time)) {
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
                                outputFormat.setTimeZone(TimeZone.getDefault());  // local time

                                Date date = inputFormat.parse(time);
                                formattedTime = outputFormat.format(date);

                                lastMsg = user.optString("last_message", "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (time.compareTo(newestTimestamp) > 0) {
                            newestTimestamp = time;
                        }

                        String imageUrl = "https://api.dicebear.com/7.x/initials/svg?seed=" + userName;
                        newUserChatList.add(new UserChat(userId, userName, lastMsg, formattedTime, imageUrl));
                    }


                    // Only update UI if data changed (new timestamp is more recent)
                    if (!newestTimestamp.equals(lastUpdateTimestamp)) {
                        lastUpdateTimestamp = newestTimestamp;
                        userChatList.clear();
                        userChatList.addAll(newUserChatList);
                        runOnUiThread(() -> adapter.notifyDataSetChanged());
                    }


                }catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void backToProfileC2(View v){
        finish();
    }
}