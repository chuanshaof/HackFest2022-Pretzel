package com.example.nftscmers.commonactivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nftscmers.R;
import com.example.nftscmers.db.AccountDb;
import com.example.nftscmers.db.ApplicantDb;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.utils.Globals;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";

    private TextView title;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button signUp;
    private TextView signIn;

    String signUpType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Intialization
        title = findViewById(R.id.sign_up_title);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
        confirmPassword = findViewById(R.id.sign_up_confirm_password);
        signUp = findViewById(R.id.sign_up_button);
        signIn = findViewById(R.id.sign_in);

        // Setting up of initial sign up type
        signUpType = getIntent().getStringExtra(LoginActivity.TAG);
        Log.i(TAG, "onCreate: " + signUpType);

        if (signUpType.equals(Globals.APPLICANT)) {
            title.setText(getString(R.string.applicant_sign_up));
        } else if (signUpType.equals(Globals.EMPLOYER)) {
            title.setText(getString(R.string.employer_sign_up));
        } else {
            signUpType = Globals.APPLICANT;
            title.setText(getString(R.string.applicant_sign_up));
        }

        // Sign Up Button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredEmail = email.getText().toString();
                String enteredPassword = password.getText().toString();
                String confirmPasswordEntry = confirmPassword.getText().toString();

                // Ensure that entered data are all valid
                if (!Utils.isNetworkAvailable(SignUpActivity.this)
                        | Utils.invalidData(email, password, confirmPassword)
                        | Utils.invalidEmail(email)) {
                    Log.i(TAG, "onSignUp failed");
                    return;
                }

                if (!enteredPassword.equals(confirmPasswordEntry)) {
                    confirmPassword.requestFocus();
                    confirmPassword.setError(getString(R.string.sign_up_confirm_fail));
                    Log.i(TAG, getString(R.string.sign_up_confirm_fail));
                    return;
                }

                // Disable button for Firestore processing
                signUp.setEnabled(false);
                signUp.setText(getString(R.string.signing_in));

                // Check if email is existing
                new AccountDb(new AccountDb.onAccountSuccess() {
                    @Override
                    public void onResult() {
                        // Handle different sign up types
                        if (signUpType.equals(Globals.APPLICANT)) {

                        } else if (signUpType.equals(Globals.EMPLOYER)) {
                            // TODO: Handle employer's side
                        } else {
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            Utils.unexpectedError(SignUpActivity.this, TAG);
                            startActivity(intent);
                        }
                    }
                }, new AccountDb.onAccountFailure() {
                    @Override
                    public void onResult() {
                        email.requestFocus();
                        email.setError(getString(R.string.duplicate_email));
                        signUp.setEnabled(true);
                        signUp.setText(getString(R.string.sign_up));
                    }
                }).createAccount(SignUpActivity.this, email, password);
            }
        });

        // Sign In Button
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.putExtra(LoginActivity.TAG, signUpType);
                startActivity(intent);
            }
        });


    }
}