package com.example.halalah;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.halalah.TMS.AID_Data;
import com.example.halalah.TMS.Card_Scheme;
import com.example.halalah.TMS.Public_Key;
import com.example.halalah.TMS.SAMA_TMS;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.connect.SendReceiveListener;
import com.example.halalah.iso8583.ISO8583;
import com.example.halalah.packet.UnpackPurchase;
import com.example.halalah.registration.view.ITransaction;
import com.example.halalah.secure.DUKPT_KEY;
import com.example.halalah.sqlite.database.DBManager;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.ui.AmountInputActivity;
import com.example.halalah.ui.P_NAQD_InputActivity;
import com.example.halalah.ui.PacketProcessActivity;
import com.example.halalah.ui.Refund_InputActivity;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;

import java.util.Locale;

/**
 * Header POS Main
 * \Class Name: POS_MAIN
 * \Param  :
 * \Return :
 * \Pre    :
 * \Post   :
 * \Author	: Mostafa Hussiny
 * \DT		: 4/28/2020
 * \Des    : main control of transaction flow methods
 */
public class POS_MAIN implements SendReceiveListener {
    private static final String TAG = POS_MAIN.class.getSimpleName();
    public static boolean isforced;
    private static boolean cont;

    public enum Flowtrxtype {
        DESAF,
        RECONSILE,
        REVERSAL
    }

    public Flowtrxtype m_enumflowtype;


    public Context mcontext;

    public POS_MAIN() {

    }


    /** Header recognise card
     \function Name: recognise card
     \Param  :
     \Return :
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 4/28/2020  modified 1/7/2020
     \Des    : will get the card AID and check if this card is MAda based on AID or PAN
     */
    public static int Recognise_card() {
        int istate = -1;
        if (PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.MANUAL || PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.MAG) {
            istate = SAMA_TMS.Get_card_scheme_BY_PAN(PosApplication.getApp().oGPosTransaction.m_sPAN);
            PosApplication.getApp().oGPosTransaction.m_is_mada = Check_MADA_Card();


        } else
            istate = SAMA_TMS.Get_card_scheme_BY_AID(PosApplication.getApp().oGPosTransaction.m_sAID);

        return istate;

    }

    /** Header  Check_MADA_Card
     \function Name: Check_MADA_Card
     \Param  :
     \Return : boolean for true is mada
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 6/00/2020
     \Des    : check card is mada or ICS
     */
    public static boolean Check_MADA_Card() {
        return PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCard_Scheme_ID == "P1";
    }

    /** Header POS Main
     \function Name: Perform_reversal
     \Param  :
     \Return :
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 6/00/2020
     \Des    : will start collection and performe reversal transaction
     */
    public boolean perform_reversal(POSTransaction oOriginal_Transaction,POSTransaction oReversal_Transaction) {
        oReversal_Transaction = SAF_Info.BuildSAFOriginals(oReversal_Transaction, oOriginal_Transaction);
        oReversal_Transaction.m_enmTrxType = POSTransaction.TranscationType.REVERSAL;
        oReversal_Transaction.m_sMTI = PosApplication.MTI_Reversal_Advice;
        //todo copy nessasrry data from original transaction to oRevesal transaction
        //todo  save in saf the reversal advice then printing copy of reversal then performe DESAF


        return true;
    }

    public static String FormatCAKeys(Public_Key CAKeyObj) {
        int iRetRes = 0;
        StringBuilder strFormatedCAKey = new StringBuilder();

        if (CAKeyObj == null) {
            // log an error message
            return String.valueOf(iRetRes);
        }

        // Adding RID
        if (CAKeyObj.RID.length() > 0)
            strFormatedCAKey.append("9F06" + String.format(Locale.ENGLISH, "%02d", CAKeyObj.RID.length() / 2) + CAKeyObj.RID);
        else {
            //Todo Log message showing can not format sent key due to incorrect length of RID
            return String.valueOf(iRetRes);
        }


        // Adding Key_Index
        if (CAKeyObj.Key_Index.length() > 0)
            strFormatedCAKey.append("9F22" + String.format(Locale.ENGLISH, "%02d", CAKeyObj.Key_Index.length() / 2) + CAKeyObj.Key_Index);
        else {
            //Todo Log message showing can not format sent key due to incorrect length of Key_Index
            return String.valueOf(iRetRes);
        }

        // Adding CA_Public_Key_Expiry_Date
        if (CAKeyObj.CA_Public_Key_Expiry_Date.length() > 0)
            strFormatedCAKey.append("DF05" +/*String.format(Locale.ENGLISH,"%02d",CAKeyObj.CA_Public_Key_Expiry_Date.length()/2)*/"0420" + CAKeyObj.CA_Public_Key_Expiry_Date);
        else {
            //Todo Log message showing can not format sent key due to incorrect length of CA_Public_Key_Expiry_Date
            return String.valueOf(iRetRes);
        }
        // Adding Hash_ID
        if (CAKeyObj.Hash_ID.length() > 0)
            strFormatedCAKey.append("DF06" + String.format(Locale.ENGLISH, "%02d", CAKeyObj.Hash_ID.length() / 2) + CAKeyObj.Hash_ID);
        else {
            //Todo Log message showing can not format sent key due to incorrect length of Hash_ID
            return String.valueOf(iRetRes);
        }


        // Adding Digital_Signature_ID
        if (CAKeyObj.Digital_Signature_ID.length() > 0)
            strFormatedCAKey.append("DF07" + String.format(Locale.ENGLISH, "%02d", CAKeyObj.Digital_Signature_ID.length() / 2) + CAKeyObj.Digital_Signature_ID);
        else {
            //Todo Log message showing can not format sent key due to incorrect length of Digital_Signature_ID
            return String.valueOf(iRetRes);
        }


        // formating  Public Key Value  and Public_Key length
        if (CAKeyObj.Public_Key.length() > 0 && CAKeyObj.CA_Public_Key_Length.length() > 0)
            strFormatedCAKey.append("DF0281" + Integer.toHexString(CAKeyObj.Public_Key.length() / 2) + CAKeyObj.Public_Key);
        else {
            //Todo Log message showing can not format sent key due to incorrect length of Public_Key
            return String.valueOf(iRetRes);
        }

        // Adding Exponent
        if (CAKeyObj.Exponent.length() > 0)
            strFormatedCAKey.append("DF04" + String.format(Locale.ENGLISH, "%02d", CAKeyObj.Exponent.length() / 2) + CAKeyObj.Exponent);
        else {
            //Todo Log message showing can not format sent key due to incorrect length of Exponent
            return String.valueOf(iRetRes);
        }

        // Adding Check_Sum
        if (CAKeyObj.Check_Sum.length() > 0)
            strFormatedCAKey.append("DF03" + Integer.toHexString(CAKeyObj.Check_Sum.length() / 2)/*String.format(Locale.ENGLISH,"%02d",CAKeyObj.Check_Sum.length()/2)*/ + CAKeyObj.Check_Sum);
        else {
            //Todo Log message showing can not format sent key due to incorrect length of Check_Sum
            return String.valueOf(iRetRes);
        }


        // Todo log Formated string for debugging

        return strFormatedCAKey.toString();

    }

