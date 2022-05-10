package com.example.nftscmers.utils;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;

public class LoggedInUser {

    private static final String TAG = "LoggedInUser";
    private static LoggedInUser ourInstance = null;
    DocumentReference documentReference;
    String username;

    public static LoggedInUser getInstance() {
        if (ourInstance == null)
            ourInstance = new LoggedInUser();
        return ourInstance;
    }

    private LoggedInUser() {
    }

    public void setUser(DocumentReference documentReference, String username) {
        this.documentReference = documentReference;
        this.username = username;
    }

    public DocumentReference getUserDocRef() {
        return this.documentReference;
    }

    public String getUserId() {
        Log.i(TAG, "STRING" + this.documentReference.getPath().substring(this.documentReference.getPath().lastIndexOf('/') + 1));
        return this.documentReference.getPath().substring(this.documentReference.getPath().lastIndexOf('/') + 1);
    };

    public String getUserString() {
        return this.username;
    }
}