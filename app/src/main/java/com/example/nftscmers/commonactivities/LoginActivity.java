package com.example.nftscmers.commonactivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.nftscmers.employeractivities.ViewApplicationActivity;
import com.example.nftscmers.objectmodels.AccountModel;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.utils.Globals;
import com.example.nftscmers.utils.LoggedInUser;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

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

        title = findViewById(R.id.login_title);
        login = findViewById(R.id.login_button);
        signUp = findViewById(R.id.sign_up);
        forgetPassword = findViewById(R.id.forgot_password);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        // Setting up of initial sign up type
        loginType = getIntent().getStringExtra(LoginActivity.TAG);
        Log.i(TAG, "onCreate: " + loginType);

        if (loginType == null){
            loginType = Globals.APPLICANT;
            title.setText(getString(R.string.applicant_login));
        } else if (loginType.equals(Globals.APPLICANT)) {
            title.setText(getString(R.string.applicant_login));
        } else if (loginType.equals(Globals.EMPLOYER)) {
            title.setText(getString(R.string.employer_login));
        } else {
            loginType = Globals.APPLICANT;
            title.setText(getString(R.string.applicant_login));
        }

        // TODO: Make this real
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ensure that entered data are all valid
                if (!Utils.isNetworkAvailable(LoginActivity.this)
                        | Utils.invalidData(email, password)
                        | Utils.invalidEmail(email)) {
                    Log.i(TAG, "onSignUp failed");
                    return;
                }

                // Disable button for Firestore processing
                login.setEnabled(false);
                login.setText(getString(R.string.logging_in));

                String emailAddress = email.getText().toString();

                // Validate that account is correct
                DocumentReference accountDocument = FirebaseFirestore.getInstance().collection(AccountModel.getCollectionId()).document(emailAddress);
                accountDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = document.getData();

                                if (password.getText().toString().equals(data.get(AccountModel.PASSWORD)) & loginType.equals(data.get(AccountModel.ACCOUNTTYPE))) {

                                    if (loginType.equals(Globals.APPLICANT)) {
                                        LoggedInUser.getInstance().setUser(FirebaseFirestore.getInstance().collection(ApplicantModel.getCollectionId()).document(emailAddress), emailAddress, loginType);
                                        Utils.toastLog(LoginActivity.this, TAG, getString(R.string.login_success));

                                        Intent intent = new Intent(LoginActivity.this, ViewApplicationActivity.class);
                                        startActivity(intent);
                                    } else if (loginType.equals(Globals.EMPLOYER)) {
                                        LoggedInUser.getInstance().setUser(FirebaseFirestore.getInstance().collection(EmployerModel.getCollectionId()).document(emailAddress), emailAddress, loginType);
                                        Utils.toastLog(LoginActivity.this, TAG, getString(R.string.login_success));

                                        Intent intent = new Intent(LoginActivity.this, ViewJobActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Utils.unexpectedError(LoginActivity.this, TAG);
                                        login.setEnabled(true);
                                        login.setText(getString(R.string.login_button));
                                    }
                                } else {
                                    // If account information does not match
                                    Utils.toastLog(LoginActivity.this, TAG, getString(R.string.login_error));
                                    login.setEnabled(true);
                                    login.setText(getString(R.string.login_button));
                                }
                            } else {
                                // If account email does not exist
                                Log.i(TAG, getString(R.string.not_signed_up));
                                email.requestFocus();
                                email.setError(getString(R.string.not_signed_up));
                                login.setEnabled(true);
                                login.setText(getString(R.string.login_button));
                            }
                        } else {
                            Utils.fireStoreError(LoginActivity.this, TAG);
                            login.setEnabled(true);
                            login.setText(getString(R.string.login_button));
                        }
                    }
                });
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
