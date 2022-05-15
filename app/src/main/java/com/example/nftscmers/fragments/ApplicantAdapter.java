package com.example.nftscmers.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.example.nftscmers.R;
import com.example.nftscmers.objectmodels.ApplicantModel;
import com.example.nftscmers.objectmodels.TestModel;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class ApplicantAdapter extends BaseAdapter {
    public static final String TAG = "YOUR-TAG-NAME";
    ArrayList<Object> item;
    public Activity context;
    public LayoutInflater inflater;
    Context c;

    public ApplicantAdapter(ArrayList<Object> item, Activity context) {

        this.item = item;
        this.context = context;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        ImageView image1;
        TextView name;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        holder = new ViewHolder();
        convertView = inflater.inflate(R.layout.item_in_cardview, null);
//        holder.image1 = (ImageView) convertView.findViewById(R.id.thisimage);
        holder.name = (TextView) convertView.findViewById(R.id.name);

        convertView.setTag(holder);

        holder = (ViewHolder) convertView.getTag();
        final TestModel n = (TestModel) item.get(position);
        holder.image1.setImageResource(n.getImage1());
        holder.name.setText(n.getName());

        holder.name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(context, n.getName(), Toast.LENGTH_SHORT).show();

            }
        });

        return convertView;

    }
}