    /** Header  Check_transaction_allowed
     \function Name: Check_transaction_allowed
     \Param  : Transaction Type
     \Return : boolean Transaction allowed or not
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 6/00/2020
     \Des    : check transaction allowed flag for each transaction
     */
    public static boolean Check_transaction_allowed(POSTransaction.TranscationType Trxtype) {

            //note : Pre-authorization (includes PreAuthorization Extension), Purchase Advice (for Pre-Authorization Capture or Completion )
            // and Correction/Reversal (including PreAuthorization Void/Partial Void)
            // shall be configured for the mada scheme for POS terminals that support the Pre-Authorization and Capture service

        switch (Trxtype) {
            case PURCHASE://purchase  offset 0
                if ("1".equals(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(0)))
                return true;
                break;
            case PURCHASE_WITH_NAQD://PURCHASE_WITH_NAQD                                 offset 1
                if ("1".equals(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(1)))
                return true;
                break;
            case PURCHASE_ADVICE://PURCHASE_ADVICE:                                      offset 2
            case AUTHORISATION_ADVICE://AUTHORISATION_ADVICE:
                if ("1".equals(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(2)))
                return true;
                break;
            case REFUND://REFUND                                                         offset 3
                if ("1".equals(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(3)))
                return true;
                break;
            case AUTHORISATION://AUTHORISATION:                                          offset 4
                if ("1".equals(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(4)))
                return true;
                break;
            case CASH_ADVANCE://CASH_ADVANCE:                                            offset 5
                if ("1".equals(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(5)))
                return true;
                break;
            case REVERSAL://REVERSAL:                                                    offset 6
                if ("1".equals(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(6)))
                return true;
                break;
            case AUTHORISATION_EXTENSION://AUTHORISATION_EXTENSION                       offset 7
                if ("1".equals(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(7))) {
                    return true;
                }else if(PosApplication.getApp().oGPosTransaction.m_is_mada)
                {

                    return true;
                }
                break;
            case AUTHORISATION_VOID://AUTHORISATION_VOID:                                offset 8
                if ("1".equals(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(8)))
                return true;
                else if(PosApplication.getApp().oGPosTransaction.m_is_mada)
                {

                    return true;
                }
                break;
            case SADAD_BILL://SADAD_BILL:                                                offset 9
                if ("1".equals(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(9)))
                return true;
                else if(PosApplication.getApp().oGPosTransaction.m_is_mada)
                {

                    return true;
                }
                break;


        }
        return false;  // transaction not allowed
    }

    /** Header  Check_transaction_limit
     \function Name: Check_transaction_limits
     \Param  :
     \Return : int will reason
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 6/00/2020
     \Des    : check transaction limit allowed for each transaction
     */
    public static int Check_transaction_limits(POSTransaction.TranscationType Trxtype)
    {
        int istate=-1;
       /* Note
        1. The “Maximum transaction amount indicator” for the mada scheme i.e. ‘P1’  will act as the global setting for all card  schemes configured for each terminal.
        2. The terminal application shall therefore ignore all the other scheme segments “Maximum transaction amount indicator” values.
        3. When the “Maximum transaction amount indicator” is checked or unchecked it will apply to both contact and contactless  transactions.*/



        switch (Trxtype) {
            case PURCHASE://purchase  offset 0
                if ("1".equals(PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(0)))
                    istate=1;
                break;
            case PURCHASE_WITH_NAQD://PURCHASE_WITH_NAQD                                 offset 1
                if ("1".equals(PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(1)))
                    istate=1;
                break;
            case PURCHASE_ADVICE://PURCHASE_ADVICE:                                      offset 2
            case AUTHORISATION_ADVICE://AUTHORISATION_ADVICE:
                if ("1".equals(PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(2)))
                    istate=1;
                break;
            case REFUND://REFUND                                                         offset 3
                if ("1".equals(PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(3)))
                    istate=1;
                break;
            case AUTHORISATION://AUTHORISATION:                                          offset 4
                if ("1".equals(PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(4)))
                    istate=1;
                break;
            case CASH_ADVANCE://CASH_ADVANCE:                                            offset 5
                if ("1".equals(PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(5)))
                    istate=1;
                break;
            case REVERSAL://REVERSAL:                                                    offset 6
                if ("1".equals(PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(6)))
                    istate=1;
                break;
            case AUTHORISATION_EXTENSION://AUTHORISATION_EXTENSION                       offset 7
                if ("1".equals(PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(7))) {
                    istate=1;
                }else if(PosApplication.getApp().oGPosTransaction.m_is_mada)
                {

                    istate=1;
                }
                break;
            case AUTHORISATION_VOID://AUTHORISATION_VOID:                                offset 8
                if ("1".equals(PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(8)))
                    istate=1;
                else if(PosApplication.getApp().oGPosTransaction.m_is_mada)
                {

                    istate=1;
                }
                break;
            case SADAD_BILL://SADAD_BILL:                                                offset 9
                if ("1".equals(PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(9)))
                    istate=1;
                else if(PosApplication.getApp().oGPosTransaction.m_is_mada)
                {

                    istate=1;
                }
                break;


        }

        if (istate == 1) {

            if (Integer.parseInt(PosApplication.getApp().oGPosTransaction.m_sTrxAmount) > Integer.parseInt(PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount))
                istate = 0;
            else
                istate = -1;
        }


        return istate;
    }

