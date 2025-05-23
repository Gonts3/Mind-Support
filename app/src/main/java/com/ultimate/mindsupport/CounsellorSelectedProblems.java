package com.ultimate.mindsupport;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;

public class CounsellorSelectedProblems extends AppCompatActivity {

    CheckBox chkProblem1,chkProblem2,chkProblem3,chkProblem4,chkProblem5,chkProblem6,chkProblem7;
    ArrayList<String> counsellorProblemIDs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_counsellor_selected_problems);
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
    public void getCounselorProblemID(View v){
        if(chkProblem1.isSelected()){
            counsellorProblemIDs.add("1");
        }
        else if (chkProblem2.isSelected()){
            counsellorProblemIDs.add("2");
        }
        else if (chkProblem3.isSelected()){
            counsellorProblemIDs.add("3");
        }
        else if (chkProblem4.isSelected()){
            counsellorProblemIDs.add("4");
        }
        else if (chkProblem5.isSelected()){
            counsellorProblemIDs.add("5");
        }
        else if (chkProblem6.isSelected()){
            counsellorProblemIDs.add("6");
        }
        else{
            counsellorProblemIDs.add("7");
        }
    }
}