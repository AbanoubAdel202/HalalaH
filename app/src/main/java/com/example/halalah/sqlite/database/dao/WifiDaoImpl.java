package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.TMS.Wifi;
import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class WifiDaoImpl extends BaseDaoImpl<Wifi> {

    public WifiDaoImpl(Context context) {
        super(new MyDBHelper(context), Wifi.class);
    }

    /**
     * select primary Wifi connection from database
     */
    public Wifi findPrimary() {
        return findByPriority("1");
    }

    /**
     * select secondary Wifi connection from database
     */
    public Wifi findSecondary() {
        return findByPriority("1");
    }

    /**
     * select Wifi connection from database with its priority
     *
     * @param priority connections priority "1" = primary, "2" = secondary
     */
    public Wifi findByPriority(String priority) {
        StringBuffer sb = new StringBuffer("select * from Wifi where Priority='")
                .append(priority).append("'");
        List<Wifi> capklist = rawQuery(sb.toString(), null);
        if (capklist == null || capklist.size() == 0) {
            return null;
        }
        return capklist.get(0);
    }

}
