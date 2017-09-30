package com.clubinfo.insat.memorisia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class OptionsListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int type;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        if(nightMode)
            setTheme(R.style.AppTheme_Dark);
        
        setContentView(R.layout.recyclerview_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        type = Integer.parseInt(getIntent().getData().toString());
        mAdapter = createModulesListAdapter(type);
        switch (type){
            case 0:
                setTitle(R.string.edit_subjects);
                break;
            case 1:
                setTitle(R.string.edit_work);
                break;
            case 2:
                setTitle(R.string.edit_agendas);
                break;
        }
        
        if (mAdapter != null){
            recyclerView = (RecyclerView) findViewById(R.id.subjectsRecyclerView);
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(mAdapter);
        }
    }
    
    
    private OptionsRecyclerAdapter createModulesListAdapter(int type){
        final List<OptionModule> modules = new SaveManager(this).getModuleList(type);
        return new OptionsRecyclerAdapter(modules, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        MenuItem editButton = menu.findItem(R.id.action_add);
        Drawable icon = editButton.getIcon();
        icon.mutate().setColorFilter(Color.argb(255, 255, 255, 255), PorterDuff.Mode.SRC_IN);
        editButton.setIcon(icon);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                Intent intent = new Intent(this, EditOptionsActivity.class);
                Bundle b = new Bundle();
                b.putInt("type", type);
                intent.putExtras(b);
                startActivity(intent);
                break;
        }
    
        return super.onOptionsItemSelected(item);
    }
}
