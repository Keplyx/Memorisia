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
import com.clubinfo.insat.memorisia.adapters.SubjectsRecyclerAdapter;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;


public class SubjectsFragment extends BaseFragment {
    private RecyclerView recyclerView;
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_layout, container, false);
        getActivity().setTitle(R.string.subjects_title);
        recyclerView = view.findViewById(R.id.subjectsRecyclerView);
        recyclerView = Utils.setRecyclerViewDivider(recyclerView, getActivity());
        
        
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setCurrentSortType(SortType.values()[sharedPref.getInt(SettingsActivity.KEY_SUBJECTS_SORT_TYPE, 0)]);
        setReverseSort(sharedPref.getBoolean(SettingsActivity.KEY_SUBJECTS_SORT_REVERSE, false));
        generateList();
        
        MainActivity act = (MainActivity) getActivity();
        if (act.getSortButton() != null) {
            act.generateSortMenu(act.getSortButton().getSubMenu(), false);
            act.changeSortMenuItemIcon(act.getSortButton().getSubMenu().getItem(getCurrentSortType().ordinal()), isReverseSort());
        }
        return view;
    }
    
    /**
     * Generates the sorted subjects list based on the selected sort type
     */
    @Override
    public void generateList() {
        SaveManager saver = new SaveManager(getActivity());
        MainActivity act = (MainActivity) getActivity();
        RecyclerView.Adapter mAdapter;
        switch (getCurrentSortType()) {
            case SORT_1:
                mAdapter = new SubjectsRecyclerAdapter(getActivity(),
                        ModulesUtils.sortOptionModuleListByName(saver.getOptionModuleList(SaveManager.SUBJECT), isReverseSort()),
                        act.getSelectedAgendas(), getFragmentManager());
                break;
            case SORT_2:
                mAdapter = new SubjectsRecyclerAdapter(getActivity(),
                        ModulesUtils.sortOptionModuleListByDonePercent(
                                saver.getOptionModuleList(SaveManager.SUBJECT), act.getSelectedAgendas(), getActivity(), isReverseSort()),
                        act.getSelectedAgendas(), getFragmentManager());
                break;
            case SORT_3:
                mAdapter = new SubjectsRecyclerAdapter(getActivity(),
                        ModulesUtils.sortOptionModuleListByTotalWork(
                                saver.getOptionModuleList(SaveManager.SUBJECT), act.getSelectedAgendas(), getActivity(), isReverseSort()),
                        act.getSelectedAgendas(), getFragmentManager());
                break;
            default:
                mAdapter = new SubjectsRecyclerAdapter(getActivity(), saver.getOptionModuleList(SaveManager.SUBJECT),
                        act.getSelectedAgendas(), getFragmentManager());
                break;
        }
        recyclerView.setAdapter(mAdapter);
    }
}
