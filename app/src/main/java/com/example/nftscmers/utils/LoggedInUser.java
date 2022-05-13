package com.example.nftscmers.utils;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;

public class LoggedInUser {
    private static final String TAG = "LoggedInUser";
    private static LoggedInUser ourInstance = null;
    DocumentReference documentReference;
    String email;

    public static LoggedInUser getInstance() {
        if (ourInstance == null)
            ourInstance = new LoggedInUser();
        return ourInstance;
    }

    private LoggedInUser() {
    }

    public void setUser(DocumentReference documentReference, String email) {
        this.documentReference = documentReference;
        this.email = email;
    }

    public DocumentReference getUserDocRef() {
        return documentReference;
    }

    public String getEmail() {
        return email;
    }
}