package com.ultimate.mindsupport.chat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.ultimate.mindsupport.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatNotificationService extends Service {

    private static final String CHANNEL_ID = "chat_channel";
    private final Handler handler = new Handler();
    private final OkHttpClient client = new OkHttpClient();
    private int userId = -1;
    private String userType = null;
    private final int POLL_INTERVAL = 5000;
    private final Set<String> notifiedMessages = new HashSet<>();

    private final Runnable pollRunnable = new Runnable() {
        @Override
        public void run() {
            if (userId != -1 && userType != null) {
                pollForMessages();
                handler.postDelayed(this, POLL_INTERVAL);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(1, getForegroundNotification("Chat service running"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            userId = intent.getIntExtra("user_id", -1);
            userType = intent.getStringExtra("user_type");
        }

        if (userId == -1 || userType == null) {
            stopSelf();
            return START_NOT_STICKY;
        }

        handler.post(pollRunnable);
        return START_STICKY;
    }

    private void pollForMessages() {
        String url = "https://lamp.ms.wits.ac.za/home/s2841286/chat/get_users.php?user_id=" + userId + "&type=" + userType;

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                try {
                    JSONObject json = new JSONObject(responseBody);
                    JSONArray users = json.getJSONArray("users");

                    boolean anyUnread = false;

                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        boolean unread = user.optBoolean("unread", false);

                        if (unread) {
                            anyUnread = true;

                            String username = user.getString("username");
                            String message = user.optString("last_message", "New message");
                            String uniqueKey = username + "|" + message;

                            if (!notifiedMessages.contains(uniqueKey)) {
                                notifiedMessages.add(uniqueKey);
                                sendMessageNotification(username, message);
                            }
                        }
                    }

                    // If no users have unread messages, clear the notification cache
                    if (!anyUnread) {
                        notifiedMessages.clear();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void sendMessageNotification(String senderName, String messageContent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return; // Permission not granted
            }
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_action_chat)
                .setContentTitle(senderName)
                .setContentText(messageContent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat.from(this).notify((int) System.currentTimeMillis(), notification);
    }

    private Notification getForegroundNotification(String content) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("MindSupport")
                .setContentText(content)
                .setSmallIcon(android.R.drawable.sym_action_chat)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Chat Messages",
                    NotificationManager.IMPORTANCE_HIGH  // Change from IMPORTANCE_LOW to HIGH
            );
            channel.setDescription("Handles background message polling");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(pollRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
