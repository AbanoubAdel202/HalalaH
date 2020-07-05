package com.example.halalah.TMS;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@SqliteTableName("Device_Specific")
public class Device_Specific implements SqliteGenericObject {
    ///////////////////
    //Segment_1_of_Device_Specific
    ///////////////////
    @SqlitePrimaryKey
    @SqliteNotNull
    public String id;
    @SqliteNotNull
    public String m_sCard_Scheme_MADA;                        //The value configured for the mada scheme (‘P1’) shall apply to all card schemes
    @SqliteNotNull
    public String m_sTerminal_Contactless_Transaction_Limit;
    @SqliteNotNull
    public String m_sTerminal_CVM_Required_Limit;
    @SqliteNotNull
    public String m_sTerminal_Contactless_Floor_Limit;
    @SqliteNotNull
    public String m_sCard_Scheme_VC;
    @SqliteNotNull
    public String m_sTerminal_Contactless_Transaction_Limit1;         //zeros as per spec Version 6.0.9
    @SqliteNotNull
    public String m_sTerminal_CVM_Required_Limit1;                    //zeros as per spec Version 6.0.9
    @SqliteNotNull
    public String m_sTerminal_Contactless_Floor_Limit1;               //zeros as per spec Version 6.0.9
    @SqliteNotNull
    public String m_sCard_Scheme_MC;
    @SqliteNotNull
    public String m_sTerminal_Contactless_Transaction_Limit2;         //zeros as per spec Version 6.0.9
    @SqliteNotNull
    public String m_sTerminal_CVM_Required_Limit2;                    //zeros as per spec Version 6.0.9
    @SqliteNotNull
    public String m_sTerminal_Contactless_Floor_Limit2;               //zeros as per spec Version 6.0.9
    @SqliteNotNull
    public String m_sMax_SAF_Depth;
    @SqliteNotNull
    public String m_sMax_SAF_Cumulative_Amount;
    @SqliteNotNull
    public String m_sIdle_Time;
    @SqliteNotNull
    public String m_sMax_Reconciliation_Amount;
    @SqliteNotNull
    public String m_sMax_Transactions_Processed;
    @SqliteNotNull
    public String m_sQR_Code_Print_Indicator;

    @Override
    public String getId() {
        return id;
    }
}
