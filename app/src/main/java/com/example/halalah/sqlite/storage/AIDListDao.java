package com.example.halalah.sqlite.storage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.halalah.TMS.AID_List;

import java.util.List;

@Dao
public interface AIDListDao {

    @Query("SELECT * FROM AID_List")
    List<AID_List> getAll();

    @Insert
    Long insert(AID_List aidList);

    @Insert
    void insertList(AID_List... aidLists);

    @Delete
    void delete(AID_List aidList);

    @Delete
    void deleteList(AID_List aidList);

    @Update
    int update(AID_List aidList);

}
