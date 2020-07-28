package com.example.halalah.sqlite.database;

import android.content.Context;

import com.example.halalah.sqlite.database.table.AID_Data;
import com.example.halalah.sqlite.database.table.AID_List;
import com.example.halalah.sqlite.database.table.Connection_Parameters;
import com.example.halalah.sqlite.database.table.Device_Specific;
import com.example.halalah.sqlite.database.table.Dialup;
import com.example.halalah.sqlite.database.table.Gprs;
import com.example.halalah.sqlite.database.table.Gsm;
import com.example.halalah.sqlite.database.table.Message_Text;
import com.example.halalah.sqlite.database.table.Public_Key;
import com.example.halalah.sqlite.database.table.Retailer_Data;
import com.example.halalah.sqlite.database.table.Revoked_Certificates;
import com.example.halalah.sqlite.database.table.Tcp_IP;
import com.example.halalah.sqlite.database.table.Wifi;


public class MyDBHelper extends DBHelper {
    public static final String DBNAME = "hala.db";
    public static final int DBVERSION = 1;

    private static final Class<?>[] clazz = {AID_Data.class, AID_List.class,
            Connection_Parameters.class, Device_Specific.class,
            Dialup.class, Gprs.class, Gsm.class, Message_Text.class, Public_Key.class,
            Retailer_Data.class, Revoked_Certificates.class, Tcp_IP.class, Wifi.class};

    public MyDBHelper(Context context) {
        super(context, DBNAME, null, DBVERSION, clazz);
    }

}
