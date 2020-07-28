package com.example.halalah.sqlite.database.table;

import androidx.annotation.NonNull;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Table;


/**********************************************************************/
@Table(name = "Public_Key")
public class Public_Key extends BaseModel {
    /////////////////
    //Segment1
    ////////////////
    @NonNull
    public String RID;
    @NonNull
    public String Key_Index;

    public String Hash_ID;

    public String Digital_Signature_ID;

    public String Public_Key;

    public String Exponent;

    public String Check_Sum;

    public String CA_Public_Key_Length;

    public String CA_Public_Key_Expiry_Date;

}
