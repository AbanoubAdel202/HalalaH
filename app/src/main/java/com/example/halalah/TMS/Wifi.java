package com.example.halalah.TMS;

import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

import org.parceler.Parcel;

@Parcel
@Table(name = "Wifi")
public class Wifi extends Connection {

    public Wifi() {
    }

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
