/*
 * Copyright (c) 2018.
 * This file is part of Memorisia.
 *
 * Memorisia is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Memorisia is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Memorisia.  If not, see <http://www.gnu.org/licenses/>.
 */

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
import com.clubinfo.insat.memorisia.activities.MainActivity;
import com.clubinfo.insat.memorisia.activities.SettingsActivity;
import com.clubinfo.insat.memorisia.adapters.OptionModulesRecyclerAdapter;
import com.clubinfo.insat.memorisia.database.MemorisiaDatabase;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;

import java.util.List;

public class WorkTypesFragment extends BaseFragment {
    private RecyclerView recyclerView;
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_layout, container, false);
        getActivity().setTitle(R.string.work_types_title);
        recyclerView = view.findViewById(R.id.subjectsRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        
        
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
        MemorisiaDatabase db = MemorisiaDatabase.getInstance(getActivity());
        MainActivity act = (MainActivity) getActivity();
        List<OptionModule> modules = db.optionModuleDao().getOptionModulesOfType(OptionModule.WORK_TYPE);
        RecyclerView.Adapter mAdapter;
        switch (getCurrentSortType()) {
            case SORT_1:
                mAdapter = new OptionModulesRecyclerAdapter(getActivity(),
                        ModulesUtils.sortOptionModuleListByName(modules, isReverseSort()),
                        act.getSelectedAgendas(), getFragmentManager());
                break;
            case SORT_2:
                mAdapter = new OptionModulesRecyclerAdapter(getActivity(),
                        ModulesUtils.sortOptionModuleListByDonePercent(modules, act.getSelectedAgendas(), getActivity(), isReverseSort()),
                        act.getSelectedAgendas(), getFragmentManager());
                break;
            case SORT_3:
                mAdapter = new OptionModulesRecyclerAdapter(getActivity(),
                        ModulesUtils.sortOptionModuleListByTotalWork(modules, act.getSelectedAgendas(), getActivity(), isReverseSort()),
                        act.getSelectedAgendas(), getFragmentManager());
                break;
            default:
                mAdapter = new OptionModulesRecyclerAdapter(getActivity(), modules, act.getSelectedAgendas(), getFragmentManager());
                break;
        }
        recyclerView.setAdapter(mAdapter);
    }
}
