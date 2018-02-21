package com.clubinfo.insat.memorisia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.adapters.OptionsRecyclerAdapter;
import com.clubinfo.insat.memorisia.database.MemorisiaDatabase;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;

import java.util.List;

public class OptionsListActivity extends AppCompatActivity {
    int type;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setNightMode(this, true);
        setContentView(R.layout.recyclerview_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        type = Integer.parseInt(getIntent().getData().toString());
        setActivityTitle();
        setupRecyclerVIew();
    }
    
    /**
     * Sets the activity title based on the active module type
     */
    private void setActivityTitle(){
        switch (type) {
            case OptionModule.SUBJECT:
                setTitle(R.string.edit_subjects);
                break;
            case OptionModule.WORK_TYPE:
                setTitle(R.string.edit_work_types);
                break;
            case OptionModule.AGENDA:
                setTitle(R.string.edit_agendas);
                break;
        }
    }
    
    /**
     * Sets up the recycler view containing the modules based on the type
     */
    private void setupRecyclerVIew(){
        RecyclerView.Adapter mAdapter = createModulesListAdapter();
        if (mAdapter != null) {
            RecyclerView recyclerView = findViewById(R.id.subjectsRecyclerView);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        }
    }
    
    /**
     * Creates the module list based on the type, sorted by name
     */
    private OptionsRecyclerAdapter createModulesListAdapter() {
        MemorisiaDatabase db = MemorisiaDatabase.getInstance(this);
        final List<OptionModule> modules = ModulesUtils.sortOptionModuleListByName(db.optionModuleDao().getOptionModulesOfType(type), false);
        return new OptionsRecyclerAdapter(modules, this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        Utils.setToolbarIconWhite(menu.findItem(R.id.action_add));
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                Intent intent = new Intent(this, EditOptionsActivity.class);
                OptionModule module = new OptionModule(type, "", "", "#cccccc", false);
                module.setId(-1);
                Bundle b = ModulesUtils.createBundleFromModule(module);
                intent.putExtras(b);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
