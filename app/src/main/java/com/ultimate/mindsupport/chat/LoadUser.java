package com.ultimate.mindsupport.chat;

import com.ultimate.mindsupport.CurrentUser;
import com.ultimate.mindsupport.R;
import com.ultimate.mindsupport.SessionManager;
import com.ultimate.mindsupport.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadUser extends AppCompatActivity {

    LinearLayout userButtonContainer;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_load_user); // Optional: Rename to R.layout.activity_load_user if you rename the XML
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userButtonContainer = findViewById(R.id.userButtonContainer);
        OkHttpClient client = new OkHttpClient();

        int id;
        if(CurrentUser.isClient()){
            type = "client";
            id = Integer.parseInt(CurrentUser.getClient().getId());
        }
        else{
            id = Integer.parseInt(CurrentUser.getCounsellor().getId());
            type = "counsellor";
        }

       // int id = 17;
        //type = "counsellor";
        String url = "https://lamp.ms.wits.ac.za/home/s2841286/chat/get_users.php?user_id=" + id + "&type="+type;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) return;

                String responseBody = response.body().string();

                try {
                    JSONObject json = new JSONObject(responseBody);
                    JSONArray users = json.getJSONArray("users");

                    runOnUiThread(() -> {
                        for (int i = 0; i < users.length(); i++) {
                            try {
                                JSONObject user = users.getJSONObject(i);
                                int userId = user.getInt("id");
                                String userName = user.getString("username");

                                Button btn = new Button(LoadUser.this);
                                btn.setText(userName);
                                btn.setOnClickListener(v -> {
                                    Intent intent = new Intent(LoadUser.this, ChatActivity.class);
                                    intent.putExtra("receiver_id", userId); // Clicked user
                                    intent.putExtra("sender_id", id); // Current user (replace 17 as needed)
                                    startActivity(intent);
                                });
                                userButtonContainer.addView(btn);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
