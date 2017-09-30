package com.clubinfo.insat.memorisia;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

public class EditOptionsActivity extends AppCompatActivity {
    
    private TextView TextView;
    private ImageButton logoImageButton;
    private Button colorButton;
    private Switch notificationsSwitch;
    private Button deleteButton;
    
    private int type;
    private int id;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        if(nightMode)
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_edit_options);
    
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        TextView = (TextView) findViewById(R.id.moduleName);
        logoImageButton = (ImageButton) findViewById(R.id.moduleLogo);
        colorButton = (Button) findViewById(R.id.moduleColor);
        notificationsSwitch = (Switch) findViewById(R.id.moduleNotifications);
        deleteButton = (Button) findViewById(R.id.deleteModule);
        Bundle b = getIntent().getExtras();
        // if has a TextView, we are editing, else we are creating a new option
        if (b != null && b.getString("name") != null){
            id = b.getInt("id");
            TextView.setText(b.getString("name"));
            logoImageButton.setImageResource(b.getInt("logo"));
            colorButton.setBackgroundColor(Color.parseColor(b.getString("color")));
            type = b.getInt("type");
            notificationsSwitch.setChecked(b.getBoolean("notifications"));
            setTitle(getResources().getString(R.string.editing) + " " + b.getString("name"));
        }
        else if (b != null)
        {
            type = b.getInt("type");
            id = -1;
            switch (type){
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
            colorButton.setBackgroundColor(Color.parseColor("#ffffff"));
            deleteButton.setVisibility(Button.INVISIBLE);
            deleteButton.setEnabled(false);
            
        }
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
    
    public void onClickDone (View v){
        // store icon and color on change in variable
        OptionModule module = new OptionModule(id, type, TextView.getText().toString(),
                R.drawable.ic_subject_black_24dp, "#c5c5c5", notificationsSwitch.isChecked());
        
        SaveManager saver = new SaveManager(this);
        saver.saveOptionModule(module);
        exitEditOptions();
    }
    
    public void onClickDelete(View v){
        showConfirmDeleteDialog();
    }
    
    private void showConfirmDeleteDialog(){
        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.AppTheme);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        builder.setMessage(getResources().getString(R.string.confirm_delete));
        builder.setCancelable(true);
        builder.setPositiveButton(
                getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        SaveManager saver = new SaveManager(builder.getContext());
                        if (saver.getModuleList(type).size() == 1)
                            showErrorDialog();
                        else{
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
    
    private void showErrorDialog(){
        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.AppTheme);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        builder.setMessage(getResources().getString(R.string.cannot_delete));
        builder.setCancelable(true);
    
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private void exitEditOptions(){
        Intent intent = new Intent(this, OptionsListActivity.class);
        intent.setData(Uri.parse(type + ""));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    
}
