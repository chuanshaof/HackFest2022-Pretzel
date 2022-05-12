package com.example.nftscmers.objectmodels;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class AccountModel implements ObjectModel {
    public static final String TAG = "Account Model";
    public static final String COLLECTION_ID = "Accounts";

    @DocumentId
    private String documentId;

    private String email;
    private String password;
    private DocumentReference profile;

    private AccountModel() {};

    public AccountModel(String email, String password, DocumentReference profile) {
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    public static String getCollectionId() {
        return COLLECTION_ID;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DocumentReference getProfile() {
        return profile;
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

