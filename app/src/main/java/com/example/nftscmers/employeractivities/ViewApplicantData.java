package com.example.nftscmers.employeractivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;


public class ViewApplicantData extends AppCompatActivity implements View.OnClickListener{
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_checks_applicant_profile);

        backArrow = findViewById(R.id.applicant_back_arrow);
        backArrow.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.applicant_back_arrow:
                startActivity(new Intent(ViewApplicantData.this, ScrollApplicationActivity.class));
        }
    }
}

