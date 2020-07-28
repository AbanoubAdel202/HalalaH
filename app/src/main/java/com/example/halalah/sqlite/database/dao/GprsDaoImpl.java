package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.TMS.Gprs;
import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class GprsDaoImpl extends BaseDaoImpl<Gprs> {

    public GprsDaoImpl(Context context) {
        super(new MyDBHelper(context), Gprs.class);
    }

    /**
     * select primary Gprs connection from database
     */
    public Gprs findPrimary() {
        return findByPriority("1");
    }

    /**
     * select secondary Gprs connection from database
     */
    public Gprs findSecondary() {
        return findByPriority("1");
    }

    /**
     * select Gprs connection from database with its priority
     * @param priority connections priority "1" = primary, "2" = secondary
     *
     */
    public Gprs findByPriority(String priority) {
        StringBuffer sb = new StringBuffer("select * from Gprs where Priority='")
                .append(priority).append("'");
        List<Gprs> capklist = rawQuery(sb.toString(), null);
        if (capklist == null || capklist.size() == 0) {
            return null;
        }
        return capklist.get(0);
    }

}
