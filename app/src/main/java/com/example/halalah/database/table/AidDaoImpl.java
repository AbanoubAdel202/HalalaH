package com.example.halalah.database.table;

import android.content.Context;

import com.example.halalah.database.BaseDaoImpl;

import java.util.List;

public class AidDaoImpl extends BaseDaoImpl<Aid> {
    public AidDaoImpl(Context context) {
        super(new MyDBHelper(context),Aid.class);
    }

    /**
     * select all aid from database
     * @return
     */
    public List<Aid> findAllAid(){
        StringBuffer sb = new StringBuffer("select * from tb_aid");
        List<Aid> aidlist = rawQuery(sb.toString(), null);
        return aidlist;
    }

    /**
     * select aid from database
     * @param aid	the hexstring of aid
     * @return
     */
    public Aid findByAid(String aid){
        StringBuffer sb = new StringBuffer("select * from tb_aid where aid='")
                .append(aid).append("'");
        List<Aid> aidlist = rawQuery(sb.toString(), null);
        if(aidlist==null||aidlist.size()==0){
            return null;
        }
        return aidlist.get(0);
    }

}
