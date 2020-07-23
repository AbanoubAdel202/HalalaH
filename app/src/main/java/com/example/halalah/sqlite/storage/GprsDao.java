package com.example.halalah.sqlite.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.halalah.TMS.Gprs;

import java.util.List;

@Dao
public interface GprsDao {

    @Query("SELECT * FROM gprs")
    List<Gprs> getAll();

    @Insert
    Long insert(Gprs gprs);

    @Update
    int update(Gprs gprs);

    @Query("SELECT * FROM Gprs WHERE Priority = :priority LIMIT 1")
    Gprs getConnection(String priority);

    @Query("SELECT * FROM Gprs WHERE Priority = \"2\" LIMIT 1")
    Gprs getSecondaryConnection();

}
