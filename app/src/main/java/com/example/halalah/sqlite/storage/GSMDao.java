package com.example.halalah.sqlite.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.halalah.TMS.Gsm;

import java.util.List;

@Dao
public interface GSMDao {

    @Query("SELECT * FROM Gsm")
    List<Gsm> getAll();

    @Insert
    Long insert(Gsm gsm);

    @Update
    int update(Gsm gsm);

    @Query("SELECT * FROM Gsm WHERE Priority = :priority LIMIT 1")
    Gsm getConnection(String priority);

    @Query("SELECT * FROM Gsm WHERE Priority = \"2\" LIMIT 1")
    Gsm getSecondaryConnection();

}
