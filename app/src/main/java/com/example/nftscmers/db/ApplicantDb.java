package com.example.nftscmers.db;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.nftscmers.R;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.utils.FirebaseStorageReference;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class ApplicantDb extends Db{
    private static final String TAG = "ApplicantsDb";
    private static OnApplicantModel onApplicantModel;
    private static OnApplicantUploadSuccess onApplicantUploadSuccess;
    private static OnApplicantUploadFailure onApplicantUploadFailure;
    private Context context;

    public ApplicantDb() {
        super(ApplicantModel.getCollectionId());
    }

    public ApplicantDb(Context context, OnApplicantModel onApplicantModel) {
        super(ApplicantModel.getCollectionId());
        this.context = context;
        this.onApplicantModel = onApplicantModel;
    }

    public ApplicantDb(Context context, OnApplicantUploadSuccess onApplicantUploadSuccess) {
        super(ApplicantModel.getCollectionId());
        this.context = context;
        this.onApplicantUploadSuccess = onApplicantUploadSuccess;
    }

    public ApplicantDb(Context context, OnApplicantUploadSuccess onApplicantUploadSuccess, OnApplicantUploadFailure onApplicantUploadFailure) {
        super(ApplicantModel.getCollectionId());
        this.context = context;
        this.onApplicantUploadSuccess = onApplicantUploadSuccess;
        this.onApplicantUploadFailure = onApplicantUploadFailure;
    }

    /**
     * Get ApplicationModel from an email address
     * @param email an String object containing an email address
     */
    public void getApplicantModel(String email) {
        if (!Utils.isNetworkAvailable(context)) {
            onApplicantModel.onResult(null);
            return;
        }

        getApplicantModel(getDocument(email));
    }

    /**
     * Get ApplicationModel from user document reference
     * @param applicant a DocumentReference of an applicant
     */
    public void getApplicantModel(DocumentReference applicant) {
        if (!Utils.isNetworkAvailable(context)) {
            onApplicantModel.onResult(null);
            return;
        }

        applicant.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ApplicantModel applicantModel = documentSnapshot.toObject(ApplicantModel.class);
                Log.d(TAG, "onSuccess " + applicantModel.getEmail());
                onApplicantModel.onResult(applicantModel);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure");
            }
        });
    }



    /**
     * Get ApplicationModel from an email address
     * @param email an EditText object containing an email address
     */
    public void getApplicantModel(EditText email) {
        if (Utils.invalidData(email)){
            onApplicantModel.onResult(null);
            return;
        }
        getApplicantModel(email.getText().toString());
    }

    /**
     * Create new account for applicants using only email with no details yet
     * @param email an EditText object containing an email address
     */
    public void newProfile(EditText email) {
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
                        account.put(ApplicantModel.EMAIL, email.getText().toString());

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

    /**
     * Push account details to FireStore
     * @param applicantModel an ApplicantModel object containing the details of the applicant profile
     */
    public void updateProfile(ApplicantModel applicantModel) {
        if (!Utils.isNetworkAvailable(context)) {
            onApplicantModel.onResult(null);
            return;
        }

        getDocument(applicantModel.getEmail()).set(applicantModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                onApplicantUploadSuccess.onResult();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onApplicantUploadFailure.onResult();
            }
        });
    }

    /**
     * Update applications field in FireStore
     * @param application a DocumentReference object indicating the application
     */
    public void updateApplications(DocumentReference application, DocumentReference applicant) {
        if (!Utils.isNetworkAvailable(context)) {
            onApplicantUploadFailure.onResult();
            return;
        }

        applicant.update(ApplicantModel.APPLICATIONS, FieldValue.arrayUnion(application)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                onApplicantUploadSuccess.onResult();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onApplicantUploadFailure.onResult();
            }
        });
    }

    /**
     * Push new profile picture to Firebase Storage
     * @param imageView an ImageView object
     * @param email a String object indicating the user's email
     */
    public void updateProfilePicture(ImageView imageView, String email) {
        new FirebaseStorageReference() {
            @Override
            public void uploadSuccess(String url) {
                onApplicantUploadSuccess.onResult(url);
            }
            @Override
            public void uploadFailed() {
                onApplicantUploadFailure.onResult();
            }
        }.uploadImage(imageView, ApplicantModel.getCollectionId() + "/" + email);
    }

    /**
     * Push new profile picture to Firebase Storage
     * @param uri a Uri object containing the file location
     * @param email a String object indicating the user's email
     */
    public void updateProfilePicture(Uri uri, String email) {
        new FirebaseStorageReference() {
            @Override
            public void uploadSuccess(String url) {
                onApplicantUploadSuccess.onResult(url);
            }
            @Override
            public void uploadFailed() {
                onApplicantUploadFailure.onResult();
            }
        }.uploadImage(uri, ApplicantModel.getCollectionId() + "/" + email);
    }

    public interface OnApplicantModel {
        void onResult(ApplicantModel applicantModel);
    }

    public static abstract class OnApplicantUploadSuccess{
        protected void onResult() {};

        protected void onResult(String string) {};
    }

    public interface OnApplicantUploadFailure{
        void onResult();
    }

}
