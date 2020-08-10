package com.example.halalah.database.table;

import android.content.Context;

import com.example.halalah.database.BaseDaoImpl;

import java.util.List;

public class CapkDaoImpl extends BaseDaoImpl<Capk> {
    public CapkDaoImpl(Context context) {
        super(new MyDBHelper(context),Capk.class);
    }

    /**
     * select capk from database
     * @param rid	the hexstring of rid
     * @param index   index
     * @return
     */
    public Capk findByRidIndex(String rid,byte index){
        StringBuffer sb = new StringBuffer("select * from tb_capk where rid='")
                .append(rid).append("' and rindex='").append(index).append("'");
        List<Capk> capklist = rawQuery(sb.toString(), null);
        if(capklist==null||capklist.size()==0){
            return null;
        }
        return capklist.get(0);
    }

    /**
     * select capk from database
     * @return
     */
    public List<Capk> findAllCapk(){
        StringBuffer sb = new StringBuffer("select * from tb_capk");
        List<Capk> capklist = rawQuery(sb.toString(), null);
        return capklist;
    }

}
