package com.example.nftscmers.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.nftscmers.R;
import com.google.firebase.firestore.DocumentReference;

public class FeedbackFragment extends DialogFragment {
    private static final String TAG = "FeedbackFragment";
    public static final String VIEW = "VIEW FEEDBACK";
    public static final String EDIT = "EDIT FEEDBACK";

    DocumentReference job;
    String purpose;

    public FeedbackFragment(DocumentReference job, String purpose) {
        this.job = job;
        this.purpose = purpose;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_feedback);

        TextView feedbackTitle = dialog.findViewById(R.id.feedback_title);
        EditText feedback = dialog.findViewById(R.id.feedback);
        TextView feedbackSkills = dialog.findViewById(R.id.feedback_skills);

        return dialog;


    }
}
