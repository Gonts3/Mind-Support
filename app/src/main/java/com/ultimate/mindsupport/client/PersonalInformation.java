package com.ultimate.mindsupport.client;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ultimate.mindsupport.AccountManager;
import com.ultimate.mindsupport.CurrentUser;
import com.ultimate.mindsupport.R;

public class PersonalInformation extends AppCompatActivity {

    private EditText userName;
    private CardView changeNamesCard;
    private Button names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_information);
        initViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        changeNamesCard.setVisibility(View.GONE);
    }
    private void initViews(){
        changeNamesCard = findViewById(R.id.changeNamesCard);
        userName = findViewById(R.id.newClientUserName);
        names = findViewById(R.id.clientUserName);
    }
    public void changeNames(View v){
        changeNamesCard.setVisibility(View.VISIBLE);
        names.setVisibility(View.GONE);
    }
    public void backToProfile(View v){
        String username = userName.getText().toString();
        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Client client = (Client) CurrentUser.getClient();
        if (client == null) {
            Toast.makeText(this, "No client found", Toast.LENGTH_SHORT).show();
            return;
        }
        client.setUsername(username, new AccountManager.AccountCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PersonalInformation.this, message, Toast.LENGTH_SHORT).show();
                        changeNamesCard.setVisibility(View.GONE);
                        names.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PersonalInformation.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}