package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.TMS.Retailer_Data;
import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class RetailerDataDaoImpl extends BaseDaoImpl<Retailer_Data> {
    public RetailerDataDaoImpl(Context context) {
        super(new MyDBHelper(context), Retailer_Data.class);
    }

    /**
     * select retailer data from database
     *
     * @return
     */
    public Retailer_Data get() {
        StringBuffer sb = new StringBuffer("select * from Retailer_Data");
        List<Retailer_Data> dataList = rawQuery(sb.toString(), null);
        if (dataList == null || dataList.size() == 0) {
            return null;
        }
        int index = dataList.size() - 1;
        return dataList.get(index);
    }
}
