package com.clubinfo.insat.memorisia;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class OptionsListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_layout);
    
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        Bundle b = getIntent().getExtras();
        mAdapter = null;
        if (b != null){
            int option = b.getInt("option");
            switch (option){
                case 0:
                    mAdapter = createSubjectsListAdapter();
                    setTitle(R.string.edit_subjects);
                    break;
                case 1:
                    mAdapter = createWorkTypesListAdapter();
                    setTitle(R.string.edit_work);
                    break;
                case 2:
                    mAdapter = createAgendasListAdapter();
                    setTitle(R.string.edit_agendas);
                    break;
            }
        }
        if (mAdapter != null){
            recyclerView = (RecyclerView) findViewById(R.id.subjectsRecyclerView);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        }
    }
    
    private OptionsRecyclerAdapter createSubjectsListAdapter(){
        final List<OptionModule> modules = new ArrayList<>();
        int logo = R.drawable.ic_subject_black_24dp;
    
        for (int i = 0; i < 10; i++){
            OptionModule m = new OptionModule(i, "Subject " + i, logo, 0x000000, false);
            modules.add(m);
        }
        return new OptionsRecyclerAdapter(modules);
    }
    
    private OptionsRecyclerAdapter createWorkTypesListAdapter(){
        final List<OptionModule> modules = new ArrayList<>();
        int logo = R.drawable.ic_work_black_24dp;
        
        for (int i = 0; i < 10; i++){
            OptionModule m = new OptionModule(i, "Work Type " + i, logo, 0x000000, false);
            modules.add(m);
        }
        return new OptionsRecyclerAdapter(modules);
    }
    
    private OptionsRecyclerAdapter createAgendasListAdapter(){
        final List<OptionModule> modules = new ArrayList<>();
        int logo = R.drawable.ic_date_range_black_24dp;
        
        for (int i = 0; i < 10; i++){
            OptionModule m = new OptionModule(i, "Work Type " + i, logo, 0x000000, false);
            modules.add(m);
        }
        return new OptionsRecyclerAdapter(modules);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
    
        return super.onOptionsItemSelected(item);
    }
}
