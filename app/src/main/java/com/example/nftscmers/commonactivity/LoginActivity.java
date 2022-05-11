package com.example.nftscmers.commonactivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.utils.Utils;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        Button login = (Button) findViewById(R.id.loginbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView email = (TextView) findViewById(R.id.username);
                String emailString = email.getText().toString();
                TextView password = (TextView) findViewById(R.id.password);
                String passwordString = password.getText().toString();

                // Valid account is by right a method
                // validAccount(emailString, passwordString);
            }
        });
    }
}
