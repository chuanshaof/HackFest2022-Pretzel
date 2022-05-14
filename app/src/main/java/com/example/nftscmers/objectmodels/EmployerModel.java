package com.example.nftscmers.objectmodels;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class EmployerModel implements ObjectModel {
    public static final String TAG = "Employers Model";
    public static final String COLLECTION_ID = "Employers";

    public static final String EMAIL = "email";
    public static final String JOBS = "jobs";

    @DocumentId
    private String documentId;

    private String name;
    private String email;
    private String about;
    private String website;
    private String image;
    ArrayList<DocumentReference> jobs;

    private EmployerModel() {}

    public EmployerModel(String name, String email, String about, String website, String image, ArrayList<DocumentReference> jobs) {
        this.name = name;
        this.email = email;
        this.about = about;
        this.image = image;
        this.website = website;
        this.documentId = email;
        this.jobs = jobs;
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

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAbout() {
        return about;
    }

    public void setJobs(ArrayList<DocumentReference> jobs) {
        this.jobs = jobs;
    }

    public ArrayList<DocumentReference> getJobs() {
        return jobs;
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
