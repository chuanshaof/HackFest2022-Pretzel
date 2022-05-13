package com.example.nftscmers.applicantactivities;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.fragments.CropDialogFragment;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.utils.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "Applicant Edit Profile";

    ImageView profile_pic;
    EditText name;
    EditText about;
    TextView skills;
    ListView skillsList;
    Button confirm;

    ArrayList<String> original_skills_list = new ArrayList<>();
    List<String> new_skills_list = new ArrayList<>();
    ArrayList<String> display_skills_list = new ArrayList<>();

    ApplicantModel applicant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_edit_profile);

        profile_pic = findViewById(R.id.applicant_profile_pic);
        name = findViewById(R.id.applicant_name);
        about = findViewById(R.id.applicant_about);
        skills = findViewById(R.id.applicant_skills);
        skillsList = findViewById(R.id.applicant_skills_list);
        confirm = findViewById(R.id.applicant_edit_confirm);

        // Pre-load current information in the user's profile
        LoggedInUser.getInstance().getUserDocRef().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    applicant = document.toObject(ApplicantModel.class);

                    // IV_profile_pic.setImageURI();
                    name.setText(applicant.getName());
                    about.setText(applicant.getAbout());
                    populate_skills(applicant);

                    Log.d(TAG, "Cached document data: " + document.getData());
                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
            }
        });

        // when user clicks on skills
        skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

        // TODO: when user clicks on profile photo
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CropDialogFragment(new CropDialogFragment.OnCropListener() {
                    @Override
                    public void onResult(Uri uri) {

                    }
                });
            }
        });

        setup_confirm_button(applicant);
    }

    /**
     * This function populates the skills listview
     * @param applicant ApplicantModel of logged in user
     */
    private void populate_skills(ApplicantModel applicant) {
        if (original_skills_list.size() == 0) {
            for (DocumentReference docRef : applicant.getSkills()) {
                original_skills_list.add(docRef.getId());
            }
            display_skills_list = original_skills_list;
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,display_skills_list);
        skillsList.setAdapter(arrayAdapter);
    }


    /**
     * This function shows the dialog box
     */
    void showCustomDialog() {
        final Dialog dialog = new Dialog(com.example.nftscmers.applicantactivities.EditProfileActivity.this);
        //Disable default title so that the custom title "Skills" will show.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog by clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.dialog_edit_profile_skills);

        // Initializing the views of the dialog.
        EditText ET_skill_name = dialog.findViewById(R.id.et_skill_name);
        Button BT_add = dialog.findViewById(R.id.button_add);

        BT_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String skill_name = ET_skill_name.getText().toString();
                new_skills_list.add(skill_name);
                display_skills_list.add(skill_name);
                populate_skills(applicant);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * TODO: update firebase when profile edited
     * @param applicant ApplicantModel of logged in user
     */
    private void setup_confirm_button(ApplicantModel applicant) {
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Map<String, Object> name_map = new HashMap<>();
                name_map.put("name", editable.toString());
//                applicant_docRef.update(name_map);
            }
        });

        about.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Map<String, Object> about_map = new HashMap<>();
                about_map.put("about", editable.toString());
//                applicant_docRef.update(about_map);
            }
        });

        // TODO: NEED TO UPDATE COURSES WHEN UPDATE SKILLS?????????? CONFUSED
        if (original_skills_list.size() != display_skills_list.size()) {
            Map<String, Object> add_hashMap_followers = new HashMap<>();
            for (String skill_name : new_skills_list) { // this shouldn't be a string right
                add_hashMap_followers.put("skills", FieldValue.arrayUnion(skill_name));
            }
//            applicant_docRef.update(add_hashMap_followers);
        }
    }
}
