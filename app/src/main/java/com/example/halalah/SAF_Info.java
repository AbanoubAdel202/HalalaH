package com.example.halalah;

import android.widget.Switch;

public class SAF_Info {

    int m_iSAF_count;      //number of transaction saved
    int m_iMax_SAF_Depth;  //Max number of transaction
    int m_iMax_SAF_Cumulative_Amount;

    POSTransaction[] m_posTransactions_SAF;

    SAF_Info(){
        m_posTransactions_SAF= new POSTransaction[m_iMax_SAF_Depth];
        m_iMax_SAF_Cumulative_Amount=0;
        m_iSAF_count=0;
    }

    public static void SAVE_IN_SAF(POSTransaction oPostrx)
    {
        switch (oPostrx.m_enmTrxType) {
            case PURCHASE:
                oPostrx.m_enmTrxType= POSTransaction.TranscationType.PURCHASE_ADVICE;
                break;
            case AUTHORISATION:
            case AUTHORISATION_VOID:
                oPostrx.m_enmTrxType= POSTransaction.TranscationType.AUTHORISATION_ADVICE;
                break;




                //todo save postransaction in DB
        }
    }
    public static void SAVE_IN_REV(POSTransaction oPostrax)
    {

    }
    public static void Load_from_SAF()
    {

    }
    public static void Load_from_reversal()
    {
        //todo get data and set it in oGpostransaction

    }



}
