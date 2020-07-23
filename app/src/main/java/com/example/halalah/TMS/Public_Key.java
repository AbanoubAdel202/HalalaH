package com.example.halalah.TMS;

import androidx.annotation.NonNull;
import androidx.room.Entity;


/**********************************************************************/
@Entity(primaryKeys= {"RID", "Key_Index" })
public class Public_Key{
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
