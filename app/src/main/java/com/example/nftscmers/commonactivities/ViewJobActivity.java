package com.example.nftscmers.commonactivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.db.JobDb;
import com.example.nftscmers.fragments.SkillsFragment;
import com.example.nftscmers.objectmodels.JobModel;
import com.example.nftscmers.utils.Globals;
import com.example.nftscmers.utils.LoggedInUser;
import com.example.nftscmers.utils.Utils;

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

        // Loading of previous job data, pass UUID into the intent
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

                SkillsFragment skillsFragment = new SkillsFragment(job.getSkills());
                getSupportFragmentManager().beginTransaction().replace(R.id.job_skills_list, skillsFragment).commit();

                // Remove edit button if user viewing is not the employer
                if (!LoggedInUser.getInstance().getUserDocRef().equals(job.getEmployer())) {
                    Utils.disableButton(edit);
                }
            }
        }).getJobModel(getIntent().getStringExtra(ViewJobActivity.TAG));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
