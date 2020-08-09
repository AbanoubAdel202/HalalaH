package com.example.halalah.sqlite.database;

import android.content.Context;

import com.example.halalah.TMS.AID_Data;
import com.example.halalah.TMS.Aid;
import com.example.halalah.TMS.Card_Scheme;
import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.TMS.Device_Specific;
import com.example.halalah.TMS.Dialup;
import com.example.halalah.TMS.Gprs;
import com.example.halalah.TMS.Gsm;
import com.example.halalah.TMS.Message_Text;
import com.example.halalah.TMS.Public_Key;
import com.example.halalah.TMS.Retailer_Data;
import com.example.halalah.TMS.Revoked_Certificates;
import com.example.halalah.TMS.Tcp_IP;
import com.example.halalah.TMS.Wifi;


public class MyDBHelper extends DBHelper {
    public static final String DBNAME = "hala.db";
    public static final int DBVERSION = 1;

    private static final Class<?>[] clazz = {AID_Data.class, Aid.class, Card_Scheme.class,
            Connection_Parameters.class, Device_Specific.class,
            Dialup.class, Gprs.class, Gsm.class, Message_Text.class, Public_Key.class,
            Retailer_Data.class, Revoked_Certificates.class, Tcp_IP.class, Wifi.class};

    public MyDBHelper(Context context) {
        super(context, DBNAME, null, DBVERSION, clazz);
    }

}
