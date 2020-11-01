package com.example.halalah;


import android.content.Context;

import com.example.halalah.TMS.Device_Specific;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.storage.CommunicationInfo;

import java.io.Serializable;

/**
 * Header Terminal operation Data
 * \Class Name: Terminal_Operation_Data
 * \Param  :
 * \Return :
 * \Pre    :
 * \Post   :
 * \Author	: mostafa hussiny
 * \DT		: 5/31/2020
 * \Des    : Container for Terminal operation DATA
 */
public class Terminal_Operation_Data implements Serializable {




        public int TerminalType;
        public  boolean m_bregistered;
        public  boolean m_TMS_Downloaded;
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
        public  CardSchemeTotals [] g_TerminalTotals; /* Terminal Totals */
        public double    m_dTermReconciliationAmount;  // Adding All approved/accepted transaction amount
        // public double   m_dMaxtermReconciliationAmount;  // maximum total amount to force recosile
        public int       m_iTermApprovedTrxCounter;    // Counter of approved transaction usded to for checking number of approved trx against reconcilation trx limit
        //   public int      m_iTermMaxapprovedtrxcounter;  // maximum transactions to force reconsile
        public String m_sTerminal_Contactless_Floor_Limit;
        public String m_sTerminal_Contactless_Transaction_Limit;
        public String m_sTerminal_CVM_Required_Limit;
        public String m_sTRMSID;
        public String m_szBDK;    // todo to be encrypted and saved in secure area not operation data
        public byte[] m_CurrentKSN;
        public int m_ireconCounter;  //number of reconciliation on the terminal

        /*******************
         * Terminal Device Specifics  *
         *******************/

        public Device_Specific m_DeviceSpecific;

         /*******************
         * Terminal retailer options *
         *******************/
            public enum Terminallanguage{
                Arabic,
            English
            }
            public Terminallanguage enum_Terminallanguage;

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

    //TMSCoonect
    public String m_sTMSHeader;
    public int TMS_currentcount;
    public int TMS_endcount;

    //COMMUNICATION
    public CommunicationInfo communicationInfo;
    public String Hostip;
    public int Hostport;

    public Terminal_Operation_Data(){
        m_bregistered = false;
        m_iPinKeyboardMode=1;
        m_sMerchantID="000000000000";
        m_sTerminalID="0000000000000000";
        sMerchant_Category_Code="5411";
        sAcquirer_ID="588847";
        m_sCurrencycode="682";
        m_sCountrycode="682";
        TerminalType=22;
        saf_info=new SAF_Info();
        g_NumberOfCardSchemes = 10;
        m_iSTAN=100;
        m_CurrentKSN= BCDASCII.hexStringToBytes( "00000000000000000000");
        m_szBDK = "0123456789ABCDEFFEDCBA9876543210";
        m_sMaximum_transaction_amount_indicator="1111111111";
        m_sMaximum_transaction_amount="999999999999";
        Mada_Auto_Selection=1;

        m_sTerminal_Contactless_Floor_Limit="000000001000";
        m_sTerminal_Contactless_Transaction_Limit="000000050000";
        m_sTerminal_CVM_Required_Limit="000000007000";

        //TerminalStatus
        //Terminal Dial Indicator
         terminal_dial_indicator ="1";// '1'-9', 'A' - 'F' (i.e. total range is '1'-'F').
        //Printer Status
        Printer_Status="2";  //'0' = No printer. '1' = Out of paper. '2' = Plain paper receipt.
        //Idle Time
        Idle_Time="010101"; //hhmmss
        //Magnetic Reader Status
        Magnetic_Reader_Status="0";// '0'=Okay. '1' = Out of order.
        //Chip Card Reader Status
        Chip_Card_Reader_Status="0";// '0'=Okay. '1' = Out of order.
        //GPS Location Coordinates
        GPS_Location_Coordinates="N402646W0795856";// ANNNNNNANNNNNNN e.g. N402646W0795856 Which represents, N 40° 26′ 46″ W 079° 58′ 56″
        //Contactless Reader Status
        Contactless_Reader_Status="0";// '0'=Okay. '1' = Out of order. '9' = Not supported.
        //Connection Start Time
        Connection_Start_Time="000000000"; //HHMMSSsss As 24 hour clock
        //Connection End Time
        Connection_End_Time="000000000"; //HHMMSSsss As 24 hour clock
        //Request Sent Time
        Request_Sent_Time="000000000"; //HHMMSSsss As 24 hour clock
        //Response Received Time
        Response_Received_Time="000000000"; //HHMMSSsss As 24 hour clock
        //Performance Timers Reference
        Performance_Timers_Reference="123456789123456"; //original RRN for the online authorization or financial message for which the timers refer to.
        //mada EFTPOS specification release version
        mada_EFTPOS_specification_release_version="060009"; //The POS should send the version number without dots, and with 2 digits each with a leading zero, if applicable, for the Major, Minor and Patch specification version numbers i.e. Version 6.0.3 should be expressed as 060003 and Version 10.2.0 should be expressed as 100200
        //Connection Details
        Connection_Details="01010101"; //Connection Priority ‘01’ Primary ‘02’ Secondary,   Network Service Provider (NSP) ‘01’ iNET ‘02’ Mobily ‘03’ Zain ‘04’ Sky Band ‘05’ Geidea ,   Provider ‘01’ STC ‘02’ Mobily ‘03’ Zain ‘04’ Sky Band  , Connection Method ‘01’ Dial-up ‘02’ SIM ‘03’ TCP/IP ‘04’ VSAT ‘05’ DSL ‘06’ WiFi
        g_TerminalTotals = new CardSchemeTotals[10];
        //TMSHEADER
        m_sTMSHeader="3040000";
         TMS_currentcount= 0;
        TMS_endcount = 0;
        m_TMS_Downloaded = false;
        //communication
        Hostip = "192.168.8.154";
        Hostport = 2030;



    }



}


