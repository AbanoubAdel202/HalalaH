package com.example.halalah.TMS;

import androidx.annotation.NonNull;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;


/**********************************************************************/
@Table(name = "Public_Key")
public class Public_Key extends BaseModel {
    /////////////////
    //Segment1
    ////////////////
    @Column(name = "RID")
    public String RID;
    @Column(name = "Key_Index")
    public String Key_Index;
    @Column(name = "Hash_ID")
    public String Hash_ID;
    @Column(name = "Digital_Signature_ID")
    public String Digital_Signature_ID;
    @Column(name = "Public_Key")
    public String Public_Key;
    @Column(name = "Exponent")
    public String Exponent;
    @Column(name = "Check_Sum")
    public String Check_Sum;
    @Column(name = "CA_Public_Key_Length")
    public String CA_Public_Key_Length;
    @Column(name = "CA_Public_Key_Expiry_Date")
    public String CA_Public_Key_Expiry_Date;

    public Public_Key(){

    }

}
