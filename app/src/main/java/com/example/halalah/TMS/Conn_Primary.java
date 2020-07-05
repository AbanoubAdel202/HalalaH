package com.example.halalah.TMS;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@SqliteTableName("Conn_Primary")
public class Conn_Primary implements SqliteGenericObject {
    @SqlitePrimaryKey
    @SqliteNotNull
    public String id;
    @SqliteNotNull
    public Dialup dialup;
    @SqliteNotNull
    public Tcp_IP tcp_ip;
    @SqliteNotNull
    public Gprs gprs;
    @SqliteNotNull
    public Wifi wifi;
    @SqliteNotNull
    public Gsm gsm;

    public Conn_Primary() {
        dialup = new Dialup();
        tcp_ip = new Tcp_IP();
        gprs = new Gprs();
        wifi = new Wifi();
        gsm = new Gsm();
    }

    @Override
    public String getId() {
        return id;
    }
}
