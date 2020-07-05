package com.example.halalah.sqlite.storage;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.halalah.TMS.AID_List;

import java.util.List;

public interface AIDListDao {

    @Query("SELECT * FROM AID_List")
    List<AID_List> getAll();

    @Query("SELECT * FROM AID_List WHERE id IN (:userIds)")
    List<AID_List> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(AID_List... users);

    @Delete
    void delete(AID_List user);
}
