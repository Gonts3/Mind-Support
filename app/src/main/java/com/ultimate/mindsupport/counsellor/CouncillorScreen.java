package com.ultimate.mindsupport.counsellor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ultimate.mindsupport.AccountManager;
import com.ultimate.mindsupport.LoginManager;
import com.ultimate.mindsupport.R;
import com.ultimate.mindsupport.SessionManager;
import com.ultimate.mindsupport.chat.LoadUser;
import com.ultimate.mindsupport.client.ClientLoginActivity;
import com.ultimate.mindsupport.client.ClientProfileFragment;
import com.ultimate.mindsupport.client.ClientScreen;

public class CouncillorScreen extends AppCompatActivity {

    BottomNavigationView counsellorBottomNavigation;
    TextView userName;

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
        counsellorBottomNavigation = findViewById(R.id.button_nav2);
        counsellorBottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                getSupportFragmentManager().popBackStack();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
            }else if(id == R.id.nav_chat){
                Intent chatIntent = new Intent(this, LoadUser.class);
                startActivity(chatIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
            }else if(id == R.id.nav_profile){
                //bottomNavigation.setVisibility(View.GONE);
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
    private void initViews(){
        userName = findViewById(R.id.profileName);
    }
    public void counsellorLogOut(View v){
        LoginManager.Logout();
        Intent intent = new Intent(this, CounsellorLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void counsellorDeleteAcc(View v){
        AccountManager.DeleteCounsellor(SessionManager.getCounsellorId(), new AccountManager.AccountCallback() {
            @Override
            public void onSuccess(String message) {
                counsellorLogOut(v);
                Toast.makeText(CouncillorScreen.this, "Account successfully deleted", Toast.LENGTH_SHORT).show();
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
    }

//    public void editCounsellorInfo(View v){
//        Intent intent = new Intent(this, PersonalInformation.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
//    }
}