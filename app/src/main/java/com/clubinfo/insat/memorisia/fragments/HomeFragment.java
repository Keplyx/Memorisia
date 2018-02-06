package com.clubinfo.insat.memorisia.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.activities.MainActivity;
import com.clubinfo.insat.memorisia.adapters.WorksRecyclerAdapter;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    
    private RecyclerView weekRecyclerView;
    private RecyclerView starsRecyclerView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        getActivity().setTitle(R.string.home_title);
        
        weekRecyclerView = v.findViewById(R.id.week_recycler);
        starsRecyclerView = v.findViewById(R.id.stars_recycler);
        Utils.setRecyclerViewDivider(weekRecyclerView, getActivity());
        Utils.setRecyclerViewDivider(starsRecyclerView, getActivity());
    
        generateList();
        
        return v;
    }
    
    public void generateList(){
        generateWeekList();
        generateStarsList();
    }
    
    public void generateWeekList() {
        SaveManager saver = new SaveManager(getActivity());
        List<WorkModule> worksList = ModulesUtils.getWorkModuleListByWeek(
                saver.getWorkModuleList(null, null, null), new int[]{CalendarDay.today().getDay(), CalendarDay.today().getMonth() + 1, CalendarDay.today().getYear()});
        RecyclerView.Adapter mAdapter;
        mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByDate(worksList, false));
        weekRecyclerView.setAdapter(mAdapter);
    }
    
    public void generateStarsList() {
        SaveManager saver = new SaveManager(getActivity());
        List<WorkModule> worksList = ModulesUtils.getWorkModuleListByPriority(
                saver.getWorkModuleList(null, null, null), 5);
        RecyclerView.Adapter mAdapter;
        mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByDate(worksList, false));
        starsRecyclerView.setAdapter(mAdapter);
    }
}
