package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.AID_Data;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class AIDRepo extends Repository<AID_Data> {

    public AIDRepo(Context context) {
        super(context);
    }

    public AID_Data getById(String aid) {
        AID_Data wrapper = new AID_Data();
        wrapper.AID = aid;

        return queryById(wrapper);
    }

    public List<AID_Data> getAll() {
        return queryAll();
    }


}
