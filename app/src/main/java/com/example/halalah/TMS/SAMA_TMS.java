package com.example.halalah.TMS;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * all TMS parameters
 */

enum TMS_TAG
    {
        //1 Retailer Data
        //////////////////
        //segment1
        /////////////////
        Next_load,
        Reconciliation_time,
        Retailer_Name_Arabic,
        Retailer_Number_English,
        Retailer_Name_English,
        Retailer_Language_Indicator_D2,
        Terminal_Currency_Code,
        Terminal_Country_Code,
        Transaction_Currency_Exponent,
        Currency_Symbol_Arabic,
        Currency_Symbol_English,
        Arabic_Receipt_1,
        Arabic_Receipt_2,
        English_Receipt_1,
        English_Receipt_2,
        /////////////////
        //Segment2
        ////////////////
        Retailer_Address_1_Arabic,
        Retailer_Address_1_English,
        /////////////////
        //Segment3
        ////////////////
        Retailer_Address_2_Arabic,
        Retailer_Address_2_English,
        /////////////////
        //Segment4
        ////////////////
        Terminal_Capability,
        Additional_Terminal_Capabilities,
        Download_Phone_Number,
        EMV_Terminal_Type,
        Automatic_Load,
        SAF_Retry_Limit,
        SAF_Default_Message_Transmission_Number,
        /**********************************************************************/
        //2_Card_Scheme
        /////////////////
        //Segment1
        ////////////////
        m_sCard_Scheme_ID,
        m_sCard_Scheme_Name_Arabic,
        m_sCard_Scheme_Name_English,
        m_sCard_Scheme_Acquirer_ID,
        m_sMerchant_Category_Code,
        m_sMerchant_ID,
        m_sTerminal_ID,
        m_sEnable_EMV,
        m_sCheck_Service_Code,
        m_sOffline_Refund_PreAuthorization_Capture_Service_Indicator,
        /////////////////
        //Segment2
        ////////////////
        m_sTransactions_Allowed,
        m_sCardholder_Authentication,
        m_sSupervisor_Functions,
        m_sFloor_Limit_Indicator,
        m_sTerminal_Floor_Limit,
        m_sTerminal_Floor_Limit_Fallback,
        Maximum_sash_back,
        m_sMaximum_transaction_amount_indicator,
        m_sMaximum_amount_allowed,
        uhn_Check,
        m_sExpiry_Date_Position,
        /////////////////
        //Segment3
        ////////////////
        Card_Ranges,
        m_sCard_Prefix_Sequence_Indicator,

        /**********************************************************************/
        //3_Message_Text
        /////////////////
        //Segment1
        ////////////////
        m_sMessage_Code,
        m_sDisplay_Code,
        m_sArabic_Message_Text,
        m_sEnglish_Message_Text,

        /**********************************************************************/
        //4_Public_Key
        /////////////////
        //Segment1
        ////////////////
        RID,
        Key_Index,
        Hash_ID,
        Digital_Signature_ID,
        Public_Key,
        Exponent,
        Check_Sum,
        CA_Public_Key_Length,
        CA_Public_Key_Expiry_Date,
        /**********************************************************************/
        //5_Connection_Parameters
        /////////////////
        //Segment1_of_Dialup_Connections
        ////////////////
        Priority,
        Communication_Type, //01=Dialup_,_02=TCP/IP_,03=GPRS,04=wifi,05=GSM
        Phone_Number,
        Dial_Attempts_to_Phone,
        Connect__Time_for_Phone,
        Baud_Rate_Default2,
        Parity_Bit,
        Data_Bit,
        Stop_Bit,
        Response_timeout,
        /////////////////
        //Segment_1_of_TCP/IP_Connections
        /////////////////
        Network_IP_Address,
        Network_TCP_Support,
        Count_Access_Retries,
        Response_Time_Out,
        SSL_Certificate_File,
        /////////////////
        //Segment_1_of_GPRS_Connections
        /////////////////
        GPRS_dial_Number,
        GPRS_access_point_name,
        Connect__Time_for_GPRS_phone,
        Network_IP_address,
        Network_TCP_port,
        Dial_attempts_to_network,
        Response_time_out,

        /////////////////
        //Segment_1_of_wifi_Connections
        /////////////////
        //like_GRPS
        /////////////////
        //Segment_1_of_GSm_sonnections
        /////////////////
        //like_dialup


        /**********************************************************************/
        //6_Device_Specific
        ///////////////////
        //Segment_1_of_Device_Specific
        ///////////////////
        Data,
        Card_Scheme,
        m_sTerminal_Contactless_Transaction_Limit,
        m_sTerminal_CVM_Required_Limit,
        m_sTerminal_Contactless_Floor_Limit,
        Card_Scheme_Dummy1,
        m_sTerminal_Contactless_Transaction_Limit1,
        m_sTerminal_CVM_Required_Limit1,
        m_sTerminal_Contactless_Floor_Limit1,
        Card_Scheme_dummy2,
        m_sTerminal_Contactless_Transaction_Limit2,
        m_sTerminal_CVM_Required_Limit2,
        m_sTerminal_Contactless_Floor_Limit2,
        m_sMax_SAF_Depth,
        m_sMax_SAF_Cumulative_Amount,
        m_sIdle_Time,
        m_sMax_Reconciliation_Amount,
        m_sMax_Transactions_Processed,
        m_sQR_Code_Print_Indicator,
        /**********************************************************************/
        //8_AID_Data
        /////////////
        //Segment_1_of_AID_List
        //////////////////////
        AID_1,
        AID_2,
        AID_3,
        AID_4,
        AID_5,
        AID_6,
        AID_7,
        AID_8,
        AID_9,
        AID_10,
        ////////////
        //Segment_1_of_AID_Data,
        ///////////
        AID,
        AID_Label,
        Terminal_AID_version_numbers,
        Exact_only_selection,
        Skip_EMV_processing,
        Default_TDOL,
        Default_DDOL,
        EMV_additional_tags,
        Denial_action_code,
        Online_action_code,
        Default_action_code,
        Threshold_Value_for_Biased_Random_Selection,
        Target_Percentage,
        Maximum_Target_Percentage_for_Biased_Random_Selection,

        /**********************************************************************/
        //9_Revoked_Certificates
        ////////////////////
        //Segment 1 of Revoked Certificates
        ///////////////////
        RID_Revoked_Certificate,
        IDX,
        Cert_serial_number,

    }

