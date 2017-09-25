package com.clubinfo.insat.memorisia;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class OptionsListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        if(nightMode)
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.recyclerview_layout);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        
        option = Integer.parseInt(getIntent().getData().toString());
        mAdapter = null;
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
        
        if (mAdapter != null){
            recyclerView = (RecyclerView) findViewById(R.id.subjectsRecyclerView);
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        }
    }
    
    private OptionsRecyclerAdapter createSubjectsListAdapter(){
        final List<OptionModule> modules = new ArrayList<>();
        int logo = R.drawable.ic_subject_black_24dp;
    
        for (int i = 0; i < 10; i++){
            OptionModule m = new OptionModule(i, option, "Subject " + i, logo, Color.parseColor("#ffebee"), false);
            modules.add(m);
        }
        return new OptionsRecyclerAdapter(modules, this);
    }
    
    private OptionsRecyclerAdapter createWorkTypesListAdapter(){
        final List<OptionModule> modules = new ArrayList<>();
        int logo = R.drawable.ic_work_black_24dp;
        
        for (int i = 0; i < 10; i++){
            OptionModule m = new OptionModule(i, option, "Work Type " + i, logo, Color.parseColor("#5c6bc0"), false);
            modules.add(m);
        }
        return new OptionsRecyclerAdapter(modules, this);
    }
    
    private OptionsRecyclerAdapter createAgendasListAdapter(){
        final List<OptionModule> modules = new ArrayList<>();
        int logo = R.drawable.ic_date_range_black_24dp;
        
        for (int i = 0; i < 10; i++){
            OptionModule m = new OptionModule(i, option, "Agenda  " + i, logo, Color.parseColor("#ffc107"), false);
            modules.add(m);
        }
        return new OptionsRecyclerAdapter(modules, this);
    }
    
    
    public static void editSelected(OptionModule module){

    }
    
    public void onClickItem(View v) {
        int id = v.getId();
        Intent intent = new Intent(this, EditOptionsActivity.class);
        Bundle b = new Bundle();
        switch (id){
            default:
                b.putInt("option", 0);
                break;
            case R.id.editWork:
                b.putInt("option", 1);
                break;
            case R.id.editAgenda:
                b.putInt("option", 2);
                break;
        }
        intent.putExtras(b);
        startActivity(intent);
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
