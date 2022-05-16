package com.example.nftscmers.employeractivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.commonactivities.ViewJobActivity;
import com.example.nftscmers.db.EmployerDb;
import com.example.nftscmers.db.JobDb;
import com.example.nftscmers.fragments.DatePickerDialogFragment;
import com.example.nftscmers.fragments.SkillsDialogFragment;
import com.example.nftscmers.fragments.SkillsFragment;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.utils.Globals;
import com.example.nftscmers.utils.LoggedInUser;
import com.example.nftscmers.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kotlinx.coroutines.Job;

public class EditJobActivity extends AppCompatActivity {
    private static final String TAG = "Edit Job";

    ImageView backButton;
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

        backButton = findViewById(R.id.back_button);
        position = findViewById(R.id.edit_job_position);
        description = findViewById(R.id.edit_job_description);
        deadline = findViewById(R.id.edit_job_deadline);
        location = findViewById(R.id.edit_job_location);
        skills = findViewById(R.id.edit_job_skills);
        confirm = findViewById(R.id.edit_job_confirm);

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

                    position.setEnabled(false);
                    Utils.uneditableField(position);

                    SkillsFragment skillsFragment = new SkillsFragment(job.getSkills());
                    getSupportFragmentManager().beginTransaction().replace(R.id.applicant_skills_list, skillsFragment).commit();
                }
            }).getJobModel(getIntent().getStringExtra(ViewJobActivity.TAG));
        } else {
            job = new JobModel(LoggedInUser.getInstance().getUserDocRef());
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditJobActivity.this, ScrollApplicationActivity.class);
                startActivity(intent);
            }
        });

        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialogFragment(new DatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onResult(Calendar calendar) {
                        deadlineCalendar = calendar;
                        Utils.setValid(deadline, Globals.DATEFORMAT.format(calendar.getTime()));
                    }
                }).setMinDate(Calendar.getInstance()).show(getSupportFragmentManager(), TAG);
            }
        });

        skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SkillsDialogFragment(job.getSkills(), new SkillsDialogFragment.onConfirmListener() {
                    @Override
                    public void onResult(ArrayList<DocumentReference> skillsList) {
                        Log.d(TAG, "onResult: " + skillsList);
                        job.setSkills(skillsList);

                        SkillsFragment skillsFragment = new SkillsFragment(job.getSkills());
                        getSupportFragmentManager().beginTransaction().replace(R.id.edit_job_skills_list, skillsFragment).commit();
                    }
                }).show(getSupportFragmentManager(), TAG);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.invalidData(position, deadline, description)) {
                    return;
                }

                JobDb jobDb = new JobDb(EditJobActivity.this, new JobDb.OnJobUploadSuccess() {
                    @Override
                    public void onResult() {
                        Intent intent = new Intent(EditJobActivity.this, ScrollApplicationActivity.class);
                        startActivity(intent);
                    }
                });

                job.setDeadline(deadlineCalendar.getTime());
                job.setLocation(location.getText().toString());
                job.setDescription(description.getText().toString());
                job.setPosition(position.getText().toString());

                // Handling if it is an existing job or new job
                if (job.getDocumentId() == null) {
                    jobDb.createJob(job, LoggedInUser.getInstance().getEmail());
                } else {
                    jobDb.updateJob(job);
                }
            }
        });
    }
}