    public static boolean ValidateHostMAC() {
        ISO8583 oResponseTrx = PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg;

        String sMAC = "";

        switch (PosApplication.getApp().oGPosTransaction.m_enmTrxType) {
            case AUTHORISATION:
            case AUTHORISATION_EXTENSION:


                //0.Messa   ge Type Identifier
                sMAC = PosApplication.MTI_Authorisation_Request;
                //1. Primary bitmap   todo get bitmap
                sMAC = new String(oResponseTrx.Getbitmap());
                //2.Primary Account Number (PAN)
                sMAC.concat(new String(oResponseTrx.getDataElement(2)));
                //3.Processing Code
                sMAC.concat(new String(oResponseTrx.getDataElement(3)));
                //4.Amount, Transaction
                sMAC.concat(new String(oResponseTrx.getDataElement(4)));
                //11.System Trace Audit Number
                sMAC.concat(new String(oResponseTrx.getDataElement(11)));
                //12.Date and Time, Local Transaction
                sMAC.concat(new String(oResponseTrx.getDataElement(12)));
                //39 Action code
                sMAC.concat(new String(oResponseTrx.getDataElement(39)));
                //47.National Data
                sMAC.concat(new String(oResponseTrx.getDataElement(47)));
                //53.Security Related Control Information
                sMAC.concat(new String(oResponseTrx.getDataElement(53)));
                //55.EMV Data
                if (PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.ICC | PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.CTLS)
                    sMAC.concat(new String(oResponseTrx.getDataElement(55)));
                break;
            case AUTHORISATION_ADVICE:
                //0.Messa   ge Type Identifier
                sMAC = PosApplication.MTI_Authorisation_Advice;
                //1. Primary bitmap
                sMAC = new String(oResponseTrx.Getbitmap());
                //2.Primary Account Number (PAN)
                sMAC.concat(new String(oResponseTrx.getDataElement(2)));
                //3.Processing Code
                sMAC.concat(new String(oResponseTrx.getDataElement(3)));
                //4.Amount, Transaction
                sMAC.concat(new String(oResponseTrx.getDataElement(4)));
                //11.System Trace Audit Number
                sMAC.concat(new String(oResponseTrx.getDataElement(11)));
                //12.Date and Time, Local Transaction
                sMAC.concat(new String(oResponseTrx.getDataElement(12)));
                //39 Action code
                sMAC.concat(new String(oResponseTrx.getDataElement(39)));
                //47.National Data
                sMAC.concat(new String(oResponseTrx.getDataElement(47)));
                //53.Security Related Control Information
                sMAC.concat(new String(oResponseTrx.getDataElement(53)));
                //55.EMV Data
                if (PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.ICC | PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.CTLS)
                    sMAC.concat(new String(oResponseTrx.getDataElement(55)));

                break;
            case REFUND:
            case CASH_ADVANCE:
            case PURCHASE_WITH_NAQD:
            case PURCHASE:
                //0.Messa   ge Type Identifier
                sMAC = PosApplication.MTI_Financial_Request;
                //1. Primary bitmap
                sMAC = new String(oResponseTrx.Getbitmap());
                //2.Primary Account Number (PAN)
                sMAC.concat(new String(oResponseTrx.getDataElement(2)));
                //3.Processing Code
                sMAC.concat(new String(oResponseTrx.getDataElement(3)));
                //4.Amount, Transaction
                sMAC.concat(new String(oResponseTrx.getDataElement(4)));
                //11.System Trace Audit Number
                sMAC.concat(new String(oResponseTrx.getDataElement(11)));
                //12.Date and Time, Local Transaction
                sMAC.concat(new String(oResponseTrx.getDataElement(12)));
                //39 Action code
                sMAC.concat(new String(oResponseTrx.getDataElement(39)));
                //47.National Data
                sMAC.concat(new String(oResponseTrx.getDataElement(47)));
                //53.Security Related Control Information
                sMAC.concat(new String(oResponseTrx.getDataElement(53)));
                //55.EMV Data
                if (PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.ICC | PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.CTLS)
                    sMAC.concat(new String(oResponseTrx.getDataElement(55)));
                break;
            case PURCHASE_ADVICE:
                //0.Messa   ge Type Identifier
                sMAC = PosApplication.MTI_Financial_Transaction_Advice;
                //1. Primary bitmap
                sMAC = new String(oResponseTrx.Getbitmap());
                //2.Primary Account Number (PAN)
                sMAC.concat(new String(oResponseTrx.getDataElement(2)));
                //3.Processing Code
                sMAC.concat(new String(oResponseTrx.getDataElement(3)));
                //4.Amount, Transaction
                sMAC.concat(new String(oResponseTrx.getDataElement(4)));
                //11.System Trace Audit Number
                sMAC.concat(new String(oResponseTrx.getDataElement(11)));
                //12.Date and Time, Local Transaction
                sMAC.concat(new String(oResponseTrx.getDataElement(12)));
                //39 Action code
                sMAC.concat(new String(oResponseTrx.getDataElement(39)));
                //47.National Data
                sMAC.concat(new String(oResponseTrx.getDataElement(47)));
                //53.Security Related Control Information
                sMAC.concat(new String(oResponseTrx.getDataElement(53)));
                //55.EMV Data
                if (PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.ICC | PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.CTLS)
                    sMAC.concat(new String(oResponseTrx.getDataElement(55)));

                break;
            case REVERSAL:
                //0.Messa   ge Type Identifier
                sMAC = PosApplication.MTI_Reversal_Advice;
                //1. Primary bitmap
                sMAC = new String(oResponseTrx.Getbitmap());
                //2.Primary Account Number (PAN)
                sMAC.concat(new String(oResponseTrx.getDataElement(2)));
                //3.Processing Code
                sMAC.concat(new String(oResponseTrx.getDataElement(3)));
                //4.Amount, Transaction
                sMAC.concat(new String(oResponseTrx.getDataElement(4)));
                //11.System Trace Audit Number
                sMAC.concat(new String(oResponseTrx.getDataElement(11)));
                //12.Date and Time, Local Transaction
                sMAC.concat(new String(oResponseTrx.getDataElement(12)));
                //39 Action code
                sMAC.concat(new String(oResponseTrx.getDataElement(39)));
                //47.National Data
                sMAC.concat(new String(oResponseTrx.getDataElement(47)));
                //53.Security Related Control Information
                sMAC.concat(new String(oResponseTrx.getDataElement(53)));
                //55.EMV Data
                if (PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.ICC | PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.CTLS)
                    sMAC.concat(new String(oResponseTrx.getDataElement(55)));
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

        if (bMac.length % 8 != 0) {
            for (int i = 0; i < bMac.length % 8; i++) {
                sMAC = sMAC + 0x00;
            }
        }

        sMAC = DUKPT_KEY.CaluclateMACBlock(sMAC);

        //removinglast 4 bytes
        sMAC = sMAC.substring(3, 7);
        sMAC = sMAC + 0xFF + 0xFF + 0xFF + 0xFF;


        return sMAC.equals(oResponseTrx.getDataElement(64));
    }

    public int Check_max_cashback(int cashbackamount) {
        //todo get max cash back

        return 0;
    }

    /**
     * \Function Name: check_hardware
     * \Param  : POSTransaction POSTrx
     * \Return : double
     * \Pre    :
     * \Post   :
     * \Author	: mostafa hussiny
     * \DT		: 00/08/2020
     * \Des    : check_hardware printer contactless reader , chip , mag ,...etc
     */

    public static void check_hardware() {

        AidlPrinter mPrinterManager;
        mPrinterManager = DeviceTopUsdkServiceManager.getInstance().getPrintManager();
        int printState = -1;
        try {
            printState = mPrinterManager.getPrinterState();
            Log.i(TAG, "printState = " + printState);
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (printState) {
            case 0://PRINTER_STATE_Normal             // for mada '0' = No printer. '1' = Out of paper. '2' = Plain paper receipt.
                break;
            case 1://PRINTER_STATE_NOPAPER
                PosApplication.getApp().oGTerminal_Operation_Data.Printer_Status = "1";
            case 2:
        }
        }


    /** Header  supervisor_pass_required
     \function Name: supervisor_pass_required
     \Param  :
     \Return : void
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 6/00/2020
     \Des    : should check password parameter in TMS & popup display to enter pass
     */
    public static void supervisor_pass_required(){

      if(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sSupervisor_Functions=="1") {

          //todo dialog which ask for password}


      /*    AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
          // Get the layout inflater
          LayoutInflater inflater = mcontext.ac`.getLayoutInflater();

          // Inflate and set the layout for the dialog
          // Pass null as the parent view because its going in the dialog layout
          builder.setView(inflater.inflate(R.layout.dialog_pass, null))
                  // Add action buttons
                  .setPositiveButton(R.string.password, new AlertDialog.OnClickListener() {
                      @Override
                      public void onClick(AlertDialog dialog, int id) {

                          // todo check pass if correct
                      }
                  })
                  .setNegativeButton(R.string.dialog_cancle, new AlertDialog.OnClickListener() {
                      public void onClick(AlertDialog dialog, int id) {
                          Fragment_home_Transaction.this.getDialog().cancel();
                      }
                  });*/
      }
    }
    /** Header  Check_Servicecode
     \function Name: Check_Servicecode
     \Param  :
     \Return : void
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 6/00/2020
     \Des    : checking service code of magnetic cards
     */
    public static void Check_Servicecode(String track2) {
        Log.i(TAG, "isEmvCard: track2: " + track2);
        if ((track2 != null) && (track2.length() > 0)) {
            int index = track2.indexOf("=");
            String subTrack2 = track2.substring(index);

            switch (subTrack2.charAt(7))
            {

                case 0: //No restrictions, PIN required
                case 5: //Goods and services only (no cash), PIN required
                case 6: //No restrictions, use PIN where feasible
                case 7: //Goods and services only (no cash), use PIN where feasible
                    PosApplication.getApp().oGPosTransaction.m_enmTrxCVM= POSTransaction.CVM.ONLINE_PIN;
                    break;
                case 1: //No restrictions
                case 2: //Goods and services only (no cash)
                case 3: //ATM only, PIN required
                case 4: //Cash only
                    PosApplication.getApp().oGPosTransaction.m_enmTrxCVM= POSTransaction.CVM.NO_CVM;
                 break;


            }
        }
        Log.i(TAG, "Check_Servicecode: "+PosApplication.getApp().oGPosTransaction.m_enmTrxCVM);

    }

    /** Header  Check_CVM
     \function Name: Check_CVM
     \Param  :
     \Return : int
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 6/00/2020
     \Des    : check CVM on TMS for each transaction if pin required or not
     */
    public static int Check_CVM(POSTransaction.TranscationType Trxtype) {

        switch (Trxtype) {
            case PURCHASE://purchase  offset 0

                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCardholder_Authentication.charAt(0)));

            case PURCHASE_WITH_NAQD://PURCHASE_WITH_NAQD                                 offset 1
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCardholder_Authentication.charAt(1)));

