package com.clubinfo.insat.memorisia.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.activities.SettingsActivity;

import java.io.IOException;
import java.util.Calendar;

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
     * Sets the toolbar icons color to white to match the title
     *
     * @param item The menu item that will change color
     */
    public static void setToolbarIconWhite(MenuItem item) {
        Drawable icon = item.getIcon();
        icon.mutate().setColorFilter(Color.argb(255, 255, 255, 255), PorterDuff.Mode.SRC_IN);
        item.setIcon(icon);
    }
    
    /**
     * Sets the application theme to dark based on shared preferences
     *
     * @param context Current context
     * @param hasActionBar Whether to use a style with an action bar
     */
    public static void setNightMode(Context context, boolean hasActionBar) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        if (nightMode && !hasActionBar)
            context.setTheme(R.style.AppTheme_Dark_NoActionBar);
        else if (nightMode)
            context.setTheme(R.style.AppTheme_Dark);
    }
    
    /**
     * Adds an horizontal divider to a vertical recycler view
     *
     * @param recyclerView RecyclerView to add divider to
     * @param context Current context
     * @return RecyclerView with divider
     */
    public static RecyclerView setRecyclerViewDivider(RecyclerView recyclerView, Context context) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(mLayoutManager);
        return recyclerView;
    }
    
    
    /**
     * Gets the difference between 2 arrays. (array1 - array2)
     *
     * @param array1 First array to compare
     * @param array2 Second array to compare
     * @return Array containing the difference
     */
    public static int[] getArrayDelta(int[] array1, int[] array2) {
        int[] delta = new int[array1.length];
        for (int i = 0; i < array1.length; i++){
            delta[i] = array1[i] - array2[i];
        }
        return delta;
    }
    
    /**
     * Gets the string representation of a date array. (dd/mm/yyyy)
     *
     * @param date Date to convert
     * @return String representing the date
     */
    public static String getDateText(int[] date) {
        String day = date[0] < 10 ? "0" + date[0] : "" + date[0];
        String month = date[1] < 10 ? "0" + date[1] : "" + date[1];
        return day + "/" + month + "/" + date[2];
    }
    
    /**
     * Gets the string representation of a time array. (hh:mm)
     *
     * @param time Date to convert
     * @return String representing the time
     */
    public static String getTimeText(int[] time) {
        String hours = time[0] < 10 ? "0" + time[0] : "" + time[0];
        String minutes = time[1] < 10 ? "0" + time[1] : "" + time[1];
        return hours + ":" + minutes;
    }
}
