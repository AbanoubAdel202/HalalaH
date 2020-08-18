package com.example.halalah.TMS;

import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

import org.parceler.Parcel;

@Parcel
@Table(name = "Gprs")
public class Gprs extends Connection {

    @Column(name = "GPRS_dial_Number")
    public String GPRS_dial_Number;

    @Column(name = "GPRS_access_point_name")
    public String GPRS_access_point_name;

    @Column(name = "Connect__Time_for_GPRS_phone")
    public String Connect__Time_for_GPRS_phone;

    @Column(name = "Network_IP_address")
    public String Network_IP_address;

    @Column(name = "Network_TCP_port")
    public String Network_TCP_port;

    @Column(name = "Dial_attempts_to_network")
    public String Dial_attempts_to_network;

    @Column(name = "Response_time_out")
    public String Response_time_out;

    @Column(name = "SSL_Certificate_file")
    public String SSL_Certificate_file;

    public Gprs(){

    }
}
