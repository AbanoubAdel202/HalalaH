package com.example.halalah;
import android.util.Log;

import com.example.halalah.TMS.Card_Scheme;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.iso8583.ISO8583;
import com.example.halalah.packet.PackPurchase;
import com.example.halalah.packet.PackUtils;
import com.example.halalah.util.ExtraUtil;

import java.lang.reflect.Array;

/** Header POSTransaction
	\Class Name: POSTransaction
	\Param  :
	\Return :
	\Pre    :
	\Post   :
	\Author	: Moamen Ahmed
	\DT		: 4/28/2020
	\Des    : Container for Transaction data fields
*/
public class POSTransaction {
    private static final String TAG = Utils.TAGPUBLIC + POSTransaction.class.getSimpleName();


    // Card Type
    public int m_iCardType;
    public enum CardType{
        MAG  ,
        ICC  ,
        CTLS ,
        MANUAL,
        FALLBACK
    }

    // Transaction Type (Authorization ,Financial ,Reversal Reconciliation ,Admin, File Action (TMS) , Network)
    public int m_iTransactionType;
    public enum TranscationType{
        PURCHASE               ,
        PURCHASE_WITH_NAQD     ,
        REFUND                 ,
        AUTHORISATION          ,
        AUTHORISATION_EXTENSION,
        PURCHASE_ADVICE        ,
        AUTHORISATION_VOID	   ,
        CASH_ADVANCE           ,
        REVERSAL               ,
        SADAD_BILL             ,
        RECONCILIATION         ,
        TMS_FILE_DOWNLOAD      ,
        TERMINAL_REGISTRATION  ,
        ADMIN                  ,
        AUTHORISATION_ADVICE
    }

    // Mostafa hussiny 18/5/2020 modified for all CVM list
    // CVM Type
    enum CVM{
        ONLINE_PIN,
        OFFLINE_PIN,
        SIGNATURE,
        NO_CVM,
        CDCVM
    }

    // Constractur
    public POSTransaction()
    {
                m_RequestISOMsg=new ISO8583();          /* ISO8583 Request message to be sent constractor*/
                m_ResponseISOMsg=new ISO8583();         /* ISO8583 Response message constractor*/
    }


    // Transaction Data Elements as per SP Terminal interface specification document 6.0.9
    public String	m_sPAN;								/* DE2   – Primary account Number -Size 19  –Reconcilation value would be Terminal ID as is present in DE 41*/
    public String	m_sProcessCode;	    				/* DE3   – Process Code  –Size 6 */
    public String	m_sTrxAmount;						/* DE4   – Transaction Amount – Size 12*/
    public String	m_sTrxDateTime;					/* DE7   – Transaction Date Time  – size 10 (MMDDhhmmss) */
    public String	m_sSTAN;							/* DE11  – Transaction STAN  – Size 6*/
    public String	m_sLocalTrxDateTime;				/* DE12  – Local Transaction Date Time  –n 12 (YYMMDDhhmmss)*/
    public String	m_sCardExpDate;						/* DE14  – Card Expiration Date  –n 4 (YYMM)*/
    public String	m_sPOSEntryMode;					/* DE22  – Point of Service Data Code – an 12*/
    public String	m_sCardSeqNum;						/* DE 23 – Card Sequence Number -Size n 3*/
    public String	m_sFunctionCode;					/* DE 24 – Function Code -Size n 3*/
    public String	m_sMsgReasonCode;					/* DE 25 – Message Reason Code -Size 4*/
    public String	m_sCardAcceptorBusinessCode;		/* DE 26 – Card Acceptor Business Code -Size 4*/
    public String	m_sReconDateTime;					/* DE 28 – Reconciliation Date - Size n 6 (YYMMDD)*/
    public String	m_sOrigAmount;						/* DE 30 – Original Amount - Size n 12*/
    public String	m_sAquirerInsIDCode; 				/* DE 32 – Acquirer Institution Identification Code -Size n..11 , Recieved after terminal registration*/
    public String	m_sTrack2;			    			/* DE 35 – Track-2 Data -Size z..37*/
    public String	m_sRRNumber;						/* DE 37 – Retrieval Reference Number -Size anp 12*/
    public String	m_sApprovalCode;					/* DE 38 – Approval Code -Size anp 6 , Host reveived value*/
    public String	m_sActionCode;						/* DE 39 – Action Code - Size n 3 , Host reveived value*/
    public String	m_sTerminalID;						/* DE 41 – Card Acceptor Terminal Identification -Size ans 16 */
    public String	m_sMerchantID;						/* DE 42 – Card Acceptor Identification Code (Merchant ID) - Size ans 15*/
    public String   m_sAdditionalResponseData;			/* DE 44 - Additional Response Data - Size ans..99 , ag based data elements originating from either the Issuer,mada Switch */
    public String   m_sCardSchemeSponsorID;				/* DE 47 – Private - Card Scheme Sponsor ID - Size ans..999 , Used to sent Bank ID(len = 4) and Card Scheme ID(len 2) and other details for Bill Payments*/
    public String   m_sHostData_DE48;					/* DE 48 – Private – Additional Data-Size ans..999 ,Used during terminal registation and campaign footer messages */
    public String   m_sCurrencyCode;				    /* DE 49 – Currency Code, Transaction - Size n 3 , fixed value "682" */
    public String   m_sReconCurrencyCode;				/* DE 50 – DE 50 – Currency Code, Reconciliation - Size n 3 , "682" */
    public String	m_sTrxPIN;							/* DE 52 – Personal Identification Number (PIN) - Size 8 BYTE */
    public String   m_sTrxSecurityControl;	            /* DE 53 – Security Related Control Information - Size b..48 , KSN + KSN Descriptor (609)*/
    public String	m_sAdditionalAmount;				/* DE 54 – Additional Amounts Used currently for CashBack  , formated as "0040+CurrencyCode (682)D+ CashBack amount*/
    public String   m_sICCRelatedTags;					/* DE 55 – ICC Related Data (Tags for both ICC & CTLS Trx) Request/Response - Size b..255 */
    public String   m_sOriginalTrxData;					/* DE 56 – Original Data Elements - Size ANP..58*/
    public String   m_sTransportData;                   /* DE 59 – Transport Data -Size ans..999*/
    public String   m_sTerminalStatus ;                  /* DE 62 – Private – Terminal Status ans..999*/
    public String   m_sTrxMACBlock;                     /* DE 64/128 – Message Authentication Code (MAC) - Size 8 bytes only the left 4 bytes will be used for the MAC and the right 4 bytes will bepadded with F’s.*/
    public String   m_sDataRecord72;	                /* DE 72 – Data Record Used on TMS and Admin messages -  Size ans11..999*/
    public String   m_sReconciliationTotals;            /* DE 124 – Private – mada POS Terminal Reconciliation Totals - Size  ans..999*/



    /*Cash Back Amount , Used for DE54 Additional data elements*/
    /*************************/
    public String  m_sCashBackAmount;                  /* Used to fill DE 54 cashBack amount Value , Size 12*/

    /*Related Orginal Trx Data to be used on DE56*/
    /*********************************************/
    public String	m_sOrigMTI;	 					    /* Original MTI , Size 4*/
    public String	m_sOrigSTAN;				        /* Original DE 11 STAN , Size 11*/
    public String	m_sOrigTrxDateTime;		            /* Original DE 7  Transaction Date Time , Size 10*/
    public String	m_sOrigLocalTrxDateTime;	        /* Original DE 12 Local Transaction Date Time ,Size 12*/
    public String	m_sOrigAquirerInsIDCode;            /* Original DE 32 Aquirer Insititution ID Code , Size 11 Default "00" */
    public String	m_sOrigFWAquirerInsIDCode;          /* Original DE 33 Forwarding Aquirer Insititution ID Code , Size 11 Default "00" */
    public String	m_sOrigRRNumber;			        /* Original DE 37 Retrival Refrence Number , Size 12*/
    public String	m_sOrigLocalTrxDate;			    /* Original Local Transaction Date , Size 6 Default value of ‘000000’ if unavailable.*/
    /*********************************************/

