package com.example.nftscmers.applicantactivities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.adapters.ApplicationHistoryAdapter;
import com.example.nftscmers.db.ApplicantDb;
import com.example.nftscmers.db.ApplicationDb;
import com.example.nftscmers.db.JobDb;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.objectmodels.ApplicationModel;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.utils.LoggedInUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicationHistoryActivity extends AppCompatActivity {
    public static final String TAG = "Application History";

    ListView applicationListView;
    ArrayList<HashMap<String, String>> applicationDetailsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_application_history);

        applicationListView = findViewById(R.id.application_history_list);

        LoggedInUser.getInstance().setUser(null, "jon@gmail.com", null);

        ArrayAdapter arrayAdapter = new ApplicationHistoryAdapter(ApplicationHistoryActivity.this, R.layout.item_application_history, applicationDetailsList);
        applicationListView.setAdapter(arrayAdapter);

        new ApplicantDb(ApplicationHistoryActivity.this, new ApplicantDb.OnApplicantModel() {
            @Override
            public void onResult(ApplicantModel applicantModel) {
                ArrayList<DocumentReference> applicationsList = applicantModel.getApplications();

                for (DocumentReference application : applicationsList) {
                    Log.d(TAG, "onResult: " + application.getId());
                    new ApplicationDb(ApplicationHistoryActivity.this, new ApplicationDb.OnApplicationModel() {
                        @Override
                        public void onResult(ApplicationModel applicationModel) {
                            HashMap<String, String> applicationDetails = new HashMap<>();
                            applicationDetails.put(ApplicationModel.STATUS, applicationModel.getStatus());

                            new JobDb(ApplicationHistoryActivity.this, new JobDb.OnJobModel() {
                                @Override
                                public void onResult(JobModel jobModel) {
                                    applicationDetails.put(ApplicationModel.COMPANY, jobModel.getEmployerName());
                                    applicationDetails.put(ApplicationModel.POSITION, jobModel.getPosition());
                                    applicationDetailsList.add(applicationDetails);

                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }).getJobModel(applicationModel.getJob());
                        }
                    }).getApplicationModel(application);
                }
            }
        }).getApplicantModel(LoggedInUser.getInstance().getEmail());
    }
}
