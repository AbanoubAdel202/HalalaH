package com.example.halalah.sqlite.database.dao;

import android.content.Context;


import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.table.AID_Data;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class AidDaoImpl extends BaseDaoImpl<AID_Data> {
    public AidDaoImpl(Context context) {
        super(new MyDBHelper(context),AID_Data.class);
    }

    /**
     * select all aid from database
     * @return
     */
    public List<AID_Data> getAll(){
        StringBuffer sb = new StringBuffer("select * from AID_Data");
        List<AID_Data> aidlist = rawQuery(sb.toString(), null);
        return aidlist;
    }

    /**
     * select aid from database
     * @param aid	the hexstring of aid
     * @return
     */
    public AID_Data get(String aid){
        StringBuffer sb = new StringBuffer("select * from AID_Data where aid='")
                .append(aid).append("'");
        List<AID_Data> aidlist = rawQuery(sb.toString(), null);
        if(aidlist==null||aidlist.size()==0){
            return null;
        }
        return aidlist.get(0);
    }

}
