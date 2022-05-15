package com.example.nftscmers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nftscmers.R;
import com.example.nftscmers.objectmodels.JobModel;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class JobHistoryAdapter extends ArrayAdapter<DocumentReference> {
    private final LayoutInflater mInflater;
    private final List<DocumentReference> jobModelList;

    public JobHistoryAdapter(Context context, int resource, @NonNull ArrayList<DocumentReference> jobModelList) {
        super(context, resource, jobModelList);
        this.jobModelList = jobModelList;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_job_history, parent, false);

        return view;
    }
}
