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
import com.clubinfo.insat.memorisia.modules.Module;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class WorkViewFragment extends Fragment {
    
    private RecyclerView recyclerView;
    
    public static final int SORT_NAME = 0;
    public static final int SORT_PRIORITY = 1;
    public static final int SORT_WORK_TYPE = 2;
    
    private OptionModule module;
    private int currentSortType = 0;
    private boolean reverseSort = false;
    
    public int getSubjectId() {
        return module.getId();
    }
    
    public boolean isReverseSort() {
        return reverseSort;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_layout, container, false);
    
        module = ModulesUtils.createOptionModuleFromBundle(getArguments());
        getActivity().setTitle(module.getText());
        
        recyclerView = view.findViewById(R.id.subjectsRecyclerView);
        recyclerView = Utils.setRecyclerViewDivider(recyclerView, getActivity());
        
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
    
    /**
     * Sets the sort type to use for the works list, then saves it to the shared prefs
     *
     * @param type Sort type
     */
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
    
    /**
     * Generates the works list based on the selected sort type
     */
    public void generateWorksList(){
        SaveManager saver = new SaveManager(getActivity());
        MainActivity act = (MainActivity) getActivity();
        List<Integer> subjects = new ArrayList<>();
        subjects.add(module.getId());
        List<WorkModule> worksList = saver.getWorkModuleList(act.getSelectedAgendas(), subjects, null);
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
