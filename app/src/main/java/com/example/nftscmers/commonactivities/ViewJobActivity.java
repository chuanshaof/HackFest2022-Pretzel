package com.example.nftscmers.commonactivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.commonactivities.ViewJobActivity;
import com.example.nftscmers.db.EmployerDb;
import com.example.nftscmers.db.JobDb;
import com.example.nftscmers.fragments.DatePickerDialogFragment;
import com.example.nftscmers.fragments.SkillsDialogFragment;
import com.example.nftscmers.fragments.SkillsFragment;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.utils.Globals;
import com.example.nftscmers.utils.LoggedInUser;
import com.example.nftscmers.utils.Utils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kotlinx.coroutines.Job;

public class ViewJobActivity extends AppCompatActivity {
    public static final String TAG = "View Job";

    ImageView backButton;
    ImageView edit;
    TextView employer;
    ImageView employerPic;
    TextView position;
    TextView description;
    TextView deadline;
    TextView location;
    Button confirm;

    JobModel job;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);

        backButton = findViewById(R.id.job_back);
        edit = findViewById(R.id.job_edit);
        employer = findViewById(R.id.job_employer);
        employerPic = findViewById(R.id.job_picture);
        position = findViewById(R.id.job_position);
        description = findViewById(R.id.job_description);
        deadline = findViewById(R.id.job_deadline);
        location = findViewById(R.id.job_location);
        confirm = findViewById(R.id.job_confirm);

        LoggedInUser.getInstance().setUser(FirebaseFirestore.getInstance().collection(EmployerModel.getCollectionId()).document("employer@gmail.com"), "employer@gmail.com", Globals.EMPLOYER);

        // TODO: pass the Job UUID into the intent
        // Loading of previous job data
        new JobDb(ViewJobActivity.this, new JobDb.OnJobModel() {
            @Override
            public void onResult(JobModel jobModel) {
                Log.d(TAG, "onResult: " + jobModel);
                job = jobModel;

                Utils.loadImage(employerPic, job.getEmployerPic());
                Utils.setValid(employer, job.getEmployerName());
                Utils.setValid(position, job.getPosition());
                Utils.setValid(description, job.getDescription());

                Utils.setValid(deadline, Globals.DATEFORMAT.format(job.getDeadline()));
                Utils.setValid(location, job.getLocation());

                position.setEnabled(false);
                Utils.uneditableField(position);

//                SkillsFragment skillsFragment = new SkillsFragment(job.getSkills());
//                getSupportFragmentManager().beginTransaction().replace(R.id.applicant_skills_list, skillsFragment).commit();

                // Remove edit button if user viewing is not the employer
                if (!LoggedInUser.getInstance().getUserDocRef().equals(job.getEmployer())) {
                    edit.setEnabled(false);
                    edit.setClickable(false);
                    edit.setVisibility(View.GONE);
                }
            }
        }).getJobModel("f27a560d-77fe-4c06-a416-a632055197db");
        //getJobModel(getIntent().getStringExtra(ViewJobActivity.TAG));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewJobActivity.this, ViewJobActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Link to somewhere else
                Intent intent = new Intent(ViewJobActivity.this, ViewJobActivity.class);
                startActivity(intent);
            }
        });

//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Utils.invalidData(position, deadline, description)) {
//                    return;
//                }
//
//                JobDb jobDb = new JobDb(ViewJobActivity.this, new JobDb.OnJobUploadSuccess() {
//                    @Override
//                    public void onResult() {
//                        Intent intent = new Intent(ViewJobActivity.this, ViewJobActivity.class);
//                        startActivity(intent);
//                    }
//                });
//
//                job.setDeadline(deadlineCalendar.getTime());
//                job.setLocation(location.getText().toString());
//                job.setDescription(description.getText().toString());
//                job.setPosition(position.getText().toString());
//
//                // Handling if it is an existing job or new job
//                if (job.getDocumentId() == null) {
//                    jobDb.createJob(job);
//                } else {
//                    jobDb.updateJob(job);
//                }
//            }
//        });
    }
}
