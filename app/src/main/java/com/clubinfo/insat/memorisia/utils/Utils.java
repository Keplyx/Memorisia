package com.clubinfo.insat.memorisia.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.activities.SettingsActivity;

import java.io.IOException;

/**
 * Class providing general utility static methods
 */
public class Utils {
    
    /**
     * Converts an image referenced by its path to a Bitmap
     *
     * @param context  Current context
     * @param filePath Path relative to the data folder
     * @return Bitmap representation of the asset
     */
    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        if (filePath == null)
            return null;
        AssetManager assetManager = context.getAssets();
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(assetManager.open(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    
    /**
     * Sets the application theme to dark based on shared preferences
     *
     * @param context Current context
     */
    public static void setNightMode(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        if (nightMode)
            context.setTheme(R.style.AppTheme_Dark_NoActionBar);
    }
    
}
