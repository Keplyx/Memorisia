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
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.fragments.CalendarFragment;
import com.clubinfo.insat.memorisia.fragments.HomeFragment;
import com.clubinfo.insat.memorisia.fragments.SubjectsFragment;
import com.clubinfo.insat.memorisia.fragments.WorkViewFragment;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    public static String PACKAGE_NAME;
    
    private Context context;
    
    Menu menu;
    MenuItem editButton;
    MenuItem sortButton;
    MenuItem agendaButton;
    private boolean isNightMode;
    
    private List<Integer> selectedAgendas = new ArrayList<>();
    
    public static final String FRAG_HOME = "HOME";
    public static final String FRAG_SUBJECTS = "SUBJECTS";
    public static final String FRAG_CALENDAR = "CALENDAR";
    public static final String FRAG_WORKS = "WORKS";
    
    
    private static final int MENU_SORT_NAME = Menu.FIRST;
    private static final int MENU_SORT_DONE_PERCENT = Menu.FIRST + 1;
    private static final int MENU_SORT_TOTAL_WORK = Menu.FIRST + 2;
    private static final int MENU_SORT_PRIORITY = Menu.FIRST + 3;
    private static final int MENU_SORT_WORK_TYPE = Menu.FIRST + 4;
    
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
                Bundle b = new Bundle();
                b.putString("text", "");
                b.putInt("workTypeId", -1);
                b.putInt("subjectId", -1);
                b.putInt("agendaId", -1);
                b.putInt("priority", -1);
                b.putInt("id", -1);
                b.putBoolean("state", false);
                b.putBoolean("notifications", false);
                WorkViewFragment works = (WorkViewFragment) getFragmentManager().findFragmentByTag(FRAG_WORKS);
                if (works != null && works.isVisible()) {
                    SaveManager saver = new SaveManager(context);
                    OptionModule module = ModulesUtils.getModuleOfId(saver.getOptionModuleList(SaveManager.SUBJECT), works.getSubjectId());
                    b.putInt("subjectId", module.getId());
                }
                intent.putExtras(b);
                context.startActivity(intent);
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
        if (agendaButton != null)
            generateAgendaMenu(agendaButton.getSubMenu());
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
    
    public void checkSortButtonState(){
        SubjectsFragment subjects = (SubjectsFragment) getFragmentManager().findFragmentByTag(FRAG_SUBJECTS);
        WorkViewFragment works = (WorkViewFragment) getFragmentManager().findFragmentByTag(FRAG_WORKS);
        if (subjects != null && subjects.isVisible() || (works != null && works.isVisible())) {
            sortButton.setVisible(true);
        }
        else{
            sortButton.setVisible(false);
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
    public boolean onCreateOptionsMenu(Menu m) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu = m;
        getMenuInflater().inflate(R.menu.main, menu);
        editButton = menu.findItem(R.id.action_edit);
        sortButton = menu.findItem(R.id.action_sort);
        agendaButton = menu.findItem(R.id.action_agenda);
        checkEditButtonState();
        checkSortButtonState();
        setToolbarIconWhite(editButton);
        setToolbarIconWhite(sortButton);
        setToolbarIconWhite(agendaButton);
        
        SubjectsFragment subjects = (SubjectsFragment) getFragmentManager().findFragmentByTag(FRAG_SUBJECTS);
        WorkViewFragment works = (WorkViewFragment) getFragmentManager().findFragmentByTag(FRAG_WORKS);
        Menu subMenu = sortButton.getSubMenu();
        if (subjects != null && subjects.isVisible())
            generateSortMenu(subMenu, true);
        else if (works != null && works.isVisible())
            generateSortMenu(subMenu, false);
        generateAgendaMenu(agendaButton.getSubMenu());
        return true;
    }
    
    private void setToolbarIconWhite(MenuItem item){
        Drawable icon = item.getIcon();
        icon.mutate().setColorFilter(Color.argb(255, 255, 255, 255), PorterDuff.Mode.SRC_IN);
        item.setIcon(icon);
    }
    
    public void generateSortMenu(Menu menu, boolean isSubjects){
        menu.clear();
        menu.add(0, MENU_SORT_NAME, Menu.NONE, R.string.sort_name);
        if (isSubjects) {
            menu.add(0, MENU_SORT_DONE_PERCENT, Menu.NONE, R.string.sort_percent);
            menu.add(0, MENU_SORT_TOTAL_WORK, Menu.NONE, R.string.sort_total_work);
        }
        else {
            menu.add(0, MENU_SORT_PRIORITY, Menu.NONE, R.string.sort_priority);
            menu.add(0, MENU_SORT_WORK_TYPE, Menu.NONE, R.string.sort_type);
        }
    }
    
    public void generateAgendaMenu(final Menu menu){
        SaveManager saver = new SaveManager(this);
        List<OptionModule> modules = saver.getOptionModuleList(SaveManager.AGENDA);
        menu.clear();
        for (int i = 0; i < modules.size(); i++) {
            MenuItem item = menu.add(0, modules.get(i).getId(), Menu.NONE, modules.get(i).getText());
            item.setCheckable(true);
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    menuItem.setChecked(!menuItem.isChecked());
                    if (menuItem.isChecked())
                        selectedAgendas.add(menuItem.getItemId());
                    else {
                        for (int i = 0; i < selectedAgendas.size(); i++){
                            if (selectedAgendas.get(i) == menuItem.getItemId())
                                selectedAgendas.remove(i);
                        }
                    }
                    SubjectsFragment subjects = (SubjectsFragment) getFragmentManager().findFragmentByTag(FRAG_SUBJECTS);
                    WorkViewFragment works = (WorkViewFragment) getFragmentManager().findFragmentByTag(FRAG_WORKS);
                    if (subjects != null && subjects.isVisible())
                        subjects.generateSubjectsList();
                    else if (works != null && works.isVisible())
                        works.generateWorksList();
                    return false;
                }
            });
        }
    }
    
    public Menu getMenu() {
        return menu;
    }
    
    public MenuItem getSortButton() {
        return sortButton;
    }
    
    public List<Integer> getSelectedAgendas() {
        return selectedAgendas;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SubjectsFragment subjects = (SubjectsFragment) getFragmentManager().findFragmentByTag(FRAG_SUBJECTS);
        WorkViewFragment works = (WorkViewFragment) getFragmentManager().findFragmentByTag(FRAG_WORKS);
        switch (id){
            case R.id.action_edit:
                if (works != null && works.isVisible()) {
                    Intent intent = new Intent(this, EditOptionsActivity.class);
                    SaveManager saver = new SaveManager(this);
                    OptionModule module = ModulesUtils.getModuleOfId(saver.getOptionModuleList(SaveManager.SUBJECT), works.getSubjectId());
                    Bundle b = new Bundle();
                    b.putString("name", module.getText());
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
            case MENU_SORT_NAME:
                if (subjects != null && subjects.isVisible()) {
                    subjects.setSortType(SubjectsFragment.SORT_NAME);
                    changeSortMenuItemIcon(item, subjects.isReverseSort());
                }
                else if (works != null && works.isVisible()) {
                    works.setSortType(WorkViewFragment.SORT_NAME);
                    changeSortMenuItemIcon(item, works.isReverseSort());
                }
                break;
            case MENU_SORT_DONE_PERCENT:
                if (subjects != null && subjects.isVisible()) {
                    subjects.setSortType(SubjectsFragment.SORT_DONE_PERCENT);
                    changeSortMenuItemIcon(item, subjects.isReverseSort());
                }
                break;
            case MENU_SORT_TOTAL_WORK:
                if (subjects != null && subjects.isVisible()) {
                    subjects.setSortType(SubjectsFragment.SORT_TOTAL_WORK);
                    changeSortMenuItemIcon(item, subjects.isReverseSort());
                }
                break;
            case MENU_SORT_PRIORITY:
                if (works != null && works.isVisible()) {
                    works.setSortType(WorkViewFragment.SORT_PRIORITY);
                    changeSortMenuItemIcon(item, works.isReverseSort());
                }
                break;
            case MENU_SORT_WORK_TYPE:
                if (works != null && works.isVisible()) {
                    works.setSortType(WorkViewFragment.SORT_WORK_TYPE);
                    changeSortMenuItemIcon(item, works.isReverseSort());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void changeSortMenuItemIcon(MenuItem item, boolean isReverse){
        for (int i = 0; i < sortButton.getSubMenu().size(); i++){
            MenuItem it = sortButton.getSubMenu().getItem(i);
            if (it.getItemId() != item.getItemId())
                it.setIcon(0);
            else if (!isReverse)
                it.setIcon(R.drawable.ic_arrow_downward_black_24dp);
            else if (isReverse)
                it.setIcon(R.drawable.ic_arrow_upward_black_24dp);
        }
    }
    
    
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = new HomeFragment();
        String tag = null;
        editButton.setVisible(false);
        sortButton.setVisible(false);
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            tag = FRAG_HOME;
        } else if (id == R.id.nav_subjects) {
            fragment = new SubjectsFragment();
            tag = FRAG_SUBJECTS;
            editButton.setVisible(true);
            sortButton.setVisible(true);
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
