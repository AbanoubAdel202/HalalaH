package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.TMS.Tcp_IP;
import com.example.halalah.sqlite.database.BaseDaoImpl;
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
        return findByPriority("2");
    }

    /**
     * select Tcp_IP connection from database with its priority
     *
     * @param priority connections priority "1" = primary, "2" = secondary
     */
    public Tcp_IP findByPriority(String priority) {
        StringBuffer sb = new StringBuffer("select * from Tcp_IP where Priority='")
                .append(priority).append("'");
        List<Tcp_IP> list = rawQuery(sb.toString(), null);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

}
