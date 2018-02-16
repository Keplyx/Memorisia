package com.clubinfo.insat.memorisia.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

public class WorkViewFragment extends BaseFragment {
    
    private RecyclerView recyclerView;
    private OptionModule module;

    
    public OptionModule getParentModule() {
        return module;
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_layout, container, false);
        
        module = ModulesUtils.createOptionModuleFromBundle(getArguments());
        getActivity().setTitle(module.getText());
        
        recyclerView = view.findViewById(R.id.subjectsRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        
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
        List<Integer> modules = new ArrayList<>();
        modules.add(module.getId());
        boolean isParentSubject = module.getType() == SaveManager.SUBJECT;
        List<WorkModule> workList;
        if (isParentSubject)
            workList = saver.getWorkModuleList(act.getSelectedAgendas(), modules, null);
        else
            workList = saver.getWorkModuleList(act.getSelectedAgendas(), null, modules);
        RecyclerView.Adapter mAdapter;
        
        switch (getCurrentSortType()) {
            case SORT_1:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByName(workList, isReverseSort()), isParentSubject);
                break;
            case SORT_2:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByPriority(workList, isReverseSort()), isParentSubject);
                break;
            case SORT_3:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByWorkType(workList, getActivity(), isReverseSort()), isParentSubject);
                break;
            case SORT_4:
                mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByDate(workList, isReverseSort()), isParentSubject);
                break;
            default:
                mAdapter = new WorksRecyclerAdapter(getActivity(), workList, isParentSubject);
                break;
        }
        recyclerView.setAdapter(mAdapter);
    }
    
}