   public Card_Scheme card_scheme = new Card_Scheme();
       public String m_sAID;                             // AID for the card ICC

//    Terminal Status Data (REQUEST), to be used on DE62
//    *********************************************
//    public char 	m_cTermDialIndic;					 Tag 01 , Terminal Dial Indicator , Size 1
//    public char 	m_cTermPrinterStatus;				 Tag 02 , Terminal Printer Status , Size 1
//    public String   m_sTerminalIdleTime;		         Tag 03 , Terminal Idle Time ,Size 6 hhmmss
//    public char	    m_cMagReaderStatus;			         Tag 04 , Terminal Magnetic reader status , Size 1
//    public char	    m_cChipReaderStatus;		         Tag 05 , Terminal ICC Reader status ,Size 1
//    public String   m_sGPSLocatoin;                      Tag 07 , GPS Location Coordinates ,Size 15
//    public char     m_cCTLSReaderStatus;                 Tag 09 , Terminal CTLS Reader status ,Size 1
//    public String   m_sStartConnectionTime;              Tag 10 , Connection Start Time , Size n 9 HHMMSSsss
//    public String   m_sEndConnectionTime;                Tag 11 , Connection End Time , Size n 9 HHMMSSsss
//    public String   m_sRequestSentTime;                  Tag 12 , Request Sent Time , Size n 9 HHMMSSsss
//    public String   m_sResponseReceivedTime;             Tag 13 , Response Received Time , Size n 9 HHMMSSsss
//     Tag 14 , RRN member variable could be used, Size n 12 , Defualt value would be Zeros
//    public String   m_sSpecReleaseVersion;               Tag 15 , mada EFTPOS specification release version , Size 6 , "6.0.09 -> 060009"
//    //Connection Details                                 Tag 16 Connectino Details as Connection Priority ,Network Service Provider(NSP) ,Provider  and Connection Method
//    public String   m_sConnectionPriority;               Tag 16 ,POS 1 Connection Priority , Len 2
//    public String   m_sNetworkServiceProvide;            Tag 16 ,POS 2 Network Service Provider(NSP) ,LEN 2
//    public String   m_sNetworkProvide;                   Tag 16 ,POS 3 Provider ,Len 2
//    public String   m_sConnectionMethod;                 Tag 16 ,POS 4 Method ,Len 2
//    *********************************************




  //  /*Terminal Status Data (RESPONSE), to be used on DE62*/
  //  /***********************************************/
  //  public char     m_cForceTMSDownloadFlag;           /* Tag 06 ,Terminal Online Flag(TMS Download) Host ,Size 1*/
  //  public char     m_cForceReconciliationFlag;        /* Tag 08 , Force Reconciliation Flag Host ,Size 1*/
  //  /***********************************************/




    /*Data record size used to indicate DE72 size*/
    /********************************************/
    public int      m_iDataRecord72Size;
    /********************************************/



    /* Transaction variables*/
    /********************************************/
    public ISO8583          m_RequestISOMsg;          /* ISO8583 Request message to be sent*/
    public ISO8583          m_ResponseISOMsg;         /* ISO8583 Response message */

    public CardType            m_enmTrxCardType;        /* Transaction used card ICC , MAG or CTLS */
    public TranscationType     m_enmTrxType;            /* Transaction Type , Purchase , Refund ...*/
    public CVM                 m_enmTrxCVM;             /* Transaction CVM */
    public int                 m_TransactionStaus;      /* Approved or Rejected */

    /********************************************/






    // Member Fucntions
    /************************************************************************************************************/

    // OPeration & Data
    public int    Reset()
    {
        new POSTransaction();
        return 0;
    }

    public String ComposeReconciliationTotals(int iNumberOfCardScheme , CardSchemeTotals[] TotalsArray )
    {
        //getting Purchase total


        return "0";
    }
    public String ComposeICCTags(CardType enmCard)
    {
        return "0";
    }
    public String ComposeTerminalRegistrationData()
    {


        return "0";
    }
    public int    ParseICCTags(String ICCHostTags)
    {
        return 0;
    }

    /**
     * \Function Name: ComposeMACBlockData
     * \Param  : TranscationType
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 5/19/2020
     * \Des    : for building MAC Generation and added to MAC DATA ELEMENT
     */
    public String ComposeMACBlockData(TranscationType TrxType)
    {
        String sMAC="";

        switch(TrxType){
            case AUTHORISATION:
            case AUTHORISATION_EXTENSION:


                //0.Messa   ge Type Identifier
                sMAC=PosApplication.MTI_Authorisation_Request;
                //1. Primary bitmap   todo get bitmap
                //2.Primary Account Number (PAN)
                sMAC.concat(m_sPAN);
                //3.Processing Code
                sMAC.concat(m_sProcessCode);
                //4.Amount, Transaction
                sMAC.concat(m_sTrxAmount);
                //11.System Trace Audit Number
                sMAC.concat(m_sSTAN);
                //12.Date and Time, Local Transaction
                sMAC.concat(m_sLocalTrxDateTime);
                //47.National Data
                sMAC.concat(m_sCardSchemeSponsorID);
                //53.Security Related Control Information
                sMAC.concat(m_sTrxSecurityControl);
                //55.EMV Data
                sMAC.concat(m_sICCRelatedTags);
                break;
            case AUTHORISATION_ADVICE:
                //0.Messa   ge Type Identifier
                sMAC=PosApplication.MTI_Authorisation_Advice;
                //1. Primary bitmap   todo get bitmap
                //2.Primary Account Number (PAN)
                sMAC.concat(m_sPAN);
                //3.Processing Code
                sMAC.concat(m_sProcessCode);
                //4.Amount, Transaction
                sMAC.concat(m_sTrxAmount);
                //11.System Trace Audit Number
                sMAC.concat(m_sSTAN);
                //12.Date and Time, Local Transaction
                sMAC.concat(m_sLocalTrxDateTime);
                //47.National Data
                sMAC.concat(m_sCardSchemeSponsorID);
                //53.Security Related Control Information
                sMAC.concat(m_sTrxSecurityControl);
                //55.EMV Data
                sMAC.concat(m_sICCRelatedTags);
                break;
            case REFUND:
            case CASH_ADVANCE:
            case PURCHASE_WITH_NAQD:
            case PURCHASE:
                //0.Messa   ge Type Identifier
                sMAC=PosApplication.MTI_Financial_Request;
                //1. Primary bitmap
                sMAC.concat(new String(m_RequestISOMsg.Getbitmap()));
                //2.Primary Account Number (PAN)
                sMAC.concat(m_sPAN);
                //3.Processing Code
                sMAC.concat(m_sProcessCode);
                //4.Amount, Transaction
                sMAC.concat(m_sTrxAmount);
                //11.System Trace Audit Number
                sMAC.concat(m_sSTAN);
                //12.Date and Time, Local Transaction
                sMAC.concat(m_sLocalTrxDateTime);
                //47.National Data
                sMAC.concat(m_sCardSchemeSponsorID);
                //53.Security Related Control Information
                sMAC.concat(m_sTrxSecurityControl);
                //55.EMV Data
                sMAC.concat(m_sICCRelatedTags);
                break;
            case PURCHASE_ADVICE:
                //0.Messa   ge Type Identifier
                sMAC=PosApplication.MTI_Financial_Transaction_Advice;
                //1. Primary bitmap   todo get bitmap
                //2.Primary Account Number (PAN)
                sMAC.concat(m_sPAN);
                //3.Processing Code
                sMAC.concat(m_sProcessCode);
                //4.Amount, Transaction
                sMAC.concat(m_sTrxAmount);
                //11.System Trace Audit Number
                sMAC.concat(m_sSTAN);
                //12.Date and Time, Local Transaction
                sMAC.concat(m_sLocalTrxDateTime);
                //47.National Data
                sMAC.concat(m_sCardSchemeSponsorID);
                //53.Security Related Control Information
                sMAC.concat(m_sTrxSecurityControl);
                //55.EMV Data
                sMAC.concat(m_sICCRelatedTags);
                break;
            case REVERSAL:
                //0.Messa   ge Type Identifier
                sMAC=PosApplication.MTI_Reversal_Advice;
                //1. Primary bitmap   todo get bitmap
                //2.Primary Account Number (PAN)
                sMAC.concat(m_sPAN);
                //3.Processing Code
                sMAC.concat(m_sProcessCode);
                //4.Amount, Transaction
                sMAC.concat(m_sTrxAmount);
                //11.System Trace Audit Number
                sMAC.concat(m_sSTAN);
                //12.Date and Time, Local Transaction
                sMAC.concat(m_sLocalTrxDateTime);
                //47.National Data
                sMAC.concat(m_sCardSchemeSponsorID);
                //53.Security Related Control Information
                sMAC.concat(m_sTrxSecurityControl);
                //55.EMV Data
                sMAC.concat(m_sICCRelatedTags);
                break;

            case TMS_FILE_DOWNLOAD:
                break;
            case RECONCILIATION:
        }
        //0.Messa   ge Type Identifier
        //2.Primary Account Number (PAN)
        //3.Processing Code
        //4.Amount, Transaction
        //11.System Trace Audit Number
        //12.Date and Time, Local Transaction
        //39.Action Code
        //47.National Data
        //53.Security Related Control Information
        //55.EMV Data
        //72.Data Record
        //124.Private - (POS Terminal Reconciliation)
        byte[] bMac = sMAC.getBytes();
        if (bMac.length%2==1) {
            sMAC.concat("F");
        }

        //todo send bmac or smac to security module for mac generation

        //m_sTrxMACBlock= securityfunction for mac
        return "0";
    }

