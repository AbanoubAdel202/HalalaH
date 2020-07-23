package com.example.halalah.TMS;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Revoked_Certificates {
    ////////////////////
    //Segment 1 of Revoked Certificates
    ///////////////////
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int pk;

    public String RID_Revoked_Certificate;

    public String IDX;

    public String Cert_serial_number;
}
