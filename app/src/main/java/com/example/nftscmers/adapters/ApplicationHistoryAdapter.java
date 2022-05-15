package com.example.nftscmers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nftscmers.R;
import com.example.nftscmers.objectmodels.ApplicationModel;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplicationHistoryAdapter extends ArrayAdapter<HashMap<String, String>>{
    private final LayoutInflater mInflater;
    private final List<HashMap<String, String>> applicationList;

    OnItemClickListener onItemClickListener;

    public ApplicationHistoryAdapter(Context context, int resource, @NonNull ArrayList<HashMap<String, String>> applicationList, OnItemClickListener onItemClickListener) {
        super(context, resource, applicationList);
        this.applicationList = applicationList;
        mInflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onResult(int position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_application_history, parent, false);

        TextView applicationCompany = view.findViewById(R.id.application_company);
        TextView applicationPosition = view.findViewById(R.id.application_position);
        TextView applicationStatus = view.findViewById(R.id.application_status);

        HashMap<String, String> application = applicationList.get(position);

        applicationCompany.setText(application.get(ApplicationModel.COMPANY));
        applicationPosition.setText(application.get(ApplicationModel.POSITION));
        applicationStatus.setText(application.get(ApplicationModel.STATUS));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onResult(position);
            }
        });

        return view;
    }
}
