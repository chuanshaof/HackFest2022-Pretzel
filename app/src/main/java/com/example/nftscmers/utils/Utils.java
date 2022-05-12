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
import android.util.Patterns;
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
import com.example.nftscmers.R;
import com.example.nftscmers.commonactivities.SignUpActivity;
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

    /**
     * This method checks if an Activity has a network connection
     * @param context a Context object (Context is the superclass of AppCompatActivity
     * @return a boolean object
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean haveNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.i(TAG, "Active Network: " + haveNetwork);

        if (haveNetwork == false) {
            Toast.makeText(context, context.getString(R.string.network_missing), Toast.LENGTH_SHORT).show();
        }
        return haveNetwork;
    }

    /**
     * This method loads an image from an url onto ImageView
     * @param url a String object that contains the image url
     * @param imageView an ImageView Object to be set for the picture
     */
    public static void loadImage(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);
    }

    /**
     * This method checks for a valid email address format
     * @param editText an EditText object filled with an email address
     * @return a boolean object
     */
    public static boolean invalidEmail(EditText editText) {
        if (!Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches()) {
            editText.setError("Please provide valid email");
            editText.requestFocus();
            Log.i(TAG, "invalidEmail");
            return true;
        }
        return false;
    }

    /**
     * Entry validation using varargs
     * https://www.c-sharpcorner.com/UploadFile/1e5156/validation/
     * @param editTexts an ArrayList of EditText objects
     * @return a boolean object
     */
    public static boolean invalidData(EditText... editTexts) {
        boolean invalid = false;
        for (EditText editText : editTexts) {
            invalid = invalid | invalidData(editText);
        }
        return invalid;
    }

    /**
     * Entry validation
     * @param editText an EditText object
     * @return a boolean object
     */
    public static boolean invalidData(EditText editText) {
        if (editText.getText().toString().length() == 0) {
            editText.requestFocus();
            editText.setError("Field cannot be empty");
            Log.i(TAG, "invalidData");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Handling of unexpected errors
     * @param context a Context object
     * @param TAG a String object
     */
    public static void unexpectedError(Context context, String TAG){
        Log.d(TAG, context.getString(R.string.unexpected_error));
        Toast.makeText(context, context.getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show();
    }

    /**
     * Showing Toast and
     * @param context a Context object
     * @param TAG a String object
     * @param message a String object
     */
    public static void toastLog(Context context, String TAG, String message){
        Log.i(TAG, message);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static String getDocumentFromPath(String documentId){
        return documentId.substring(documentId.lastIndexOf("/") + 1);
    }

    public static DocumentReference getCurrentUser(FirebaseFirestore db){
        DocumentReference user = LoggedInUser.getInstance().getUserDocRef(); // singleton
        if (user != null) {
            return user;
        }
        return null;
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
}
