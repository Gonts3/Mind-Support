package com.ultimate.mindsupport.chat;

import static androidx.core.content.ContextCompat.startForegroundService;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatFragment extends Fragment {
    LinearLayout emptyStateLayout ;
    private int id;
    private String type;
    private OkHttpClient client;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<UserChat> userChatList = new ArrayList<>();
    private final Handler handler = new Handler();
    private final int POLL_INTERVAL = 1000;
    private String lastUpdateTimestamp = "";
    private TextView emptyStateText;

    private final Runnable refreshUsersRunnable = new Runnable() {
        @Override
        public void run() {
            loadUsers();
            handler.postDelayed(this, POLL_INTERVAL);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_load_user, container, false); // reuse existing layout
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client = new OkHttpClient();
        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        if (CurrentUser.isClient()) {
            id = Integer.parseInt(CurrentUser.getClient().getId());
            type = "client";
        } else {
            id = Integer.parseInt(CurrentUser.getCounsellor().getId());
            type = "counsellor";
        }

        adapter = new UserAdapter(requireContext(), userChatList, userChat -> {
            Intent intent = new Intent(requireContext(), ChatActivity.class);
            intent.putExtra("receiver_id", userChat.getId());
            intent.putExtra("sender_id", id);
            intent.putExtra("receiver_name", userChat.getName());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(refreshUsersRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(refreshUsersRunnable);
    }

    private void loadUsers() {

        String url = "https://lamp.ms.wits.ac.za/home/s2841286/chat/get_users.php?user_id=" + id + "&type=" + type;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Failed to load users", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();
                try {
                    JSONObject json = new JSONObject(responseBody);
                    JSONArray users = json.getJSONArray("users");

                    String newestTimestamp = "";
                    List<UserChat> newUserChatList = new ArrayList<>();

                    requireActivity().runOnUiThread(() -> {
                        if (users.length() == 0) {
                            if(CurrentUser.isClient()){
                                emptyStateText = emptyStateLayout.findViewById(R.id.emptyStateText);
                                emptyStateText.setText("You don't have a counsellor assigned to you, \n " +
                                        "This means your counsellor decided that your problem has been resolved." +
                                        " You can either delete your account or select a new problem.");


                            }else{
                                emptyStateText = emptyStateLayout.findViewById(R.id.emptyStateText);
                                emptyStateText.setText("No clients currently assigned to you. \n Keep checking this window for a new client.");

                            }
                            emptyStateLayout.setVisibility(View.VISIBLE);
                        } else {
                                emptyStateLayout.setVisibility(View.GONE);
                        }
                    });

                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        int userId = user.getInt("id");
                        String userName = user.getString("username");
                        String time = user.optString("last_message_time", "");

                        String lastMsg = "No messages yet";
                        String formattedTime = "";
                        if (time != null && !time.isEmpty() && !"null".equalsIgnoreCase(time)) {
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
                                outputFormat.setTimeZone(TimeZone.getDefault());
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

                        boolean unread = user.optBoolean("unread", false);
                        String imageUrl = "https://api.dicebear.com/7.x/initials/svg?seed=" + userName;

                        UserChat userChat = new UserChat(userId, userName, lastMsg, formattedTime, imageUrl);
                        userChat.setUnread(unread);

                        newUserChatList.add(userChat);
                    }

                    if (!newUserChatList.equals(userChatList)) {
                        userChatList.clear();
                        userChatList.addAll(newUserChatList);
                        requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
