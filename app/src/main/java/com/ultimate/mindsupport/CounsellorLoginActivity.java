package com.ultimate.mindsupport;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CounsellorLoginActivity extends AppCompatActivity {
    CardView signUp,otpCard2,counsSignIn;
    EditText txtNewCounsFname,txtNewCounsLname,txtNewCounsRegNum,txtNewCounsEmail,txtNewClientPassword,counsOTP,textEmail,textPassword;
    String CounsToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_counsellor_login);
        initViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.councillor), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initially hide both sign-up and sign-in cards
        signUp.setVisibility(View.INVISIBLE);

    }
    private void initViews(){
        signUp = findViewById(R.id.councillorSignUpScreen);
        otpCard2 = findViewById(R.id.CotpCard2);
        counsSignIn = findViewById(R.id.councillorSignInCard);
        txtNewCounsFname = findViewById(R.id.newCounsFname);
        txtNewCounsLname = findViewById(R.id.newCounsLname);
        txtNewCounsRegNum = findViewById(R.id.newCounsRegNum);
        txtNewCounsEmail = findViewById(R.id.newCounsEmail);
        txtNewClientPassword = findViewById(R.id.newCounsPassword);
        counsOTP = findViewById(R.id.txtOtp2);
        textEmail = findViewById(R.id.username2);
        textPassword = findViewById(R.id.password2);
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
    private void showSaveLoginDialog(Class<?> targetActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to have your login details  ?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            // When the user click yes button then app will take to anada sscreen
            Intent intent = new Intent(this, targetActivity);
            startActivity(intent);
        });
        // Set the Negative button with No name Lambda
        // OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
            Intent intent = new Intent(this, targetActivity);
            startActivity(intent);
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void doCounselorLogin(View v ){
        String password = textPassword.getText().toString();
        String email = textEmail.getText().toString();

        LoginManager.LoginUser(email, password, "counselor", new LoginManager.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                Intent intent = new Intent(CounsellorLoginActivity.this, CouncillorScreen.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() ->
                        Toast.makeText(CounsellorLoginActivity.this,error, Toast.LENGTH_LONG).show()
                );
            }
        });

    }

    public void otpCard2(View v) {
        String fName = txtNewCounsFname.getText().toString();
        String lName = txtNewCounsLname.getText().toString();
        String regNo = txtNewCounsRegNum.getText().toString();
        String password = txtNewClientPassword.getText().toString();
        String email = txtNewCounsEmail.getText().toString();

        CounsellorRegistrationManager.registerCounsellor(fName, lName, regNo, password, email, new CounsellorRegistrationManager.RegistrationCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        otpCard2.setVisibility(View.VISIBLE);

                        signUp.setVisibility(View.INVISIBLE);
                        otpCard2.setTranslationY(otpCard2.getHeight()); // Push dwn
                        ObjectAnimator slideUp = ObjectAnimator.ofFloat(otpCard2, "translationY", 0f);
                        slideUp.setDuration(400);
                        slideUp.start();

                    }
                });

            }
            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CounsellorLoginActivity.this,error, Toast.LENGTH_LONG);
                    }
                });

            }
        });
    }

    public void doSend2(View v){
        String email = txtNewCounsEmail.getText().toString();

        EmailVerification.SendOtp(email, new EmailVerification.VerificationCallback() {
            @Override
            public void onSuccess(String message) {
                CounsToken = message;
            }

            @Override
            public void onFailure(String error) {

            }
        });


    }

    private void clearCounsellorInputFields() {
        txtNewCounsFname.setText("");
        txtNewCounsLname.setText("");
        txtNewCounsRegNum.setText("");
        txtNewCounsEmail.setText("");
        txtNewClientPassword.setText("");
        counsOTP.setText("");
    }
    public void doVerify2(View v){
        String otp = counsOTP.getText().toString();
        EmailVerification.VerifyOtp(otp, CounsToken, new EmailVerification.VerificationCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CounsellorLoginActivity.this, message, Toast.LENGTH_LONG).show();
                        signUp.setVisibility(View.GONE);
                        otpCard2.setVisibility(View.GONE);
                        //clear all input fields
                        clearCounsellorInputFields();

                    }
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CounsellorLoginActivity.this, "Wrong OTP", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


    }

    }