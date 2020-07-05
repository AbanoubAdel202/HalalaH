package com.example.halalah.TMS;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@SqliteTableName("Gprs")
public class Gprs implements SqliteGenericObject {
    @SqlitePrimaryKey
    @SqliteNotNull
    public String id;
    @SqliteNotNull
    public String Priority;
    @SqliteNotNull
    public String Communication_Type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
    @SqliteNotNull
    public String GPRS_dial_Number;
    @SqliteNotNull
    public String GPRS_access_point_name;
    @SqliteNotNull
    public String Connect__Time_for_GPRS_phone;
    @SqliteNotNull
    public String Network_IP_address;
    @SqliteNotNull
    public String Network_TCP_port;
    @SqliteNotNull
    public String Dial_attempts_to_network;
    @SqliteNotNull
    public String Response_time_out;
    @SqliteNotNull
    public String SSL_Certificate_file;

    @Override
    public String getId() {
        return id;
    }
}