/*************
 * Author: Eng\MostafaHussiny
 * Date:8/4/2020
 * class contains all SAMA TMS parameters and can be Get using Get_sama_param , Load_Sama_param , Save all Data in files on application directory
**************/
public class SAMA_TMS implements Serializable
    {

        private static final String DT_Retailer_Data="01",DT_Card_Scheme ="02",DT_Message_Text="03",DT_Key="04",
                            DT_Connection_Parameters="05",DT_Device_Specific="06",DT_AID_List="07",
                            DT_AID_Data="08",DT_Revoked_Certificates="09";
        private static final String  mada="P1", Visa_Credit="VC" , Visa_Debit="VD", Master_Card="MC", Maestro="DM", AMEX="AX", UnionPay="UP", JCB="JB", Discover="DC";
        private static final String  DIAL_UP="01",TCP_IP="02",GPRS="03",WIFI="04",GSM="05";
        private static final int MAXBUFFERLEN=2048;
        private static final int MAXSCHEMES=10;
        private static final int MAXCAPK=30;
        private static final int MAXMSG=999;









        public Retailer_Data retailer_data;
        public Card_Scheme card_scheme;
        public Message_Text[] message_text;
        public Message_Text empty;
        public Public_Key public_keys;
        public Connection_Parameters connection_parameters;
        public Device_Specific device_specific;
        public AID_List aid_list;
        public AID_Data aid_data;
        public Revoked_Certificates revoked_certificates;
        public String NUM_REC;
        public String NUM_REC_DLN;


        char GS=''; //group seperator  1D
        char FS='';// field seperator  1C

        public SAMA_TMS()
        {   retailer_data=new Retailer_Data();
            card_scheme = new Card_Scheme();
            //public_keys = new Public_Key[MAXCAPK];
            public_keys = new Public_Key();
            message_text=new Message_Text[MAXMSG];
            empty=new Message_Text();
            Arrays.fill(message_text,empty);
            connection_parameters = new Connection_Parameters();
            device_specific=new Device_Specific();
            aid_list=new AID_List();
            aid_data=new AID_Data();
            revoked_certificates=new Revoked_Certificates();
        }
      public  void Get_Sama_param(String DE72_Buffer, Context applicationContext)
                    {

                       /* Intent intent = new Intent(applicationContext,ildescreenActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        applicationContext.startActivity(intent);*/
                        /*  int i=8;  //skip header+GS
                        int dataOffset=0;
                        int segmant=0;

                        String  data = new char[MAXBUFFERLEN];
                        //int num_rec=Integer.getInteger(NUM_REC);
                       // int num_rec_count=Integer.getInteger(NUM_REC_DLN);
                      //  Check_HDR(DE72_Buffer_TMS);
                        String  sDatatype={'0','0'};
//                        Log.d("NUMBER OF RECORDS", NUM_REC);
  //                      Log.d("NUMBER OF RECORDS TO BE DOWNLOADED", NUM_REC_DLN);

                                System.arraycopy(DE72_Buffer, i, sDatatype, dataOffset, 2);//DE72_Buffer.substring(i + 1, i + 3);
                                i=i+2; // sDatatype

                               String ssDatatype = new String(sDatatype);

                                segmant= Character.getNumericValue(DE72_Buffer[i]);
                                i++;
                                switch(ssDatatype) {

                                    case DT_Retailer_Data:


                                       switch (segmant)
                                       {


                                           case 1: //segmant 1 exist


                                               if(DE72_Buffer[i]!='') {
                                                   
                                                   //element=1
                                                  // System.arraycopy(DE72_Buffer, i, retailer_data.m_sNext_load, 0, 10);//(YYMMDDHHMM) next load time
                                                  retailer_data.m_sNext_load= Arrays.copyOfRange(DE72_Buffer,i,i+10);

                                                   Log.d("Nextloadtime", "nextloadtime: "+ retailer_data.m_sNext_load);
                                                   i = i + 10;
                                               }
                                               else
                                               {


                                                   do {
                                                       int element = 2;
                                                       while (DE72_Buffer[i++] == '') //seperator exist
                                                       {

                                                           if (DE72_Buffer[i] != '') //check values not seperator
                                                           {

                                                               switch (element)
                                                               {


                                                                   case 2:
                                                                       // System.arraycopy(DE72_Buffer, i, retailer_data.m_sReconciliation_time, 0, 4); //HHMM Reconciliation_time
                                                                       retailer_data.m_sReconciliation_time= Arrays.copyOfRange(DE72_Buffer,i,i+4);
                                                                       i=i+4;
                                                                       break;
                                                                   case 3:
                                                                       //System.arraycopy(DE72_Buffer, i, retailer_data.m_sRetailer_Name_Arabic, 0, 32); //Retailer Name to be printed on all receipts. Field contains Arabic text. Note: Arabic fields must be reversed

                                                                       retailer_data.m_sRetailer_Name_Arabic= Arrays.copyOfRange(DE72_Buffer,i,i+32);
                                                                     //  i=i+arabiclen;
                                                                       break;
                                                                   case 4:
                                                                     //  System.arraycopy(DE72_Buffer, i, retailer_data.m_sRetailer_Name_English, 0, 32);
                                                                       retailer_data.m_sRetailer_Number_English= Arrays.copyOfRange(DE72_Buffer,i,i+32);
                                                                       i=i+32;
                                                                       break;

                                                                     String  Retailer_Number_English;
                                                                   String  Retailer_Name_English;
                                                                   String  Retailer_Language_Indicator_D2;
                                                                   String  Terminal_Currency_Code;
                                                                   String  Terminal_Country_Code;
                                                                   String  Transaction_Currency_Exponent;
                                                                   String  Currency_Symbol_Arabic;
                                                                   String  Currency_Symbol_English;
                                                                   String  Arabic_Receipt_1;
                                                                   String  Arabic_Receipt_2;
                                                                   String  English_Receipt_1;
                                                                   String  English_Receipt_2;/*
                                                               }
                                                               element++;
                                                           }
                                                           else
                                                           {
                                                               element++;
                                                           }

                                                       }

                                                   } while (i < (DE72_Buffer.length + 1));
                                               }
                                               break;
                                           case 2:
                                               /////////////////
                                               //Segment2
                                               ////////////////
                                               String  Retailer_Address_1_Arabic;
                                               String  Retailer_Address_1_English;
                                               break;
                                           case 3:
                                               /////////////////
                                               //Segment3
                                               ////////////////
                                               String  Retailer_Address_2_Arabic;
                                               String  Retailer_Address_2_English;
                                               break;
                                           case 4:
                                               /////////////////
                                               //Segment4
                                               ////////////////
                                               String  Terminal_Capability;
                                               String  Additional_Terminal_Capabilities;
                                               String  Download_Phone_Number;
                                               String  EMV_Terminal_Type;
                                               String  Automatic_Load;
                                               String  SAF_Retry_Limit;
                                               String  SAF_Default_Message_Transmission_Number;
                                               break;
                                       }
                                        break;
                                    case DT_Card_Scheme:
                                        break;
                                    case DT_Message_Text:
                                        break;
                                    case DT_Key:
                                        break;
                                    case DT_Connection_Parameters:
                                        break;
                                    case DT_Device_Specific:
                                        break;
                                    case DT_AID_List:
                                        break;
                                    case DT_AID_Data:
                                        break;
                                    case DT_Revoked_Certificates:
                                        break;

                                }











*/
                       
                       
                        String[] temp;
                        String sDatatype="";
                        String[] segments = DE72_Buffer.split("\u001D");
                        //segment[0] is header if needed to check
                        //checking data type
                        sDatatype=segments[1].substring(0,2);

                        switch(sDatatype)
                        {
                            case DT_Retailer_Data:
                            //retailer Data
                                for (int i = 1; i < segments.length; i++)
                                {


                                    //start loading each segement elements for retailer data

                                    switch (segments[i].substring(2,3)) {

                                        //////////////////
                                        //segment1
                                        /////////////////
                                        case "1":
                                            String[] ret_seg1_elements = new String[15];
                                            Arrays.fill(ret_seg1_elements, "");
                                            temp=segments[i].split("\u001C");
                                            System.arraycopy(temp,0,ret_seg1_elements,0,temp.length);
                                            if(ret_seg1_elements[0].length()==13 )
                                            {   String nextload=ret_seg1_elements[0].substring(3,12);  //check max length 12 or 13
                                                 retailer_data.m_sNext_load = nextload;
                                            }
                                            retailer_data.m_sReconciliation_time = ret_seg1_elements[1];
                                            retailer_data.m_sRetailer_Name_Arabic = ret_seg1_elements[2];
                                            retailer_data.m_sRetailer_Number_English = ret_seg1_elements[3];
                                            retailer_data.m_sRetailer_Name_English = ret_seg1_elements[4];
                                            retailer_data.m_sRetailer_Language_Indicator_D2 = ret_seg1_elements[5];
                                            retailer_data.m_sTerminal_Currency_Code = ret_seg1_elements[6];
                                            retailer_data.m_sTerminal_Country_Code = ret_seg1_elements[7];
                                            retailer_data.m_sTransaction_Currency_Exponent = ret_seg1_elements[8];
                                            retailer_data.m_sCurrency_Symbol_Arabic = ret_seg1_elements[9];
                                            retailer_data.m_sCurrency_Symbol_English = ret_seg1_elements[10];
                                            retailer_data.m_sArabic_Receipt_1 = ret_seg1_elements[11];
                                            retailer_data.m_sArabic_Receipt_2 = ret_seg1_elements[12];
                                            retailer_data.m_sEnglish_Receipt_1 = ret_seg1_elements[13];
                                            retailer_data.m_sEnglish_Receipt_2 = ret_seg1_elements[14];
                                            break;
                                        case "2":
                                            String ret_seg2_elements[] = new String[2];
                                            Arrays.fill(ret_seg2_elements, "");
                                            temp=segments[i].split("\u001C");
                                            System.arraycopy(temp,0,ret_seg2_elements,0,temp.length);

                                            /////////////////
                                            //Segment2
                                            ////////////////
                                            if(ret_seg2_elements[0].length()>3 )
                                            {   String address=ret_seg2_elements[0].substring(3,ret_seg2_elements[0].length());
                                                retailer_data.m_sRetailer_Address_1_Arabic = address;
                                            }

                                            retailer_data.m_sRetailer_Address_1_English = ret_seg2_elements[1];
                                            break;
                                        case "3":
                                            String ret_seg3_elements[] = new String[2];
                                            Arrays.fill(ret_seg3_elements, "");
                                            temp=segments[i].split("\u001C");
                                            System.arraycopy(temp,0,ret_seg3_elements,0,temp.length);
                                            /////////////////
                                            //Segment3
                                            ////////////////
                                            if(ret_seg3_elements[0].length()>3 )
                                            {   String address=ret_seg3_elements[0].substring(3,ret_seg3_elements[0].length());  //check max length 12 or 13
                                                retailer_data.m_sRetailer_Address_2_Arabic = address;
                                            }


                                            retailer_data.m_sRetailer_Address_2_English = ret_seg3_elements[1];
                                            break;
                                        case "4":
                                            String[] ret_seg4_elements = new String[7];
                                            Arrays.fill(ret_seg4_elements, "");
                                            temp=segments[i].split("\u001C");
                                            System.arraycopy(temp,0,ret_seg4_elements,0,temp.length);

                                            /////////////////
                                            //Segment4
                                            ////////////////
                                            if(ret_seg4_elements[0].length()>3)
                                            {      String address=ret_seg4_elements[0].substring(3,ret_seg4_elements[0].length());  //getting terminal cap
                                                retailer_data.m_sTerminal_Capability = address;
                                            }
                                            retailer_data.m_sAdditional_Terminal_Capabilities = ret_seg4_elements[1];
                                            retailer_data.m_sDownload_Phone_Number = ret_seg4_elements[2];
                                            retailer_data.m_sEMV_Terminal_Type = ret_seg4_elements[3];
                                            retailer_data.m_sAutomatic_Load = ret_seg4_elements[4];
                                            retailer_data.m_sSAF_Retry_Limit = ret_seg4_elements[5];
                                            retailer_data.m_sSAF_Default_Message_Transmission_Number = ret_seg4_elements[6];
                                           
                                            break;
                                    }


                                 }
                                //todo save retailer data
                                break;
                            case DT_Card_Scheme:

                                //card scheme
                                 for(int i=1;i<segments.length;i++)
                                 {    int scheme_id=-1;
                                     // checking scheme ID along with segement number to add to the specific ID
                                   switch(segments[i].substring(2,3)) {
                                         case "1":
                                         /////////////////
                                         //Segment1   mada="P1", Visa_Credit="VC" , Visa_Debit="VD", Master_Card="MC", Maestro="DM", AMEX="AX", UnionPay="UP", JCB="JB", Discover="DC"
                                         ////////////////
                                          String crd_seg1_elements[] = new String[10];
                                          Arrays.fill(crd_seg1_elements, "");
                                          temp=segments[i].split("\u001C");
                                          System.arraycopy(temp,0,crd_seg1_elements,0,temp.length);
                                           scheme_id=checkcardscheme(crd_seg1_elements[0].substring(3,5));

                                          card_scheme.m_sCard_Scheme_ID=crd_seg1_elements[0].substring(3,5);
                                          card_scheme.m_sCard_Scheme_Name_Arabic=crd_seg1_elements[1];
                                          card_scheme.m_sCard_Scheme_Name_English=crd_seg1_elements[2];
                                          card_scheme.m_sCard_Scheme_Acquirer_ID=crd_seg1_elements[3];
                                          card_scheme.m_sMerchant_Category_Code=crd_seg1_elements[4];
                                          card_scheme.m_sMerchant_ID=crd_seg1_elements[5];
                                          card_scheme.m_sTerminal_ID=crd_seg1_elements[6];
                                          card_scheme. m_sEnable_EMV=crd_seg1_elements[7];
                                          card_scheme.m_sCheck_Service_Code=crd_seg1_elements[8];
                                          card_scheme.m_sOffline_Refund_PreAuthorization_Capture_Service_Indicator=crd_seg1_elements[9];
                                         break;
                                         case "2":
                                         /////////////////
                                         //Segment2
                                         ////////////////
                                         String crd_seg2_elements[] = new String[14];
                                         Arrays.fill(crd_seg2_elements, "");
                                         temp=segments[i].split("\u001C");
                                         System.arraycopy(temp,0,crd_seg2_elements,0,temp.length);
                                         scheme_id=checkcardscheme(crd_seg2_elements[0].substring(3,5));
                                         card_scheme.m_sTransactions_Allowed=crd_seg2_elements[1];
                                         card_scheme.m_sCardholder_Authentication=crd_seg2_elements[2];
                                         card_scheme.m_sSupervisor_Functions=crd_seg2_elements[3];
                                         card_scheme.m_sManual_entry_allowed=crd_seg2_elements[4];
                                         card_scheme.m_sFloor_Limit_Indicator=crd_seg2_elements[5];
                                         card_scheme.m_sTerminal_Floor_Limit=crd_seg2_elements[6];
                                         card_scheme.m_sTerminal_Floor_Limit_Fallback=crd_seg2_elements[7];
                                         card_scheme.m_sMaximum_Cash_back=crd_seg2_elements[8];
                                         card_scheme.m_sMaximum_transaction_amount_indicator=crd_seg2_elements[9];
                                         card_scheme.m_sMaximum_amount_allowed=crd_seg2_elements[10];
                                         card_scheme.m_sLuhn_Check =crd_seg2_elements[11];
                                         card_scheme.m_sExpiry_Date_Position=crd_seg2_elements[12];
                                         card_scheme.m_sDelay_Call_Set_up=crd_seg2_elements[13];
                                             break;
                                         case "3":
                                         /////////////////
                                         //Segment3
                                         ////////////////
                                         String crd_seg3_elements[] = new String[3];
                                         Arrays.fill(crd_seg3_elements, "");
                                         temp=segments[i].split("\u001C");
                                         System.arraycopy(temp,0,crd_seg3_elements,0,temp.length);
                                         scheme_id=checkcardscheme(crd_seg3_elements[0].substring(3,5));
                                         card_scheme.cardranges=crd_seg3_elements[1].split("");
                                         card_scheme.m_sCard_Prefix_Sequence_Indicator=crd_seg3_elements[2];
                                         break;
                                     }
                                     // append&save(cardscheme);    // card scheme maay be sent in two meesages so we need to add to last one
                                 }

                                break;
                            case DT_Message_Text:

                                for (int i = 1; i < segments.length; i++) {
                                    /////////////////
                                    //Segment1
                                    ////////////////
                                    String msg_seg1_elements[] = new String[4];
                                    Arrays.fill(msg_seg1_elements, "");
                                    temp = segments[i].split("\u001C");
                                    System.arraycopy(temp, 0, msg_seg1_elements, 0, temp.length);
                                    if (msg_seg1_elements[0].length() > 3) {
                                        String m_sMessage_Code = msg_seg1_elements[0].substring(3, msg_seg1_elements[0].length());  //check max length 3
                                          message_text[i].m_sMessage_Code=m_sMessage_Code;
                                    }

                                     message_text[i].m_sDisplay_Code=msg_seg1_elements[1];
                                     message_text[i].m_sArabic_Message_Text=msg_seg1_elements[2];
                                     message_text[i].m_sEnglish_Message_Text=msg_seg1_elements[3];
                                }
                                //append&Save(message_test)   // note we need to append to the file as new messages will added
                                break;
                            case DT_Key:
                                    for (int i = 1; i < segments.length; i++) {
                                        /////////////////
                                        //Segment1
                                        ////////////////
                                        String Capk_seg1_elements[] = new String[9];
                                        Arrays.fill(Capk_seg1_elements, "");

                                        temp = segments[i].split("\u001C");
                                        System.arraycopy(temp, 0, Capk_seg1_elements, 0, temp.length);
                                        if (Capk_seg1_elements[0].length() > 3 ||Capk_seg1_elements[0].length()==13) {
                                            String CAPK = Capk_seg1_elements[0].substring(3, Capk_seg1_elements[0].length());  //check max length 3
                                            public_keys.RID=CAPK;

                                        }
                                        public_keys.Key_Index=Capk_seg1_elements[1];
                                        public_keys.Hash_ID=Capk_seg1_elements[2];
                                        public_keys.Digital_Signature_ID=Capk_seg1_elements[3];
                                        public_keys.Public_Key=Capk_seg1_elements[4];
                                        public_keys.Exponent=Capk_seg1_elements[5];
                                        public_keys.Check_Sum=Capk_seg1_elements[6];
                                        public_keys.CA_Public_Key_Length=Capk_seg1_elements[7];
                                        public_keys.CA_Public_Key_Expiry_Date=Capk_seg1_elements[8];

                                    }
                                // Save(public_key)         // save each data on file

                                break;
                            case DT_Connection_Parameters:
                                for (int i = 1; i < segments.length; i++) {
                                    /////////////////
                                    //Segment1
                                    ////////////////
                                    String conn_seg1_elements[] = new String[10];
                                    Arrays.fill(conn_seg1_elements, "");

                                    temp = segments[i].split("\u001C");
                                    System.arraycopy(temp, 0, conn_seg1_elements, 0, temp.length);

                                     String priority = conn_seg1_elements[0].substring(3, conn_seg1_elements[0].length());  //check max length 3
                                     String connection_type =conn_seg1_elements[1]; // checking communication type
                                     if(priority.equals("1"))
                                        {
                                            switch(connection_type)
                                                {
                                                    case DIAL_UP:
                                                        
                                                        connection_parameters.conn_primary.dialup. Priority=priority;
                                                        connection_parameters.conn_primary.dialup. Communication_Type=connection_type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
                                                        connection_parameters.conn_primary.dialup. Phone_Number=conn_seg1_elements[2];
                                                        connection_parameters.conn_primary.dialup. Dial_Attempts_to_Phone=conn_seg1_elements[3];
                                                        connection_parameters.conn_primary.dialup. Connect__Time_for_Phone=conn_seg1_elements[4];
                                                        connection_parameters.conn_primary.dialup. Baud_Rate_Default2=conn_seg1_elements[5];
                                                        connection_parameters.conn_primary.dialup. Parity_Bit=conn_seg1_elements[6];
                                                        connection_parameters.conn_primary.dialup. Data_Bit=conn_seg1_elements[7];
                                                        connection_parameters.conn_primary.dialup. Stop_Bit=conn_seg1_elements[8];
                                                        connection_parameters.conn_primary.dialup. Response_timeout=conn_seg1_elements[9];
                                                        break;
                                                    case TCP_IP:
                                                        connection_parameters.conn_primary.tcp_ip. Priority=priority;
                                                        connection_parameters.conn_primary.tcp_ip.Communication_Type=connection_type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
                                                        connection_parameters.conn_primary.tcp_ip.Network_IP_Address=conn_seg1_elements[2];
                                                        connection_parameters.conn_primary.tcp_ip.Network_TCP_Support=conn_seg1_elements[3];
                                                        connection_parameters.conn_primary.tcp_ip.Count_Access_Retries=conn_seg1_elements[4];
                                                        connection_parameters.conn_primary.tcp_ip.Response_Time_Out=conn_seg1_elements[5];
                                                        connection_parameters.conn_primary.tcp_ip.SSL_Certificate_File=conn_seg1_elements[6];
                                                        break;
                                                    case GPRS:
                                                        connection_parameters.conn_primary.gprs. Priority=priority;
                                                        connection_parameters.conn_primary.gprs. Communication_Type=connection_type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
                                                        connection_parameters.conn_primary.gprs. GPRS_dial_Number=conn_seg1_elements[2];
                                                        connection_parameters.conn_primary.gprs. GPRS_access_point_name=conn_seg1_elements[3];
                                                        connection_parameters.conn_primary.gprs. Connect__Time_for_GPRS_phone=conn_seg1_elements[4];
                                                        connection_parameters.conn_primary.gprs. Network_IP_address=conn_seg1_elements[5];
                                                        connection_parameters.conn_primary.gprs. Network_TCP_port=conn_seg1_elements[6];
                                                        connection_parameters.conn_primary.gprs. Dial_attempts_to_network=conn_seg1_elements[7];
                                                        connection_parameters.conn_primary.gprs. Response_time_out=conn_seg1_elements[8];
                                                        connection_parameters.conn_primary.gprs. SSL_Certificate_file=conn_seg1_elements[9];
                                                        break;
                                                    case WIFI:
                                                        connection_parameters.conn_primary.wifi. Priority=priority;
                                                        connection_parameters.conn_primary.wifi. Communication_Type=connection_type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
                                                        connection_parameters.conn_primary.wifi. Network_IP_Address=conn_seg1_elements[2];
                                                        connection_parameters.conn_primary.wifi. Network_TCP_Support=conn_seg1_elements[3];
                                                        connection_parameters.conn_primary.wifi. Count_Access_Retries=conn_seg1_elements[4];
                                                        connection_parameters.conn_primary.wifi. Response_Time_Out=conn_seg1_elements[5];
                                                        connection_parameters.conn_primary.wifi. SSL_Certificate_File=conn_seg1_elements[6];

                                             
                                               

                                                        break;
                                                    case GSM:
                                                        connection_parameters.conn_primary.gsm. Priority=priority;
                                                        connection_parameters.conn_primary.gsm. Communication_Type=connection_type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
                                                        connection_parameters.conn_primary.gsm. Phone_Number=conn_seg1_elements[1];
                                                        connection_parameters.conn_primary.gsm. Dial_Attempts_to_Phone=conn_seg1_elements[2];
                                                        connection_parameters.conn_primary.gsm. Connect__Time_for_Phone=conn_seg1_elements[3];
                                                        connection_parameters.conn_primary.gsm. Baud_Rate_Default2=conn_seg1_elements[4];
                                                        connection_parameters.conn_primary.gsm. Parity_Bit=conn_seg1_elements[5];
                                                        connection_parameters.conn_primary.gsm. Data_Bit=conn_seg1_elements[6];
                                                        connection_parameters.conn_primary.gsm. Stop_Bit=conn_seg1_elements[7];
                                                        connection_parameters.conn_primary.gsm. Response_timeout=conn_seg1_elements[8];
                                                        break;
                                                }


                                        }
                                     else
                                         {
                                             switch(connection_type)
                                             {
                                                 case DIAL_UP:

                                                     connection_parameters.conn_secondary.dialup. Priority=priority;
                                                     connection_parameters.conn_secondary.dialup. Communication_Type=connection_type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
                                                     connection_parameters.conn_secondary.dialup. Phone_Number=conn_seg1_elements[2];
                                                     connection_parameters.conn_secondary.dialup. Dial_Attempts_to_Phone=conn_seg1_elements[3];
                                                     connection_parameters.conn_secondary.dialup. Connect__Time_for_Phone=conn_seg1_elements[4];
                                                     connection_parameters.conn_secondary.dialup. Baud_Rate_Default2=conn_seg1_elements[5];
                                                     connection_parameters.conn_secondary.dialup. Parity_Bit=conn_seg1_elements[6];
                                                     connection_parameters.conn_secondary.dialup. Data_Bit=conn_seg1_elements[7];
                                                     connection_parameters.conn_secondary.dialup. Stop_Bit=conn_seg1_elements[8];
                                                     connection_parameters.conn_secondary.dialup. Response_timeout=conn_seg1_elements[9];
                                                     break;
                                                 case TCP_IP:
                                                     connection_parameters.conn_secondary.tcp_ip. Priority=priority;
                                                     connection_parameters.conn_secondary.tcp_ip.Communication_Type=connection_type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
                                                     connection_parameters.conn_secondary.tcp_ip.Network_IP_Address=conn_seg1_elements[2];
                                                     connection_parameters.conn_secondary.tcp_ip.Network_TCP_Support=conn_seg1_elements[3];
                                                     connection_parameters.conn_secondary.tcp_ip.Count_Access_Retries=conn_seg1_elements[4];
                                                     connection_parameters.conn_secondary.tcp_ip.Response_Time_Out=conn_seg1_elements[5];
                                                     connection_parameters.conn_secondary.tcp_ip.SSL_Certificate_File=conn_seg1_elements[6];
                                                     break;
                                                 case GPRS:
                                                     connection_parameters.conn_secondary.gprs. Priority=priority;
                                                     connection_parameters.conn_secondary.gprs. Communication_Type=connection_type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
                                                     connection_parameters.conn_secondary.gprs. GPRS_dial_Number=conn_seg1_elements[2];
                                                     connection_parameters.conn_secondary.gprs. GPRS_access_point_name=conn_seg1_elements[3];
                                                     connection_parameters.conn_secondary.gprs. Connect__Time_for_GPRS_phone=conn_seg1_elements[4];
                                                     connection_parameters.conn_secondary.gprs. Network_IP_address=conn_seg1_elements[5];
                                                     connection_parameters.conn_secondary.gprs. Network_TCP_port=conn_seg1_elements[6];
                                                     connection_parameters.conn_secondary.gprs. Dial_attempts_to_network=conn_seg1_elements[7];
                                                     connection_parameters.conn_secondary.gprs. Response_time_out=conn_seg1_elements[8];
                                                     connection_parameters.conn_secondary.gprs. SSL_Certificate_file=conn_seg1_elements[9];
                                                     break;
                                                 case WIFI:
                                                     connection_parameters.conn_secondary.wifi. Priority=priority;
                                                     connection_parameters.conn_secondary.wifi. Communication_Type=connection_type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
                                                     connection_parameters.conn_secondary.wifi. Network_IP_Address=conn_seg1_elements[2];
                                                     connection_parameters.conn_secondary.wifi. Network_TCP_Support=conn_seg1_elements[3];
                                                     connection_parameters.conn_secondary.wifi. Count_Access_Retries=conn_seg1_elements[4];
                                                     connection_parameters.conn_secondary.wifi. Response_Time_Out=conn_seg1_elements[5];
                                                     connection_parameters.conn_secondary.wifi. SSL_Certificate_File=conn_seg1_elements[6];




                                                     break;
                                                 case GSM:
                                                     connection_parameters.conn_secondary.gsm. Priority=priority;
                                                     connection_parameters.conn_secondary.gsm. Communication_Type=connection_type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
                                                     connection_parameters.conn_secondary.gsm. Phone_Number=conn_seg1_elements[1];
                                                     connection_parameters.conn_secondary.gsm. Dial_Attempts_to_Phone=conn_seg1_elements[2];
                                                     connection_parameters.conn_secondary.gsm. Connect__Time_for_Phone=conn_seg1_elements[3];
                                                     connection_parameters.conn_secondary.gsm. Baud_Rate_Default2=conn_seg1_elements[4];
                                                     connection_parameters.conn_secondary.gsm. Parity_Bit=conn_seg1_elements[5];
                                                     connection_parameters.conn_secondary.gsm. Data_Bit=conn_seg1_elements[6];
                                                     connection_parameters.conn_secondary.gsm. Stop_Bit=conn_seg1_elements[7];
                                                     connection_parameters.conn_secondary.gsm. Response_timeout=conn_seg1_elements[8];
                                                     break;
                                             }
                                         }
                                }
                                // save (connection_parameter)
                                break;

                            case DT_Device_Specific:
                                for (int i = 1; i < segments.length; i++) {
                                    /////////////////
                                    //Segment1              // Data length variable 256
                                    ////////////////  // impelmentation of array and segment will be over head but for systematic work and incase future device spec become segments
                                    String dspec_seg1_elements[] = new String[2];
                                    Arrays.fill(dspec_seg1_elements, "");

                                    temp = segments[i].split("\u001C");
                                    System.arraycopy(temp, 0, dspec_seg1_elements, 0, temp.length);
                                    if (dspec_seg1_elements[0].length() > 3)
                                    {   int index=3;
                                        int offset=2;
                                        device_specific. m_sCard_Scheme_MADA= dspec_seg1_elements[0].substring(index,index+offset);   //length 2//The value configured for the mada scheme (‘P1’) shall apply to all card schemes
                                        index=index+offset;
                                        offset=5;
                                        device_specific. m_sTerminal_Contactless_Transaction_Limit= dspec_seg1_elements[0].substring(index, index+offset);  //length 5
                                        index=index+offset;
                                        offset=5;
                                        device_specific. m_sTerminal_CVM_Required_Limit= dspec_seg1_elements[0].substring(index, index+offset);             //length 5
                                        index=index+offset;
                                        offset=5;
                                        device_specific. m_sTerminal_Contactless_Floor_Limit= dspec_seg1_elements[0].substring(index, index+offset);        //length 5
                                        index=index+offset;
                                        offset=2;
                                        device_specific. m_sCard_Scheme_VC= dspec_seg1_elements[0].substring(index, index+offset);                          //length 2
                                        index=index+offset;
                                        offset=5;
                                        device_specific. m_sTerminal_Contactless_Transaction_Limit1= dspec_seg1_elements[0].substring(index, index+offset); //length 5      //zeros as per spec Version 6.0.9
                                        index=index+offset;
                                        offset=5;
                                        device_specific. m_sTerminal_CVM_Required_Limit1= dspec_seg1_elements[0].substring(index, index+offset);            //length 5              //zeros as per spec Version 6.0.9
                                        index=index+offset;
                                        offset=5;
                                        device_specific. m_sTerminal_Contactless_Floor_Limit1= dspec_seg1_elements[0].substring(index, index+offset);       //length 5           //zeros as per spec Version 6.0.9
                                        index=index+offset;
                                        offset=2;
                                        device_specific. m_sCard_Scheme_MC= dspec_seg1_elements[0].substring(index, index+offset);                          //length 2
                                        index=index+offset;
                                        offset=5;
                                        device_specific. m_sTerminal_Contactless_Transaction_Limit2= dspec_seg1_elements[0].substring(index, index+offset); //length 5      //zeros as per spec Version 6.0.9
                                        index=index+offset;
                                        offset=5;
                                        device_specific. m_sTerminal_CVM_Required_Limit2= dspec_seg1_elements[0].substring(index, index+offset);            //length 5        //zeros as per spec Version 6.0.9
                                        index=index+offset;
                                        offset=5;
                                        device_specific. m_sTerminal_Contactless_Floor_Limit2= dspec_seg1_elements[0].substring(index, index+offset);       //length 5        //zeros as per spec Version 6.0.9
                                        index=index+offset;
                                        offset=3;
                                        device_specific. m_sMax_SAF_Depth= dspec_seg1_elements[0].substring(index, index+offset);                           //length 3
                                        index=index+offset;
                                        offset=7;
                                        device_specific. m_sMax_SAF_Cumulative_Amount= dspec_seg1_elements[0].substring(index, index+offset);               //length 7 //not included decimal point amounts
                                        index=index+offset;
                                        offset=3;
                                        device_specific. m_sIdle_Time= dspec_seg1_elements[0].substring(index, index+offset);                               //length 3
                                        index=index+offset;
                                        offset=8;
                                        device_specific. m_sMax_Reconciliation_Amount= dspec_seg1_elements[0].substring(index, index+offset);               //length 8
                                        index=index+offset;
                                        offset=4;
                                        device_specific. m_sMax_Transactions_Processed= dspec_seg1_elements[0].substring(index, index+offset);              //length 4
                                        index=index+offset;
                                        offset=1;
                                        device_specific. m_sQR_Code_Print_Indicator= dspec_seg1_elements[0].substring(index, index+offset);                 //length 1
                                    }

                                }
                               /* try {
                                    Save(applicationContext);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/
                                break;
                            case DT_AID_List:
                                for (int i = 1; i < segments.length; i++) {
                                    /////////////////
                                    //Segment1
                                    ////////////////
                                    temp = segments[i].split("\u001C");
                                    String aidl_seg1_elements[] = new String[temp.length];
                                    Arrays.fill(aidl_seg1_elements, "");
                                    aid_list.AID = new String[temp.length];
                                    System.arraycopy(temp, 0, aidl_seg1_elements, 0, temp.length);
                                    if (aidl_seg1_elements[0].length() > 3) {
                                        String aid1 = aidl_seg1_elements[0].substring(3, aidl_seg1_elements[0].length());  //check max length 3
                                        aid_list.AID[0]=aid1;           //AID 1
                                    }
                                    for(int j=1;j<temp.length;j++)   //AID 2-10 or more if exist
                                    {
                                        aid_list.AID[j] = aidl_seg1_elements[j];
                                    }

                                }
                                //save(aid_list);
                                break;
                            case DT_AID_Data:
                                for (int i = 1; i < segments.length; i++) {
                                    /////////////////
                                    //Segment1
                                    ////////////////
                                    String aidD_seg1_elements[] = new String[14];
                                    Arrays.fill(aidD_seg1_elements, "");

                                    temp = segments[i].split("\u001C");
                                    System.arraycopy(temp, 0, aidD_seg1_elements, 0, temp.length);
                                    if (aidD_seg1_elements[0].length() > 3) {
                                        String aid = aidD_seg1_elements[0].substring(3, aidD_seg1_elements[0].length());  //check max length 3
                                        aid_data.AID=aid;
                                    }

                                    aid_data.AID_Label=aidD_seg1_elements[1];
                                    aid_data.Terminal_AID_version_numbers=aidD_seg1_elements[2];
                                    aid_data.Exact_only_selection=aidD_seg1_elements[3];
                                    aid_data.Skip_EMV_processing=aidD_seg1_elements[4];
                                    aid_data.Default_TDOL=aidD_seg1_elements[5];
                                    aid_data.Default_DDOL=aidD_seg1_elements[6];
                                    aid_data.EMV_additional_tags=aidD_seg1_elements[7];
                                    aid_data.Denial_action_code=aidD_seg1_elements[8];
                                    aid_data.Online_action_code=aidD_seg1_elements[9];
                                    aid_data.Default_action_code=aidD_seg1_elements[10];
                                    aid_data.Threshold_Value_for_Biased_Random_Selection=aidD_seg1_elements[11];
                                    aid_data.Target_Percentage=aidD_seg1_elements[12];
                                    aid_data.Maximum_Target_Percentage_for_Biased_Random_Selection=aidD_seg1_elements[13];

                                }
                                //save(AID_data);
                                break;
                            case DT_Revoked_Certificates:
                                for (int i = 1; i < segments.length; i++) {
                                    /////////////////
                                    //Segment1
                                    ////////////////
                                    String rev_seg1_elements[] = new String[3];
                                    Arrays.fill(rev_seg1_elements, "");

                                    temp = segments[i].split("\u001C");
                                    System.arraycopy(temp, 0, rev_seg1_elements, 0, temp.length);
                                    if (rev_seg1_elements[0].length() > 3) {
                                        String rid_revoked_cert = rev_seg1_elements[0].substring(3, rev_seg1_elements[0].length());  //check max length 3
                                        revoked_certificates.RID_Revoked_Certificate=rid_revoked_cert;
                                    }

                                    revoked_certificates.IDX=rev_seg1_elements[1];
                                    revoked_certificates.Cert_serial_number=rev_seg1_elements[2];


                                }

                                break;
                        }

//

                    }

        private int checkcardscheme(String substring)
        {
            int i=99;
            switch (substring)
            {
                case mada:
                    i=0;
                    break;
                case Visa_Credit:
                    i=1;
                    break;
                case Visa_Debit:
                    i=2;
                    break;
                case Master_Card:
                    i=3;
                    break;
                case Maestro:
                    i=4;
                    break;
                case AMEX:
                    i=5;
                    break;
                case UnionPay:
                    i=6;
                    break;
                case JCB:
                    i=7;
                    break;
                case Discover:
                    i=8;
                    break;

            }
            return i;
        }


        public void Load_Sama_param()
                        {
                            // load all elements on SAMA_TMS class
                        }
        public void getcardschema(int ID)
                    {

                    }

        public void Check_HDR(String buffer)
        {
            String header="";
            if (buffer.startsWith("306"))
            {   NUM_REC=buffer.substring(3,5);
                NUM_REC_DLN=buffer.substring(5,7);
                header="306";
                Log.d("TMS HDR", header);
                Log.d("NUMBER OF RECORDS", NUM_REC);
                Log.d("NUMBER OF RECORDS TO BE DOWNLOADED", NUM_REC_DLN);

            }

        }
        public void Save(Context context) throws IOException {
            File dir = new File("mostafa");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File("mostafa", "image.png");
            FileOutputStream fos =context.openFileOutput("sama TMS files", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();

        }
        public void load(Context context) throws IOException, ClassNotFoundException {
            FileInputStream fis = context.openFileInput("fileName");
            ObjectInputStream is = new ObjectInputStream(fis);
            SAMA_TMS loadedclass = (SAMA_TMS) is.readObject();
            is.close();
            fis.close();
        }

        public static int Get_card_scheme_BY_AID(String AID)
        {
            // todo get Card scheme by ID form database
            return 0;
        }
        public static int Get_card_scheme_BY_PAN(String PAN)
        {
            //todo get card scheme by pan from database
            return 0;
        }

    }

/*class Connection_Parameters {
    /////////////////
//Segment1_of_Dialup_Connections
////////////////


    Primary


    public String Priority;
    public String Communication_Type; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
    public String Phone_Number;
    public String Dial_Attempts_to_Phone;
    public String Connect__Time_for_Phone;
    public String Baud_Rate_Default2;
    public String Parity_Bit;
    public String Data_Bit;
    public String Stop_Bit;
    public String Response_timeout;
    /////////////////
//Segment_1_of_TCP/IP_Connections
/////////////////
    public String Priority_TCP;
    public String Communication_Type_TCP; //01=Dialup_;_02=TCP/IP_;03=GPRS;04=wifi;05=GSM
    public String Network_IP_Address;
    public String Network_TCP_Support;
    public String Count_Access_Retries;
    public String Response_Time_Out;
    public String SSL_Certificate_File;
    /////////////////
//Segment_1_of_GPRS_Connections
/////////////////
    public String GPRS_dial_Number;
    public String GPRS_access_point_name;
    public String Connect__Time_for_GPRS_phone;
    public String Network_IP_address;
    public String Network_TCP_port;
    public String Dial_attempts_to_network;
    public String Response_time_out;

/////////////////
//Segment_1_of_wifi_Connections
/////////////////
//like_GRPS
/////////////////
//Segment_1_of_GSm_sonnections
/////////////////
//like_dialup
}*/            // removed and replaced with primary and secondary

/**********************************************************************/




