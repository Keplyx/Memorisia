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

package com.clubinfo.insat.memorisia.utils;


import android.content.Context;
import android.os.Bundle;

import com.clubinfo.insat.memorisia.database.MemorisiaDatabase;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class providing static methods to manage {@link com.clubinfo.insat.memorisia.modules.Module modules}
 */
public class ModulesUtils {
    
    /**
     * Sorts the given {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} list alphabetically (0-1, A-Z).
     *
     * @param modules {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} list to sort
     * @param reverse True to reverse the sorting order (Z-A instead of A-Z)
     * @return Sorted list
     */
    public static List<OptionModule> sortOptionModuleListByName(List<OptionModule> modules, final boolean reverse) {
        Collections.sort(modules, new Comparator<OptionModule>() {
            @Override
            public int compare(OptionModule optionModule, OptionModule t1) {
                int r = reverse ? -1 : 1;
                return r * optionModule.getText().compareTo(t1.getText());
            }
        });
        return modules;
    }
    
    /**
     * Sorts the given {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list alphabetically (0-1, A-Z).
     *
     * @param modules {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list to sort
     * @param reverse True to reverse the sorting order (Z-A instead of A-Z)
     * @return Sorted list
     */
    public static List<WorkModule> sortWorkModuleListByName(List<WorkModule> modules, final boolean reverse) {
        Collections.sort(modules, new Comparator<WorkModule>() {
            @Override
            public int compare(WorkModule optionModule, WorkModule t1) {
                int r = reverse ? -1 : 1;
                return r * optionModule.getText().compareTo(t1.getText());
            }
        });
        return modules;
    }
    
    /**
     * Sorts the given {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} list by the percent of work done,
     * lower percent first. Modules without work will always be placed at the end, no matter if the sorting is reversed or not.
     *
     * @param modules {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} list to sort
     * @param agendas Selected agendas id to search work in
     * @param context Current context
     * @param reverse True to reverse the sorting order (Higher percent first instead of lower)
     * @return Sorted list
     */
    public static List<OptionModule> sortOptionModuleListByDonePercent(List<OptionModule> modules, final List<Integer> agendas, final Context context, final boolean reverse) {
        Collections.sort(modules, new Comparator<OptionModule>() {
            @Override
            public int compare(OptionModule optionModule, OptionModule t1) {
                MemorisiaDatabase db = MemorisiaDatabase.getInstance(context);
                List<WorkModule> worksList1 = new ArrayList<>();
                List<WorkModule> worksList2 = new ArrayList<>();
                if (optionModule.getType() == OptionModule.SUBJECT)
                    worksList1 = db.workModuleDao().getWorkModulesOfSubject(agendas, optionModule.getId());
                else if (optionModule.getType() == OptionModule.WORK_TYPE)
                    worksList1 = db.workModuleDao().getWorkModulesOfWorkType(agendas, optionModule.getId());
    
                if (t1.getType() == OptionModule.SUBJECT)
                    worksList2 = db.workModuleDao().getWorkModulesOfSubject(agendas, t1.getId());
                else if (t1.getType() == OptionModule.WORK_TYPE)
                    worksList2 = db.workModuleDao().getWorkModulesOfWorkType(agendas, t1.getId());
                
                String compare1 = "999", compare2 = "999";
                if (reverse) {
                    compare1 = "0";
                    compare2 = "0";
                }
                int r = reverse ? -1 : 1;
                if (worksList1.size() > 0)
                    compare1 = "" + (double) getWorkDoneNumber(worksList1) / worksList1.size();
                if (worksList2.size() > 0)
                    compare2 = "" + (double) getWorkDoneNumber(worksList2) / worksList2.size();
                return r * compare1.compareTo(compare2);
            }
        });
        return modules;
    }
    
    /**
     * Sorts the given {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} list by the total amount of work,
     * lower amount first.
     *
     * @param modules {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} list to sort
     * @param agendas Selected agendas id to search work in
     * @param context Current context
     * @param reverse True to reverse the sorting order (Higher amount first instead of lower)
     * @return Sorted list
     */
    public static List<OptionModule> sortOptionModuleListByTotalWork(List<OptionModule> modules, final List<Integer> agendas, final Context context, final boolean reverse) {
        Collections.sort(modules, new Comparator<OptionModule>() {
            @Override
            public int compare(OptionModule optionModule, OptionModule t1) {
                MemorisiaDatabase db = MemorisiaDatabase.getInstance(context);
                List<WorkModule> worksList1 = new ArrayList<>();
                List<WorkModule> worksList2 = new ArrayList<>();
                if (optionModule.getType() == OptionModule.SUBJECT)
                    worksList1 = db.workModuleDao().getWorkModulesOfSubject(agendas, optionModule.getId());
                else if (optionModule.getType() == OptionModule.WORK_TYPE)
                    worksList1 = db.workModuleDao().getWorkModulesOfWorkType(agendas, optionModule.getId());
                
                if (t1.getType() == OptionModule.SUBJECT)
                    worksList2 = db.workModuleDao().getWorkModulesOfSubject(agendas, t1.getId());
                else if (t1.getType() == OptionModule.WORK_TYPE)
                    worksList2 = db.workModuleDao().getWorkModulesOfWorkType(agendas, t1.getId());
                
                String compare1 = "0", compare2 = "0";
                if (worksList1.size() > 0)
                    compare1 = "" + worksList1.size();
                if (worksList2.size() > 0)
                    compare2 = "" + worksList2.size();
                int r = reverse ? -1 : 1;
                return r * compare1.compareTo(compare2);
            }
        });
        return modules;
    }
    
