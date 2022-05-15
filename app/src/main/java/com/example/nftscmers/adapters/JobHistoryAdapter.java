package com.example.nftscmers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nftscmers.R;
import com.example.nftscmers.objectmodels.JobModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JobHistoryAdapter extends ArrayAdapter<HashMap<String, String>> {
    private final LayoutInflater mInflater;
    private final List<HashMap<String, String>> jobList;

    public JobHistoryAdapter(Context context, int resource, @NonNull ArrayList<HashMap<String, String>> jobList) {
        super(context, resource, jobList);
        this.jobList = jobList;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_job_history, parent, false);

        TextView jobPosition = view.findViewById(R.id.job_position);
        TextView jobDeadline = view.findViewById(R.id.job_deadline);

        HashMap<String, String> job = jobList.get(position);

        jobPosition.setText(job.get(JobModel.POSITION));
        jobDeadline.setText(job.get(JobModel.DEADLINE));

        return view;
    }
}
