package com.example.halalah.TMS;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Column;

public class Connection extends BaseModel {

    public static final String DIALUP = "01";
    public static final String TCPIP = "_02";
    public static final String GPRS = "03";
    public static final String WIFI = "04";
    public static final String GSM = "05";

    @Column(name = "AID")
    public String Priority;
    @Column(name = "Communication_Type")
    public String Communication_Type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM

    public Connection() {
    }
}
