package com.ultimate.mindsupport;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ultimate.mindsupport.client.Client;
import com.ultimate.mindsupport.client.ClientLoginActivity;
import com.ultimate.mindsupport.counsellor.CounsellorLoginActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class TestingActivity extends AppCompatActivity {
    TextView txtTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_testing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtTest = findViewById(R.id.txtTests);
        Client client = (Client) CurrentUser.getClient();
        if (CurrentUser.isClient()) {
            txtTest.setText(client.getUsername() + " dealing with " + client.getProblemId());


        } else {
            txtTest.setText("nothin");
        }




    }
    public void AddProblems(View v){
        txtTest = findViewById(R.id.txtTests);

        if(CurrentUser.getClient()!=null) {


        }else{
            Counsellor counsellor = (Counsellor) CurrentUser.getCounsellor();
            if (counsellor != null) {
                txtTest.setText(counsellor.getFname() + " " + counsellor.getLname());
            } else {
                txtTest.setText("nothigng");
            }


        }


    }
    public void DoProblems(View v) {
        txtTest = findViewById(R.id.txtTests);
        Counsellor counsellor = (Counsellor) CurrentUser.getCounsellor();

        if (counsellor != null) {
            counsellor.setNames("Jack","Smalllephant", new AccountManager.AccountCallback() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() ->{
                        Toast.makeText(TestingActivity.this, message, Toast.LENGTH_LONG).show();
                        txtTest.setText(counsellor.getFname() + " "+ counsellor.getLname());
                    });
                }

                @Override
                public void onFailure(String error) {
                    runOnUiThread(() ->{
                        Toast.makeText(TestingActivity.this, error, Toast.LENGTH_LONG).show();
                        Log.d("onFailure: ","here");
                    });
                }
            });
        }
    }
}