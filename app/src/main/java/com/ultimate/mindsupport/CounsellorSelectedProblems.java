package com.ultimate.mindsupport;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

public class CounsellorSelectedProblems extends AppCompatActivity {

    CheckBox chkProblem1,chkProblem2,chkProblem3,chkProblem4,chkProblem5,chkProblem6,chkProblem7;
    ArrayList<String> counsellorProblemIDs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_counsellor_selected_problems);
        initViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void initViews(){
        chkProblem1 = findViewById(R.id.chkProblem1);
        chkProblem2 = findViewById(R.id.chkProblem2);
        chkProblem3 = findViewById(R.id.chkProblem3);
        chkProblem4 = findViewById(R.id.chkProblem4);
        chkProblem5 = findViewById(R.id.chkProblem5);
        chkProblem6 = findViewById(R.id.chkProblem6);
        chkProblem7 = findViewById(R.id.chkProblem7);
    }
    public void getCounselorProblemID(View v) {
        counsellorProblemIDs.clear();
        if (chkProblem1.isChecked()) {
            counsellorProblemIDs.add("1");
        }
        if (chkProblem2.isChecked()) {
            counsellorProblemIDs.add("2");
        }if (chkProblem3.isChecked()) {
            counsellorProblemIDs.add("3");
        }if (chkProblem4.isChecked()) {
            counsellorProblemIDs.add("4");
        }if (chkProblem5.isChecked()) {
            counsellorProblemIDs.add("5");
        }if (chkProblem6.isChecked()) {
            counsellorProblemIDs.add("6");
        }if(chkProblem7.isChecked()){
            counsellorProblemIDs.add("7");
        }
        Counsellor counsellor = (Counsellor) CurrentUser.getCounsellor();
        if (counsellor != null) {
            counsellor.addProblems(counsellorProblemIDs, new ProblemManager.ProblemCallback() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() ->{
                        Toast.makeText(CounsellorSelectedProblems.this, message, Toast.LENGTH_LONG).show();
                    });
                    Intent intent =  new Intent(CounsellorSelectedProblems.this, TestingActivity.class);
                    startActivity(intent);
                    counsellorProblemIDs.clear();
                }

                @Override
                public void onFailure(String error) {
                    runOnUiThread(() ->{
                        Toast.makeText(CounsellorSelectedProblems.this, error, Toast.LENGTH_LONG).show();
                        Log.d("onFailure: ",counsellorProblemIDs.toString());
                        counsellorProblemIDs.clear();
                    });
                }
            });


            }


        }
    }
