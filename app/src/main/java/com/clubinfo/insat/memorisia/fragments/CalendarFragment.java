package com.clubinfo.insat.memorisia.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clubinfo.insat.memorisia.CalendarEventDecorator;
import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.activities.MainActivity;
import com.clubinfo.insat.memorisia.activities.SettingsActivity;
import com.clubinfo.insat.memorisia.adapters.WorksRecyclerAdapter;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarFragment extends BaseFragment {
    
    private RecyclerView recyclerView;
    private MaterialCalendarView calendarView;
    
    private int[] selectedDate = {CalendarDay.today().getDay(), CalendarDay.today().getMonth() + 1, CalendarDay.today().getYear()};
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.calendar_fragment, container, false);
        getActivity().setTitle(R.string.calendar_title);
        recyclerView = v.findViewById(R.id.calendarRecyclerView);
        recyclerView = Utils.setRecyclerViewDivider(recyclerView, getActivity());
    
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setCurrentSortType(SortType.values()[sharedPref.getInt(SettingsActivity.KEY_WORKS_SORT_TYPE, 0)]);
        setReverseSort(sharedPref.getBoolean(SettingsActivity.KEY_WORKS_SORT_REVERSE, false));
    
        calendarView = v.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new com.prolificinteractive.materialcalendarview.OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                selectedDate = new int[]{date.getDay(), date.getMonth() + 1, date.getYear()}; // months start at 0 in calendarView
                generateList();
            }
        });
        calendarView.setSelectedDate(CalendarDay.today());
        generateList();
        MainActivity act = (MainActivity) getActivity();
        if (act.getSortButton() != null) {
            act.generateSortMenu(act.getSortButton().getSubMenu(), true);
            act.changeSortMenuItemIcon(act.getSortButton().getSubMenu().getItem(getCurrentSortType().ordinal()), isReverseSort());
        }
        return v;
    }
    
    /**
     * Generates the works list
     */
    @Override
    public void generateList() {
        SaveManager saver = new SaveManager(getActivity());
        MainActivity act = (MainActivity) getActivity();
        List<WorkModule> worksList = ModulesUtils.getWorkModuleListByDate(
                saver.getWorkModuleList(act.getSelectedAgendas(), null, null), selectedDate);
        RecyclerView.Adapter mAdapter;
        switch (getCurrentSortType()) {
            case SORT_1:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByName(worksList, isReverseSort()));
                break;
            case SORT_2:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByPriority(worksList, isReverseSort()));
                break;
            case SORT_3:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByWorkType(worksList, getActivity(), isReverseSort()));
                break;
            case SORT_4:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByDate(worksList, isReverseSort()));
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
}
