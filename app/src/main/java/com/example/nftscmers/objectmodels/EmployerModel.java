package com.example.nftscmers.objectmodels;

import com.google.firebase.firestore.DocumentId;

public class EmployerModel implements ObjectModel {
    public static final String TAG = "Employers Model";
    public static final String COLLECTION_ID = "Employers";

    public static final String EMAIL = "Email";

    @DocumentId
    private String documentId;

    private String name;
    private String email;
    private String about;
    private String picture;

    private EmployerModel() {}

    public EmployerModel(String name, String email, String about, String picture) {
        this.name = name;
        this.email = email;
        this.about = about;
        this.picture = picture;
        this.documentId = email;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAbout() {
        return about;
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
