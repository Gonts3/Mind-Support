package com.ultimate.mindsupport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ultimate.mindsupport.client.ClientScreen;
import com.ultimate.mindsupport.counsellor.CouncillorScreen;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // force light mode
        new Handler().postDelayed(() -> {
        }, 5000);
        try {
            SessionManager.init(this);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        if (SessionManager.isLoggedIn()) {
            if (SessionManager.getUserType().equals("client")) {
                SessionManager.loadClientSession(new GetUser.GetUserCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Intent intent = new Intent(SplashActivity.this, ClientScreen.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String error) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

            } else {
                SessionManager.loadCounsellorSession(new GetUser.GetUserCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Intent intent = new Intent(SplashActivity.this, CouncillorScreen.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String error) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);

                    }


                });
            }
        }else{
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

        }


    }

}