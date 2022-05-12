package com.example.nftscmers.db;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nftscmers.R;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;

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
     * Create new account for applicants using only email and password
     * @param context a Context object
     * @param email an EditText object containing an email address
     * @param password a String object containing a password
     * @return null
     */
    public void newProfile(Context context, EditText email, String password) {
        if (Utils.invalidData(email)){
            onApplicantUploadFailure.onResult();
            return;
        }



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
