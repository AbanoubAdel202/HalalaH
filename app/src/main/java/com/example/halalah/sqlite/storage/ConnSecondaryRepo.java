package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.Conn_Primary;
import com.example.halalah.TMS.Conn_ٍSecondary;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class ConnSecondaryRepo extends Repository<Conn_ٍSecondary> {

    public ConnSecondaryRepo(Context context) {
        super(context);
    }

    public Conn_ٍSecondary getById(String aid) {
        Conn_ٍSecondary wrapper = new Conn_ٍSecondary();
        wrapper.id = aid;

        return queryById(wrapper);
    }

    public List<Conn_ٍSecondary> getAll() {
        return queryAll();
    }


}
