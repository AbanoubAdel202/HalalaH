package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.AID_List;
import com.example.halalah.TMS.Device_Specific;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class DeviceSpecificRepo extends Repository<Device_Specific> {

    public DeviceSpecificRepo(Context context) {
        super(context);
    }

    public Device_Specific getById(String aid) {
        Device_Specific wrapper = new Device_Specific();
        wrapper.id = aid;

        return queryById(wrapper);
    }

    public List<Device_Specific> getAll() {
        return queryAll();
    }


}
