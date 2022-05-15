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
import com.example.nftscmers.objectmodels.ApplicationModel;

import java.util.ArrayList;
import java.util.List;

public class ApplicationHistoryAdapter extends ArrayAdapter<ApplicationModel> {
    private final LayoutInflater mInflater;
    private final List<ApplicationModel> applicationModelList;

    public ApplicationHistoryAdapter(Context context, int resource, @NonNull ArrayList<ApplicationModel> applicationModelList) {
        super(context, resource, applicationModelList);
        this.applicationModelList = applicationModelList;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_application_history, parent, false);

        TextView applicationCompany = view.findViewById(R.id.application_company);
        TextView applicationPosition = view.findViewById(R.id.application_position);
        TextView applicationStatus = view.findViewById(R.id.application_status);

        return view;
    }
}