    // Message
    public int  BuildISO8583Message(TranscationType TrxType){
        switch(TrxType)
        {
            case PURCHASE:
            case PURCHASE_WITH_NAQD :
            case REFUND   :
            case CASH_ADVANCE:
            {
                ComposeFinancialMessage( TrxType);
            }
            break;
            case AUTHORISATION:
            case AUTHORISATION_VOID:
            case AUTHORISATION_EXTENSION:
            {
                ComposeAuthoriszationMessage(TrxType);
            }
            case AUTHORISATION_ADVICE:
            {
                ComposeAuthorisationAdviseMessage(TrxType);
            }
            break;
            case PURCHASE_ADVICE:
            {
                ComposePurchaseAdviseMessage(TrxType);
            }
            break;
            case REVERSAL:
            {
                ComposeReversalMessage();
            }
            break;
            case RECONCILIATION :
            {
                CompoaseReconciliationMessage();
            }
            break;
            case TMS_FILE_DOWNLOAD:
            {
                ComposeFileDownloadMessage();
            }
            break;
            case TERMINAL_REGISTRATION:
            {
                ComposeNetworkMessage();
            }
            break;
            case ADMIN:
            {
                ComposeAdministrativeMessage();
            }
            break;
            case SADAD_BILL:
            {
                // Todo
            }
            break;
            default:
                //Error not matching
        } // End Switch
       return 0;
    }

    /**
     * \Function Name: ComposeRepeatMessage
     * \Param  : TranscationType
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 5/17/2020
     * \Des    : for building repeat messages and ready to be coverted to str for sending (MTI+DEs)
     */
    public int    ComposeRepeatMessage(TranscationType TrxType)
    {

        switch(TrxType)
        {
            case PURCHASE_ADVICE:
                m_RequestISOMsg.SetMTI(PosApplication.MTI_Financial_Transaction_Advice_repeat);
                break;
            case AUTHORISATION_ADVICE:
                m_RequestISOMsg.SetMTI(PosApplication.MTI_Authorisation_Advice_Repeat);
                break;
            case  REVERSAL:
                m_RequestISOMsg.SetMTI(PosApplication.MTI_Reversal_Advice_Repeat);
                break;
            case RECONCILIATION:
                m_RequestISOMsg.SetMTI(PosApplication.MTI_Terminal_Reconciliation_Advice_Repeat);
                break;
            case TMS_FILE_DOWNLOAD:
                    m_RequestISOMsg.SetMTI(PosApplication.MTI_File_Action_Request_Repeat);

        }


        return 0;
    }


    /**
     * \Function Name: ComposeFinancialMessage
     * \Param  : TranscationType
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 5/17/2020
     * \Des    : for building financial messages and ready to be coverted to str for sending (MTI+DEs)
     */

