package com.example.nftscmers.commonactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.utils.Globals;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    private String loginType;

    private TextView title;
    private TextView signUp;
    private Button login;
    private TextView forgetPassword;

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginType = Globals.APPLICANT;


        title = findViewById(R.id.login_title);
        login = findViewById(R.id.login_button);
        signUp = findViewById(R.id.sign_up);
        forgetPassword = findViewById(R.id.forgot_password);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        // TODO: Make this real
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

                // Valid account is by right a method
                // validAccount(emailString, passwordString);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.putExtra(LoginActivity.TAG, loginType);
                startActivity(intent);
            }
        });

        // TODO: Make this real, but currently KIV
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Sorry, function is work in progress", Toast.LENGTH_SHORT);
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                intent.putExtra(LoginActivity.TAG, loginType);
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
            loginType = Globals.EMPLOYER;
        } else if (item.getItemId() == R.id.applicant_login) {
            title.setText("Applicant Login");
            loginType = Globals.APPLICANT;
        }
        return true;
    }
}
