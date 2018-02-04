package com.clubinfo.insat.memorisia.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.activities.MainActivity;
import com.clubinfo.insat.memorisia.activities.SettingsActivity;
import com.clubinfo.insat.memorisia.adapters.WorksRecyclerAdapter;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class WorkViewFragment extends BaseFragment {
    
    private RecyclerView recyclerView;
    private OptionModule subject;

    
    public int getSubjectId() {
        return subject.getId();
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_layout, container, false);
        
        subject = ModulesUtils.createOptionModuleFromBundle(getArguments());
        getActivity().setTitle(subject.getText());
        
        recyclerView = view.findViewById(R.id.subjectsRecyclerView);
        recyclerView = Utils.setRecyclerViewDivider(recyclerView, getActivity());
        
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setCurrentSortType(SortType.values()[sharedPref.getInt(SettingsActivity.KEY_WORKS_SORT_TYPE, 0)]);
        setReverseSort(sharedPref.getBoolean(SettingsActivity.KEY_WORKS_SORT_REVERSE, false));
        generateList();
        
        MainActivity act = (MainActivity) getActivity();
        if (act.getSortButton() != null) {
            act.generateSortMenu(act.getSortButton().getSubMenu(), true);
            act.changeSortMenuItemIcon(act.getSortButton().getSubMenu().getItem(getCurrentSortType().ordinal()), isReverseSort());
        }
        return view;
    }
    
    /**
     * Generates the works list based on the selected sort type
     */
    @Override
    public void generateList() {
        SaveManager saver = new SaveManager(getActivity());
        MainActivity act = (MainActivity) getActivity();
        List<Integer> subjects = new ArrayList<>();
        subjects.add(subject.getId());
        List<WorkModule> worksList = saver.getWorkModuleList(act.getSelectedAgendas(), subjects, null);
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
    }
    
}
