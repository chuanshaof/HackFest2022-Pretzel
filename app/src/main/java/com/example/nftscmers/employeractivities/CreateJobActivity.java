package com.example.nftscmers.employeractivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

public class CreateJobActivity extends AppCompatActivity {
    private static final String TAG = "CreateJobActivity";
    FirebaseFirestore db;

    // UI Elements
    ImageView back_button;
    TextView create_job;
    TextView employer_name;
    EditText edit_job;
    EditText edit_job_description;
    EditText edit_job_deadline;
    EditText edit_job_location;
    TextView edit_skills;
    TextView edit_tags;

    Button confirm_button;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_create_job);

        // initialise Firestore db
        db = FirebaseFirestore.getInstance();

        // initialise UI elements
        back_button = findViewById(R.id.back_button);
        create_job = findViewById(R.id.create_job);
        employer_name = findViewById(R.id.employer_name);
        edit_job = findViewById(R.id.edit_job);
        edit_job_description = findViewById(R.id.edit_job_description);
        edit_job_deadline = findViewById(R.id.edit_job_deadline);
        edit_job_location = findViewById(R.id.edit_job_location);
        edit_skills = findViewById(R.id.edit_skills);
        edit_tags = findViewById(R.id.edit_tags);
        confirm_button = findViewById(R.id.confirm_button);

        // initialise buttons
        //TODO: Back Button -> Go back to previous page
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateJobActivity.this, ViewApplicationActivity.class);
                startActivity(intent);
            }
        });

        edit_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        edit_job_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        edit_job_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        edit_job_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        edit_skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        edit_tags.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

             }
         });

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }
    }

