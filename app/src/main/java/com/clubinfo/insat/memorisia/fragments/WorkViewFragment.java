package com.clubinfo.insat.memorisia.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.adapters.WorksRecyclerAdapter;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkViewFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    
    private int subjectId;
    
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
        
        generateWorksList();
        
        return view;
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
        mAdapter = new WorksRecyclerAdapter(getActivity(), worksList);
        recyclerView.setAdapter(mAdapter);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        generateWorksList();
    }
    
}
