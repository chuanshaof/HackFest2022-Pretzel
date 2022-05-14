package com.example.nftscmers.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nftscmers.R;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class SkillsFragment extends Fragment {

    private static final String TAG = "SkillsFragment";
    private ArrayList<DocumentReference> skillsList;

    public SkillsFragment(ArrayList<DocumentReference> skillsList) {
        if (skillsList == null) {
            this.skillsList = new ArrayList<>();
        } else {
            this.skillsList = skillsList;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skills, container, false);

        ArrayList<String> skillsListString = new ArrayList<>();
        for (DocumentReference skill : skillsList) {
            skillsListString.add(skill.getId());
        }

        ListView skillsListView = view.findViewById(R.id.skills_list);
        ArrayAdapter<String> skillsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, skillsListString);

        skillsListView.setAdapter(skillsAdapter);

        return view;
    }
}
