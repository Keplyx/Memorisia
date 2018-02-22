/*
 * Copyright (c) 2018.
 * This file is part of Memorisia.
 *
 * Memorisia is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Memorisia is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Memorisia.  If not, see <http://www.gnu.org/licenses/>.
 */

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.activities.MainActivity;
import com.clubinfo.insat.memorisia.activities.SettingsActivity;
import com.clubinfo.insat.memorisia.database.MemorisiaDatabase;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        for (String s : list) {
            logosList.add(logosPath + s);
        }
        return logosList;
    }
    
    /**
     * Checks if modules are present, if one category is empty, it creates default entries
     *
     * @param context  Current context
     */
    public static void checkModulesPresent(Context context) {
        MemorisiaDatabase db = MemorisiaDatabase.getInstance(context);
        List<OptionModule> agendas = db.optionModuleDao().getOptionModulesOfType(OptionModule.AGENDA);
        List<OptionModule> subjects = db.optionModuleDao().getOptionModulesOfType(OptionModule.SUBJECT);
        List<OptionModule> workTypes = db.optionModuleDao().getOptionModulesOfType(OptionModule.WORK_TYPE);
        if (agendas.size() == 0 && subjects.size() == 0 && workTypes.size() == 0)
            generateDefaultModules(context);
    }

    /**
     * Creates a default modules if none is present
     *
     * @param context  Current context
     */
    private static void generateDefaultModules(Context context) {
        MemorisiaDatabase db = MemorisiaDatabase.getInstance(context);
        List<OptionModule> modules = new ArrayList<>();
        List<String> logosList = generateLogosList(context);
        List<Integer> moduleIds = new ArrayList<>();
        
        modules.add(new OptionModule(OptionModule.AGENDA, context.getString(R.string.default_module_home),
                logosList.get(7), "#33b819")); // 0
        modules.add(new OptionModule(OptionModule.AGENDA, context.getString(R.string.default_module_work),
                logosList.get(7), "#ba4200")); // 1
        
        modules.add(new OptionModule(OptionModule.SUBJECT, context.getString(R.string.default_module_start),
                logosList.get(3), "#d5ce12")); // 2
        modules.add(new OptionModule(OptionModule.SUBJECT, context.getString(R.string.default_module_tuto),
                logosList.get(3), "#1265d5")); // 3
    

        modules.add(new OptionModule(OptionModule.WORK_TYPE, context.getString(R.string.default_module_important),
                logosList.get(11), "#d512d3")); // 4
        modules.add(new OptionModule(OptionModule.WORK_TYPE, context.getString(R.string.default_module_misc),
                logosList.get(11), "#12d5a5")); // 5
        
        for (int i = 0; i < modules.size(); i++){
            moduleIds.add((int)db.optionModuleDao().insertOptionModules(modules.get(i))[0]);
            if (modules.get(i).getType() == OptionModule.AGENDA) {
                MainActivity act = (MainActivity) context;
                act.addAgendaToSelected(moduleIds.get(i));
            }
        }
        
        List<WorkModule> workModuleList = new ArrayList<>();
        workModuleList.add(new WorkModule(moduleIds.get(1), moduleIds.get(2), moduleIds.get(4), 5, new int[]{-1, -1, -1}, new int[]{-1, -1},
                context.getString(R.string.default_work_drawer), false));
        workModuleList.add(new WorkModule(moduleIds.get(1), moduleIds.get(2), moduleIds.get(4), 5, new int[]{-1, -1, -1}, new int[]{-1, -1},
                context.getString(R.string.default_work_add), false));
    
        workModuleList.add(new WorkModule(moduleIds.get(1), moduleIds.get(3), moduleIds.get(4), 3, new int[]{-1, -1, -1}, new int[]{-1, -1},
                context.getString(R.string.default_work_edit), false));
        workModuleList.add(new WorkModule(moduleIds.get(1), moduleIds.get(3), moduleIds.get(4), 3, new int[]{-1, -1, -1}, new int[]{-1, -1},
                context.getString(R.string.default_work_delete), false));
        workModuleList.add(new WorkModule(moduleIds.get(1), moduleIds.get(3), moduleIds.get(4), 3, new int[]{-1, -1, -1}, new int[]{-1, -1},
                context.getString(R.string.default_work_date), false));
        workModuleList.add(new WorkModule(moduleIds.get(1), moduleIds.get(3), moduleIds.get(4), 3, new int[]{-1, -1, -1}, new int[]{-1, -1},
                context.getString(R.string.default_work_prio), false));
        
        workModuleList.add(new WorkModule(moduleIds.get(1), moduleIds.get(3), moduleIds.get(5), 0, new int[]{-1, -1, -1}, new int[]{-1, -1},
                context.getString(R.string.default_work_commands), false));
        workModuleList.add(new WorkModule(moduleIds.get(1), moduleIds.get(3), moduleIds.get(5), 0, new int[]{-1, -1, -1}, new int[]{-1, -1},
                context.getString(R.string.default_work_settings), false));
        workModuleList.add(new WorkModule(moduleIds.get(1), moduleIds.get(3), moduleIds.get(5), 0, new int[]{-1, -1, -1}, new int[]{-1, -1},
                context.getString(R.string.default_work_night), false));
    
        for (WorkModule w : workModuleList){
            db.workModuleDao().insertWorkModules(w);
        }

    }
    
    
    /**
     * Checks if works are outdated, based on preferences. If yes, they are deleted
     *
     * @param context  Current context
     */
    public static void checkWorkOutdated(Context context) {
        MemorisiaDatabase db = MemorisiaDatabase.getInstance(context);
        List<WorkModule> workModuleList = db.workModuleDao().getAllWorkModules();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPref.getBoolean(SettingsActivity.KEY_DELETE_OLD, false)) {
            Calendar limit = getOutdatedWorkLimit(context);
            for (WorkModule m : workModuleList) {
                Calendar current = m.getDateAsCalendar();
                if (current != null && limit.after(current)){
                    if (!sharedPref.getBoolean(SettingsActivity.KEY_DELETE_OLD_ONLY_DONE, false) || m.isState()) {
                        db.workModuleDao().deleteWorkModules(m);
                    }
                }
            }
        }
    }
    
    /**
     * Gets the outdated limit from preferences as a Calendar
     *
     * @param context  Current context
     * @return Calender representing the date limit
     */
    private static Calendar getOutdatedWorkLimit(Context context) {
        Calendar limit = Calendar.getInstance();
        String selected = PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsActivity.KEY_DELETE_OLD_TIME, "");
        String[] aliasArray = context.getResources().getStringArray(R.array.delete_old_time_range_alias);
        for (int i = 0; i <= 3; i++) {
            if (selected.equals(aliasArray[i]))
                limit.add(Calendar.DAY_OF_MONTH, -i);
        }
        if (selected.equals(aliasArray[4]))
            limit.add(Calendar.WEEK_OF_MONTH, -1);
        if (selected.equals(aliasArray[5]))
            limit.add(Calendar.MONTH, -1);
        if (selected.equals(aliasArray[6]))
            limit.add(Calendar.MONTH, -6);
        if (selected.equals(aliasArray[7]))
            limit.add(Calendar.YEAR, -1);
        return limit;
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
    static int getTimeDelta(int[] time1, int[] time2) {
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
     * Stores the selected agendas list to the shared preferences, to keep the selection from resetting on each app start
     *
     * @param context Current context
     * @param selected selected agendas to save
     */
    public static void saveSelectedAgendasToPrefs(Context context, List<Integer> selected) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        List<String> agendas = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            agendas.add(selected.get(i).toString());
        }
        Set<String> set = new HashSet<>(agendas); // Convert the int list to a Set to save it as a preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(SettingsActivity.KEY_SELECTED_AGENDAS, set);
        editor.apply();
    }
    
    /**
     * Recovers the previously saved selected agendas list from shared preferences
     *
     * @param context Current context
     *
     * @return list of selected agendas Ids
     */
    public static List<Integer> getSelectedAgendasFromPrefs(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set = sharedPref.getStringSet(SettingsActivity.KEY_SELECTED_AGENDAS, null);
        List<String> agendas = new ArrayList<>();
        if (set != null)
            agendas = new ArrayList<>(set);
        List<Integer> selected = new ArrayList<>();
        for (int i = 0; i < agendas.size(); i++) {
            selected.add(Integer.parseInt(agendas.get(i)));
        }
        return selected;
    }
}
