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
import com.clubinfo.insat.memorisia.modules.OptionModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditOptionsActivity extends AppCompatActivity {
    
    private static String logo = "";
    private final String logosPath = "logos/";
    private Context context;
    private TextView TextView;
    private GridView logosGridView;
    private Button colorButton;
    private Switch notificationsSwitch;
    private Button deleteButton;
    private int type;
    private int id;
    private String color;
    private List<String> logosList = new ArrayList<>();
    
    public static void setSelectedLogo(String l) {
        logo = l;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        if (nightMode)
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_edit_options);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        generateLogosList();
        TextView = (TextView) findViewById(R.id.moduleName);
        logosGridView = (GridView) findViewById(R.id.logosContainer);
        colorButton = (Button) findViewById(R.id.moduleColor);
        notificationsSwitch = (Switch) findViewById(R.id.moduleNotifications);
        deleteButton = (Button) findViewById(R.id.deleteModule);
        Bundle b = getIntent().getExtras();
        // if has a TextView, we are editing, else we are creating a new option
        if (b != null && b.getString("name") != null) {
            id = b.getInt("id");
            TextView.setText(b.getString("name"));
            logo = b.getString("logo");
            color = b.getString("color");
            colorButton.setBackgroundColor(Color.parseColor(color));
            type = b.getInt("type");
            notificationsSwitch.setChecked(b.getBoolean("notifications"));
            setTitle(getResources().getString(R.string.editing) + " " + b.getString("name"));
            if (logosList.size() > 0)
                logosGridView.setAdapter(new LogosListAdapter(this, logosList, logo, color));
        } else if (b != null) {
            type = b.getInt("type");
            id = -1;
            switch (type) {
                case 0:
                    setTitle(R.string.create_subject);
                    break;
                case 1:
                    setTitle(R.string.create_work);
                    break;
                case 2:
                    setTitle(R.string.create_agenda);
                    break;
            }
            TextView.setText(R.string.name);
            color = "#cccccc";
            colorButton.setBackgroundColor(Color.parseColor(color));
            deleteButton.setVisibility(Button.INVISIBLE);
            deleteButton.setEnabled(false);
            if (logosList.size() > 0) {
                logosGridView.setAdapter(new LogosListAdapter(this, logosList, logosList.get(0), color));
                logo = logosList.get(0);
            }
        }
        context = this;
    }
    
    private void generateLogosList() {
        String[] list = new String[0];
        try {
            list = getAssets().list("logos");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.length; i++) {
            logosList.add(logosPath + list[i]);
        }
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
    
    public void onClickColorPicker(View v) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, Color.parseColor(color), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int intColor) {
                colorButton.setBackgroundColor(intColor);
                color = String.format("#%06X", (0xFFFFFF & intColor));
                if (logosList.size() > 0)
                    logosGridView.setAdapter(new LogosListAdapter(context, logosList, logo, color));
            }
            
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
            
        });
        dialog.show();
    }
    
    public void onClickDone(View v) {
        OptionModule module = new OptionModule(id, type, TextView.getText().toString(),
                logo, color, notificationsSwitch.isChecked());
        
        SaveManager saver = new SaveManager(this);
        saver.saveModule(module);
        exitEditOptions();
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
                        if (saver.getOptionModuleList(type).size() == 1)
                            showErrorDialog();
                        else {
                            saver.deleteOptionModule(id);
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
    
    private void showErrorDialog() {
        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.AppTheme);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        builder.setMessage(getResources().getString(R.string.cannot_delete));
        builder.setCancelable(true);
        
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private void exitEditOptions() {
        Intent intent = new Intent(this, OptionsListActivity.class);
        intent.setData(Uri.parse(type + ""));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    
}
