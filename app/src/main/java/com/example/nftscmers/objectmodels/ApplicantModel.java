package com.example.nftscmers.objectmodels;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicantModel implements ObjectModel {
    public static final String TAG = "Applicant Model";
    public static final String COLLECTION_ID = "Applicants";

    @DocumentId
    private String documentId;

    private final String name;
    private String image;
    private String about;
    private String linkedIn;
    private ArrayList<HashMap<String, Integer>> experiences;
    private ArrayList<DocumentReference> tags;
    private ArrayList<DocumentReference> courses;
    private ArrayList<DocumentReference> skills;

    public ApplicantModel(String name, String image, String about, String linkedIn,
                          ArrayList<HashMap<String, Integer>> experiences, ArrayList<DocumentReference> tags,
                          ArrayList<DocumentReference> courses, ArrayList<DocumentReference> skills) {
        this.name = name;
        this.about = about;
        this.linkedIn = linkedIn;
        this.experiences = experiences;
        this.tags = tags;
        this.courses = courses;
        this.skills = skills;
    }

    public String getName() {
        return name;
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

    public ArrayList<HashMap<String, Integer>> getExperiences() {
        return experiences;
    }

    public void setExperiences(ArrayList<HashMap<String, Integer>> experiences) {
        this.experiences = experiences;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public ArrayList<DocumentReference> getTags() {
        return tags;
    }

    public void setTags(ArrayList<DocumentReference> tags) {
        this.tags = tags;
    }

    public ArrayList<DocumentReference> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<DocumentReference> courses) {
        this.courses = courses;
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
