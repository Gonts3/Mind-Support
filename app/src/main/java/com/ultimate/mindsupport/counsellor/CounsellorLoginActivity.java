package com.ultimate.mindsupport.counsellor;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.ultimate.mindsupport.CounsellorSelectedProblems;
import com.ultimate.mindsupport.EmailVerification;
import com.ultimate.mindsupport.GetUser;
import com.ultimate.mindsupport.LoginManager;
import com.ultimate.mindsupport.R;
import com.ultimate.mindsupport.SessionManager;
import com.ultimate.mindsupport.TestingActivity;
import com.ultimate.mindsupport.chat.LoadUser;

import java.io.IOException;
import java.security.GeneralSecurityException;

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
        try {
            SessionManager.init(this);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        if (SessionManager.isLoggedIn()) {
//            SessionManager.loadCounsellorSession(new GetUser.GetUserCallback() {
//                @Override
//                public void onSuccess(String message) {
//                    Intent intent = new Intent(CounsellorLoginActivity.this, LoadUser.class);
//                    startActivity(intent);
//                }
//
//                @Override
//                public void onFailure(String error) {
//
//                }
//            });
//
//       }
         //Initially hide both sign-up and sign-in cards
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

    public void doCounselorLogin(View v ){
        String password = textPassword.getText().toString();
        String email = textEmail.getText().toString().trim();

        LoginManager.LoginUser(email, password, "counsellor", new LoginManager.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() ->
                        Toast.makeText(CounsellorLoginActivity.this,message, Toast.LENGTH_LONG).show()
                );
                SessionManager.loadCounsellorSession(new GetUser.GetUserCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Intent intent = new Intent(CounsellorLoginActivity.this, LoadUser.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });

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
                        Toast.makeText(CounsellorLoginActivity.this,message, Toast.LENGTH_LONG);
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
        Button sendOtp = (Button)v;  // Cast the View to a Button
        // Disable the button
        sendOtp.setEnabled(false);
        // Start 30-second countdown (adjust time if needed)
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                sendOtp.setText("Wait " + millisUntilFinished / 1000 + "s");
            }
            public void onFinish() {
                sendOtp.setText("Send OTP");
                sendOtp.setEnabled(true); // Re-enable the button
            }
        }.start();
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