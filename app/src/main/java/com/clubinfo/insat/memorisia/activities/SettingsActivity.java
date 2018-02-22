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

package com.clubinfo.insat.memorisia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    
    public static final String KEY_NIGHT_MODE = "pref_key_night_mode";
    
    public static final String KEY_DELETE_OLD = "pref_key_delete_old";
    public static final String KEY_DELETE_OLD_TIME = "pref_key_delete_old_time";
    public static final String KEY_DELETE_OLD_ONLY_DONE = "pref_key_delete_old_only_done";
    
    public static final String KEY_SUBJECTS_SORT_TYPE = "pref_key_subjects_sort_type";
    public static final String KEY_SUBJECTS_SORT_REVERSE = "pref_key_subjects_sort_reverse";
    public static final String KEY_WORKS_SORT_TYPE = "pref_key_works_sort_type";
    public static final String KEY_WORKS_SORT_REVERSE = "pref_key_works_sort_reverse";
    public static final String KEY_SELECTED_AGENDAS = "pref_set_selected_agendas";
    
    public static final String KEY_LAST_AGENDA = "pref_key_last_selected_agenda";
    public static final String KEY_LAST_SUBJECT = "pref_key_last_selected_subject";
    public static final String KEY_LAST_WORK_TYPE = "pref_key_last_selected_work_type";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        if(nightMode)
            setTheme(R.style.AppTheme_Dark);
    
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.settings_title);
        
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_NIGHT_MODE)) {
            Intent intent = getIntent();
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP); // Prevent loops in back button
            finish();
            startActivity(intent);
        }
    }
    
}
