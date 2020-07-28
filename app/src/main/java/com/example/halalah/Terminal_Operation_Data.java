package com.example.halalah;


/** Header Terminal operation Data
 \Class Name: Terminal_Operation_Data
 \Param  :
 \Return :
 \Pre    :
 \Post   :
 \Author	: mostafa hussiny
 \DT		: 5/31/2020
 \Des    : Container for Terminal operation DATA
 */
public class Terminal_Operation_Data {



    public Terminal_Operation_Data(){
                m_iPinKeyboardMode=1;
                m_sMerchantID="111111111111111";
                m_sTerminalID="12345678";
                sMerchant_Category_Code="1234";
                sAcquirer_ID="AAIB";
                m_sCurrencycode="0628";
                m_sCountrycode="0628";
                TerminalType=22;
                saf_info=new SAF_Info();
                g_NumberOfCardSchemes = 10;
        }
        public int TerminalType;
        public  boolean m_bregistered;
        public  int m_iSTAN;
        public int m_iTransactionCounter;
        public  String m_sTerminalID;
        public  String m_sMerchantID;
        public String m_sCurrencycode;
        public String m_sCountrycode;
        public  int Mada_Auto_Selection;
        public  int m_iPinKeyboardMode; // 1 for out of order 0 for inorder
        public int lastreconcile_time;
        public SAF_Info saf_info;
        public String sMerchant_Category_Code;
        public String sAcquirer_ID;
        public boolean breversal_flg;
        public boolean breconsile_flag;   // to force reconciliation
        public boolean bDeSAF_flag;
        public boolean bTMS_flag;
        public int MaxConRetry;   //max connection Retry count
        public  int g_NumberOfCardSchemes;    /* Number of supported card scheme is 10 */
        public  CardSchemeTotals [] g_TerminalTotals = new CardSchemeTotals[g_NumberOfCardSchemes]; /* Terminal Totals */
        double    m_dTermReconciliationAmount;  // Adding All approved/accepted transaction amount
        int       m_iTermApprovedTrxCounter;    // Counter of approved transaction usded to for checking number of approved trx against reconcilation trx limit





}


