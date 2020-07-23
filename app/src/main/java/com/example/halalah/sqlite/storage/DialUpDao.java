package com.example.halalah.sqlite.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.halalah.TMS.Dialup;
import java.util.List;

@Dao
public interface DialUpDao {

    @Query("SELECT * FROM Dialup")
    List<Dialup> getAll();

    @Insert
    Long insert(Dialup dialup);

    @Update
    int update(Dialup dialup);

    @Query("SELECT * FROM Dialup WHERE Priority = :priority LIMIT 1")
    Dialup getConnection(String priority);

}
