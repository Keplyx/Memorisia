package com.clubinfo.insat.memorisia.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.adapters.CustomSpinnerAdapter;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
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
    private Button datePickerButton;
    
    private WorkModule actualWork;
    
    private boolean isNightMode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setNightMode(this, true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        setContentView(R.layout.activity_edit_work);
        
        findComponents();
        generateModuleLists();
        createSpinners();
        isNightMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        actualWork = ModulesUtils.createWorkModuleFromBundle(getIntent().getExtras());
        setDefaultComponentValues();
        if (actualWork.getId() == -1) {
            setTitle(getResources().getString(R.string.add_work));
            Button deleteButton = (Button) findViewById(R.id.deleteButton);
            deleteButton.setVisibility(Button.INVISIBLE);
            deleteButton.setEnabled(false);
        }
        else
            setTitle(getResources().getString(R.string.edit_work));
        
        if (isNightMode) {
            ImageButton clearDateButton = (ImageButton) findViewById(R.id.clearDateButton);
            Drawable icon = clearDateButton.getDrawable();
            icon.mutate().setColorFilter(Color.argb(255, 255, 255, 255), PorterDuff.Mode.SRC_IN);
            clearDateButton.setImageDrawable(icon);
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
        datePickerButton = (Button) findViewById(R.id.pickDateButton);
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
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.simple_icon_spinner, agendasList);
        agendasSpinner.setAdapter(adapter);
        adapter = new CustomSpinnerAdapter(this, R.layout.simple_icon_spinner, subjectsList);
        subjectsSpinner.setAdapter(adapter);
        adapter = new CustomSpinnerAdapter(this, R.layout.simple_icon_spinner, workTypesList);
        worksSpinner.setAdapter(adapter);
    }
    
    /**
     * Sets the default values for components in the activity, based on the actual work.
     * If an id of the actual work is -1, it will be replaced by the id of the selected item in the spinner.
     */
    private void setDefaultComponentValues() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int agenda = sharedPref.getInt(SettingsActivity.KEY_LAST_AGENDA, -1);
        int subject = sharedPref.getInt(SettingsActivity.KEY_LAST_SUBJECT, -1);
        int workType = sharedPref.getInt(SettingsActivity.KEY_LAST_WORK_TYPE, -1);
        
        if (actualWork.getAgendaId() != -1)
            agendasSpinner.setSelection(ModulesUtils.getPosInList(agendasList, actualWork.getAgendaId()));
        else if (agenda != -1)
            agendasSpinner.setSelection(ModulesUtils.getPosInList(agendasList, agenda));
        else
            actualWork.setAgendaId(agendasList.get(agendasSpinner.getSelectedItemPosition()).getId());
        
        if (actualWork.getSubjectId() != -1)
            subjectsSpinner.setSelection(ModulesUtils.getPosInList(subjectsList, actualWork.getSubjectId()));
        else if (subject != -1)
            subjectsSpinner.setSelection(ModulesUtils.getPosInList(subjectsList, subject));
        else
            actualWork.setSubjectId(subjectsList.get(subjectsSpinner.getSelectedItemPosition()).getId());
        
        if (actualWork.getWorkTypeId() != -1)
            worksSpinner.setSelection(ModulesUtils.getPosInList(workTypesList, actualWork.getWorkTypeId()));
        else if (workType != -1)
            worksSpinner.setSelection(ModulesUtils.getPosInList(workTypesList, workType));
        else
            actualWork.setWorkTypeId(workTypesList.get(worksSpinner.getSelectedItemPosition()).getId());
        
        priorityBar.setRating((float) actualWork.getPriority());
        descriptionTextView.setText(actualWork.getText());
        notificationsSwitch.setChecked(actualWork.isNotificationsEnabled());
        setupDatePickerButton();
    }
    
    /**
     * Generates the text for the date picker button based on the selected date
     */
    private void setupDatePickerButton() {
        if (actualWork.getDate()[0] == -1)
            datePickerButton.setText(getResources().getString(R.string.pick_date));
        else if (actualWork.getTime()[0] == -1)
            datePickerButton.setText(Utils.getDateText(actualWork.getDate()));
        else
            datePickerButton.setText(Utils.getDateText(actualWork.getDate()) + "  |  " + Utils.getTimeText(actualWork.getTime()));
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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SettingsActivity.KEY_LAST_AGENDA, actualWork.getAgendaId());
        editor.putInt(SettingsActivity.KEY_LAST_SUBJECT, actualWork.getSubjectId());
        editor.putInt(SettingsActivity.KEY_LAST_WORK_TYPE, actualWork.getWorkTypeId());
        editor.apply();
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
    
    /**
     * Shows a date picker to the user, using an external library
     *
     * @param v View that called the method
     */
    public void showDatePickerDialog(View v) {
        CalendarDatePickerDialogFragment picker = setupDatePicker();
        picker.show(getSupportFragmentManager(), "datepicker");
    }
    
    /**
     * Clears the selected date by the user
     *
     * @param v View that called the method
     */
    public void clearDate(View v) {
        actualWork.setDate(new int[]{-1, -1, -1});
        actualWork.setTime(new int[]{-1, -1});
        setupDatePickerButton();
    }
    
    /**
     * Sets up a date picker at the current day
     */
    private CalendarDatePickerDialogFragment setupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        MonthAdapter.CalendarDay day = new MonthAdapter.CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        final CalendarDatePickerDialogFragment datePicker = new CalendarDatePickerDialogFragment()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setDateRange(day, null);
        
        if (actualWork.getDate()[0] != -1)
            datePicker.setPreselectedDate(actualWork.getDate()[2], actualWork.getDate()[1] - 1, actualWork.getDate()[0]);
        else
            datePicker.setPreselectedDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        
        
        if (isNightMode)
            datePicker.setThemeDark();
        else
            datePicker.setThemeLight();
        datePicker.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                actualWork.setDate(new int[]{dayOfMonth, monthOfYear + 1, year});
                setupDatePickerButton();
                RadialTimePickerDialogFragment timerPicker = setupTimePicker();
                timerPicker.show(getSupportFragmentManager(), "timepicker");
            }
        });
        return datePicker;
    }
    
    /**
     * Sets up a time picker at the current time
     */
    private RadialTimePickerDialogFragment setupTimePicker() {
        Calendar calendar = Calendar.getInstance();
        final RadialTimePickerDialogFragment timerPicker = new RadialTimePickerDialogFragment();
        
        if (actualWork.getTime()[0] != -1)
            timerPicker.setStartTime(actualWork.getTime()[0], actualWork.getTime()[1]);
        else
            timerPicker.setStartTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        
        if (isNightMode)
            timerPicker.setThemeDark();
        else
            timerPicker.setThemeLight();
        
        timerPicker.setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
                actualWork.setTime(new int[]{hourOfDay, minute});
                setupDatePickerButton();
            }
        });
        return timerPicker;
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