    public int    ComposeFinancialMessage(TranscationType TrxType)
    {


        
        m_RequestISOMsg.ClearFields();
/*
        byte[] bfield2 = null;
        String sfield3 = null;
        String sfield4 = null;
        String sfield7 = null;
        String sfield11 = null;
        String sfield12 = null;
        String sfield14 = null;  //c2
        String sfield22 = null;
        String sfield23 = null; //c3
        String sfield24 = null;
        String sfield25 = null; //c18
        String sfield26 = null;
        String sfield32 = null;
        byte[] bfield35 = null; //c4
        String sfield37 = null;
        String sfield41 = null;
        String sfield42 = null;
        String sfield47 = null;
        String sfield49 = null;
        byte[] bfield52 = null; //c6
        String sfield53 = null;
        String sfield54 = null; //c8
        byte[] bfield55 = null; //c7
        String sfield56 = null; //c16
        String sfield59 = null; // c_
        String sfield62 = null;
        byte[] bfield64 = null;

        cardType = m_enmTrxCardType;
        bfield52 = m_sTrxPIN.getBytes();
        */



        //1. Set Financial MTI
        m_RequestISOMsg.SetMTI(PosApplication.MTI_Financial_Request);



        //2. Set PAN


            m_RequestISOMsg.SetDataElement(2, m_sPAN.getBytes(), m_sPAN.length());
            Log.i(TAG, "DE 2 [m_sPAN]= " + m_sPAN+"Length ="+m_sPAN.length());



        //3.Set Processing Code
        m_RequestISOMsg.SetDataElement(3, m_sProcessCode.getBytes(), m_sProcessCode.length());
        Log.i(TAG, "DE 3 [m_sProcessCode]= " + m_sProcessCode+"Length ="+m_sProcessCode.length());


        //4. Amount
        m_RequestISOMsg.SetDataElement(4, m_sTrxAmount.getBytes(), m_sTrxAmount.length());
        Log.i(TAG, "DE 4 [m_sTrxAmount]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        //7.local Transaction Date & time
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, "DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxDateTime.length());



        //11.STAN
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, "DE 11 [m_sSTAN]= " + m_sTrxAmount+"Length ="+m_sSTAN.length());

        //12. Local Transaction time and date
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, "DE 12 [m_sLocalTrxDateTime]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        //14. Card Expiry Date
        if (m_enmTrxCardType == CardType.MANUAL) {
            if (m_sCardExpDate != null) {
                m_RequestISOMsg.SetDataElement(14, m_sCardExpDate.getBytes(), m_sCardExpDate.length());
                Log.i(TAG, "DE 14 [m_sCardExpDate]= " + m_sCardExpDate+"Length ="+m_sCardExpDate.length());
            }
        }

        //22.Pos Entry Mode
        if (m_enmTrxCardType == CardType.MAG) {

        } else if (m_enmTrxCardType == CardType.ICC) {

        } else if (m_enmTrxCardType == CardType.CTLS) {

        }
        m_RequestISOMsg.SetDataElement(22, m_sPOSEntryMode.getBytes(), m_sPOSEntryMode.length());
        Log.i(TAG, "DE 22 [m_sPOSEntryMode]= " + m_sPOSEntryMode+"Length ="+m_sPOSEntryMode.length());

        //23.Card Sequence Number
        if (m_enmTrxCardType == CardType.ICC) {
            m_RequestISOMsg.SetDataElement(23, m_sCardSeqNum.getBytes(), m_sCardSeqNum.length());
            Log.i(TAG, "DE 23 [m_sCardSeqNum]= " + m_sCardSeqNum+"Length ="+m_sCardSeqNum.length());
        }

        //24. Function Code
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, "DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        //25. Message reason Code
        if(m_enmTrxCardType==CardType.FALLBACK) {
            m_RequestISOMsg.SetDataElement(25, m_sMsgReasonCode.getBytes(), m_sMsgReasonCode.length());
            Log.i(TAG, "DE 25 [m_sMsgReasonCode]= " + m_sMsgReasonCode + "Length =" + m_sMsgReasonCode.length());
        }

        //26. Card Acceptor Bussiness Code
        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, "DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());

        //32. Acquirer institution ID
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, "DE 32 [m_sAquirerInsIDCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());

        //35.Track2 Data
        if ((m_enmTrxCardType == CardType.MAG) || (m_enmTrxCardType == CardType.ICC)) {

            m_RequestISOMsg.SetDataElement(35, m_sTrack2.getBytes(), m_sTrack2.length());
            Log.i(TAG, "DE 35 [m_sTrack2]= " + m_sTrack2+"Length ="+m_sTrack2.length());
        }

        //37. RRN
        m_RequestISOMsg.SetDataElement(37, m_sRRNumber.getBytes(), m_sRRNumber.length());
        Log.i(TAG, "DE 37 [m_sRRNumber]= " + m_sRRNumber+"Length ="+m_sRRNumber.length());


        //41.Terminal ID
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, "DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        //42.Merchant ID
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, "DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        //47.CardScheme Sponsor ID
        m_RequestISOMsg.SetDataElement(47, m_sCardSchemeSponsorID.getBytes(), m_sCardSchemeSponsorID.length());
        Log.i(TAG, "DE 47 [m_sCardSchemeSponsorID]= " + m_sCardSchemeSponsorID+"Length ="+m_sCardSchemeSponsorID.length());

        //49.Currency Code
        m_RequestISOMsg.SetDataElement(49, m_sCurrencyCode.getBytes(), m_sCurrencyCode.length());
        Log.i(TAG, "DE 49 [m_sCurrencyCode]= " + m_sCurrencyCode+"Length ="+m_sCurrencyCode.length());


        //52.PIN
        m_RequestISOMsg.SetDataElement(52, m_sTrxPIN.getBytes(), m_sTrxPIN.length());
        Log.i(TAG, "DE 52 [m_sTrxPIN]= " + m_sTrxPIN+"Length ="+m_sTrxPIN.length());


        //53. Transaction Security control
        m_RequestISOMsg.SetDataElement(53, m_sTrxSecurityControl.getBytes(), m_sTrxSecurityControl.length());
        Log.i(TAG, "DE 53 [m_sTrxSecurityControl]= " + m_sTrxSecurityControl+"Length ="+m_sTrxSecurityControl.length());

        //54. Additional amounts
        m_RequestISOMsg.SetDataElement(54, m_sAdditionalAmount.getBytes(), m_sAdditionalAmount.length());
        Log.i(TAG, "DE 54 [m_sAdditionalAmount]= " + m_sAdditionalAmount+"Length ="+m_sAdditionalAmount.length());

        //55. ICC related Data
        if (m_enmTrxCardType == CardType.ICC)
        m_RequestISOMsg.SetDataElement(55, m_sICCRelatedTags.getBytes(), m_sICCRelatedTags.length());
        Log.i(TAG, "DE 55 [m_sICCRelatedTags]= " + m_sICCRelatedTags+"Length ="+m_sICCRelatedTags.length());

        //56.original Transaction data
        if(m_enmTrxType==TrxType.REFUND)     // we can check here if refund only
        {
        m_RequestISOMsg.SetDataElement(56, m_sOriginalTrxData.getBytes(), m_sOriginalTrxData.length());
        Log.i(TAG, "DE 56 [m_sOriginalTrxData]= " + m_sOriginalTrxData+"Length ="+m_sOriginalTrxData.length());
        }

        //59.Transaport Data
        m_RequestISOMsg.SetDataElement(59, m_sTransportData.getBytes(), m_sTransportData.length());
        Log.i(TAG, "DE 59 [m_sTransportData]= " + m_sTransportData+"Length ="+m_sTransportData.length());

        //62.Terminal Status
        ComposeTerminalStatusData();
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, "DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());

        //64.Transaction Block
        m_RequestISOMsg.SetDataElement(64, m_sTrxMACBlock.getBytes(), m_sTrxMACBlock.length());
        Log.i(TAG, "DE 64 [m_sTrxMACBlock]= " + m_sTrxMACBlock+"Length ="+m_sTrxMACBlock.length());


        return 0;
    }
    /**
     \Function Name: ComposeAuthoriszationMessage
     \Param  : Transaction type
     \Return : int
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 17/05/2020
     \Des    : Compose Authorization for building auth messages and ready to be coverted to str for sending (MTI+DEs)
     */
    public int    ComposeAuthoriszationMessage(TranscationType TrxType)
    {

        //Clear  Message
        m_RequestISOMsg.ClearFields();


        //1. Set Authorization MTI
        m_RequestISOMsg.SetMTI(PosApplication.MTI_Authorisation_Request);



        //2. Set PAN
        if (m_enmTrxCardType !=CardType.MANUAL ) {

            m_RequestISOMsg.SetDataElement(2, m_sPAN.getBytes(), m_sPAN.length());
            Log.i(TAG, "DE 2 [m_sPAN]= " + m_sPAN+"Length ="+m_sPAN.length());

        }

        //3.Set Processing Code
        m_RequestISOMsg.SetDataElement(3, m_sProcessCode.getBytes(), m_sProcessCode.length());
        Log.i(TAG, "DE 3 [m_sProcessCode]= " + m_sProcessCode+"Length ="+m_sProcessCode.length());


        //4. Amount
        m_RequestISOMsg.SetDataElement(4, m_sTrxAmount.getBytes(), m_sTrxAmount.length());
        Log.i(TAG, "DE 4 [m_sTrxAmount]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        //7.loacl Transaction Date & time
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, "DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxDateTime.length());




        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, "DE 11 [m_sSTAN]= " + m_sTrxAmount+"Length ="+m_sSTAN.length());


        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, "DE 12 [m_sLocalTrxDateTime]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());



        if (m_enmTrxCardType == CardType.MANUAL) {
            if (m_sCardExpDate != null) {
                m_RequestISOMsg.SetDataElement(14, m_sCardExpDate.getBytes(), m_sCardExpDate.length());
                Log.i(TAG, "DE 14 [m_sCardExpDate]= " + m_sCardExpDate+"Length ="+m_sCardExpDate.length());
            }
        }

        if (m_enmTrxCardType == CardType.MAG) {

        } else if (m_enmTrxCardType == CardType.ICC) {

        } else if (m_enmTrxCardType == CardType.CTLS) {

        }
        m_RequestISOMsg.SetDataElement(22, m_sPOSEntryMode.getBytes(), m_sPOSEntryMode.length());
        Log.i(TAG, "DE 22 [m_sPOSEntryMode]= " + m_sPOSEntryMode+"Length ="+m_sPOSEntryMode.length());

        if (m_enmTrxCardType == CardType.ICC) {
            m_RequestISOMsg.SetDataElement(23, m_sCardSeqNum.getBytes(), m_sCardSeqNum.length());
            Log.i(TAG, "DE 23 [m_sCardSeqNum]= " + m_sCardSeqNum+"Length ="+m_sCardSeqNum.length());
        }


        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, "DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());



        m_RequestISOMsg.SetDataElement(25, m_sMsgReasonCode.getBytes(), m_sMsgReasonCode.length());
        Log.i(TAG, "DE 25 [m_sMsgReasonCode]= " + m_sMsgReasonCode+"Length ="+m_sMsgReasonCode.length());

        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, "DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());

        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, "DE 32 [m_sAquirerInsIDCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());


        if ((m_enmTrxCardType == CardType.MAG) || (m_enmTrxCardType == CardType.ICC)) {

            m_RequestISOMsg.SetDataElement(35, m_sTrack2.getBytes(), m_sTrack2.length());
            Log.i(TAG, "DE 35 [m_sTrack2]= " + m_sTrack2+"Length ="+m_sTrack2.length());
        }


        m_RequestISOMsg.SetDataElement(37, m_sRRNumber.getBytes(), m_sRRNumber.length());
        Log.i(TAG, "DE 37 [m_sRRNumber]= " + m_sRRNumber+"Length ="+m_sRRNumber.length());



        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, "DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());


        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, "DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());


        m_RequestISOMsg.SetDataElement(47, m_sCardSchemeSponsorID.getBytes(), m_sCardSchemeSponsorID.length());
        Log.i(TAG, "DE 47 [m_sCardSchemeSponsorID]= " + m_sCardSchemeSponsorID+"Length ="+m_sCardSchemeSponsorID.length());


        m_RequestISOMsg.SetDataElement(49, m_sCurrencyCode.getBytes(), m_sCurrencyCode.length());
        Log.i(TAG, "DE 49 [m_sCurrencyCode]= " + m_sCurrencyCode+"Length ="+m_sCurrencyCode.length());


        if(m_sTrxPIN != null) {
            m_RequestISOMsg.SetDataElement(52, m_sTrxPIN.getBytes(), m_sTrxPIN.length());
            Log.i(TAG, "DE 52 [m_sTrxPIN]= " + m_sTrxPIN + "Length =" + m_sTrxPIN.length());
        }


        m_RequestISOMsg.SetDataElement(53, m_sTrxSecurityControl.getBytes(), m_sTrxSecurityControl.length());
        Log.i(TAG, "DE 53 [m_sTrxSecurityControl]= " + m_sTrxSecurityControl+"Length ="+m_sTrxSecurityControl.length());


        m_RequestISOMsg.SetDataElement(54, m_sAdditionalAmount.getBytes(), m_sAdditionalAmount.length());
        Log.i(TAG, "DE 54 [m_sAdditionalAmount]= " + m_sAdditionalAmount+"Length ="+m_sAdditionalAmount.length());


        if (m_enmTrxCardType == CardType.ICC)
        {

            m_RequestISOMsg.SetDataElement(55, m_sICCRelatedTags.getBytes(), m_sICCRelatedTags.length());
            Log.i(TAG, "DE 55 [m_sICCRelatedTags]= " + m_sICCRelatedTags + "Length =" + m_sICCRelatedTags.length());
        }


        /*if(m_enmTrxType==TrxType.REFUND)     // we can check here if refund only
        {
            m_RequestISOMsg.SetDataElement(56, m_sOriginalTrxData.getBytes(), m_sOriginalTrxData.length());
            Log.i(TAG, "DE 56 [m_sOriginalTrxData]= " + m_sOriginalTrxData+"Length ="+m_sOriginalTrxData.length());
        }*/

        if( m_sTransportData!=null )
        {
            m_RequestISOMsg.SetDataElement(59, m_sTransportData.getBytes(), m_sTransportData.length());
            Log.i(TAG, "DE 59 [m_sTransportData]= " + m_sTransportData + "Length =" + m_sTransportData.length());
        }

        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, "DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());


        m_RequestISOMsg.SetDataElement(64, m_sTrxMACBlock.getBytes(), m_sTrxMACBlock.length());
        Log.i(TAG, "DE 64 [m_sTrxMACBlock]= " + m_sTrxMACBlock+"Length ="+m_sTrxMACBlock.length());


        return 0;
    }

