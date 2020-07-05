package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.Revoked_Certificates;
import com.example.halalah.TMS.Tcp_IP;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class TcpipRepo extends Repository<Tcp_IP> {

    public TcpipRepo(Context context) {
        super(context);
    }

    public Tcp_IP getById(String id) {
        Tcp_IP wrapper = new Tcp_IP();
        wrapper.id = id;

        return queryById(wrapper);
    }

    public List<Tcp_IP> getAll() {
        return queryAll();
    }


}
