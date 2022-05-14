package com.example.nftscmers.employeractivities;

import android.content.Intent;
import android.net.Uri;
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
import com.example.nftscmers.db.EmployerDb;
import com.example.nftscmers.fragments.CropDialogFragment;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.utils.LoggedInUser;
import com.example.nftscmers.utils.Utils;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "Employer Edit Profile";

    ImageView profilePic;
    EditText name;
    TextView email;
    EditText about;
    EditText website;
    Button confirm;

    EmployerModel employer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_edit_profile);

        profilePic = findViewById(R.id.employer_profile_pic);
        name = findViewById(R.id.employer_name);
        email = findViewById(R.id.employer_email);
        about = findViewById(R.id.employer_about);
        website = findViewById(R.id.employer_website);

        confirm = findViewById(R.id.employer_edit_confirm);

        Utils.uneditableField(email);

        // Loading of previous employer data
        new EmployerDb(EditProfileActivity.this, new EmployerDb.OnEmployerModel() {
            @Override
            public void onResult(EmployerModel employerModel) {
                Log.d(TAG, "onResult: " + employerModel);
                employer = employerModel;

                Utils.loadImage(profilePic, employer.getImage());
                Utils.setValid(name, employer.getName());
                Utils.setValid(about, employer.getAbout());
                Utils.setValid(website, employer.getWebsite());
                Utils.setValid(email, employer.getEmail());
            }
        }).getEmployerModel(LoggedInUser.getInstance().getEmail());

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: test");
                new CropDialogFragment(new CropDialogFragment.OnCropListener() {
                    @Override
                    public void onResult(Uri uri) {
                        new EmployerDb(EditProfileActivity.this, new EmployerDb.OnEmployerUploadSuccess() {
                            @Override
                            protected void onResult(String string) {
                                Utils.loadImage(profilePic, string);
                                employer.setImage(string);
                            }
                        }).updateProfilePicture(uri, employer.getEmail());
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

                employer.setName(name.getText().toString());
                employer.setAbout(about.getText().toString());
                employer.setWebsite(website.getText().toString());

                new EmployerDb(EditProfileActivity.this, new EmployerDb.OnEmployerUploadSuccess() {
                    @Override
                    protected void onResult() {
                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                }, new EmployerDb.OnEmployerUploadFailure() {
                    @Override
                    public void onResult() {
                        Utils.fireStoreError(EditProfileActivity.this, TAG);
                    }
                }).updateProfile(employer);
            }
        });
    }
}