    /**
	\Function Name: ComposeReversalMessage
	\Param  :
	\Return : int
	\Pre    :
	\Post   :
	\Author	: Moamen Ahmed
	\DT		: 17/05/2020
	\Des    : Compose Reversal Transaction Message
    */
    public int    ComposeReversalMessage()
    {
        //Clear  Message
        m_RequestISOMsg.ClearFields();


        //1. Set Reversal MTI
        m_RequestISOMsg.SetMTI(PosApplication.MTI_Reversal_Advice);

        // Set PAN
        m_RequestISOMsg.SetDataElement(2, m_sPAN.getBytes(), m_sPAN.length());
        Log.i(TAG, "DE 2 [m_sPAN]= " + m_sPAN+"Length ="+m_sPAN.length());


        // Set Processing Code
        m_RequestISOMsg.SetDataElement(3, m_sProcessCode.getBytes(), m_sProcessCode.length());
        Log.i(TAG, " DE 3 [m_sProcessCode]= " + m_sProcessCode+"Length ="+m_sProcessCode.length());


        // Set Transaction Amount
        m_RequestISOMsg.SetDataElement(4, m_sTrxAmount.getBytes(), m_sTrxAmount.length());
        Log.i(TAG, " DE 4 [m_sTrxAmount]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        // Set Transmission Date and Time
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, " DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxAmount.length());


        // Set Transaction STAN
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, " DE 11 [m_sSTAN]= " + m_sSTAN+"Length ="+m_sSTAN.length());


        // Set Date & Time, Local Transaction
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, " DE 12 [m_sLocalTrxDateTime]= " + m_sLocalTrxDateTime+"Length ="+m_sLocalTrxDateTime.length());


        // Set  Date, Expiration if manual (new flag should be added to POSTRansaction class)
        if(m_enmTrxCardType == CardType.MANUAL)
        {
            m_RequestISOMsg.SetDataElement(14, m_sCardExpDate.getBytes(), m_sCardExpDate.length());
            Log.i(TAG, " DE 14 [m_sCardExpDate]= " + m_sCardExpDate+"Length ="+m_sCardExpDate.length());
        }

        // Set Point of Service Data Code
        m_RequestISOMsg.SetDataElement(22, m_sPOSEntryMode.getBytes(), m_sPOSEntryMode.length());
        Log.i(TAG, " DE 22 [m_sPOSEntryMode]= " + m_sPOSEntryMode+"Length ="+m_sPOSEntryMode.length());


        // Set Card Sequence Number
        if(m_enmTrxCardType == CardType.ICC || m_enmTrxCardType == CardType.CTLS )
        {
            m_RequestISOMsg.SetDataElement(23, m_sCardSeqNum.getBytes(), m_sCardSeqNum.length());
            Log.i(TAG, " DE 23 [m_sCardSeqNum]= " + m_sCardSeqNum+"Length ="+m_sCardSeqNum.length());
        }

        // Set Function Code (400)
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, " DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        // Set Message Reason Code
        m_RequestISOMsg.SetDataElement(25, m_sMsgReasonCode.getBytes(), m_sMsgReasonCode.length());
        Log.i(TAG, " DE 25 [m_sMsgReasonCode]= " + m_sMsgReasonCode+"Length ="+m_sMsgReasonCode.length());


        // Set Card Acceptor Business Code
        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, " DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());


        // Set Card Acceptor Business Code
        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, " DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());

        /*Checking DE 30 when previus transaction is Authorizaiton */

        // Set Acquirer Institution Identification Code
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, " DE 32 [m_sCardAcceptorBusinessCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());

        // Set m_sTrack2
        if(m_enmTrxCardType == CardType.ICC || m_enmTrxCardType == CardType.MAG )
        {
            m_RequestISOMsg.SetDataElement(35, m_sTrack2.getBytes(), m_sTrack2.length());
            Log.i(TAG, " DE 35 [m_sTrack2]= " + m_sTrack2+"Length ="+m_sTrack2.length());
        }

        // Set Retrieval Reference Number
        m_RequestISOMsg.SetDataElement(37, m_sRRNumber.getBytes(), m_sRRNumber.length());
        Log.i(TAG, " DE 37 [m_sCardAcceptorBusinessCode]= " + m_sRRNumber+"Length ="+m_sRRNumber.length());

        // Set Approval Code
        if(m_sApprovalCode != null)
        {
            m_RequestISOMsg.SetDataElement(38, m_sApprovalCode.getBytes(), m_sApprovalCode.length());
            Log.i(TAG, " DE 38 [m_sApprovalCode]= " + m_sApprovalCode+"Length ="+m_sApprovalCode.length());
        }

        // Set Terminal Identification
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, " DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        // Set Merchant ID
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, " DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        // Set Card Scheme ID
        m_RequestISOMsg.SetDataElement(47, m_sCardSchemeSponsorID.getBytes(), m_sCardSchemeSponsorID.length());
        Log.i(TAG, " DE 47 [m_sCardSchemeSponsorID]= " + m_sCardSchemeSponsorID+"Length ="+m_sCardSchemeSponsorID.length());

        // Set Card Scheme ID(682)
        m_RequestISOMsg.SetDataElement(49, m_sCurrencyCode.getBytes(), m_sCardSchemeSponsorID.length());
        Log.i(TAG, " DE 49 [m_sCurrencyCode]= " + m_sCurrencyCode+"Length ="+m_sCurrencyCode.length());


        // Set KSN
        m_RequestISOMsg.SetDataElement(53, m_sCurrencyCode.getBytes(), m_sTrxSecurityControl.length());
        Log.i(TAG, " DE 53 [m_sTrxSecurityControl]= " + m_sCurrencyCode+"Length ="+m_sTrxSecurityControl.length());


        // Set ICC/CTLS Tag
        if(m_enmTrxCardType == CardType.ICC || m_enmTrxCardType == CardType.CTLS )
        {
            m_RequestISOMsg.SetDataElement(55, m_sICCRelatedTags.getBytes(), m_sICCRelatedTags.length());
            Log.i(TAG, " DE 55 [m_sICCRelatedTags]= " + m_sICCRelatedTags+"Length ="+m_sICCRelatedTags.length());

        }

        // Set Original Data Elements
        m_RequestISOMsg.SetDataElement(56, m_sOriginalTrxData.getBytes(), m_sOriginalTrxData.length());
        Log.i(TAG, " DE 56 [m_sOriginalTrxData]= " + m_sOriginalTrxData+"Length ="+m_sOriginalTrxData.length());

        // Set Terminal Status
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, " DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());


        // Should implment function for Compose MAC bloc data
        // ComposeMACData(TrxType);

        // Set MAC
        m_RequestISOMsg.SetDataElement(64, m_sTrxMACBlock.getBytes(), m_sTrxMACBlock.length());
        Log.i(TAG, " DE 64 [m_sTrxMACBlock]= " + m_sTrxMACBlock+"Length ="+m_sTrxMACBlock.length());


        return 0;

    }

    /**
     \Function Name: CompoaseReconciliationMessage
     \Param  : void
     \Return : int
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 18/05/2020
     \Des    : build reconciliation message DE+MTI
     */
    public int    CompoaseReconciliationMessage()
    {
        //Clear  Message
        m_RequestISOMsg.ClearFields();


        //1. Set MTI
        m_RequestISOMsg.SetMTI(PosApplication.MTI_Terminal_Reconciliation_Advice);

        // Set Terminal ID instead of PAN
        m_RequestISOMsg.SetDataElement(2, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, "DE 2 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        // Set Transmission Date and Time
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, " DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxAmount.length());


        // Set Transaction STAN
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, " DE 11 [m_sSTAN]= " + m_sSTAN+"Length ="+m_sSTAN.length());


        // Set Date & Time, Local Transaction
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, " DE 12 [m_sLocalTrxDateTime]= " + m_sLocalTrxDateTime+"Length ="+m_sLocalTrxDateTime.length());

        // Set Function code
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, " DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        // Set Card Acceptor Business Code
        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, " DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());


        // Set Reconciliation date
        m_RequestISOMsg.SetDataElement(28, m_sReconDateTime.getBytes(), m_sReconDateTime.length());
        Log.i(TAG, " DE 28 [m_sReconDateTime]= " + m_sReconDateTime+"Length ="+m_sReconDateTime.length());


        // Set Acquirer Institution Identification Code
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, " DE 32 [m_sCardAcceptorBusinessCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());


        // Set Terminal Identification
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, " DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        // Set Merchant ID
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, " DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        // Set reconciliation currency code(682)
        m_RequestISOMsg.SetDataElement(50, m_sReconCurrencyCode.getBytes(), m_sReconCurrencyCode.length());
        Log.i(TAG, " DE 50 [m_sReconCurrencyCode]= " + m_sReconCurrencyCode+"Length ="+m_sReconCurrencyCode.length());


        // Set KSN
        m_RequestISOMsg.SetDataElement(53, m_sCurrencyCode.getBytes(), m_sTrxSecurityControl.length());
        Log.i(TAG, " DE 53 [m_sTrxSecurityControl]= " + m_sCurrencyCode+"Length ="+m_sTrxSecurityControl.length());


        // Set Terminal Status
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, " DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());

        //Set MADA POS Terminal Reconciliation Totals
        m_RequestISOMsg.SetDataElement(124, m_sReconciliationTotals.getBytes(), m_sReconciliationTotals.length());
        Log.i(TAG, " DE 124 [m_sReconciliationTotals]= " + m_sReconciliationTotals+"Length ="+m_sReconciliationTotals.length());

        //MAC
        m_RequestISOMsg.SetDataElement(128, m_sTrxMACBlock.getBytes(), m_sTrxMACBlock.length());
        Log.i(TAG, " DE 124 [m_sTrxMACBlock]= " + m_sTrxMACBlock+"Length ="+m_sTrxMACBlock.length());



        return 0;
    }

    /**
     \Function Name: ComposeNetworkMessage
     \Param  : void
     \Return : int
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 18/05/2020
     \Des    : build Network message DE+MTI
     */
    public int    ComposeNetworkMessage()
    {
        //Clear  Message
        m_RequestISOMsg.ClearFields();


        //1. Set Terminal Registration MTI
        m_RequestISOMsg.SetMTI(PosApplication.MTI_Network_Management_Request);


        // Set Transmission Date and Time
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, " DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxAmount.length());


        // Set Transaction STAN
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, " DE 11 [m_sSTAN]= " + m_sSTAN+"Length ="+m_sSTAN.length());


        // Set Date & Time, Local Transaction
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, " DE 12 [m_sLocalTrxDateTime]= " + m_sLocalTrxDateTime+"Length ="+m_sLocalTrxDateTime.length());

        // Set Function code
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, " DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());

