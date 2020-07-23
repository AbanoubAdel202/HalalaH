package com.example.halalah.TMS;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Dialup extends Connection {

    @NonNull
    @PrimaryKey
    public int pk;

    public String Phone_Number;

    public String Dial_Attempts_to_Phone;

    public String Connect__Time_for_Phone;

    public String Baud_Rate_Default2;

    public String Parity_Bit;

    public String Data_Bit;

    public String Stop_Bit;

    public String Response_timeout;
}
