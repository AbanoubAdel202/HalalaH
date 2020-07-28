package com.example.halalah.sqlite.database.table;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Table;

@Table(name = "Revoked_Certificates")
public class Revoked_Certificates extends BaseModel{
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
