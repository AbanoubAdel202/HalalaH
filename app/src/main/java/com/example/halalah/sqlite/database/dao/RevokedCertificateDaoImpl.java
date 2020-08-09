package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.TMS.Public_Key;
import com.example.halalah.TMS.Revoked_Certificates;
import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class RevokedCertificateDaoImpl extends BaseDaoImpl<Revoked_Certificates> {
    public RevokedCertificateDaoImpl(Context context) {
        super(new MyDBHelper(context), Revoked_Certificates.class);
    }

    /**
     * select all Revoked_Certificates from database
     *
     * @return
     */
    public List<Revoked_Certificates> getAll() {
        StringBuffer sb = new StringBuffer("select * from Revoked_Certificates");
        List<Revoked_Certificates> list = rawQuery(sb.toString(), null);
        return list;
    }

}
