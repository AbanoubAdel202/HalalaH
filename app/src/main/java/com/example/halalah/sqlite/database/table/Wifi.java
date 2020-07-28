package com.example.halalah.sqlite.database.table;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

@Table(name = "Wifi")
public class Wifi extends Connection {
    @Column(name = "Network_IP_Address")
    public String Network_IP_Address;
    @Column(name = "Network_TCP_Support")
    public String Network_TCP_Support;
    @Column(name = "Count_Access_Retries")
    public String Count_Access_Retries;
    @Column(name = "Response_Time_Out")
    public String Response_Time_Out;
    @Column(name = "SSL_Certificate_File")
    public String SSL_Certificate_File;
}
