package com.example.nftscmers.commonactivities;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.db.ApplicationDb;
import com.example.nftscmers.fragments.SkillsDialogFragment;
import com.example.nftscmers.fragments.SkillsFragment;
import com.example.nftscmers.objectmodels.ApplicationModel;
import com.example.nftscmers.utils.Globals;
import com.example.nftscmers.utils.LoggedInUser;
import com.example.nftscmers.utils.Utils;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity {
    private static final String TAG = "FeedbackActivity";

    ImageView backButton;
    TextView feedbackTitle;
    EditText feedback;
    TextView skills;
    Button submit;

    ApplicationModel application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        backButton = findViewById(R.id.back_button);
        feedbackTitle = findViewById(R.id.feedback_title);
        feedback = findViewById(R.id.feedback);
        skills = findViewById(R.id.feedback_skills);
        submit = findViewById(R.id.feedback_submit);

        new ApplicationDb(FeedbackActivity.this, new ApplicationDb.OnApplicationModel() {
            @Override
            public void onResult(ApplicationModel applicationModel) {
                application = applicationModel;
                feedback.setText(applicationModel.getFeedback());
                SkillsFragment skillsFragment = new SkillsFragment(applicationModel.getFeedbackSkills());
                getSupportFragmentManager().beginTransaction().replace(R.id.feedback_skills_list, skillsFragment).commit();
            }
        }).getApplicationModel(getIntent().getStringExtra(TAG));

        if (LoggedInUser.getInstance().getAccountType() == Globals.EMPLOYER) {
            Utils.disableButton(backButton);
            feedbackTitle.setText(getString(R.string.feedback_view));
        } else {
            Utils.disableButton(submit);
            feedbackTitle.setText(getString(R.string.feedback_set));
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // When user clicks on skills
        skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SkillsDialogFragment(application.getFeedbackSkills(), new SkillsDialogFragment.onConfirmListener() {
                    @Override
                    public void onResult(ArrayList<DocumentReference> skillsList) {
                        Log.d(TAG, "onResult: " + skillsList);
                        application.setFeedbackSkills(skillsList);

                        SkillsFragment skillsFragment = new SkillsFragment(application.getFeedbackSkills());
                        getSupportFragmentManager().beginTransaction().replace(R.id.feedback_skills_list, skillsFragment).commit();
                    }
                }).show(getSupportFragmentManager(), TAG);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.invalidData(feedback)) {
                    return;
                } else {
                    application.setFeedback(feedback.getText().toString());
                    new ApplicationDb(FeedbackActivity.this, new ApplicationDb.OnApplicationUploadSuccess() {
                        @Override
                        public void onResult() {
                            finish();
                        }
                    }).updateApplication(application, getIntent().getStringExtra(TAG));
                }
            }
        });
    }
}
