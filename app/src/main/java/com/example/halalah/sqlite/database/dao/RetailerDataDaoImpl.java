package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.MyDBHelper;
import com.example.halalah.sqlite.database.table.Retailer_Data;

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
    public Retailer_Data getRetailerData() {
        StringBuffer sb = new StringBuffer("select * from Retailer_Data");
        List<Retailer_Data> dataList = rawQuery(sb.toString(), null);
        if (dataList == null || dataList.size() == 0) {
            return null;
        }
        return dataList.get(0);
    }
}
