package com.example.nftscmers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.nftscmers.R;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class SkillsAdapter extends ArrayAdapter<DocumentReference> {
    private final LayoutInflater mInflater;
    private final List<DocumentReference> skillsList;
    private OnDeleteListener onDeleteListener;

    public SkillsAdapter(Context context, int resource, @NonNull ArrayList<DocumentReference> skillsList) {
        super(context, resource, skillsList);
        this.skillsList = skillsList;
        mInflater = LayoutInflater.from(context);
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = mInflater.inflate(R.layout.dialog_item_skills, parent, false);
        TextView skillName = view.findViewById(R.id.skill_name);
        DocumentReference skill = skillsList.get(position);
        Button deleteSkill = view.findViewById(R.id.skill_delete);
        skillName.setText(skill.getId());

        deleteSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteListener.onDelete(position);
            }
        });

        return view;
    }

    public interface OnDeleteListener {
        void onDelete(int position);
    }
}
