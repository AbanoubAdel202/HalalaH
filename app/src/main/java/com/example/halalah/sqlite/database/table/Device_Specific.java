package com.example.halalah.sqlite.database.table;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

@Table(name = "Device_Specific")
public class Device_Specific extends BaseModel {

    @Column(name = "p1Limits")
    public Limits p1Limits;
    @Column(name = "vcLimits")
    public Limits vcLimits;
    @Column(name = "mcLimits")
    public Limits mcLimits;
    @Column(name = "m_sMax_SAF_Depth")
    public String m_sMax_SAF_Depth;
    @Column(name = "m_sMax_SAF_Cumulative_Amount")
    public String m_sMax_SAF_Cumulative_Amount;
    @Column(name = "m_sIdle_Time")
    public String m_sIdle_Time;
    @Column(name = "m_sMax_Reconciliation_Amount")
    public String m_sMax_Reconciliation_Amount;
    @Column(name = "m_sMax_Transactions_Processed")
    public String m_sMax_Transactions_Processed;
    @Column(name = "m_sQR_Code_Print_Indicator")
    public String m_sQR_Code_Print_Indicator;
}
