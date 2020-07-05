package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.Dialup;
import com.example.halalah.TMS.Gprs;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class GprsRepo extends Repository<Gprs> {

    public GprsRepo(Context context) {
        super(context);
    }

    public Gprs getById(String aid) {
        Gprs wrapper = new Gprs();
        wrapper.id = aid;

        return queryById(wrapper);
    }

    public List<Gprs> getAll() {
        return queryAll();
    }


}
