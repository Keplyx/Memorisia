package com.clubinfo.insat.memorisia;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

public class EditOptionsActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        if(nightMode)
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_edit_options);
    
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    
        TextView name = (TextView) findViewById(R.id.moduleName);
        ImageButton logo = (ImageButton) findViewById(R.id.moduleLogo);
        Button color = (Button) findViewById(R.id.moduleColor);
        Switch notifications = (Switch) findViewById(R.id.moduleNotifications);
        
        Bundle b = getIntent().getExtras();
        if (b != null){
            name.setText(b.getString("name"));
            logo.setImageResource(b.getInt("logo"));
            color.setBackgroundColor(b.getInt("color"));
            notifications.setChecked(b.getBoolean("notifications"));
        }
        
        
        setTitle(b.getString("name"));
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
