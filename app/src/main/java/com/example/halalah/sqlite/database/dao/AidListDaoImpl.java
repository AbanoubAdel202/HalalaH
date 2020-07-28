package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.TMS.Aid;
import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class AidListDaoImpl extends BaseDaoImpl<Aid> {
    public AidListDaoImpl(Context context) {
        super(new MyDBHelper(context), Aid.class);
    }

    /**
     * select all aid from database
     * @return
     */
    public List<Aid> getAll(){
        StringBuffer sb = new StringBuffer("select * from Aid");
        List<Aid> aidlist = rawQuery(sb.toString(), null);
        return aidlist;
    }

    /**
     * select aid from database
     * @param aid	the hexstring of aid
     * @return
     */
    public Aid get(String aid){
        StringBuffer sb = new StringBuffer("select * from Aid where aidName='")
                .append(aid).append("'");
        List<Aid> aidlist = rawQuery(sb.toString(), null);
        if(aidlist==null||aidlist.size()==0){
            return null;
        }
        return aidlist.get(0);
    }

}
