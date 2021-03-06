/*
 * Copyright (c) 2018.
 * This file is part of Memorisia.
 *
 * Memorisia is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Memorisia is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Memorisia.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.clubinfo.insat.memorisia.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
import com.clubinfo.insat.memorisia.database.MemorisiaDatabase;
import com.clubinfo.insat.memorisia.fragments.BaseFragment;
import com.clubinfo.insat.memorisia.fragments.CalendarFragment;
import com.clubinfo.insat.memorisia.fragments.HomeFragment;
import com.clubinfo.insat.memorisia.fragments.WorkViewFragment;
import com.clubinfo.insat.memorisia.fragments.SubjectsFragment;
import com.clubinfo.insat.memorisia.fragments.WorkTypesFragment;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    public enum Frags {
        FRAG_HOME,
        FRAG_SUBJECTS,
        FRAG_WORKS,
        FRAG_WORK_TYPES,
        FRAG_CALENDAR
    }
    
    public static String PACKAGE_NAME;
    Menu menu;
    MenuItem editButton;
    MenuItem sortButton;
    MenuItem agendaButton;
    private Context context;
    private boolean isNightMode;
    private List<Integer> selectedAgendas = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setNightMode(this, false);
        setContentView(R.layout.activity_main);
        
        isNightMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);

        context = this;
        PACKAGE_NAME = getPackageName();
        selectedAgendas = Utils.getSelectedAgendasFromPrefs(context);
        
        if (savedInstanceState == null)
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment(), Frags.FRAG_HOME.name()).commit();
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewWork();
            }
        });
        
        Utils.checkModulesPresent(this);
        Utils.checkWorkOutdated(this);
    }
    
    /**
     * Creates a new {@link com.clubinfo.insat.memorisia.activities.EditWorkActivity EditWorkActivity}
     * with default values.
     */
    private void createNewWork() {
        Intent intent = new Intent(context, EditWorkActivity.class);
        WorkModule work = new WorkModule( -1, -1, -1, 0, new int[]{-1, -1, -1}, new int[]{-1, -1}, "", false);
        work.setId(-1);
        if (isFragmentActive(Frags.FRAG_WORKS)) {
            WorkViewFragment workFragment = (WorkViewFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_WORKS.name());
            if (workFragment.getParentType() == OptionModule.SUBJECT)
                work.setSubjectId(workFragment.getParentId());
            
            if (workFragment.getParentType() == OptionModule.WORK_TYPE)
                work.setWorkTypeId(workFragment.getParentId());
            
        }
        else if (isFragmentActive(Frags.FRAG_CALENDAR)){
            CalendarFragment calendarFragment = (CalendarFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_CALENDAR.name());
            work.setDate(calendarFragment.getSelectedDate());
        }
        List<Integer> agendas = getSelectedAgendas();
        if (agendas.size() == 1)
            work.setAgendaId(agendas.get(0));
        
        intent.putExtras(ModulesUtils.createBundleFromModule(work));
        context.startActivity(intent);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        if (nightMode != isNightMode) { // Restart activity to apply night mode
            Intent intent = getIntent();
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP); // Prevent loops in back button
            finish();
            startActivity(intent);
        }
        if (agendaButton != null)
            generateAgendaMenu(agendaButton.getSubMenu());
    }
    
    /**
     * Displays the edit subjects button if the
     * {@link com.clubinfo.insat.memorisia.fragments.SubjectsFragment SubjectsFragment} or
     * {@link WorkViewFragment WorkViewFragment}
     * are active
     */
    private void checkEditButtonState() {
        if (isFragmentActive(Frags.FRAG_SUBJECTS) || isFragmentActive(Frags.FRAG_WORK_TYPES) || isFragmentActive(Frags.FRAG_WORKS)) {
            editButton.setVisible(true);
        } else {
            editButton.setVisible(false);
        }
    }
    
    /**
     * Displays the sort button if the
     * {@link com.clubinfo.insat.memorisia.fragments.SubjectsFragment SubjectsFragment} or
     * {@link WorkViewFragment WorkViewFragment} or
     * {@link com.clubinfo.insat.memorisia.fragments.CalendarFragment CalendarFragment}
     * are active
     */
    public void checkSortButtonState() {
        if (isFragmentActive(Frags.FRAG_SUBJECTS) || isFragmentActive(Frags.FRAG_WORKS)
                || isFragmentActive(Frags.FRAG_WORK_TYPES) || isFragmentActive(Frags.FRAG_CALENDAR)) {
            sortButton.setVisible(true);
        } else {
            sortButton.setVisible(false);
        }
    }
    
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        Utils.setToolbarIconWhite(editButton);
        Utils.setToolbarIconWhite(sortButton);
        Utils.setToolbarIconWhite(agendaButton);
        
        Menu subMenu = sortButton.getSubMenu();
        generateSortMenu(subMenu, isFragmentActive(Frags.FRAG_WORKS) || isFragmentActive(Frags.FRAG_CALENDAR));
        
        generateAgendaMenu(agendaButton.getSubMenu());
        return true;
    }
    
    /**
     * Generates the corresponding sort menu for the active fragment
     *
     * @param menu       Sort menu holding the sort options
     * @param isWorks    Is the menu for subjects sorting or work sorting?
     */
    public void generateSortMenu(Menu menu, boolean isWorks) {
        menu.clear();
        menu.add(0, R.id.sort_1, Menu.NONE, R.string.sort_name);
        if (isWorks) {
            menu.add(0, R.id.sort_2, Menu.NONE, R.string.sort_priority);
            menu.add(0, R.id.sort_3, Menu.NONE, R.string.sort_type);
            menu.add(0, R.id.sort_4, Menu.NONE, R.string.sort_date);
        } else {
            menu.add(0, R.id.sort_2, Menu.NONE, R.string.sort_percent);
            menu.add(0, R.id.sort_3, Menu.NONE, R.string.sort_total_work);
        }
        SubjectsFragment subjects = (SubjectsFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_SUBJECTS.name());
        WorkViewFragment works = (WorkViewFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_WORKS.name());
        WorkTypesFragment workTypes = (WorkTypesFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_WORK_TYPES.name());
        CalendarFragment calendar = (CalendarFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_CALENDAR.name());
        if (isFragmentActive(Frags.FRAG_WORKS))
            changeSortMenuItemIcon(menu.getItem(works.getCurrentSortType().ordinal()), works.isReverseSort());
        else if (isFragmentActive(Frags.FRAG_WORK_TYPES))
            changeSortMenuItemIcon(menu.getItem(workTypes.getCurrentSortType().ordinal()), workTypes.isReverseSort());
        else if (isFragmentActive(Frags.FRAG_SUBJECTS))
            changeSortMenuItemIcon(menu.getItem(subjects.getCurrentSortType().ordinal()), subjects.isReverseSort());
        else if (isFragmentActive(Frags.FRAG_CALENDAR))
            changeSortMenuItemIcon(menu.getItem(calendar.getCurrentSortType().ordinal()), calendar.isReverseSort());
    }
    
    /**
     * Generates the agenda menu in the toolbar allowing the user to select which ones to display
     *
     * @param menu Menu holding the agendas
     */
    public void generateAgendaMenu(final Menu menu) {
        selectedAgendas = Utils.getSelectedAgendasFromPrefs(context);
        List<OptionModule> modules = MemorisiaDatabase.getInstance(context).optionModuleDao().getOptionModulesOfType(OptionModule.AGENDA);
        menu.clear();
        for (int i = 0; i < modules.size(); i++) {
            MenuItem item = menu.add(0, modules.get(i).getId(), Menu.NONE, modules.get(i).getText());
            item.setCheckable(true);
            item.setChecked(selectedAgendas.contains(modules.get(i).getId())); // Check the entry if user previously selected it
            // Update the selected agendas list on click and re-generate the corresponding list
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (!menuItem.isChecked()) {
                        menuItem.setChecked(true);
                        selectedAgendas.add(menuItem.getItemId());
                    } else if (canDeselectAgenda(menuItem.getItemId())) {
                        for (int i = 0; i < selectedAgendas.size(); i++) {
                            if (selectedAgendas.get(i) == menuItem.getItemId())
                                selectedAgendas.remove(i);
                        }
                        menuItem.setChecked(false);
                    }
                    Utils.saveSelectedAgendasToPrefs(context, selectedAgendas);
                    SubjectsFragment subjects = (SubjectsFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_SUBJECTS.name());
                    WorkViewFragment works = (WorkViewFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_WORKS.name());
                    WorkTypesFragment workTypes = (WorkTypesFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_WORK_TYPES.name());
                    CalendarFragment calendar = (CalendarFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_CALENDAR.name());
                    HomeFragment home = (HomeFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_HOME.name());
                    if (isFragmentActive(Frags.FRAG_SUBJECTS))
                        subjects.generateList();
                    else if (isFragmentActive(Frags.FRAG_WORKS))
                        works.generateList();
                    else if (isFragmentActive(Frags.FRAG_WORK_TYPES))
                        workTypes.generateList();
                    else if (isFragmentActive(Frags.FRAG_CALENDAR))
                        calendar.generateList();
                    else if (isFragmentActive(Frags.FRAG_HOME))
                        home.generateList();
                    
                    return false;
                }
            });
        }
    }
    
    /**
     * Checks if the selected agenda in the menu is the only one active.
     * If it is the only one, prevents the user from deselecting it.
     *
     * @param id Agenda id
     * @return True if the user can deselect the agenda, false otherwise
     */
    private boolean canDeselectAgenda(int id) {
        Menu menu = agendaButton.getSubMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isChecked() && menu.getItem(i).getItemId() != id)
                return true;
        }
        return false;
    }
    
    /**
     * @return Reference to the {@link android.view.MenuItem MenuItem} holding the sort options
     */
    public MenuItem getSortButton() {
        return sortButton;
    }
    
    /**
     * @return List of the selected agendas
     */
    public List<Integer> getSelectedAgendas() {
        return selectedAgendas;
    }
    
    /**
     * @param id id of the agenda to select
     */
    public void addAgendaToSelected(int id) {
        selectedAgendas.add(id);
        Utils.saveSelectedAgendasToPrefs(this, selectedAgendas);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        BaseFragment.SortType sort = BaseFragment.SortType.SORT_1;
        SubjectsFragment subjects = (SubjectsFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_SUBJECTS.name());
        WorkViewFragment works = (WorkViewFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_WORKS.name());
        WorkTypesFragment workTypes = (WorkTypesFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_WORK_TYPES.name());
        CalendarFragment calendar = (CalendarFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_CALENDAR.name());
        switch (id) {
            case R.id.action_edit:
                if (isFragmentActive(Frags.FRAG_WORKS))
                    editCurrentOptionModule(works.getParentId());
                else if (isFragmentActive(Frags.FRAG_SUBJECTS))
                    editModules(OptionModule.SUBJECT);
                else if (isFragmentActive(Frags.FRAG_WORK_TYPES))
                    editModules(OptionModule.WORK_TYPE);
                break;
            case R.id.sort_1:
                sort = BaseFragment.SortType.SORT_1;
                break;
            case R.id.sort_2:
                sort = BaseFragment.SortType.SORT_2;
                break;
            case R.id.sort_3:
                sort = BaseFragment.SortType.SORT_3;
                break;
            case R.id.sort_4:
                sort = BaseFragment.SortType.SORT_4;
                break;
        }
        if ((id == R.id.sort_1 || id == R.id.sort_2 || id == R.id.sort_3 || id == R.id.sort_4)){
            if (isFragmentActive(Frags.FRAG_SUBJECTS)) {
                subjects.setSortType(sort, true);
                changeSortMenuItemIcon(item, subjects.isReverseSort());
            } else if (isFragmentActive(Frags.FRAG_WORK_TYPES)) {
                workTypes.setSortType(sort, false);
                changeSortMenuItemIcon(item, workTypes.isReverseSort());
            }else if (isFragmentActive(Frags.FRAG_WORKS)) {
                works.setSortType(sort, false);
                changeSortMenuItemIcon(item, works.isReverseSort());
            } else if (isFragmentActive(Frags.FRAG_CALENDAR)) {
                calendar.setSortType(sort, false);
                changeSortMenuItemIcon(item, calendar.isReverseSort());
            }
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Creates a new {@link com.clubinfo.insat.memorisia.activities.EditOptionsActivity EditOptionsActivity}
     * corresponding to the given subject.
     *
     * @param id Id of the option module to edit
     */
    private void editCurrentOptionModule(int id) {
        Intent intent = new Intent(this, EditOptionsActivity.class);
        Bundle b = new Bundle();
        b.putInt("id", id);
        intent.putExtras(b);
        startActivity(intent);
    }
    
    /**
     * Creates a new {@link com.clubinfo.insat.memorisia.activities.OptionsListActivity OptionsListActivity}
     */
    private void editModules(int type) {
        Intent intent = new Intent(this, OptionsListActivity.class);
        intent.setData(Uri.parse(type + ""));
        startActivity(intent);
    }
    
    /**
     * Changes the icon of a given MenuItem to display the current sorting order
     *
     * @param item      Item that will have its icon changed
     * @param isReverse True to make the arrow point upward, false for downward
     */
    public void changeSortMenuItemIcon(MenuItem item, boolean isReverse) {
        for (int i = 0; i < sortButton.getSubMenu().size(); i++) {
            MenuItem it = sortButton.getSubMenu().getItem(i);
            if (it.getItemId() != item.getItemId())
                it.setIcon(0);
            if (!isReverse)
                item.setIcon(R.drawable.ic_arrow_downward_black_24dp);
            else
                item.setIcon(R.drawable.ic_arrow_upward_black_24dp);
        }
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = new HomeFragment();
        String tag = null;
        editButton.setVisible(false);
        sortButton.setVisible(false);
        if (id == R.id.nav_home) { // Display the home fragment
            fragment = new HomeFragment();
            tag = Frags.FRAG_HOME.name();
        } else if (id == R.id.nav_subjects) { // Display the subjects fragment
            fragment = new SubjectsFragment();
            tag = Frags.FRAG_SUBJECTS.name();
            editButton.setVisible(true);
            sortButton.setVisible(true);
        } else if (id == R.id.nav_work_types) { // Display the work types fragment
            fragment = new WorkTypesFragment();
            tag = Frags.FRAG_WORK_TYPES.name();
            editButton.setVisible(true);
            sortButton.setVisible(true);
        } else if (id == R.id.nav_calendar) { // Display the calendar fragment
            fragment = new CalendarFragment();
            tag = Frags.FRAG_CALENDAR.name();
            editButton.setVisible(false);
            sortButton.setVisible(true);
        } else if (id == R.id.nav_settings) { // Display the settings activity
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_about) { // Display the about activity
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        // Remove backstack to prevent problems when pressing back button
        FragmentManager fm = getFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.content_frame, fragment, tag);
        ft.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    
    public Frags getActiveFragment(){
        SubjectsFragment subjects = (SubjectsFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_SUBJECTS.name());
        WorkViewFragment works = (WorkViewFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_WORKS.name());
        WorkTypesFragment workTypes = (WorkTypesFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_WORK_TYPES.name());
        CalendarFragment calendar = (CalendarFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_CALENDAR.name());
        HomeFragment home = (HomeFragment) getFragmentManager().findFragmentByTag(Frags.FRAG_HOME.name());
        if (subjects != null && subjects.isVisible())
            return Frags.FRAG_SUBJECTS;
        else if (works != null && works.isVisible())
            return Frags.FRAG_WORKS;
        else if (workTypes != null && workTypes.isVisible())
            return Frags.FRAG_WORK_TYPES;
        else if (calendar != null && calendar.isVisible())
            return Frags.FRAG_CALENDAR;
        else if (home != null && home.isVisible())
            return Frags.FRAG_HOME;
        else
            return null;
    }
    
    public boolean isFragmentActive(Frags Frag){
        return getActiveFragment() == Frag;
    }
    
}
