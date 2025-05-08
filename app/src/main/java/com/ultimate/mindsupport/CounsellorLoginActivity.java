package com.ultimate.mindsupport;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CounsellorLoginActivity extends AppCompatActivity {
    CardView signUp,otpCard2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_counsellor_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.councillor), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        signUp = findViewById(R.id.councillorSignUpScreen);
        otpCard2 = findViewById(R.id.CotpCard2);

        // Initially hide both sign-up and sign-in cards
        signUp.setVisibility(View.INVISIBLE);

    }
    public void SignUpCard(View v){
        signUp.setVisibility(View.VISIBLE);
        signUp.setTranslationY(signUp.getHeight()); // Push dwn
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(signUp, "translationY", 0f);
        slideUp.setDuration(400);
        slideUp.start();
    }

    public void backToMain(View v){
        signUp.setVisibility(View.INVISIBLE);
    }
    public void saveDetails(View v /*,boolean saveLogin*/){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to have your login details  ?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            // When the user click yes button then app will take to anada sscreen
            Intent intent = new Intent(this,CouncillorScreen.class);
            startActivity(intent);
        });
        // Set the Negative button with No name Lambda
        // OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
            Intent intent = new Intent(this, CouncillorScreen.class);
            startActivity(intent);
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void otpCard2(View v) {
        otpCard2.setVisibility(View.VISIBLE);
        otpCard2.setTranslationY(otpCard2.getHeight()); // Push dwn
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(otpCard2, "translationY", 0f);
        slideUp.setDuration(400);
        slideUp.start();
    }

    public void doSend2(View v){

    }

    public void doVerify2(View v){


    }

    }