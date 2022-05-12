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
import com.example.nftscmers.applicantactivities.EditProfileActivity;
import com.example.nftscmers.db.AccountDb;
import com.example.nftscmers.db.ApplicantDb;
import com.example.nftscmers.db.EmployerDb;
import com.example.nftscmers.objectmodels.AccountModel;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.utils.Globals;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

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


        if (signUpType == null){
            signUpType = Globals.APPLICANT;
            title.setText(getString(R.string.applicant_login));
        } else if (signUpType.equals(Globals.APPLICANT)) {
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
                signUp.setText(getString(R.string.signing_up));

                // Ensuring that email has not already been registered
                DocumentReference accountDocument = FirebaseFirestore.getInstance().collection(AccountModel.getCollectionId()).document(email.getText().toString());
                accountDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                email.requestFocus();
                                email.setError(getString(R.string.duplicate_email));
                                signUp.setEnabled(true);
                                signUp.setText(getString(R.string.sign_up));
                                Log.i(TAG, getString(R.string.duplicate_email));
                                return;
                            } else {
                                // Creating new profile in applicant
                                if (signUpType.equals(Globals.APPLICANT)) {
                                    new ApplicantDb(new ApplicantDb.OnApplicantUploadSuccess() {
                                        @Override
                                        public void onResult() {
                                            Map<String, Object> account = new HashMap<>();
                                            account.put("email", email.getText().toString());
                                            account.put("password", password.getText().toString());
                                            account.put("profile", new ApplicantDb().getDocument(email.getText().toString()));
                                            accountDocument.set(account).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Utils.toastLog(SignUpActivity.this, TAG, getString(R.string.sign_up_success));
                                                    Intent intent = new Intent(SignUpActivity.this, com.example.nftscmers.applicantactivities.EditProfileActivity.class);
                                                    startActivity(intent);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, getString(R.string.firestore_error), e);
                                                    signUp.setEnabled(true);
                                                    signUp.setText(getString(R.string.sign_up));
                                                }
                                            });
                                        }
                                    }).newProfile(SignUpActivity.this, email);
                                } else if (signUpType.equals(Globals.EMPLOYER)) {
                                    new EmployerDb(new EmployerDb.OnEmployerUploadSuccess() {
                                        @Override
                                        public void onResult() {
                                            Map<String, Object> account = new HashMap<>();
                                            account.put("email", email.getText().toString());
                                            account.put("password", password.getText().toString());
                                            account.put("profile", new EmployerDb().getDocument(email.getText().toString()));
                                            accountDocument.set(account).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Utils.toastLog(SignUpActivity.this, TAG, getString(R.string.sign_up_success));
                                                    Intent intent = new Intent(SignUpActivity.this, com.example.nftscmers.employeractivities.EditProfileActivity.class);
                                                    startActivity(intent);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Utils.fireStoreError(SignUpActivity.this, TAG);
                                                    signUp.setEnabled(true);
                                                    signUp.setText(getString(R.string.sign_in));
                                                }
                                            });
                                        }
                                    }).newProfile(SignUpActivity.this, email);
                                } else {
                                    Utils.unexpectedError(SignUpActivity.this, TAG);
                                    signUp.setEnabled(true);
                                    signUp.setText(getString(R.string.sign_up));
                                }
                            }
                        } else {
                            Utils.fireStoreError(SignUpActivity.this, TAG);
                            signUp.setEnabled(true);
                            signUp.setText(getString(R.string.sign_up));
                        }
                    }
                });
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