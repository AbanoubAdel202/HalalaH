package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.Public_Key;
import com.example.halalah.TMS.Retailer_Data;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class RetailerDataRepo extends Repository<Retailer_Data> {

    public RetailerDataRepo(Context context) {
        super(context);
    }

    public Retailer_Data getById(String id) {
        Retailer_Data wrapper = new Retailer_Data();
        wrapper.id = id;

        return queryById(wrapper);
    }

    public List<Retailer_Data> getAll() {
        return queryAll();
    }


}
