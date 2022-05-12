package com.example.nftscmers.applicantactivities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftscmers.R;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";

    ImageView IV_profile_pic;
    EditText ET_name;
    EditText ET_about;
    TextView TV_skills;
    ListView LV_skills;
    Button BT_confirm;

    List<String> skills_list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_edit_profile);

//        IV_profile_pic = findViewById(R.id.iv_profile_pic);
//        ET_name = findViewById(R.id.et_name);
//        ET_about = findViewById(R.id.et_about);
//        TV_skills = findViewById(R.id.tv_skills);
//        LV_skills = findViewById(R.id.lv_skills);
//        BT_confirm = findViewById(R.id.button_confirm);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,skills_list);
        LV_skills.setAdapter(arrayAdapter);

        TV_skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
    }


    /**
     * This function shows the dialog box
     */
    void showCustomDialog() {
        final Dialog dialog = new Dialog(EditProfileActivity.this);
        //Disable default title so that the custom title "Skills" will show.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog by clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.dialog_edit_profile_skills);

        // Initializing the views of the dialog.
        EditText ET_skill_name = dialog.findViewById(R.id.et_skill_name);
        Button BT_add = dialog.findViewById(R.id.button_add);

        // TODO: update firestore and show on listview
        BT_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String skill_name = ET_skill_name.getText().toString();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
