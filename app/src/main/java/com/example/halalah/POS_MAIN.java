package com.example.halalah;

import android.content.Context;
import android.content.Intent;

import com.example.halalah.TMS.SAMA_TMS;
import com.example.halalah.ui.AmountInputActivity;

/** Header POS Main
 \Class Name: POS_MAIN
 \Param  :
 \Return :
 \Pre    :
 \Post   :
 \Author	: Mostafa Hussiny
 \DT		: 4/28/2020
 \Des    : main control of transaction flow methods
 */
public class POS_MAIN {
    private static final String TAG = POS_MAIN.class.getSimpleName();

    public Context mcontext;
        public POS_MAIN()
        {

        }



    public void Start_Transaction(POSTransaction oPos_trans, POSTransaction.TranscationType Trxtype)
    {
        oPos_trans.Reset();
        if(Trxtype== POSTransaction.TranscationType.REVERSAL) //reversal
        {
            //check host transaction
            //check reversal time out
            //check Transaction mode
            POSTransaction oReversal_Trx = null;
            perform_reversal(oPos_trans,oReversal_Trx);
        }
        Intent AmountACT;
        switch(Trxtype)
        {
            case PURCHASE://purchase

                //get amount
                AmountACT= new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(AmountACT);
                break;
            case PURCHASE_WITH_NAQD://PURCHASE_WITH_NAQD
                //get amount
                 AmountACT = new Intent(mcontext, AmountInputActivity.class);
                 AmountACT.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(AmountACT);

                break;
            case REFUND://REFUND

                // TODO check supervisour password
                // TODO get original transaction Type
                // TODO Input RRN,other required data

                break;
            case AUTHORISATION://AUTHORISATION:
                //get amount
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(AmountACT);

                break;
            case AUTHORISATION_ADVICE://AUTHORISATION_ADVICE:
                //get amount
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(AmountACT);
                break;
            case AUTHORISATION_VOID://AUTHORISATION_VOID:
                break;
            case AUTHORISATION_EXTENSION://AUTHORISATION_EXTENSION
                break;
            case PURCHASE_ADVICE://PURCHASE_ADVICE:
                //todo PURCHASE_ADVICE
                break;

            case CASH_ADVANCE://CASH_ADVANCE:
                //todo start cash advance
                break;
            case REVERSAL://REVERSAL:
                //todo check and do reversal

                break;
            case SADAD_BILL://SADAD_BILL:
                //todo  sadad
                break;
            case RECONCILIATION://RECONCILIATION:
                //todo
                break;
            case TMS_FILE_DOWNLOAD://TMS_FILE_DOWNLOAD:
                //todo TMS download transaction

              //  oPos_trans.ComposeFileDownloadMessage();
                //todo send to packet activity

                break;
            case TERMINAL_REGISTRATION://TREMINAL_REGISTRATION:

                // based on Moamen Ahmed Registeration file , Terminal_Registeration.java also

                PosApplication.getApp().oGTerminal_Registeration.LoadTerminalRegistrationData();

                if( PosApplication.getApp().oGTerminal_Registeration.ValidatePKIFiles(1,1) != 0)
                    //while(true)
                      //  DisplayErrorMessage("INVALID PKI")

                do
                {
                    if (PosApplication.getApp().oGTerminal_Registeration.StartRegistrationProcess(PosApplication.getApp().oGPosTransaction) ==  0)
                    {
                        PosApplication.getApp().oGTerminal_Registeration.bRegistered = true;
                    }
                    else
                    {
                        //todo
                        //DisplayError();

                        PosApplication.getApp().oGTerminal_Registeration.PromptRegisterationSetting();
                    }
                } while(PosApplication.getApp().oGTerminal_Registeration.bRegistered != true);


                //TMSDownloadProcess();
                Start_Transaction(oPos_trans, POSTransaction.TranscationType.TMS_FILE_DOWNLOAD);
                break;
            case ADMIN://ADMIN:
                break;




        }


    }

