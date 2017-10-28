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
import com.clubinfo.insat.memorisia.adapters.SubjectsRecyclerAdapter;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;


public class SubjectsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    
    public static final int SORT_NAME = 0;
    public static final int SORT_DONE_PERCENT = 1;
    public static final int SORT_TOTAL_WORK = 2;
    
    private int currentSortType = 0;
    private boolean reverseSort = false;
    
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_layout, container, false);
        getActivity().setTitle(R.string.subjects_title);
        recyclerView = view.findViewById(R.id.subjectsRecyclerView);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(mLayoutManager);
    
    
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentSortType = sharedPref.getInt(SettingsActivity.KEY_SUBJECTS_SORT_TYPE, 0);
        reverseSort = sharedPref.getBoolean(SettingsActivity.KEY_SUBJECTS_SORT_REVERSE, false);
        generateSubjectsList();
    
        MainActivity act = (MainActivity) getActivity();
        if (act.getSortButton() != null) {
            act.generateSortMenu(act.getSortButton().getSubMenu(), true);
            act.changeSortMenuItemIcon(act.getSortButton().getSubMenu().getItem(currentSortType), reverseSort);
        }
        
        
        return view;
    }
    
    public void setSortType(int type){
        reverseSort = type == currentSortType && !reverseSort;
        currentSortType = type;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SettingsActivity.KEY_SUBJECTS_SORT_TYPE, currentSortType);
        editor.putBoolean(SettingsActivity.KEY_SUBJECTS_SORT_REVERSE, reverseSort);
        editor.apply();
        generateSubjectsList();
    }
    
    public boolean isReverseSort() {
        return reverseSort;
    }
    
    private void generateSubjectsList(){
        SaveManager saver = new SaveManager(getActivity());
        switch (currentSortType) {
            case SORT_NAME:
                mAdapter = new SubjectsRecyclerAdapter(getActivity(), ModulesUtils.sortOptionModuleListByName(saver.getOptionModuleList(SaveManager.SUBJECT), reverseSort), getFragmentManager());
                break;
            case SORT_DONE_PERCENT:
                mAdapter = new SubjectsRecyclerAdapter(getActivity(), ModulesUtils.sortOptionModuleListByDonePercent(saver.getOptionModuleList(SaveManager.SUBJECT), getActivity(), reverseSort), getFragmentManager());
                break;
            case SORT_TOTAL_WORK:
                mAdapter = new SubjectsRecyclerAdapter(getActivity(), ModulesUtils.sortOptionModuleListByTotalWork(saver.getOptionModuleList(SaveManager.SUBJECT), getActivity(), reverseSort), getFragmentManager());
                break;
            default:
                mAdapter = new SubjectsRecyclerAdapter(getActivity(), saver.getOptionModuleList(SaveManager.SUBJECT), getFragmentManager());
                break;
        }
        recyclerView.setAdapter(mAdapter);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        generateSubjectsList();
    }
}
