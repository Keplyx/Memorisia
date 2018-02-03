package com.clubinfo.insat.memorisia.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.clubinfo.insat.memorisia.CalendarEventDecorator;
import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.activities.MainActivity;
import com.clubinfo.insat.memorisia.activities.SettingsActivity;
import com.clubinfo.insat.memorisia.adapters.SubjectsRecyclerAdapter;
import com.clubinfo.insat.memorisia.adapters.WorksRecyclerAdapter;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.List;


public class CalendarFragment extends Fragment {
    
    public static final int SORT_NAME = 0;
    public static final int SORT_PRIORITY = 1;
    public static final int SORT_WORK_TYPE = 2;
    public static final int SORT_DATE = 3;
    private RecyclerView recyclerView;
    private MaterialCalendarView calendarView;
    private int currentSortType = 0;
    private boolean reverseSort = false;
    
    private int[] selectedDate;
    
    public int getCurrentSortType() {
        return currentSortType;
    }
    
    public boolean isReverseSort() {
        return reverseSort;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.calendar_fragment, container, false);
        getActivity().setTitle(R.string.calendar_title);
        recyclerView = v.findViewById(R.id.calendarRecyclerView);
        recyclerView = Utils.setRecyclerViewDivider(recyclerView, getActivity());
    
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentSortType = sharedPref.getInt(SettingsActivity.KEY_WORKS_SORT_TYPE, 0);
        reverseSort = sharedPref.getBoolean(SettingsActivity.KEY_WORKS_SORT_REVERSE, false);
    
        calendarView = v.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new com.prolificinteractive.materialcalendarview.OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                selectedDate = new int[]{date.getDay(), date.getMonth() + 1, date.getYear()}; // months start at 0 in calendarView
                generateWorksList();
            }
        });
        
        MainActivity act = (MainActivity) getActivity();
        if (act.getSortButton() != null) {
            act.generateSortMenu(act.getSortButton().getSubMenu(), false);
            act.changeSortMenuItemIcon(act.getSortButton().getSubMenu().getItem(currentSortType), reverseSort);
        }
        return v;
    }
    
    /**
     * Sets the sort type to use for the works list, then saves it to the shared prefs
     *
     * @param type Sort type
     */
    public void setSortType(int type) {
        reverseSort = type == currentSortType && !reverseSort;
        currentSortType = type;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SettingsActivity.KEY_WORKS_SORT_TYPE, currentSortType);
        editor.putBoolean(SettingsActivity.KEY_WORKS_SORT_REVERSE, reverseSort);
        editor.apply();
        generateWorksList();
    }
    
    /**
     * Generates the works list
     */
    public void generateWorksList() {
        SaveManager saver = new SaveManager(getActivity());
        MainActivity act = (MainActivity) getActivity();
        List<WorkModule> worksList = ModulesUtils.getWorkModuleListByDate(
                saver.getWorkModuleList(act.getSelectedAgendas(), null, null), selectedDate);
        RecyclerView.Adapter mAdapter;
        switch (currentSortType) {
            case SORT_NAME:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByName(worksList, reverseSort));
                break;
            case SORT_PRIORITY:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByPriority(worksList, reverseSort));
                break;
            case SORT_WORK_TYPE:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByWorkType(worksList, getActivity(), reverseSort));
                break;
            case SORT_DATE:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByDate(worksList, reverseSort));
                break;
            default:
                mAdapter = new WorksRecyclerAdapter(getActivity(), worksList);
                break;
        }
        recyclerView.setAdapter(mAdapter);
        setDecorator();
    }
    
    private void setDecorator() {
        SaveManager saver = new SaveManager(getActivity());
        MainActivity act = (MainActivity) getActivity();
        List<CalendarDay> dates = new ArrayList<>();
        List<WorkModule> worksList = saver.getWorkModuleList(act.getSelectedAgendas(), null, null);
        for (WorkModule work : worksList) {
            if (work.getDate() != null && work.getDate()[0] != -1 && work.getDate()[1] != -1 && work.getDate()[2] != -1){
                dates.add(new CalendarDay(work.getDate()[2], work.getDate()[1] - 1, work.getDate()[0]));
            }
        }
        calendarView.addDecorator(new CalendarEventDecorator(dates));
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        generateWorksList();
    }
}
