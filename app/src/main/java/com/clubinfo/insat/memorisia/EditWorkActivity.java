package com.clubinfo.insat.memorisia;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class EditWorkActivity extends AppCompatActivity {
    
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
