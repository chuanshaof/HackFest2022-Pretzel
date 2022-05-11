package com.example.nftscmers.objectmodels;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CoursesModel implements ObjectModel {
    public static final String TAG = "Courses Model";
    public static final String COLLECTION_ID = "Courses";

    @DocumentId
    private String documentId;

    private final String link;
    private ArrayList<DocumentReference> skills;

    public CoursesModel(String link, ArrayList<DocumentReference> skills) {
        this.link = link;
        this.skills = skills;
    }

    public String getLink() {
        return link;
    }

    public ArrayList<DocumentReference> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<DocumentReference> skills) {
        this.skills = skills;
    }

    @Override
    public String getDocumentId() {
        return null;
    }

    @Override
    public void setDocumentId(String documentId) {

    }
}


