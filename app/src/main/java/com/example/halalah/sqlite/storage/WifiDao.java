package com.example.halalah.sqlite.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.halalah.TMS.Wifi;

import java.util.List;

@Dao
public interface WifiDao {

    @Query("SELECT * FROM Wifi")
    List<Wifi> getAll();

    @Insert
    Long insert(Wifi wifi);

    @Update
    int update(Wifi wifi);


    @Query("SELECT * FROM Wifi WHERE Priority = :priority LIMIT 1")
    Wifi getConnection(String priority);


}
