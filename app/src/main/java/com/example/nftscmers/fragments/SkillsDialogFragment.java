package com.example.nftscmers.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.nftscmers.R;
import com.example.nftscmers.adapters.SkillsAdapter;
import com.example.nftscmers.db.SkillsDb;
import com.example.nftscmers.objectmodels.SkillsModel;
import com.example.nftscmers.utils.LoggedInUser;
import com.example.nftscmers.utils.Utils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SkillsDialogFragment extends DialogFragment {
    public static String TAG = "SkillsDialogFragment";

    ArrayList<DocumentReference> skillsList;
    onConfirmListener onConfirmListener;

    public SkillsDialogFragment(ArrayList<DocumentReference> skillsList, onConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;

        if (skillsList == null) {
            this.skillsList = new ArrayList<>();
        } else {
            this.skillsList = skillsList;
        }
    }

    public interface onConfirmListener {
        void onResult(ArrayList<DocumentReference> skillsList);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_skills);
        dialog.setCanceledOnTouchOutside(false);

        ListView listView = dialog.findViewById(R.id.skills_dialog_list);
        Button confirmButton = dialog.findViewById(R.id.skills_confirm);
        EditText addText = dialog.findViewById(R.id.skills_add_text);
        Button addButton = dialog.findViewById(R.id.skills_add_button);

        ArrayAdapter arrayAdapter = new SkillsAdapter(getContext(), R.layout.dialog_item_skills, skillsList);
        listView.setAdapter(arrayAdapter);

        ((SkillsAdapter) arrayAdapter).setOnDeleteListener(new SkillsAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int position) {
                skillsList.remove(position);
                arrayAdapter.notifyDataSetChanged();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onSkillsConfirm");
                onConfirmListener.onResult(skillsList);
                dialog.dismiss();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.invalidData(addText)) {
                    return;
                } else if (addText.getText().toString() == ""){
                    // TODO: Check if added skill already exists
                } else {
                    new SkillsDb(getContext(), new SkillsDb.OnSkillsModel() {
                        @Override
                        public void onResult(SkillsModel skillsModel) {
                            Log.d(TAG, "onResult: " + skillsModel.getSkill() + " added to " + LoggedInUser.getInstance().getEmail());
                            skillsList.add(FirebaseFirestore.getInstance().collection(SkillsModel.getCollectionId()).document(skillsModel.getSkill()));
                            arrayAdapter.notifyDataSetChanged();
                            addText.setText("");
                        }
                    }).getSkillsModel(addText.getText().toString());
                }
            }
        });

        return dialog;
    }
}
