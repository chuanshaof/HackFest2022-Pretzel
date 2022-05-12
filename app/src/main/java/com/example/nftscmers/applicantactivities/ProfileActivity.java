package com.example.nftscmers.applicantactivities;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.utils.LoggedInUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "Applicant.EditProfileActivity";

    ImageView IV_back;
    ImageView IV_edit_profile;
    ImageView IV_profile_pic;
    TextView TV_name;
    TextView TV_about;
    TextView TV_linkedin;
    Button BT_logout;
    ListView LV_skills;

    ApplicantModel applicant;

    ArrayList<String> skills_list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_profile);

        IV_back = findViewById(R.id.iv_back_arrow);
        IV_edit_profile = findViewById(R.id.iv_edit_profile);
        IV_profile_pic = findViewById(R.id.iv_profile_pic);
        TV_name = findViewById(R.id.tv_name);
        TV_about = findViewById(R.id.tv_about);
        TV_linkedin = findViewById(R.id.tv_linkedin);
        BT_logout = findViewById(R.id.button_logout);
        LV_skills = findViewById(R.id.lv_skills);

        /*
        TODO: get logged in user (applicant acc) ->
         firebase get userdoc snapshot to applicant object model ->
         */
        applicant = null; // update this

        setup_edit_button();
    }

    /**
     * This function populates the entire profile page
     * @param applicant ApplicantModel of logged in user
     */
    private void populate_profile(ApplicantModel applicant) {
        for (DocumentReference docRef : applicant.getSkills()) {
            skills_list.add(docRef.getId());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,skills_list);
        LV_skills.setAdapter(arrayAdapter);
    }


    /**
     * This function sets up the edit icon and navigate user to edit profile page on press
     */
    private void setup_edit_button(){
        IV_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

}
