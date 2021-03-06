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

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.activities.MainActivity;
import com.clubinfo.insat.memorisia.adapters.WorksRecyclerAdapter;
import com.clubinfo.insat.memorisia.database.MemorisiaDatabase;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

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
        weekRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        starsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    
        generateList();
        
        return v;
    }
    
    public void generateList(){
        generateWeekList();
        generateStarsList();
    }
    
    public void generateWeekList() {
        MemorisiaDatabase db = MemorisiaDatabase.getInstance(getActivity());
        MainActivity act = (MainActivity) getActivity();
        List<WorkModule> worksList = ModulesUtils.getWorkModuleListByWeek(
                db.workModuleDao().getWorkModulesOfAgenda(act.getSelectedAgendas()), new int[]{CalendarDay.today().getDay(), CalendarDay.today().getMonth() + 1, CalendarDay.today().getYear()});
        RecyclerView.Adapter mAdapter;
        mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByDate(worksList, false));
        weekRecyclerView.setAdapter(mAdapter);
    }
    
    public void generateStarsList() {
        MemorisiaDatabase db = MemorisiaDatabase.getInstance(getActivity());
        MainActivity act = (MainActivity) getActivity();
        List<WorkModule> worksList = ModulesUtils.getWorkModuleListByPriority(
                db.workModuleDao().getWorkModulesOfAgenda(act.getSelectedAgendas()), 5);
        RecyclerView.Adapter mAdapter;
        mAdapter = new WorksRecyclerAdapter(getActivity(), ModulesUtils.sortWorkModuleListByDate(worksList, false));
        starsRecyclerView.setAdapter(mAdapter);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        generateList();
    }
}
