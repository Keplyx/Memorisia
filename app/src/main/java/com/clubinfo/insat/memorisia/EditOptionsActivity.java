package com.clubinfo.insat.memorisia;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

public class EditOptionsActivity extends AppCompatActivity {
    
    private int type;
    
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
        // if has a name, we are editing, else we are creating a new option
        if (b != null && b.getString("name") != null){
            name.setText(b.getString("name"));
            logo.setImageResource(b.getInt("logo"));
            color.setBackgroundColor(b.getInt("color"));
            type = b.getInt("type");
            notifications.setChecked(b.getBoolean("notifications"));
            setTitle(getResources().getString(R.string.editing) + " " + b.getString("name"));
        }
        else if (b != null)
        {
            type = b.getInt("type");
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
            name.setText(R.string.name);
            color.setBackgroundColor(Color.parseColor("#ffffff"));
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
}
