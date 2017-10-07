package com.clubinfo.insat.memorisia.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.clubinfo.insat.memorisia.fragments.CalendarFragment;
import com.clubinfo.insat.memorisia.fragments.HomeFragment;
import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.fragments.SubjectsFragment;
import com.clubinfo.insat.memorisia.fragments.WorkViewFragment;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    public static String PACKAGE_NAME;
    
    private Context context;
    
    MenuItem editButton;
    private boolean isNightMode;
    
    public static final String FRAG_HOME = "HOME";
    public static final String FRAG_SUBJECTS = "SUBJECTS";
    public static final String FRAG_CALENDAR = "CALENDAR";
    public static final String FRAG_WORKS = "WORKS";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        isNightMode = nightMode;
        if(nightMode)
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        
        setContentView(R.layout.activity_main);
    
        PACKAGE_NAME = getApplicationContext().getPackageName();
        
        if (savedInstanceState == null)
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    
        context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditWorkActivity.class);
                startActivity(intent);
            }
        });
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        if (nightMode != isNightMode){
            Intent intent = getIntent();
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP); // Prevent loops in back button
            finish();
            startActivity(intent);
        }
    }
    
    private void checkEditButtonState(){
        SubjectsFragment subjects = (SubjectsFragment) getFragmentManager().findFragmentByTag(FRAG_SUBJECTS);
        WorkViewFragment works = (WorkViewFragment) getFragmentManager().findFragmentByTag(FRAG_WORKS);
        if ((subjects != null && subjects.isVisible()) || (works != null && works.isVisible())) {
            editButton.setVisible(true);
        }
        else{
            editButton.setVisible(false);
        }
    }
    
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        editButton = menu.findItem(R.id.action_edit);
        checkEditButtonState();
        Drawable icon = editButton.getIcon();
        icon.mutate().setColorFilter(Color.argb(255, 255, 255, 255), PorterDuff.Mode.SRC_IN);
        editButton.setIcon(icon);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_edit:
                WorkViewFragment works = (WorkViewFragment) getFragmentManager().findFragmentByTag(FRAG_WORKS);
                if (works != null && works.isVisible()) {
                    Intent intent = new Intent(this, EditOptionsActivity.class);
                    SaveManager saver = new SaveManager(this);
                    OptionModule module = ModulesUtils.getModuleOfId(saver.getModuleList(SaveManager.SUBJECT), works.getSubjectId());
                    Bundle b = new Bundle();
                    b.putString("name", module.getName());
                    b.putString("logo", module.getLogo());
                    b.putString("color", module.getColor());
                    b.putInt("type", module.getType());
                    b.putInt("id", module.getId());
                    b.putBoolean("notifications", module.isNotificationsEnabled());
                    intent.putExtras(b);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(this, OptionsListActivity.class);
                    intent.setData(Uri.parse("0"));
                    startActivity(intent);
                }
              
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = new HomeFragment();
        String tag = null;
        editButton.setVisible(false);
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            tag = FRAG_HOME;
        } else if (id == R.id.nav_subjects) {
            fragment = new SubjectsFragment();
            tag = FRAG_SUBJECTS;
            editButton.setVisible(true);
        } else if (id == R.id.nav_calendar) {
            fragment = new CalendarFragment();
            tag = FRAG_CALENDAR;
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
    
        ft.replace(R.id.content_frame, fragment, tag).commit();
    
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
