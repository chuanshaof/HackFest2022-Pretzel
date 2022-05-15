package com.example.nftscmers.objectmodels;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.xml.validation.Validator;

/*
 * Firebase Firestore Document Object Model for the ApplicationsModel Collection
 * @ID documentId: string
 *
 * @field application: DocumentReference from Applications Collection
 * @field feedback: String
 * @field job: DocumentReference from Jobs Collection
 * @field status: String
 */

public class ApplicationModel implements ObjectModel {
    public static final String TAG = "Application Model";
    public static final String COLLECTION_ID = "Applications";

    public static final String ACCEPTED = "Accepted";
    public static final String REJECTED = "Rejected";
    public static final String PENDING = "Pending";

    @DocumentId
    private String documentId;

    private DocumentReference applicant;
    private String feedback;
    private DocumentReference job;
    private String status;
    private ArrayList<DocumentReference> feedbackSkills;

    public ApplicationModel(DocumentReference applicant, DocumentReference job, String feedback, ArrayList<DocumentReference> feedbackSkills, String status) {
        this.applicant = applicant;
        this.feedback = feedback;
        this.job = job;
        this.status = status;
        this.feedbackSkills = feedbackSkills;
    }

    public static String getCollectionId() {
        return COLLECTION_ID;
    }

    public static String getTAG() {
        return TAG;
    }

    public DocumentReference getApplicant() {
        return applicant;
    }

    public DocumentReference getJob() {
        return job;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStatus() {
        return status;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<DocumentReference> getFeedbackSkills() {
        return feedbackSkills;
    }

    public void setFeedbackSkills(ArrayList<DocumentReference> feedbackSkills) {
        this.feedbackSkills = feedbackSkills;
    }

    @Override
    public String getDocumentId() {
        return documentId;
    }

    @Override
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "documentId='" + documentId + '\'';
    }
}

