package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.table.AID_List;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class AidListDaoImpl extends BaseDaoImpl<AID_List> {
    public AidListDaoImpl(Context context) {
        super(new MyDBHelper(context),AID_List.class);
    }

    /**
     * select all aid from database
     * @return
     */
    public List<AID_List> getAll(){
        StringBuffer sb = new StringBuffer("select * from AID_List");
        List<AID_List> aidlist = rawQuery(sb.toString(), null);
        return aidlist;
    }

    /**
     * select aid from database
     * @param aid	the hexstring of aid
     * @return
     */
    public AID_List get(String aid){
        StringBuffer sb = new StringBuffer("select * from AID_List where AID='")
                .append(aid).append("'");
        List<AID_List> aidlist = rawQuery(sb.toString(), null);
        if(aidlist==null||aidlist.size()==0){
            return null;
        }
        return aidlist.get(0);
    }

}
