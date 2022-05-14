package com.example.nftscmers.db;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nftscmers.R;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class JobsDb extends Db {
    private static final String TAG = "JobsDb";
    private static OnJobsModel onJobsModel;
    private static OnJobsUploadSuccess onJobsUploadSuccess;
    private static OnJobsUploadFailure onJobsUploadFailure;

    public JobsDb() {
        super(JobModel.getCollectionId());
    }

    public JobsDb(OnJobsModel onJobsModelSuccess) {
        super(JobModel.getCollectionId());
        this.onJobsModel = onJobsModelSuccess;
    }

    public JobsDb(JobsDb.OnJobsUploadSuccess onJobsUploadSuccess) {
        super(JobModel.getCollectionId());
        this.onJobsUploadSuccess = onJobsUploadSuccess;
    }

    public JobsDb(JobsDb.OnJobsUploadSuccess onJobsUploadSuccess, JobsDb.OnJobsUploadFailure onJobsUploadFailure) {
        super(EmployerModel.getCollectionId());
        this.onJobsUploadSuccess = onJobsUploadSuccess;
        this.onJobsUploadFailure = onJobsUploadFailure;
    }

    /**
     * Get JobsModel from an email address
     * @param context a Context object
     * @param email an EditText object containing an email address
     */
    public void getJobsModel(Context context, String email) {
        if (!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, R.string.network_missing, Toast.LENGTH_SHORT).show();
            onJobsModel.onResult(null);
            return;
        }

        getDocument(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                JobModel JobModel = documentSnapshot.toObject(JobModel.class);
                onJobsModel.onResult(JobModel);
            }
        });
    }

    /**
     * Get EmployerModel from an email address
     * @param context a Context object
     * @param email an EditText object containing an email address
     */
    public void getJobsModel(Context context, EditText email) {
        if (Utils.invalidData(email)){
            onJobsModel.onResult(null);
            return;
        }
        getJobsModel(context, email.getText().toString());
    }

    /**
     * Create new account for Employers using only email with no details yet
     * @param context a Context object
     * @param email an EditText object containing an email address
     */
    public void newProfile(Context context, EditText email) {
        if (Utils.invalidData(email)){
            onJobsUploadFailure.onResult();
            return;
        }

        getDocument(email.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i(TAG, context.getString(R.string.duplicate_email));
                        onJobsUploadFailure.onResult();
                    } else {
                        Map<String, Object> account = new HashMap<>();
                        account.put(EmployerModel.EMAIL, email.getText().toString());

                        getCollection().document(email.getText().toString()).set(account)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        onJobsUploadSuccess.onResult();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, context.getString(R.string.firestore_error), e);
                                        onJobsUploadFailure.onResult();
                                    }
                                });
                    }
                } else {
                    Log.w(TAG, context.getString(R.string.firestore_error));
                    onJobsUploadFailure.onResult();
                }
            }
        });
    }


    public interface OnJobsModel {
        void onResult(JobModel JobModel);
    }

    public interface OnJobsUploadSuccess{
        void onResult();
    }

    public interface OnJobsUploadFailure{
        void onResult();
    }

}

