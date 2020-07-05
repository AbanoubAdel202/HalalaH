package com.example.halalah.TMS;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@SqliteTableName("Wifi")
public class Wifi implements SqliteGenericObject {

    @SqlitePrimaryKey
    @SqliteNotNull
    public String id;
    @SqliteNotNull
    public String Priority;
    @SqliteNotNull
    public String Communication_Type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
    @SqliteNotNull
    public String Network_IP_Address;
    @SqliteNotNull
    public String Network_TCP_Support;
    @SqliteNotNull
    public String Count_Access_Retries;
    @SqliteNotNull
    public String Response_Time_Out;
    @SqliteNotNull
    public String SSL_Certificate_File;

    @Override
    public String getId() {
        return id;
    }
}
