package com.example.halalah.TMS;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@SqliteTableName("Dialup")
public class Dialup implements SqliteGenericObject {
    @SqlitePrimaryKey
    @SqliteNotNull
    public String id;
    @SqliteNotNull
    public String Priority;
    @SqliteNotNull
    public String Communication_Type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
    @SqliteNotNull
    public String Phone_Number;
    @SqliteNotNull
    public String Dial_Attempts_to_Phone;
    @SqliteNotNull
    public String Connect__Time_for_Phone;
    @SqliteNotNull
    public String Baud_Rate_Default2;
    @SqliteNotNull
    public String Parity_Bit;
    @SqliteNotNull
    public String Data_Bit;
    @SqliteNotNull
    public String Stop_Bit;
    @SqliteNotNull
    public String Response_timeout;

    @Override
    public String getId() {
        return id;
    }
}
