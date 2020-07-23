package com.example.halalah.sqlite.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.halalah.TMS.Tcp_IP;

import java.util.List;

@Dao
public interface TcpIPDao {

    @Query("SELECT * FROM Tcp_IP")
    List<Tcp_IP> getAll();

    @Insert
    Long insert(Tcp_IP tcpIp);

    @Update
    int update(Tcp_IP tcpIp);

    @Query("SELECT * FROM Tcp_IP WHERE Priority = :priority LIMIT 1")
    Tcp_IP getConnection(String priority);



}
