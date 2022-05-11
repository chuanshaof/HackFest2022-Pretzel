package com.example.nftscmers.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

//import com.example.nftscmers.MainPageActivity;
//import com.example.nftscmers.R;
//import com.example.nftscmers.fragments.DatePickerDialogFragment;
//import com.example.nftscmers.fragments.TimePickerDialogFragment;
//import com.example.nftscmers.interfaces.CustomDialogInterface;
//import com.example.nftscmers.objectmodel.EventModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

public class Utils {

    private static final String TAG = "UTILS";

    // Global app dateFormat style
    public static final DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM @ hh:mm aa");


    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean haveNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.i(TAG, "Active Network: " + haveNetwork);
        return haveNetwork;
    }

    public static Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }

    public static void loadImage(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);
    }


    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    public static String getDocumentFromPath(String documentId){
        return documentId.substring(documentId.lastIndexOf("/") + 1);
    }

    public static DocumentReference getCurrentUser(FirebaseFirestore db){
        DocumentReference user = LoggedInUser.getInstance().getUserDocRef(); // singleton
        if (user != null) {
            return user;
        }
        user = db.document("/Users/Test4");
        return user;
    }

    /**
     * Entry validation using varargs (found online)
     * https://www.c-sharpcorner.com/UploadFile/1e5156/validation/
     */
    public static boolean invalidData(EditText... editTexts) {
        boolean invalid = false;
        for (EditText editText : editTexts) {
            invalid = invalid | invalidData(editText);
        }
        return invalid;
    }

    public static boolean invalidData(EditText editText) {
        if (editText.getText().toString().length() == 0) {
            editText.requestFocus();
            editText.setError("Field cannot be empty");
            return true;
        } else {
            return false;
        }
    }

    public static boolean invalidData(ArrayList<EditText> editTextArrayList){
        boolean invalid = false;
        for (EditText editText : editTextArrayList) {
            invalid = invalid | invalidData(editText);
        }
        return invalid;
    }

    public static void disableButton(MaterialButton button){
        button.setEnabled(false);
        button.setClickable(false);
        button.setVisibility(View.GONE);
        Log.d(TAG, "button : " + button + " disabled");
    }

    public static void disableButton(Button button){
        button.setEnabled(false);
        button.setClickable(false);
        button.setVisibility(View.GONE);
        Log.d(TAG, "button : " + button + " disabled");
    }

    public static void enableButton(MaterialButton button){
        button.setEnabled(true);
        button.setClickable(true);
        button.setVisibility(View.VISIBLE);
    }
}
