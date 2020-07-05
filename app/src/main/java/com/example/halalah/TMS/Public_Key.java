package com.example.halalah.TMS;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@SqliteTableName("Public_Key")
public class Public_Key implements SqliteGenericObject {
    /////////////////
    //Segment1
    ////////////////
    @SqliteNotNull
    public String RID;
    @SqliteNotNull
    public String Key_Index;
    @SqliteNotNull
    public String Hash_ID;
    @SqliteNotNull
    public String Digital_Signature_ID;
    @SqliteNotNull
    public String Public_Key;
    @SqliteNotNull
    public String Exponent;
    @SqliteNotNull
    public String Check_Sum;
    @SqliteNotNull
    public String CA_Public_Key_Length;
    @SqliteNotNull
    public String CA_Public_Key_Expiry_Date;


    @Override
    public String getId() {
        return RID;
    }
}
