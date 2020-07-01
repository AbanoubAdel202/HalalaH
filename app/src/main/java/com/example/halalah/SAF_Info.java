package com.example.halalah;

class SAF_Info {

    int m_iSAF_count;      //number of transaction saved
    int m_iMax_SAF_Depth;  //Max number of transaction
    int m_iMax_SAF_Cumulative_Amount;

    POSTransaction[] m_posTransactions_SAF;

    SAF_Info(){
        m_posTransactions_SAF= new POSTransaction[m_iMax_SAF_Depth];
        m_iMax_SAF_Cumulative_Amount=0;
        m_iSAF_count=0;
    }


}
