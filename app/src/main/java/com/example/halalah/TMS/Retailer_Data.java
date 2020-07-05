package com.example.halalah.TMS;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@SqliteTableName("Retailer_Data")
public class Retailer_Data implements SqliteGenericObject {
    //////////////////
    //segment1
    /////////////////
    @SqlitePrimaryKey
    @SqliteNotNull
    public String id;
    @SqliteNotNull
    public String m_sNext_load;
    @SqliteNotNull
    public String m_sReconciliation_time;
    @SqliteNotNull
    public String m_sRetailer_Name_Arabic;
    @SqliteNotNull
    public String m_sRetailer_Number_English;
    @SqliteNotNull
    public String m_sRetailer_Name_English;
    @SqliteNotNull
    public String m_sRetailer_Language_Indicator_D2;
    @SqliteNotNull
    public String m_sTerminal_Currency_Code;
    @SqliteNotNull
    public String m_sTerminal_Country_Code;
    @SqliteNotNull
    public String m_sTransaction_Currency_Exponent;
    @SqliteNotNull
    public String m_sCurrency_Symbol_Arabic;
    @SqliteNotNull
    public String m_sCurrency_Symbol_English;
    @SqliteNotNull
    public String m_sArabic_Receipt_1;
    @SqliteNotNull
    public String m_sArabic_Receipt_2;
    @SqliteNotNull
    public String m_sEnglish_Receipt_1;
    @SqliteNotNull
    public String m_sEnglish_Receipt_2;
    /////////////////
    //Segment2
    ////////////////
    @SqliteNotNull
    public String m_sRetailer_Address_1_Arabic;
    @SqliteNotNull
    public String m_sRetailer_Address_1_English;
    /////////////////
    //Segment3
    ////////////////
    @SqliteNotNull
    public String m_sRetailer_Address_2_Arabic;
    @SqliteNotNull
    public String m_sRetailer_Address_2_English;
    /////////////////
    //Segment4
    ////////////////
    @SqliteNotNull
    public String m_sTerminal_Capability;
    @SqliteNotNull
    public String m_sAdditional_Terminal_Capabilities;
    @SqliteNotNull
    public String m_sDownload_Phone_Number;
    @SqliteNotNull
    public String m_sEMV_Terminal_Type;
    @SqliteNotNull
    public String m_sAutomatic_Load;
    @SqliteNotNull
    public String m_sSAF_Retry_Limit;
    @SqliteNotNull
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

    @Override
    public String getId() {
        return id;
    }
}
