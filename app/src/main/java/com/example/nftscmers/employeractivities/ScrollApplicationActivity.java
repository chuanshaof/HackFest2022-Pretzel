package com.example.nftscmers.employeractivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.adapters.ApplicantAdapter;
import com.example.nftscmers.commonactivities.FeedbackActivity;
import com.example.nftscmers.db.ApplicationDb;
import com.example.nftscmers.db.EmployerDb;
import com.example.nftscmers.db.JobDb;
import com.example.nftscmers.db.ApplicantDb;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.objectmodels.ApplicationModel;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.utils.LoggedInUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;

public class ScrollApplicationActivity extends AppCompatActivity {
    SwipeFlingAdapterView flingAdapterView;
    HashMap<ApplicantModel, ArrayList<DocumentReference>> jobTracker;

    public static final String TAG = "ScrollApplication";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_application);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        flingAdapterView=findViewById(R.id.swipe);

        ArrayList<ApplicantModel> item = new ArrayList<>();
        jobTracker = new HashMap<>();

        ApplicantAdapter arrayAdapter =new ApplicantAdapter(ScrollApplicationActivity.this, R.layout.item_in_cardview, item);
        flingAdapterView.setAdapter(arrayAdapter);

        new EmployerDb(ScrollApplicationActivity.this, new EmployerDb.OnEmployerModel() {
            @Override
            public void onResult(EmployerModel employerModel) {
                for (DocumentReference job : employerModel.getJobs()) {
                    new JobDb(ScrollApplicationActivity.this, new JobDb.OnJobModel() {
                        @Override
                        public void onResult(JobModel jobModel) {
                            for (DocumentReference application : jobModel.getPending()) {
                                Log.d(TAG, "onResult: " + application.getId());
                                new ApplicationDb(ScrollApplicationActivity.this, new ApplicationDb.OnApplicationModel() {
                                    @Override
                                    public void onResult(ApplicationModel applicationModel) {
                                        new ApplicantDb(ScrollApplicationActivity.this, new ApplicantDb.OnApplicantModel() {
                                            @Override
                                            public void onResult(ApplicantModel applicantModel) {
                                                Log.d(TAG, "onResult: " + applicantModel);
                                                item.add(applicantModel);

                                                ArrayList<DocumentReference> tracker = new ArrayList<>();
                                                tracker.add(application);
                                                tracker.add(job);

                                                jobTracker.put(applicantModel, tracker);
                                                arrayAdapter.notifyDataSetChanged();
                                            }
                                        }).getApplicantModel(applicationModel.getApplicant());
                                    }
                                }).getApplicationModel(application);
                            }
                        }
                    }).getJobModel(job);
                }
            }
        }).getEmployerModel(LoggedInUser.getInstance().getEmail());

        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                item.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                new JobDb(ScrollApplicationActivity.this, new JobDb.OnJobUploadSuccess() {
                    @Override
                    public void onResult() {
                        new ApplicationDb(ScrollApplicationActivity.this, new ApplicationDb.OnApplicationUploadSuccess() {
                            @Override
                            public void onResult() {
                                Toast.makeText(ScrollApplicationActivity.this,"Rejected",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ScrollApplicationActivity.this, FeedbackActivity.class);
                                intent.putExtra(FeedbackActivity.TAG, jobTracker.get(o).get(0).getId());
                                startActivityForResult(intent, 0);
                            }
                        }).updateApplicationStatus(ApplicationModel.REJECTED, jobTracker.get(o).get(0));
                    }
                }).deletePending(jobTracker.get(o).get(0), jobTracker.get(o).get(1));
            }

            @Override
            public void onRightCardExit(Object o) {
                Log.d(TAG, "onRightCardExit: " + o);
                new JobDb(ScrollApplicationActivity.this, new JobDb.OnJobUploadSuccess() {
                    @Override
                    public void onResult() {
                        new ApplicationDb(ScrollApplicationActivity.this, new ApplicationDb.OnApplicationUploadSuccess() {
                            @Override
                            public void onResult() {
                                Toast.makeText(ScrollApplicationActivity.this,"Accepted",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ScrollApplicationActivity.this, FeedbackActivity.class);
                                intent.putExtra(FeedbackActivity.TAG, jobTracker.get(o).get(0).getId());
                                startActivityForResult(intent, 0);
                            }
                        }).updateApplicationStatus(ApplicationModel.ACCEPTED, jobTracker.get(o).get(0));
                    }
                }).deletePending(jobTracker.get(o).get(0), jobTracker.get(o).get(1));
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });

        flingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {
                Intent intent = new Intent(ScrollApplicationActivity.this, com.example.nftscmers.applicantactivities.ProfileActivity.class);
                intent.putExtra(com.example.nftscmers.applicantactivities.ProfileActivity.TAG, item.get(i).getDocumentId());
                startActivity(intent);
            }
        });

        Button like,dislike;

        like=findViewById(R.id.like);
        dislike=findViewById(R.id.dislike);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingAdapterView.getTopCardListener().selectRight();
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingAdapterView.getTopCardListener().selectLeft();
            }
        });

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;

                switch(item.getItemId())
                {
                    case R.id.history:
                        intent = new Intent(ScrollApplicationActivity.this, JobHistoryActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.create:
                        startActivity(new Intent(getApplicationContext(), EditJobActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        intent = new Intent(ScrollApplicationActivity.this, ProfileActivity.class);
                        intent.putExtra(ProfileActivity.TAG, LoggedInUser.getInstance().getEmail());
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }




}






