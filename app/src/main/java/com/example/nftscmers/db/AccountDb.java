package com.example.nftscmers.db;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nftscmers.R;
import com.example.nftscmers.commonactivities.SignUpActivity;
import com.example.nftscmers.objectmodels.AccountModel;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AccountDb extends Db{
    private static final String TAG = "AccountsDb";
    private static onAccountSuccess onAccountSuccess;
    private static onAccountFailure onAccountFailure;

    public AccountDb() {
        super(AccountModel.getCollectionId());
    }

    public AccountDb(onAccountSuccess onAccountSuccess, onAccountFailure onAccountFailure) {
        super(AccountModel.getCollectionId());
        this.onAccountSuccess = onAccountSuccess;
        this.onAccountFailure = onAccountFailure;
    }

    /**
     * Create new Account using email and password
     * @param context a Context object
     * @param email an EditText object containing an email address
     * @param password a String object containing a password
     * @return null
     */
    public void createAccount(Context context, EditText email, EditText password) {
        if (Utils.invalidData(email)){
            onAccountFailure.onResult();
            return;
        }

        getDocument(email.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        email.requestFocus();
                        email.setError(context.getString(R.string.duplicate_email));
                        Log.i(TAG, context.getString(R.string.duplicate_email));
                        onAccountFailure.onResult();
                    } else {
                        Map<String, Object> account = new HashMap<>();
                        account.put("email", email.getText().toString());
                        account.put("password", password.getText().toString());
                        account.put("profile", null);

                        getCollection().document(email.getText().toString())
                                .set(account)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Utils.toastLog(context, TAG, context.getString(R.string.sign_up_success));
                                        onAccountSuccess.onResult();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, context.getString(R.string.firestore_error), e);
                                        onAccountFailure.onResult();
                                    }
                                });
                    }
                } else {
                    Utils.unexpectedError(context, TAG);
                    onAccountFailure.onResult();
                }
            }
        });
    }

    /**
     * Log into Account using email and password
     * @param context a Context object
     * @param email an EditText object containing an email address
     * @param password a String object containing a password
     * @return null
     */
    public void loginAccount(Context context, EditText email, String password) {
        return;
    }

    public interface onAccountSuccess{
        void onResult();
    }

    public interface onAccountFailure{
        void onResult();
    }

}
