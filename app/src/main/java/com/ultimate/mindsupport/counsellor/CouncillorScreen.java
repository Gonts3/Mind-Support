package com.ultimate.mindsupport.counsellor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ultimate.mindsupport.AccountManager;
import com.ultimate.mindsupport.LoginManager;
import com.ultimate.mindsupport.MainActivity;
import com.ultimate.mindsupport.R;
import com.ultimate.mindsupport.SessionManager;
import com.ultimate.mindsupport.chat.ChatFragment;
import com.ultimate.mindsupport.chat.LoadUser;
import com.ultimate.mindsupport.client.ClientLoginActivity;
import com.ultimate.mindsupport.client.ClientProfileFragment;
import com.ultimate.mindsupport.client.ClientScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CouncillorScreen extends AppCompatActivity {

    BottomNavigationView counsellorBottomNavigation;
    TextView userName,dailyQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_councillor_screen);
        initViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dailyQuote = findViewById(R.id.quotes2);
        List<String> quotes = loadQuotesFromAssets();
        if (!quotes.isEmpty()) {
            Random random = new Random();
            String quote = quotes.get(random.nextInt(quotes.size()));
            dailyQuote.setText(quote);
        }
        counsellorBottomNavigation = findViewById(R.id.button_nav2);
        counsellorBottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                dailyQuote.setVisibility(View.VISIBLE);
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
            }
            if(id == R.id.nav_chat){
                dailyQuote.setVisibility(View.GONE);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container2, new ChatFragment())
                        .setCustomAnimations(
                                android.R.anim.slide_in_left,  // enter
                                android.R.anim.slide_out_right,  // exit
                                android.R.anim.slide_in_left,  // popEnter
                                android.R.anim.slide_out_right  // popExit
                        )
                        .addToBackStack(null)
                        .commit();

                        return true;

            }else if(id == R.id.nav_profile){
                //bottomNavigation.setVisibility(View.GONE);
                dailyQuote.setVisibility(View.GONE);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container2, new CounsellorProfileFragment())
                        .addToBackStack(null)
                        .commit();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
            }
            return false;
        });
    }

    private void showLogoutDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.RoundedAlertDialog);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_logout, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();

        Button cancelBtn = dialogView.findViewById(R.id.btn_cancel);
        Button logoutBtn = dialogView.findViewById(R.id.btn_logout);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        logoutBtn.setOnClickListener(v -> {
            dialog.dismiss();
            LoginManager.Logout();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show();
        });
    }
    private void showDeleteDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.RoundedAlertDialog);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();

        Button cancelBtn = dialogView.findViewById(R.id.btn_cancel);
        Button logoutBtn = dialogView.findViewById(R.id.btn_logout);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        logoutBtn.setOnClickListener(v -> {
            dialog.dismiss();
            AccountManager.DeleteCounsellor(SessionManager.getCounsellorId(), new AccountManager.AccountCallback() {
                @Override
                public void onSuccess(String message) {
                    counsellorLogOut(v);
                    Toast.makeText(CouncillorScreen.this, "Account successfully deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CouncillorScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(String error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CouncillorScreen.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        });
    }
    private void initViews(){
        userName = findViewById(R.id.profileName);
    }
    public void counsellorLogOut(View v){
        showLogoutDialog(this);
    }
    public void counsellorDeleteAcc(View v){
        showDeleteDialog(this);
    }

//    public void editCounsellorInfo(View v){
//        Intent intent = new Intent(this, PersonalInformation.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
//    }
private List<String> loadQuotesFromAssets() {
    List<String> quotes = new ArrayList<>();
    try {
        InputStream is = getResources().openRawResource(R.raw.quotes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                quotes.add(line.trim());
            }
        }
        reader.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return quotes;
}
}