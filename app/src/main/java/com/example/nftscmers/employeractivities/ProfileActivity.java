package com.example.nftscmers.employeractivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.applicantactivities.ScrollJobActivity;
import com.example.nftscmers.commonactivities.LoginActivity;
import com.example.nftscmers.db.EmployerDb;
import com.example.nftscmers.fragments.SkillsFragment;
import com.example.nftscmers.fragments.YesNoDialogFragment;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.utils.Globals;
import com.example.nftscmers.utils.LoggedInUser;
import com.example.nftscmers.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = "EmployerProfile";

    ImageView back;
    ImageView editProfile;
    ImageView profilePic;
    TextView name;
    TextView email;
    TextView about;
    TextView linkedIn;
    Button logout;

    EmployerModel employer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_profile);

        back = findViewById(R.id.employer_back_arrow);
        editProfile = findViewById(R.id.employer_edit_profile);
        profilePic = findViewById(R.id.employer_profile_pic);
        name = findViewById(R.id.employer_name);
        email = findViewById(R.id.employer_email);
        about = findViewById(R.id.employer_about);
        linkedIn = findViewById(R.id.employer_linkedIn);
        logout = findViewById(R.id.button_logout);

        // Loading of previous employer data
        new EmployerDb(ProfileActivity.this, new EmployerDb.OnEmployerModel() {
            @Override
            public void onResult(EmployerModel employerModel) {
                Log.d(TAG, "onResult: " + employerModel);
                employer = employerModel;

                Utils.loadImage(profilePic, employer.getImage());
                Utils.setValid(name, employer.getName());
                Utils.setValid(about, employer.getAbout());
                Utils.setValid(linkedIn, employer.getWebsite());
                Utils.setValid(email, employer.getEmail());
            }
        }).getEmployerModel(getIntent().getStringExtra(TAG));

        if (!getIntent().getStringExtra(TAG).equals(LoggedInUser.getInstance().getEmail())){
            Utils.disableButton(editProfile);
            Utils.disableButton(logout);
        }

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new YesNoDialogFragment("Confirm Logout?", new YesNoDialogFragment.OnClickListener() {
                    @Override
                    public void onResult(boolean bool) {
                        if (bool == true) {
                            LoggedInUser.getInstance().setUser(null, null, null);
                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                }).show(getSupportFragmentManager(), TAG);
            }
        });

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(), JobHistoryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), ScrollApplicationActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });
    }
}
