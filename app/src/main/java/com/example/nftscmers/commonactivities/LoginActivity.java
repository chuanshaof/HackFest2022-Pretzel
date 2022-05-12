package com.example.nftscmers.commonactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private final String[] loginTypes = new String[] {"Applicant", "Employer"};
    private String loginType;

    private TextView title;
    private TextView signUp;
    private Button login;
    private TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginType = loginTypes[0];

        title = findViewById(R.id.login_title);
        login = findViewById(R.id.loginbtn);
        signUp = findViewById(R.id.sign_up);
        forgetPassword = findViewById(R.id.forgot_password);

        // TODO: Make this real
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

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.putExtra(SignUpActivity.TAG, loginType);
                startActivity(intent);
            }
        });

        // TODO: Make this real, but currently KIV
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Sorry, function is work in progress", Toast.LENGTH_SHORT);
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                intent.putExtra(SignUpActivity.TAG, loginType);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.register) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.employer_login) {
            title.setText("Employer Login");
            loginType = loginTypes[1];
        } else if (item.getItemId() == R.id.applicant_login) {
            title.setText("Applicant Login");
            loginType = loginTypes[0];
        }
        return true;
    }
}
