package com.ultimate.mindsupport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ultimate.mindsupport.client.ClientLoginActivity;
import com.ultimate.mindsupport.client.ClientScreen;
import com.ultimate.mindsupport.counsellor.CouncillorScreen;
import com.ultimate.mindsupport.counsellor.CounsellorLoginActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });


        }
        public void councillorActivity (View v){
            Intent intent = new Intent(this, CounsellorLoginActivity.class);
            startActivity(intent);
        }
        public void clientActivity (View v){
            Intent intent = new Intent(this, ClientLoginActivity.class);
            startActivity(intent);
        }
    }