    public boolean perform_reversal(POSTransaction oOriginal_Transaction,POSTransaction oReversal_Transaction)
    {
        return true;
    }

    public static int Recognise_card(){
            if(PosApplication.getApp().oGPosTransaction.m_enmTrxCardType==POSTransaction.CardType.MANUAL)
            {
                SAMA_TMS.Get_card_scheme_BY_PAN(PosApplication.getApp().oGPosTransaction.m_sPAN);
            }
               SAMA_TMS.Get_card_scheme_BY_AID(PosApplication.getApp().oGPosTransaction.m_sAID);

           return 0;

    }

    /** Header  Check_transaction_allowed
     \function Name: Check_transaction_allowed
     \Param  : Transaction Type
     \Return : boolean Transaction allowed or not
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 6/7/2020
     \Des    : check transaction allowed flag for each transaction
     */
    public static boolean Check_transaction_allowed(POSTransaction.TranscationType Trxtype) {

            //note : Pre-authorization (includes PreAuthorization Extension), Purchase Advice (for Pre-Authorization Capture or Completion )
            // and Correction/Reversal (including PreAuthorization Void/Partial Void)
            // shall be configured for the mada scheme for POS terminals that support the Pre-Authorization and Capture service

        switch (Trxtype) {
            case PURCHASE://purchase  offset 0
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(0)))
                return true;
                break;
            case PURCHASE_WITH_NAQD://PURCHASE_WITH_NAQD                                 offset 1
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(1)))
                return true;
                break;
            case PURCHASE_ADVICE://PURCHASE_ADVICE:                                      offset 2
            case AUTHORISATION_ADVICE://AUTHORISATION_ADVICE:
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(2)))
                return true;
                break;
            case REFUND://REFUND                                                         offset 3
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(3)))
                return true;
                break;
            case AUTHORISATION://AUTHORISATION:                                          offset 4
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(4)))
                return true;
                break;
            case CASH_ADVANCE://CASH_ADVANCE:                                            offset 5
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(5)))
                return true;
                break;
            case REVERSAL://REVERSAL:                                                    offset 6
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(6)))
                return true;
                break;
            case AUTHORISATION_EXTENSION://AUTHORISATION_EXTENSION                       offset 7
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(7)))
                return true;
                break;
            case AUTHORISATION_VOID://AUTHORISATION_VOID:                                offset 8
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(8)))
                return true;
                break;
            case SADAD_BILL://SADAD_BILL:                                                offset 9
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(9)))
                return true;
                break;


        }
        return false;  // transaction not allowed
    }
    public static int Check_transaction_limits()
    {

        return 0;
    }
    public boolean Check_manual_allowed()
    {
        if(PosApplication.getApp().oGPosTransaction.card_scheme.m_sManual_entry_allowed=="1")
            return true;
        else
            return false;
    }
    public int Check_max_cashback(int cashbackamount)
    {
        return 0;
    }

    public static boolean Check_MADA_Card()
    {
      //  if()
        return true;
    }

}




