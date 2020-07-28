package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.table.Device_Specific;
import com.example.halalah.sqlite.database.MyDBHelper;
import com.example.halalah.sqlite.database.table.Limits;

import java.util.List;

public class DeviceSpecificDaoImpl extends BaseDaoImpl<Device_Specific> {

    public DeviceSpecificDaoImpl(Context context) {
        super(new MyDBHelper(context), Device_Specific.class);
    }

    /**
     * select all aid from database
     *
     * @return
     */
    public List<Device_Specific> getAll() {
        StringBuffer sb = new StringBuffer("select * from Device_Specific");
        List<Device_Specific> list = rawQuery(sb.toString(), null);
        return list;
    }

    public Device_Specific get() {
        StringBuffer sb = new StringBuffer("select * from Device_Specific");
        List<Device_Specific> list = rawQuery(sb.toString(), null);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public Limits getLimits(String cardSchemeId) {
        StringBuffer sb = new StringBuffer("select * from Device_Specific");
        List<Device_Specific> list = rawQuery(sb.toString(), null);
        if (list == null || list.size() == 0) {
            return null;
        }
        Device_Specific deviceSpecific = list.get(0);
        if (cardSchemeId == "P1"){
            return deviceSpecific.p1Limits;
        } else if (cardSchemeId == "VC"){
            return deviceSpecific.vcLimits;
        } else if (cardSchemeId == "MC"){
            return deviceSpecific.mcLimits;
        }
        return null;
    }

}