/*

        // Set Acquirer Institution Identification Code
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, " DE 32 [m_sAquirerInsIDCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());

        // Set Action code
        m_RequestISOMsg.SetDataElement(39, m_sActionCode.getBytes(), m_sActionCode.length());
        Log.i(TAG, " DE 39 [m_sActionCode]= " + m_sActionCode+"Length ="+m_sActionCode.length());


        // Set Terminal Identification
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, " DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        // Set Merchant ID
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, " DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());
*/

        // Set HostData
        m_RequestISOMsg.SetDataElement(48, m_sHostData_DE48.getBytes(), m_sHostData_DE48.length());
        Log.i(TAG, " DE 48 [m_sHostData_DE48]= " + m_sHostData_DE48+"Length ="+m_sHostData_DE48.length());

        // Set Terminal Status
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, " DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());

        return 0;
    }

    /**
     \Function Name: ComposeFileDownloadMessage
     \Param  : void
     \Return : int
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 18/05/2020
     \Des    : build download parameter message DE+MTI
     */
    public int    ComposeFileDownloadMessage()
    {
        //Clear  Message
        m_RequestISOMsg.ClearFields();


        //1. Set File action MTI
        m_RequestISOMsg.SetMTI(PosApplication.MTI_File_Action_Request);


        // Set Transmission Date and Time
        m_sTrxDateTime= ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, " DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxAmount.length());


        // Set Transaction STAN
        m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.iSTAN);
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, " DE 11 [m_sSTAN]= " + m_sSTAN+"Length ="+m_sSTAN.length());


        // Set Date & Time, Local Transaction
        m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, " DE 12 [m_sLocalTrxDateTime]= " + m_sLocalTrxDateTime+"Length ="+m_sLocalTrxDateTime.length());

        // Set Function code
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, " DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        // Set Acquirer Institution Identification Code
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, " DE 32 [m_sAquirerInsIDCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());

        // Set Action code
        m_RequestISOMsg.SetDataElement(39, m_sActionCode.getBytes(), m_sActionCode.length());
        Log.i(TAG, " DE 39 [m_sActionCode]= " + m_sActionCode+"Length ="+m_sActionCode.length());


        // Set Terminal Identification
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, " DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        // Set Merchant ID
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, " DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        // Set KSN
        m_RequestISOMsg.SetDataElement(53, m_sCurrencyCode.getBytes(), m_sTrxSecurityControl.length());
        Log.i(TAG, " DE 53 [m_sTrxSecurityControl]= " + m_sCurrencyCode+"Length ="+m_sTrxSecurityControl.length());


        // Set Terminal Status
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, " DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());

        // Set Data Record
        m_RequestISOMsg.SetDataElement(72, m_sDataRecord72.getBytes(), m_sDataRecord72.length());
        Log.i(TAG, " DE 72 [m_sDataRecord72]= " + m_sDataRecord72+"Length ="+m_sDataRecord72.length());

        //MAC
        m_RequestISOMsg.SetDataElement(128, m_sTrxMACBlock.getBytes(), m_sTrxMACBlock.length());
        Log.i(TAG, " DE 124 [m_sTrxMACBlock]= " + m_sTrxMACBlock+"Length ="+m_sTrxMACBlock.length());
        return 0;
    }

    /**
     \Function Name: ComposeAdministrativeMessage
     \Param  : void
     \Return : int
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 18/05/2020
     \Des    : build Administrative message DE+MTI
     */
    public int    ComposeAdministrativeMessage()
    {
        //Clear  Message
        m_RequestISOMsg.ClearFields();


        //1. Set MTI
        m_RequestISOMsg.SetMTI(PosApplication.MTI_Administrative_Notification);


        // Set Transmission Date and Time
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, " DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxAmount.length());


        // Set Transaction STAN
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, " DE 11 [m_sSTAN]= " + m_sSTAN+"Length ="+m_sSTAN.length());


        // Set Date & Time, Local Transaction
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, " DE 12 [m_sLocalTrxDateTime]= " + m_sLocalTrxDateTime+"Length ="+m_sLocalTrxDateTime.length());

        // Set Function code
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, " DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());



        // Set Acquirer Institution Identification Code
        if(m_sAquirerInsIDCode!=null) {
            m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
            Log.i(TAG, " DE 32 [m_sCardAcceptorBusinessCode]= " + m_sAquirerInsIDCode + "Length =" + m_sAquirerInsIDCode.length());
        }

        //Set ActionCode
        if(m_sActionCode != null)
        {
            m_RequestISOMsg.SetDataElement(39, m_sActionCode.getBytes(), m_sActionCode.length());
            Log.i(TAG, " DE 39 [m_sActionCode]= " + m_sActionCode+"Length ="+m_sActionCode.length());
        }
        // Set Terminal Identification
        if(m_sTerminalID!=null) {
            m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
            Log.i(TAG, " DE 41 [m_sTerminalID]= " + m_sTerminalID + "Length =" + m_sTerminalID.length());
        }
        // Set Merchant ID
        if(m_sMerchantID!=null) {
            m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
            Log.i(TAG, " DE 42 [m_sMerchantID]= " + m_sMerchantID + "Length =" + m_sMerchantID.length());
        }
        //Set Data Record
        m_RequestISOMsg.SetDataElement(72, m_sDataRecord72.getBytes(), m_sDataRecord72.length());
        Log.i(TAG, " DE 72 [m_sDataRecord72]= " + m_sDataRecord72+"Length ="+m_sDataRecord72.length());

        return 0;
    }
    /**
     * \Function Name: ComposePurchaseAdviseMessage
     * \Param  : TranscationType
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 5/18/2020
     * \Des    : for building Purchase ADVICE messages and ready to be coverted to str for sending (MTI+DEs)
     */
    public int    ComposePurchaseAdviseMessage(TranscationType TrxType)
    {

        //Clear  Message
        m_RequestISOMsg.ClearFields();

        //1. Set Financial Advice MTI
        m_RequestISOMsg.SetMTI(PosApplication.MTI_Financial_Transaction_Advice);



        //2. Set PAN
        if (m_enmTrxCardType !=CardType.MANUAL ) {

            m_RequestISOMsg.SetDataElement(2, m_sPAN.getBytes(), m_sPAN.length());
            Log.i(TAG, "DE 2 [m_sPAN]= " + m_sPAN+"Length ="+m_sPAN.length());
        }

        //3.Set Processing Code
        m_RequestISOMsg.SetDataElement(3, m_sProcessCode.getBytes(), m_sProcessCode.length());
        Log.i(TAG, "DE 3 [m_sProcessCode]= " + m_sProcessCode+"Length ="+m_sProcessCode.length());


        //4.Set Amount
        m_RequestISOMsg.SetDataElement(4, m_sTrxAmount.getBytes(), m_sTrxAmount.length());
        Log.i(TAG, "DE 4 [m_sTrxAmount]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        //7.local Transaction Date & time
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, "DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxDateTime.length());



        //11.STAN
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, "DE 11 [m_sSTAN]= " + m_sTrxAmount+"Length ="+m_sSTAN.length());

        //12. Local Transaction time and date
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, "DE 12 [m_sLocalTrxDateTime]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        //14. Card Expiry Date
        if (m_enmTrxCardType == CardType.MANUAL) {
            if (m_sCardExpDate != null) {
                m_RequestISOMsg.SetDataElement(14, m_sCardExpDate.getBytes(), m_sCardExpDate.length());
                Log.i(TAG, "DE 14 [m_sCardExpDate]= " + m_sCardExpDate+"Length ="+m_sCardExpDate.length());
            }
        }

        //22.Pos Entry Mode
        if (m_enmTrxCardType == CardType.MAG) {

        } else if (m_enmTrxCardType == CardType.ICC) {

        } else if (m_enmTrxCardType == CardType.CTLS) {

        }
        m_RequestISOMsg.SetDataElement(22, m_sPOSEntryMode.getBytes(), m_sPOSEntryMode.length());
        Log.i(TAG, "DE 22 [m_sPOSEntryMode]= " + m_sPOSEntryMode+"Length ="+m_sPOSEntryMode.length());

        //23.Card Sequence Number
        if (m_enmTrxCardType == CardType.ICC) {
            m_RequestISOMsg.SetDataElement(23, m_sCardSeqNum.getBytes(), m_sCardSeqNum.length());
            Log.i(TAG, "DE 23 [m_sCardSeqNum]= " + m_sCardSeqNum+"Length ="+m_sCardSeqNum.length());
        }

        //24. Function Code
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, "DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        //25. Message reason Code
        m_RequestISOMsg.SetDataElement(25, m_sMsgReasonCode.getBytes(), m_sMsgReasonCode.length());
        Log.i(TAG, "DE 25 [m_sMsgReasonCode]= " + m_sMsgReasonCode+"Length ="+m_sMsgReasonCode.length());


        //26. Card Acceptor Bussiness Code
        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, "DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());

        //30.Original amount
        if(m_sOrigAmount!=null)
        {
            m_RequestISOMsg.SetDataElement(26, m_sOrigAmount.getBytes(), m_sOrigAmount.length());
            Log.i(TAG, "DE 30 [m_sOrigAmount]= " + m_sOrigAmount+"Length ="+m_sOrigAmount.length());

        }
        //32. Acquirer institution ID
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, "DE 32 [m_sAquirerInsIDCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());

        //35.Track2 Data
        if ((m_enmTrxCardType == CardType.MAG) || (m_enmTrxCardType == CardType.ICC)) {

            m_RequestISOMsg.SetDataElement(35, m_sTrack2.getBytes(), m_sTrack2.length());
            Log.i(TAG, "DE 35 [m_sTrack2]= " + m_sTrack2+"Length ="+m_sTrack2.length());
        }

        //37. RRN
        m_RequestISOMsg.SetDataElement(37, m_sRRNumber.getBytes(), m_sRRNumber.length());
        Log.i(TAG, "DE 37 [m_sRRNumber]= " + m_sRRNumber+"Length ="+m_sRRNumber.length());


        //38.Approval code
        m_RequestISOMsg.SetDataElement(38, m_sApprovalCode.getBytes(), m_sApprovalCode.length());
        Log.i(TAG, "DE 38 [m_sApprovalCode]= " + m_sApprovalCode+"Length ="+m_sApprovalCode.length());


        //41.Terminal ID
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, "DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        //42.Merchant ID
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, "DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        //47.CardScheme Sponsor ID
        m_RequestISOMsg.SetDataElement(47, m_sCardSchemeSponsorID.getBytes(), m_sCardSchemeSponsorID.length());
        Log.i(TAG, "DE 47 [m_sCardSchemeSponsorID]= " + m_sCardSchemeSponsorID+"Length ="+m_sCardSchemeSponsorID.length());

        //49.Currency Code
        m_RequestISOMsg.SetDataElement(49, m_sCurrencyCode.getBytes(), m_sCurrencyCode.length());
        Log.i(TAG, "DE 49 [m_sCurrencyCode]= " + m_sCurrencyCode+"Length ="+m_sCurrencyCode.length());


        //52.PIN
        if(m_enmTrxCVM==CVM.ONLINE_PIN) {
            m_RequestISOMsg.SetDataElement(52, m_sTrxPIN.getBytes(), m_sTrxPIN.length());
            Log.i(TAG, "DE 52 [m_sTrxPIN]= " + m_sTrxPIN + "Length =" + m_sTrxPIN.length());
        }

        //53. Transaction Security control
        m_RequestISOMsg.SetDataElement(53, m_sTrxSecurityControl.getBytes(), m_sTrxSecurityControl.length());
        Log.i(TAG, "DE 53 [m_sTrxSecurityControl]= " + m_sTrxSecurityControl+"Length ="+m_sTrxSecurityControl.length());

        //54. Additional amounts
        m_RequestISOMsg.SetDataElement(54, m_sAdditionalAmount.getBytes(), m_sAdditionalAmount.length());
        Log.i(TAG, "DE 54 [m_sAdditionalAmount]= " + m_sAdditionalAmount+"Length ="+m_sAdditionalAmount.length());

        //55. ICC related Data
        if (m_enmTrxCardType == CardType.ICC)
            m_RequestISOMsg.SetDataElement(55, m_sICCRelatedTags.getBytes(), m_sICCRelatedTags.length());
        Log.i(TAG, "DE 55 [m_sICCRelatedTags]= " + m_sICCRelatedTags+"Length ="+m_sICCRelatedTags.length());

        //56.original Transaction data
        if(m_enmTrxType==TrxType.REFUND ||m_enmTrxType==TrxType.PURCHASE_ADVICE)     // we can check here if refund only
        {
            m_RequestISOMsg.SetDataElement(56, m_sOriginalTrxData.getBytes(), m_sOriginalTrxData.length());
            Log.i(TAG, "DE 56 [m_sOriginalTrxData]= " + m_sOriginalTrxData+"Length ="+m_sOriginalTrxData.length());
        }

        //59.Transaport Data
        m_RequestISOMsg.SetDataElement(59, m_sTransportData.getBytes(), m_sTransportData.length());
        Log.i(TAG, "DE 59 [m_sTransportData]= " + m_sTransportData+"Length ="+m_sTransportData.length());

        //62.Terminal Status
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, "DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());

        //64.Transaction Block
        m_RequestISOMsg.SetDataElement(64, m_sTrxMACBlock.getBytes(), m_sTrxMACBlock.length());
        Log.i(TAG, "DE 64 [m_sTrxMACBlock]= " + m_sTrxMACBlock+"Length ="+m_sTrxMACBlock.length());


        return 0;
    }

    /**
     * \Function Name: ComposeAuthorisationAdviseMessage
     * \Param  : TranscationType
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 5/18/2020
     * \Des    : for building AUTH ADVICE messages and ready to be coverted to str for sending (MTI+DEs)
     */
    public int    ComposeAuthorisationAdviseMessage(TranscationType TrxType)
    {
        //Clear  Message
        m_RequestISOMsg.ClearFields();


        //1. Set Authorization Advice MTI
        m_RequestISOMsg.SetMTI(PosApplication.MTI_Authorisation_Advice);



        //2. Set PAN
        if (m_enmTrxCardType !=CardType.MANUAL ) {

            m_RequestISOMsg.SetDataElement(2, m_sPAN.getBytes(), m_sPAN.length());
            Log.i(TAG, "DE 2 [m_sPAN]= " + m_sPAN+"Length ="+m_sPAN.length());

        }

        //3.Set Processing Code
        m_RequestISOMsg.SetDataElement(3, m_sProcessCode.getBytes(), m_sProcessCode.length());
        Log.i(TAG, "DE 3 [m_sProcessCode]= " + m_sProcessCode+"Length ="+m_sProcessCode.length());


        //4. Amount
        m_RequestISOMsg.SetDataElement(4, m_sTrxAmount.getBytes(), m_sTrxAmount.length());
        Log.i(TAG, "DE 4 [m_sTrxAmount]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        //7.local Transaction Date & time
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, "DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxDateTime.length());



        //11.STAN
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, "DE 11 [m_sSTAN]= " + m_sTrxAmount+"Length ="+m_sSTAN.length());

        //12. Local Transaction time and date
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, "DE 12 [m_sLocalTrxDateTime]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        //14. Card Expiry Date
        if (m_enmTrxCardType == CardType.MANUAL) {
            if (m_sCardExpDate != null) {
                m_RequestISOMsg.SetDataElement(14, m_sCardExpDate.getBytes(), m_sCardExpDate.length());
                Log.i(TAG, "DE 14 [m_sCardExpDate]= " + m_sCardExpDate+"Length ="+m_sCardExpDate.length());
            }
        }

        //22.Pos Entry Mode
        if (m_enmTrxCardType == CardType.MAG) {

        } else if (m_enmTrxCardType == CardType.ICC) {

        } else if (m_enmTrxCardType == CardType.CTLS) {

        }
        m_RequestISOMsg.SetDataElement(22, m_sPOSEntryMode.getBytes(), m_sPOSEntryMode.length());
        Log.i(TAG, "DE 22 [m_sPOSEntryMode]= " + m_sPOSEntryMode+"Length ="+m_sPOSEntryMode.length());

        //23.Card Sequence Number
        if (m_enmTrxCardType == CardType.ICC) {
            m_RequestISOMsg.SetDataElement(23, m_sCardSeqNum.getBytes(), m_sCardSeqNum.length());
            Log.i(TAG, "DE 23 [m_sCardSeqNum]= " + m_sCardSeqNum+"Length ="+m_sCardSeqNum.length());
        }

        //24. Function Code
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, "DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        //25. Message reason Code
        m_RequestISOMsg.SetDataElement(25, m_sMsgReasonCode.getBytes(), m_sMsgReasonCode.length());
        Log.i(TAG, "DE 25 [m_sMsgReasonCode]= " + m_sMsgReasonCode+"Length ="+m_sMsgReasonCode.length());


        //26. Card Acceptor Bussiness Code
        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, "DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());

        //30.Original amount
        if(m_sOrigAmount!=null)
        {
            m_RequestISOMsg.SetDataElement(26, m_sOrigAmount.getBytes(), m_sOrigAmount.length());
            Log.i(TAG, "DE 30 [m_sOrigAmount]= " + m_sOrigAmount+"Length ="+m_sOrigAmount.length());

        }
        //32. Acquirer institution ID
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, "DE 32 [m_sAquirerInsIDCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());

        //35.Track2 Data
        if ((m_enmTrxCardType == CardType.MAG) || (m_enmTrxCardType == CardType.ICC)) {

            m_RequestISOMsg.SetDataElement(35, m_sTrack2.getBytes(), m_sTrack2.length());
            Log.i(TAG, "DE 35 [m_sTrack2]= " + m_sTrack2+"Length ="+m_sTrack2.length());
        }

        //37. RRN
        m_RequestISOMsg.SetDataElement(37, m_sRRNumber.getBytes(), m_sRRNumber.length());
        Log.i(TAG, "DE 37 [m_sRRNumber]= " + m_sRRNumber+"Length ="+m_sRRNumber.length());


        //38.Approval code
        m_RequestISOMsg.SetDataElement(38, m_sApprovalCode.getBytes(), m_sApprovalCode.length());
        Log.i(TAG, "DE 38 [m_sApprovalCode]= " + m_sApprovalCode+"Length ="+m_sApprovalCode.length());


        //41.Terminal ID
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, "DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        //42.Merchant ID
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, "DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        //47.CardScheme Sponsor ID
        m_RequestISOMsg.SetDataElement(47, m_sCardSchemeSponsorID.getBytes(), m_sCardSchemeSponsorID.length());
        Log.i(TAG, "DE 47 [m_sCardSchemeSponsorID]= " + m_sCardSchemeSponsorID+"Length ="+m_sCardSchemeSponsorID.length());

        //49.Currency Code
        m_RequestISOMsg.SetDataElement(49, m_sCurrencyCode.getBytes(), m_sCurrencyCode.length());
        Log.i(TAG, "DE 49 [m_sCurrencyCode]= " + m_sCurrencyCode+"Length ="+m_sCurrencyCode.length());


        //52.PIN
        if(m_enmTrxCVM==CVM.ONLINE_PIN) {
            m_RequestISOMsg.SetDataElement(52, m_sTrxPIN.getBytes(), m_sTrxPIN.length());
            Log.i(TAG, "DE 52 [m_sTrxPIN]= " + m_sTrxPIN + "Length =" + m_sTrxPIN.length());
        }

        //53. Transaction Security control
        m_RequestISOMsg.SetDataElement(53, m_sTrxSecurityControl.getBytes(), m_sTrxSecurityControl.length());
        Log.i(TAG, "DE 53 [m_sTrxSecurityControl]= " + m_sTrxSecurityControl+"Length ="+m_sTrxSecurityControl.length());


        //55. ICC related Data
        if (m_enmTrxCardType == CardType.ICC)
            m_RequestISOMsg.SetDataElement(55, m_sICCRelatedTags.getBytes(), m_sICCRelatedTags.length());
        Log.i(TAG, "DE 55 [m_sICCRelatedTags]= " + m_sICCRelatedTags+"Length ="+m_sICCRelatedTags.length());

        //56.original Transaction data
        m_RequestISOMsg.SetDataElement(56, m_sOriginalTrxData.getBytes(), m_sOriginalTrxData.length());
        Log.i(TAG, "DE 56 [m_sOriginalTrxData]= " + m_sOriginalTrxData+"Length ="+m_sOriginalTrxData.length());


        //59.Transaport Data
        m_RequestISOMsg.SetDataElement(59, m_sTransportData.getBytes(), m_sTransportData.length());
        Log.i(TAG, "DE 59 [m_sTransportData]= " + m_sTransportData+"Length ="+m_sTransportData.length());

        //62.Terminal Status
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, "DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());

        //64.Transaction Block
        m_RequestISOMsg.SetDataElement(64, m_sTrxMACBlock.getBytes(), m_sTrxMACBlock.length());
        Log.i(TAG, "DE 64 [m_sTrxMACBlock]= " + m_sTrxMACBlock+"Length ="+m_sTrxMACBlock.length());


        return 0;
    }

    /**
     * \Function Name: ComposeTerminalStatus
     * \Param  : TranscationType
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 5/18/2020
     * \Des    : for building AUTH ADVICE messages and ready to be coverted to str for sending (MTI+DEs)
     */
    public void ComposeTerminalStatusData()
    {
        //Terminal Dial Indicator 01   1-F
            m_sTerminalStatus="01"+PosApplication.terminal_dial_indicator; // '1'-9', 'A' - 'F' (i.e. total range is '1'-'F').
        //Printer Status
            m_sTerminalStatus.concat("02"+PosApplication.Printer_Status);  //'0' = No printer. '1' = Out of paper. '2' = Plain paper receipt.
        //Idle Time
            m_sTerminalStatus.concat("03"+PosApplication.Idle_Time);  //hhmmss
        //Magnetic Reader Status
            m_sTerminalStatus.concat("04"+PosApplication.Magnetic_Reader_Status);// '0'=Okay. '1' = Out of order.
        //Chip Card Reader Status
            m_sTerminalStatus.concat("05"+PosApplication.Chip_Card_Reader_Status);// '0'=Okay. '1' = Out of order.

        //GPS Location Coordinates
            m_sTerminalStatus.concat("07"+PosApplication.GPS_Location_Coordinates);// ANNNNNNANNNNNNN e.g. N402646W0795856 Which represents, N 40° 26′ 46″ W 079° 58′ 56″

        //Contactless Reader Status
            m_sTerminalStatus.concat("09"+PosApplication.Contactless_Reader_Status);// '0'=Okay. '1' = Out of order. '9' = Not supported.

        //Connection Start Time
            m_sTerminalStatus.concat("10"+PosApplication.Connection_Start_Time); //HHMMSSsss As 24 hour clock

        //Connection End Time
            m_sTerminalStatus.concat("11"+PosApplication.Connection_End_Time ); //HHMMSSsss As 24 hour clock

        //Request Sent Time
            m_sTerminalStatus.concat("12"+PosApplication.Request_Sent_Time ); //HHMMSSsss As 24 hour clock

        //Response Received Time
            m_sTerminalStatus.concat("13"+PosApplication.Response_Received_Time ); //HHMMSSsss As 24 hour clock
        //Performance Timers Reference
            m_sTerminalStatus.concat("14"+PosApplication.Performance_Timers_Reference); //original RRN for the online authorization or financial message for which the timers refer to.
        //mada EFTPOS specification release version
            //todo getversion details
            //getversion()
            m_sTerminalStatus.concat("15"+PosApplication.mada_EFTPOS_specification_release_version ); //The POS should send the version number without dots, and with 2 digits each with a leading zero, if applicable, for the Major, Minor and Patch specification version numbers i.e. Version 6.0.3 should be expressed as 060003 and Version 10.2.0 should be expressed as 100200
        //Connection Details
            //todo getting connection detail
            //getconnectiondetail()
            m_sTerminalStatus.concat("16"+PosApplication.Connection_Details); //Connection Priority ‘01’ Primary ‘02’ Secondary,   Network Service Provider (NSP) ‘01’ iNET ‘02’ Mobily ‘03’ Zain ‘04’ Sky Band ‘05’ Geidea ,   Provider ‘01’ STC ‘02’ Mobily ‘03’ Zain ‘04’ Sky Band  , Connection Method ‘01’ Dial-up ‘02’ SIM ‘03’ TCP/IP ‘04’ VSAT ‘05’ DSL ‘06’ WiFi


/*
        ///for response
        //Terminal Online Flag
            m_sTerminalStatus.concat("06"+PosApplication.Terminal_Online_Flag); // '0'=No action. '1' = Go on-line.
        //Force Reconciliation Flag
            m_sTerminalStatus.concat("08"+PosApplication.Force_Reconciliation_Flag);// '0'=No action. '1' = Go on-line.*/

    }

}