/*
       oPos_trans.ComposeFinancialMessage(POSTransaction.TranscationType.PURCHASE);
               // oPos_trans.m_RequestISOMsg.isotostr();
               //send
               //1221
               //String data ="31 32 32 31 37 32 33 30 30 37 43 31 32 45 43 32 38 42 30 35 31 36 34 38 34 37 38 33 35 30 31 30 34 37 34 39 31 32 30 30 30 30 30 30 30 30 30 30 30 30 30 30 31 30 30 30 31 32 30 36 30 32 31 38 31 32 30 30 30 30 34 38 31 38 31 32 30 36 30 34 35 34 34 36 37 31 30 33 30 31 35 31 33 33 34 43 30 30 30 32 30 30 31 30 30 35 35 33 31 31 30 36 35 38 38 38 34 39 33 34 34 38 34 37 38 33 35 30 31 30 34 37 34 39 31 32 3D 32 31 30 32 32 32 31 31 38 38 38 38 37 35 38 30 32 30 34 35 34 34 36 30 30 30 30 34 37 34 37 34 39 31 32 30 30 30 34 37 30 30 30 31 32 33 30 31 34 39 30 31 32 33 30 31 30 31 31 32 33 34 35 36 37 38 20 20 20 30 30 36 53 41 49 42 50 31 36 38 32 31 33 47 FF F0 01 11 10 00 00 00 08 36 30 39 31 35 39 82 02 3C 00 9F 36 02 02 6A 9F 26 08 19 12 47 8F 99 43 C9 AD 9F 27 01 40 9F 34 03 42 00 00 9F 1E 08 30 37 30 30 30 30 31 31 9F 10 12 06 01 0A 03 60 AC 04 0A 02 00 00 00 00 00 4E AF 33 3E 9F 33 03 E0 F8 C8 9F 35 01 22 95 05 08 80 04 00 00 9F 37 04 C1 62 B7 38 9F 02 06 00 00 00 00 10 00 9F 03 06 00 00 00 00 00 00 9F 1A 02 06 82 5F 2A 02 06 82 9A 03 18 12 06 9C 01 00 84 07 A0 00 00 02 28 20 10 50 04 6D 61 64 61 9F 12 0A 6D 61 64 61 20 44 65 62 69 74 4F 07 A0 00 00 02 28 20 10 34 32 31 31 30 30 30 30 30 30 34 37 31 32 30 36 30 31 35 34 34 36 31 38 31 32 30 36 30 34 35 34 34 36 30 36 35 38 38 38 34 39 30 30 31 31 36 30 31 31 30 32 32 30 33 30 30 30 30 30 38 30 34 30 30 35 30 30 37 4E 32 34 34 31 33 38 45 30 34 36 34 33 34 33 30 39 30 31 30 30 30 30 30 30 30 30 30 30 31 31 30 30 30 30 30 30 30 30 30 31 32 30 30 30 30 30 30 30 30 30 31 33 30 30 30 30 30 30 30 30 30 31 34 30 30 30 30 30 30 30 30 30 30 30 30 31 35 30 36 30 30 30 33 31 36 30 32 30 32 30 32 30 32 FB B9 A7 FF FF FF FF";
               String data ="31 35 33 34 43 32 33 30 30 30 31 31 30 32 43 30 34 38 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 31 31 31 36 34 37 30 30 30 31 32 33 30 31 34 39 30 31 32 33 31 32 30 39 30 30 32 39 31 32 30 30 30 32 30 35 31 38 31 32 30 39 30 33 32 39 31 32 31 38 31 32 30 39 30 36 35 38 38 38 34 39 35 30 31 34 37 30 30 30 31 32 33 30 31 34 39 30 31 32 33 30 31 30 31 31 32 33 34 35 36 37 38 20 20 20 36 38 32 31 33 47 FF F0 01 11 10 00 00 00 04 36 30 39 38 36 36 30 39 56 43 52 59 44 42 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 4D 43 52 59 44 42 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 50 31 52 59 44 42 30 30 30 30 30 30 30 30 30 31 30 30 30 30 30 30 30 30 30 31 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 31 44 4D 52 59 44 42 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 56 44 52 59 44 42 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 41 58 52 59 44 42 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 44 4D 53 41 49 42 30 30 30 30 30 30 30 30 30 38 30 30 30 30 30 30 30 30 30 30 34 37 38 35 30 30 30 30 30 30 30 30 30 30 32 30 30 30 30 30 30 30 30 30 30 32 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 50 31 53 41 49 42 30 30 30 30 30 30 30 30 30 32 30 30 30 30 30 30 30 30 30 30 30 31 31 31 31 30 30 30 30 30 30 30 30 30 32 30 30 30 30 30 30 30 30 30 30 30 31 30 30 38 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 56 43 53 41 49 42 30 30 30 30 30 30 30 30 30 37 30 30 30 30 30 30 30 30 35 34 30 33 38 35 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 2D 30 30 30 30 30 30 30 30 33 8E A8 F8 18 FF FF FF FF";
               // byte[] x=data.getBytes();
               byte[] x = BCDASCII.hexStringToBytes(data);
               oPos_trans.m_ResponseISOMsg.strtoiso(x);
               oPos_trans.m_sOrigMTI=oPos_trans.m_ResponseISOMsg.getMTI();
               */
