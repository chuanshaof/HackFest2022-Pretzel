package com.example.nftscmers.employeractivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;
import com.example.nftscmers.fragments.ApplicantAdapter;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.objectmodels.TestModel;
import com.example.nftscmers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ScrollApplicationActivity extends AppCompatActivity {

    private ArrayAdapter<Object> arrayAdapter;
//    ApplicantAdapter arrayAdapter;
    TextView name;
    TextView email;
    ImageView image;
    SwipeFlingAdapterView flingAdapterView;


    // TODO: Change the TAG
    public static final String TAG = "YOUR-TAG-NAME";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_dislike_button_bottom);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        flingAdapterView=findViewById(R.id.swipe);

        ArrayList<Object> item = new ArrayList<>();
//        item.add("welcome");


        //Iterating through list of applicant details in firebase and displaying on card swipe
        db.collection("Applicants")
                .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, document.getId() + " => " + document.getData());
                        item.add(document.getString("email") + "\n" + document.getString("about") + "\n" + document.get("skills"));
//                        Utils.loadImage(model.getImage(), thisimage);
                    arrayAdapter.notifyDataSetChanged();
                }} else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });



        arrayAdapter=new ArrayAdapter<>(ScrollApplicationActivity.this, R.layout.item_in_cardview, R.id.name, item);

        flingAdapterView.setAdapter(arrayAdapter);

        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                item.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {

                Toast.makeText(ScrollApplicationActivity.this,"dislike",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object o) {

                Toast.makeText(ScrollApplicationActivity.this,"like",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });

        flingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {
                Toast.makeText(ScrollApplicationActivity.this, "data is "+item.get(i),Toast.LENGTH_SHORT).show();
            }
        });

        Button like,dislike;

        like=findViewById(R.id.like);
        dislike=findViewById(R.id.dislike);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingAdapterView.getTopCardListener().selectRight();
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingAdapterView.getTopCardListener().selectLeft();
            }
        });

    }




}






