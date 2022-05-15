package com.example.nftscmers.objectmodels;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class TestModel implements ObjectModel {
    public static final String TAG = "Applicant Model";
    public static final String COLLECTION_ID = "Applicants";

    public static final String EMAIL = "email";

    @DocumentId
    private String documentId;

    private String name;
    private int image1;
    private String about;
    private String linkedIn;
    private String email;
    private ArrayList<DocumentReference> tags;
    private ArrayList<DocumentReference> skills;
    private ArrayList<DocumentReference> applications;



    public static String getCollectionId() {
        return COLLECTION_ID;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage1() {
        return image1;
    }

    public void setImage1(int image1) {
        this.image1 = image1;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<DocumentReference> getTags() {
        return tags;
    }

    public void setTags(ArrayList<DocumentReference> tags) {
        this.tags = tags;
    }

    public void addTag(DocumentReference tag) {
        this.tags.add(tag);
    }

    public ArrayList<DocumentReference> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<DocumentReference> skills) {
        this.skills = skills;
    }

    public void addSkill(DocumentReference skill) {
        this.skills.add(skill);
    }

    public void setApplications(ArrayList<DocumentReference> applications) {
        this.applications = applications;
    }

    public ArrayList<DocumentReference> getApplications() {
        return applications;
    }

    @Override
    public String getDocumentId() {
        return documentId;
    }

    @Override
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}

