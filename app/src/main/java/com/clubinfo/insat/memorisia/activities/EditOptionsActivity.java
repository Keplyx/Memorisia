package com.clubinfo.insat.memorisia.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.adapters.LogosListAdapter;
import com.clubinfo.insat.memorisia.database.MemorisiaDatabase;
import com.clubinfo.insat.memorisia.modules.Module;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditOptionsActivity extends AppCompatActivity {
    
    private Context context;
    private TextView title;
    private GridView logosGridView;
    private Button colorButton;
    private Button deleteButton;
    
    private OptionModule module;
    
    private List<String> logosList = new ArrayList<>();
    
    public void setSelectedLogo(String l) {
        module.setLogo(l);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Utils.setNightMode(this, true);
        setContentView(R.layout.activity_edit_options);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        logosList = Utils.generateLogosList(this);
        getComponents();
        
        int id = getIntent().getExtras().getInt("id");
        if (id != -1)
            module = MemorisiaDatabase.getInstance(context).optionModuleDao().getOptionModuleOfId(id);
        else
            module = ModulesUtils.createOptionModuleFromBundle(getIntent().getExtras());
        
        colorButton.setBackgroundColor(Color.parseColor(module.getColor()));
        if (id != -1) {
            title.setText(module.getText());
            setTitle(getResources().getString(R.string.editing) + " " + module.getText());
            if (logosList.size() > 0)
                logosGridView.setAdapter(new LogosListAdapter(this, logosList, module.getLogo(), module.getColor()));
        } else {
            switch (module.getType()) {
                case OptionModule.SUBJECT:
                    setTitle(R.string.create_subject);
                    break;
                case OptionModule.WORK_TYPE:
                    setTitle(R.string.create_work);
                    break;
                case OptionModule.AGENDA:
                    setTitle(R.string.create_agenda);
                    break;
            }
            title.setText(R.string.name);
            deleteButton.setVisibility(Button.INVISIBLE);
            deleteButton.setEnabled(false);
            if (logosList.size() > 0) {
                logosGridView.setAdapter(new LogosListAdapter(this, logosList, logosList.get(0), module.getColor()));
                module.setLogo(logosList.get(0));
            }
        }
    }
    
    /**
     * Gets graphical components from the activity
     */
    private void getComponents() {
        title = findViewById(R.id.moduleName);
        logosGridView = findViewById(R.id.logosContainer);
        colorButton = findViewById(R.id.moduleColor);
        deleteButton = findViewById(R.id.deleteModule);
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
    
    /**
     * Shows a color picker dialog to the user, allowing him to choose a color for the module
     *
     * @param v View that called the method
     */
    public void onClickColorPicker(View v) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, Color.parseColor(module.getColor()), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int intColor) {
                colorButton.setBackgroundColor(intColor);
                module.setColor(String.format("#%06X", (0xFFFFFF & intColor)));
                if (logosList.size() > 0)
                    logosGridView.setAdapter(new LogosListAdapter(context, logosList, module.getLogo(), module.getColor()));
            }
            
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
            
        });
        dialog.show();
    }
    
    /**
     * Saves the module corresponding to user selection and exits the activity
     *
     * @param v View that called the method
     */
    public void onClickDone(View v) {
        module.setText(title.getText().toString());
        long id = MemorisiaDatabase.getInstance(context).optionModuleDao().insertOptionModules(module)[0];
        // Add to selected agendas list if module is agenda
        if (module.getType() == OptionModule.AGENDA){
            List<Integer> selectedAgendas = Utils.getSelectedAgendasFromPrefs(context);
            selectedAgendas.add((int) id);
            Utils.saveSelectedAgendasToPrefs(context, selectedAgendas);
        }
        exitEditOptions();
    }
    
    /**
     * Shows a confirm dialog when the user clicks the delete button
     *
     * @param v View that called the method
     */
    public void onClickDelete(View v) {
        List<WorkModule> workList = null;
        switch (module.getType()){
            case OptionModule.AGENDA:
                workList = MemorisiaDatabase.getInstance(context).workModuleDao().getWorkModulesOfAgenda(module.getId());
                break;
            case OptionModule.SUBJECT:
                workList = MemorisiaDatabase.getInstance(context).workModuleDao().getWorkModulesOfSubject(module.getId());
                break;
            case OptionModule.WORK_TYPE:
                workList = MemorisiaDatabase.getInstance(context).workModuleDao().getWorkModulesOfWorkType(module.getId());
                break;
        }
        if (workList != null && workList.size() > 0)
             showErrorDialog(getResources().getString(R.string.cannot_delete_non_empty));
        else
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
                        MemorisiaDatabase data = MemorisiaDatabase.getInstance(context);
                        if (data.optionModuleDao().getOptionModulesOfType(module.getType()).size() == 1)
                            showErrorDialog(getResources().getString(R.string.cannot_delete_last));
                        else {
                            data.optionModuleDao().deleteOptionModules(module);
                            exitEditOptions();
                        }
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
     * Shows an error dialog telling the user he cannot delete an entry
     *
     * @param message       Message to display to the user
     */
    private void showErrorDialog(String message) {
        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.AppTheme);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        builder.setMessage(message);
        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    /**
     * Exits the activity without adding it to the backtrace
     */
    private void exitEditOptions() {
        Intent intent = new Intent(this, OptionsListActivity.class);
        intent.setData(Uri.parse(module.getType() + ""));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    
}
