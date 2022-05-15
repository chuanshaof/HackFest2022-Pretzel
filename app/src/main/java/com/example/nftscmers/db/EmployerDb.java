package com.example.nftscmers.db;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.nftscmers.R;
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

public class EmployerDb extends Db {
    private static final String TAG = "EmployersDb";
    private static OnEmployerModel onEmployerModel;
    private static OnEmployerUploadSuccess onEmployerUploadSuccess;
    private static OnEmployerUploadFailure onEmployerUploadFailure;
    private Context context;

    public EmployerDb() {
        super(EmployerModel.getCollectionId());
    }

    public EmployerDb(Context context, OnEmployerModel onEmployerModelSuccess) {
        super(EmployerModel.getCollectionId());
        this.onEmployerModel = onEmployerModelSuccess;
        this.context = context;
    }

    public EmployerDb(Context context, EmployerDb.OnEmployerUploadSuccess onEmployerUploadSuccess) {
        super(EmployerModel.getCollectionId());
        this.onEmployerUploadSuccess = onEmployerUploadSuccess;
        this.context = context;
    }

    public EmployerDb(Context context, EmployerDb.OnEmployerUploadSuccess onEmployerUploadSuccess, EmployerDb.OnEmployerUploadFailure onEmployerUploadFailure) {
        super(EmployerModel.getCollectionId());
        this.onEmployerUploadSuccess = onEmployerUploadSuccess;
        this.onEmployerUploadFailure = onEmployerUploadFailure;
        this.context = context;
    }

    /**
     * Get EmployerModel from an email address
     * @param email an EditText object containing an email address
     */
    public void getEmployerModel(String email) {
        if (!Utils.isNetworkAvailable(context)) {
            onEmployerModel.onResult(null);
            return;
        }

        getDocument(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                EmployerModel employerModel = documentSnapshot.toObject(EmployerModel.class);
                onEmployerModel.onResult(employerModel);
            }
        });
    }

    /**
     * Get EmployerModel from user document reference
     * * @param employer a DocumentReference of an employer
     */
    public void getEmployerModel(DocumentReference employer) {
        if (!Utils.isNetworkAvailable(context)) {
            onEmployerModel.onResult(null);
            return;
        }

        employer.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                EmployerModel employerModel = documentSnapshot.toObject(EmployerModel.class);
                onEmployerModel.onResult(employerModel);
            }
        });
    }

    /**
     * Get EmployerModel from an email address
     * @param email an EditText object containing an email address
     */
    public void getEmployerModel(EditText email) {
        if (Utils.invalidData(email)){
            onEmployerModel.onResult(null);
            return;
        }
        getEmployerModel(email.getText().toString());
    }

    /**
     * Create new account for Employers using only email with no details yet
     * @param email an EditText object containing an email address
     */
    public void newProfile(EditText email) {
        if (Utils.invalidData(email)){
            onEmployerUploadFailure.onResult();
            return;
        }

        getDocument(email.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i(TAG, context.getString(R.string.duplicate_email));
                        onEmployerUploadFailure.onResult();
                    } else {
                        Map<String, Object> account = new HashMap<>();
                        account.put(EmployerModel.EMAIL, email.getText().toString());

                        getCollection().document(email.getText().toString()).set(account)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        onEmployerUploadSuccess.onResult();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, context.getString(R.string.firestore_error), e);
                                        onEmployerUploadFailure.onResult();
                                    }
                                });
                    }
                } else {
                    Log.w(TAG, context.getString(R.string.firestore_error));
                    onEmployerUploadFailure.onResult();
                }
            }
        });
    }

    /**
     * Push account details to FireStore
     * @param employerModel an EmployerModel object containing the details of the employer profile
     */
    public void updateProfile(EmployerModel employerModel) {
        if (!Utils.isNetworkAvailable(context)) {
            onEmployerModel.onResult(null);
            return;
        }

        getDocument(employerModel.getEmail()).set(employerModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                onEmployerUploadSuccess.onResult();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onEmployerUploadFailure.onResult();
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
                onEmployerUploadSuccess.onResult(url);
            }
            @Override
            public void uploadFailed() {
                onEmployerUploadFailure.onResult();
            }
        }.uploadImage(imageView, EmployerModel.getCollectionId() + "/" + email);
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
                onEmployerUploadSuccess.onResult(url);
            }
            @Override
            public void uploadFailed() {
                onEmployerUploadFailure.onResult();
            }
        }.uploadImage(uri, EmployerModel.getCollectionId() + "/" + email);
    }

    /**
     * Update the job field of an employer
     * @param job a DocumentReference object indicating the job to be added
     * @param employer a DocumentReference object indicating the employer to be edited
     */
    public void updateJob(DocumentReference job, DocumentReference employer) {
        employer.update(EmployerModel.JOBS, FieldValue.arrayUnion(job)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                onEmployerUploadSuccess.onResult();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onEmployerUploadFailure.onResult();
            }
        });
    }

    /**
     * Update the job field of an employer
     * @param employer a DocumentReference object indicating the employer
     */
    public void getEmployerName(DocumentReference employer) {
        employer.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                EmployerModel employerModel = documentSnapshot.toObject(EmployerModel.class);
                onEmployerUploadSuccess.onResult(employerModel.getName());
            }
        });
    }

    public interface OnEmployerModel {
        void onResult(EmployerModel EmployerModel);
    }

    public abstract static class OnEmployerUploadSuccess{
        protected void onResult() {}
        protected void onResult(String string) {}
    }

    public interface OnEmployerUploadFailure{
        void onResult();
    }

}
