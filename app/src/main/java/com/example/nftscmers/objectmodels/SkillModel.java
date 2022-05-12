package com.example.nftscmers.objectmodels;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class SkillModel implements ObjectModel {
    public static final String TAG = "Skill Model";
    public static final String COLLECTION_ID = "Skill";

    @DocumentId
    private String documentId;
    private ArrayList<DocumentReference> courses;

    SkillModel(ArrayList<DocumentReference> courses) {
        this.courses = courses;
    }

    public String getCollectionId() {
        return COLLECTION_ID;
    }

    public static String getTAG() {
        return TAG;
    }

    public ArrayList<DocumentReference> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<DocumentReference> courses) {
        this.courses = courses;
    }

    public void addCourses(DocumentReference course) {
        this.courses.add(course);
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