            case PURCHASE_ADVICE://PURCHASE_ADVICE:                                      offset 2
            case AUTHORISATION_ADVICE://AUTHORISATION_ADVICE:
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCardholder_Authentication.charAt(2)));

            case REFUND://REFUND                                                         offset 3
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCardholder_Authentication.charAt(3)));

            case AUTHORISATION://AUTHORISATION:                                          offset 4
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCardholder_Authentication.charAt(4)));
            case CASH_ADVANCE://CASH_ADVANCE:                                            offset 5
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCardholder_Authentication.charAt(5)));
            case REVERSAL://REVERSAL:                                                    offset 6
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCardholder_Authentication.charAt(6)));
            case AUTHORISATION_EXTENSION://AUTHORISATION_EXTENSION                       offset 7
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCardholder_Authentication.charAt(7)));
            case AUTHORISATION_VOID://AUTHORISATION_VOID:                                offset 8
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCardholder_Authentication.charAt(8)));
            case SADAD_BILL://SADAD_BILL:                                                offset 9
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCardholder_Authentication.charAt(9)));

        }
        return -1;
    }




/********************************************************************************
    // Topwise Tags

9f06 07 A0000002282010      -->AID
    df01 01 00                   -->ApplicationSelectionIndicator(0:PartMatch,1:ExactMatch)
9f09 02 00084                -->ApplicationVersionNumber

    df11 05 BC40BC8000           -->TerminalActionCode-Default
    df12 05 BC40BC8000		     -->TerminalActionCode-Online
    df13 05 0010000000           -->TerminalActionCode-Decline

9f1b 04 00000000             -->TerminalFloorLimit
    df15 04 00000290             -->ThresholdValueforBiasedRandomSelection
    df16 01 00                   -->MaximumTargetPercentagetobeUsedforBiasedRandomSelection
    df17 01 00                   -->TargetPercentagetobeUsedforRandomSelection
    df14 03 9F3704               -->DDOLdefault
    df19 06 000000000000         -->ContactlessReaderfloorLimit
    df20 06 000099999999         -->ContactlessReader
    df21 06 000000010000         -->Contactless Reader CVM Limit



// Sample formated string for mada AIDs
9f0607A0000002282010df0101009f09020084df1105BC40BC8000df1205BC40BC8000df130500100000009f1b0400000000df150400000290df160100df170100df14039F3704df1906000000000000df2006000099999999df2106000000010000
9f0608a0000003330101df0101009f09020020df1105fc78fcf8f0df1205fc78fcf8f0df130500100000009f1b04000186a0df150400000028df160150df170120df14039f3704df2006000099999999df2106000000100000df1906000000030000

*********************************************************************************************/






    public static String FormatAIDData(AID_Data AIDObj)
    {
        int iRetRes = -1;
        StringBuilder strFormatedAIDData = new StringBuilder();

        if (AIDObj == null)
        {
            // log an error message
            return String.valueOf(iRetRes);
        }

        // Adding AID
        if (AIDObj.AID.length() > 0)
            strFormatedAIDData.append("9f06" +String.format(Locale.ENGLISH,"%02d",AIDObj.AID.length()/2)+AIDObj.AID);

        // Adding ApplicationSelectionIndicator(0:PartMatch,1:ExactMatch)
        strFormatedAIDData.append("df010100");

        // Adding ApplicationVersionNumber
        if (AIDObj.Terminal_AID_version_numbers.length() > 0)
            strFormatedAIDData.append("9f09" +"02"+AIDObj.Terminal_AID_version_numbers.substring(0,4));

        // Adding Default_action_code
        if (AIDObj.Denial_action_code.length() > 0)
            strFormatedAIDData.append("df11" +String.format(Locale.ENGLISH,"%02d",AIDObj.Default_action_code.length()/2)+AIDObj.Default_action_code);


        // Adding Online_action_code
        if (AIDObj.Denial_action_code.length() > 0)
            strFormatedAIDData.append("df12" +String.format(Locale.ENGLISH,"%02d",AIDObj.Online_action_code.length()/2)+AIDObj.Online_action_code);


        // Adding Denial_action_code
        if (AIDObj.Denial_action_code.length() > 0)
            strFormatedAIDData.append("df13" +String.format(Locale.ENGLISH,"%02d",AIDObj.Denial_action_code.length()/2)+AIDObj.Denial_action_code);

            strFormatedAIDData.append("9f1b"+String.format(Locale.ENGLISH,"%02d","00000000".length()/2)+"00000000");

        // Adding ThresholdValueforBiasedRandomSelection
        if (AIDObj.Threshold_Value_for_Biased_Random_Selection.length() > 0)
            strFormatedAIDData.append("df15" +String.format(Locale.ENGLISH,"%02d",AIDObj.Threshold_Value_for_Biased_Random_Selection.length()/2)+AIDObj.Threshold_Value_for_Biased_Random_Selection);

        // Adding Maximum_Target_Percentage_for_Biased_Random_Selection
        if (AIDObj.Maximum_Target_Percentage_for_Biased_Random_Selection.length() > 0)
            strFormatedAIDData.append("df16" +String.format(Locale.ENGLISH,"%02d",AIDObj.Maximum_Target_Percentage_for_Biased_Random_Selection.length()/2)+AIDObj.Maximum_Target_Percentage_for_Biased_Random_Selection);

        // Adding Target_Percentage
        if (AIDObj.Target_Percentage.length() > 0)
            strFormatedAIDData.append("df17" +String.format(Locale.ENGLISH,"%02d",AIDObj.Target_Percentage.length()/2)+AIDObj.Target_Percentage);

        // Adding Default_DDOL
        if (AIDObj.Default_DDOL.length() > 0)
            strFormatedAIDData.append("df14" +String.format(Locale.ENGLISH,"%02d",AIDObj.Default_DDOL.length()/2)+AIDObj.Default_DDOL);
        // Adding Default_TDOL
        if (AIDObj.Default_TDOL.length() > 0)
            strFormatedAIDData.append("df8102" +String.format(Locale.ENGLISH,"%02d",AIDObj.Default_TDOL.length()/2)+AIDObj.Default_TDOL);

        // Adding Contactless floor limit
        //"DF19" ClssFloorLimit
        if(PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_Contactless_Floor_Limit.length()>0)
        strFormatedAIDData.append("DF19"+String.format(Locale.ENGLISH,"%02d",PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_Contactless_Floor_Limit.length()/2)+PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_Contactless_Floor_Limit);
        //adding Contactless Transaction limit
        //"DF20" ClssTxnLimit
        if(PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_Contactless_Transaction_Limit.length()>0)
        strFormatedAIDData.append("DF20"+String.format(Locale.ENGLISH,"%02d",PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_Contactless_Transaction_Limit.length()/2)+PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_Contactless_Transaction_Limit);
        //adding Contactless CVM limit
        //"DF21" ClssCVMLimit
        if(PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_CVM_Required_Limit.length()>0)
        strFormatedAIDData.append("DF21"+String.format(Locale.ENGLISH,"%02d",PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_CVM_Required_Limit.length()/2)+PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_CVM_Required_Limit);


	/*
				>>>>>>>>>>>>>>>>>>>>>>>>>> NOTES <<<<<<<<<<<<<<<<<<<<<<<

		 - Floor limits Value = Cardschema floor limits
		 - Contactless Limits (CVM , FLoor,Reader) would be mada for all card schemes and AIDs
	*/

        // Log AID formated message

        return strFormatedAIDData.toString();

    }



    /*
Topwise Sample formated string
------------------------------

PUBLICKEY=9F0605A0000000659F220109DF05083230303931323331DF060101DF070101DF028180B72A8FEF5B27F2B550398FDCC256F714BAD497FF56094B7408328CB626AA6F0E6A9DF8388EB9887BC930170BCC1213E90FC070D52C8DCD0FF9E10FAD36801FE93FC998A721705091F18BC7C98241CADC15A2B9DA7FB963142C0AB640D5D0135E77EBAE95AF1B4FEFADCF9C012366BDDA0455C1564A68810D7127676D493890BDDF040103DF03144410C6D51C2F83ADFD92528FA6E38A32DF048D0A




9F06 RID                                      [5]    >> A000000065
9F22 Certification Authority Public Key Index [1]    >> 09
DF05 CA Public Key Expiry Date                [8]    >> 3230303931323331 // Length should be 4
DF06 Hash ID                                  [1]    >> 01
DF07 Digital Signature ID                     [1]    >> 01
DF02 Public Key Value                         [Var]  >> B72A8FEF5B27F2B550398FDCC256F714BAD497FF56094B7408328CB626AA6F0E6A9DF8388EB9887BC930170BCC1213E90FC070D52C8DCD0FF9E10FAD36801FE93FC998A721705091F18BC7C98241CADC15A2B9DA7FB963142C0AB640D5D0135E77EBAE95AF1B4FEFADCF9C012366BDDA0455C1564A68810D7127676D493890BD
DF04 Exponent                                 [01]   >> 03
DF03 Check Sum                                [20]   >> 4410C6D51C2F83ADFD92528FA6E38A32DF048D0A

*/

    public void Start_Transaction(POSTransaction oPos_trans, POSTransaction.TranscationType Trxtype) {
        Start_Transaction(oPos_trans, Trxtype, null);
    }


    /**
     Header  PerfomTermHostResponseFlow
     \function Name: PerfomTermHostResponseFlow
     \Param  :
     \Return : int
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 7/15/2020
     \Des    : Host response parsing logic
     After Terminal successefuly send and receive message it should do the following

     0-  Once terminal received response it would set Response Received Time buffer for next message.  ?????
     1-  Parse response message into ISO8583 format
     2-  Check and validate Host MAC Block DE 64 / 128
     3-  Get Host Action Code DE 39 to check message is approved or rejected (Approved response code as per Specification document part b  section 3.2.20 DE 39 – Action Code)
     4-  Get and Check Host Approval (Authorization code) , 3.2.19 DE 38 – Approval Code
     5-  Check Host MAC block
     6-  Check Data Element 47 host card scheme switch for all transaction EXCEPT  metwork ,file download , reconciliation
     7-  Check Data Element 44 tokenised card transactions (PAR,FPAN) , 3.2.23 DE 44 - Additional Response Data
     8-  Update LEDs with tranasction status (Approved/DEcline) Red /Green Contactless
     9-  Check tag 8A , 91 in case of ICC transaction and map value of tag 8A,
     10- Perform 2nd Generation and issuer script
     11- Display Transaction result (from TMS downloaded message based on action code)and print receipt
     12- Display Authorization Code (Approval Code)
     13- Update Terminal totals
     14- Check Save in SAF if required
     15- Check for Force reconciliation
     16- Check for Force TMS download
     17- DeSAF for SAF processing
     18- Save terminal operation data
     19- Disconnected
     20- Check for integration Result




     Note:
     * Message could be repeated in case of timeout
     - Authorization Advice: 1121
     - Reversal:	         1421
     - Purchase Advice:	     1221
     - File Down:            130X
     - Network               1804
     - Reconciliation        15XX
     * If user remove ICC card during transaction StartCardRemovedTransaction() should be implemented (reversal , receipts,...)

     */
    public int PerfomTermHostResponseFlow(byte[] recePacket, int errReason)
    {
        boolean bRetRes;


        Process_Rece_Packet( recePacket);
        Log.i(TAG, "Process_Rece_Packet: ");
        ValidateHostMAC();

        if(!CheckHostActionCode(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(39).toString()))
        {
            //todo declined transaction check response code and print message+
            //todo Update LEDs with tranasction status (DEcline) Red  Contactless
        }
        PosApplication.getApp().oGPosTransaction.m_sApprovalCode=PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(38).toString();
        Check_DE44(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(44));
        Check_DE47(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(47));
        //todo Update LEDs with tranasction status (Approved) Green Contactless
        Check_DE55(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(55));
        //todo Perform 2nd Generation and issuer script if exist
        SaveLastTransaction(PosApplication.getApp().oGPosTransaction,CurrentSaving.REMOVE);
        //todo reciept printing



        Update_Terminal_totals();



        if(errReason==-1||errReason==-2) //timeout//cannot send
        {
            CheckandSaveInSAF(PosApplication.getApp().oGPosTransaction, true);
        }
        else
        {
            CheckandSaveInSAF(PosApplication.getApp().oGPosTransaction, false);
        }
        if(!CheckForceReconciliation()) {


            if(!CheckForTMSDownload()) {
                if(PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag)
                    DeSAF(SAF_Info.DESAFtype.PARTIAL);
                else
                Update_TermData();
            }

            //clear flags

        }

        //todo GetDateTime(int iFormat);           // iFormat , All , HHSS,YYMM,




        ////////////////////////////////////////////////////
        return 0;
    }



    /**
     Header  Update_Terminal_totals
     \function Name: Update_Terminal_totals
     \Param  :
     \Return : int
     \Pre    :
     \Post   :
     \Author	: Mostafa Hussiny
     \DT		: 7/14/2020
     \Des    : update terminal operation data class then save it incase of power off
     */
    private static void Update_TermData() {
    }


    private static void Update_Terminal_totals() {

        PosApplication.getApp().oGTerminal_Operation_Data.m_dTermReconciliationAmount=+GetTrxCumulativeAmount(PosApplication.getApp().oGPosTransaction);
        // update card scheme totals
    }

    private static void Check_DE55(byte[] dataElement) {

    }

    private static void Check_DE47(byte[] dataElement) {
    }

    private static void Check_DE44(byte[] dataElement) {
    }


    @Override
    public void showConnectionStatus(int connectionStatus) {

    }

    @Override
    public void onSuccess(byte[] receivedPacket) {

        if(PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag&!PosApplication.getApp().oGTerminal_Operation_Data.breconsile_flag) {
            Parse_DeSAF_Response(receivedPacket);

            if (PosApplication.getApp().oGTerminal_Operation_Data.saf_info.DeSAF_count >0)
                               DeSAF(SAF_Info.DESAFtype.PARTIAL);
        }

        if(PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag&PosApplication.getApp().oGTerminal_Operation_Data.breconsile_flag)// RECONSILE
                if(PosApplication.getApp().oGTerminal_Operation_Data.saf_info.m_iSAFTrxNumber>0)
                {
                    Parse_DeSAF_Response(receivedPacket);
                    DeSAF(SAF_Info.DESAFtype.FULL);
                }
                else
                {
                    Parse_DeSAF_Response(receivedPacket);
                    PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag=false;
                    StartReconciliation(false);
                }
        if(!PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag&!PosApplication.getApp().oGTerminal_Operation_Data.breconsile_flag)
             StartReconciliation(false);
        if (PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag&PosApplication.getApp().oGTerminal_Operation_Data.bTMS_flag) {
            if (PosApplication.getApp().oGTerminal_Operation_Data.saf_info.m_iSAFTrxNumber > 0) {
                DeSAF(SAF_Info.DESAFtype.FULL);
            } else {
                StartTMSDownload(false);
            }
        }
        if(!PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag&PosApplication.getApp().oGTerminal_Operation_Data.bTMS_flag)
            StartTMSDownload(false);



    }



    @Override
    public void onFailure(int errReason) {

    }

    enum Flags{ ForceTMS,
        FORCERECONCILATION
    }
    private static boolean Check_DE62(byte[] byte_dataElement, Flags flagstype) {
        boolean bRetRes = false;
        String sDE62;
        sDE62=byte_dataElement.toString();
        switch(flagstype)
        {
            case ForceTMS:


                if (sDE62.contains("061") )
                    bRetRes = true;
            case FORCERECONCILATION:
                if (sDE62.contains("081"))
                    bRetRes=true;

        }
        return bRetRes;
    }



    public static boolean Process_Rece_Packet(byte[] recePacket) {
        boolean bRetRes;
        Log.i(TAG, "Process_Rece_Packet("+recePacket+")");


        if (PosApplication.getApp().oGPosTransaction.m_enmTrxType == POSTransaction.TranscationType.PURCHASE) {
            UnpackPurchase unpackpurchase = new UnpackPurchase(recePacket, recePacket.length);
            PosApplication.getApp().oGPosTransaction.m_sActionCode = unpackpurchase.getResponse();
            return true;
        }
        return false;



    }
    public static boolean CheckHostActionCode(String sDE39)
    {
        boolean bRetRes = false ;
        switch(sDE39)
        {
            case "000":
            case "001":
            case "003":
            case "007":
            case "060":
            case "087":
            case "089":
            case "400": // Reversal
            case "913": // Reversal Duplicate transmission , Specification part b Section 4.3.16 Reversal Transaction Timeout
            case "500": // Reconciliation
            case "800": // Network
                return true; // Approved transaction
            default :
                return bRetRes;
        }

    }

    public void Start_Transaction(POSTransaction oPos_trans, POSTransaction.TranscationType Trxtype, ITransaction.View transactionView) {
        PosApplication.getApp().oGPosTransaction.Reset();


        Intent AmountACT;
        switch (Trxtype) {
            case PURCHASE://purchase

                //get amount
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type", Trxtype);
                mcontext.startActivity(AmountACT);
                break;
            case PURCHASE_WITH_NAQD://PURCHASE_WITH_NAQD
                //get amount
                AmountACT = new Intent(mcontext, P_NAQD_InputActivity.class);
                AmountACT.putExtra("transaction Type", Trxtype);
                mcontext.startActivity(AmountACT);

                break;
            case REFUND://REFUND

                // TODO get original transaction Type
                AmountACT = new Intent(mcontext, Refund_InputActivity.class);
                AmountACT.putExtra("transaction Type", Trxtype);
                mcontext.startActivity(AmountACT);

                break;
            case AUTHORISATION://AUTHORISATION:


                //get amount
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type", Trxtype);
                mcontext.startActivity(AmountACT);

                break;
            case AUTHORISATION_ADVICE://AUTHORISATION_ADVICE:
                //get amount
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type", Trxtype);
                mcontext.startActivity(AmountACT);
                break;
            case AUTHORISATION_VOID://AUTHORISATION_VOID:
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type", Trxtype);
                break;
            case AUTHORISATION_EXTENSION://AUTHORISATION_EXTENSION
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type", Trxtype);
                break;
            case PURCHASE_ADVICE://PURCHASE_ADVICE:
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type", Trxtype);

                break;

            case CASH_ADVANCE://CASH_ADVANCE:
                //todo start cash advance
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type", Trxtype);
                break;
            case REVERSAL://REVERSAL
                //todo check host transaction
                //todo check reversal time out
                //todo check Transaction mode
                POSTransaction oReversal_Trx = null;
                perform_reversal(oPos_trans, oReversal_Trx);


                break;
            case SADAD_BILL://SADAD_BILL:
                //todo  sadad
                break;
            case RECONCILIATION://RECONCILIATION:
                //todo
                break;
            case TMS_FILE_DOWNLOAD://TMS_FILE_DOWNLOAD:

                Intent TMSprocess = new Intent(mcontext, PacketProcessActivity.class);
                TMSprocess.putExtra("transaction Type", Trxtype);

                break;
            case TERMINAL_REGISTRATION://TREMINAL_REGISTRATION:

                PosApplication.getApp().oGTerminal_Registeration.StartRegistrationProcess(PosApplication.getApp().oGPosTransaction, transactionView);

                break;
            case ADMIN://ADMIN:
                break;




        }


    }




    public static int     GetTMSTranResponseMessage(String sActionCode){
        int iRetRes=-1;

        return iRetRes;
    }

    //for host response
    public boolean CheckForTMSDownload()
    {   boolean bRetRes=false;
        //todo check TMS flag in DE 62 for TMS
        bRetRes=Check_DE62(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(62),Flags.ForceTMS);
        Log.i(TAG, "ValidateHostResponse: CheckForTMSDownload: " + bRetRes);
        if(bRetRes) {
            PosApplication.getApp().oGTerminal_Operation_Data.bTMS_flag = true;
            DeSAF(SAF_Info.DESAFtype.FULL);   //Desaf will performe TMS download after finishing
        }
        return bRetRes;
    }
    private void StartTMSDownload(boolean bForced) {

        Log.i(TAG, "StartTMSDownload: "+bForced);
        int iResult;
        if(!bForced)
        {
        }
        iResult=PosApplication.getApp().oGPosTransaction.ComposeFileDownloadMessage();
        Log.i(TAG, "ComposeFileDownloadMessage: "+iResult);
        byte[]TMSreq = PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
        Log.i(TAG, "TMS request buffer: "+TMSreq);


        CommunicationsHandler communicationsHandler = CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));

        communicationsHandler.setSendReceiveListener(this);
        communicationsHandler.sendReceive(TMSreq);
    }

    // for Host response
    public boolean	CheckForceReconciliation()
    {   boolean bRetRes=false;
        Log.i(TAG, "ValidateHostResponse: CheckForceReconciliation: "+bRetRes);
            bRetRes =Check_DE62(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(62),Flags.FORCERECONCILATION);
            if(bRetRes) {
                PosApplication.getApp().oGTerminal_Operation_Data.breconsile_flag=true;
                DeSAF(SAF_Info.DESAFtype.FULL);   //Desaf will performe reconsile after finishing

            }
        return bRetRes;
    }

    private void StartReconciliation(boolean bForced) {
        Log.i(TAG, "StartReconciliation: "+bForced);
        int iResult;
        if(!bForced)
        {
        }
       iResult=PosApplication.getApp().oGPosTransaction.CompoaseReconciliationMessage();
        Log.i(TAG, "CompoaseReconciliationMessage: "+iResult);
        byte[]sendtotals = PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
        Log.i(TAG, "Reconciliation buffer: "+sendtotals);


        CommunicationsHandler communicationsHandler = CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));

        communicationsHandler.setSendReceiveListener(this);
        communicationsHandler.sendReceive(sendtotals);



    }

    public int DeSAF(SAF_Info.DESAFtype en_Type) // iType full , partial (only 2 transaction)
    {   POSTransaction SAF_transaction=null;
        int iRetRes=-1;

        switch (en_Type)
        {
            case PARTIAL:
                if (PosApplication.getApp().oGTerminal_Operation_Data.saf_info.DeSAF_partial_count==0)
                {
                    //partial SAF finished nex transaction checks after SAF
                }
                else {
                    if (PosApplication.getApp().oGTerminal_Operation_Data.breversal_flg) {
                        SAF_transaction = SAF_Info.Load_from_Reversal();
                        PosApplication.getApp().oGTerminal_Operation_Data.saf_info.DeSAF_partial_count--;
                        PosApplication.getApp().oGTerminal_Operation_Data.saf_info.m_iSAFTrxNumber--;
                        PosApplication.getApp().oGPosTransaction.ComposeReversalMessage(); //1420
                        byte[] sendPacket = PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
                        CommunicationsHandler communicationsHandler = CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));
                        communicationsHandler.setSendReceiveListener(this);
                        communicationsHandler.sendReceive(sendPacket);
                    }
                    else {
                        SAF_transaction = SAF_Info.Load_from_SAF();
                        PosApplication.getApp().oGTerminal_Operation_Data.saf_info.DeSAF_partial_count--;
                        PosApplication.getApp().oGTerminal_Operation_Data.saf_info.m_iSAFTrxNumber--;
                            switch (SAF_transaction.m_enmTrxType) {
                                case PURCHASE_ADVICE: //1220
                                    PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(SAF_transaction.m_enmTrxType);
                                    break;
                                case AUTHORISATION_ADVICE://1120
                                    PosApplication.getApp().oGPosTransaction.ComposeAuthorisationAdviseMessage(SAF_transaction.m_enmTrxType);
                                    break;
                            }
                        byte[] sendPacket = PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
                        CommunicationsHandler communicationsHandler = CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));
                        communicationsHandler.setSendReceiveListener(this);
                        communicationsHandler.sendReceive(sendPacket);
                    }

                }
                break;

            //todo performe message building and send and recive for reversal advice and SAF transaction
            case FULL:
                   if(PosApplication.getApp().oGTerminal_Operation_Data.breversal_flg) {
                    SAF_transaction = SAF_Info.Load_from_Reversal();
                    PosApplication.getApp().oGTerminal_Operation_Data.saf_info.m_iSAFTrxNumber--;
                    PosApplication.getApp().oGPosTransaction.ComposeReversalMessage();
                    byte[] sendPacket = PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
                    CommunicationsHandler communicationsHandler = CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));
                    communicationsHandler.setSendReceiveListener(this);
                    communicationsHandler.sendReceive(sendPacket);



                }
                else {


                        SAF_transaction = SAF_Info.Load_from_SAF();
                       PosApplication.getApp().oGTerminal_Operation_Data.saf_info.m_iSAFTrxNumber--;

                       switch (SAF_transaction.m_enmTrxType) {
                           case PURCHASE_ADVICE: //1220
                               PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(SAF_transaction.m_enmTrxType);
                               break;
                           case AUTHORISATION_ADVICE://1120
                               PosApplication.getApp().oGPosTransaction.ComposeAuthorisationAdviseMessage(SAF_transaction.m_enmTrxType);
                               break;
                       }
                        byte[] sendPacket = PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
                        CommunicationsHandler communicationsHandler = CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));
                        communicationsHandler.setSendReceiveListener(this);
                        communicationsHandler.sendReceive(sendPacket);



                }
                break;

        }
        return iRetRes;
    }

    public static int Parse_DeSAF_Response(byte[] recePacket)
    {
        int iRetres=-1;

        Process_Rece_Packet( recePacket);
        Log.i(TAG, "Process_Rece_Packet: ");
        ValidateHostMAC();

        if(!CheckHostActionCode(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(39).toString()))
        {
            //todo declined transaction check response code and print message or not for saf resonse

        }
        PosApplication.getApp().oGPosTransaction.m_sApprovalCode=PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(38).toString();

        return iRetres;
    }
    public static int CheckandSaveInSAF(POSTransaction POSTrx, boolean bIsReversal)
    {
        // todo checking transaction is eligable to be saved in SAF or not
        int iRetRes=-1;
        if (bIsReversal) {
            POSTrx = SAF_Info.BuildSAFOriginals(POSTrx, PosApplication.getApp().oGPosTransaction);
            POSTrx.m_enmTrxType = POSTransaction.TranscationType.REVERSAL;
            SAF_Info.SAVE_IN_REV(POSTrx);
        }
        else {
            if(POSTrx.m_is_mada)
            {
                switch (POSTrx.m_enmTrxType){
                    case PURCHASE:

                        if((POSTrx.m_enmTrxCardType== POSTransaction.CardType.ICC||POSTrx.m_enmTrxCardType== POSTransaction.CardType.CTLS)&POSTrx.m_bIsOfflineTrx) {//Offline Financial Transaction – mada Chip/Contactless Card(Approved)8
                            POSTrx=SAF_Info.BuildSAFOriginals(POSTrx,PosApplication.getApp().oGPosTransaction);
                            POSTrx.m_enmTrxType = POSTransaction.TranscationType.PURCHASE_ADVICE;
                           SAF_Info.SAVE_IN_SAF(POSTrx);
                        }
                        //todo save  Offline Financial Transaction – mada Chip/Contactless Card (Declined)
                        break;
                    case AUTHORISATION:
                       break;

                }

            }
            else //ICS
            {
                switch (POSTrx.m_enmTrxType){
                    case PURCHASE:
                        if(POSTrx.m_enmTrxCardType== POSTransaction.CardType.ICC) {
                            POSTrx=SAF_Info.BuildSAFOriginals(POSTrx,PosApplication.getApp().oGPosTransaction);
                            POSTrx.m_enmTrxType = POSTransaction.TranscationType.PURCHASE_ADVICE;
                            SAF_Info.SAVE_IN_SAF(POSTrx);
                        }
                    case AUTHORISATION:
                        if(POSTrx.m_enmTrxCardType== POSTransaction.CardType.ICC) {
                            POSTrx=SAF_Info.BuildSAFOriginals(POSTrx,PosApplication.getApp().oGPosTransaction);
                            POSTrx.m_enmTrxType = POSTransaction.TranscationType.AUTHORISATION_ADVICE;
                            SAF_Info.SAVE_IN_SAF(POSTrx);
                        }

                }
            }
        }
        PosApplication.getApp().oGTerminal_Operation_Data.saf_info.m_dSAFCumulativeAmount=+Integer.parseInt(POSTrx.m_sTrxAmount);
        PosApplication.getApp().oGTerminal_Operation_Data.saf_info.m_iSAFTrxNumber++;
        return iRetRes;

    }
    public enum CurrentSaving{ SAVE,
                        REMOVE}
    public static int     SaveLastTransaction(POSTransaction POSTrx,CurrentSaving enumsave) // IState = Save or Remove  , Used to save curren tranaction incase of power failure and removed it after ending
    {
        int iRetRes=-1;


            //todo  mohamed save transaction in DB
        return iRetRes;
    }

    public static int GetDateTime(int iFormat)           // iFormat , All , HHSS,YYMM
    {
        int iRetRes=-1;
            // check with moamend this function for update date and time from host
        return iRetRes;
    }

    /**
            \Function Name: CheckReconciliationLimits
	\Param  :
            \Return : boolean
	\Pre    :
            \Post   :
            \Author	: Moamen Ahmed
	\DT		: 13/07/2020
            \Des    : Check Reconciliation cumulative limits for DESAFing processing (Full) , TMS limits should be loaded for checking , Specification document part b Page 227 ,
            */

    public boolean CheckReconciliationLimits()
    {
        boolean bRetRes =  false;
        // Checking Reconciliation Limits 4.16
        if(PosApplication.getApp().oGTerminal_Operation_Data.m_dTermReconciliationAmount >= Double.parseDouble(PosApplication.getApp().oGSama_TMS.device_specific.m_sMax_Reconciliation_Amount ))
        {
            //Todo Log message

            bRetRes = true ;


        }

        // Checking Number approved transaction  4.17

        if(PosApplication.getApp().oGTerminal_Operation_Data.m_iTermApprovedTrxCounter >= Integer.parseInt(PosApplication.getApp().oGSama_TMS.device_specific.m_sMax_Transactions_Processed ))
        {
            //Todo Log message

            bRetRes = true ;


        }
        return bRetRes;
    }

