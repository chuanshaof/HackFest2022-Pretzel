package com.example.nftscmers.objectmodels;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class AccountModel implements ObjectModel {
    public static final String TAG = "Account Model";
    public static final String COLLECTION_ID = "Accounts";

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ACCOUNTTYPE = "accountType";
    public static final String PROFILE = "profile";

    @DocumentId
    private String documentId;

    private String email;
    private String password;
    private String accountType;
    private DocumentReference profile;

    private AccountModel() {};

    public AccountModel(String email, String password, String accountType, DocumentReference profile) {
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.profile = profile;
        this.documentId = email;
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

    public String getAccountType() {
        return accountType;
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

    @Override
    public String toString() {
        return "AccountModel{" +
                "documentId='" + documentId + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accountType='" + accountType + '\'' +
                ", profile=" + profile +
                '}';
    }
}

