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

public class ClientLoginActivity extends AppCompatActivity {

    CardView signUp,otpCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.client_activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.client), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        signUp = findViewById(R.id.clientSignUpScreen);
        otpCard = findViewById(R.id.CotpCard);
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
            Intent intent = new Intent(this, ClientScreen.class);
            startActivity(intent);
        });
        // Set the Negative button with No name Lambda
        // OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
            Intent intent = new Intent(this, ClientScreen.class);
            startActivity(intent);
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void optCard(View v){
        otpCard.setVisibility(View.VISIBLE);
        otpCard.setTranslationY(otpCard.getHeight()); // Push dwn
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(otpCard, "translationY", 0f);
        slideUp.setDuration(400);
        slideUp.start();
    }

    public void doSend(View v){

    }

    public void doVerify(View v){


    }
}