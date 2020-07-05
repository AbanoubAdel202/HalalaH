package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.Gprs;
import com.example.halalah.TMS.Gsm;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class GsmRepo extends Repository<Gsm> {

    public GsmRepo(Context context) {
        super(context);
    }

    public Gsm getById(String aid) {
        Gsm wrapper = new Gsm();
        wrapper.id = aid;

        return queryById(wrapper);
    }

    public List<Gsm> getAll() {
        return queryAll();
    }


}
