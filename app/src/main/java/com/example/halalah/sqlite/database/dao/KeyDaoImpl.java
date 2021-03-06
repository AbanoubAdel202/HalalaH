package com.example.halalah.sqlite.database.dao;

import android.content.Context;


import com.example.halalah.TMS.Public_Key;
import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class KeyDaoImpl extends BaseDaoImpl<Public_Key> {
    public KeyDaoImpl(Context context) {
        super(new MyDBHelper(context), Public_Key.class);
    }

    /**
     * select capk from database
     *
     * @param rid   the hexstring of rid
//     * @param index index
     * @return
     */
    public Public_Key getByRid(String rid) {
        StringBuffer sb = new StringBuffer("select * from Public_Key where RID='")
                .append(rid).append("'");
//        .append("' and Key_Index='").append(index).append("'");
        List<Public_Key> capklist = rawQuery(sb.toString(), null);
        if (capklist == null || capklist.size() == 0) {
            return null;
        }
        return capklist.get(0);
    }

    /**
     * select capk from database
     *
     * @return
     */
    public List<Public_Key> getAll() {
        StringBuffer sb = new StringBuffer("select * from Public_Key");
        List<Public_Key> capklist = rawQuery(sb.toString(), null);
        return capklist;
    }

}
