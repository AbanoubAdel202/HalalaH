package com.example.halalah.sqlite.repository.component;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.halalah.TMS.AID_List;
import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.TMS.Dialup;
import com.example.halalah.TMS.Gprs;
import com.example.halalah.TMS.Gsm;
import com.example.halalah.TMS.Tcp_IP;
import com.example.halalah.TMS.Wifi;
import com.example.halalah.sqlite.storage.AIDListDao;
import com.example.halalah.sqlite.storage.ConnectionParametersDao;
import com.example.halalah.sqlite.storage.DialUpDao;
import com.example.halalah.sqlite.storage.GSMDao;
import com.example.halalah.sqlite.storage.GprsDao;
import com.example.halalah.sqlite.storage.TcpIPDao;
import com.example.halalah.sqlite.storage.WifiDao;
import com.example.halalah.sqlite.storage.dao.BaseDao;

@Database(entities = {AID_List.class, Connection_Parameters.class, Gprs.class, Wifi.class, Gsm.class, Tcp_IP.class, Dialup.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AIDListDao getAidListDao();
//    public abstract BaseDao getBaseDao();
    public abstract ConnectionParametersDao getConnectionParametersDao();
    public abstract WifiDao getWifiDao();
    public abstract GprsDao getGprsDao();
    public abstract GSMDao getGSMDao();
    public abstract TcpIPDao getTcpIPDao();
    public abstract DialUpDao getDialUpDao();
}
