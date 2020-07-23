package com.example.halalah.TMS;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Gprs extends Connection {

    @NonNull
    @PrimaryKey
    public int pk;

    public String GPRS_dial_Number;

    public String GPRS_access_point_name;

    public String Connect__Time_for_GPRS_phone;

    public String Network_IP_address;

    public String Network_TCP_port;

    public String Dial_attempts_to_network;

    public String Response_time_out;

    public String SSL_Certificate_file;
}
