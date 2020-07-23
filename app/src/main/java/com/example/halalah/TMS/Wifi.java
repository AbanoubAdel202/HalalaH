package com.example.halalah.TMS;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Wifi extends Connection {

    @NonNull
    @PrimaryKey
    public int pk;

    public String Network_IP_Address;

    public String Network_TCP_Support;

    public String Count_Access_Retries;

    public String Response_Time_Out;

    public String SSL_Certificate_File;
}
