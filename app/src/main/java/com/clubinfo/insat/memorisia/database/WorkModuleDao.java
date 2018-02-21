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
    
    @Query("SELECT * FROM work_modules WHERE agendaId = :agenda")
    List<WorkModule> getWorkModulesOfAgenda(int agenda);
    
    @Query("SELECT * FROM work_modules WHERE agendaId IN (:agendas)")
    List<WorkModule> getWorkModulesOfAgenda(List<Integer> agendas);
    
    @Query("SELECT * FROM work_modules WHERE subjectId = :subject")
    List<WorkModule> getWorkModulesOfSubject(int subject);
    
    @Query("SELECT * FROM work_modules WHERE agendaId IN (:agendas) AND subjectID = :subjects")
    List<WorkModule> getWorkModulesOfSubject(List<Integer> agendas, int subjects);
    
    @Query("SELECT * FROM work_modules WHERE workTypeId = :workType")
    List<WorkModule> getWorkModulesOfWorkType(int workType);
    
    @Query("SELECT * FROM work_modules WHERE agendaId IN (:agendas) AND workTypeId = :workTypes")
    List<WorkModule> getWorkModulesOfWorkType(List<Integer> agendas, int workTypes);
    
    
    
    @Query("SELECT * FROM work_modules WHERE id = :id")
    WorkModule getWorkModuleOfId(int id);
}
