package com.example.nftscmers.employeractivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.objectmodels.ApplicationModel;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {
    private static final String TAG = "employeractivities.FeedbackActivity";

    EditText ET_feedback;
    Button BT_submit;

    DocumentReference application_docRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_feedback);

        ET_feedback = findViewById(R.id.et_feedback);
        BT_submit = findViewById(R.id.button_submit);

        // TODO: pass the ApplicantionModel of the Application from ViewApplication activity to here via intent
        // TODO: pass the Document Reference of the Application from ViewApplication activity to here via intent
        Intent intent = getIntent();

        if (intent != null ) {
            application_docRef = (DocumentReference) intent.getSerializableExtra("DocRef");
        }

        // When user clicks submit button
        ET_feedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable feedback) {
                BT_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: Update firebase
                        Map<String, Object> feedback_map = new HashMap<>();
                        feedback_map.put("feedback", feedback.toString());
                        application_docRef.update(feedback_map);
                    }
                });
            }
        });
    }
}
