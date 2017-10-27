package com.clubinfo.insat.memorisia.utils;


import android.content.Context;
import android.util.Log;

import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ModulesUtils {
    
    public static List<OptionModule> sortModuleListByName(List<OptionModule> modules, final boolean reverse){
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
    
    public static List<OptionModule> sortModuleListByDonePercent(List<OptionModule> modules, Context ctx, boolean reverse){
        final Context context = ctx;
        final boolean reverseSort = reverse;
        Collections.sort(modules, new Comparator<OptionModule>() {
            @Override
            public int compare(OptionModule optionModule, OptionModule t1) {
                SaveManager saver = new SaveManager(context);
                List<WorkModule> worksList1 = saver.getWorkModuleList(-1, optionModule.getId(), -1);
                List<WorkModule> worksList2 = saver.getWorkModuleList(-1, t1.getId(), -1);
                String compare1 = "999", compare2 = "999";
                if (worksList1.size() > 0)
                    compare1 = "" + (double) getWorkDoneNumber(worksList1) / worksList1.size();
                if (worksList2.size() > 0)
                    compare2 = "" + (double) getWorkDoneNumber(worksList2) / worksList2.size();
                if (!reverseSort)
                    return compare1.compareTo(compare2);
                else
                    return -1 * compare1.compareTo(compare2);
            }
        });
        return modules;
    }
    
    public static List<OptionModule> sortModuleListByTotalWork(List<OptionModule> modules, Context ctx, final boolean reverse){
        final Context context = ctx;
        final boolean reverseSort = reverse;
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
                    return -1 * compare1.compareTo(compare2);
                else
                    return compare1.compareTo(compare2);
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
