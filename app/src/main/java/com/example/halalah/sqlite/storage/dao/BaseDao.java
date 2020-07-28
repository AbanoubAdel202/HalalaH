package com.example.halalah.sqlite.storage.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface BaseDao<T> {
    @Insert
    Long insert(T obj);

    @Insert
    void insert(List<T> array);

    @Update
    void update(T obj);

    @Delete
    void delete(T obj);

    @RawQuery
    List<T> getList(SupportSQLiteQuery query);

    @RawQuery
    T getOne(SupportSQLiteQuery query);

//    @RawQuery
//    List<T> query(SupportSQLiteQuery query);
}
