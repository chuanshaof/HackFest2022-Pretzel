package com.example.nftscmers.db;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.nftscmers.R;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.objectmodels.ApplicationModel;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.utils.FirebaseStorageReference;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ApplicationDb extends Db{
    private static final String TAG = "ApplicationsDb";
    private static OnApplicationModel onApplicationModel;
    private static OnApplicationUploadSuccess onApplicationUploadSuccess;
    private static OnApplicationUploadFailure onApplicationUploadFailure;
    private Context context;

    public ApplicationDb() {
        super(ApplicationModel.getCollectionId());
    }

    public ApplicationDb(Context context, OnApplicationModel onApplicationModel) {
        super(ApplicationModel.getCollectionId());
        this.context = context;
        this.onApplicationModel = onApplicationModel;
    }

    public ApplicationDb(Context context, OnApplicationUploadSuccess onApplicationUploadSuccess) {
        super(ApplicationModel.getCollectionId());
        this.context = context;
        this.onApplicationUploadSuccess = onApplicationUploadSuccess;
    }

    public ApplicationDb(Context context, OnApplicationUploadSuccess onApplicationUploadSuccess, OnApplicationUploadFailure onApplicationUploadFailure) {
        super(ApplicationModel.getCollectionId());
        this.context = context;
        this.onApplicationUploadSuccess = onApplicationUploadSuccess;
        this.onApplicationUploadFailure = onApplicationUploadFailure;
    }

    /**
     * Get ApplicationModel from an email address
     * @param uid a String object containing an email address
     */
    public void getApplicationModel(String uid) {
        if (!Utils.isNetworkAvailable(context)) {
            onApplicationModel.onResult(null);
            return;
        }

        getDocument(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ApplicationModel applicationModel = documentSnapshot.toObject(ApplicationModel.class);
                Log.d(TAG, "onSuccess " + applicationModel.getDocumentId());
                onApplicationModel.onResult(applicationModel);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    /**
     * Create new account for applications using only email with no details yet
     * @param applicant a DocumentReference object containing the applicant
     * @param job a DocumentReference object containing the job that is being applied to
     */
    public void newApplication(DocumentReference applicant, DocumentReference job) {
        DocumentReference application = getDocument(UUID.randomUUID().toString());

        new ApplicantDb(context, new ApplicantDb.OnApplicantUploadSuccess() {
            @Override
            protected void onResult() {
                new JobDb(context, new JobDb.OnJobUploadSuccess() {
                    @Override
                    public void onResult() {
                        ApplicationModel applicationModel = new ApplicationModel(applicant, job, null, null, ApplicationModel.PENDING);

                        application.set(applicationModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                onApplicationUploadSuccess.onResult();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                onApplicationUploadFailure.onResult();
                            }
                        });
                    }
                }).addPending(application, job);
            }
        }).updateApplications(application, applicant);
    }

    /**
     * Update feedback of the application
     * @param applicationModel an ApplicationModel
     * @param uid containing the application ID
     */
    public void updateApplication(ApplicationModel applicationModel, String uid) {
        if (!Utils.isNetworkAvailable(context)) {
            onApplicationModel.onResult(null);
            return;
        }

        getDocument(uid).set(applicationModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                onApplicationUploadSuccess.onResult();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onApplicationUploadFailure.onResult();
            }
        });
    }

    /**
     * Update feedback of the application
     * @param status a String object containing the new status of the application
     * @param uid a String object containing the application ID
     */
    public void updateApplicationStatus(String status, String uid) {
        if (!Utils.isNetworkAvailable(context)) {
            onApplicationModel.onResult(null);
            return;
        }

        updateApplicationStatus(status, getDocument(uid));
    }

    /**
     * Update feedback of the application
     * @param status a String object containing the new status of the application
     * @param application a DocumentReference referring to the application to be edited
     */
    public void updateApplicationStatus(String status, DocumentReference application) {
        if (!Utils.isNetworkAvailable(context)) {
            onApplicationModel.onResult(null);
            return;
        }

        application.update(ApplicationModel.STATUS, status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                onApplicationUploadSuccess.onResult();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onApplicationUploadFailure.onResult();
            }
        });
    }


    public interface OnApplicationModel {
        void onResult(ApplicationModel applicationModel);
    }

    public interface OnApplicationUploadSuccess{
        void onResult();
    }

    public interface OnApplicationUploadFailure{
        void onResult();
    }

}
