package com.clubinfo.insat.memorisia.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.adapters.LogosListAdapter;
import com.clubinfo.insat.memorisia.modules.Module;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditOptionsActivity extends AppCompatActivity {
    
    private final String logosPath = "logos/";
    private Context context;
    private TextView title;
    private GridView logosGridView;
    private Button colorButton;
    private Switch notificationsSwitch;
    private Button deleteButton;
    
    private OptionModule module;
    
    private List<String> logosList = new ArrayList<>();
    
    public void setSelectedLogo(String l) {
        module.setLogo(l);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setNightMode(this, true);
        setContentView(R.layout.activity_edit_options);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        logosList = Utils.generateLogosList(this);
        getComponents();
        
        module = ModulesUtils.createOptionModuleFromBundle(getIntent().getExtras());
        colorButton.setBackgroundColor(Color.parseColor(module.getColor()));
        notificationsSwitch.setChecked(module.isNotificationsEnabled());
        if (module.getId() != -1) {
            title.setText(module.getText());
            setTitle(getResources().getString(R.string.editing) + " " + module.getText());
            if (logosList.size() > 0)
                logosGridView.setAdapter(new LogosListAdapter(this, logosList, module.getLogo(), module.getColor()));
        } else {
            switch (module.getType()) {
                case SaveManager.SUBJECT:
                    setTitle(R.string.create_subject);
                    break;
                case SaveManager.WORK_TYPE:
                    setTitle(R.string.create_work);
                    break;
                case SaveManager.AGENDA:
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
        context = this;
    }
    
    /**
     * Gets graphical components from the activity
     */
    private void getComponents() {
        title = (TextView) findViewById(R.id.moduleName);
        logosGridView = (GridView) findViewById(R.id.logosContainer);
        colorButton = (Button) findViewById(R.id.moduleColor);
        notificationsSwitch = (Switch) findViewById(R.id.moduleNotifications);
        deleteButton = (Button) findViewById(R.id.deleteModule);
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
        module.setNotificationsEnabled(notificationsSwitch.isChecked());
        new SaveManager(this).saveModule(module);
        exitEditOptions();
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
                        if (saver.getOptionModuleList(module.getType()).size() == 1)
                            showErrorDialog();
                        else {
                            saver.deleteOptionModule(module.getId());
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
     */
    private void showErrorDialog() {
        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.AppTheme);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        builder.setMessage(getResources().getString(R.string.cannot_delete));
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
