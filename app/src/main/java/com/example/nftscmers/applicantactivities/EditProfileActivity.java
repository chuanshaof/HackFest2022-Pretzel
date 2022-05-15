package com.example.nftscmers.applicantactivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.db.ApplicantDb;
import com.example.nftscmers.fragments.CropDialogFragment;
import com.example.nftscmers.fragments.SkillsDialogFragment;
import com.example.nftscmers.fragments.SkillsFragment;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.utils.LoggedInUser;
import com.example.nftscmers.utils.Utils;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "Applicant Edit Profile";

    ImageView profilePic;
    EditText name;
    TextView email;
    EditText about;
    EditText linkedIn;
    TextView skills;
    ListView skillsList;
    Button confirm;

    ApplicantModel applicant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_edit_profile);

        profilePic = findViewById(R.id.applicant_profile_pic);
        name = findViewById(R.id.applicant_name);
        email = findViewById(R.id.applicant_email);
        about = findViewById(R.id.applicant_about);
        linkedIn = findViewById(R.id.applicant_linkedin);

        skills = findViewById(R.id.applicant_skills);
        confirm = findViewById(R.id.applicant_edit_confirm);

        Utils.uneditableField(name);
        Utils.uneditableField(email);

        // Loading of previous applicant data
        new ApplicantDb(EditProfileActivity.this, new ApplicantDb.OnApplicantModel() {
            @Override
            public void onResult(ApplicantModel applicantModel) {
                Log.d(TAG, "onResult: " + applicantModel);
                applicant = applicantModel;

                Utils.loadImage(profilePic, applicant.getImage());
                Utils.setValid(name, applicant.getName());
                Utils.setValid(about, applicant.getAbout());
                Utils.setValid(linkedIn, applicant.getLinkedIn());
                Utils.setValid(email, applicant.getEmail());

                SkillsFragment skillsFragment = new SkillsFragment(applicant.getSkills());
                getSupportFragmentManager().beginTransaction().replace(R.id.applicant_skills_list, skillsFragment).commit();
            }
        }).getApplicantModel(LoggedInUser.getInstance().getEmail());

        // When user clicks on skills
        skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SkillsDialogFragment(applicant.getSkills(), new SkillsDialogFragment.onConfirmListener() {
                    @Override
                    public void onResult(ArrayList<DocumentReference> skillsList) {
                        Log.d(TAG, "onResult: " + skillsList);
                        applicant.setSkills(skillsList);

                        SkillsFragment skillsFragment = new SkillsFragment(applicant.getSkills());
                        getSupportFragmentManager().beginTransaction().replace(R.id.applicant_skills_list, skillsFragment).commit();
                    }
                }).show(getSupportFragmentManager(), TAG);
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: test");
                new CropDialogFragment(new CropDialogFragment.OnCropListener() {
                    @Override
                    public void onResult(Uri uri) {
                        new ApplicantDb(EditProfileActivity.this, new ApplicantDb.OnApplicantUploadSuccess() {
                            @Override
                            protected void onResult(String string) {
                                Utils.loadImage(profilePic, string);
                                applicant.setImage(string);
                            }
                        }).updateProfilePicture(uri, applicant.getEmail());
                    }
                }).show(getSupportFragmentManager(), TAG);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.invalidData(name, about)) {
                    return;
                }

                applicant.setName(name.getText().toString());
                applicant.setAbout(about.getText().toString());
                applicant.setLinkedIn(linkedIn.getText().toString());

                new ApplicantDb(EditProfileActivity.this, new ApplicantDb.OnApplicantUploadSuccess() {
                    @Override
                    protected void onResult() {
                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                }, new ApplicantDb.OnApplicantUploadFailure() {
                    @Override
                    public void onResult() {
                        Utils.fireStoreError(EditProfileActivity.this, TAG);
                    }
                }).updateProfile(applicant);
            }
        });
    }
}
