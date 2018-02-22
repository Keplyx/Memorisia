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
