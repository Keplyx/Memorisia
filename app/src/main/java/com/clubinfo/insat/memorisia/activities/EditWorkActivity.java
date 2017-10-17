package com.clubinfo.insat.memorisia.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;

import java.util.ArrayList;
import java.util.List;

public class EditWorkActivity extends AppCompatActivity {
    
    private List<OptionModule> agendasList = new ArrayList<>();
    private List<OptionModule> subjectsList = new ArrayList<>();
    private List<OptionModule> workTypesList = new ArrayList<>();
    
    private Spinner agendasSpinner;
    private Spinner subjectsSpinner;
    private Spinner worksSpinner;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        if(nightMode)
            setTheme(R.style.AppTheme_Dark);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        setContentView(R.layout.activity_edit_work);
        setTitle(getResources().getString(R.string.add_work));
        
        SaveManager saver = new SaveManager(this);
        
        agendasSpinner = (Spinner) findViewById(R.id.agendaSpinner);
        agendasList = ModulesUtils.sortModuleListByName(saver.getOptionModuleList(SaveManager.AGENDA));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getModulesNames(agendasList));
        agendasSpinner.setAdapter(adapter);
        
        subjectsSpinner = (Spinner) findViewById(R.id.subjectSpinner);
        subjectsList = ModulesUtils.sortModuleListByName(saver.getOptionModuleList(SaveManager.SUBJECT));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getModulesNames(subjectsList));
        subjectsSpinner.setAdapter(adapter);
        
        worksSpinner = (Spinner) findViewById(R.id.workTypeSpinner);
        workTypesList = ModulesUtils.sortModuleListByName(saver.getOptionModuleList(SaveManager.WORKTYPE));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getModulesNames(workTypesList));
        worksSpinner.setAdapter(adapter);
    }
    
    public String[] getModulesNames(List<OptionModule> modules){
        List<String> names = new ArrayList<>();
        for (int i = 0; i < modules.size(); i++){
            names.add(modules.get(i).getText());
        }
        return names.toArray(new String[names.size()]);
    }
    
    public void onClickDoneWork(View v){
        int selectedAgenda = agendasList.get(agendasSpinner.getSelectedItemPosition()).getId();
        int selectedSubject = subjectsList.get(subjectsSpinner.getSelectedItemPosition()).getId();
        int selectedWorkType = workTypesList.get(worksSpinner.getSelectedItemPosition()).getId();
        RatingBar bar = (RatingBar) findViewById(R.id.priorityRatingBar);
        int priority = (int) bar.getRating();
        EditText desc = (EditText) findViewById(R.id.descriptionEditText);
        String description = desc.getText().toString();
        Switch notif = (Switch) findViewById(R.id.notificationsSwitch);
        boolean notifications = notif.isChecked();
        WorkModule work = new WorkModule(-1, selectedAgenda, selectedSubject, selectedWorkType, priority, description, notifications, false);
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
