package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.AID_List;
import com.example.halalah.TMS.Dialup;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class DialupRepo extends Repository<Dialup> {

    public DialupRepo(Context context) {
        super(context);
    }

    public Dialup getById(String aid) {
        Dialup wrapper = new Dialup();
        wrapper.id = aid;

        return queryById(wrapper);
    }

    public List<Dialup> getAll() {
        return queryAll();
    }


}
