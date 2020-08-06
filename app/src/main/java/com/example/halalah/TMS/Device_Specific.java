package com.example.halalah.TMS;

public class Device_Specific
    {
        ///////////////////
    //Segment_1_of_Device_Specific
    ///////////////////

        // based on moamen request data become objects
        public String m_sCard_Scheme_MADA;                        //The value configured for the mada scheme (‘P1’) shall apply to all card schemes
        public ContactlessLimits mada_CTLS_Limits;
        public ContactlessLimits Visa_CTLS_Limits;
        public ContactlessLimits Mastercard_CTLS_Limits;
        public String m_sMax_SAF_Depth;
        public String m_sMax_SAF_Cumulative_Amount;
        public String m_sIdle_Time;
        public String m_sMax_Reconciliation_Amount;
        public String m_sMax_Transactions_Processed;
        public String m_sQR_Code_Print_Indicator;
    }
