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

package com.clubinfo.insat.memorisia.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.activities.MainActivity;
import com.clubinfo.insat.memorisia.activities.SettingsActivity;
import com.clubinfo.insat.memorisia.utils.Utils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class BaseFragment extends Fragment {
    
    private SortType currentSortType = SortType.SORT_1;
    private boolean reverseSort = false;
    
    public enum SortType {
        SORT_1,
        SORT_2,
        SORT_3,
        SORT_4;
    }
    
    public SortType getCurrentSortType() {
        return currentSortType;
    }
    
    public void setCurrentSortType(SortType value) {
        currentSortType = value;
    }
    
    public boolean isReverseSort() {
        return reverseSort;
    }
    
    public void setReverseSort(boolean value) {
        reverseSort = value;
    }
    
    /**
     * Sets the sort type to use for the list, then saves it to the shared prefs
     *
     * @param type Sort type
     */
    public void setSortType(SortType type, boolean isSubjects) {
        reverseSort = type == currentSortType && !reverseSort;
        currentSortType = type;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        if (isSubjects){
            editor.putInt(SettingsActivity.KEY_SUBJECTS_SORT_TYPE, currentSortType.ordinal());
            editor.putBoolean(SettingsActivity.KEY_SUBJECTS_SORT_REVERSE, reverseSort);
        } else {
            editor.putInt(SettingsActivity.KEY_WORKS_SORT_TYPE, currentSortType.ordinal());
            editor.putBoolean(SettingsActivity.KEY_WORKS_SORT_REVERSE, reverseSort);
        }
        editor.apply();
        generateList();
    }
    
    public void generateList(){
        
    }
    
    @Override
    public void onResume() {
        super.onResume();
        generateList();
    }
    
}
