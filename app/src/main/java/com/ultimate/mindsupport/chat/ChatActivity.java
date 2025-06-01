package com.ultimate.mindsupport.chat;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ChatActivity extends AppCompatActivity {

    int senderId, receiverId;
    RecyclerView recyclerViewMessages;
    EditText messageInput;
    ImageButton sendButton;

    List<Message> messageList = new ArrayList<>();
    MessageAdapter adapter;
    private Handler handler = new Handler();
    private int lastMessageId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat2);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        messageInput = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.buttonSend);

        senderId = getIntent().getIntExtra("sender_id", -1);
        receiverId = getIntent().getIntExtra("receiver_id", -1);

        String receiverName = getIntent().getStringExtra("receiver_name");
        TextView name  = findViewById(R.id.textUserName);
        TextView letter = findViewById(R.id.textProfileLetter);
        letter.setText(receiverName.substring(0, 1).toUpperCase());
        name.setText(receiverName);

        adapter = new MessageAdapter(messageList, senderId);
        recyclerViewMessages.setAdapter(adapter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));


        if (senderId != -1 && receiverId != -1) {
            loadMessages(senderId, receiverId);
            markMessagesAsRead(senderId, receiverId);
        }



        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(senderId, receiverId, message);
                messageInput.setText("");
                recyclerViewMessages.scrollToPosition(messageList.size() - 1);
            }
        });

        startMessagePolling();


    }

    private void sendMessage(int senderId, int receiverId, String content) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("sender_id", String.valueOf(senderId))
                .add("receiver_id", String.valueOf(receiverId))
                .add("content", content)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2841286//chat/send_message.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> loadMessages(senderId, receiverId));
            }
        });
    }

    private void loadMessages(int sender, int receiver) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2841286/chat/get_messages.php"
                + "?sender_id=" + sender
                + "&receiver_id=" + receiver
                + "&last_id=" + lastMessageId;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                try {
                    JSONObject json = new JSONObject(responseBody);
                    JSONArray messages = json.getJSONArray("messages");

                    if (messages.length() > 0) {
                        for (int i = 0; i < messages.length(); i++) {
                            JSONObject msg = messages.getJSONObject(i);
                            int id = msg.getInt("id"); // New!
                            int from = msg.getInt("sender_id");
                            String content = msg.getString("content");
                            String timestamp = msg.optString("timestamp", "");

                            messageList.add(new Message(from, content, timestamp));

                            // Track the latest ID
                            if (id > lastMessageId) {
                                lastMessageId = id;
                            }
                        }

                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startMessagePolling() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchNewMessages();
                handler.postDelayed(this, 2000); // repeat every 2 seconds
            }
        }, 2000);
    }

    private void fetchNewMessages() {
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2841286/chat/get_messages.php"
                + "?sender_id=" + senderId
                + "&receiver_id=" + receiverId
                + "&last_id=" + lastMessageId;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                try {
                    JSONObject json = new JSONObject(responseBody);
                    JSONArray messages = json.getJSONArray("messages");

                    if (messages.length() > 0) {
                        for (int i = 0; i < messages.length(); i++) {
                            JSONObject msg = messages.getJSONObject(i);
                            int from = msg.getInt("sender_id");
                            String content = msg.getString("content");
                            String timestamp = msg.getString("timestamp");
                            int msgId = msg.getInt("id");

                            messageList.add(new Message(from, content, timestamp));
                            lastMessageId = Math.max(lastMessageId, msgId);
                        }

                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void markMessagesAsRead(int userId, int chatPartnerId) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2841286/chat/Mark_read.php"
                + "?user_id=" + userId
                + "&chat_partner_id=" + chatPartnerId;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Optional: you can parse response here if needed
                String resp = response.body().string();
                System.out.println("Mark_read response: " + resp);
            }
        });
    }


    public void backToChatList(View view) {
        finish();
    }
}