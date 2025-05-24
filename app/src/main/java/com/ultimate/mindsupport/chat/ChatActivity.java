package com.ultimate.mindsupport.chat;

import android.os.Bundle;
import com.ultimate.mindsupport.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatActivity extends AppCompatActivity {
    int senderId, receiverId;
    private WebSocket webSocket;
    private OkHttpClient client;
    TextView messageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        client = new OkHttpClient();
        messageBox = findViewById(R.id.messageBox);

        senderId = getIntent().getIntExtra("sender_id", -1);
        receiverId = getIntent().getIntExtra("receiver_id", -1);

        if (senderId != -1 && receiverId != -1) {
            loadMessages(senderId, receiverId);
        }

        startWebSocket();

        EditText messageInput = findViewById(R.id.messageInput);
        Button sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(senderId, receiverId, message);
                messageInput.setText(""); // Clear input
            }
        });

    }

    private void sendMessage(int senderId, int receiverId, String content) {
        // Send to WebSocket for real-time delivery
        if (webSocket != null) {
            JSONObject msg = new JSONObject();
            try {
                msg.put("type", "chat");
                msg.put("sender_id", senderId);
                msg.put("receiver_id", receiverId);
                msg.put("message", content);
                webSocket.send(msg.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Also send to PHP backend to store in DB
        OkHttpClient httpClient = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("sender_id", String.valueOf(senderId))
                .add("receiver_id", String.valueOf(receiverId))
                .add("content", content)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2841286/chat/send_message.php")
                .post(formBody)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> loadMessages(senderId, receiverId));
            }
        });
    }


    private void loadMessages(int sender, int receiver) {
        String url = "https://lamp.ms.wits.ac.za/home/s2841286/chat/get_messages.php?sender_id=" + sender + "&receiver_id=" + receiver;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                try {
                    JSONObject json = new JSONObject(responseBody);
                    JSONArray messages = json.getJSONArray("messages");

                    StringBuilder msgDisplay = new StringBuilder();

                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject msg = messages.getJSONObject(i);
                        String content = msg.getString("content");
                        int from = msg.getInt("sender_id");

                        msgDisplay.append(from == senderId ? "Me: " : "Them: ").append(content).append("\n");
                    }

                    runOnUiThread(() -> messageBox.setText(msgDisplay.toString()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startWebSocket() {
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("ws://146.141.21.111:8080") // Your server's IP
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d("WebSocket", "Connected");

                // Send registration message
                JSONObject registerMessage = new JSONObject();
                try {
                    registerMessage.put("type", "register");
                    registerMessage.put("user_id", senderId);
                    webSocket.send(registerMessage.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d("WebSocket", "Received: " + text);
                runOnUiThread(() -> {
                    try {
                        JSONObject msg = new JSONObject(text);
                        String message = msg.getString("message");
                        int from = msg.getInt("from");

                        String newMessage = (from == senderId ? "Me: " : "Them: ") + message + "\n";
                        messageBox.append(newMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                Log.d("WebSocket", "Closing: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e("WebSocket", "Error: " + t.getMessage());
            }
        });
    }
}