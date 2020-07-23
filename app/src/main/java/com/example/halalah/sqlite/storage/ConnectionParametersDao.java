package com.example.halalah.sqlite.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.halalah.TMS.Connection_Parameters;

import java.util.List;

@Dao
public interface ConnectionParametersDao {

    @Query("SELECT * FROM Connection_Parameters")
    List<Connection_Parameters> getAll();

    @Insert
    Long insert(Connection_Parameters connection_parameters);

    @Update
    int update(Connection_Parameters connection_parameters);

    @Query("SELECT primaryConnectionType FROM Connection_Parameters LIMIT 1")
    String getPrimaryConnectionType();

    @Query("SELECT secondaryConnectionType FROM Connection_Parameters LIMIT 1")
    String getSecondaryConnectionType();

}
