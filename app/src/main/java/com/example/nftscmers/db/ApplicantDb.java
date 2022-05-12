package com.example.nftscmers.db;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nftscmers.R;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ApplicantDb extends Db{
    private static final String TAG = "ApplicantsDb";
    private static OnApplicantModelSuccess onApplicantModelSuccess;
    private static OnApplicantUploadSuccess onApplicantUploadSuccess;
    private static OnApplicantUploadFailure onApplicantUploadFailure;

    public ApplicantDb() {
        super(ApplicantModel.getCollectionId());
    }

    public ApplicantDb(OnApplicantModelSuccess onApplicantModelSuccess) {
        super(ApplicantModel.getCollectionId());
        this.onApplicantModelSuccess = onApplicantModelSuccess;
    }

    public ApplicantDb(OnApplicantUploadSuccess onApplicantUploadSuccess) {
        super(ApplicantModel.getCollectionId());
        this.onApplicantUploadSuccess = onApplicantUploadSuccess;
    }

    public ApplicantDb(OnApplicantUploadSuccess onApplicantUploadSuccess, OnApplicantUploadFailure onApplicantUploadFailure) {
        super(ApplicantModel.getCollectionId());
        this.onApplicantUploadSuccess = onApplicantUploadSuccess;
        this.onApplicantUploadFailure = onApplicantUploadFailure;
    }

    /**
     * Get ApplicationModel from an email address
     * @param context a Context object
     * @param email an EditText object containing an email address
     * @return null
     */
    public void getApplicantData(Context context, String email) {
        if (!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, R.string.network_missing, Toast.LENGTH_SHORT).show();
            onApplicantModelSuccess.onResult(null);
            return;
        }

        getDocument(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ApplicantModel applicantModel = documentSnapshot.toObject(ApplicantModel.class);
                onApplicantModelSuccess.onResult(applicantModel);
            }
        });
    }

    /**
     * Get ApplicationModel from an email address
     * @param context a Context object
     * @param email an EditText object containing an email address
     * @return null
     */
    public void getApplicantData(Context context, EditText email) {
        if (Utils.invalidData(email)){
            onApplicantModelSuccess.onResult(null);
            return;
        }
        getApplicantData(context, email.getText().toString());
    }

    /**
     * Create new account for applicants using only email with no details yet
     * @param context a Context object
     * @param email an EditText object containing an email address
     * @return null
     */
    public void newProfile(Context context, EditText email) {
        if (Utils.invalidData(email)){
            onApplicantUploadFailure.onResult();
            return;
        }

        getDocument(email.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i(TAG, context.getString(R.string.duplicate_email));
                        onApplicantUploadFailure.onResult();
                    } else {
                        Map<String, Object> account = new HashMap<>();
                        account.put("email", email.getText().toString());

                        getCollection().document(email.getText().toString()).set(account)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        onApplicantUploadSuccess.onResult();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, context.getString(R.string.firestore_error), e);
                                        onApplicantUploadFailure.onResult();
                                    }
                                });
                    }
                } else {
                    Log.w(TAG, context.getString(R.string.firestore_error));
                    onApplicantUploadFailure.onResult();
                }
            }
        });
    }


    public interface OnApplicantModelSuccess{
        void onResult(ApplicantModel applicantModel);
    }

    public interface OnApplicantUploadSuccess{
        void onResult();
    }

    public interface OnApplicantUploadFailure{
        void onResult();
    }

}
