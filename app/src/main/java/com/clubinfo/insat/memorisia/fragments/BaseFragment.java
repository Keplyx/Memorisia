package com.clubinfo.insat.memorisia.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.clubinfo.insat.memorisia.activities.SettingsActivity;

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
