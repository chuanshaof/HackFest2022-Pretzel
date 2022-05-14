package com.example.nftscmers.db;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nftscmers.R;
import com.example.nftscmers.employeractivities.EditJobActivity;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class JobDb extends Db {
    private static final String TAG = "JobsDb";
    private static OnJobModel onJobModel;
    private static OnJobUploadSuccess onJobUploadSuccess;
    private static OnJobUploadFailure onJobUploadFailure;
    private Context context;

    public JobDb() {
        super(JobModel.getCollectionId());
    }

    public JobDb(Context context, OnJobModel onJobModel) {
        super(JobModel.getCollectionId());
        this.onJobModel = onJobModel;
        this.context = context;
    }

    public JobDb(Context context, JobDb.OnJobUploadSuccess onJobsUploadSuccess) {
        super(JobModel.getCollectionId());
        this.onJobUploadSuccess = onJobsUploadSuccess;
        this.context = context;
    }

    public JobDb(Context context, JobDb.OnJobUploadSuccess onJobsUploadSuccess, JobDb.OnJobUploadFailure onJobsUploadFailure) {
        super(EmployerModel.getCollectionId());
        this.onJobUploadSuccess = onJobsUploadSuccess;
        this.onJobUploadFailure = onJobsUploadFailure;
        this.context = context;
    }

    /**
     * Get JobModel from the job ID
     * @param email an EditText object containing an email address
     */
    public void getJobModel(String email) {
        if (!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, R.string.network_missing, Toast.LENGTH_SHORT).show();
            onJobModel.onResult(null);
            return;
        }

        getDocument(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                JobModel JobModel = documentSnapshot.toObject(JobModel.class);
                onJobModel.onResult(JobModel);
            }
        });
    }

    public interface OnJobModel {
        void onResult(JobModel JobModel);
    }

    public interface OnJobUploadSuccess{
        void onResult();
    }

    public interface OnJobUploadFailure{
        void onResult();
    }

}

