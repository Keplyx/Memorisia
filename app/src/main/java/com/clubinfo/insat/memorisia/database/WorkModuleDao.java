package com.clubinfo.insat.memorisia.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.clubinfo.insat.memorisia.modules.WorkModule;

import java.util.List;

@Dao
public interface WorkModuleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWorkModules(WorkModule... workModules);
    
    @Update
    void updateWorkModules(WorkModule... workModules);
    
    @Delete
    void deleteWorkModules(WorkModule... workModules);
    
    @Query("SELECT * FROM work_modules")
    List<WorkModule> getAllWorkModules();
    
    @Query("SELECT * FROM work_modules WHERE agendaId IN (:agendas)")
    List<WorkModule> getWorkModules(List<Integer> agendas);
    
    @Query("SELECT * FROM work_modules WHERE agendaId IN (:agendas) AND subjectID IN (:subjects)")
    List<WorkModule> getWorkModules(List<Integer> agendas, List<Integer> subjects);
    
    @Query("SELECT * FROM work_modules WHERE agendaId IN (:agendas) AND subjectID IN (:subjects) AND workTypeId IN (:workTypes)")
    List<WorkModule> getWorkModules(List<Integer> agendas, List<Integer> subjects, List<Integer> workTypes);
    
    @Query("SELECT * FROM work_modules WHERE id = :id")
    WorkModule getWorkModuleOfId(int id);
}
