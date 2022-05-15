package com.example.nftscmers.employeractivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.adapters.JobHistoryAdapter;
import com.example.nftscmers.commonactivities.FeedbackActivity;
import com.example.nftscmers.commonactivities.ViewJobActivity;
import com.example.nftscmers.db.EmployerDb;
import com.example.nftscmers.db.JobDb;
import com.example.nftscmers.db.JobDb;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.utils.Globals;
import com.example.nftscmers.utils.LoggedInUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class JobHistoryActivity extends AppCompatActivity {
    public static final String TAG = "Job History";

    ListView jobListView;
    ArrayList<HashMap<String, String>> jobDetailsList = new ArrayList<>();
    ArrayList<DocumentReference> jobsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_history);

        jobListView = findViewById(R.id.job_history_list);

        ArrayAdapter arrayAdapter = new JobHistoryAdapter(JobHistoryActivity.this, R.layout.item_job_history, jobDetailsList, new JobHistoryAdapter.OnItemClickListener() {
            @Override
            public void onResult(int position) {
                Intent intent = new Intent(JobHistoryActivity.this, ViewJobActivity.class);
                intent.putExtra(ViewJobActivity.TAG, jobsList.get(position).getId());

                startActivityForResult(intent, 0);
            }
        });
        jobListView.setAdapter(arrayAdapter);

        new EmployerDb(JobHistoryActivity.this, new EmployerDb.OnEmployerModel() {
            @Override
            public void onResult(EmployerModel employerModel) {
                jobsList = employerModel.getJobs();

                for (DocumentReference job : jobsList) {
                    Log.d(TAG, "onResult: " + job.getId());
                    new JobDb(JobHistoryActivity.this, new JobDb.OnJobModel() {
                        @Override
                        public void onResult(JobModel jobModel) {
                            HashMap<String, String> jobDetails = new HashMap<>();
                            jobDetails.put(JobModel.POSITION, jobModel.getPosition());
                            jobDetails.put(JobModel.DEADLINE, Globals.DATEFORMAT.format(jobModel.getDeadline()));
                            jobDetailsList.add(jobDetails);

                            arrayAdapter.notifyDataSetChanged();
                        }
                    }).getJobModel(job);
                }
            }
        }).getEmployerModel(LoggedInUser.getInstance().getEmail());
    }
}

