package com.example.nftscmers.objectmodels;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ApplicantModel implements ObjectModel {
    public static final String TAG = "Applicant Model";
    public static final String COLLECTION_ID = "Applicants";

    public static final String EMAIL = "email";
    public static final String NAME = "name";

    @DocumentId
    private String documentId;

    private String name;
    private String image;
    private String about;
    private String linkedIn;
    private String email;
    private ArrayList<DocumentReference> tags;
    private ArrayList<DocumentReference> skills;
    private ArrayList<DocumentReference> applications;

    private ApplicantModel() {};

    public ApplicantModel(String name, String image, String about, String linkedIn, String email,
                          ArrayList<DocumentReference> tags, ArrayList<DocumentReference> skills,
                          ArrayList<DocumentReference> applications) {
        this.name = name;
        this.image = image;
        this.about = about;
        this.linkedIn = linkedIn;
        this.email = email;
        this.tags = tags;
        this.skills = skills;
        this.documentId = email;
        this.applications = applications;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
