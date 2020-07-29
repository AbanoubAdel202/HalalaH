package com.example.halalah.TMS;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Column;

public class Connection extends BaseModel {

    @Column(name = "Priority")
    public String Priority;
    @Column(name = "Communication_Type", unique = true)
    public String Communication_Type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM

    public Connection() {
    }
}
