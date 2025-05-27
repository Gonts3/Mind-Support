package com.ultimate.mindsupport.client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ultimate.mindsupport.CurrentUser;
import com.ultimate.mindsupport.ProblemManager;
import com.ultimate.mindsupport.R;
import com.ultimate.mindsupport.TestingActivity;

public class SelectProblemsActivity extends AppCompatActivity {


    RadioButton problem1,problem2,problem3,problem4,problem5,problem6,problem7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_problems);
        initViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void initViews(){
        problem1 = findViewById(R.id.problem1);
        problem2 = findViewById(R.id.problem2);
        problem3 = findViewById(R.id.problem3);
        problem4 = findViewById(R.id.problem4);
        problem5 = findViewById(R.id.problem5);
        problem6 = findViewById(R.id.problem6);
        problem7 = findViewById(R.id.problem7);
    }

    public void getProblemID(View v){
        RadioGroup group = findViewById(R.id.idGroup);

        int selectedId = group.getCheckedRadioButtonId();

        if (selectedId != -1) {
            RadioButton selected = findViewById(selectedId);

            String selectedText = selected.getText().toString();

            Toast.makeText(this, "You chose: " + selectedText, Toast.LENGTH_SHORT).show();

            int problemId = Integer.parseInt(selected.getTag().toString());
            Log.d("problemId", String.valueOf(problemId));

            Client client = (Client) CurrentUser.get();

            client.setProblemId(String.valueOf(problemId), new ProblemManager.ProblemCallback() {
                @Override
                public void onSuccess(String message) {
                    //go to client screen
                    Intent intent =  new Intent(SelectProblemsActivity.this, ClientScreen.class);
                    startActivity(intent);
                }
                @Override
                public void onFailure(String error) {
                    runOnUiThread(() ->{
                        Toast.makeText(SelectProblemsActivity.this, error, Toast.LENGTH_LONG).show();
                    });
                }
            });

        }
        else {
            Toast.makeText(this, "Please select a problem", Toast.LENGTH_SHORT).show();
        }
    }
}