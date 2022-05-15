package com.example.nftscmers.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.nftscmers.R;
import com.example.nftscmers.db.SkillsDb;
import com.example.nftscmers.objectmodels.SkillsModel;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class CourseDialogFragment extends DialogFragment {
    public static String TAG = "CourseDialogFragment";

    ArrayList<String> courses;

    public CourseDialogFragment(ArrayList<String> courses) {
        if (courses == null) {
            this.courses = new ArrayList<>();
            this.courses.add(getString(R.string.no_courses));
        } else {
            this.courses = courses;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_courses);

        ListView listView = dialog.findViewById(R.id.course_list);
        Button close = dialog.findViewById(R.id.course_close);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1, courses);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(courses.get(i))));
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
