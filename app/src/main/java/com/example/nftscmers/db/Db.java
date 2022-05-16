package com.example.nftscmers.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nftscmers.applicantactivities.ScrollJobActivity;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

abstract class Db {
    /*
        declaring Db class as abstract is so that it can't be instantiated.
     */

    private final FirebaseFirestore db;
    private final CollectionReference collection;

    public Db(String collectionName) {
        db = FirebaseFirestore.getInstance();
        this.collection = db.collection(collectionName);
    }

    public CollectionReference getCollection() {
        return this.collection;
    }

    public CollectionReference getCollection(String collectionName){
        return db.collection(collectionName);
    }

    public FirebaseFirestore getDb(){
        return this.db;
    }

    public DocumentReference getDocument(String documentName){
        return collection.document(documentName);
    }
}