    /**
     * Sorts the given {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list by priority,
     * lower first.
     *
     * @param modules {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list to sort
     * @param reverse True to reverse the sorting order (Higher first instead of lower)
     * @return Sorted list
     */
    public static List<WorkModule> sortWorkModuleListByPriority(List<WorkModule> modules, final boolean reverse) {
        Collections.sort(modules, new Comparator<WorkModule>() {
            @Override
            public int compare(WorkModule m1, WorkModule m2) {
                int r = reverse ? -1 : 1;
                return r * (m1.getPriority() - m2.getPriority());
            }
        });
        return modules;
    }
    
    /**
     * Gets {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} items,
     * with the specified priority.
     *
     * @param modules {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list to sort
     * @param priority      priority to search for
     * @return Filtered list
     */
    public static List<WorkModule> getWorkModuleListByPriority(List<WorkModule> modules, int priority) {
        List<WorkModule> FilteredModules = new ArrayList<>();
        for (WorkModule work : modules){
            if (work.getPriority() == priority)
                FilteredModules.add(work);
        }
        return FilteredModules;
    }
    
    /**
     * Sorts the given {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list by work type id,
     * lower id first.
     *
     * @param modules {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list to sort
     * @param context Current context
     * @param reverse True to reverse the sorting order (Higher first instead of lower)
     * @return Sorted list
     */
    public static List<WorkModule> sortWorkModuleListByWorkType(List<WorkModule> modules, final Context context, final boolean reverse) {
        Collections.sort(modules, new Comparator<WorkModule>() {
            @Override
            public int compare(WorkModule m1, WorkModule m2) {
                MemorisiaDatabase db = MemorisiaDatabase.getInstance(context);
                OptionModule o1 = db.optionModuleDao().getOptionModuleOfId(m1.getWorkTypeId());
                OptionModule o2 = db.optionModuleDao().getOptionModuleOfId(m2.getWorkTypeId());
                int r = reverse ? -1 : 1;
                return r * o1.getText().compareTo(o2.getText());
            }
        });
        return modules;
    }
    
    /**
     * Sorts the given {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list by due date,
     * first comparing the date, then time if both dates are the same. Due sooner first. Works without date
     * will be placed at the end no matter if the sorting is reversed or not.
     *
     * @param modules {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list to sort
     * @param reverse True to reverse the sorting order (Due later first instead of sooner)
     * @return Sorted list
     */
    public static List<WorkModule> sortWorkModuleListByDate(List<WorkModule> modules, final boolean reverse) {
        Collections.sort(modules, new Comparator<WorkModule>() {
            @Override
            public int compare(WorkModule m1, WorkModule m2) {
                int r = reverse ? -1 : 1;
                int dateDelta = Utils.getDateDelta(m2.getDate(), m1.getDate());
                if (m1.getDate()[0] == -1 && m2.getDate()[0] == -1)
                    return 0;
                // Put works without date at the end
                if (m1.getDate()[0] == -1)
                    return 1;
                if (m2.getDate()[0] == -1)
                    return -1;
                if (dateDelta != 0)
                    return r * dateDelta;
                
                int timeDelta = Utils.getTimeDelta(m2.getTime(), m1.getTime());
                // Put works without time at the bottom
                if (m1.getTime()[0] == -1)
                    return 1;
                if (m2.getTime()[0] == -1)
                    return -1;
                if (timeDelta != 0)
                    return r * timeDelta;

                return 0;
            }
        });
        return modules;
    }
    
    /**
     * Gets {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} items,
     * due for the next 7 days.
     *
     * @param modules {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list to sort
     * @param date      date to start at
     * @return Filtered list
     */
    public static List<WorkModule> getWorkModuleListByWeek(List<WorkModule> modules, int[] date) {
        Calendar deadLine = CalendarDay.from(date[2], date[1] - 1, date[0]).getCalendar();
        Calendar start = CalendarDay.from(deadLine).getCalendar();
        deadLine.add(Calendar.DAY_OF_MONTH, 7);
        List<WorkModule> FilteredModules = new ArrayList<>();
        for (WorkModule work : modules){
            Calendar current = CalendarDay.from(work.getDate()[2], work.getDate()[1] -1, work.getDate()[0]).getCalendar();
            if (work.getDate() == null || work.getDate()[0] == -1)
                continue;
            if (current.before(deadLine) && current.after(start) || current.equals(start))
                FilteredModules.add(work);
        }
        return FilteredModules;
    }
    
