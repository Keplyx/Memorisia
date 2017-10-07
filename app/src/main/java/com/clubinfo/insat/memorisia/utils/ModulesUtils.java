package com.clubinfo.insat.memorisia.utils;


import com.clubinfo.insat.memorisia.modules.OptionModule;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ModulesUtils {
    
    public static List<OptionModule> sortModuleListByName(List<OptionModule> modules){
        Collections.sort(modules, new Comparator<OptionModule>() {
            @Override
            public int compare(OptionModule optionModule, OptionModule t1) {
                return optionModule.getText().compareTo(t1.getText());
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
        
        
    }
