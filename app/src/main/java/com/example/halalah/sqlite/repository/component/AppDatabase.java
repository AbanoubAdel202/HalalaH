package com.example.halalah.sqlite.repository.component;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.halalah.TMS.AID_List;
import com.example.halalah.sqlite.storage.AIDListDao;

@Database(entities = {AID_List.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AIDListDao aidListDao();
}