    /**
     * Gets {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} items,
     * with the same date as the one specified.
     *
     * @param modules {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list to sort
     * @param date      date to filter the list with
     * @return filtered list
     */
    public static List<WorkModule> getWorkModuleListByDate(List<WorkModule> modules, int[] date) {
        List<WorkModule> FilteredModules = new ArrayList<>();
        for (WorkModule work : modules){
            if (date != null){
                boolean valid = true;
                for (int i = 0; i < date.length; i++){
                    if (work.getDate() == null || work.getDate()[i] != date[i]) {
                        valid = false;
                        break;
                    }
                }
                if (valid)
                    FilteredModules.add(work);
            }
        }
        return FilteredModules;
    }
    
    /**
     * Gets the {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} corresponding to the given id in a list.
     *
     * @param modules {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} list to search in
     * @param id      {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} id to find
     * @return {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} corresponding to the given id, null if none was found
     */
    public static OptionModule getModuleOfId(List<OptionModule> modules, int id) {
        for (int i = 0; i < modules.size(); i++) {
            if (modules.get(i).getId() == id)
                return modules.get(i);
        }
        return null;
    }
    
    /**
     * Gets the position of an {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} in the given list.
     *
     * @param modules {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} list to search in
     * @param id      id of the {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} to find
     * @return Position of the {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule}, -1 if none was found
     */
    public static int getPosInList(List<OptionModule> modules, int id) {
        for (int i = 0; i < modules.size(); i++) {
            if (modules.get(i).getId() == id)
                return i;
        }
        return -1;
    }
    
    /**
     * Gets number of works done in a {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list.
     *
     * @param list {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list to search in
     * @return Number of work done
     */
    public static int getWorkDoneNumber(List<WorkModule> list) {
        int counter = 0;
        for (WorkModule work : list) {
            if (work.isState())
                counter++;
        }
        return counter;
    }
    
    /**
     * Creates a {@link android.os.Bundle Bundle} storing the properties of a given
     * {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule}.
     *
     * @param module {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} to store
     * @return Bundle containing the given {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} properties
     */
    public static Bundle createBundleFromModule(OptionModule module) {
        Bundle b = new Bundle();
        b.putInt("id", module.getId());
        b.putInt("type", module.getType());
        b.putString("text", module.getText());
        b.putString("logo", module.getLogo());
        b.putString("color", module.getColor());
        return b;
    }
    
    /**
     * Creates a {@link android.os.Bundle Bundle} storing the properties of a given
     * {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule}.
     *
     * @param module {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} to store
     * @return Bundle containing the given {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} properties
     */
    public static Bundle createBundleFromModule(WorkModule module) {
        Bundle b = new Bundle();
        b.putInt("id", module.getId());
        b.putInt("agendaId", module.getAgendaId());
        b.putInt("subjectId", module.getSubjectId());
        b.putInt("workTypeId", module.getWorkTypeId());
        b.putInt("priority", module.getPriority());
        b.putIntArray("date", module.getDate());
        b.putIntArray("time", module.getTime());
        b.putString("text", module.getText());
        b.putBoolean("state", module.isState());
        return b;
    }
    
    /**
     * Creates an {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} storing the properties of a given
     * {@link android.os.Bundle Bundle}.
     *
     * @param b {@link android.os.Bundle Bundle} to convert
     * @return {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} containing the given
     * {@link android.os.Bundle Bundle} properties
     */
    public static OptionModule createOptionModuleFromBundle(Bundle b) {
        int type = b.getInt("type");
        String text = b.getString("text");
        String logo = b.getString("logo");
        String color = b.getString("color");
        return new OptionModule(type, text, logo, color);
    }
    
    /**
     * Creates a {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} storing the properties of a given
     * {@link android.os.Bundle Bundle}.
     *
     * @param b {@link android.os.Bundle Bundle} to convert
     * @return {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} containing the given
     * {@link android.os.Bundle Bundle} properties
     */
    public static WorkModule createWorkModuleFromBundle(Bundle b) {
        int agendaId = b.getInt("agendaId");
        int subjectId = b.getInt("subjectId");
        int workTypeId = b.getInt("workTypeId");
        int priority = b.getInt("priority");
        int[] date = b.getIntArray("date");
        int[] time = b.getIntArray("time");
        String text = b.getString("text");
        boolean state = b.getBoolean("state");
        return new WorkModule(agendaId, subjectId, workTypeId, priority, date, time, text, state);
    }
}
