package com.ultimate.mindsupport.counsellor;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
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
import com.ultimate.mindsupport.EmailVerification;
import com.ultimate.mindsupport.GetUser;
import com.ultimate.mindsupport.LoginManager;
import com.ultimate.mindsupport.ProblemManager;
import com.ultimate.mindsupport.R;
import com.ultimate.mindsupport.SessionManager;
import com.ultimate.mindsupport.client.Client;
import com.ultimate.mindsupport.client.ClientLoginActivity;
import com.ultimate.mindsupport.client.ClientScreen;
import com.ultimate.mindsupport.client.SelectProblemsActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class CounsellorLoginActivity extends AppCompatActivity {
    private CardView signUp,otpCard2,counsSignIn,counsellorResetPassword;
    private EditText txtNewCounsFname,txtNewCounsLname,txtNewCounsRegNum,txtNewCounsEmail,txtNewClientPassword,counsOTP,textEmail,textPassword,reEmail,rePassword;
    private String CounsToken;
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
        reEmail = findViewById(R.id.counsEmail);
        rePassword = findViewById(R.id.counsNewPassword);
        counsellorResetPassword = findViewById(R.id.counsellorResetPassword);
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

    private void showSaveDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.RoundedAlertDialog);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_save, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();

        Button cancelBtn = dialogView.findViewById(R.id.btn_cancel);
        Button logoutBtn = dialogView.findViewById(R.id.btn_logout);

        logoutBtn.setOnClickListener(v -> {
                Counsellor counsellor = CurrentUser.getCounsellor();
                counsellor.getProblems(new ProblemManager.ProblemListCallback() {
                    @Override
                    public void onSuccess(List<String> problems) {

                        Intent intent = new Intent(CounsellorLoginActivity.this, CouncillorScreen.class);
                        startActivity(intent);


                    }

                    @Override
                    public void onFailure(String error) {
                        Intent intent = new Intent(CounsellorLoginActivity.this, CounsellorSelectedProblems.class);
                        startActivity(intent);


                    }

                });
        });
        cancelBtn.setOnClickListener(v -> {
            CurrentUser.setLoggedIn(false);
            Counsellor counsellor = CurrentUser.getCounsellor();
            counsellor.getProblems(new ProblemManager.ProblemListCallback() {
                @Override
                public void onSuccess(List<String> problems) {

                    Intent intent = new Intent(CounsellorLoginActivity.this, CouncillorScreen.class);
                    startActivity(intent);


                }

                @Override
                public void onFailure(String error) {
                    Intent intent = new Intent(CounsellorLoginActivity.this, CounsellorSelectedProblems.class);
                    startActivity(intent);


                }

            });


        });


    }

    public void doCounselorLogin(View v ){
        String password = textPassword.getText().toString();
        String email = textEmail.getText().toString().trim();


        LoginManager.LoginUser(email, password, "counsellor", new LoginManager.LoginCallback() {
            @Override
            public void onSuccess(String message) {

                SessionManager.loadCounsellorSession(new GetUser.GetUserCallback() {
                    @Override
                    public void onSuccess(String message) {
                        runOnUiThread(() ->
                                Toast.makeText(CounsellorLoginActivity.this,message, Toast.LENGTH_LONG).show()
                        );
                        runOnUiThread(() ->
                                showSaveDialog(CounsellorLoginActivity.this)
                        );
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

    public void forgotCounsellorPassword(View v){
        counsellorResetPassword.setVisibility(View.VISIBLE);
        counsellorResetPassword.setTranslationY(counsellorResetPassword.getHeight()); // Push dwn
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(counsellorResetPassword, "translationY", 0f);
        slideUp.setDuration(400);
        slideUp.start();
        counsSignIn.setVisibility(View.GONE);
    }
    public void backToLoginCounsellor(View v){
        String email = reEmail.getText().toString();
        String password = rePassword.getText().toString();
        AccountManager.ResetPassword(email, password, "counsellor", new LoginManager.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CounsellorLoginActivity.this,"Password changed",Toast.LENGTH_LONG).show();
                        counsellorResetPassword.setVisibility(View.GONE);
                        counsSignIn.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CounsellorLoginActivity.this,error,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    }