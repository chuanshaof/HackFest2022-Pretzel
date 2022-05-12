package com.example.nftscmers.db;

import com.example.nftscmers.objectmodels.EmployerModel;

public class EmployerDb {
    private static final String TAG = "EmployersDb";
    private static OnEmployerModelSuccess onEmployerModelSuccess;
    private static OnEmployerUploadSuccess onEmployerUploadSuccess;
    private static OnEmployerUploadFailure onEmployerUploadFailure;


    public interface OnEmployerModelSuccess{
        void onResult(EmployerModel EmployerModel);
    }

    public interface OnEmployerUploadSuccess{
        void onResult();
    }

    public interface OnEmployerUploadFailure{
        void onResult();
    }

}
