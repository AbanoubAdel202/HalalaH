package com.example.halalah;
import android.util.Log;
import android.widget.Switch;

import com.example.halalah.TMS.Card_Scheme;
import com.example.halalah.TMS.SAMA_TMS;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.iso8583.ISO8583;

import com.example.halalah.packet.PackUtils;
import com.example.halalah.secure.DUKPT_KEY;
import com.example.halalah.util.BytesUtil;
import com.example.halalah.util.ExtraUtil;

import java.lang.reflect.Array;
import java.util.Locale;

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




    public boolean m_is_mada;
    public boolean m_is_final;
    public Boolean m_bIsOfflineTrx;
    private int reversal_status;


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
    public enum CVM{
        ONLINE_PIN,
        OFFLINE_PIN,
        SIGNATURE,
        NO_CVM,
        CDCVM,
        OFFLINE_PIN_SIGNATURE,
        ONLINE_PIN_SIGNATURE

    }

    // Constractur
    public POSTransaction()
    {
            m_is_mada=false;
            m_is_final=false;
        // Transaction Data Elements as per SP Terminal interface specification document 6.0.9
            m_sMTI=null;                                /* MTI*/
        	m_sPAN=null;								/* DE2   – Primary account Number -Size 19  –Reconcilation value would be Terminal ID as is present in DE 41*/
        	m_sProcessCode=null;	    				/* DE3   – Process Code  –Size 6 */
        	m_sTrxAmount=null;						/* DE4   – Transaction Amount – Size 12*/
        	m_sTrxDateTime=null;					/* DE7   – Transaction Date Time  – size 10 (MMDDhhmmss) */
        	m_sSTAN=null;							/* DE11  – Transaction STAN  – Size 6*/
        	m_sLocalTrxDateTime=null;				/* DE12  – Local Transaction Date Time  –n 12 (YYMMDDhhmmss)*/
        	m_sCardExpDate=null;						/* DE14  – Card Expiration Date  –n 4 (YYMM)*/
        	m_sPOSEntryMode=null;					/* DE22  – Point of Service Data Code – an 12*/
        	m_sCardSeqNum=null;						/* DE 23 – Card Sequence Number -Size n 3*/
        	m_sFunctionCode=null;					/* DE 24 – Function Code -Size n 3*/
        	m_sMsgReasonCode=null;					/* DE 25 – Message Reason Code -Size 4*/
        	m_sCardAcceptorBusinessCode=null;		/* DE 26 – Card Acceptor Business Code -Size 4*/
        	m_sReconDateTime=null;					/* DE 28 – Reconciliation Date - Size n 6 (YYMMDD)*/
        	m_sOrigAmount=null;						/* DE 30 – Original Amount - Size n 12*/
        	m_sAquirerInsIDCode=null; 				/* DE 32 – Acquirer Institution Identification Code -Size n..11 , Recieved after terminal registration*/
        	m_sTrack2=null;			    			/* DE 35 – Track-2 Data -Size z..37*/
        	m_sRRNumber=null;						/* DE 37 – Retrieval Reference Number -Size anp 12*/
        	m_sApprovalCode=null;					/* DE 38 – Approval Code -Size anp 6 , Host reveived value*/
        	m_sActionCode=null;						/* DE 39 – Action Code - Size n 3 , Host reveived value*/
        	m_sTerminalID=null;						/* DE 41 – Card Acceptor Terminal Identification -Size ans 16 */
        	m_sMerchantID=null;						/* DE 42 – Card Acceptor Identification Code (Merchant ID) - Size ans 15*/
            m_sAdditionalResponseData=null;			/* DE 44 - Additional Response Data - Size ans..99 , ag based data elements originating from either the Issuer,mada Switch */
            m_sCardSchemeSponsorID=null;				/* DE 47 – Private - Card Scheme Sponsor ID - Size ans..999 , Used to sent Bank ID(len = 4) and Card Scheme ID(len 2) and other details for Bill Payments*/
            m_sHostData_DE48=null;					/* DE 48 – Private – Additional Data-Size ans..999 ,Used during terminal registation and campaign footer messages */
            m_sCurrencyCode=null;				    /* DE 49 – Currency Code, Transaction - Size n 3 , fixed value "682" */
            m_sReconCurrencyCode=null;				/* DE 50 – DE 50 – Currency Code, Reconciliation - Size n 3 , "682" */
        	m_sTrxPIN=null;							/* DE 52 – Personal Identification Number (PIN) - Size 8 BYTE */
            m_sTrxSecurityControl=null;	            /* DE 53 – Security Related Control Information - Size b..48 , KSN + KSN Descriptor (609)*/
        	m_sAdditionalAmount=null;				/* DE 54 – Additional Amounts Used currently for CashBack  , formated as "0040+CurrencyCode (682)D+ CashBack amount*/
            m_sICCRelatedTags=null;					/* DE 55 – ICC Related Data (Tags for both ICC & CTLS Trx) Request/Response - Size b..255 */
            m_sOriginalTrxData=null;					/* DE 56 – Original Data Elements - Size ANP..58*/
            m_sTransportData=null;                   /* DE 59 – Transport Data -Size ans..999*/
            m_sTerminalStatus =null;                  /* DE 62 – Private – Terminal Status ans..999*/
            m_sTrxMACBlock=null;                     /* DE 64/128 – Message Authentication Code (MAC) - Size 8 bytes only the left 4 bytes will be used for the MAC and the right 4 bytes will bepadded with F’s.*/
            m_sDataRecord72=null;	                /* DE 72 – Data Record Used on TMS and Admin messages -  Size ans11..999*/
            m_sReconciliationTotals=null;            /* DE 124 – Private – mada POS Terminal Reconciliation Totals - Size  ans..999*/

            m_RequestISOMsg=new ISO8583();                  /* ISO8583 Request message to be sent constractor*/
            m_ResponseISOMsg=new ISO8583();         /* ISO8583 Response message constractor*/
        m_card_scheme = new Card_Scheme();                /* card scheme for the card used in this transaction */
    }


    // Transaction Data Elements as per SP Terminal interface specification document 6.0.9
    public String   m_sMTI;                                /* MTI*/
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
    public byte[]   m_sTrxSecurityControl;	            /* DE 53 – Security Related Control Information - Size b..48 , KSN + KSN Descriptor (609)*/
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
    public TranscationType   m_enum_OrigTRxtype;            /* Original Transaction Type*/
    /*********************************************/

   public Card_Scheme m_card_scheme;
   public String      m_sAID;                             // AID for the card ICC


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

    public String ComposeReconciliationTotals(int iNumberOfCardScheme , CardSchemeTotals[] oTotalsArray )
    {
       /**
        01-02 2 Number of Card Schemes (values from
        01 to 10)
        03-98 96 Card Scheme Totals – 01
        99-194 96 Card Scheme Totals – 02
        195-290 96 Card Scheme Totals – 03
        291-386 96 Card Scheme Totals – 04
        387-482 96 Card Scheme Totals – 05
        483-578 96 Card Scheme Totals – 06
        579-674 96 Card Scheme Totals – 07
        675-770 96 Card Scheme Totals – 08
        771-866 96 Card Scheme Totals – 09
        867-962 96 Card Scheme Totals – 10
        */
        String stotalsDE124=null;

        stotalsDE124.concat(String.format(Locale.ENGLISH,"%02D",iNumberOfCardScheme));
       /* if(iNumberOfCardScheme>99)
            return "number of cards is more than 99";
        if(iNumberOfCardScheme>9)
            stotalsDE124.concat(Integer.toString(iNumberOfCardScheme));  // number of cards from 10 to 99
        else                                                            //number of cards from 0 to 9
            stotalsDE124.concat("0"+ iNumberOfCardScheme);
*/



        for(int i=0; i<iNumberOfCardScheme;i++) {
            stotalsDE124.concat(oTotalsArray[i].m_szCardSchmID.toString());            /* Card scheme ID*/
            stotalsDE124.concat(oTotalsArray[i].m_szCardSchemeAcqID.toString());       /* Card Scheme Acquirer ID*/
            // Trx Totals
            stotalsDE124.concat(String.format(Locale.ENGLISH,"%010D",oTotalsArray[i].m_lDebitCount));            /* Debit Count*/
            stotalsDE124.concat(String.format(Locale.ENGLISH,"%015D",oTotalsArray[i].m_dDebitAmount));           /* Debit Amount*/
            stotalsDE124.concat(String.format(Locale.ENGLISH,"%010D",oTotalsArray[i].m_lCreditCount));           /* Credit Count*/
            stotalsDE124.concat(String.format(Locale.ENGLISH,"%015D",oTotalsArray[i].m_dCreditAmount));          /* Credit Amount*/
            stotalsDE124.concat(String.format(Locale.ENGLISH,"%015D",oTotalsArray[i].m_dCashBackAmount));        /* Cash Back Amount*/
            stotalsDE124.concat(String.format(Locale.ENGLISH,"%015D",oTotalsArray[i].m_dCashAdvanceAmount));     /* Cash Advance Amount*/
            stotalsDE124.concat(String.format(Locale.ENGLISH,"%010D",oTotalsArray[i].m_lAuthorisationCount));    /* Authorisation Count*/
        }
        return stotalsDE124;
    }
    public String ComposeICCTags(CardType enmCard)
    {
        return "0";
    }

    public String ComposeTerminalRegistrationData() // use ComposeNetworkMessage instead
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
                sMAC.concat(new String(m_sTrxSecurityControl));
                //55.EMV Data
                sMAC.concat(m_sICCRelatedTags);
                break;
            case AUTHORISATION_ADVICE:
                //0.Messa   ge Type Identifier
                sMAC=PosApplication.MTI_Authorisation_Advice;
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
                sMAC.concat(new String(m_sTrxSecurityControl));
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
                sMAC.concat(new String(m_sTrxSecurityControl));
                //55.EMV Data
                if(m_enmTrxCardType==CardType.ICC|m_enmTrxCardType==CardType.CTLS)
                sMAC.concat(m_sICCRelatedTags);
                break;
            case PURCHASE_ADVICE:
                //0.Messa   ge Type Identifier
                sMAC=PosApplication.MTI_Financial_Transaction_Advice;
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
                sMAC.concat(new String(m_sTrxSecurityControl));
                //55.EMV Data
                sMAC.concat(m_sICCRelatedTags);
                break;
            case REVERSAL:
                //0.Messa   ge Type Identifier
                sMAC=PosApplication.MTI_Reversal_Advice;
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
                sMAC.concat(new String(m_sTrxSecurityControl));
                //55.EMV Data
                sMAC.concat(m_sICCRelatedTags);
                break;

            case TMS_FILE_DOWNLOAD:
                //0.Messa   ge Type Identifier
                sMAC=PosApplication.MTI_File_Action_Request;
                //1. Primary bitmap
                sMAC.concat(new String(m_RequestISOMsg.Getbitmap()));
                //11.System Trace Audit Number
                sMAC.concat(m_sSTAN);
                //12.Date and Time, Local Transaction
                sMAC.concat(m_sLocalTrxDateTime);
                //53.Security Related Control Information
                sMAC.concat(new String(m_sTrxSecurityControl));
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
        if (bMac.length%8!=0) {
            for(int i=0 ;i<bMac.length%8;i++)
            {
                sMAC=sMAC+0x00;
            }
        }

        m_sTrxMACBlock= DUKPT_KEY.CaluclateMACBlock(sMAC);

        //removinglast 4 bytes
        m_sTrxMACBlock=m_sTrxMACBlock.substring(0,4);
        m_sTrxMACBlock=m_sTrxMACBlock.concat("ÿÿÿÿ");


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
                ComposeFinancialAdviseMessage(TrxType);
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
        GetDE03();
        m_RequestISOMsg.SetDataElement(3, m_sProcessCode.getBytes(), m_sProcessCode.length());
        Log.i(TAG, "DE 3 [m_sProcessCode]= " + m_sProcessCode+"Length ="+m_sProcessCode.length());


        //4. Amount
        m_RequestISOMsg.SetDataElement(4, m_sTrxAmount.getBytes(), m_sTrxAmount.length());
        Log.i(TAG, "DE 4 [m_sTrxAmount]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        //7.local Transaction Date & time
        m_sTrxDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, "DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxDateTime.length());



        //11.STAN
        m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, "DE 11 [m_sSTAN]= " + m_sSTAN+"Length ="+m_sSTAN.length());

        //12. Local Transaction time and date
        m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, "DE 12 [m_sLocalTrxDateTime]= " + m_sLocalTrxDateTime+"Length ="+m_sTrxAmount.length());


        //14. Card Expiry Date
        if (m_enmTrxCardType == CardType.MANUAL) {
            if (m_sCardExpDate != null) {
                m_RequestISOMsg.SetDataElement(14, m_sCardExpDate.getBytes(), m_sCardExpDate.length());
                Log.i(TAG, "DE 14 [m_sCardExpDate]= " + m_sCardExpDate+"Length ="+m_sCardExpDate.length());
            }
        }

        //22.Pos Entry Mode
        GetDE22_POSEntryMode();
        m_RequestISOMsg.SetDataElement(22, m_sPOSEntryMode.getBytes(), m_sPOSEntryMode.length());
        Log.i(TAG, "DE 22 [m_sPOSEntryMode]= " + m_sPOSEntryMode+"Length ="+m_sPOSEntryMode.length());

        //23.Card Sequence Number
        if (m_enmTrxCardType == CardType.ICC | m_enmTrxCardType == CardType.CTLS ) {
            m_RequestISOMsg.SetDataElement(23, m_sCardSeqNum.getBytes(), m_sCardSeqNum.length());
            Log.i(TAG, "DE 23 [m_sCardSeqNum]= " + m_sCardSeqNum+"Length ="+m_sCardSeqNum.length());
        }

        //24. Function Code
        GetDE24_FunctionCode();
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, "DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        //25. Message reason Code
        GetDE25_Messagereasoncode();
        m_RequestISOMsg.SetDataElement(25, m_sMsgReasonCode.getBytes(), m_sMsgReasonCode.length());
        Log.i(TAG, "DE 25 [m_sMsgReasonCode]= " + m_sMsgReasonCode + "Length =" + m_sMsgReasonCode.length());

        //26. Card Acceptor Bussiness Code
        m_sCardAcceptorBusinessCode=PosApplication.getApp().oGTerminal_Operation_Data.sMerchant_Category_Code;
        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, "DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());

        //32. Acquirer institution ID
        m_sAquirerInsIDCode=PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID;
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, "DE 32 [m_sAquirerInsIDCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());

        //35.Track2 Data
        if ((m_enmTrxCardType == CardType.MAG) || (m_enmTrxCardType == CardType.ICC)) {

            m_RequestISOMsg.SetDataElement(35, m_sTrack2.getBytes(), m_sTrack2.length());
            Log.i(TAG, "DE 35 [m_sTrack2]= " + m_sTrack2+"Length ="+m_sTrack2.length());
        }

        //37. RRN
        GetDE_37_RRN();
        m_RequestISOMsg.SetDataElement(37, m_sRRNumber.getBytes(), m_sRRNumber.length());
        Log.i(TAG, "DE 37 [m_sRRNumber]= " + m_sRRNumber+"Length ="+m_sRRNumber.length());


        //41.Terminal ID
        m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, "DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        //42.Merchant ID
        m_sMerchantID=PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID;
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, "DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        //47.CardScheme Sponsor ID
        GetDE47_CardSchemeSponsorID();
        m_RequestISOMsg.SetDataElement(47, m_sCardSchemeSponsorID.getBytes(), m_sCardSchemeSponsorID.length());
        Log.i(TAG, "DE 47 [m_sCardSchemeSponsorID]= " + m_sCardSchemeSponsorID+"Length ="+m_sCardSchemeSponsorID.length());

        //49.Currency Code
        m_sCurrencyCode=PosApplication.getApp().oGTerminal_Operation_Data.m_sCurrencycode;
        m_RequestISOMsg.SetDataElement(49, m_sCurrencyCode.getBytes(), m_sCurrencyCode.length());
        Log.i(TAG, "DE 49 [m_sCurrencyCode]= " + m_sCurrencyCode+"Length ="+m_sCurrencyCode.length());


     /*   //52.PIN
        m_RequestISOMsg.SetDataElement(52, m_sTrxPIN.getBytes(), m_sTrxPIN.length());
        Log.i(TAG, "DE 52 [m_sTrxPIN]= " + m_sTrxPIN+"Length ="+m_sTrxPIN.length());
*/

        //53. Transaction Security control
        byte[]sec= DUKPT_KEY.getKSN();

        m_sTrxSecurityControl= BytesUtil.mergeBytes(sec,"609".getBytes());

        if(m_sTrxSecurityControl!=null)
        {
            m_RequestISOMsg.SetDataElement(53, m_sTrxSecurityControl, m_sTrxSecurityControl.length);
            Log.i(TAG, "DE 53 [m_sTrxSecurityControl]= " + m_sTrxSecurityControl + "Length =" + m_sTrxSecurityControl.length);
        }
        //54. Additional amounts
        if(TrxType==TranscationType.PURCHASE_WITH_NAQD) {
            m_RequestISOMsg.SetDataElement(54, m_sAdditionalAmount.getBytes(), m_sAdditionalAmount.length());
            Log.i(TAG, "DE 54 [m_sAdditionalAmount]= " + m_sAdditionalAmount + "Length =" + m_sAdditionalAmount.length());
        }
        //55. ICC related Data
        if (m_enmTrxCardType == CardType.ICC | m_enmTrxCardType == CardType.CTLS) {
            byte[] ICCdata= BCDASCII.fromASCIIToBCD(m_sICCRelatedTags,0,m_sICCRelatedTags.length(),true);
            m_RequestISOMsg.SetDataElement(55, ICCdata, ICCdata.length);
            Log.i(TAG, "DE 55 [m_sICCRelatedTags]= " + m_sICCRelatedTags + "Length =" + m_sICCRelatedTags.length());
        }
        //56.original Transaction data
        if(m_enmTrxType==TrxType.REFUND )
        {
            GetDE56_Original_TRX_Data();
        m_RequestISOMsg.SetDataElement(56, m_sOriginalTrxData.getBytes(), m_sOriginalTrxData.length());
        Log.i(TAG, "DE 56 [m_sOriginalTrxData]= " + m_sOriginalTrxData+"Length ="+m_sOriginalTrxData.length());
        }

        //59.Transaport Data
       /* if(m_sTransportData!=null)
        m_RequestISOMsg.SetDataElement(59, m_sTransportData.getBytes(), m_sTransportData.length());
        Log.i(TAG, "DE 59 [m_sTransportData]= " + m_sTransportData+"Length ="+m_sTransportData.length());*/

        //62.Terminal Status
        ComposeTerminalStatusData();
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, "DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());

        //64.Transaction Block
        ComposeMACBlockData(m_enmTrxType);
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


            m_RequestISOMsg.SetDataElement(2, m_sPAN.getBytes(), m_sPAN.length());
            Log.i(TAG, "DE 2 [m_sPAN]= " + m_sPAN+"Length ="+m_sPAN.length());



        //3.Set Processing Code
        GetDE03();
        m_RequestISOMsg.SetDataElement(3, m_sProcessCode.getBytes(), m_sProcessCode.length());
        Log.i(TAG, "DE 3 [m_sProcessCode]= " + m_sProcessCode+"Length ="+m_sProcessCode.length());


        //4. Amount
        m_RequestISOMsg.SetDataElement(4, m_sTrxAmount.getBytes(), m_sTrxAmount.length());
        Log.i(TAG, "DE 4 [m_sTrxAmount]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        //7.loacl Transaction Date & time
        m_sTrxDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, "DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxDateTime.length());



        m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, "DE 11 [m_sSTAN]= " + m_sTrxAmount+"Length ="+m_sSTAN.length());

        m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, "DE 12 [m_sLocalTrxDateTime]= " + m_sLocalTrxDateTime+"Length ="+m_sLocalTrxDateTime.length());



        if (m_enmTrxCardType == CardType.MANUAL) {
            if (m_sCardExpDate != null) {
                m_RequestISOMsg.SetDataElement(14, m_sCardExpDate.getBytes(), m_sCardExpDate.length());
                Log.i(TAG, "DE 14 [m_sCardExpDate]= " + m_sCardExpDate+"Length ="+m_sCardExpDate.length());
            }
        }

        GetDE22_POSEntryMode();
        m_RequestISOMsg.SetDataElement(22, m_sPOSEntryMode.getBytes(), m_sPOSEntryMode.length());
        Log.i(TAG, "DE 22 [m_sPOSEntryMode]= " + m_sPOSEntryMode+"Length ="+m_sPOSEntryMode.length());

        if (m_enmTrxCardType == CardType.ICC) {
            m_RequestISOMsg.SetDataElement(23, m_sCardSeqNum.getBytes(), m_sCardSeqNum.length());
            Log.i(TAG, "DE 23 [m_sCardSeqNum]= " + m_sCardSeqNum+"Length ="+m_sCardSeqNum.length());
        }

        GetDE24_FunctionCode();
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, "DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        GetDE25_Messagereasoncode();
        m_RequestISOMsg.SetDataElement(25, m_sMsgReasonCode.getBytes(), m_sMsgReasonCode.length());
        Log.i(TAG, "DE 25 [m_sMsgReasonCode]= " + m_sMsgReasonCode+"Length ="+m_sMsgReasonCode.length());

        m_sCardAcceptorBusinessCode=PosApplication.getApp().oGTerminal_Operation_Data.sMerchant_Category_Code;
        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, "DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());

        m_sAquirerInsIDCode=PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID;
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, "DE 32 [m_sAquirerInsIDCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());


        if ((m_enmTrxCardType == CardType.MAG) || (m_enmTrxCardType == CardType.ICC)) {

            m_RequestISOMsg.SetDataElement(35, m_sTrack2.getBytes(), m_sTrack2.length());
            Log.i(TAG, "DE 35 [m_sTrack2]= " + m_sTrack2+"Length ="+m_sTrack2.length());
        }

        GetDE_37_RRN();
        m_RequestISOMsg.SetDataElement(37, m_sRRNumber.getBytes(), m_sRRNumber.length());
        Log.i(TAG, "DE 37 [m_sRRNumber]= " + m_sRRNumber+"Length ="+m_sRRNumber.length());


        m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, "DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        m_sMerchantID=PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID;
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, "DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        GetDE47_CardSchemeSponsorID();
        m_RequestISOMsg.SetDataElement(47, m_sCardSchemeSponsorID.getBytes(), m_sCardSchemeSponsorID.length());
        Log.i(TAG, "DE 47 [m_sCardSchemeSponsorID]= " + m_sCardSchemeSponsorID+"Length ="+m_sCardSchemeSponsorID.length());

        m_sCurrencyCode=PosApplication.getApp().oGTerminal_Operation_Data.m_sCurrencycode;
        m_RequestISOMsg.SetDataElement(49, m_sCurrencyCode.getBytes(), m_sCurrencyCode.length());
        Log.i(TAG, "DE 49 [m_sCurrencyCode]= " + m_sCurrencyCode+"Length ="+m_sCurrencyCode.length());


        if(m_sTrxPIN != null) {
            m_RequestISOMsg.SetDataElement(52, m_sTrxPIN.getBytes(), m_sTrxPIN.length());
            Log.i(TAG, "DE 52 [m_sTrxPIN]= " + m_sTrxPIN + "Length =" + m_sTrxPIN.length());
        }


        byte[]sec= DUKPT_KEY.getKSN();

        m_sTrxSecurityControl= BytesUtil.mergeBytes(sec,"609".getBytes());

        if(m_sTrxSecurityControl!=null) {
            m_RequestISOMsg.SetDataElement(53,m_sTrxSecurityControl, m_sTrxSecurityControl.length);
            Log.i(TAG, "DE 53 [m_sTrxSecurityControl] = " + m_sTrxSecurityControl + " Length =" + m_sTrxSecurityControl.length);
        }
        if(PosApplication.getApp().oGPosTransaction.m_enmTrxType==TranscationType.PURCHASE_WITH_NAQD) {
            m_RequestISOMsg.SetDataElement(54, m_sAdditionalAmount.getBytes(), m_sAdditionalAmount.length());
            Log.i(TAG, "DE 54 [m_sAdditionalAmount]= " + m_sAdditionalAmount + "Length =" + m_sAdditionalAmount.length());
        }

        if (m_enmTrxCardType == CardType.ICC || m_enmTrxCardType == CardType.CTLS)
        {
                byte[] ICCdata= BCDASCII.fromASCIIToBCD(m_sICCRelatedTags,0,m_sICCRelatedTags.length(),true);
            m_RequestISOMsg.SetDataElement(55, ICCdata, ICCdata.length);
            Log.i(TAG, "DE 55 [m_sICCRelatedTags]= " + m_sICCRelatedTags + "Length =" + m_sICCRelatedTags.length());
        }


        if(m_enmTrxType==TranscationType.REFUND) {
            GetDE56_Original_TRX_Data();
            m_RequestISOMsg.SetDataElement(56, m_sOriginalTrxData.getBytes(), m_sOriginalTrxData.length());
            Log.i(TAG, "DE 56 [m_sOriginalTrxData]= " + m_sOriginalTrxData + "Length =" + m_sOriginalTrxData.length());
        }
