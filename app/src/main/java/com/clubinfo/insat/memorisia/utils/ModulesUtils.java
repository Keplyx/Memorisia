package com.clubinfo.insat.memorisia.utils;


import android.content.Context;
import android.os.Bundle;

import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;

import java.util.ArrayList;
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
                if (!reverse)
                    return optionModule.getText().compareTo(t1.getText());
                else
                    return -1 * optionModule.getText().compareTo(t1.getText());
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
                if (!reverse)
                    return optionModule.getText().compareTo(t1.getText());
                else
                    return -1 * optionModule.getText().compareTo(t1.getText());
            }
        });
        return modules;
    }
    
    /**
     * Sorts the given {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule} list by the percent of work done,
     * lower percent first.
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
                SaveManager saver = new SaveManager(context);
                List<Integer> subjectId = new ArrayList<>();
                subjectId.add(optionModule.getId());
                List<WorkModule> worksList1 = saver.getWorkModuleList(agendas, subjectId, null);
                subjectId.remove(0);
                subjectId.add(t1.getId());
                List<WorkModule> worksList2 = saver.getWorkModuleList(agendas, subjectId, null);
                String compare1 = "999", compare2 = "999";
                if (reverse) {
                    compare1 = "0";
                    compare2 = "0";
                }
                if (worksList1.size() > 0)
                    compare1 = "" + (double) getWorkDoneNumber(worksList1) / worksList1.size();
                if (worksList2.size() > 0)
                    compare2 = "" + (double) getWorkDoneNumber(worksList2) / worksList2.size();
                if (!reverse)
                    return compare1.compareTo(compare2);
                else
                    return -1 * compare1.compareTo(compare2);
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
                SaveManager saver = new SaveManager(context);
                List<Integer> subjectId = new ArrayList<>();
                subjectId.add(optionModule.getId());
                List<WorkModule> worksList1 = saver.getWorkModuleList(agendas, subjectId, null);
                subjectId.remove(0);
                subjectId.add(t1.getId());
                List<WorkModule> worksList2 = saver.getWorkModuleList(agendas, subjectId, null);
                String compare1 = "0", compare2 = "0";
                if (worksList1.size() > 0)
                    compare1 = "" + worksList1.size();
                if (worksList2.size() > 0)
                    compare2 = "" + worksList2.size();
                if (!reverse)
                    return compare1.compareTo(compare2);
                else
                    return -1 * compare1.compareTo(compare2);
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
                if (!reverse)
                    return m1.getPriority() - m2.getPriority();
                else
                    return -1 * (m1.getPriority() - m2.getPriority());
            }
        });
        return modules;
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
                SaveManager saver = new SaveManager(context);
                List<OptionModule> optionModules = saver.getOptionModuleList(SaveManager.WORKTYPE);
                OptionModule o1 = getModuleOfId(optionModules, m1.getWorkTypeId());
                OptionModule o2 = getModuleOfId(optionModules, m2.getWorkTypeId());
                if (!reverse)
                    return o1.getText().compareTo(o2.getText());
                else
                    return -1 * o1.getText().compareTo(o2.getText());
            }
        });
        return modules;
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
        b.putString("name", module.getText());
        b.putString("logo", module.getLogo());
        b.putString("color", module.getColor());
        b.putInt("type", module.getType());
        b.putInt("id", module.getId());
        b.putBoolean("notifications", module.isNotificationsEnabled());
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
        b.putString("text", module.getText());
        b.putInt("workTypeId", module.getWorkTypeId());
        b.putInt("subjectId", module.getSubjectId());
        b.putInt("agendaId", module.getAgendaId());
        b.putInt("priority", module.getPriority());
        b.putInt("id", module.getId());
        b.putBoolean("state", module.isState());
        b.putBoolean("notifications", module.isNotificationsEnabled());
        return b;
    }
    
}
