package com.example.nftscmers.applicantactivities;

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
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.utils.Globals;
import com.example.nftscmers.utils.LoggedInUser;
import com.example.nftscmers.utils.Utils;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "Applicant Edit Profile";

    ImageView profile_pic;
    EditText name;
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

        profile_pic = findViewById(R.id.applicant_profile_pic);
        name = findViewById(R.id.applicant_name);
        about = findViewById(R.id.applicant_about);
        linkedIn = findViewById(R.id.applicant_linkedin);

        skills = findViewById(R.id.applicant_skills);
        skillsList = findViewById(R.id.applicant_skills_list);
        confirm = findViewById(R.id.applicant_edit_confirm);

        LoggedInUser.getInstance().setUser(null, "tester@gmail.com", Globals.APPLICANT);

        // Loading of previous applicant data
        new ApplicantDb(new ApplicantDb.OnApplicantModel() {
            @Override
            public void onResult(ApplicantModel applicantModel) {
                Log.d(TAG, "onResult: " + applicantModel);
                applicant = applicantModel;

                Utils.loadImage(profile_pic, applicant.getImage());
                Utils.setValid(name, applicant.getName());
                Utils.setValid(about, applicant.getAbout());
                Utils.setValid(linkedIn, applicant.getLinkedIn());
            }
        }).getApplicantModel(EditProfileActivity.this, LoggedInUser.getInstance().getEmail());

        // when user clicks on skills
        skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SkillsDialogFragment(applicant.getSkills(), new SkillsDialogFragment.onConfirmListener() {
                    @Override
                    public void onResult(ArrayList<DocumentReference> skillsList) {
                        Log.d(TAG, "onResult: " + skillsList);
                        applicant.setSkills(skillsList);
                    }
                }).show(getSupportFragmentManager(), TAG);
            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CropDialogFragment(new CropDialogFragment.OnCropListener() {
                    @Override
                    public void onResult(Uri uri) {
                        profile_pic.setImageURI(uri);
                    }
                });
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

//
//    /**
//     * TODO: update firebase when profile edited
//     * @param applicant ApplicantModel of logged in user
//     */
//    private void setup_confirm_button(ApplicantModel applicant) {
//        name.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                Map<String, Object> name_map = new HashMap<>();
//                name_map.put("name", editable.toString());
////                applicant_docRef.update(name_map);
//            }
//        });
//
//        about.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                Map<String, Object> about_map = new HashMap<>();
//                about_map.put("about", editable.toString());
////                applicant_docRef.update(about_map);
//            }
//        });
//
//        // TODO: NEED TO UPDATE COURSES WHEN UPDATE SKILLS?????????? CONFUSED
//        if (original_skills_list.size() != display_skills_list.size()) {
//            Map<String, Object> add_hashMap_followers = new HashMap<>();
//            for (String skill_name : new_skills_list) { // this shouldn't be a string right
//                add_hashMap_followers.put("skills", FieldValue.arrayUnion(skill_name));
//            }
////            applicant_docRef.update(add_hashMap_followers);
//        }
//    }
}
