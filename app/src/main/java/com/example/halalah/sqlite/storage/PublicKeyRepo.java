package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.Message_Text;
import com.example.halalah.TMS.Public_Key;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class PublicKeyRepo extends Repository<Public_Key> {

    public PublicKeyRepo(Context context) {
        super(context);
    }

    public Public_Key getById(String id) {
        Public_Key wrapper = new Public_Key();
        wrapper.RID = id;

        return queryById(wrapper);
    }

    public List<Public_Key> getAll() {
        return queryAll();
    }


}