/* oPos_trans.m_sPAN=new String(oPos_trans.m_ResponseISOMsg.getBit(2));
                oPos_trans.m_sProcessCode=new String(oPos_trans.m_ResponseISOMsg.getBit(3));
                oPos_trans.m_sTrxAmount=new String(oPos_trans.m_ResponseISOMsg.getBit(4));
                oPos_trans.m_sTrxDateTime=new String(oPos_trans.m_ResponseISOMsg.getBit(7));//7
                oPos_trans.m_sSTAN=new String(oPos_trans.m_ResponseISOMsg.getBit(11));
                oPos_trans.m_sLocalTrxDateTime=new String(oPos_trans.m_ResponseISOMsg.getBit(12));//12
                oPos_trans.m_sTrack2=new String(oPos_trans.m_ResponseISOMsg.getBit(35));*//*




/*
***set MTI ***
                oPos_trans.m_RequestISOMsg.SetMTI(PosApplication.MTI_Financial_Request);
                        oPos_trans.m_RequestISOMsg.ClearFields();

                        byte[]  field2 ="5061150708203311421".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(2,field2, field2.length );
                        byte[] field3 = "9G000G".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(3, field3, field3.length);
                        byte[] field4 = "200".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(4, field4, field4.length);

                        byte[] field7 = "0531124151".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(7, field7, field7.length);


                        byte[] field11 = "20".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(11, field11, field11.length);


                        byte[] field12 = "124151".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(12, field12, field12.length);

                        byte[] field13 = "0531".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(13, field13, field13.length);

                        byte[] field14 = "2106".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(14, field14, field14.length);

                        byte[] field22 = "051".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(22, field22, field22.length);

                        byte[] field23 = "001".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(23, field23, field23.length);
                        byte[] field25 = "00".getBytes();

                        oPos_trans.m_RequestISOMsg.SetDataElement(25, field25, field25.length);
                        byte[] field26 = "12".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(26, field26, field26.length);

                        byte[] field32 = "111129".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(32, field32, field32.length);

                        byte[] field35 = "5061150708203311421D2106601019546474".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(35, field35, field35.length);


                        byte[] field37 = "00000000002".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(37, field37, field37.length);

                        byte[] field41 = "2058RH14".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(41, field41, field41.length);

                        byte[] field42 = "2058LA015782326".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(42, field42, field42.length);

                        byte[] field49 = "556".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(49, field49, field49.length);
                        String field55Str ="9F2608368992B162D89F859F2701809F10140FA501A20330100000000000000000000F01AAAA9F37044579DCC99F360200D9950500802488009A031905319C01009F02060000000002005F2A020566820258009F1A0205669F34034203009F3303E0F8C89F350122";
                        //  byte[] field55 = BCDASCII.fromASCIIToBCD(field55Str,0 ,field55Str.length(),false);
                        byte[] field55 =field55Str.getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(55, field55, field55.length);

                        byte[] field64 = "8E A8 F8 18 FF FF FF FF".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(64, field64, field64.length);

                        byte[] field72 = "AAAABBBBCCCCDDDDEEEEFFFFGGGGHHHHIIIIJJJJKKKKLLLLMMMMNNNNOOOOBBBBQQQQRRRRSSSSTTTTUUUUVVVVWWWWXXXXYYYYZZZZ".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(72, field72, field72.length);

                        byte[] field124 = "1234567891234567891234567891234567800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000009999".getBytes();
                        oPos_trans.m_RequestISOMsg.SetDataElement(124, field124, field124.length);

                        byte[] bOutdata =oPos_trans.m_RequestISOMsg.isotostr();*/
