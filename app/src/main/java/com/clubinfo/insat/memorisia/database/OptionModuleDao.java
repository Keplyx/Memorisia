package com.clubinfo.insat.memorisia.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.clubinfo.insat.memorisia.modules.OptionModule;

import java.util.List;

@Dao
public interface OptionModuleDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertOptionModules(OptionModule... optionModules);
    
    @Update
    void updateOptionModules(OptionModule... optionModules);
    
    @Delete
    void deleteOptionModules(OptionModule... optionModules);
    
    @Query("SELECT * FROM option_modules")
    List<OptionModule> getAllOptionModules();
    
    @Query("SELECT * FROM option_modules WHERE type = :type")
    List<OptionModule> getOptionModulesOfType(int type);
    
    @Query("SELECT * FROM option_modules WHERE id = :id")
    OptionModule getOptionModuleOfId(int id);
}
