package com.example.halalah.sqlite.database.table;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

@Table(name = "Dialup")
public class Dialup extends Connection {

    @Column(name = "Phone_Number")
    public String Phone_Number;
    @Column(name = "Dial_Attempts_to_Phone")
    public String Dial_Attempts_to_Phone;
    @Column(name = "Connect__Time_for_Phone")
    public String Connect__Time_for_Phone;
    @Column(name = "Baud_Rate_Default2")
    public String Baud_Rate_Default2;
    @Column(name = "Parity_Bit")
    public String Parity_Bit;
    @Column(name = "Data_Bit")
    public String Data_Bit;
    @Column(name = "Stop_Bit")
    public String Stop_Bit;
    @Column(name = "Response_timeout")
    public String Response_timeout;
}
