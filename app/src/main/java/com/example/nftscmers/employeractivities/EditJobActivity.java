package com.example.nftscmers.employeractivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.commonactivities.ViewJobActivity;
import com.example.nftscmers.db.JobDb;
import com.example.nftscmers.fragments.SkillsFragment;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.utils.Globals;
import com.example.nftscmers.utils.Utils;

import java.util.Calendar;
import java.util.Date;

import kotlinx.coroutines.Job;

public class EditJobActivity extends AppCompatActivity {
    private static final String TAG = "Edit Job";

    Button backButton;
    EditText position;
    EditText description;
    EditText deadline;
    EditText location;
    TextView skills;
    Button confirm;

    Calendar deadlineCalendar;
    JobModel job;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_edit_job);

        position = findViewById(R.id.edit_job_position);
        description = findViewById(R.id.edit_job_description);
        deadline = findViewById(R.id.edit_job_deadline);
        location = findViewById(R.id.edit_job_location);
        skills = findViewById(R.id.edit_job_skills);
        confirm = findViewById(R.id.edit_job_confirm);

        Utils.uneditableField(position);

        if (getIntent().getStringExtra(ViewJobActivity.TAG) != null) {
            // Loading of previous job data
            new JobDb(EditJobActivity.this, new JobDb.OnJobModel() {
                @Override
                public void onResult(JobModel jobModel) {
                    Log.d(TAG, "onResult: " + jobModel);
                    job = jobModel;

                    Utils.setValid(position, job.getPosition());
                    Utils.setValid(description, job.getDescription());

                    Utils.setValid(deadline, Globals.DATEFORMAT.format(job.getDeadline()));
                    Utils.setValid(location, job.getLocation());

                    SkillsFragment skillsFragment = new SkillsFragment(job.getSkills());
                    getSupportFragmentManager().beginTransaction().replace(R.id.applicant_skills_list, skillsFragment).commit();
                }
            }).getJobModel(getIntent().getStringExtra(ViewJobActivity.TAG));
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditJobActivity.this, ViewJobActivity.class);
                startActivity(intent);
            }
        });

        deadline.

//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Utils.invalidData(position, deadline, description)) {
//                    return;
//                }
//
//                new JobDb(EditJobActivity.this, new JobDb.OnJobsUploadSuccess() {
//                    @Override
//                    protected void onResult() {
//                        Intent intent = new Intent(EditJobActivity.this, ProfileActivity.class);
//                        startActivity(intent);
//                    }
//                }, new JobDb.OnJobsUploadFailure() {
//                    @Override
//                    public void onResult() {
//                        Utils.fireStoreError(EditJobActivity.this, TAG);
//                    }
//                }).updateProfile(job);
//            }
//        });
    }
}
