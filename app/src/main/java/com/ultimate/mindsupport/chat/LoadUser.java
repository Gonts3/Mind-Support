package com.ultimate.mindsupport.chat;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ultimate.mindsupport.CurrentUser;
import com.ultimate.mindsupport.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadUser extends AppCompatActivity {
    int id;
    String type;
    private OkHttpClient client;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<UserChat> userChatList = new ArrayList<>();


    private final android.os.Handler handler = new android.os.Handler();
    private final int POLL_INTERVAL = 1000; // 1 seconds
    private String lastUpdateTimestamp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_user);

        client = new OkHttpClient();
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(CurrentUser.isClient()){
            id = Integer.parseInt(CurrentUser.getClient().getId());
            type = "client";
        }
        else{
            id = Integer.parseInt(CurrentUser.getCounsellor().getId());
            type = "counsellor";
        }

        adapter = new UserAdapter(this, userChatList, userChat -> {
            Intent intent = new Intent(LoadUser.this, ChatActivity.class);
            intent.putExtra("receiver_id", userChat.getId());
            intent.putExtra("sender_id", id);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    private final Runnable refreshUsersRunnable = new Runnable() {
        @Override
        public void run() {
            loadUsers(); // fetch and update UI if needed
            handler.postDelayed(this, POLL_INTERVAL); // schedule next run
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(refreshUsersRunnable); // start polling
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(refreshUsersRunnable); // stop polling when not visible
    }

    private void loadUsers() {
        String url = "https://lamp.ms.wits.ac.za/home/s2841286/chat/get_users.php?user_id=" + id + "&type="+type;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(LoadUser.this, "Failed to load users", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();

                try {
                    JSONObject json = new JSONObject(responseBody);
                    JSONArray users = json.getJSONArray("users");

                    // Determine the latest timestamp in this data set
                    String newestTimestamp = "";
                    List<UserChat> newUserChatList = new ArrayList<>();

                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);

                        int userId = user.getInt("id");
                        String userName = user.getString("username");

                        String lastMsg = user.optString("last_message", "");
                        String time = user.optString("last_message_time", "");

                        String formattedTime = time;
                        try {
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
                            Date date = inputFormat.parse(time);
                            formattedTime = outputFormat.format(date);
                        } catch (Exception e) {
                            e.printStackTrace();
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