/*

        if( m_sTransportData!=null )
        {
            m_RequestISOMsg.SetDataElement(59, m_sTransportData.getBytes(), m_sTransportData.length());
            Log.i(TAG, "DE 59 [m_sTransportData]= " + m_sTransportData + "Length =" + m_sTransportData.length());
        }
*/

        ComposeTerminalStatusData();
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, "DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());

        ComposeMACBlockData(m_enmTrxType);

        m_RequestISOMsg.SetDataElement(64,m_sTrxMACBlock.getBytes(), m_sTrxMACBlock.length());
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
        GetDE03();
        m_RequestISOMsg.SetDataElement(3, m_sProcessCode.getBytes(), m_sProcessCode.length());
        Log.i(TAG, " DE 3 [m_sProcessCode]= " + m_sProcessCode+"Length ="+m_sProcessCode.length());


        // Set Transaction Amount
        m_RequestISOMsg.SetDataElement(4, m_sTrxAmount.getBytes(), m_sTrxAmount.length());
        Log.i(TAG, " DE 4 [m_sTrxAmount]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        // Set Transmission Date and Time
        m_sTrxDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, " DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxAmount.length());


        // Set Transaction STAN
        m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, " DE 11 [m_sSTAN]= " + m_sSTAN+"Length ="+m_sSTAN.length());


        // Set Date & Time, Local Transaction
        m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();  //todo original transaction time
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, " DE 12 [m_sLocalTrxDateTime]= " + m_sLocalTrxDateTime+"Length ="+m_sLocalTrxDateTime.length());


        // Set  Date, Expiration if manual (new flag should be added to POSTRansaction class)
        if(m_enmTrxCardType == CardType.MANUAL)
        {
            m_RequestISOMsg.SetDataElement(14, m_sCardExpDate.getBytes(), m_sCardExpDate.length());
            Log.i(TAG, " DE 14 [m_sCardExpDate]= " + m_sCardExpDate+"Length ="+m_sCardExpDate.length());
        }

        // Set Point of Service Data Code
        GetDE22_POSEntryMode();
        m_RequestISOMsg.SetDataElement(22, m_sPOSEntryMode.getBytes(), m_sPOSEntryMode.length());
        Log.i(TAG, " DE 22 [m_sPOSEntryMode]= " + m_sPOSEntryMode+"Length ="+m_sPOSEntryMode.length());


        // Set Card Sequence Number
        if(m_enmTrxCardType == CardType.ICC || m_enmTrxCardType == CardType.CTLS )
        {
            m_RequestISOMsg.SetDataElement(23, m_sCardSeqNum.getBytes(), m_sCardSeqNum.length());
            Log.i(TAG, " DE 23 [m_sCardSeqNum]= " + m_sCardSeqNum+"Length ="+m_sCardSeqNum.length());
        }

        // Set Function Code (400)
        GetDE24_FunctionCode();
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, " DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        // Set Message Reason Code
        GetDE25_Messagereasoncode();
        m_RequestISOMsg.SetDataElement(25, m_sMsgReasonCode.getBytes(), m_sMsgReasonCode.length());
        Log.i(TAG, " DE 25 [m_sMsgReasonCode]= " + m_sMsgReasonCode+"Length ="+m_sMsgReasonCode.length());


        // Set Card Acceptor Business Code
        m_sCardAcceptorBusinessCode=PosApplication.getApp().oGTerminal_Operation_Data.sMerchant_Category_Code;
        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, " DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());


        /*Checking DE 30 when previus transaction is Authorizaiton */

        // Set Acquirer Institution Identification Code
        m_sAquirerInsIDCode=PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID;
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, " DE 32 [m_sCardAcceptorBusinessCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());

        // Set m_sTrack2
        if(m_enmTrxCardType == CardType.ICC || m_enmTrxCardType == CardType.MAG )
        {
            m_RequestISOMsg.SetDataElement(35, m_sTrack2.getBytes(), m_sTrack2.length());
            Log.i(TAG, " DE 35 [m_sTrack2]= " + m_sTrack2+"Length ="+m_sTrack2.length());
        }

        // Set Retrieval Reference Number
        GetDE_37_RRN();
        m_RequestISOMsg.SetDataElement(37, m_sRRNumber.getBytes(), m_sRRNumber.length());
        Log.i(TAG, " DE 37 [m_sCardAcceptorBusinessCode]= " + m_sRRNumber+"Length ="+m_sRRNumber.length());

        // Set Approval Code
        if(m_sApprovalCode != null)
        {
            m_RequestISOMsg.SetDataElement(38, m_sApprovalCode.getBytes(), m_sApprovalCode.length());
            Log.i(TAG, " DE 38 [m_sApprovalCode]= " + m_sApprovalCode+"Length ="+m_sApprovalCode.length());
        }

        // Set Terminal Identification
        m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, " DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        // Set Merchant ID
        m_sMerchantID=PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID;
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, " DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        // Set Card Scheme ID
        GetDE47_CardSchemeSponsorID();
        m_RequestISOMsg.SetDataElement(47, m_sCardSchemeSponsorID.getBytes(), m_sCardSchemeSponsorID.length());
        Log.i(TAG, " DE 47 [m_sCardSchemeSponsorID]= " + m_sCardSchemeSponsorID+"Length ="+m_sCardSchemeSponsorID.length());

        // Set Card Scheme ID(682)
        m_sCurrencyCode=PosApplication.getApp().oGTerminal_Operation_Data.m_sCurrencycode;
        m_RequestISOMsg.SetDataElement(49, m_sCurrencyCode.getBytes(), m_sCardSchemeSponsorID.length());
        Log.i(TAG, " DE 49 [m_sCurrencyCode]= " + m_sCurrencyCode+"Length ="+m_sCurrencyCode.length());


        // Set KSN
        byte[]sec= DUKPT_KEY.getKSN();

        m_sTrxSecurityControl= BytesUtil.mergeBytes(sec,"609".getBytes());

        if(m_sTrxSecurityControl!=null) {
            m_RequestISOMsg.SetDataElement(53, m_sTrxSecurityControl, m_sTrxSecurityControl.length);
            Log.i(TAG, " DE 53 [m_sTrxSecurityControl]= " + m_sTrxSecurityControl + "Length =" + m_sTrxSecurityControl.length);

        }
        // Set ICC/CTLS Tag
        if(m_enmTrxCardType == CardType.ICC || m_enmTrxCardType == CardType.CTLS )
        {byte[] ICCdata= BCDASCII.fromASCIIToBCD(m_sICCRelatedTags,0,m_sICCRelatedTags.length(),true);
            m_RequestISOMsg.SetDataElement(55, ICCdata, ICCdata.length);

            Log.i(TAG, " DE 55 [m_sICCRelatedTags]= " + m_sICCRelatedTags+"Length ="+m_sICCRelatedTags.length());

        }

        // Set Original Data Elements
        GetDE56_Original_TRX_Data();
        m_RequestISOMsg.SetDataElement(56, m_sOriginalTrxData.getBytes(), m_sOriginalTrxData.length());
        Log.i(TAG, " DE 56 [m_sOriginalTrxData]= " + m_sOriginalTrxData+"Length ="+m_sOriginalTrxData.length());

        // Set Terminal Status
        ComposeTerminalStatusData();
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, " DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());


        // Should implment function for Compose MAC bloc data
        // ComposeMACData(TrxType);

        // Set MAC
        ComposeMACBlockData(m_enmTrxType);
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
        m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
        m_RequestISOMsg.SetDataElement(2, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, "DE 2 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        // Set Transmission Date and Time
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, " DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxAmount.length());


        // Set Transaction STAN
        m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, " DE 11 [m_sSTAN]= " + m_sSTAN+"Length ="+m_sSTAN.length());


        // Set Date & Time, Local Transaction
        m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, " DE 12 [m_sLocalTrxDateTime]= " + m_sLocalTrxDateTime+"Length ="+m_sLocalTrxDateTime.length());

        // Set Function code
        GetDE24_FunctionCode();
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, " DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        // Set Card Acceptor Business Code
        m_sCardAcceptorBusinessCode=PosApplication.getApp().oGTerminal_Operation_Data.sMerchant_Category_Code;
        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, " DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());


        // Set Reconciliation date
        m_sReconDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(28, m_sReconDateTime.getBytes(), m_sReconDateTime.length());
        Log.i(TAG, " DE 28 [m_sReconDateTime]= " + m_sReconDateTime+"Length ="+m_sReconDateTime.length());


        // Set Acquirer Institution Identification Code
        m_sAquirerInsIDCode=PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID;
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, " DE 32 [m_sCardAcceptorBusinessCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());


        // Set Terminal Identification
        m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, " DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        // Set Merchant ID
        m_sMerchantID=PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID;
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, " DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        // Set reconciliation currency code(682)
        m_RequestISOMsg.SetDataElement(50, m_sReconCurrencyCode.getBytes(), m_sReconCurrencyCode.length());
        Log.i(TAG, " DE 50 [m_sReconCurrencyCode]= " + m_sReconCurrencyCode+"Length ="+m_sReconCurrencyCode.length());


        // Set KSN
        byte[]sec= DUKPT_KEY.getKSN();

        m_sTrxSecurityControl= BytesUtil.mergeBytes(sec,"609".getBytes());

        if(m_sTrxSecurityControl!=null) {
            m_RequestISOMsg.SetDataElement(53, m_sTrxSecurityControl, m_sTrxSecurityControl.length);
            Log.i(TAG, " DE 53 [m_sTrxSecurityControl]= " + m_sTrxSecurityControl + "Length =" + m_sTrxSecurityControl.length);
        }

        // Set Terminal Status
        ComposeTerminalStatusData();
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, " DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());

        //Set MADA POS Terminal Reconciliation Totals
        m_sReconciliationTotals=ComposeReconciliationTotals(PosApplication.getApp().oGTerminal_Operation_Data.g_NumberOfCardSchemes,PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals);
        m_RequestISOMsg.SetDataElement(124, m_sReconciliationTotals.getBytes(), m_sReconciliationTotals.length());
        Log.i(TAG, " DE 124 [m_sReconciliationTotals]= " + m_sReconciliationTotals+"Length ="+m_sReconciliationTotals.length());

        //MAC
        ComposeMACBlockData(m_enmTrxType);
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
        m_sTrxDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, " DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxDateTime.length());


        // Set Transaction STAN
        m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, " DE 11 [m_sSTAN]= " + m_sSTAN+"Length ="+m_sSTAN.length());


        // Set Date & Time, Local Transaction
        m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, " DE 12 [m_sLocalTrxDateTime]= " + m_sLocalTrxDateTime+"Length ="+m_sLocalTrxDateTime.length());

        // Set Function code
        GetDE24_FunctionCode();
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
        ComposeTerminalStatusData();
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
        Log.i(TAG, " DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxDateTime.length());


        // Set Transaction STAN
        m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, " DE 11 [m_sSTAN]= " + m_sSTAN+"Length ="+m_sSTAN.length());


        // Set Date & Time, Local Transaction
        m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, " DE 12 [m_sLocalTrxDateTime]= " + m_sLocalTrxDateTime+"Length ="+m_sLocalTrxDateTime.length());

        // Set Function code
        GetDE24_FunctionCode();
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, " DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        // Set Acquirer Institution Identification Code
        m_sAquirerInsIDCode=PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID;
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, " DE 32 [m_sAquirerInsIDCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());



        // Set Terminal Identification
        m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, " DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        // Set Merchant ID
        m_sMerchantID=PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID;
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, " DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        // Set KSN
        byte[]sec= DUKPT_KEY.getKSN();

        m_sTrxSecurityControl= BytesUtil.mergeBytes(sec,"609".getBytes());

        if(m_sTrxSecurityControl!=null) {
            m_RequestISOMsg.SetDataElement(53, m_sTrxSecurityControl, m_sTrxSecurityControl.length);
            Log.i(TAG, " DE 53 [m_sTrxSecurityControl]= " + m_sTrxSecurityControl + "Length =" + m_sTrxSecurityControl.length);
        }

        // Set Terminal Status
        ComposeTerminalStatusData();
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, " DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());

        // Set Data Record
        m_sDataRecord72=PosApplication.getApp().oGTerminal_Operation_Data.m_sTMSHeader;
        m_RequestISOMsg.SetDataElement(72, m_sDataRecord72.getBytes(), m_sDataRecord72.length());
        Log.i(TAG, " DE 72 [m_sDataRecord72]= " + m_sDataRecord72+"Length ="+m_sDataRecord72.length());

        //MAC
        ComposeMACBlockData(m_enmTrxType);
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
        m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, " DE 11 [m_sSTAN]= " + m_sSTAN+"Length ="+m_sSTAN.length());


        // Set Date & Time, Local Transaction
        m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(12, m_sLocalTrxDateTime.getBytes(), m_sLocalTrxDateTime.length());
        Log.i(TAG, " DE 12 [m_sLocalTrxDateTime]= " + m_sLocalTrxDateTime+"Length ="+m_sLocalTrxDateTime.length());

        // Set Function code
        GetDE24_FunctionCode();
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, " DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());



        // Set Acquirer Institution Identification Code
        m_sAquirerInsIDCode=PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID;
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
        m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
        if(m_sTerminalID!=null) {
            m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
            Log.i(TAG, " DE 41 [m_sTerminalID]= " + m_sTerminalID + "Length =" + m_sTerminalID.length());
        }
        // Set Merchant ID
        m_sMerchantID=PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID;
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
    public int    ComposeFinancialAdviseMessage(TranscationType TrxType)
    {

        //Clear  Message
        m_RequestISOMsg.ClearFields();

        //1. Set Financial Advice MTI
        m_RequestISOMsg.SetMTI(PosApplication.MTI_Financial_Transaction_Advice);



        //2. Set PAN
           m_RequestISOMsg.SetDataElement(2, m_sPAN.getBytes(), m_sPAN.length());
            Log.i(TAG, "DE 2 [m_sPAN]= " + m_sPAN+"Length ="+m_sPAN.length());


        //3.Set Processing Code
        GetDE03();
        m_RequestISOMsg.SetDataElement(3, m_sProcessCode.getBytes(), m_sProcessCode.length());
        Log.i(TAG, "DE 3 [m_sProcessCode]= " + m_sProcessCode+"Length ="+m_sProcessCode.length());


        //4.Set Amount
        m_RequestISOMsg.SetDataElement(4, m_sTrxAmount.getBytes(), m_sTrxAmount.length());
        Log.i(TAG, "DE 4 [m_sTrxAmount]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        //7.local Transaction Date & time
        m_sTrxDateTime=ExtraUtil.GetDate_Time();
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, "DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxDateTime.length());



        //11.STAN
        m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, "DE 11 [m_sSTAN]= " + m_sTrxAmount+"Length ="+m_sSTAN.length());

        //12. Local Transaction time and date
        m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
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
        GetDE22_POSEntryMode();
        m_RequestISOMsg.SetDataElement(22, m_sPOSEntryMode.getBytes(), m_sPOSEntryMode.length());
        Log.i(TAG, "DE 22 [m_sPOSEntryMode]= " + m_sPOSEntryMode+"Length ="+m_sPOSEntryMode.length());

        //23.Card Sequence Number
        if (m_enmTrxCardType == CardType.ICC) {
            m_RequestISOMsg.SetDataElement(23, m_sCardSeqNum.getBytes(), m_sCardSeqNum.length());
            Log.i(TAG, "DE 23 [m_sCardSeqNum]= " + m_sCardSeqNum+"Length ="+m_sCardSeqNum.length());
        }

        //24. Function Code
        GetDE24_FunctionCode();
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, "DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        //25. Message reason Code
        GetDE25_Messagereasoncode();
        m_RequestISOMsg.SetDataElement(25, m_sMsgReasonCode.getBytes(), m_sMsgReasonCode.length());
        Log.i(TAG, "DE 25 [m_sMsgReasonCode]= " + m_sMsgReasonCode+"Length ="+m_sMsgReasonCode.length());


        //26. Card Acceptor Bussiness Code
        m_sCardAcceptorBusinessCode=PosApplication.getApp().oGTerminal_Operation_Data.sMerchant_Category_Code;
        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, "DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());

        //30.Original amount
        if(m_sOrigAmount!=null)
        {
            m_RequestISOMsg.SetDataElement(30, m_sOrigAmount.getBytes(), m_sOrigAmount.length());
            Log.i(TAG, "DE 30 [m_sOrigAmount]= " + m_sOrigAmount+"Length ="+m_sOrigAmount.length());

        }
        //32. Acquirer institution ID
        m_sAquirerInsIDCode=PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID;
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, "DE 32 [m_sAquirerInsIDCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());

        //35.Track2 Data
        if ((m_enmTrxCardType == CardType.MAG) || (m_enmTrxCardType == CardType.ICC)) {

            m_RequestISOMsg.SetDataElement(35, m_sTrack2.getBytes(), m_sTrack2.length());
            Log.i(TAG, "DE 35 [m_sTrack2]= " + m_sTrack2+"Length ="+m_sTrack2.length());
        }

        //37. RRN
        GetDE_37_RRN();
        m_RequestISOMsg.SetDataElement(37, m_sRRNumber.getBytes(), m_sRRNumber.length());
        Log.i(TAG, "DE 37 [m_sRRNumber]= " + m_sRRNumber+"Length ="+m_sRRNumber.length());


        //38.Approval code
        if(m_enmTrxType!=TrxType.REFUND) //todo other transactions if not exist
        {
            m_RequestISOMsg.SetDataElement(38, m_sApprovalCode.getBytes(), m_sApprovalCode.length());
            Log.i(TAG, "DE 38 [m_sApprovalCode]= " + m_sApprovalCode + "Length =" + m_sApprovalCode.length());
        }
        //39.action code

        m_RequestISOMsg.SetDataElement(39, m_sActionCode.getBytes(), m_sActionCode.length());
        Log.i(TAG, "DE 41 [m_sTerminalID]= " + m_sActionCode+"Length ="+m_sActionCode.length());

        //41.Terminal ID
        m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, "DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        //42.Merchant ID
        m_sMerchantID=PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID;
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, "DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        //47.CardScheme Sponsor ID
        GetDE47_CardSchemeSponsorID();
        m_RequestISOMsg.SetDataElement(47, m_sCardSchemeSponsorID.getBytes(), m_sCardSchemeSponsorID.length());
        Log.i(TAG, "DE 47 [m_sCardSchemeSponsorID]= " + m_sCardSchemeSponsorID+"Length ="+m_sCardSchemeSponsorID.length());

        //49.Currency Code
        m_sCurrencyCode=PosApplication.getApp().oGTerminal_Operation_Data.m_sCurrencycode;
        m_RequestISOMsg.SetDataElement(49, m_sCurrencyCode.getBytes(), m_sCurrencyCode.length());
        Log.i(TAG, "DE 49 [m_sCurrencyCode]= " + m_sCurrencyCode+"Length ="+m_sCurrencyCode.length());


        //52.PIN
        if(m_enmTrxCVM==CVM.ONLINE_PIN) {
            m_RequestISOMsg.SetDataElement(52, m_sTrxPIN.getBytes(), m_sTrxPIN.length());
            Log.i(TAG, "DE 52 [m_sTrxPIN]= " + m_sTrxPIN + "Length =" + m_sTrxPIN.length());
        }

        //53. Transaction Security control
        byte[]sec= DUKPT_KEY.getKSN();

        m_sTrxSecurityControl= BytesUtil.mergeBytes(sec,"609".getBytes());

        if(m_sTrxSecurityControl!=null) {
            m_RequestISOMsg.SetDataElement(53, m_sTrxSecurityControl, m_sTrxSecurityControl.length);
            Log.i(TAG, "DE 53 [m_sTrxSecurityControl]= " + m_sTrxSecurityControl + "Length =" + m_sTrxSecurityControl.length);
        }
        //54. Additional amounts
        m_RequestISOMsg.SetDataElement(54, m_sAdditionalAmount.getBytes(), m_sAdditionalAmount.length());
        Log.i(TAG, "DE 54 [m_sAdditionalAmount]= " + m_sAdditionalAmount+"Length ="+m_sAdditionalAmount.length());

        //55. ICC related Data
        if (m_enmTrxCardType == CardType.ICC || m_enmTrxCardType == CardType.CTLS ) {
            byte[] ICCdata = BCDASCII.fromASCIIToBCD(m_sICCRelatedTags, 0, m_sICCRelatedTags.length(), true);
            m_RequestISOMsg.SetDataElement(55, ICCdata, ICCdata.length);
            Log.i(TAG, "DE 55 [m_sICCRelatedTags]= " + m_sICCRelatedTags + "Length =" + m_sICCRelatedTags.length());
        }
        //56.original Transaction data
        if(m_enmTrxType==TrxType.REFUND ||m_enmTrxType==TrxType.PURCHASE_ADVICE)
        {
            GetDE56_Original_TRX_Data();
            m_RequestISOMsg.SetDataElement(56, m_sOriginalTrxData.getBytes(), m_sOriginalTrxData.length());
            Log.i(TAG, "DE 56 [m_sOriginalTrxData]= " + m_sOriginalTrxData+"Length ="+m_sOriginalTrxData.length());
        }

     /*   //59.Transaport Data
        m_RequestISOMsg.SetDataElement(59, m_sTransportData.getBytes(), m_sTransportData.length());
        Log.i(TAG, "DE 59 [m_sTransportData]= " + m_sTransportData+"Length ="+m_sTransportData.length());
*/
        //62.Terminal Status
        ComposeTerminalStatusData();
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, "DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());

        //64.Transaction Block
        ComposeMACBlockData(m_enmTrxType);
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
        GetDE03();
        m_RequestISOMsg.SetDataElement(3, m_sProcessCode.getBytes(), m_sProcessCode.length());
        Log.i(TAG, "DE 3 [m_sProcessCode]= " + m_sProcessCode+"Length ="+m_sProcessCode.length());


        //4. Amount
        m_RequestISOMsg.SetDataElement(4, m_sTrxAmount.getBytes(), m_sTrxAmount.length());
        Log.i(TAG, "DE 4 [m_sTrxAmount]= " + m_sTrxAmount+"Length ="+m_sTrxAmount.length());


        //7.local Transaction Date & time
        m_RequestISOMsg.SetDataElement(7, m_sTrxDateTime.getBytes(), m_sTrxDateTime.length());
        Log.i(TAG, "DE 7 [m_sTrxDateTime]= " + m_sTrxDateTime+"Length ="+m_sTrxDateTime.length());



        //11.STAN
        m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
        m_RequestISOMsg.SetDataElement(11, m_sSTAN.getBytes(), m_sSTAN.length());
        Log.i(TAG, "DE 11 [m_sSTAN]= " + m_sTrxAmount+"Length ="+m_sSTAN.length());

        //12. Local Transaction time and date
        m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
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
        GetDE22_POSEntryMode();
        m_RequestISOMsg.SetDataElement(22, m_sPOSEntryMode.getBytes(), m_sPOSEntryMode.length());
        Log.i(TAG, "DE 22 [m_sPOSEntryMode]= " + m_sPOSEntryMode+"Length ="+m_sPOSEntryMode.length());

        //23.Card Sequence Number
        if (m_enmTrxCardType == CardType.ICC) {
            m_RequestISOMsg.SetDataElement(23, m_sCardSeqNum.getBytes(), m_sCardSeqNum.length());
            Log.i(TAG, "DE 23 [m_sCardSeqNum]= " + m_sCardSeqNum+"Length ="+m_sCardSeqNum.length());
        }

        //24. Function Code
        GetDE24_FunctionCode();
        m_RequestISOMsg.SetDataElement(24, m_sFunctionCode.getBytes(), m_sFunctionCode.length());
        Log.i(TAG, "DE 24 [m_sFunctionCode]= " + m_sFunctionCode+"Length ="+m_sFunctionCode.length());


        //25. Message reason Code
        GetDE25_Messagereasoncode();
        m_RequestISOMsg.SetDataElement(25, m_sMsgReasonCode.getBytes(), m_sMsgReasonCode.length());
        Log.i(TAG, "DE 25 [m_sMsgReasonCode]= " + m_sMsgReasonCode+"Length ="+m_sMsgReasonCode.length());


        //26. Card Acceptor Bussiness Code
        m_sCardAcceptorBusinessCode=PosApplication.getApp().oGTerminal_Operation_Data.sMerchant_Category_Code;
        m_RequestISOMsg.SetDataElement(26, m_sCardAcceptorBusinessCode.getBytes(), m_sCardAcceptorBusinessCode.length());
        Log.i(TAG, "DE 26 [m_sCardAcceptorBusinessCode]= " + m_sCardAcceptorBusinessCode+"Length ="+m_sCardAcceptorBusinessCode.length());

        //30.Original amount
        if(m_sOrigAmount!=null)
        {
            m_RequestISOMsg.SetDataElement(26, m_sOrigAmount.getBytes(), m_sOrigAmount.length());
            Log.i(TAG, "DE 30 [m_sOrigAmount]= " + m_sOrigAmount+"Length ="+m_sOrigAmount.length());

        }
        //32. Acquirer institution ID
        m_sAquirerInsIDCode=PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID;
        m_RequestISOMsg.SetDataElement(32, m_sAquirerInsIDCode.getBytes(), m_sAquirerInsIDCode.length());
        Log.i(TAG, "DE 32 [m_sAquirerInsIDCode]= " + m_sAquirerInsIDCode+"Length ="+m_sAquirerInsIDCode.length());

        //35.Track2 Data
        if ((m_enmTrxCardType == CardType.MAG) || (m_enmTrxCardType == CardType.ICC)) {

            m_RequestISOMsg.SetDataElement(35, m_sTrack2.getBytes(), m_sTrack2.length());
            Log.i(TAG, "DE 35 [m_sTrack2]= " + m_sTrack2+"Length ="+m_sTrack2.length());
        }

        //37. RRN
        GetDE_37_RRN();
        m_RequestISOMsg.SetDataElement(37, m_sRRNumber.getBytes(), m_sRRNumber.length());
        Log.i(TAG, "DE 37 [m_sRRNumber]= " + m_sRRNumber+"Length ="+m_sRRNumber.length());


        //38.Approval code
        m_RequestISOMsg.SetDataElement(38, m_sApprovalCode.getBytes(), m_sApprovalCode.length());
        Log.i(TAG, "DE 38 [m_sApprovalCode]= " + m_sApprovalCode+"Length ="+m_sApprovalCode.length());

        //39.action code
        GetDE39_Actioncode();
        m_RequestISOMsg.SetDataElement(39, m_sActionCode.getBytes(), m_sActionCode.length());
        Log.i(TAG, "DE 41 [m_sTerminalID]= " + m_sActionCode+"Length ="+m_sActionCode.length());


        //41.Terminal ID
        m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
        m_RequestISOMsg.SetDataElement(41, m_sTerminalID.getBytes(), m_sTerminalID.length());
        Log.i(TAG, "DE 41 [m_sTerminalID]= " + m_sTerminalID+"Length ="+m_sTerminalID.length());

        //42.Merchant ID
        m_sMerchantID=PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID;
        m_RequestISOMsg.SetDataElement(42, m_sMerchantID.getBytes(), m_sMerchantID.length());
        Log.i(TAG, "DE 42 [m_sMerchantID]= " + m_sMerchantID+"Length ="+m_sMerchantID.length());

        //47.CardScheme Sponsor ID
        GetDE47_CardSchemeSponsorID();
        m_RequestISOMsg.SetDataElement(47, m_sCardSchemeSponsorID.getBytes(), m_sCardSchemeSponsorID.length());
        Log.i(TAG, "DE 47 [m_sCardSchemeSponsorID]= " + m_sCardSchemeSponsorID+"Length ="+m_sCardSchemeSponsorID.length());

        //49.Currency Code
        m_sCurrencyCode=PosApplication.getApp().oGTerminal_Operation_Data.m_sCurrencycode;
        m_RequestISOMsg.SetDataElement(49, m_sCurrencyCode.getBytes(), m_sCurrencyCode.length());
        Log.i(TAG, "DE 49 [m_sCurrencyCode]= " + m_sCurrencyCode+"Length ="+m_sCurrencyCode.length());


        //52.PIN
        if(m_enmTrxCVM==CVM.ONLINE_PIN) {
            m_RequestISOMsg.SetDataElement(52, m_sTrxPIN.getBytes(), m_sTrxPIN.length());
            Log.i(TAG, "DE 52 [m_sTrxPIN]= " + m_sTrxPIN + "Length =" + m_sTrxPIN.length());
        }

        //53. Transaction Security control
        byte[]sec= DUKPT_KEY.getKSN();

        m_sTrxSecurityControl= BytesUtil.mergeBytes(sec,"609".getBytes());

        if(m_sTrxSecurityControl!=null) {
            m_RequestISOMsg.SetDataElement(53, m_sTrxSecurityControl, m_sTrxSecurityControl.length);
            Log.i(TAG, "DE 53 [m_sTrxSecurityControl]= " + m_sTrxSecurityControl + "Length =" + m_sTrxSecurityControl);
        }

        //55. ICC related Data
        if (m_enmTrxCardType == CardType.ICC || m_enmTrxCardType == CardType.CTLS) {
            byte[] ICCdata = BCDASCII.fromASCIIToBCD(m_sICCRelatedTags, 0, m_sICCRelatedTags.length(), true);
            m_RequestISOMsg.SetDataElement(55, ICCdata, ICCdata.length);
            Log.i(TAG, "DE 55 [m_sICCRelatedTags]= " + m_sICCRelatedTags + "Length =" + m_sICCRelatedTags.length());
        }
        //56.original Transaction data
        GetDE56_Original_TRX_Data();
        m_RequestISOMsg.SetDataElement(56, m_sOriginalTrxData.getBytes(), m_sOriginalTrxData.length());
        Log.i(TAG, "DE 56 [m_sOriginalTrxData]= " + m_sOriginalTrxData+"Length ="+m_sOriginalTrxData.length());


     /*   //59.Transaport Data
        m_RequestISOMsg.SetDataElement(59, m_sTransportData.getBytes(), m_sTransportData.length());
        Log.i(TAG, "DE 59 [m_sTransportData]= " + m_sTransportData+"Length ="+m_sTransportData.length());
*/
        //62.Terminal Status
        ComposeTerminalStatusData();
        m_RequestISOMsg.SetDataElement(62, m_sTerminalStatus.getBytes(), m_sTerminalStatus.length());
        Log.i(TAG, "DE 62 [m_sTerminalStatus]= " + m_sTerminalStatus+"Length ="+m_sTerminalStatus.length());

        //64.Transaction Block
        ComposeMACBlockData(m_enmTrxType);

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
            m_sTerminalStatus="01"+PosApplication.getApp().oGTerminal_Operation_Data.terminal_dial_indicator; // '1'-9', 'A' - 'F' (i.e. total range is '1'-'F').
        //Printer Status
            m_sTerminalStatus.concat("02"+PosApplication.getApp().oGTerminal_Operation_Data.Printer_Status);  //'0' = No printer. '1' = Out of paper. '2' = Plain paper receipt.
        //Idle Time
            m_sTerminalStatus.concat("03"+PosApplication.getApp().oGTerminal_Operation_Data.Idle_Time);  //hhmmss
        //Magnetic Reader Status
            m_sTerminalStatus.concat("04"+PosApplication.getApp().oGTerminal_Operation_Data.Magnetic_Reader_Status);// '0'=Okay. '1' = Out of order.
        //Chip Card Reader Status
            m_sTerminalStatus.concat("05"+PosApplication.getApp().oGTerminal_Operation_Data.Chip_Card_Reader_Status);// '0'=Okay. '1' = Out of order.

        //GPS Location Coordinates
            m_sTerminalStatus.concat("07"+PosApplication.getApp().oGTerminal_Operation_Data.GPS_Location_Coordinates);// ANNNNNNANNNNNNN e.g. N402646W0795856 Which represents, N 40° 26′ 46″ W 079° 58′ 56″

        //Contactless Reader Status
            m_sTerminalStatus.concat("09"+PosApplication.getApp().oGTerminal_Operation_Data.Contactless_Reader_Status);// '0'=Okay. '1' = Out of order. '9' = Not supported.

        //Connection Start Time
            m_sTerminalStatus.concat("10"+PosApplication.getApp().oGTerminal_Operation_Data.Connection_Start_Time); //HHMMSSsss As 24 hour clock

        //Connection End Time
            m_sTerminalStatus.concat("11"+PosApplication.getApp().oGTerminal_Operation_Data.Connection_End_Time ); //HHMMSSsss As 24 hour clock

        //Request Sent Time
            m_sTerminalStatus.concat("12"+PosApplication.getApp().oGTerminal_Operation_Data.Request_Sent_Time ); //HHMMSSsss As 24 hour clock

        //Response Received Time
            m_sTerminalStatus.concat("13"+PosApplication.getApp().oGTerminal_Operation_Data.Response_Received_Time ); //HHMMSSsss As 24 hour clock
        //Performance Timers Reference
            m_sTerminalStatus.concat("14"+PosApplication.getApp().oGTerminal_Operation_Data.Performance_Timers_Reference); //original RRN for the online authorization or financial message for which the timers refer to.
        //mada EFTPOS specification release version
            //todo getversion details
            //getversion()
            m_sTerminalStatus.concat("15"+PosApplication.getApp().oGTerminal_Operation_Data.mada_EFTPOS_specification_release_version ); //The POS should send the version number without dots, and with 2 digits each with a leading zero, if applicable, for the Major, Minor and Patch specification version numbers i.e. Version 6.0.3 should be expressed as 060003 and Version 10.2.0 should be expressed as 100200
        //Connection Details
            //todo getting connection detail
            //getconnectiondetail()
            m_sTerminalStatus.concat("16"+PosApplication.getApp().oGTerminal_Operation_Data.Connection_Details); //Connection Priority ‘01’ Primary ‘02’ Secondary,   Network Service Provider (NSP) ‘01’ iNET ‘02’ Mobily ‘03’ Zain ‘04’ Sky Band ‘05’ Geidea ,   Provider ‘01’ STC ‘02’ Mobily ‘03’ Zain ‘04’ Sky Band  , Connection Method ‘01’ Dial-up ‘02’ SIM ‘03’ TCP/IP ‘04’ VSAT ‘05’ DSL ‘06’ WiFi


/*
        ///for response
        //Terminal Online Flag
            m_sTerminalStatus.concat("06"+PosApplication.getApp().oGTerminal_Operation_Data.Terminal_Online_Flag); // '0'=No action. '1' = Go on-line.
        //Force Reconciliation Flag
            m_sTerminalStatus.concat("08"+PosApplication.getApp().oGTerminal_Operation_Data.Force_Reconciliation_Flag);// '0'=No action. '1' = Go on-line.*/

    }

    /** for reference
     * \Function Name: GetDE03
     * \Param  : void
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 6/10/2020
     * \Des    : for getting PROCESSING CODE to be used in message request
     */

    public void GetDE03()
    {

/*
        mada Chip Card:
        000000 Purchase from Default Account
        090000 Purchase with Cash Back from Default Account
        200000 Refund to Default Account
        500000 Bill / Fee Payment from Default Account
        mada Magnetic Stripe or Contactless Card:
        000000 Purchase from Default Account
        200000 Refund to Default Account
        500000 Bill / Fee Payment from Default Account
        GCCNET:
        000000 Purchase from Default Account
        200000 Refund to Default Account
        500000 Bill / Fee Payment from Default Account
        Non-mada (IBCS Chip Card:
        000000 Purchase from Default Account
        003000 Purchase from Credit Card Account
        013000 Cash Advance from Credit Card Account
        200000 Refund to Default Account
        203000 Refund to Credit Card Account
        500000 Bill / Fee Payment from Default Account
        903000 Authorisation Only from Credit Card Account
        Non-mada (IBCS) Magnetic Stripe or Contactless Card:
        000000 Purchase from Default Account
        003000 Purchase from Credit Card Account
        013000 Cash Advance from Credit Card Account
        200000 Refund to Default Account
        203000 Refund to Credit Card Account
        500000 Bill / Fee Payment from Default Account
        903000 Authorisation Only from Credit Card Account
*/

        if(m_card_scheme.m_sCard_Scheme_ID=="P1") {
            switch (m_enmTrxType) {
                case PURCHASE:
                case PURCHASE_ADVICE:

                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="000000";
                    break;
                case PURCHASE_WITH_NAQD:
                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="090000";
                break;
                case AUTHORISATION_ADVICE:
                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="220000";
                    break;

                case REFUND:
                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="200000";
                    break;
                case AUTHORISATION:
                case AUTHORISATION_EXTENSION:
                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="900000"; //check credit account
                    break;
                case AUTHORISATION_VOID:
                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="220000";
                    break;
                case REVERSAL:
                    //original processing code and should not alter the data
                    break;
                case RECONCILIATION:

                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="500000";
                    break;
                case CASH_ADVANCE:
                    switch(m_card_scheme.m_sCard_Scheme_ID)
                    {
                        case"P1":
                            PosApplication.getApp().oGPosTransaction.m_sProcessCode="010000";
                            break;
                        default:
                    }

            }
        }
        else {    // non mada card
            switch (m_enmTrxType) {
                case PURCHASE:
                case PURCHASE_ADVICE:
                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="000000";

                    //todo purchase with criedit card account
                    // PosApplication.getApp().oGPosTransaction.m_sProcessCode="003000";

                    break;

                case PURCHASE_WITH_NAQD:
                case REFUND:
                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="200000";
                    //todo refund with criedit card account
                    // PosApplication.getApp().oGPosTransaction.m_sProcessCode="203000";
                    break;
                case AUTHORISATION:
                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="900000";
                    //todo authorization with criedit card account
                    // PosApplication.getApp().oGPosTransaction.m_sProcessCode="903000";
                    break;
                case AUTHORISATION_EXTENSION:
                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="900000";
                    break;
                case AUTHORISATION_ADVICE:
                case AUTHORISATION_VOID:
                case REVERSAL:
                    //todo original processing code
                    //PosApplication.getApp().oGPosTransaction.m_sProcessCode=;
                case RECONCILIATION:
                case CASH_ADVANCE:
                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="013000";
                    break;

                case SADAD_BILL:
                    PosApplication.getApp().oGPosTransaction.m_sProcessCode="500000";
                    break;


            }
        }
    }


    /** for reference
     * \Function Name: GetDE22
     * \Param  : void
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 5/28/2020
     * \Des    : for getting entry mode data element 22
     */

    //Position 1 – Card data input capability (Indicates the primary means of getting the information on the card into the terminal)
     enum Card_data_input_capability{PAN_Entry_Mode_Unknown ,
        Manual_no_terminal  ,
        Magnetic_stripe_read ,
        Bar_Code    ,
        OCR     ,
        ICC ,
        Key_Entered ,
        Contactless }
    //Position 2 – Cardholder authentication capability (Indicates the primary means of verifying the cardholder at this terminal.)
    enum Cardholder_authentication_capability{No_electronic_authentication,
        PIN,
        Electronic_signature_analysis,
        Biometrics,
        Biographic,
        Electronic_authentication_inoperative,
        other,

    }
    //Position 3 – Card capture capability (Indicates whether or not the terminal has the ability to capture a card) which always 0

    // Position 4 – Operating environment  (Indicates if the terminal is attended by the card acceptor at its location)
    enum Operating_environment {
        On_premises_of_card_acceptor_attended,
        On_premises_of_card_acceptor_unattended,
        Off_premises_of_card_acceptor_attended,
        Off_premises_of_card_acceptor_unattended
    }
    //Position 5 – Cardholder present
    enum Cardholder_present{
        Cardholder_present,
        Cardholder_not_present_unspecified
    }
    //Position 6 – Card present
    enum Card_present{
        Card_not_present,
        Card_present
    }

    // Position 7 – Card data input mode (Indicates method used to input the information from the card to the terminal)
    enum Card_data_input_mode {
        Unspecified,
        Manual_no_terminal,
        Magnetic_stripe_read,
        Bar_Code,
        OCR,
        ICC,
        Key_entered,
        Contactless
    }
    //Position 8 – Cardholder authentication method (Indicates the method for verifying the cardholder identity)
    enum Cardholder_authentication_method {
        Not_authenticated,
        PIN,
        Electronic_signature_analysis,
        Biometrics,
        Biographic,
        Manual_signature_analysis,
        Other_manual_verifications
    }
    //Position 9 – Cardholder authentication entity  (Indicates the entity verifying the cardholder identity)
    enum Cardholder_authentication_entity{
        Not_authenticated,
        ICC,
        CAD,
        Authorising_agent,
        By_merchant,
        other
    }
    //Position 10 – Card data output capability (Indicates the ability of the terminal to update the card)
    enum Card_data_output_capability
    {
        Unknown,
        None,
        Magnetic_Stripe_write,
        ICC
    }
    //Position 11 – Terminal output capability (Indicates the ability of the terminal to print/display messages)
    enum Terminal_output_capability{
        Unknown,
        None,
        Printing,
        Display,
        Printing_and_display
    }
    //Position 12 – PIN capture capability (Indicates the length of PIN which the terminal is capable of capturing)
    enum PIN_capture_capability{
        No_PIN_capture_capability,
        Device_PIN_capture_capability_unknown,
        Four_characters,
        Five_characters,
        Six_characters,
        Seven_characters,
        Eight_characters,
        Nine_characters,
        Ten_characters,
        Eleven_characters,
        Twelve_characters
    }


    public void GetDE22_POSEntryMode()
    {   Card_data_input_capability cdic = null;
        Cardholder_authentication_capability cac=null;
        //Byte Card_capture_capability;added hard coded
        Operating_environment oe=null;
        Cardholder_present Cardholder_p=null;
        Card_present Card_p=null;
        Card_data_input_mode cdim=null;
        Cardholder_authentication_method cam=null;
        Cardholder_authentication_entity cae=null;
        Card_data_output_capability coc=null;
        Terminal_output_capability toc=null;
        PIN_capture_capability pcc=null;

        char[] bDE22=new char[12];
    if(m_enmTrxCardType==CardType.ICC)
    {    cdic= Card_data_input_capability.ICC;
         oe= Operating_environment.On_premises_of_card_acceptor_attended;
        Cardholder_p= Cardholder_present.Cardholder_present;
        Card_p=Card_present.Card_present;
        cdim=Card_data_input_mode.ICC;
        coc = Card_data_output_capability.ICC;
        toc = Terminal_output_capability.Printing_and_display;   // todo if terminal type is minipos Terminal_output_capability.display;
        pcc = PIN_capture_capability.Twelve_characters;


        if (m_enmTrxCVM==CVM.ONLINE_PIN)
        {
            cac=Cardholder_authentication_capability.PIN;
            cam=Cardholder_authentication_method.PIN;
            cae=Cardholder_authentication_entity.Authorising_agent;
        }

        else if(m_enmTrxCVM==CVM.OFFLINE_PIN)
        {
            cac=Cardholder_authentication_capability.PIN;
            cam=Cardholder_authentication_method.PIN;
            cae=Cardholder_authentication_entity.ICC;
        }
        else if(m_enmTrxCVM==CVM.SIGNATURE)
        {
            cac=Cardholder_authentication_capability.other;
            cam=Cardholder_authentication_method.Manual_signature_analysis;
            cae=Cardholder_authentication_entity.By_merchant;
        }
        else if(m_enmTrxCVM==CVM.NO_CVM)
        {
            cac=Cardholder_authentication_capability.No_electronic_authentication;
            cam=Cardholder_authentication_method.Not_authenticated;
            cae=Cardholder_authentication_entity.Not_authenticated;
        }

    }
    else if (m_enmTrxCardType==CardType.CTLS)
    {
        cdic= Card_data_input_capability.Contactless;
        oe= Operating_environment.On_premises_of_card_acceptor_attended;
        Cardholder_p= Cardholder_present.Cardholder_present;
        Card_p=Card_present.Card_present;
        cdim=Card_data_input_mode.Contactless;
        coc = Card_data_output_capability.ICC;
        toc = Terminal_output_capability.Printing_and_display;
        pcc = PIN_capture_capability.Twelve_characters;

        if (m_enmTrxCVM==CVM.ONLINE_PIN)
        {
            cac=Cardholder_authentication_capability.PIN;
            cam=Cardholder_authentication_method.PIN;
            cae=Cardholder_authentication_entity.Authorising_agent;
        }

        else if(m_enmTrxCVM==CVM.OFFLINE_PIN)
        {
            cac=Cardholder_authentication_capability.PIN;
            cam=Cardholder_authentication_method.PIN;
            cae=Cardholder_authentication_entity.ICC;

        }
        else if(m_enmTrxCVM==CVM.SIGNATURE)
        {
            cac=Cardholder_authentication_capability.other;
            cam=Cardholder_authentication_method.Manual_signature_analysis;
            cae=Cardholder_authentication_entity.Not_authenticated;

        }
        else if(m_enmTrxCVM==CVM.NO_CVM)
        {
            cac=Cardholder_authentication_capability.No_electronic_authentication;
            cam=Cardholder_authentication_method.Not_authenticated;
            //todo if transaction below floor limit and offline to be ICC or online by Authorization agent
            cae=Cardholder_authentication_entity.ICC;
        }

    }
    else if (m_enmTrxCardType==CardType.MAG)
    {
        cdic= Card_data_input_capability.Magnetic_stripe_read;
        oe= Operating_environment.On_premises_of_card_acceptor_attended;
        Cardholder_p= Cardholder_present.Cardholder_present;
        Card_p=Card_present.Card_present;
        cdim=Card_data_input_mode.Magnetic_stripe_read;
        coc = Card_data_output_capability.Magnetic_Stripe_write;
        toc = Terminal_output_capability.Printing_and_display;
        pcc = PIN_capture_capability.Twelve_characters;


        if (m_enmTrxCVM==CVM.ONLINE_PIN)
        {
            cac=Cardholder_authentication_capability.PIN;
            cam=Cardholder_authentication_method.PIN;
            cae=Cardholder_authentication_entity.Authorising_agent;
        }
        else if (m_enmTrxCVM==CVM.ONLINE_PIN_SIGNATURE)
        {
            cac=Cardholder_authentication_capability.PIN;
            cam=Cardholder_authentication_method.PIN;
            cae=Cardholder_authentication_entity.Authorising_agent;

        }

        else if(m_enmTrxCVM==CVM.SIGNATURE)
        {
            cac=Cardholder_authentication_capability.other;
            cam=Cardholder_authentication_method.Manual_signature_analysis;
            cae=Cardholder_authentication_entity.By_merchant;
        }
        else if(m_enmTrxCVM==CVM.NO_CVM)
        {
            cac=Cardholder_authentication_capability.No_electronic_authentication;
            cam=Cardholder_authentication_method.Not_authenticated;
            cae=Cardholder_authentication_entity.Not_authenticated;
        }

    }
    else if (m_enmTrxCardType==CardType.MANUAL)
    {
        cdic= Card_data_input_capability.Key_Entered;
        oe= Operating_environment.On_premises_of_card_acceptor_attended;
        Cardholder_p= Cardholder_present.Cardholder_not_present_unspecified;
        Card_p=Card_present.Card_not_present;
        cdim=Card_data_input_mode.Key_entered;
        coc = Card_data_output_capability.None;
        toc = Terminal_output_capability.Printing_and_display;
        pcc = PIN_capture_capability.Twelve_characters;

        if (m_enmTrxCVM==CVM.ONLINE_PIN)
        {
            cac=Cardholder_authentication_capability.PIN;
            cam=Cardholder_authentication_method.PIN;
            cae=Cardholder_authentication_entity.Authorising_agent;
        }
        else if (m_enmTrxCVM==CVM.ONLINE_PIN_SIGNATURE)
        {
            cac=Cardholder_authentication_capability.PIN;
            cam=Cardholder_authentication_method.PIN;
            cae=Cardholder_authentication_entity.Authorising_agent;

        }

        else if(m_enmTrxCVM==CVM.SIGNATURE)
        {
            cac=Cardholder_authentication_capability.other;
            cam=Cardholder_authentication_method.Manual_signature_analysis;
            cae=Cardholder_authentication_entity.By_merchant;
        }
        else if(m_enmTrxCVM==CVM.NO_CVM)
        {
            cac=Cardholder_authentication_capability.No_electronic_authentication;
            cam=Cardholder_authentication_method.Not_authenticated;
            cae=Cardholder_authentication_entity.Not_authenticated;
        }


    }
    else
    {
        cdic= Card_data_input_capability.PAN_Entry_Mode_Unknown;
        cac = Cardholder_authentication_capability.other;
        oe= Operating_environment.On_premises_of_card_acceptor_attended;
        Cardholder_p= Cardholder_present.Cardholder_not_present_unspecified;
        Card_p=Card_present.Card_not_present;
        cdim=Card_data_input_mode.Unspecified;
        cam=Cardholder_authentication_method.Not_authenticated;
        cae=Cardholder_authentication_entity.Not_authenticated;
        coc = Card_data_output_capability.Unknown;
        toc = Terminal_output_capability.Printing_and_display;
        pcc = PIN_capture_capability.Twelve_characters;




    }

        switch(cdic){

            case PAN_Entry_Mode_Unknown:
                bDE22[0]='0';
                break;
            case Manual_no_terminal:
                bDE22[0]='1';
                break;
            case Magnetic_stripe_read:
                bDE22[0]='2';
                break;
            case Bar_Code:
                bDE22[0]='3';
                break;
            case OCR:
                bDE22[0]='4';
                break;
            case ICC:
                bDE22[0]='5';
                break;
            case Key_Entered:
                bDE22[0]='6';
                break;
            case Contactless:
                bDE22[0]='7';
                break;

        }

        switch(cac){
            case No_electronic_authentication:
                bDE22[1]='0';
                break;
            case PIN:
                bDE22[1]='1';
                break;
            case Electronic_signature_analysis:
                bDE22[1]='2';
                break;
            case Biometrics:
                bDE22[1]='3';
                break;
            case Biographic:
                bDE22[1]='4';
                break;
            case Electronic_authentication_inoperative:
                bDE22[1]='5';
                break;
            case other:
                bDE22[1]='6';
                break;
        }

        bDE22[2]='0';   // for position 3 always 0

        switch (oe)
        {
            case On_premises_of_card_acceptor_attended:
                bDE22[3]='1';
                break;
            case On_premises_of_card_acceptor_unattended:
                bDE22[3]='2';
                break;
            case Off_premises_of_card_acceptor_attended:
                bDE22[3]='3';
                break;
            case Off_premises_of_card_acceptor_unattended:
                bDE22[3]='4';
                break;
        }
        switch (Cardholder_p)
        {
            case Cardholder_present:
                bDE22[4]='0';
                break;
            case Cardholder_not_present_unspecified:
                bDE22[4]='1';
                break;
        }
        switch (Card_p)
        {
            case Card_not_present:
                bDE22[5]='0';
                break;
            case Card_present:
                bDE22[5]='1';
                break;
        }
        switch (cdim)
        {
            case Unspecified:
                bDE22[6]='0';
                break;
            case Manual_no_terminal:
                bDE22[6]='1';
                break;
            case  Magnetic_stripe_read:
                bDE22[6]='2';
                break;
            case Bar_Code:
                bDE22[6]='3';
                break;
            case OCR:
                bDE22[6]='4';
                break;
            case ICC:
                bDE22[6]='5';
                break;
            case Key_entered:
                bDE22[6]='6';
                break;
            case Contactless:
                bDE22[6]='7';
                break;
        }

        switch (cam)
        {
            case Not_authenticated:
                bDE22[7]='0';
                break;
            case PIN:
                bDE22[7]='1';
                break;
            case Electronic_signature_analysis:
                bDE22[7]='2';
                break;
            case Biometrics:
                bDE22[7]='3';
                break;
            case Biographic:
                bDE22[7]='4';
                break;
            case Manual_signature_analysis:
                bDE22[7]='5';
                break;
            case Other_manual_verifications:
                bDE22[7]='6';
                break;

        }

        switch (cae)
        {
            case Not_authenticated:
                bDE22[8]='0';
                break;
            case ICC:
                bDE22[8]='1';
                break;
            case CAD:
                bDE22[8]='2';
                break;
            case Authorising_agent:
                bDE22[8]='3';
                break;
            case By_merchant:
                bDE22[8]='4';
                break;
            case other:
                bDE22[8]='5';
                break;

        }

        switch (coc)
        {
            case Unknown:
                bDE22[9]='0';
                break;
            case None:
                bDE22[9]='1';
            case Magnetic_Stripe_write:
                bDE22[9]='2';
            case ICC:
                bDE22[9]='3';
        }

        switch (toc)
        {
            case Unknown:
                bDE22[10]='0';
                break;
            case None:
                bDE22[10]='1';
                break;
            case Printing:
                bDE22[10]='2';
                break;
            case Display:
                bDE22[10]='3';
                break;
            case Printing_and_display:
                bDE22[10]='4';
                break;
        }

        switch (pcc)
        {
            case No_PIN_capture_capability:
                bDE22[11]='0';
                break;
            case Device_PIN_capture_capability_unknown:
                bDE22[11]='1';
                break;
            case Four_characters:
                bDE22[11]='4';
                break;
            case Five_characters:
                bDE22[11]='5';
                break;
            case Six_characters:
                bDE22[11]='6';
                break;
            case Seven_characters:
                bDE22[11]='7';
                break;
            case Eight_characters:
                bDE22[11]='8';
                break;
            case Nine_characters:
                bDE22[11]='9';
                break;
            case Ten_characters:
                bDE22[11]='A';
                break;
            case Eleven_characters:
                bDE22[11]='B';
                break;
            case Twelve_characters:
                bDE22[11]='C';
                break;
        }

        m_sPOSEntryMode=String.copyValueOf(bDE22);


    }
    /**
     * \Function Name: GetDE24
     * \Param  : void
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 5/28/2020
     * \Des    :getting Function code
     */

    enum  Function_Code{
        Original_authorisation_amount_accurate,
        Original_authorisation_amount_estimated,
        Original_authorisation_Bill_Payment,
        Original_authorization_Fee_Payment,
        Notification_of_pre_authorisation_initial_completion,
        Notification_of_a_pre_authorisation_expiry_extension,
        Original_financial_request_advice,
        Original_financial_request_advice_Bill_Payment,
        Original_financial_request_advice_Fee_Payment,
        Previously_approved_authorisation_amount_same,
        Previously_approved_authorisation_amount_differs,
        Previously_approved_authorisation_Bill_Payment,
        Previously_approved_authorisation_Fee_Payment,
        Replace_fields_within_record_partial_download,
        Replace_entire_record_partial_download,
        Replace_file_full_download,
        Full_reversal_transaction_did_not_complete_as_approved,
        Full_reversal_transaction_did_not_complete_as_approved_Bill_Payment,
        Full_reversal_transaction_did_not_complete_as_approved_Fee_Payment,
        Terminal_reconciliation,
        Force_reconciliation,
        Unable_to_parse_message,
        MAC_Error,
        Device_authentication
    }
    public void GetDE24_FunctionCode(){
       Function_Code fc=null;

        switch(m_enmTrxType)
        {
            case PURCHASE:
            case PURCHASE_WITH_NAQD:
            case REFUND:
            case CASH_ADVANCE:
                fc=Function_Code.Original_financial_request_advice;
               /* switch(card_scheme.m_sCard_Scheme_ID) {
                    case "P1":  //MADA
                         fc=Function_Code.Original_financial_request_advice;
                        break;
                    default: //ICS
                        fc=Function_Code.Original_financial_request_advice;
                }*/
                break;
            case PURCHASE_ADVICE:
                if(m_is_mada & (m_enmTrxCardType==CardType.CTLS|m_enmTrxCardType==CardType.MAG|m_enmTrxCardType==CardType.ICC|m_enmTrxCardType==CardType.MANUAL)) //MADA  if fallback is not exist will remove all entries and it will be for allmada
                {
                    if (m_is_final)
                    fc = Function_Code.Previously_approved_authorisation_amount_same;
                    else
                    fc = Function_Code.Previously_approved_authorisation_amount_differs;
                }
                else  //ICS
                {
                    if (m_enmTrxCardType==CardType.MANUAL) {
                        if (m_is_final)
                            fc = Function_Code.Previously_approved_authorisation_amount_same;
                        else
                            fc = Function_Code.Previously_approved_authorisation_amount_differs;
                    }

                }

            case AUTHORISATION:
                if(m_is_mada & (m_enmTrxCardType==CardType.CTLS|m_enmTrxCardType==CardType.MAG|m_enmTrxCardType==CardType.ICC)) //MADA
                fc=Function_Code.Original_authorisation_amount_estimated;//101
                else //ICS
                fc=Function_Code.Original_authorisation_amount_accurate;
            case AUTHORISATION_VOID:
                if(m_is_mada) //MADA
                        fc = Function_Code.Original_authorisation_amount_estimated;  // Original authorisation – amount estimated (used for mada preauthorizations and mada pre-authorization full or partial voids)
                 else
                        fc = Function_Code.Original_authorisation_amount_accurate;

                break;


            case AUTHORISATION_ADVICE:
                fc=Function_Code.Notification_of_pre_authorisation_initial_completion;
                break;
            case AUTHORISATION_EXTENSION:
                fc=Function_Code.Notification_of_a_pre_authorisation_expiry_extension;
                break;
            case REVERSAL:
                if(m_enum_OrigTRxtype==TranscationType.PURCHASE)
                    fc=Function_Code.Full_reversal_transaction_did_not_complete_as_approved;
                else if (m_enum_OrigTRxtype==TranscationType.SADAD_BILL)
                    fc=Function_Code.Full_reversal_transaction_did_not_complete_as_approved_Bill_Payment;
                else
                    fc=Function_Code.Full_reversal_transaction_did_not_complete_as_approved_Fee_Payment;

                break;
            case SADAD_BILL:
                if(m_is_mada)
                    fc = Function_Code.Original_financial_request_advice_Bill_Payment;  // todo check fees also
                else   //ICS
                    fc = Function_Code.Original_authorisation_Bill_Payment;

            case RECONCILIATION:
                if(!POS_MAIN.isforced)
                    fc=Function_Code.Terminal_reconciliation;
                else
                    fc=Function_Code.Force_reconciliation;
                break;
            case ADMIN:
                //todo admin cases
                fc=Function_Code.MAC_Error;
                break;
            case TMS_FILE_DOWNLOAD:
                if(PosApplication.getApp().oGSama_TMS.tms_download_type ==SAMA_TMS.TMS_Download_Type.Field_record)
                    fc = Function_Code.Replace_fields_within_record_partial_download;
                else if (PosApplication.getApp().oGSama_TMS.tms_download_type ==SAMA_TMS.TMS_Download_Type.partial_record)
                    fc = Function_Code.Replace_entire_record_partial_download;
                else if (PosApplication.getApp().oGSama_TMS.tms_download_type ==SAMA_TMS.TMS_Download_Type.full_download)
                    fc = Function_Code.Replace_file_full_download;
                else fc=Function_Code.Replace_file_full_download;

                break;
            case TERMINAL_REGISTRATION:
                fc=Function_Code.Device_authentication;
                break;


        }



        switch (fc) {
            case Original_authorisation_amount_accurate:
                m_sFunctionCode="100";
                break;
            case Original_authorisation_amount_estimated:
                m_sFunctionCode="101";
                break;
            case Original_authorisation_Bill_Payment:
                m_sFunctionCode="160";
                break;
            case Original_authorization_Fee_Payment:
                m_sFunctionCode="161";
                break;
            case Notification_of_pre_authorisation_initial_completion:
                m_sFunctionCode="182";
                break;
            case Notification_of_a_pre_authorisation_expiry_extension:
                m_sFunctionCode="183";
                break;
            case Original_financial_request_advice:
                m_sFunctionCode="200";
                break;
            case Original_financial_request_advice_Bill_Payment:
                m_sFunctionCode="260";
                break;
            case Original_financial_request_advice_Fee_Payment:
                m_sFunctionCode="261";
                break;
            case Previously_approved_authorisation_amount_same:
                m_sFunctionCode="201";
                break;
            case Previously_approved_authorisation_amount_differs:
                m_sFunctionCode="202";
                break;
            case Previously_approved_authorisation_Bill_Payment:
                m_sFunctionCode="262";
                break;
            case Previously_approved_authorisation_Fee_Payment:
                m_sFunctionCode="263";
                break;
            case Replace_fields_within_record_partial_download:
                m_sFunctionCode="302";
                break;
            case Replace_entire_record_partial_download:
                m_sFunctionCode="304";
                break;
            case Replace_file_full_download:
                m_sFunctionCode="306";
                break;
            case Full_reversal_transaction_did_not_complete_as_approved:
                m_sFunctionCode="400";
                break;
            case Full_reversal_transaction_did_not_complete_as_approved_Bill_Payment:
                m_sFunctionCode="420";
                break;
            case Full_reversal_transaction_did_not_complete_as_approved_Fee_Payment:
                m_sFunctionCode="421";
                break;
            case Terminal_reconciliation:
                m_sFunctionCode="570";
                break;
            case Force_reconciliation:
                m_sFunctionCode="571";
                break;
            case Unable_to_parse_message:
                m_sFunctionCode="650";
                break;
            case MAC_Error:
                m_sFunctionCode="691";
                break;
            case Device_authentication:
                m_sFunctionCode="814";
                break;
        }

    }


    enum Message_reason_code
    {
        //1000-1499 Reason for an advice/notification message rather than a request message.
        // The valid codes for an 1120 / 1121 Authorization Transaction Advice
        // or 1220 / 1221 Financial Transaction Advice generated for a chip ICC, contactless and magnetic stripe card are as follows.
        Terminal_processed,
        ICC_or_contactless_application_processed,
        Magnetic_Stripe_Under_floor_limit,
        Approved_by_mada_POS_in_Stand_In_Mode,
        mada_Preauthorization_Void_or_Partial_Void,
        mada_Preauthorization_Extension,
        //1500-1999 Reason for a request message rather than an advice/notification message.
        // The valid codes for an 1100 Authorisation Request generated for an ICS chip card or
        // a 1200 Financial Request for mada or GCCNet ICC contact and contactless cards or for ICS  contactless cards are as follows.
        ICC_random_selection,
        Terminal_random_selection,
        On_line_forced_by_ICC_CDF_or_ADF,
        On_line_forced_by_card_acceptor,
        On_line_forced_by_terminal,
        On_line_forced_by_card_issuer,
        Merchant_suspicious,
        //Reason for a 1200 Financial Transaction Request for an ICS chip card rather than an 1100 Authorisation Message.
        Fallback_from_chip_to_magnetic_stripe,
        Contactless_Transaction,
        Contactless_Transaction_Advice,
        // 4000-4499 Reason for a reversal. The valid codes for an 1420 / 1421 Reversal Advice are as follows.
        Customer_cancellation,
        Unspecified_no_action_taken,
        Suspected_malfunction,
        Format_error_no_action_taken,
        Original_amount_incorrect,
        Response_received_too_late,
        Card_acceptor_device_unable_to_complete_transaction,
        Unable_to_deliver_message_to_point_of_service,
        Invalid_response_no_action_taken,
        Timeout_waiting_for_response,
        MAC_failure
        }
    /**
     * \Function Name: GetDE25Messagereasoncode
     * \Param  : void
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 5/28/2020
     * \Des    : for getting messeage reason code
     */
    public void GetDE25_Messagereasoncode()
    {   Message_reason_code mrc =null;

            //todo getting original message reason code from authorization request message response


        switch(m_enmTrxType)
        {
            case PURCHASE:
                      switch(m_card_scheme.m_sCard_Scheme_ID)
                      {
                          case "P1":            // For MADA card

                                        switch (m_enmTrxCardType) {
                                            case ICC:
                                                mrc=Message_reason_code.On_line_forced_by_ICC_CDF_or_ADF;  //todo condition of other //15xx
                                                break;
                                            case CTLS:
                                                mrc=Message_reason_code.Contactless_Transaction;
                                                break;
                                            case MAG:

                                                break;
                                            case FALLBACK:
                                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                                break;
                                            case MANUAL:
                                                mrc=Message_reason_code.Terminal_processed;
                                                break;
                                        }


                              break;


                          default:       // For IBCS
                              switch (m_enmTrxCardType) {
                                  case ICC:
                                       mrc= Message_reason_code.On_line_forced_by_card_acceptor;
                                      break;
                                  case CTLS:
                                        mrc=Message_reason_code.Contactless_Transaction;
                                      break;
                                  case MAG:
                                        mrc= Message_reason_code.On_line_forced_by_card_acceptor;
                                      break;
                                  case FALLBACK:
                                      mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                      break;

                                  case MANUAL:
                                      mrc=Message_reason_code.Terminal_processed;
                                      break;
                              }
                              break;

                      }
                break;
            case PURCHASE_ADVICE:
                switch(m_card_scheme.m_sCard_Scheme_ID)
                {
                    case "P1":            // For MADA card

                        switch (m_enmTrxCardType) {
                            case ICC:
                                mrc= Message_reason_code.Terminal_processed;//1004
                                break;
                            case CTLS:
                                mrc= Message_reason_code.Contactless_Transaction_Advice;
                                break;
                            case MAG:

                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;
                            case MANUAL:
                                mrc=Message_reason_code.Terminal_processed;
                                break;
                        }


                        break;


                    default:       // For IBCS
                        switch (m_enmTrxCardType) {
                            case ICC:

                                break;
                            case CTLS:

                                break;
                            case MAG:

                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;

                            case MANUAL:
                                mrc=Message_reason_code.Terminal_processed;
                                break;
                        }
                        break;

                }
                break;

            case PURCHASE_WITH_NAQD:
                switch(m_card_scheme.m_sCard_Scheme_ID)
                {
                    case "P1":            // For MADA card

                        switch (m_enmTrxCardType) {
                            case ICC:
                                mrc=Message_reason_code.ICC_random_selection;  //todo condition of other //15xx
                                break;
                            case CTLS:
                                mrc=Message_reason_code.Contactless_Transaction;
                                break;
                            case MAG:

                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;
                            case MANUAL:

                                break;
                        }


                        break;


                    default:       // For IBCS
                        switch (m_enmTrxCardType) {
                            case ICC:

                                break;
                            case CTLS:

                                break;
                            case MAG:

                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;

                            case MANUAL:

                                break;
                        }
                        break;

                }
                break;

            case REFUND:
                switch(m_card_scheme.m_sCard_Scheme_ID)
                {
                    case "P1":            // For MADA card

                        switch (m_enmTrxCardType) {
                            case ICC:
                                mrc=Message_reason_code.ICC_random_selection;  //todo condition of other //15xx
                                break;
                            case CTLS:
                                mrc=Message_reason_code.Contactless_Transaction;
                                break;
                            case MAG:

                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;
                            case MANUAL:

                                break;
                        }


                        break;


                    default:       // For ICS
                        switch (m_enmTrxCardType) {
                            case ICC:
                            case CTLS:
                                if(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sOffline_Refund_PreAuthorization_Capture_Service_Indicator=="1")
                                mrc=Message_reason_code.Terminal_processed;
                                else
                                    mrc=Message_reason_code.ICC_or_contactless_application_processed;
                                break;
                            case MAG:
                                mrc=Message_reason_code.Terminal_processed;
                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;

                            case MANUAL:

                                break;
                        }
                        break;

                }
                break;
            case AUTHORISATION:
                switch(m_card_scheme.m_sCard_Scheme_ID)
                {
                    case "P1":            // For MADA card

                        switch (m_enmTrxCardType) {
                            case ICC:
                                mrc=Message_reason_code.ICC_random_selection;  //todo condition of other //15xx
                                break;
                            case CTLS:
                                mrc=Message_reason_code.Contactless_Transaction;
                                break;
                            case MAG:

                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;
                            case MANUAL:

                                break;
                        }


                        break;


                    default:       // For IBCS
                        switch (m_enmTrxCardType) {
                            case ICC:
                                mrc=Message_reason_code.ICC_random_selection;
                                break;
                            case CTLS:
                                mrc=Message_reason_code.Contactless_Transaction;
                                break;
                            case MAG:

                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;

                            case MANUAL:

                                break;
                        }
                        break;

                }
                break;
            case SADAD_BILL:
                switch(m_card_scheme.m_sCard_Scheme_ID)
                {
                    case "P1":            // For MADA card

                        switch (m_enmTrxCardType) {
                            case ICC:
                                mrc=Message_reason_code.ICC_random_selection;  //todo condition of other //15xx
                                break;
                            case CTLS:
                                mrc=Message_reason_code.Contactless_Transaction;
                                break;
                            case MAG:

                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;
                            case MANUAL:

                                break;
                        }


                        break;


                    default:       // For IBCS
                        switch (m_enmTrxCardType) {
                            case ICC:
                                mrc=Message_reason_code.ICC_random_selection;
                                break;
                            case CTLS:
                                mrc=Message_reason_code.Contactless_Transaction;
                                break;
                            case MAG:

                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;

                            case MANUAL:

                                break;
                        }
                        break;

                }
                break;
            case REVERSAL:
                switch(PosApplication.getApp().oGPosTransaction.reversal_status)
                {
                        case 1://Customer_cancellation:
                            m_sMsgReasonCode="4000";
                            break;
                        case 2://Unspecified_no_action_taken:
                            m_sMsgReasonCode="4001";
                            break;
                        case 3://Suspected_malfunction:
                            m_sMsgReasonCode="4002";
                            break;
                        case 4://Format_error_no_action_taken:
                            m_sMsgReasonCode="4003";
                            break;
                        case 5://Original_amount_incorrect:
                            m_sMsgReasonCode="4005";
                            break;
                        case 6://Response_received_too_late:
                            m_sMsgReasonCode="4006";
                            break;
                        case 7:// Card_acceptor_device_unable_to_complete_transaction:
                            m_sMsgReasonCode="4007";
                            break;
                        case 8://Unable_to_deliver_message_to_point_of_service:
                            m_sMsgReasonCode="4013";
                            break;
                        case 9://Invalid_response_no_action_taken:
                            m_sMsgReasonCode="4020";
                            break;
                        case 10://Timeout_waiting_for_response:
                            m_sMsgReasonCode="4021";
                            break;
                        case 11://MAC_failure:
                            m_sMsgReasonCode="4351";

                            break;
                }


                break;
            case CASH_ADVANCE:
                switch(m_card_scheme.m_sCard_Scheme_ID)
                {
                    case "P1":            // For MADA card

                        switch (m_enmTrxCardType) {
                            case ICC:
                                mrc=Message_reason_code.ICC_random_selection;  //todo condition of other //15xx
                                break;
                            case CTLS:
                                mrc=Message_reason_code.Contactless_Transaction;
                                break;
                            case MAG:

                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;
                            case MANUAL:

                                break;
                        }


                        break;


                    default:       // For IBCS
                        switch (m_enmTrxCardType) {
                            case ICC:

                                break;
                            case CTLS:

                                break;
                            case MAG:

                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;

                            case MANUAL:

                                break;
                        }
                        break;

                }
                break;
            case AUTHORISATION_VOID:
                switch(m_card_scheme.m_sCard_Scheme_ID)
                {
                    case "P1":            // For MADA card

                        switch (m_enmTrxCardType) {
                            case ICC:
                            case CTLS:
                            case MAG:
                                mrc=Message_reason_code.mada_Preauthorization_Void_or_Partial_Void; //1151
                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;
                            case MANUAL:

                                break;
                        }


                        break;


                    default:       // For ICS
                        switch (m_enmTrxCardType) {
                            case ICC:
                            case CTLS:
                            case MAG:
                                break;

                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;
                            case MANUAL:

                                break;
                        }
                        break;

                }

                break;
            case AUTHORISATION_EXTENSION:
                switch(m_card_scheme.m_sCard_Scheme_ID)
                {
                    case "P1":            // For MADA card

                        switch (m_enmTrxCardType) {
                            case ICC:
                            case CTLS:
                            case MAG:
                                mrc=Message_reason_code.mada_Preauthorization_Extension;  //1152
                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;
                            case MANUAL:

                                break;
                        }


                        break;


                    default:       // For ICS
                        switch (m_enmTrxCardType) {
                            case ICC:
                            case CTLS:
                            case MAG:

                                mrc=Message_reason_code.mada_Preauthorization_Extension; //1152
                                break;
                            case FALLBACK:
                                mrc= Message_reason_code.Fallback_from_chip_to_magnetic_stripe;//1776
                                break;
                            case MANUAL:

                                break;
                        }
                        break;

                }

                        break;





        }

        switch(mrc)
        {
            //1000-1499 Reason for an advice/notification message rather than a request message.
            // The valid codes for an 1120 / 1121 Authorization Transaction Advice
            // or 1220 / 1221 Financial Transaction Advice generated for a chip ICC, case contactless and magnetic stripe card are as follows.

            case Terminal_processed:
                m_sMsgReasonCode="1004";
                break;

           case  ICC_or_contactless_application_processed:
                   m_sMsgReasonCode="1005";
               break;
                case Magnetic_Stripe_Under_floor_limit:
                   m_sMsgReasonCode="1006";
                   break;
             case Approved_by_mada_POS_in_Stand_In_Mode:
                   m_sMsgReasonCode="1008";
                break;
             case mada_Preauthorization_Void_or_Partial_Void:
                 m_sMsgReasonCode="1151";
                break;
             case mada_Preauthorization_Extension:
                 m_sMsgReasonCode="1152";
                break;
            //1500-1999 Reason for a request message rather than an advice/notification message.
            // The valid codes for an 1100 Authorisation Request generated for an ICS chip card or
            // a 1200 Financial Request for mada or GCCNet ICC contact and contactless cards or for ICS  contactless cards are as follows.
             case ICC_random_selection:
                 m_sMsgReasonCode="1502";
                break;
             case Terminal_random_selection:
                 m_sMsgReasonCode="1503";
                break;
             case On_line_forced_by_ICC_CDF_or_ADF:
                 m_sMsgReasonCode="1505";
                break;
             case On_line_forced_by_card_acceptor:
                 m_sMsgReasonCode="1506";
                break;
             case On_line_forced_by_terminal:
                 m_sMsgReasonCode="1508";
                break;
             case On_line_forced_by_card_issuer:
                 m_sMsgReasonCode="1509";
                break;
             case Merchant_suspicious:
                 m_sMsgReasonCode="1511";
                break;
            //Reason for a 1200 Financial Transaction Request for an ICS chip card rather than an 1100 Authorisation Message.
             case Fallback_from_chip_to_magnetic_stripe:
                 m_sMsgReasonCode="1776";
                break;
             case Contactless_Transaction:
                 m_sMsgReasonCode="1990";
                break;
             case Contactless_Transaction_Advice:
                 m_sMsgReasonCode="1490";
                break;
            // 4000-4499 Reason for a reversal. The valid codes for an 1420 / 1421 Reversal Advice are as follows.
             case Customer_cancellation:
                 m_sMsgReasonCode="4000";
                break;
             case Unspecified_no_action_taken:
                 m_sMsgReasonCode="4001";
                break;
             case Suspected_malfunction:
                 m_sMsgReasonCode="4002";
                break;
             case Format_error_no_action_taken:
                 m_sMsgReasonCode="4003";
                break;
             case Original_amount_incorrect:
                 m_sMsgReasonCode="4005";
                break;
             case Response_received_too_late:
                 m_sMsgReasonCode="4006";
                break;
             case Card_acceptor_device_unable_to_complete_transaction:
                 m_sMsgReasonCode="4007";
                break;
             case Unable_to_deliver_message_to_point_of_service:
                 m_sMsgReasonCode="4013";
                break;
             case Invalid_response_no_action_taken:
                 m_sMsgReasonCode="4020";
                break;
             case Timeout_waiting_for_response:
                 m_sMsgReasonCode="4021";
                break;
             case MAC_failure:
                 m_sMsgReasonCode="4351";

                break;

        }




    }



    /**
     * \Function Name: GetDE_37_RRN
     * \Param  : void
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 6/xx/2020
     * \Des    : for getting RRN
     */

    public void GetDE_37_RRN()
    {
        m_sRRNumber= m_sSTAN+m_sLocalTrxDateTime;
    }

    public void GetDE39_Actioncode() {

        if(m_enmTrxType==TranscationType.PURCHASE_ADVICE | m_enmTrxType==TranscationType.AUTHORISATION_VOID | m_enmTrxType==TranscationType.AUTHORISATION_EXTENSION)
        m_sActionCode="107";
        else if(PosApplication.getApp().oGTerminal_Operation_Data.TerminalType==35)
        {
                m_sActionCode="000";
        }


    }
    public void GetDE47_CardSchemeSponsorID() {



                switch (m_enmTrxType) {
                    case SADAD_BILL:
                        //todo Sadad data addition
                        m_sCardSchemeSponsorID = PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCard_Scheme_Acquirer_ID + PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCard_Scheme_ID+"additional SADAD BILL DATA";
                        break;
                    default:
                        m_sCardSchemeSponsorID = PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCard_Scheme_Acquirer_ID + PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCard_Scheme_ID;
                        break;


                }
    }

    /**
     * \Function Name: GetDE56_Original_TRX_Data
     * \Param  : void
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 6/xx/2020
     * \Des    : for getting original transaction data incase of refund or reversal . etc.....
     */

    private void GetDE56_Original_TRX_Data() {


  //        Reversals (mada, GCCNet & ICS cards) / Completions (ICS cards) / Refunds (GCCNet & ICS cards)
  //      DE 56 contains 6 data elements from the original transaction (1100/1200) to be refunded, completed or reversed.
       /* Original Message Type Identifier (n 4);
        Original DE 11 Systems Trace Audit Number (n 6);Default "000000".
        Original DE 7 Transmission Date and Time (n 10);Default "0000000000"
        Original DE 12 Date and Time Local Transaction (n 12);Default "000000000000"
        Original DE 32 Acquirer Institution Identification Code(n..11); Default "00".
        441Original DE 33 Forwarding Institution Identification Code(n..11); Default "00".*/

        /*Note: For EFTPOS transactions where Original DE 7
        Transmission Date and Time, Original DE 11 Systems Trace
        Audit Number, Original DE 12 Date and Time Local Transaction
        and Original DE 37 Retrieval Reference Number are not
        available (e.g. Voice Authorisation Advice), these sub-fields may
        be populated with default values consisting of all zeros. If
        Original DE 32 Acquirer Institution Identification Code or Original
        DE 33 Forwarding Institution Identification Code are not
        available, the length of these sub-fields is set to zero.*/

        if((m_card_scheme.m_sCard_Scheme_ID!="P1" & m_enmTrxType==TranscationType.REFUND)  |  m_enmTrxType==TranscationType.REVERSAL | (m_card_scheme.m_sCard_Scheme_ID!="P1" & m_enmTrxType==TranscationType.AUTHORISATION_ADVICE)) {

            if (m_sOrigSTAN == null)
                m_sOrigSTAN="000000";
            if(m_sOrigTrxDateTime==null)
                m_sOrigTrxDateTime="0000000000";
            if(m_sOrigLocalTrxDateTime==null)
                m_sOrigLocalTrxDateTime="000000000000";
            if(m_sOrigAquirerInsIDCode==null)
                m_sOrigAquirerInsIDCode="00";
            if(m_sOrigFWAquirerInsIDCode==null)
                m_sOrigFWAquirerInsIDCode="00";


            m_sOriginalTrxData = m_sOrigMTI + m_sOrigSTAN + m_sOrigTrxDateTime + m_sOrigLocalTrxDateTime + m_sOrigAquirerInsIDCode + m_sOrigFWAquirerInsIDCode;
        }


                //mada Cards Refunds, Completions, Voids & Pre-Auth Extensions

        /*Original Message Type Identifier – the message type identifying the original transaction. length 4
           DE 37 Retrieval Reference Number of the original transaction as keyed by the Retailer.length 12
          Original local transaction date from original transaction receipt. Default value of ‘000000’ if unavailable. length 6
         */
        if (m_card_scheme.m_sCard_Scheme_ID =="P1" & (m_enmTrxType==TranscationType.REFUND |m_enmTrxType==TranscationType.AUTHORISATION_EXTENSION|m_enmTrxType==TranscationType.AUTHORISATION_EXTENSION|m_enmTrxType==TranscationType.AUTHORISATION_ADVICE|m_enmTrxType==TranscationType.AUTHORISATION_VOID))
        {
            if(m_sOrigLocalTrxDate==null)
                m_sOrigLocalTrxDate="000000";
            m_sOriginalTrxData= m_sOrigMTI+m_sOrigRRNumber+m_sOrigLocalTrxDate;
        }

        //todo check DE65 page 103 in manaual entry IBCS will be 3 sub fields not 6 subfields

    }



}


