package com.example.nftscmers.db;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nftscmers.R;
import com.example.nftscmers.objectmodels.SkillsModel;
import com.example.nftscmers.objectmodels.SkillsModel;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SkillsDb extends Db {
    private static final String TAG = "SkillsDb";
    private static OnSkillsModel onSkillsModel;
    private Context context;

    private SkillsDb() {
        super(SkillsModel.getCollectionId());
    }

    public SkillsDb(Context context, OnSkillsModel onSkillsModel) {
        super(SkillsModel.getCollectionId());
        this.context = context;
        this.onSkillsModel = onSkillsModel;
    }

    public interface OnSkillsModel {
        void onResult(SkillsModel skillsModel);
    }

    /**
     * Get SkillsModel from skill name
     * @param skill a String Object
     */
    public void getSkillsModel(String skill) {
        getSkillsModel(getDocument(skill));
    }

    /**
     * Get SkillsModel from skill reference
     * @param skillReference a DocumentReference
     */
    public void getSkillsModel(DocumentReference skillReference) {
        if (!Utils.isNetworkAvailable(context)) {
            onSkillsModel.onResult(null);
            return;
        }

        skillReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                SkillsModel skillsModel = documentSnapshot.toObject(SkillsModel.class);

                if (skillsModel == null) {
                    createNewSkill(skillReference.getId());
                } else {
                    Log.i(TAG, "onSuccess: " + skillReference.getId());
                    onSkillsModel.onResult(skillsModel);
                }
            }
        });
    }


    /**
     * Get SkillsModel from skill name
     * @param skill an EditText object
     */
    public void getSkillsModel(EditText skill) {
        if (Utils.invalidData(skill)){
            onSkillsModel.onResult(null);
            return;
        }
        getSkillsModel(skill.getText().toString());
    }

    /**
     * Create a new SkillsModel with skill name
     * @param skill an EditText object
     */
    public void createNewSkill(String skill) {
        if (!Utils.isNetworkAvailable(context)) {
            onSkillsModel.onResult(null);
            return;
        }

        SkillsModel skillsModel = new SkillsModel(skill, null);
        FirebaseFirestore.getInstance().collection(SkillsModel.getCollectionId()).document(skill).set(skillsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i(TAG, "on Document Create: " + skillsModel.getSkill());
                onSkillsModel.onResult(skillsModel);
            }
        });
    }

    /**
     * Create a new SkillsModel with skill name
     * @param skill an EditText object
     */
    public void createNewSkill(EditText skill) {
        if (Utils.invalidData(skill)){
            onSkillsModel.onResult(null);
            return;
        }
        getSkillsModel(skill.getText().toString());
    }
}
