package com.clubinfo.insat.memorisia;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class SettingsFragment extends Fragment {
    Switch nightModeSwitch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, container, false);
        getActivity().setTitle(R.string.settings_title);
        final SharedPreferences sharedPref = getActivity().getSharedPreferences("general_pref", Context.MODE_PRIVATE);
        
        nightModeSwitch = v.findViewById(R.id.nightModeSwitch);
        nightModeSwitch.setChecked(sharedPref.getBoolean("night_mode", false));
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("night_mode", isChecked);
                editor.commit();
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
        });
        return v;
    }
    
}
