package com.ultimate.mindsupport.client;

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
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ultimate.mindsupport.AccountManager;
import com.ultimate.mindsupport.CurrentUser;
import com.ultimate.mindsupport.LoginManager;
import com.ultimate.mindsupport.R;
import com.ultimate.mindsupport.SessionManager;
import com.ultimate.mindsupport.chat.LoadUser;

public class ClientScreen extends AppCompatActivity {

    BottomNavigationView clientBottomNavigation;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_screen);
        initViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        clientBottomNavigation = findViewById(R.id.button_nav);
        clientBottomNavigation.setOnItemSelectedListener(item -> {
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
                        .replace(R.id.fragment_container, new ClientProfileFragment())
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
//        userName.setText(CurrentUser.getClient().getUsername());
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    public void clientLogOut(View v){
        LoginManager.Logout();
        Intent intent = new Intent(this, ClientLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void clientDeleteAcc(View v){
        AccountManager.DeleteClient(SessionManager.getClientId(), new AccountManager.AccountCallback() {
            @Override
            public void onSuccess(String message) {
                clientLogOut(v);
                Toast.makeText(ClientScreen.this, "Account successfully deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ClientScreen.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void chooseProblems(View v){
        Intent intent = new Intent(this, SelectProblemsActivity.class);
        startActivity(intent);
    }
}

