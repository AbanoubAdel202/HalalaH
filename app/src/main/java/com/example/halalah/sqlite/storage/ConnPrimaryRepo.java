package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.AID_List;
import com.example.halalah.TMS.Conn_Primary;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class ConnPrimaryRepo extends Repository<Conn_Primary> {

    public ConnPrimaryRepo(Context context) {
        super(context);
    }

    public Conn_Primary getById(String aid) {
        Conn_Primary wrapper = new Conn_Primary();
        wrapper.id = aid;

        return queryById(wrapper);
    }

    public List<Conn_Primary> getAll() {
        return queryAll();
    }


}
