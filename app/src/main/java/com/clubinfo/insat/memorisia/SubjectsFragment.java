package com.clubinfo.insat.memorisia;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


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
        final List<OptionModule> modules = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            OptionModule m = new OptionModule(i, 0, "Subject " + i, R.drawable.ic_subject_black_24dp, Color.parseColor("#E2261B"), false);
            modules.add(m);
        }
        mAdapter = new SubjectsRecyclerAdapter(modules);
        recyclerView.setAdapter(mAdapter);
    
        return view;
    }
}
