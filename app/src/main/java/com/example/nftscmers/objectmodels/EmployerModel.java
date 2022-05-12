package com.example.nftscmers.objectmodels;

import com.google.firebase.firestore.DocumentId;

public class EmployerModel implements ObjectModel {
    public static final String TAG = "Employers Model";
    public static final String COLLECTION_ID = "Employers";

    @DocumentId
    private String documentId;

    private final String name;
    private String about;
    private String picture;

    public EmployerModel(String name, String about, String picture) {
        this.name = name;
        this.about = about;
        this.picture = picture;
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
