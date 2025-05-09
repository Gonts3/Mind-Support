package com.ultimate.mindsupport;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClientLoginActivity extends AppCompatActivity {

    CardView signUp,otpCard,signIn;
    EditText txtClientEmail,txtClientUsername,txtClientPassword,txtOtp,textUser,textEmail,textPassword;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.client_activity_login);
        initViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.client), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initially hide both sign-up and sign-in cards
        signUp.setVisibility(View.INVISIBLE);

    }
    private void initViews(){
        signUp = findViewById(R.id.clientSignUpScreen);
        signIn = findViewById(R.id.clientSignInScreen);
        otpCard = findViewById(R.id.CotpCard);
        txtClientEmail = findViewById(R.id.newClientEmail);
        txtClientUsername = findViewById(R.id.newClientUsername);
        txtClientPassword = findViewById(R.id.newClientPassword);
        //textUser = findViewById(R.id.username);
        textEmail = findViewById(R.id.username);
        textPassword = findViewById(R.id.password);
        txtOtp = findViewById(R.id.newOtp);
    }


    public void SignUpCard(View v){
        signUp.setVisibility(View.VISIBLE);
        signUp.setTranslationY(signUp.getHeight()); // Push dwn
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(signUp, "translationY", 0f);
        slideUp.setDuration(400);
        slideUp.start();
    }

    public void backToMain(View v){
        signUp.setVisibility(View.GONE);//CHANGED TO GONE
    }

    public void doClientLogin(View v){
        //String username = txtClientUsername.getText().toString();
        String password = textPassword.getText().toString();
        String email = textEmail.getText().toString();

        LoginManager.LoginUser(email, password, "client", new LoginManager.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                Intent intent = new Intent(ClientLoginActivity.this, CouncillorScreen.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(String error) {
                runOnUiThread(() ->
                        Toast.makeText(ClientLoginActivity.this,error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }
    public void otpCard(View v){
        String username = txtClientUsername.getText().toString();
        String email = txtClientEmail.getText().toString();
        String password = txtClientPassword.getText().toString();
        //txtClientPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);//hides password characters with dots

        ClientRegistrationManager.registerClient(username, password, email, new ClientRegistrationManager.RegistrationCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        otpCard.setVisibility(View.VISIBLE);
                        otpCard.setTranslationY(otpCard.getHeight()); // Push dwn
                        ObjectAnimator slideUp = ObjectAnimator.ofFloat(otpCard, "translationY", 0f);
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
                        Toast.makeText(ClientLoginActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
    public void doSend(View v){
        String email = txtClientEmail.getText().toString();

        EmailVerification.SendOtp(email, new EmailVerification.VerificationCallback() {
            @Override
            public void onSuccess(String message) {
                token = message;
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }
    private void clearCounsellorInputFields() {
        txtClientEmail.setText("");
        txtClientUsername.setText("");
        txtClientPassword.setText("");
        txtOtp.setText("");
    }
    public void doVerify(View v){
        String otp = txtOtp.getText().toString();

        EmailVerification.VerifyOtp(otp, token, new EmailVerification.VerificationCallback() {
            @Override
            public void onSuccess(String message) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ClientLoginActivity.this, message, Toast.LENGTH_LONG).show();
                    otpCard.setVisibility(View.GONE);//CHANGED FROM INVISIBLE TO GONE
                    signUp.setVisibility(View.GONE);

                    // Clear all input fields
                    clearCounsellorInputFields();
                }
            });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ClientLoginActivity.this, "Wrong OTP", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });


    }
}