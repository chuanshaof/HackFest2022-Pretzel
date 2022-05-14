package com.example.nftscmers.applicantactivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.db.ApplicantDb;
import com.example.nftscmers.fragments.SkillsFragment;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.utils.Globals;
import com.example.nftscmers.utils.LoggedInUser;
import com.example.nftscmers.utils.Utils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ApplicantEditProfile";

    ImageView back;
    ImageView editProfile;
    ImageView profilePic;
    TextView name;
    TextView email;
    TextView about;
    TextView linkedIn;
    Button logout;
    ListView skills;

    ApplicantModel applicant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_profile);

        back = findViewById(R.id.applicant_back_arrow);
        editProfile = findViewById(R.id.applicant_edit_profile);
        profilePic = findViewById(R.id.applicant_profile_pic);
        name = findViewById(R.id.applicant_name);
        email = findViewById(R.id.applicant_email);
        about = findViewById(R.id.applicant_about);
        linkedIn = findViewById(R.id.applicant_linkedIn);

        LoggedInUser.getInstance().setUser(null, "tester@gmail.com", Globals.APPLICANT);

        // Loading of previous applicant data
        new ApplicantDb(ProfileActivity.this, new ApplicantDb.OnApplicantModel() {
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
                getSupportFragmentManager().beginTransaction().replace(R.id.applicant_skills, skillsFragment).commit();
            }
        }).getApplicantModel(LoggedInUser.getInstance().getEmail());

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
