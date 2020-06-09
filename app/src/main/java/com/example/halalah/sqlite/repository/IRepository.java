package com.example.halalah.sqlite.repository;

import java.util.List;

public interface IRepository<T> {

    void closeRepo();

    boolean insert(T object);

    boolean update(T object, String... properties);

    boolean delete(T object);

    T queryById(T object);

    List<T> query(T object, String... properties);

    List<T> queryAll();
}
