package com.clubinfo.insat.memorisia.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.activities.SettingsActivity;
import com.clubinfo.insat.memorisia.modules.Module;
import com.clubinfo.insat.memorisia.modules.OptionModule;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Class providing general utility static methods
 */
public class Utils {
    
    /**
     * Populates the logosList with the correct paths to the logos, stored in the logos folder
     *
     * @param context  Current context
     */
    public static List<String> generateLogosList(Context context) {
        final String logosPath = "logos/";
        List<String> logosList = new ArrayList<>();
        String[] list = new String[0];
        try {
            list = context.getAssets().list("logos");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.length; i++) {
            logosList.add(logosPath + list[i]);
        }
        return logosList;
    }
    
    /**
     * Checks if modules are present, if one category is empty, it creates default entries
     *
     * @param context  Current context
     */
    public static void checkModulesPresent(Context context) {
        SaveManager saver = new SaveManager(context);
        List<OptionModule> agendas = saver.getOptionModuleList(SaveManager.AGENDA);
        List<OptionModule> subjects = saver.getOptionModuleList(SaveManager.SUBJECT);
        List<OptionModule> workTypes = saver.getOptionModuleList(SaveManager.WORK_TYPE);
        
        generateDefaultModules(context, agendas.size() == 0, subjects.size() == 0, workTypes.size() == 0);
    }
    
    /**
     * Creates a default modules if none is present
     *
     * @param context  Current context
     */
    public static void generateDefaultModules(Context context, boolean isAgenda, boolean isSubject, boolean isWorkType) {
        SaveManager saver = new SaveManager(context);
        List<OptionModule> modules = new ArrayList<>();
        List<String> logosList = generateLogosList(context);
        if (isAgenda){
            modules.add(new OptionModule(-1, SaveManager.AGENDA, context.getString(R.string.default_module_home),
                    logosList.get(0), "#33b819", true));
            modules.add(new OptionModule(-1, SaveManager.AGENDA, context.getString(R.string.default_module_work),
                    logosList.get(0), "#ba4200", true));
            
        }
        if (isSubject) {
            modules.add(new OptionModule(-1, SaveManager.SUBJECT, context.getString(R.string.default_module_subject1),
                    logosList.get(0), "#d5ce12", true));
            modules.add(new OptionModule(-1, SaveManager.SUBJECT, context.getString(R.string.default_module_subject2),
                    logosList.get(0), "#1265d5", true));
        }
        if (isWorkType) {
            modules.add(new OptionModule(-1, SaveManager.WORK_TYPE, context.getString(R.string.default_module_work_type1),
                    logosList.get(0), "#d512d3", true));
            modules.add(new OptionModule(-1, SaveManager.WORK_TYPE, context.getString(R.string.default_module_work_type2),
                    logosList.get(0), "#12d5a5", true));
        }
        
        for (OptionModule m : modules){
            saver.saveModule(m);
        }
    }
    
    
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
        // Pretty ugly, removing it until finding a better one
        // DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(mLayoutManager);
        return recyclerView;
    }
    
    /**
     * Gets the difference between 2 dates in days
     *
     * @param date1 First date to compare
     * @param date2 Second date to compare
     * @return Difference in days
     */
    public static int getDateDelta(int[] date1, int[] date2) {
        if (date1[0] < 0 || date2[0] < 0)
            return 0;
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(date1[2], date1[1] - 1, date1[0]);
        end.set(date2[2], date2[1] - 1, date2[0]);
        Date startDate = start.getTime();
        Date endDate = end.getTime();
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        return (int) diffDays;
    }
    
    /**
     * Gets the difference between 2 times in minutes
     *
     * @param time1 First time to compare
     * @param time2 Second time to compare
     * @return Difference in minutes
     */
    public static int getTimeDelta(int[] time1, int[] time2) {
        if (time1[0] < 0 || time2[0] < 0)
            return 0;
        int start = time1[0] * 60 + time1[1];
        int end = time2[0] * 60 + time2[1];
        return end - start;
    }
    
    /**
     * Gets the string representation of a date array. (dd/mm/yyyy)
     *
     * @param date Date to convert
     * @return String representing the date
     */
    public static String getDateText(int[] date) {
        if (date[0] < 0)
            return "";
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
        if (time[0] < 0)
            return "";
        String hours = time[0] < 10 ? "0" + time[0] : "" + time[0];
        String minutes = time[1] < 10 ? "0" + time[1] : "" + time[1];
        return hours + ":" + minutes;
    }
    
    /**
     * Checks if a ScrollView is scrollable by comparing its height to its child.
     *
     * @param scrollView ScrollView to check
     * @return True if scrollable, false otherwise
     */
    public static boolean canScroll(ScrollView scrollView) {
        View child = scrollView.getChildAt(0);
        if (child != null) {
            int childHeight = child.getHeight();
            return scrollView.getHeight() < childHeight + scrollView.getPaddingTop() + scrollView.getPaddingBottom();
        }
        return false;
    }
}
