package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.AID_Data;
import com.example.halalah.TMS.AID_List;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class AIDListRepo extends Repository<AID_List> {

    public AIDListRepo(Context context) {
        super(context);
    }

    public AID_List getById(String aid) {
        AID_List wrapper = new AID_List();
        wrapper.id = aid;

        return queryById(wrapper);
    }

    public List<AID_List> getAll() {
        return queryAll();
    }


}
