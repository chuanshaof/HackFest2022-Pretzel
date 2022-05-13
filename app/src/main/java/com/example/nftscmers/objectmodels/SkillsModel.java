package com.example.nftscmers.objectmodels;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class SkillsModel implements ObjectModel {
    public static final String TAG = "Skill Model";
    public static final String COLLECTION_ID = "Skills";

    @DocumentId
    private String documentId;
    private String skill;
    private ArrayList<DocumentReference> courses;

    private SkillsModel() {}

    public SkillsModel(@NonNull String skill, ArrayList<DocumentReference> courses) {
        this.skill = skill;
        this.courses = courses;
        this.documentId = skill;
    }

    public static String getCollectionId() {
        return COLLECTION_ID;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getSkill() {
        return skill;
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

    @Override
    public String toString() {
        return "SkillsModel{" +
                "documentId='" + documentId + '\'' +
                ", courses=" + courses +
                '}';
    }
}

