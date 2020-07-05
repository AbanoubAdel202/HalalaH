package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.Retailer_Data;
import com.example.halalah.TMS.Revoked_Certificates;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class RevokedCertificatesRepo extends Repository<Revoked_Certificates> {

    public RevokedCertificatesRepo(Context context) {
        super(context);
    }

    public Revoked_Certificates getById(String id) {
        Revoked_Certificates wrapper = new Revoked_Certificates();
        wrapper.id = id;

        return queryById(wrapper);
    }

    public List<Revoked_Certificates> getAll() {
        return queryAll();
    }


}
