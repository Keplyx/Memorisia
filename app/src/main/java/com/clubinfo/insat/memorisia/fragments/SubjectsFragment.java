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
import com.clubinfo.insat.memorisia.adapters.SubjectsRecyclerAdapter;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;


public class SubjectsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_layout, container, false);
        getActivity().setTitle(R.string.subjects_title);
        recyclerView = view.findViewById(R.id.subjectsRecyclerView);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(mLayoutManager);
        
        generateSubjectsList();

        
        return view;
    }
    
    private void generateSubjectsList(){
        SaveManager saver = new SaveManager(getActivity());
        mAdapter = new SubjectsRecyclerAdapter(getActivity(), ModulesUtils.sortModuleListByName(saver.getOptionModuleList(SaveManager.SUBJECT)), getFragmentManager());
        recyclerView.setAdapter(mAdapter);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        generateSubjectsList();
    }
}
