package com.clubinfo.insat.memorisia.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private EditText descriptionTextView;
    private RatingBar priorityBar;
    private Switch notificationsSwitch;
    
    private WorkModule actualModule;
    
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
        findComponents();
        
        SaveManager saver = new SaveManager(this);
        
        agendasList = ModulesUtils.sortOptionModuleListByName(saver.getOptionModuleList(SaveManager.AGENDA), false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getModulesNames(agendasList));
        agendasSpinner.setAdapter(adapter);
        
        subjectsList = ModulesUtils.sortOptionModuleListByName(saver.getOptionModuleList(SaveManager.SUBJECT), false);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getModulesNames(subjectsList));
        subjectsSpinner.setAdapter(adapter);
        
        workTypesList = ModulesUtils.sortOptionModuleListByName(saver.getOptionModuleList(SaveManager.WORKTYPE), false);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getModulesNames(workTypesList));
        worksSpinner.setAdapter(adapter);
        
        Bundle b = getIntent().getExtras();
        // if has an id, we are editing, else we are creating a new work
        if (b != null) {
            if (b.getInt("agendaId") != -1)
                agendasSpinner.setSelection(ModulesUtils.getPosInList(agendasList, b.getInt("agendaId")));
            if (b.getInt("subjectId") != -1)
                subjectsSpinner.setSelection(ModulesUtils.getPosInList(subjectsList, b.getInt("subjectId")));
            if (b.getInt("workTypeId") != -1)
                worksSpinner.setSelection(ModulesUtils.getPosInList(workTypesList, b.getInt("workTypeId")));
            if (b.getInt("priority") != -1)
                priorityBar.setRating((float) b.getInt("priority"));
            descriptionTextView.setText(b.getString("text"));
            notificationsSwitch.setChecked(b.getBoolean("notifications"));
            generateModule(b.getInt("id"));
        }
        else
            generateModule(-1);
        
        if (actualModule.getId() == -1) {
            Button deleteButton = (Button) findViewById(R.id.deleteButton);
            deleteButton.setVisibility(Button.INVISIBLE);
            deleteButton.setEnabled(false);
        }
    }
    
    private void findComponents(){
        agendasSpinner = (Spinner) findViewById(R.id.agendaSpinner);
        subjectsSpinner = (Spinner) findViewById(R.id.subjectSpinner);
        worksSpinner = (Spinner) findViewById(R.id.workTypeSpinner);
        descriptionTextView = (EditText) findViewById(R.id.descriptionEditText);
        priorityBar = (RatingBar) findViewById(R.id.priorityRatingBar);
        notificationsSwitch = (Switch) findViewById(R.id.notificationsSwitch);
    }
    
    private void generateModule(int id){
        int selectedAgenda = agendasList.get(agendasSpinner.getSelectedItemPosition()).getId();
        int selectedSubject = subjectsList.get(subjectsSpinner.getSelectedItemPosition()).getId();
        int selectedWorkType = workTypesList.get(worksSpinner.getSelectedItemPosition()).getId();
        int priority = (int) priorityBar.getRating();
        String description = descriptionTextView.getText().toString();
        boolean notifications = notificationsSwitch.isChecked();
        actualModule = new WorkModule(id, selectedAgenda, selectedSubject, selectedWorkType, priority, description, notifications, false);
    }
    
    
    public String[] getModulesNames(List<OptionModule> modules){
        List<String> names = new ArrayList<>();
        for (int i = 0; i < modules.size(); i++){
            names.add(modules.get(i).getText());
        }
        return names.toArray(new String[names.size()]);
    }
    
    public void onClickDoneWork(View v){
        generateModule(actualModule.getId());
        SaveManager saver = new SaveManager(this);
        saver.saveModule(actualModule);
        exitEditWork();
    }
    
    public void onClickDelete(View v) {
        showConfirmDeleteDialog();
    }
    
    private void showConfirmDeleteDialog() {
        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.AppTheme);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        builder.setMessage(getResources().getString(R.string.confirm_delete));
        builder.setCancelable(true);
        builder.setPositiveButton(
                getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        SaveManager saver = new SaveManager(builder.getContext());
                        saver.deleteWorkModule(actualModule.getId());
                        exitEditWork();
                        dialog.cancel();
                    }
                });
        
        builder.setNegativeButton(
                getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private void exitEditWork() {
//        Intent intent = new Intent(this, OptionsListActivity.class);
//        intent.setData(Uri.parse(type + ""));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        finish();
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
