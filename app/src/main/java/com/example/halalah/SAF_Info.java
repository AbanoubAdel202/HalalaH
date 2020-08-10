package com.example.halalah;

import android.widget.Switch;

public class SAF_Info {



    enum DESAFtype{
        PARTIAL,
        FULL
    }

    int m_iMax_SAF_Depth;  //Max number of transaction
    int m_iMax_SAF_Cumulative_Amount;
    int       m_iSAFTrxNumber;              // Number of saved transactions in SAF file
    double    m_dSAFCumulativeAmount;       // Total amount of approved transaction (calculation based on reconciliation as debit++ , credit-- , reversal(refund ++) -- )
    int    DeSAF_partial_count=2;
    int    DeSAF_count;


    POSTransaction[] m_posTransactions_SAF;

    SAF_Info(){

        m_posTransactions_SAF= new POSTransaction[m_iMax_SAF_Depth];
        m_iMax_SAF_Cumulative_Amount=0;

        m_dSAFCumulativeAmount=0;
        m_iSAFTrxNumber=0;
    }
    public static POSTransaction BuildSAFOriginals(POSTransaction SAFTransaction, POSTransaction originalTransaction) {
        SAFTransaction=originalTransaction;
        SAFTransaction.m_enum_OrigTRxtype=originalTransaction.m_enmTrxType;
        SAFTransaction.m_sOrigAmount=originalTransaction.m_sTrxAmount;
        SAFTransaction.m_sOrigMTI=originalTransaction.m_sMTI;
        SAFTransaction.m_sOrigSTAN=originalTransaction.m_sSTAN;
        SAFTransaction.m_sOrigTrxDateTime=originalTransaction.m_sTrxDateTime;
        SAFTransaction.m_sOrigLocalTrxDateTime=originalTransaction.m_sLocalTrxDateTime;
        SAFTransaction.m_sOrigAquirerInsIDCode=originalTransaction.m_sAquirerInsIDCode;
        SAFTransaction.m_sOrigFWAquirerInsIDCode="00"; //based on moamen advice it should be 00
        SAFTransaction.m_sOrigRRNumber=originalTransaction.m_sRRNumber;
        SAFTransaction.m_sOrigLocalTrxDate=originalTransaction.m_sLocalTrxDateTime.substring(0,6);//check with moamen

        return SAFTransaction;

    }
    public static void SAVE_IN_SAF(POSTransaction oPostrx)
    {
                       //todo save pos_transaction in DB

    }
    public static void SAVE_IN_REV(POSTransaction oPostrax)
    {
        //todo save transaction reversal

    }
    public static POSTransaction Load_from_SAF()
    {   POSTransaction oSAF_transaction=null;

        //todo load SAF Transaction;

        return oSAF_transaction;

    }
    public static POSTransaction Load_from_Reversal()
    {       POSTransaction oSAF_transaction=null;
            //todo get data and set it in return original transaction for reversal
            return oSAF_transaction;
    }

    /**
	\Function Name: CheckSAFLimits
	\Param  :
	\Return : boolean
	\Pre    :
	\Post   :
	\Author	: Moamen Ahmed
	\DT		: 13/07/2020
	\Des    : Check SAF cumulative limits for DESAFing processing ( Partail ) , TMS limits should be loaded for checking () , Specification document part b Page 4
*/

    public boolean CheckSAFLimits()
    {
        boolean bRetRes = false;

        // Rule #1 Default case

        // Rule #2 Check MAX Number SAF Depth
        if(m_iSAFTrxNumber >= m_iMax_SAF_Depth )
        {
            //Todo Log message

            bRetRes = true ;

            return bRetRes;
        }

        if (m_dSAFCumulativeAmount >= m_iMax_SAF_Cumulative_Amount)
        {
            //Todo Log message
            bRetRes = true  ;
            return bRetRes;
        }

        // Rule #3 Idle Time
	/*
		POS terminal is idle , Start  DeSAF processing
	*/




        //Todo Log message  not SAF Limits fired
        return bRetRes;
    }






}
