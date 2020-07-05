package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.Tcp_IP;
import com.example.halalah.TMS.Wifi;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class WifiRepo extends Repository<Wifi> {

    public WifiRepo(Context context) {
        super(context);
    }

    public Wifi getById(String id) {
        Wifi wrapper = new Wifi();
        wrapper.id = id;

        return queryById(wrapper);
    }

    public List<Wifi> getAll() {
        return queryAll();
    }


}
