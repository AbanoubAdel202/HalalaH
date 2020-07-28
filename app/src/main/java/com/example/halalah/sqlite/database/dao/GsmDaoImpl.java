package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.TMS.Gsm;
import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class GsmDaoImpl extends BaseDaoImpl<Gsm> {

    public GsmDaoImpl(Context context) {
        super(new MyDBHelper(context), Gsm.class);
    }

    /**
     * select primary Gsm connection from database
     */
    public Gsm findPrimary() {
        return findByPriority("1");
    }

    /**
     * select secondary Gsm connection from database
     */
    public Gsm findSecondary() {
        return findByPriority("1");
    }

    /**
     * select Gsm connection from database with its priority
     *
     * @param priority connections priority "1" = primary, "2" = secondary
     */
    public Gsm findByPriority(String priority) {
        StringBuffer sb = new StringBuffer("select * from Gsm where Priority='")
                .append(priority).append("'");
        List<Gsm> capklist = rawQuery(sb.toString(), null);
        if (capklist == null || capklist.size() == 0) {
            return null;
        }
        return capklist.get(0);
    }

}
