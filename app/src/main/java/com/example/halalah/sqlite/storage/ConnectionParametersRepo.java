package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class ConnectionParametersRepo extends Repository<Connection_Parameters> {

    public ConnectionParametersRepo(Context context) {
        super(context);
    }

    public Connection_Parameters getById(String aid) {
        Connection_Parameters wrapper = new Connection_Parameters();
        wrapper.id = aid;

        return queryById(wrapper);
    }

    public List<Connection_Parameters> getAll() {
        return queryAll();
    }


}
