package com.example.halalah.TMS;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Retailer_Data {
    //////////////////
    //segment1
    /////////////////
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int pk;

    public String m_sNext_load;

    public String m_sReconciliation_time;

    public String m_sRetailer_Name_Arabic;

    public String m_sRetailer_Number_English;

    public String m_sRetailer_Name_English;

    public String m_sRetailer_Language_Indicator_D2;

    public String m_sTerminal_Currency_Code;

    public String m_sTerminal_Country_Code;

    public String m_sTransaction_Currency_Exponent;

    public String m_sCurrency_Symbol_Arabic;

    public String m_sCurrency_Symbol_English;

    public String m_sArabic_Receipt_1;

    public String m_sArabic_Receipt_2;

    public String m_sEnglish_Receipt_1;

    public String m_sEnglish_Receipt_2;
    /////////////////
    //Segment2
    ////////////////

    public String m_sRetailer_Address_1_Arabic;

    public String m_sRetailer_Address_1_English;
    /////////////////
    //Segment3
    ////////////////

    public String m_sRetailer_Address_2_Arabic;

    public String m_sRetailer_Address_2_English;
    /////////////////
    //Segment4
    ////////////////

    public String m_sTerminal_Capability;

    public String m_sAdditional_Terminal_Capabilities;

    public String m_sDownload_Phone_Number;

    public String m_sEMV_Terminal_Type;

    public String m_sAutomatic_Load;

    public String m_sSAF_Retry_Limit;

    public String m_sSAF_Default_Message_Transmission_Number;

    public Retailer_Data() {
        //////////////////
        //segment1
        /////////////////

        m_sNext_load = "";
        m_sReconciliation_time = "";
        m_sRetailer_Name_Arabic = "";
        m_sRetailer_Number_English = "";
        m_sRetailer_Name_English = "";
        m_sRetailer_Language_Indicator_D2 = "";
        m_sTerminal_Currency_Code = "";
        m_sTerminal_Country_Code = "";
        m_sTransaction_Currency_Exponent = "";
        m_sCurrency_Symbol_Arabic = "";
        m_sCurrency_Symbol_English = "";
        m_sArabic_Receipt_1 = "";
        m_sArabic_Receipt_2 = "";
        m_sEnglish_Receipt_1 = "";
        m_sEnglish_Receipt_2 = "";
        /////////////////
        //Segment2
        ////////////////
        m_sRetailer_Address_1_Arabic = "";
        m_sRetailer_Address_1_English = "";
        /////////////////
        //Segment3
        ////////////////
        m_sRetailer_Address_2_Arabic = "";
        m_sRetailer_Address_2_English = "";
        /////////////////
        //Segment4
        ////////////////
        m_sTerminal_Capability = "";
        m_sAdditional_Terminal_Capabilities = "";
        m_sDownload_Phone_Number = "";
        m_sEMV_Terminal_Type = "";
        m_sAutomatic_Load = "";
        m_sSAF_Retry_Limit = "";
        m_sSAF_Default_Message_Transmission_Number = "";
    }
}
