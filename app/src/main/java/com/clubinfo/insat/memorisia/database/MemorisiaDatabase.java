package com.clubinfo.insat.memorisia.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;

@Database(entities = {OptionModule.class, WorkModule.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MemorisiaDatabase extends RoomDatabase {
    
    private static MemorisiaDatabase INSTANCE;
    
    public abstract OptionModuleDao optionModuleDao();
    
    public abstract WorkModuleDao workModuleDao();
    
    public static MemorisiaDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MemorisiaDatabase.class, "modules_database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }
    
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
