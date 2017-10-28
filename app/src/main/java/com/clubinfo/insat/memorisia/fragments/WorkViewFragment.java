package com.clubinfo.insat.memorisia.fragments;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.activities.MainActivity;
import com.clubinfo.insat.memorisia.activities.SettingsActivity;
import com.clubinfo.insat.memorisia.adapters.WorksRecyclerAdapter;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;

import java.util.List;

public class WorkViewFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    
    public static final int SORT_NAME = 0;
    public static final int SORT_PRIORITY = 1;
    public static final int SORT_WORK_TYPE = 2;
    
    private int subjectId;
    
    private int currentSortType = 0;
    private boolean reverseSort = false;
    
    public int getSubjectId() {
        return subjectId;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_layout, container, false);
        
        subjectId = getArguments().getInt("id");
        getActivity().setTitle(getSubjectName());
        
        recyclerView = view.findViewById(R.id.subjectsRecyclerView);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(mLayoutManager);
        
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentSortType = sharedPref.getInt(SettingsActivity.KEY_WORKS_SORT_TYPE, 0);
        reverseSort = sharedPref.getBoolean(SettingsActivity.KEY_WORKS_SORT_REVERSE, false);
        generateWorksList();
    
        MainActivity act = (MainActivity) getActivity();
        if (act.getSortButton() != null) {
            act.generateSortMenu(act.getSortButton().getSubMenu(), false);
            act.changeSortMenuItemIcon(act.getSortButton().getSubMenu().getItem(currentSortType), reverseSort);
        }
        
        return view;
    }
    
    public void setSortType(int type){
        reverseSort = type == currentSortType && !reverseSort;
        currentSortType = type;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SettingsActivity.KEY_WORKS_SORT_TYPE, currentSortType);
        editor.putBoolean(SettingsActivity.KEY_WORKS_SORT_REVERSE, reverseSort);
        editor.apply();
        generateWorksList();
    }
    
    public boolean isReverseSort() {
        return reverseSort;
    }
    
    private String getSubjectName(){
        SaveManager saver = new SaveManager(getActivity());
        List<OptionModule> list = saver.getOptionModuleList(SaveManager.SUBJECT);
        for (OptionModule m : list){
            if (m.getId() == subjectId)
                return m.getText();
        }
        return "Subject not found";
    }
    
    private void generateWorksList(){
        SaveManager saver = new SaveManager(getActivity());
        List<WorkModule> worksList = saver.getWorkModuleList(-1, subjectId, -1);
        switch (currentSortType) {
            case SORT_NAME:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByName(worksList, reverseSort));
                break;
            case SORT_PRIORITY:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByPriority(worksList, getActivity(), reverseSort));
                break;
            case SORT_WORK_TYPE:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByWorkType(worksList, getActivity(), reverseSort));
                break;
            default:
                mAdapter = new WorksRecyclerAdapter(getActivity(), worksList);
                break;
        }
        recyclerView.setAdapter(mAdapter);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        generateWorksList();
    }
    
}
