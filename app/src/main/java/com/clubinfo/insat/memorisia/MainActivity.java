package com.clubinfo.insat.memorisia;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    MenuItem editButton;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        SharedPreferences mPrefs = getSharedPreferences("general_pref", Context.MODE_PRIVATE);
        if (mPrefs.getBoolean("night_mode", false))
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
        editButton.setVisible(false);
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
                Intent intent = new Intent(this, OptionsListActivity.class);
                Bundle b = new Bundle();
                b.putInt("option", 0);
                intent.putExtras(b);
                startActivity(intent);
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
        editButton.setVisible(false);
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_subjects) {
            fragment = new SubjectsFragment();
            editButton.setVisible(true);
        } else if (id == R.id.nav_calendar) {
            fragment = new CalendarFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
        }
    
        
        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    
    
    public void onClickOptionButton(View v) {
        int id = v.getId();
        Intent intent = new Intent(this, OptionsListActivity.class);
        Bundle b = new Bundle();
        switch (id){
            default:
                b.putInt("option", 0);
                break;
            case R.id.editWork:
                b.putInt("option", 1);
                break;
            case R.id.editAgenda:
                b.putInt("option", 2);
                break;
        }
        intent.putExtras(b);
        startActivity(intent);
        //finish();
    }
}
