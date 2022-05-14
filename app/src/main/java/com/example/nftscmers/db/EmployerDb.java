package com.example.nftscmers.db;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nftscmers.R;
import com.example.nftscmers.objectmodels.EmployerModel;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class EmployerDb extends Db {
    private static final String TAG = "EmployersDb";
    private static OnEmployerModel onEmployerModel;
    private static OnEmployerUploadSuccess onEmployerUploadSuccess;
    private static OnEmployerUploadFailure onEmployerUploadFailure;

    public EmployerDb() {
        super(EmployerModel.getCollectionId());
    }

    public EmployerDb(OnEmployerModel onEmployerModelSuccess) {
        super(EmployerModel.getCollectionId());
        this.onEmployerModel = onEmployerModelSuccess;
    }

    public EmployerDb(EmployerDb.OnEmployerUploadSuccess onEmployerUploadSuccess) {
        super(EmployerModel.getCollectionId());
        this.onEmployerUploadSuccess = onEmployerUploadSuccess;
    }

    public EmployerDb(EmployerDb.OnEmployerUploadSuccess onEmployerUploadSuccess, EmployerDb.OnEmployerUploadFailure onEmployerUploadFailure) {
        super(EmployerModel.getCollectionId());
        this.onEmployerUploadSuccess = onEmployerUploadSuccess;
        this.onEmployerUploadFailure = onEmployerUploadFailure;
    }

    /**
     * Get EmployerModel from an email address
     * @param context a Context object
     * @param email an EditText object containing an email address
     */
    public void getEmployerModel(Context context, String email) {
        if (!Utils.isNetworkAvailable(context)) {
            onEmployerModel.onResult(null);
            return;
        }

        getDocument(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                EmployerModel EmployerModel = documentSnapshot.toObject(EmployerModel.class);
                onEmployerModel.onResult(EmployerModel);
            }
        });
    }

    /**
     * Get EmployerModel from an email address
     * @param context a Context object
     * @param email an EditText object containing an email address
     */
    public void getEmployerModel(Context context, EditText email) {
        if (Utils.invalidData(email)){
            onEmployerModel.onResult(null);
            return;
        }
        getEmployerModel(context, email.getText().toString());
    }

    /**
     * Create new account for Employers using only email with no details yet
     * @param context a Context object
     * @param email an EditText object containing an email address
     */
    public void newProfile(Context context, EditText email) {
        if (Utils.invalidData(email)){
            onEmployerUploadFailure.onResult();
            return;
        }

        getDocument(email.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i(TAG, context.getString(R.string.duplicate_email));
                        onEmployerUploadFailure.onResult();
                    } else {
                        Map<String, Object> account = new HashMap<>();
                        account.put(EmployerModel.EMAIL, email.getText().toString());

                        getCollection().document(email.getText().toString()).set(account)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        onEmployerUploadSuccess.onResult();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, context.getString(R.string.firestore_error), e);
                                        onEmployerUploadFailure.onResult();
                                    }
                                });
                    }
                } else {
                    Log.w(TAG, context.getString(R.string.firestore_error));
                    onEmployerUploadFailure.onResult();
                }
            }
        });
    }


    public interface OnEmployerModel {
        void onResult(EmployerModel EmployerModel);
    }

    public interface OnEmployerUploadSuccess{
        void onResult();
    }

    public interface OnEmployerUploadFailure{
        void onResult();
    }

}
