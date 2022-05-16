package com.example.nftscmers.employeractivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.adapters.ApplicantAdapter;
import com.example.nftscmers.db.EmployerDb;
import com.example.nftscmers.db.JobDb;
import com.example.nftscmers.employeractivities.ProfileActivity;
import com.example.nftscmers.db.ApplicantDb;
import com.example.nftscmers.fragments.SkillsFragment;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.objectmodels.TestModel;
import com.example.nftscmers.utils.LoggedInUser;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class ScrollApplicationActivity extends AppCompatActivity {

//    private ApplicantAdapter arrayAdapter;
//    ApplicantAdapter arrayAdapter;
    TextView name;
    TextView email;
    ImageView image;
    SwipeFlingAdapterView flingAdapterView;
    
    // TODO: Change the TAG
    public static final String TAG = "YOUR-TAG-NAME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_dislike_button_bottom);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        flingAdapterView=findViewById(R.id.swipe);

        ArrayList<Object> item = new ArrayList<>();

        ApplicantAdapter arrayAdapter =new ApplicantAdapter(ScrollApplicationActivity.this, R.layout.item_in_cardview, item);
        flingAdapterView.setAdapter(arrayAdapter);

        new EmployerDb(ScrollApplicationActivity.this, new EmployerDb.OnEmployerModel() {
            @Override
            public void onResult(EmployerModel employerModel) {
                for (DocumentReference job : employerModel.getJobs()) {
                    new JobDb(ScrollApplicationActivity.this, new JobDb.OnJobModel() {
                        @Override
                        public void onResult(JobModel jobModel) {
                            for (DocumentReference applicant : jobModel.getPending()) {
                                new ApplicantDb(ScrollApplicationActivity.this, new ApplicantDb.OnApplicantModel() {
                                    @Override
                                    public void onResult(ApplicantModel applicantModel) {
                                        Log.d(TAG, "onResult: " + applicantModel);
                                        item.add(applicantModel);
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                }).getApplicantModel(applicant);
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

                Toast.makeText(ScrollApplicationActivity.this,"dislike",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(ScrollApplicationActivity.this,"like",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ScrollApplicationActivity.this, "data is "+item.get(i),Toast.LENGTH_SHORT).show();
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






