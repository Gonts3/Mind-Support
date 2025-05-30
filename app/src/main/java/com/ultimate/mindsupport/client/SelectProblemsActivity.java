package com.ultimate.mindsupport.client;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

import java.util.HashMap;

public class SelectProblemsActivity extends AppCompatActivity {


    RadioButton problem1,problem2,problem3,problem4,problem5,problem6,problem7;
    ImageButton details1,details2,details3,details4,details5,details6,details7;

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
        if (CurrentUser.getClient()!=null){
            int id = Integer.parseInt(CurrentUser.getClient().getProblemId());
            if(id==1){
                problem1.setTextColor(Color.GRAY);
                problem1.setEnabled(false);
            }
            else if(id==2){
                problem2.setTextColor(Color.GRAY);
                problem2.setEnabled(false);
            }
            else if(id==3){
                problem3.setTextColor(Color.GRAY);
                problem3.setEnabled(false);
            }
            else if(id==4){
                problem4.setTextColor(Color.GRAY);
                problem4.setEnabled(false);
            }
            else if(id==5){
                problem5.setTextColor(Color.GRAY);
                problem6.setEnabled(false);
            }
            else if(id==6){
                problem6.setTextColor(Color.GRAY);
                problem6.setEnabled(false);
            }
            else{
                problem7.setTextColor(Color.GRAY);
                problem7.setEnabled(false);
            }

        }


    }
    private void showProblemDetailsDialog(String title, String description) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_problem_details, null);
        builder.setView(dialogView);


        TextView textTitle = dialogView.findViewById(R.id.text_title);
        TextView textDescription = dialogView.findViewById(R.id.text_description);
        Button btnClose = dialogView.findViewById(R.id.btn_close);

        textTitle.setText(title);
        textDescription.setText(description);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false); // Optional: prevents dismissal by tapping outside
        dialog.show();

        btnClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void initViews(){
        problem1 = findViewById(R.id.problem1);
        problem2 = findViewById(R.id.problem2);
        problem3 = findViewById(R.id.problem3);
        problem4 = findViewById(R.id.problem4);
        problem5 = findViewById(R.id.problem5);
        problem6 = findViewById(R.id.problem6);
        problem7 = findViewById(R.id.problem7);

        details1 = findViewById(R.id.details1);
        details2 = findViewById(R.id.details2);
        details3 = findViewById(R.id.details3);
        details4 = findViewById(R.id.details4);
        details5 = findViewById(R.id.details5);
        details6 = findViewById(R.id.details6);
        details7 = findViewById(R.id.details7);

        details1.setOnClickListener(v -> showProblemDetailsDialog(
                problem1.getText().toString(),
                "Anxiety\n" +
                        "Depression\n" +
                        "Stress\n" +
                        "Bipolar disorder\n" +
                        "Obsessive-compulsive disorder (OCD)\n" +
                        "Post-traumatic stress disorder (PTSD)"
        ));
        details2.setOnClickListener(v -> showProblemDetailsDialog(
                problem2.getText().toString(),
                "Low self-esteem\n" +
                        "Anger management\n" +
                        "Grief and loss\n" +
                        "Identity issues\n" +
                        "Loneliness\n" +
                        "Trauma recovery"
        ));
        details3.setOnClickListener(v -> showProblemDetailsDialog(
                problem3.getText().toString(),
                "Romantic relationships\n" +
                        "Family conflict\n" +
                        "Divorce/separation\n" +
                        "Parenting challenges\n" +
                        "Abuse (emotional, physical, sexual)"
        ));
        details4.setOnClickListener(v -> showProblemDetailsDialog(
                problem4.getText().toString(),
                "Exam stress\n" +
                        "Career guidance\n" +
                        "Study motivation\n" +
                        "Workplace issues\n" +
                        "Burnout"
        ));
        details5.setOnClickListener(v -> showProblemDetailsDialog(
                problem5.getText().toString(),
                "Alcohol addiction\n" +
                        "Drug addiction\n" +
                        "Gambling addiction\n" +
                        "Pornography addiction\n" +
                        "Gaming addiction"
        ));
        details6.setOnClickListener(v -> showProblemDetailsDialog(
                problem6.getText().toString(),
                "LGBTQ+ support\n" +
                        "Gender identity\n" +
                        "Coming out\n" +
                        "Sexual health"
        ));
        details7.setOnClickListener(v -> showProblemDetailsDialog(
                problem7.getText().toString(),
                "Goal setting\n" +
                        "Decision making\n" +
                        "Motivation\n" +
                        "Time management\n" +
                        "Spiritual or existential questions"
        ));

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
                    //go to client screen after assigning counsellor
                    client.assignCounsellor(new ProblemManager.ProblemCallback() {
                        @Override
                        public void onSuccess(String message) {
                            runOnUiThread(() ->
                                    Toast.makeText(SelectProblemsActivity.this,message, Toast.LENGTH_LONG).show()
                            );
                            Intent intent =  new Intent(SelectProblemsActivity.this, ClientScreen.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onFailure(String error) {

                        }
                    });

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