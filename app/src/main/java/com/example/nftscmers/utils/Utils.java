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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.nftscmers.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

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

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

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
    public static void loadImage(ImageView imageView, String url) {
        if (url != null) {
            Picasso.get().load(url).transform(new CropCircleTransformation()).into(imageView);
        }
    }

    /**
     * Showing Toast and
     * @param textView a TextView object
     * @param text a String object to be set
     */
    public static void setValid(TextView textView, String text){
        if (text != null) {
            textView.setText(text);
        }
    }

    /**
     * This method checks for a valid email address format
     * @param textView a TextView object that
     */
    public static void uneditableField(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.requestFocus();
                textView.setError("Field is uneditable");
                Toast.makeText(view.getContext(), "Field is uneditable", Toast.LENGTH_SHORT).show();
            }
        });
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
        Log.w(TAG, context.getString(R.string.unexpected_error));
        Toast.makeText(context, context.getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show();
    }

    /**
     * Handling of unexpected errors
     * @param context a Context object
     * @param TAG a String object
     */
    public static void fireStoreError(Context context, String TAG){
        Log.w(TAG, context.getString(R.string.firestore_error));
        Toast.makeText(context, context.getString(R.string.firestore_error), Toast.LENGTH_SHORT).show();
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
}
