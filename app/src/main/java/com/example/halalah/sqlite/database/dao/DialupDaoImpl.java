package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.TMS.Dialup;
import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class DialupDaoImpl extends BaseDaoImpl<Dialup> {

    public DialupDaoImpl(Context context) {
        super(new MyDBHelper(context), Dialup.class);
    }

    /**
     * select primary dialup connection from database
     */
    public Dialup findPrimary() {
        return findByPriority("1");
    }

    /**
     * select secondary dialup connection from database
     */
    public Dialup findSecondary() {
        return findByPriority("2");
    }

    /**
     * select dialup connection from database with its priority
     *
     * @param priority connections priority "1" = primary, "2" = secondary
     */
    public Dialup findByPriority(String priority) {
        StringBuffer sb = new StringBuffer("select * from Dialup where Priority='")
                .append(priority).append("'");
        List<Dialup> list = rawQuery(sb.toString(), null);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

}
