package com.example.nftscmers.applicantactivities;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;

import com.example.nftscmers.adapters.ApplicantAdapter;
import com.example.nftscmers.adapters.EmployerAdapter;
import com.example.nftscmers.adapters.JobAdapter;
import com.example.nftscmers.commonactivities.FeedbackActivity;
import com.example.nftscmers.commonactivities.ViewJobActivity;
import com.example.nftscmers.db.ApplicantDb;
import com.example.nftscmers.db.ApplicationDb;
import com.example.nftscmers.db.EmployerDb;
import com.example.nftscmers.db.JobDb;
import com.example.nftscmers.employeractivities.ScrollApplicationActivity;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.objectmodels.ApplicationModel;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.utils.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScrollJobActivity extends AppCompatActivity {

    private JobAdapter arrayAdapter;
    SwipeFlingAdapterView flingAdapterView;
    HashMap<JobModel, DocumentReference> jobTracker;

    public static final String TAG = "ScrollJob";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_dislike_button_bottom);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        flingAdapterView = findViewById(R.id.swipe);
        ArrayList<JobModel> data = new ArrayList<>();
        jobTracker = new HashMap<>();

        arrayAdapter = new JobAdapter(ScrollJobActivity.this, R.layout.item_in_cardview, data);
        flingAdapterView.setAdapter(arrayAdapter);

        db.collection(JobModel.getCollectionId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        new JobDb(ScrollJobActivity.this, new JobDb.OnJobModel() {
                            @Override
                            public void onResult(JobModel jobModel) {
                                if (!jobModel.getPending().contains(LoggedInUser.getInstance().getUserDocRef())){
                                    data.add(jobModel);
                                    arrayAdapter.notifyDataSetChanged();

                                    jobTracker.put(jobModel, documentSnapshot.getReference());
                                }
                            }
                        }).getJobModel(documentSnapshot.getReference());
                    }
                }
            }
        });

        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                data.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(ScrollJobActivity.this,"Skipped Job",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object o) {
                new ApplicationDb(ScrollJobActivity.this, new ApplicationDb.OnApplicationUploadSuccess() {
                    @Override
                    public void onResult() {
                        Toast.makeText(ScrollJobActivity.this,"Applied",Toast.LENGTH_SHORT).show();
                    }
                }).newApplication(LoggedInUser.getInstance().getUserDocRef(), jobTracker.get(o));
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
                Toast.makeText(ScrollJobActivity.this, "data is " + data.get(i),Toast.LENGTH_SHORT).show();
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
                        intent = new Intent(ScrollJobActivity.this, ApplicationHistoryActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.profile:
                        intent = new Intent(ScrollJobActivity.this, ProfileActivity.class);
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
