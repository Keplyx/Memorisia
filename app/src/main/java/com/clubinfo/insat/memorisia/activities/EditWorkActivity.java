package com.clubinfo.insat.memorisia.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.clubinfo.insat.memorisia.utils.Utils;

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
    
    private WorkModule actualWork;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setNightMode(this, true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        setContentView(R.layout.activity_edit_work);
        setTitle(getResources().getString(R.string.add_work));
        findComponents();
        generateModuleLists();
        createSpinners();
        
        actualWork = ModulesUtils.createWorkModuleFromBundle(getIntent().getExtras());
        setDefaultComponentValues();
        if (actualWork.getId() == -1) {
            Button deleteButton = (Button) findViewById(R.id.deleteButton);
            deleteButton.setVisibility(Button.INVISIBLE);
            deleteButton.setEnabled(false);
        }
    }
    
    /**
     * Finds all necessary components in the activity
     */
    private void findComponents() {
        agendasSpinner = (Spinner) findViewById(R.id.agendaSpinner);
        subjectsSpinner = (Spinner) findViewById(R.id.subjectSpinner);
        worksSpinner = (Spinner) findViewById(R.id.workTypeSpinner);
        descriptionTextView = (EditText) findViewById(R.id.descriptionEditText);
        priorityBar = (RatingBar) findViewById(R.id.priorityRatingBar);
        notificationsSwitch = (Switch) findViewById(R.id.notificationsSwitch);
    }
    
    /**
     * Generates the modules lists (agendas, subjects and work types).
     * Sorted alphabetically.
     */
    private void generateModuleLists() {
        SaveManager saver = new SaveManager(this);
        agendasList = ModulesUtils.sortOptionModuleListByName(saver.getOptionModuleList(SaveManager.AGENDA), false);
        subjectsList = ModulesUtils.sortOptionModuleListByName(saver.getOptionModuleList(SaveManager.SUBJECT), false);
        workTypesList = ModulesUtils.sortOptionModuleListByName(saver.getOptionModuleList(SaveManager.WORK_TYPE), false);
    }
    
    /**
     * Creates the spinners based on the available modules
     */
    private void createSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getModulesNames(agendasList));
        agendasSpinner.setAdapter(adapter);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getModulesNames(subjectsList));
        subjectsSpinner.setAdapter(adapter);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getModulesNames(workTypesList));
        worksSpinner.setAdapter(adapter);
    }
    
    /**
     * Sets the default values for components in the activity, based on the actual work.
     * If an id of the actual work is -1, it will be replaced by the id of the selected item in the spinner.
     */
    private void setDefaultComponentValues() {
        if (actualWork.getAgendaId() != -1)
            agendasSpinner.setSelection(ModulesUtils.getPosInList(agendasList, actualWork.getAgendaId()));
        else
            actualWork.setAgendaId(agendasList.get(agendasSpinner.getSelectedItemPosition()).getId());
        if (actualWork.getSubjectId() != -1)
            subjectsSpinner.setSelection(ModulesUtils.getPosInList(subjectsList, actualWork.getSubjectId()));
        else
            actualWork.setSubjectId(subjectsList.get(subjectsSpinner.getSelectedItemPosition()).getId());
        if (actualWork.getWorkTypeId() != -1)
            worksSpinner.setSelection(ModulesUtils.getPosInList(workTypesList, actualWork.getWorkTypeId()));
        else
            actualWork.setWorkTypeId(workTypesList.get(worksSpinner.getSelectedItemPosition()).getId());
        priorityBar.setRating((float) actualWork.getPriority());
        descriptionTextView.setText(actualWork.getText());
        notificationsSwitch.setChecked(actualWork.isNotificationsEnabled());
    }
    
    /**
     * Generates the actual module with values from components from the activity
     */
    private void generateModule() {
        actualWork.setAgendaId(agendasList.get(agendasSpinner.getSelectedItemPosition()).getId());
        actualWork.setSubjectId(subjectsList.get(subjectsSpinner.getSelectedItemPosition()).getId());
        actualWork.setWorkTypeId(workTypesList.get(worksSpinner.getSelectedItemPosition()).getId());
        actualWork.setPriority((int) priorityBar.getRating());
        actualWork.setText(descriptionTextView.getText().toString());
        actualWork.setNotificationsEnabled(notificationsSwitch.isChecked());
    }
    
    /**
     * Gets the names of each OptionModule in a list
     *
     * @param modules OptionModule list to extract names from
     */
    public String[] getModulesNames(List<OptionModule> modules) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < modules.size(); i++) {
            names.add(modules.get(i).getText());
        }
        return names.toArray(new String[names.size()]);
    }
    
    /**
     * Generates and saves the actual module, then exits the activity.
     *
     * @param v View that called the method
     */
    public void onClickDoneWork(View v) {
        generateModule();
        SaveManager saver = new SaveManager(this);
        saver.saveModule(actualWork);
        finish();
    }
    
    /**
     * Shows a confirm dialog when the user clicks the delete button
     *
     * @param v View that called the method
     */
    public void onClickDelete(View v) {
        showConfirmDeleteDialog();
    }
    
    /**
     * Shows a confirm delete dialog
     */
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
                        saver.deleteWorkModule(actualWork.getId());
                        finish();
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
