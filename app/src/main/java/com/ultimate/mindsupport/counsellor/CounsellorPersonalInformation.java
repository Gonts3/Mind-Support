package com.ultimate.mindsupport.counsellor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ultimate.mindsupport.AccountManager;
import com.ultimate.mindsupport.CurrentUser;
import com.ultimate.mindsupport.R;
import com.ultimate.mindsupport.client.Client;
import com.ultimate.mindsupport.client.ClientScreen;
import com.ultimate.mindsupport.client.PersonalInformation;

public class CounsellorPersonalInformation extends AppCompatActivity {

    private EditText newCounsellorFName,newClientLName;
    private CardView changeCounsNamesCard;
    private Button names,problems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_counsellor_personal_infomation);
        initViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void initViews(){
        changeCounsNamesCard = findViewById(R.id.changeCounsNamesCard);
        newCounsellorFName = findViewById(R.id.newCounsellorFName);
        newClientLName = findViewById(R.id.newClientLName);
        names = findViewById(R.id.counsNames);
        problems = findViewById(R.id.counsellorProblems);
    }
    public void changeCounsNames(View v){
        changeCounsNamesCard.setVisibility(View.VISIBLE);
        names.setVisibility(View.GONE);
        problems.setVisibility(View.GONE);
    }

    public void backToCounsProfile(View v){
        String Fname = newCounsellorFName.getText().toString();
        String Lname = newClientLName.getText().toString();
        if (Fname.isEmpty() & Lname.isEmpty() ) {
            Toast.makeText(this, "One field required!", Toast.LENGTH_SHORT).show();
            return;
        }
        Counsellor counsellor = (Counsellor) CurrentUser.getCounsellor();
        if (counsellor == null) {
            Toast.makeText(this, "No client found", Toast.LENGTH_SHORT).show();
            return;
        }
        counsellor.setNames(Fname, Lname, new AccountManager.AccountCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CounsellorPersonalInformation.this, "Names changed", Toast.LENGTH_SHORT).show();
                        changeCounsNamesCard.setVisibility(View.GONE);
                        names.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CounsellorPersonalInformation.this, "Username not changed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void chooseProblemsCouns(View v){
        Intent intent = new Intent(this, CounsellorSelectedProblems.class);
        startActivity(intent);
    }

    public void backToProfileC(View v){
        finish();
    }

}