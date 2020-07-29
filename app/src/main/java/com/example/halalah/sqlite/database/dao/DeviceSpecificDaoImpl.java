package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.TMS.Device_Specific;
import com.example.halalah.TMS.Limits;
import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.MyDBHelper;

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
        if (list == null || list.size() == 0) {
            return null;
        }

        for (int i = 0; i < list.size(); i++) {
            Device_Specific deviceSpecific = list.get(i);
            deviceSpecific.setMcLimitsStr(deviceSpecific.getMcLimitsStr());
            deviceSpecific.setVcLimitsStr(deviceSpecific.getVcLimitsStr());
            deviceSpecific.setP1LimitsStr(deviceSpecific.getP1LimitsStr());
            list.set(i, deviceSpecific);
        }
        return list;
    }

    public Device_Specific get() {
        StringBuffer sb = new StringBuffer("select * from Device_Specific");
        List<Device_Specific> list = rawQuery(sb.toString(), null);
        if (list == null || list.size() == 0) {
            return null;
        }
        Device_Specific deviceSpecific = list.get(0);
        deviceSpecific.setMcLimitsStr(deviceSpecific.getMcLimitsStr());
        deviceSpecific.setVcLimitsStr(deviceSpecific.getVcLimitsStr());
        deviceSpecific.setP1LimitsStr(deviceSpecific.getP1LimitsStr());
        return deviceSpecific;
    }

    public Limits getLimits(String cardSchemeId) {

        Device_Specific deviceSpecific = get();

        if (cardSchemeId == "P1"){
            return deviceSpecific.getP1Limits();
        } else if (cardSchemeId == "VC"){
            return deviceSpecific.getVcLimits();
        } else if (cardSchemeId == "MC"){
            return deviceSpecific.getMcLimits();
        }
        return null;
    }

    @Override
    public long insert(Device_Specific entity) {
        // Empty the table first
        StringBuffer sb = new StringBuffer("DELETE FROM Device_Specific");
        rawQuery(sb.toString(), null);

        return super.insert(entity);
    }

}
