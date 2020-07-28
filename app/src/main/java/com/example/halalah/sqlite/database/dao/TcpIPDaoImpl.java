package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.table.Tcp_IP;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class TcpIPDaoImpl extends BaseDaoImpl<Tcp_IP> {

    public TcpIPDaoImpl(Context context) {
        super(new MyDBHelper(context), Tcp_IP.class);
    }

    /**
     * select primary Tcp_IP connection from database
     */
    public Tcp_IP findPrimary() {
        return findByPriority("1");
    }

    /**
     * select secondary Tcp_IP connection from database
     */
    public Tcp_IP findSecondary() {
        return findByPriority("1");
    }

    /**
     * select Tcp_IP connection from database with its priority
     *
     * @param priority connections priority "1" = primary, "2" = secondary
     */
    public Tcp_IP findByPriority(String priority) {
        StringBuffer sb = new StringBuffer("select * from Tcp_IP where Priority='")
                .append(priority).append("'");
        List<Tcp_IP> capklist = rawQuery(sb.toString(), null);
        if (capklist == null || capklist.size() == 0) {
            return null;
        }
        return capklist.get(0);
    }

}
