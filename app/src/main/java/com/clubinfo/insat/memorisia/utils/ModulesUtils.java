package com.clubinfo.insat.memorisia.utils;


import android.app.VoiceInteractor;
import android.content.Context;
import android.util.Log;

import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ModulesUtils {
    
    public static List<OptionModule> sortOptionModuleListByName(List<OptionModule> modules, final boolean reverse){
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
    
    public static List<WorkModule> sortWorkModuleListByName(List<WorkModule> modules, final boolean reverse){
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
    
    public static List<OptionModule> sortOptionModuleListByDonePercent(List<OptionModule> modules, final Context context, final boolean reverse){
        Collections.sort(modules, new Comparator<OptionModule>() {
            @Override
            public int compare(OptionModule optionModule, OptionModule t1) {
                SaveManager saver = new SaveManager(context);
                List<WorkModule> worksList1 = saver.getWorkModuleList(-1, optionModule.getId(), -1);
                List<WorkModule> worksList2 = saver.getWorkModuleList(-1, t1.getId(), -1);
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
    
    public static List<OptionModule> sortOptionModuleListByTotalWork(List<OptionModule> modules, final Context context, final boolean reverse){
        Collections.sort(modules, new Comparator<OptionModule>() {
            @Override
            public int compare(OptionModule optionModule, OptionModule t1) {
                SaveManager saver = new SaveManager(context);
                List<WorkModule> worksList1 = saver.getWorkModuleList(-1, optionModule.getId(), -1);
                List<WorkModule> worksList2 = saver.getWorkModuleList(-1, t1.getId(), -1);
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
    
    public static List<WorkModule> sortWorkModuleListByPriority(List<WorkModule> modules, final Context context, final boolean reverse){
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
    
    public static List<WorkModule> sortWorkModuleListByWorkType(List<WorkModule> modules, final Context context, final boolean reverse){
        Collections.sort(modules, new Comparator<WorkModule>() {
            @Override
            public int compare(WorkModule m1, WorkModule m2) {
                SaveManager saver = new SaveManager(context);
                List<OptionModule> optionModules = saver.getOptionModuleList(SaveManager.WORKTYPE);
                OptionModule o1 = getModuleOfId(optionModules, m1.getWorkTypeId());
                OptionModule o2 = getModuleOfId(optionModules, m2.getWorkTypeId());
                if (!reverse)
                    return  o1.getText().compareTo(o2.getText());
                else
                    return -1 * o1.getText().compareTo(o2.getText());
            }
        });
        return modules;
    }
    
    public static OptionModule getModuleOfId(List<OptionModule> modules, int id){
        for (int i = 0; i < modules.size(); i++){
            if (modules.get(i).getId() == id)
                return modules.get(i);
        }
        return null;
    }
    
    public static int getPosInList (List<OptionModule> modules, int id){
        for (int i = 0; i < modules.size(); i++){
            if (modules.get(i).getId() == id)
                return i;
        }
        return -1;
    }
    
    public static int getWorkDoneNumber(List<WorkModule> list) {
        int counter = 0;
        for (WorkModule work: list){
            if (work.isState())
                counter++;
        }
        return counter;
    }
    
}
