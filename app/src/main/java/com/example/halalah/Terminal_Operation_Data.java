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
        public double    m_dTermReconciliationAmount;  // Adding All approved/accepted transaction amount
        public int       m_iTermApprovedTrxCounter;    // Counter of approved transaction usded to for checking number of approved trx against reconcilation trx limit
        public String m_sTerminal_Contactless_Floor_Limit;
        public String m_sTerminal_Contactless_Transaction_Limit;
        public String m_sTerminal_CVM_Required_Limit;


        /*******************
         * Terminal Status *
         *******************/
        //Terminal Dial Indicator
        public  String terminal_dial_indicator;// '1'-9', 'A' - 'F' (i.e. total range is '1'-'F').
        //Printer Status
        public  String Printer_Status;  //'0' = No printer. '1' = Out of paper. '2' = Plain paper receipt.
        //Idle Time
        public  String Idle_Time; //hhmmss
        //Magnetic Reader Status
        public  String Magnetic_Reader_Status;// '0'=Okay. '1' = Out of order.
        //Chip Card Reader Status
        public  String Chip_Card_Reader_Status;// '0'=Okay. '1' = Out of order.
        //GPS Location Coordinates
        public  String GPS_Location_Coordinates;// ANNNNNNANNNNNNN e.g. N402646W0795856 Which represents, N 40° 26′ 46″ W 079° 58′ 56″
        //Contactless Reader Status
        public  String Contactless_Reader_Status;// '0'=Okay. '1' = Out of order. '9' = Not supported.
        //Connection Start Time
        public  String Connection_Start_Time; //HHMMSSsss As 24 hour clock
        //Connection End Time
        public  String Connection_End_Time; //HHMMSSsss As 24 hour clock
        //Request Sent Time
        public  String Request_Sent_Time; //HHMMSSsss As 24 hour clock
        //Response Received Time
        public  String Response_Received_Time; //HHMMSSsss As 24 hour clock
        //Performance Timers Reference
        public  String Performance_Timers_Reference; //original RRN for the online authorization or financial message for which the timers refer to.
        //mada EFTPOS specification release version
        public  String mada_EFTPOS_specification_release_version; //The POS should send the version number without dots, and with 2 digits each with a leading zero, if applicable, for the Major, Minor and Patch specification version numbers i.e. Version 6.0.3 should be expressed as 060003 and Version 10.2.0 should be expressed as 100200
        //Connection Details
        public  String Connection_Details; //Connection Priority ‘01’ Primary ‘02’ Secondary,   Network Service Provider (NSP) ‘01’ iNET ‘02’ Mobily ‘03’ Zain ‘04’ Sky Band ‘05’ Geidea ,   Provider ‘01’ STC ‘02’ Mobily ‘03’ Zain ‘04’ Sky Band  , Connection Method ‘01’ Dial-up ‘02’ SIM ‘03’ TCP/IP ‘04’ VSAT ‘05’ DSL ‘06’ WiFi


    //Terminal Online Flag
    public  String Terminal_TMS_Flag; // '0'=No action. '1' = Go on-line.
    //Force Reconciliation Flag
    public  String Force_Reconciliation_Flag;// '0'=No action. '1' = Go on-line.
    //Terminal Global Limits
    public String m_sMaximum_transaction_amount_indicator;
    public String m_sMaximum_transaction_amount;


    public Terminal_Operation_Data(){
        m_iPinKeyboardMode=1;
        m_sMerchantID="111111111111111";
        m_sTerminalID="12345678";
        sMerchant_Category_Code="1234";
        sAcquirer_ID="HALA";
        m_sCurrencycode="0628";
        m_sCountrycode="0628";
        TerminalType=22;
        saf_info=new SAF_Info();
        g_NumberOfCardSchemes = 10;
    }


}