/**
	\Function Name: GetTrxCumulativeAmount
	\Param  : POSTransaction POSTrx
	\Return : double
	\Pre    :
	\Post   :
	\Author	: Moamen Ahmed
	\DT		: 13/07/2020
	\Des    : Get Transaction amout to be updated on  m_dTermReconciliationAmount buffer for SAF Checking , not including Authorization amount
*/

    public static double GetTrxCumulativeAmount(POSTransaction POSTrx)


    {
        double dAmount =0;
        // Todo Log Start message

        switch (POSTrx.m_enmTrxType)
        {
            case PURCHASE :
            case PURCHASE_WITH_NAQD :
            case PURCHASE_ADVICE:
            case CASH_ADVANCE:
            case REFUND :
            {
                if(POSTrx.m_sProcessCode.equals("00") ||
                        POSTrx.m_sProcessCode.equals("01") ||
                        POSTrx.m_sProcessCode.equals("09"))
                {
                    dAmount = Double.parseDouble(POSTrx.m_sTrxAmount);
                }
                else  if(POSTrx.m_sProcessCode.equals("20"))
                    dAmount = Double.parseDouble(POSTrx.m_sTrxAmount) * -1;
            }
            break;
            case REVERSAL :
            {
                if(POSTrx.m_sProcessCode.equals("00") ||
                        POSTrx.m_sProcessCode.equals("01") ||
                        POSTrx.m_sProcessCode.equals("09"))
                {
                    dAmount = Double.parseDouble(POSTrx.m_sTrxAmount) * -1;
                }
                else  if(POSTrx.m_sProcessCode.equals("20"))
                    dAmount = Double.parseDouble(POSTrx.m_sTrxAmount);
            }
            break;
        }
        // Todo Log Endmessage with  return value

        return dAmount;
    }


    /**
     \Function Name: load_Terminal_configuration_file
     \Param  : POSTransaction POSTrx
     \Return : double
     \Pre    :
     \Post   :
     \Author	: mostafa hussiny
     \DT		: 00/08/2020
     \Des    : loading Terminal operation data from saved file of terminal configuration data
     */
    public static void load_Terminal_configuration_file() {

        //todo load file
        //todo copy data to oGSama_TMS in PosApplication


    }

    public boolean Check_manual_allowed() {
        return PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sManual_entry_allowed == "1";
    }

    /**
     \Function Name: Get_Terminal_Transaction_limits
     \Param  : void
     \Return : void
     \Pre    :
     \Post   :
     \Author	: mostafa hussiny
     \DT		: 09/08/2020
     \Des    : getting terminal global limit indicator and max amount values from mada card scheme
     */

    public static void Get_Terminal_Transaction_limits() {
        try {
            Card_Scheme mada_card_Scheme = DBManager.getInstance().getCardSchemeDao().getById("P1");
            PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator=mada_card_Scheme.m_sMaximum_transaction_amount_indicator;
            PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount=mada_card_Scheme.m_sMaximum_amount_allowed;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


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
