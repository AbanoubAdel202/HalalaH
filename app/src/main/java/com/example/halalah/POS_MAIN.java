package com.example.halalah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.halalah.TMS.AID_Data;
import com.example.halalah.TMS.Card_Scheme;
import com.example.halalah.TMS.Public_Key;
import com.example.halalah.TMS.SAMA_TMS;
import com.example.halalah.registration.view.ITransaction;
import com.example.halalah.card.CardManager;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.connect.SendReceiveListener;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.iso8583.ISO8583;
import com.example.halalah.packet.UnpackPacket;
import com.example.halalah.packet.UnpackResponse;
import com.example.halalah.print.Purchase_Print;
import com.example.halalah.print.Reconsile_Print;
import com.example.halalah.secure.DUKPT_KEY;
import com.example.halalah.sqlite.database.DBManager;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.storage.SaveLoadFile;
import com.example.halalah.ui.AmountInputActivity;
import com.example.halalah.ui.Display_PrintActivity;
import com.example.halalah.ui.P_NAQD_InputActivity;
import com.example.halalah.ui.PacketProcessActivity;
import com.example.halalah.ui.SearchCardActivity;
import com.example.halalah.util.BytesUtil;
import com.example.halalah.util.ExtraUtil;
import com.example.halalah.ui.Refund_InputActivity;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;

import java.util.Locale;

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
public class POS_MAIN implements SendReceiveListener {
    private static final String TAG = POS_MAIN.class.getSimpleName();
    public static boolean isforced;
    private static boolean cont;




    public enum Flowtrxtype {DESAF,
        RECONSILE,
        REVERSAL}
    public Flowtrxtype m_enumflowtype;



    public  Context mcontext;
        public POS_MAIN()
        {

        }




    public void Start_Transaction(POSTransaction oPos_trans, POSTransaction.TranscationType Trxtype, ITransaction.View transactionView)
    {
        PosApplication.getApp().oGPosTransaction.Reset();


        Intent intent;

        switch(Trxtype)
        {
            case PURCHASE://purchase

                //get amount
                intent= new Intent(mcontext, AmountInputActivity.class);
                intent.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(intent);
                break;
            case PURCHASE_WITH_NAQD://PURCHASE_WITH_NAQD
                //get amount
                /* AmountACT = new Intent(mcontext, P_NAQD_InputActivity.class);
                 AmountACT.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(AmountACT);*/
                PosApplication.getApp().oGPosTransaction.m_sTrxAmount="500.35";
                PosApplication.getApp().oGPosTransaction.m_sAdditionalAmount="200.80";

                PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.PURCHASE_WITH_NAQD;
                intent= new Intent(mcontext, SearchCardActivity.class);
                mcontext.startActivity(intent);



                break;
            case REFUND://REFUND




               /* // TODO get original transaction Type
                AmountACT = new Intent(mcontext, Refund_InputActivity.class);
                AmountACT.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(AmountACT);*/

                PosApplication.getApp().oGPosTransaction.m_sTrxAmount="500.35";
                PosApplication.getApp().oGPosTransaction.m_sOrigRRNumber="2233445566";
                PosApplication.getApp().oGPosTransaction.m_sOrigTrxDateTime="2008251212";
                PosApplication.getApp().oGPosTransaction.m_sOrigLocalTrxDateTime="200825121210";
                PosApplication.getApp().oGPosTransaction.m_sOrigMTI="1200";

                PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.REFUND;
                intent= new Intent(mcontext, SearchCardActivity.class);
                mcontext.startActivity(intent);

                break;
            case AUTHORISATION://AUTHORISATION:

                PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.AUTHORISATION;
                //get amount
                intent = new Intent(mcontext, AmountInputActivity.class);
                intent.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(intent);

             /*   PosApplication.getApp().oGPosTransaction.m_sTrxAmount="500.35";
                PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.AUTHORISATION;
                searchcard= new Intent(mcontext, SearchCardActivity.class);
                mcontext.startActivity(searchcard);*/

                break;
            case AUTHORISATION_ADVICE://AUTHORISATION_ADVICE:
                //get amount
               /* AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(AmountACT);*/

                PosApplication.getApp().oGPosTransaction.m_sTrxAmount="500.35";
                PosApplication.getApp().oGPosTransaction.m_sOrigRRNumber="121323";
                PosApplication.getApp().oGPosTransaction.m_sApprovalCode="123456";
                PosApplication.getApp().oGPosTransaction.m_sOrigRRNumber="2233445566";
                PosApplication.getApp().oGPosTransaction.m_sOrigTrxDateTime="2008251212";
                PosApplication.getApp().oGPosTransaction.m_sActionCode="000";
                PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.AUTHORISATION_ADVICE;
                intent= new Intent(mcontext, SearchCardActivity.class);
                mcontext.startActivity(intent);
                break;
            case AUTHORISATION_VOID://AUTHORISATION_VOID:
               /* AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type",Trxtype);*/
                PosApplication.getApp().oGPosTransaction.m_sApprovalCode="AVOIDA";
                PosApplication.getApp().oGPosTransaction.m_sActionCode="107";
                PosApplication.getApp().oGPosTransaction.m_is_final=true;
                PosApplication.getApp().oGPosTransaction.m_sTrxAmount="500.35";
                PosApplication.getApp().oGPosTransaction.m_sOrigRRNumber="121323";
                PosApplication.getApp().oGPosTransaction.m_sOrigAmount="500.35";
                PosApplication.getApp().oGPosTransaction.m_sOrigLocalTrxDateTime="121212121212";
                PosApplication.getApp().oGPosTransaction.m_sOrigMTI="1100";
                PosApplication.getApp().oGPosTransaction.m_sOrigSTAN="120";
                PosApplication.getApp().oGPosTransaction.m_sOrigTrxDateTime="101020201212";

                PosApplication.getApp().oGPosTransaction.m_sOrigAquirerInsIDCode="1234";
                PosApplication.getApp().oGPosTransaction.m_sOrigFWAquirerInsIDCode="00";

                PosApplication.getApp().oGPosTransaction.m_sOrigLocalTrxDate="12122020";
                PosApplication.getApp().oGPosTransaction.m_enum_OrigTRxtype= POSTransaction.TranscationType.AUTHORISATION;
                PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.AUTHORISATION_VOID;
                /*Bundle bundle=new Bundle();
                bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE);
                intent= new Intent(mcontext, PacketProcessActivity.class);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);*/
                intent= new Intent(mcontext, SearchCardActivity.class);
                mcontext.startActivity(intent);
                break;
            case AUTHORISATION_EXTENSION://AUTHORISATION_EXTENSION
                PosApplication.getApp().oGPosTransaction.m_sApprovalCode="EXTina";
                PosApplication.getApp().oGPosTransaction.m_sActionCode="107";
                PosApplication.getApp().oGPosTransaction.m_is_final=true;
                PosApplication.getApp().oGPosTransaction.m_sTrxAmount="500.35";
                PosApplication.getApp().oGPosTransaction.m_sOrigRRNumber="121323";
                PosApplication.getApp().oGPosTransaction.m_sOrigAmount="500.35";
                PosApplication.getApp().oGPosTransaction.m_sOrigLocalTrxDateTime="121212121212";
                PosApplication.getApp().oGPosTransaction.m_sOrigMTI="1100";
                PosApplication.getApp().oGPosTransaction.m_sOrigSTAN="120";
                PosApplication.getApp().oGPosTransaction.m_sOrigTrxDateTime="2008251212";

                PosApplication.getApp().oGPosTransaction.m_sOrigAquirerInsIDCode="1234";
                PosApplication.getApp().oGPosTransaction.m_sOrigFWAquirerInsIDCode="00";

                PosApplication.getApp().oGPosTransaction.m_sOrigLocalTrxDate="12122020";
                PosApplication.getApp().oGPosTransaction.m_enum_OrigTRxtype= POSTransaction.TranscationType.AUTHORISATION;
                PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.AUTHORISATION_EXTENSION;
                intent= new Intent(mcontext, SearchCardActivity.class);
                mcontext.startActivity(intent);
                break;
            case PURCHASE_ADVICE://PURCHASE_ADVICE:

                PosApplication.getApp().oGPosTransaction.m_sApprovalCode="ABCDEFG";
                PosApplication.getApp().oGPosTransaction.m_sActionCode="107";
                PosApplication.getApp().oGPosTransaction.m_is_final=true;
                PosApplication.getApp().oGPosTransaction.m_sOrigRRNumber="2233445566";
                PosApplication.getApp().oGPosTransaction.m_sOrigTrxDateTime="2008251212";
                intent = new Intent(mcontext, AmountInputActivity.class);
                intent.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(intent);

                break;

            case CASH_ADVANCE://CASH_ADVANCE:

                intent = new Intent(mcontext, AmountInputActivity.class);
                intent.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(intent);
                break;
            case REVERSAL://REVERSAL

                //todo check reversal time out
                //todo check Transaction mode
                PosApplication.getApp().oGPosTransaction=SaveLoadFile.Loadlasttransaction();
                if(!Check_transaction_allowed(POSTransaction.TranscationType.REVERSAL))
                {
                    //todo reversal not allowed for this transaction
                }

                PosApplication.getApp().oGPosTransaction.m_sLocalTrxDateTime = ExtraUtil.Get_Local_Date_Time();
                PosApplication.getApp().oGPosTransaction.reversal_status=1;
                POSTransaction oReversal_Trx = null;
                perform_reversal(PosApplication.getApp().oGPosTransaction,oReversal_Trx);


                break;
            case SADAD_BILL://SADAD_BILL:
                //todo  sadad
                break;
            case RECONCILIATION://RECONCILIATION:
                //todo
                break;
            case TMS_FILE_DOWNLOAD://TMS_FILE_DOWNLOAD:

                Intent TMSprocess= new Intent(mcontext, PacketProcessActivity.class);
                TMSprocess.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(TMSprocess);

                break;
            case TERMINAL_REGISTRATION://TREMINAL_REGISTRATION:

            case TERMINAL_REGISTRATION://TREMINAL_REGISTRATION:

                // based on Moamen Ahmed Registeration file , Terminal_Registeration.java also
                PosApplication.getApp().oGTerminal_Registeration.StartRegistrationProcess(
                        PosApplication.getApp().oGPosTransaction, transactionView);

                break;
            case ADMIN://ADMIN:
                break;




        }


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
    public boolean perform_reversal(POSTransaction oOriginal_Transaction,POSTransaction oReversal_Transaction)
    {

        //todo check reversal allowed for the original transaction or not
        oReversal_Transaction=oOriginal_Transaction;
        oReversal_Transaction=SAF_Info.BuildSAFOriginals(oReversal_Transaction,oOriginal_Transaction);
        oReversal_Transaction.m_enmTrxType= POSTransaction.TranscationType.REVERSAL;
        oReversal_Transaction.m_sMTI=PosApplication.MTI_Reversal_Advice;


        oReversal_Transaction.ComposeReversalMessage();
        byte[] mSendPacket=oReversal_Transaction.m_RequestISOMsg.isotostr();
        CommunicationsHandler communicationsHandler = CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));
        communicationsHandler.setSendReceiveListener(this);
        communicationsHandler.sendReceive(mSendPacket);

        //todo  save in saf the reversal advice then printing copy of reversal then performe DESAF



        return true;
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
    public static int Recognise_card(){
           int istate=-1;
            if(PosApplication.getApp().oGPosTransaction.m_enmTrxCardType==POSTransaction.CardType.MANUAL || PosApplication.getApp().oGPosTransaction.m_enmTrxCardType==POSTransaction.CardType.MAG )
               istate= SAMA_TMS.Get_card_scheme_BY_PAN(PosApplication.getApp().oGPosTransaction.m_sPAN);
            else
                istate=SAMA_TMS.Get_card_scheme_BY_AID(PosApplication.getApp().oGPosTransaction.m_sAID);

        if (Check_MADA_Card())
            PosApplication.getApp().oGPosTransaction.m_is_mada=true;
        else
            PosApplication.getApp().oGPosTransaction.m_is_mada=false;


        return istate;

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
                if ('1'==PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(0))
                return true;
                break;
            case PURCHASE_WITH_NAQD://PURCHASE_WITH_NAQD                                 offset 1
                if ('1'==PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(1))
                return true;
                break;
            case PURCHASE_ADVICE://PURCHASE_ADVICE:                                      offset 2
            case AUTHORISATION_ADVICE://AUTHORISATION_ADVICE:
                if ('1'==PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(2))
                return true;
                break;
            case REFUND://REFUND                                                         offset 3
                if ('1'==PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(3))
                return true;
                break;
            case AUTHORISATION://AUTHORISATION:                                          offset 4
                if ('1'==PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(4))
                return true;
                break;
            case CASH_ADVANCE://CASH_ADVANCE:                                            offset 5
                if ('1'==PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(5))
                return true;
                break;
            case REVERSAL://REVERSAL:                                                    offset 6
                if ('1'==PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(6))
                return true;
                break;
            case AUTHORISATION_EXTENSION://AUTHORISATION_EXTENSION                       offset 7
                if ('1'==PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(7)) {
                    return true;
                }else if(PosApplication.getApp().oGPosTransaction.m_is_mada)
                {

                    return true;
                }
                break;
            case AUTHORISATION_VOID://AUTHORISATION_VOID:                                offset 8
                if ('1'==PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(8))
                return true;
                else if(PosApplication.getApp().oGPosTransaction.m_is_mada)
                {

                    return true;
                }
                break;
            case SADAD_BILL://SADAD_BILL:                                                offset 9
                if ('1'==PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sTransactions_Allowed.charAt(9))
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
                if ('1'==PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(0))
                    istate=1;
                break;
            case PURCHASE_WITH_NAQD://PURCHASE_WITH_NAQD                                 offset 1
                if ('1'==PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(1))
                    istate=1;
                break;
            case PURCHASE_ADVICE://PURCHASE_ADVICE:                                      offset 2
            case AUTHORISATION_ADVICE://AUTHORISATION_ADVICE:
                if ('1'==PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(2))
                    istate=1;
                break;
            case REFUND://REFUND                                                         offset 3
                if ('1'==PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(3))
                    istate=1;
                break;
            case AUTHORISATION://AUTHORISATION:                                          offset 4
                if ('1'==PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(4))
                    istate=1;
                break;
            case CASH_ADVANCE://CASH_ADVANCE:                                            offset 5
                if ('1'==PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(5))
                    istate=1;
                break;
            case REVERSAL://REVERSAL:                                                    offset 6
                if ('1'==PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(6))
                    istate=1;
                break;
            case AUTHORISATION_EXTENSION://AUTHORISATION_EXTENSION                       offset 7
                if ('1'==PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(7)) {
                    istate=1;
                }else if(PosApplication.getApp().oGPosTransaction.m_is_mada)
                {

                    istate=1;
                }
                break;
            case AUTHORISATION_VOID://AUTHORISATION_VOID:                                offset 8
                if ('1'==PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(8))
                    istate=1;
                else if(PosApplication.getApp().oGPosTransaction.m_is_mada)
                {

                    istate=1;
                }
                break;
            case SADAD_BILL://SADAD_BILL:                                                offset 9
                if ('1'==PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount_indicator.charAt(9))
                    istate=1;
                else if(PosApplication.getApp().oGPosTransaction.m_is_mada)
                {

                    istate=1;
                }
                break;


        }

        if(istate==1) {

            if (Long.parseLong(PosApplication.getApp().oGPosTransaction.m_sTrxAmount.replace(".",""))>Long.parseLong(PosApplication.getApp().oGTerminal_Operation_Data.m_sMaximum_transaction_amount))
                istate=0;
            else
                istate=-1;
        }


        return istate;
    }
    public boolean Check_manual_allowed()
    {
        if(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sManual_entry_allowed=="1")
            return true;
        else
            return false;
    }
    public int Check_max_cashback(int cashbackamount)
    {
        //todo get max cash back

        return 0;
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
    public static boolean Check_MADA_Card()
    {
        if(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCard_Scheme_ID.equals("P1"))
        return true;

        return false;
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
        //ex. 9F0607A0000002282010
        // Adding AID
        if (AIDObj.AID.length() > 0) {
            AIDObj.AID= AIDObj.AID.replaceAll(" ", "");
            strFormatedAIDData.append("9F06" + String.format(Locale.ENGLISH, "%02d", AIDObj.AID.length()/2) + AIDObj.AID);
        }
        //ex.DF010100
        // Adding ApplicationSelectionIndicator(0:PartMatch,1:ExactMatch)
        strFormatedAIDData.append("DF010100");
        //ex.9F08020084
        // Adding ApplicationVersionNumber
        if (AIDObj.Terminal_AID_version_numbers.length() > 0)
            strFormatedAIDData.append("9F08" +"02"+AIDObj.Terminal_AID_version_numbers.substring(0,4));
        //ex.DF1105FC408CA800
        // Adding Default_action_code
        if (AIDObj.Denial_action_code.length() > 0)
        {
            AIDObj.Default_action_code=AIDObj.Default_action_code.replaceAll(" ","");
            strFormatedAIDData.append("DF11" +String.format(Locale.ENGLISH,"%02d",AIDObj.Default_action_code.length()/2)+AIDObj.Default_action_code);

        }


        //ex.DF1205FC408CF800
        // Adding Online_action_code
        if (AIDObj.Denial_action_code.length() > 0) {
            AIDObj.Online_action_code=AIDObj.Online_action_code.replaceAll(" ","");
            strFormatedAIDData.append("DF12" + String.format(Locale.ENGLISH, "%02d", AIDObj.Online_action_code.length() / 2) + AIDObj.Online_action_code);
        }
        //ex.DF13050010000000
        // Adding Denial_action_code
        if (AIDObj.Denial_action_code.length() > 0) {
            AIDObj.Denial_action_code=AIDObj.Denial_action_code.replaceAll(" ","");
            strFormatedAIDData.append("DF13" + String.format(Locale.ENGLISH, "%02d", AIDObj.Denial_action_code.length() / 2) + AIDObj.Denial_action_code);
        }
        //ex.9F1B0400000000
        strFormatedAIDData.append("9F1B"+String.format(Locale.ENGLISH,"%02d","00000000".length()/2)+"00000000");
        //ex.DF150400000290
        // Adding ThresholdValueforBiasedRandomSelection
        if (AIDObj.Threshold_Value_for_Biased_Random_Selection.length() > 0)
            strFormatedAIDData.append("DF15" +String.format(Locale.ENGLISH,"%02d",AIDObj.Threshold_Value_for_Biased_Random_Selection.length()/2)+AIDObj.Threshold_Value_for_Biased_Random_Selection);
        //ex.DF160199
        // Adding Maximum_Target_Percentage_for_Biased_Random_Selection
        if (AIDObj.Maximum_Target_Percentage_for_Biased_Random_Selection.length() > 0)
            strFormatedAIDData.append("DF16" +String.format(Locale.ENGLISH,"%02d",AIDObj.Maximum_Target_Percentage_for_Biased_Random_Selection.length()/2)+AIDObj.Maximum_Target_Percentage_for_Biased_Random_Selection);
        //ex.DF170199
        // Adding Target_Percentage
        if (AIDObj.Target_Percentage.length() > 0)
            strFormatedAIDData.append("DF17" +String.format(Locale.ENGLISH,"%02d",AIDObj.Target_Percentage.length()/2)+AIDObj.Target_Percentage);
        //ex.DF14039F3704
        // Adding Default_DDOL
        if (AIDObj.Default_DDOL.length() > 0) {
            AIDObj.Default_DDOL=AIDObj.Default_DDOL.replaceAll(" ", "");
            strFormatedAIDData.append("DF14" + String.format(Locale.ENGLISH, "%02d", AIDObj.Default_DDOL.length() / 2) + AIDObj.Default_DDOL);
        }
        //ex.DF180101
        // Adding Default_TDOL
        if (AIDObj.Default_TDOL.length() > 0) {
            AIDObj.Default_TDOL=AIDObj.Default_TDOL.replaceAll(" ","");
            strFormatedAIDData.append("DF8101" + String.format(Locale.ENGLISH, "%02d", AIDObj.Default_TDOL.length() / 2) + AIDObj.Default_TDOL);
        }
        //ex.9F7B06000000100000
        strFormatedAIDData.append("9F7B06000000100000");



        //DF1906000000100000
        // Adding Contactless floor limit
        //"DF19" ClssFloorLimit
        if(PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_Contactless_Floor_Limit!=null)
            strFormatedAIDData.append("DF19"+String.format(Locale.ENGLISH,"%02d",PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_Contactless_Floor_Limit.length()/2)+PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_Contactless_Floor_Limit);

        //DF2006000000100000
        //adding Contactless Transaction limit
        //"DF20" ClssTxnLimit
        if(PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_Contactless_Transaction_Limit!=null)
            strFormatedAIDData.append("DF20"+String.format(Locale.ENGLISH,"%02d",PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_Contactless_Transaction_Limit.length()/2)+PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_Contactless_Transaction_Limit);

        //DF2106000000100000
        //adding Contactless CVM limit
        //"DF21" ClssCVMLimit
        if(PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminal_CVM_Required_Limit!=null)
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




    public static String FormatCAKeys(Public_Key CAKeyObj)
    {
        int iRetRes = 0;
        StringBuilder strFormatedCAKey = new StringBuilder();

        if (CAKeyObj == null)
        {
            // log an error message
            return String.valueOf(iRetRes);
        }

        // Adding RID
        if (CAKeyObj.RID.length() > 0)
            strFormatedCAKey.append("9F06" +String.format(Locale.ENGLISH,"%02d",CAKeyObj.RID.length()/2)+CAKeyObj.RID.toString());
        else
        {
            //Todo Log message showing can not format sent key due to incorrect length of RID
            return String.valueOf(iRetRes) ;
        }


        // Adding Key_Index
        if (CAKeyObj.Key_Index.length() > 0)
            strFormatedCAKey.append("9F22" +String.format(Locale.ENGLISH,"%02d",CAKeyObj.Key_Index.length()/2)+CAKeyObj.Key_Index.toString());
        else
        {
            //Todo Log message showing can not format sent key due to incorrect length of Key_Index
            return String.valueOf(iRetRes);
        }

        // Adding CA_Public_Key_Expiry_Date
        if (CAKeyObj.CA_Public_Key_Expiry_Date.length() > 0)
            strFormatedCAKey.append("DF05" +/*String.format(Locale.ENGLISH,"%02d",CAKeyObj.CA_Public_Key_Expiry_Date.length()/2)*/"0420"+CAKeyObj.CA_Public_Key_Expiry_Date.toString());
        else
        {
            //Todo Log message showing can not format sent key due to incorrect length of CA_Public_Key_Expiry_Date
            return String.valueOf(iRetRes);
        }
        // Adding Hash_ID
        if (CAKeyObj.Hash_ID.length() > 0)
            strFormatedCAKey.append("DF06" +String.format(Locale.ENGLISH,"%02d",CAKeyObj.Hash_ID.length()/2)+CAKeyObj.Hash_ID.toString());
        else
        {
            //Todo Log message showing can not format sent key due to incorrect length of Hash_ID
            return String.valueOf(iRetRes);
        }


        // Adding Digital_Signature_ID
        if (CAKeyObj.Digital_Signature_ID.length() > 0)
            strFormatedCAKey.append("DF07" +String.format(Locale.ENGLISH,"%02d",CAKeyObj.Digital_Signature_ID.length()/2)+CAKeyObj.Digital_Signature_ID.toString());
        else
        {
            //Todo Log message showing can not format sent key due to incorrect length of Digital_Signature_ID
            return String.valueOf(iRetRes);
        }


        // formating  Public Key Value  and Public_Key length
        if (CAKeyObj.Public_Key.length() > 0 && CAKeyObj.CA_Public_Key_Length.length() > 0 )
            strFormatedCAKey.append("DF0281" + Integer.toHexString(CAKeyObj.Public_Key.toString().length()/2)+CAKeyObj.Public_Key.toString());
        else
        {
            //Todo Log message showing can not format sent key due to incorrect length of Public_Key
            return String.valueOf(iRetRes);
        }

        // Adding Exponent
        if (CAKeyObj.Exponent.length() > 0)
            strFormatedCAKey.append("DF04" +String.format(Locale.ENGLISH,"%02d",CAKeyObj.Exponent.length()/2)+CAKeyObj.Exponent.toString());
        else
        {
            //Todo Log message showing can not format sent key due to incorrect length of Exponent
            return String.valueOf(iRetRes);
        }

        // Adding Check_Sum
        if (CAKeyObj.Check_Sum.length() > 0)
            strFormatedCAKey.append("DF03" +Integer.toHexString(CAKeyObj.Check_Sum.toString().length()/2)/*String.format(Locale.ENGLISH,"%02d",CAKeyObj.Check_Sum.length()/2)*/+CAKeyObj.Check_Sum.toString());
        else
        {
            //Todo Log message showing can not format sent key due to incorrect length of Check_Sum
            return String.valueOf(iRetRes);
        }




        // Todo log Formated string for debugging

        return strFormatedCAKey.toString();

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
    public int PerfomTermHostResponseFlow(byte[] recePacket, int errReason, Activity activity)
    {
        int bRetRes;


        Process_Rece_Packet( recePacket);
        Log.i(TAG, "Process_Rece_Packet: ");

        if(!ValidateHostMAC())
        {
            //todo go to invalid macing process
        }
        String mResponse= BCDASCII.asciiByteArray2String(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(39));


        if(!CheckHostActionCode(mResponse))
        {
            //todo declined transaction check response code and print message+
            //todo Update LEDs with tranasction status (DEcline) Red  Contactless
        }
        else {
            PosApplication.getApp().oGPosTransaction.m_sApprovalCode = BCDASCII.asciiByteArray2String(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(38)) ;
            SaveLastTransaction(PosApplication.getApp().oGPosTransaction,CurrentSaving.SAVE);
            Check_DE44(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(44));
            Check_DE47(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(47));
            //todo Update LEDs with tranasction status (Approved) Green Contactless
            if(PosApplication.getApp().oGPosTransaction.m_enmTrxCardType== POSTransaction.CardType.ICC) {
                byte[] icc = PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(55);
                String ICCresp = BCDASCII.fromBCDToASCIIString(icc, 0, icc.length * 2, true);
                Check_DE55(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(55));
                // go do 2nd AC and Issuer if exist
                CardManager.getInstance().setRequestOnline(true, mResponse, ICCresp);

            }

          //  if(PosApplication.getApp().oGPosTransaction.m_enmTrxCardType!= POSTransaction.CardType.ICC) {

                SaveLastTransaction(PosApplication.getApp().oGPosTransaction, CurrentSaving.REMOVE);
                Savetransaction(PosApplication.getApp().oGPosTransaction);
                PosApplication.getApp().oGTerminal_Operation_Data.m_iTransactionCounter++;
                Display_printResult(activity, PosApplication.getApp().oGPosTransaction.m_sApprovalCode, PosApplication.getApp().oGPosTransaction.m_sApprovalCode, PosApplication.getApp().oGPosTransaction);
         //   }

        }

       /// if(PosApplication.getApp().oGPosTransaction.m_enmTrxCardType!= POSTransaction.CardType.ICC) {
            bRetRes = Update_Terminal_totals();
            Log.d(TAG, "PerfomTermHostResponseFlow:Update_Terminal_totals : bRetRes= " + bRetRes);


            if (errReason == -1 || errReason == -2) //timeout//cannot send
            {
                CheckandSaveInSAF(PosApplication.getApp().oGPosTransaction, true);
            } else {
                CheckandSaveInSAF(PosApplication.getApp().oGPosTransaction, false);
            }
            if (!CheckForceReconciliation()) {


                if (!CheckForTMSDownload()) {
                    if (PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag) {
                        PosApplication.getApp().oGTerminal_Operation_Data.saf_info.DeSAF_partial_count = 2;
                        DeSAF(SAF_Info.DESAFtype.PARTIAL);
                    } else {
                        Save_TermData();
                    }
                }

                //clear flags


            }
      //  }

        //todo GetDateTime(int iFormat);           // iFormat , All , HHSS,YYMM,



        activity.finish();
        ////////////////////////////////////////////////////
        return 0;
    }


    public void finalizing_EMV_transaction(Activity activity)
    {
        SaveLastTransaction(PosApplication.getApp().oGPosTransaction, CurrentSaving.REMOVE);
        Savetransaction(PosApplication.getApp().oGPosTransaction);
        PosApplication.getApp().oGTerminal_Operation_Data.m_iTransactionCounter++;
        Display_printResult(activity,PosApplication.getApp().oGPosTransaction.m_sApprovalCode, PosApplication.getApp().oGPosTransaction.m_sApprovalCode, PosApplication.getApp().oGPosTransaction);
        int bRetRes = Update_Terminal_totals();
        Log.d(TAG, "PerfomTermHostResponseFlow:Update_Terminal_totals : bRetRes= "+bRetRes);




            CheckandSaveInSAF(PosApplication.getApp().oGPosTransaction, false);

        if(!CheckForceReconciliation()) {


            if(!CheckForTMSDownload()) {
                if(PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag) {
                    PosApplication.getApp().oGTerminal_Operation_Data.saf_info.DeSAF_partial_count = 2;
                    DeSAF(SAF_Info.DESAFtype.PARTIAL);
                }
                else {
                    Save_TermData();
                }
            }

            //clear flags


        }

        //todo GetDateTime(int iFormat);           // iFormat , All , HHSS,YYMM,


    }
    private void Savetransaction(POSTransaction oGPosTransaction) {

        POSTransaction temp[]=null;
        POSTransaction transactions[]=null;
        if (PosApplication.getApp().oGTerminal_Operation_Data.m_iTransactionCounter>0) {
            temp = SaveLoadFile.LoadAllTransaction();
             transactions = new POSTransaction[temp.length + 1];
            System.arraycopy(temp, 0, transactions, 0, temp.length);
            transactions[temp.length] = oGPosTransaction;
        }
        else {
            transactions = new POSTransaction[1];
            transactions[0] = oGPosTransaction;
        }


        SaveLoadFile.Savetransactions(transactions);

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



    private static int Update_Terminal_totals() {
        int ret=-1;
        PosApplication.getApp().oGTerminal_Operation_Data.m_dTermReconciliationAmount=+GetTrxCumulativeAmount(PosApplication.getApp().oGPosTransaction);
        ret= CardSchemeTotals.UpdateTerminalTotals(PosApplication.getApp().oGPosTransaction);
    return ret;
    }

    private static void Check_DE55(byte[] dataElement) {

        // todo TLV parser

    }

    private static void Check_DE47(byte[] dataElement) {
        if (dataElement == null)
        {
            return;
        }

        //A 04  Card Scheme Sponsor ID
        byte[] Card_Scheme_Sponsor_ID = BytesUtil.subBytes(dataElement,0,4);
        PosApplication.getApp().oGPosTransaction.m_sCardSchemeSponsorID=BCDASCII.asciiByteArray2String(Card_Scheme_Sponsor_ID);
        //A 02 Card Scheme ID
        byte[] Card_Scheme_ID =BytesUtil.subBytes(dataElement,4,6);
        PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCard_Scheme_ID=BCDASCII.asciiByteArray2String(Card_Scheme_ID);

        //var ANS ..97  Additional Data;        including Bill / Fee Payment Data and future data as required.
        if(dataElement.length>6) {
            byte[] Additional_Data = BytesUtil.subBytes(dataElement, 6, dataElement.length);
        }
    }

    private static void Check_DE44(byte[] dataElement) {

        if(dataElement !=null)
        {
            String Payment_Account_Reference = BCDASCII.asciiByteArray2String(dataElement).substring(2,31);
            String FPAN_Suffix =BCDASCII.asciiByteArray2String(dataElement).substring(32,36);
        }
    }


    @Override
    public void showConnectionStatus(int connectionStatus) {

    }

    @Override
    public void onSuccess(byte[] receivedPacket) {

        Log.d(TAG, "POS_MAIN onSuccess: start");
        byte[] responseMTI=BytesUtil.subBytes(receivedPacket, 0, 4);
        String sresponseMTI=BCDASCII.asciiByteArray2String(responseMTI);
            if(sresponseMTI.equals("1434"))
            { //parse reversal

                Parse_reversal_Response(receivedPacket);
                CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext())).closeConnection();
            }
            else if(sresponseMTI.equals("1534"))
                {
                    Parse_RECONSILE_response(receivedPacket);

                    //printing reconciliation
                    CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext())).closeConnection();

                }


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
        if(!PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag&PosApplication.getApp().oGTerminal_Operation_Data.breconsile_flag)
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

        Log.d(TAG, "onFailure:  fail to send data");

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



            UnpackResponse unpackResponse = new UnpackResponse(recePacket, recePacket.length);
            PosApplication.getApp().oGPosTransaction.m_sActionCode = unpackResponse.getResponse();
            return true;





    }

    public static void Save_TermData() {

        SaveLoadFile.SAVETeminal_operation_Data(PosApplication.getApp().oGTerminal_Operation_Data);
    }
    public static boolean load_TermData() {

        PosApplication.getApp().oGTerminal_Operation_Data=SaveLoadFile.loadTeminal_operation_Data();
        return true;
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
            case "500": // Reconciliation in Balance
            case "501": // Reconciliation out of Balance
            case "800": // Network
                 Log.d(TAG, "CheckHostActionCode: approved transaction :"+sDE39);
                 bRetRes=true;
                return bRetRes; // Approved transaction

            default :
                Log.d(TAG, "CheckHostActionCode: approved transaction :"+sDE39);
                return bRetRes;

        }

    }

    public static boolean ValidateHostMAC()
    {
        Log.d(TAG, "ValidateHostMAC: start");
        ISO8583 oResponseTrx = PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg;

        byte[] bMAC=null;
        String sMACblockcaculated;

        if(PosApplication.getApp().oGPosTransaction.m_sMTI.equals(PosApplication.MTI_File_Action_Request)) {
            //0.Messa   ge Type Identifier
            bMAC = PosApplication.MTI_File_Action_Request_Response.getBytes();
            //1. Primary bitmap
            bMAC= BytesUtil.mergeBytes(bMAC,oResponseTrx.Getbitmap());
            //11.System Trace Audit Number
            bMAC= BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(11));

            //12.Date and Time, Local Transaction
            bMAC= BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(12));
            //39 Action code
            bMAC = BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(39));
            Log.d(TAG, "ValidateHostMAC:bMAC +DE 39: " + BCDASCII.bytesToHexString(bMAC));
            //53.Security Related Control Information
            bMAC= BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(53));
        }

        else if(PosApplication.getApp().oGPosTransaction.m_sMTI.equals(PosApplication.MTI_Terminal_Reconciliation_Advice))
        {
            //0.Messa   ge Type Identifier
            bMAC = PosApplication.MTI_Terminal_Reconciliation_Advice_Response.getBytes();
            //1. Primary bitmap
            bMAC= BytesUtil.mergeBytes(bMAC,oResponseTrx.Getbitmap());
            //11.System Trace Audit Number
            bMAC= BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(11));

            //12.Date and Time, Local Transaction
            bMAC= BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(12));
            //39 Action code
            bMAC = BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(39));
            Log.d(TAG, "ValidateHostMAC:bMAC +DE 39: " + BCDASCII.bytesToHexString(bMAC));
            //53.Security Related Control Information
            bMAC= BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(53));

            bMAC=BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(124));
        }
        else {

            if (PosApplication.getApp().oGPosTransaction.m_sMTI.equals(PosApplication.MTI_Authorisation_Request)) {
                //0.Messa   ge Type Identifier
                bMAC = PosApplication.MTI_Authorisation_Response.getBytes();
                Log.d(TAG, "ValidateHostMAC:bMAC MTI: " + BCDASCII.bytesToHexString(bMAC));

            } else if (PosApplication.getApp().oGPosTransaction.m_sMTI.equals(PosApplication.MTI_Authorisation_Advice)) {
                //0.Messa   ge Type Identifier
                bMAC = PosApplication.MTI_Authorisation_Advice_Response.getBytes();
                Log.d(TAG, "ValidateHostMAC:bMAC MTI: " + BCDASCII.bytesToHexString(bMAC));
            } else if (PosApplication.getApp().oGPosTransaction.m_sMTI.equals(PosApplication.MTI_Financial_Request)) {
                //0.Messa   ge Type Identifier
                bMAC = PosApplication.MTI_Financial_Reponse.getBytes();
                Log.d(TAG, "ValidateHostMAC:bMAC MTI: " + BCDASCII.bytesToHexString(bMAC));
            } else if (PosApplication.getApp().oGPosTransaction.m_sMTI.equals(PosApplication.MTI_Financial_Transaction_Advice)) {
                //0.Messa   ge Type Identifier
                bMAC = PosApplication.MTI_Financial_Transaction_Advice_response.getBytes();
                Log.d(TAG, "ValidateHostMAC:bMAC MTI: " + BCDASCII.bytesToHexString(bMAC));

            } else if (PosApplication.getApp().oGPosTransaction.m_sMTI.equals(PosApplication.MTI_Reversal_Advice)) {
                //0.Messa   ge Type Identifier
                bMAC = PosApplication.MTI_Reversal_Advice_Reponse.getBytes();
                Log.d(TAG, "ValidateHostMAC:bMAC MTI: " + BCDASCII.bytesToHexString(bMAC));
            }

            //1. Primary bitmap
            bMAC = BytesUtil.mergeBytes(bMAC,oResponseTrx.Getbitmap());
            Log.d(TAG, "ValidateHostMAC:bMAC +bitmap: " + BCDASCII.bytesToHexString(bMAC));
            //2.Primary Account Number (PAN)
            bMAC = BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(2));
            Log.d(TAG, "ValidateHostMAC:bMAC +DE 2: " + BCDASCII.bytesToHexString(bMAC));
            //3.Processing Code
            bMAC = BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(3));
            Log.d(TAG, "ValidateHostMAC:bMAC +DE 3: " + BCDASCII.bytesToHexString(bMAC));
            //4.Amount, Transaction
            bMAC = BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(4));
            Log.d(TAG, "ValidateHostMAC:bMAC +DE 4: " + BCDASCII.bytesToHexString(bMAC));
            //11.System Trace Audit Number
            bMAC = BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(11));
            Log.d(TAG, "ValidateHostMAC:bMAC +DE 11: " + BCDASCII.bytesToHexString(bMAC));
            //12.Date and Time, Local Transaction
            bMAC = BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(12));
            Log.d(TAG, "ValidateHostMAC:bMAC +DE 12: " + BCDASCII.bytesToHexString(bMAC));
            //39 Action code
            bMAC = BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(39));
            Log.d(TAG, "ValidateHostMAC:bMAC +DE 39: " + BCDASCII.bytesToHexString(bMAC));
            //47.National Data
            bMAC = BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(47));
            Log.d(TAG, "ValidateHostMAC:bMAC +DE 47: " + BCDASCII.bytesToHexString(bMAC));
            //53.Security Related Control Information
            bMAC = BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(53));
            Log.d(TAG, "ValidateHostMAC:bMAC +DE 53: " + BCDASCII.bytesToHexString(bMAC));
            //55.EMV Data
            if (PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.ICC | PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.CTLS) {
                bMAC = BytesUtil.mergeBytes(bMAC,oResponseTrx.getDataElement(55));
                Log.d(TAG, "ValidateHostMAC:bMAC +DE 55: " + BCDASCII.bytesToHexString(bMAC));
            }
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


            if (bMAC.length%8!=0) {
                for(int i=0 ;i<bMAC.length%8;i++)
                {

                    if(i==0) {
                        bMAC = BytesUtil.add(bMAC, (byte) 0x80);
                    }
                    else
                        bMAC = BytesUtil.add(bMAC, (byte) 0x00);


                }
            }
            else
            {
                bMAC = BytesUtil.add(bMAC, (byte) 0x80);
            }
        Log.d(TAG, "ValidateHostMAC: bmac: "+BCDASCII.bytesToHexString(bMAC));
        sMACblockcaculated = DUKPT_KEY.CaluclateMACBlock(bMAC);
        Log.d(TAG, "ValidateHostMAC: MAC block:  "+sMACblockcaculated);

            //removinglast 4 bytes
        sMACblockcaculated=sMACblockcaculated.substring(0,8);

        sMACblockcaculated=sMACblockcaculated.concat("FFFFFFFF");
            byte[]bresponsemac=oResponseTrx.getDataElement(64);
            String srespMAC=BCDASCII.bytesToHexString(bresponsemac);
        Log.d(TAG, "ValidateHostMAC: Response MAC block:  "+srespMAC);
            if(sMACblockcaculated.equals(srespMAC)) {
                Log.d(TAG, "ValidateHostMAC: result: true ");
                return true;
            }
        else {
                Log.d(TAG, "ValidateHostMAC: result: false ");
                return false;
            }
    }




    public static int     GetTMSTranResponseMessage(String sActionCode){
        int iRetRes=-1;

        return iRetRes;
    }

    //for host response
    public boolean CheckForTMSDownload()
    {   boolean bRetRes=false;
        byte[] DE62=PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(62);
    if(DE62!=null) {
        bRetRes = Check_DE62(DE62, Flags.ForceTMS);
        Log.i(TAG, "ValidateHostResponse: CheckForTMSDownload: " + bRetRes);
        if (bRetRes) {
            PosApplication.getApp().oGTerminal_Operation_Data.bTMS_flag = true;
            DeSAF(SAF_Info.DESAFtype.FULL);   //Desaf will performe TMS download after finishing
        }
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
    public  boolean	CheckForceReconciliation()
    {   boolean bRetRes=false;
        Log.i(TAG, "ValidateHostResponse: CheckForceReconciliation: "+bRetRes);
        byte[] DE62=PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(62);
        if(DE62!=null) {
            bRetRes = Check_DE62(DE62, Flags.FORCERECONCILATION);
            if (bRetRes) {
                PosApplication.getApp().oGTerminal_Operation_Data.breconsile_flag = true;
                DeSAF(SAF_Info.DESAFtype.FULL);   //Desaf will performe reconsile after finishing

            }
        }
        return bRetRes;
    }

    public boolean StartReconciliation(boolean bForced) {
        Log.i(TAG, "StartReconciliation: "+bForced);
        int iResult;

           bForced= isforced;


       iResult=PosApplication.getApp().oGPosTransaction.CompoaseReconciliationMessage();
        Log.i(TAG, "CompoaseReconciliationMessage: "+iResult);
        byte[]sendtotals = PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
        Log.i(TAG, "Reconciliation buffer: "+sendtotals);

        senddata(sendtotals);

        return true;

    }

    private void senddata(byte[] sendtotals) {
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
                        SAF_transaction = SAF_Info.Load_trx_from_SAF();
                        PosApplication.getApp().oGTerminal_Operation_Data.saf_info.DeSAF_partial_count--;
                        PosApplication.getApp().oGTerminal_Operation_Data.saf_info.m_iSAFTrxNumber--;
                            switch (SAF_transaction.m_enmTrxType) {
                                case PURCHASE_ADVICE: //1220
                                    SAF_transaction.ComposeFinancialAdviseMessage(SAF_transaction.m_enmTrxType);
                                    break;
                                case AUTHORISATION_ADVICE://1120
                                    SAF_transaction.ComposeAuthorisationAdviseMessage(SAF_transaction.m_enmTrxType);
                                    break;
                            }
                        byte[] sendPacket = SAF_transaction.m_RequestISOMsg.isotostr();

                        PosApplication.getApp().oGcommunicationsHandler = PosApplication.getApp().oGcommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));
                        PosApplication.getApp().oGcommunicationsHandler.setSendReceiveListener(this);
                       // communicationsHandler.sendReceive(sendPacket);
                        PosApplication.getApp().oGcommunicationsHandler.sendReceive(sendPacket);
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


                        SAF_transaction = SAF_Info.Load_trx_from_SAF();
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
        Save_TermData();
        return iRetRes;
    }

    public static int Parse_DeSAF_Response(byte[] recePacket)
    {
        int iRetres=-1;

        Process_Rece_Packet( recePacket);
        Log.i(TAG, "Process_Rece_Packet: ");
        if(!ValidateHostMAC())
        {
            //todo go to invalid macing process
        }

        if(!CheckHostActionCode(BCDASCII.bytesToHexString(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(39))))
        {
            //todo declined transaction check response code and print message or not for saf resonse

        }
        PosApplication.getApp().oGPosTransaction.m_sApprovalCode=BCDASCII.bytesToHexString(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(38));

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
            PosApplication.getApp().oGTerminal_Operation_Data.breversal_flg=true;
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
                            PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag=true;
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
                            PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag=true;
                        }
                        break;
                    case AUTHORISATION:
                        if(POSTrx.m_enmTrxCardType== POSTransaction.CardType.ICC) {
                            POSTrx=SAF_Info.BuildSAFOriginals(POSTrx,PosApplication.getApp().oGPosTransaction);
                            POSTrx.m_enmTrxType = POSTransaction.TranscationType.AUTHORISATION_ADVICE;
                            SAF_Info.SAVE_IN_SAF(POSTrx);
                            PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag=true;
                        }
                        break;

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
        PosApplication.getApp().oGTerminal_Operation_Data.breversal_flg=false;

        if (enumsave == CurrentSaving.SAVE)
        { PosApplication.getApp().oGTerminal_Operation_Data.breversal_flg=true;
        SaveLoadFile.Savelasttransaction(POSTrx);

        }
        else{
            PosApplication.getApp().oGTerminal_Operation_Data.breversal_flg=false;
        }
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
                if(POSTrx.m_sProcessCode.equals("000000") ||
                        POSTrx.m_sProcessCode.equals("010000") ||
                        POSTrx.m_sProcessCode.equals("090000"))
                {
                    dAmount = Double.parseDouble(POSTrx.m_sTrxAmount);
                    dAmount =dAmount/100;
                }
                else  if(POSTrx.m_sProcessCode.equals("200000"))
                    dAmount = Double.parseDouble(POSTrx.m_sTrxAmount) * -1;
                    dAmount = dAmount/100;
            }
            break;
            case REVERSAL :
            {
                if(POSTrx.m_sProcessCode.equals("000000") ||
                        POSTrx.m_sProcessCode.equals("010000") ||
                        POSTrx.m_sProcessCode.equals("090000"))
                {
                    dAmount = Double.parseDouble(POSTrx.m_sTrxAmount) * -1;
                    dAmount = dAmount/100;
                }
                else  if(POSTrx.m_sProcessCode.equals("200000"))
                    dAmount = Double.parseDouble(POSTrx.m_sTrxAmount);
                    dAmount = dAmount/100;
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
        // to copy initial terminal operation data
        SAMA_TMS Default_TMS = new SAMA_TMS();

        // default retailer data
        Default_TMS.retailer_data.m_sArabic_Receipt_1="Arabic_Receipt_1";
        Default_TMS.retailer_data.m_sArabic_Receipt_2="m_sArabic_Receipt_2";
        Default_TMS.retailer_data.m_sAutomatic_Load="0";
        Default_TMS.retailer_data.m_sTerminal_Capability="0000000";
        Default_TMS.retailer_data.m_sAdditional_Terminal_Capabilities="000000";
        Default_TMS.retailer_data.m_sCurrency_Symbol_Arabic="ريال";
        Default_TMS.retailer_data.m_sCurrency_Symbol_English="SAR";
        Default_TMS.retailer_data.m_sTerminal_Currency_Code="0682";
        Default_TMS.retailer_data.m_sTerminal_Country_Code="0682";
        Default_TMS.retailer_data.m_sTransaction_Currency_Exponent="2";
        Default_TMS.retailer_data.m_sSAF_Default_Message_Transmission_Number="2";
        Default_TMS.retailer_data.m_sSAF_Retry_Limit="3";
        Default_TMS.retailer_data.m_sDownload_Phone_Number="+01061456840";
        Default_TMS.retailer_data.m_sEMV_Terminal_Type="22";
        Default_TMS.retailer_data.m_sNext_load="0";
        Default_TMS.retailer_data.m_sReconciliation_time="233000";
        Default_TMS.retailer_data.m_sEnglish_Receipt_1="English_Receipt_1";
        Default_TMS.retailer_data.m_sEnglish_Receipt_2="English_Receipt_2";
        Default_TMS.retailer_data.m_sRetailer_Address_1_Arabic="هلا، المملكه العربيه السعوديه1 ";
        Default_TMS.retailer_data.m_sRetailer_Address_2_Arabic="هلا، المملكه العربيه السعوديه2 ";
        Default_TMS.retailer_data.m_sRetailer_Address_1_English="hala , saudiarabia 1";
        Default_TMS.retailer_data.m_sRetailer_Address_2_English="hala , saudiarabia 2";
        Default_TMS.retailer_data.m_sRetailer_Name_Arabic="تاجر مؤقت";
        Default_TMS.retailer_data.m_sRetailer_Name_English="temp Merchant";







        PosApplication.getApp().oGSama_TMS=Default_TMS;






    }

    /**
     \Function Name: check_hardware
     \Param  : POSTransaction POSTrx
     \Return : double
     \Pre    :
     \Post   :
     \Author	: mostafa hussiny
     \DT		: 00/08/2020
     \Des    : check_hardware printer contactless reader , chip , mag ,...etc
     */

    public static void check_hardware() {

        AidlPrinter mPrinterManager;
        mPrinterManager = DeviceTopUsdkServiceManager.getInstance().getPrintManager();
        int printState=-1;
                try {
                           printState = mPrinterManager.getPrinterState();
                          Log.i(TAG, "printState = " + printState);
                    }
                     catch (Exception e)
                    {
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

    private void Display_printResult(Activity activity,String response, String resDetail, POSTransaction POStrx) {
        Log.i(TAG, "Display_printResult(), response = " + response + ", resDetail = " + resDetail + ", printDetail = " + POStrx);
        if (POStrx.m_enmTrxType == POSTransaction.TranscationType.PURCHASE )
        {

        }

       Intent intent = new Intent(activity, Display_PrintActivity.class);

        activity.startActivity(intent);

    }


    private int Parse_RECONSILE_response(byte[] receivedPacket) {
        Log.d(TAG, "Parse_RECONSILE_response: Started");
        int iRetres=-1;
        int iNumberOfCardScheme;
        HostTotals hostTotals = null;
        Process_Rece_Packet( receivedPacket);
        Log.i(TAG, "Process_Rece_Packet: ");
        if(!ValidateHostMAC())
        {
            //todo go to invalid macing process
        }
            String sActionCodeDE39=BCDASCII.asciiByteArray2String(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(39));
        if(!CheckHostActionCode(sActionCodeDE39))
        {
        }
        else{

            if(sActionCodeDE39.equals("500")) {//process in Balance data
                //get DE124

                String sTotalsDE124=BCDASCII.asciiByteArray2String(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(124));
                iNumberOfCardScheme=Integer.parseInt(sTotalsDE124.substring(0,2));
                hostTotals=new HostTotals(iNumberOfCardScheme);
                hostTotals.cardSchemeTotals=fillHostReconciliationTotals(sTotalsDE124,iNumberOfCardScheme);
                hostTotals.inBalance=true;
                //todo Save HOSTTOTALS



            }
            else if (sActionCodeDE39.equals("501")) {       //process out of Balance
                //get DE124
                String sTotalsDE124=BCDASCII.asciiByteArray2String(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(124));
                iNumberOfCardScheme=Integer.parseInt(sTotalsDE124.substring(0,2));
                hostTotals=new HostTotals(iNumberOfCardScheme);
                hostTotals.cardSchemeTotals=fillHostReconciliationTotals(sTotalsDE124,iNumberOfCardScheme);
                hostTotals.inBalance=false;
                Log.d(TAG, "Parse_RECONSILE_response:sTotalsDE124=  "+ sTotalsDE124);
            }
            else{

            }


            ////////////for test printing here////////////
            Reconsile_Print mReconsile_print;
            mReconsile_print = new Reconsile_Print();

            mReconsile_print.printDetail(hostTotals);
            ////////////////////////////////////////////
            iRetres= 0;
        }

        return iRetres;


    }

    public CardSchemeTotals[] fillHostReconciliationTotals(String stotalsDE124,int iNumberOfCardScheme)
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


        stotalsDE124=stotalsDE124.substring(2,stotalsDE124.length());
        CardSchemeTotals[] oTotalsArray=new CardSchemeTotals[iNumberOfCardScheme];

        for(int i=0; i<iNumberOfCardScheme;i++) {
            oTotalsArray[i]=new CardSchemeTotals();
            oTotalsArray[i].m_szCardSchmID=stotalsDE124.substring(0,2);            /* Card scheme ID*/
            oTotalsArray[i].m_szCardSchemeAcqID=stotalsDE124.substring(2,6);       /* Card Scheme Acquirer ID*/
            // Trx Totals
            oTotalsArray[i].m_lDebitCount=Integer.parseInt(stotalsDE124.substring(6,16));            /* Debit Count*/
            oTotalsArray[i].m_dDebitAmount=Double.parseDouble(stotalsDE124.substring(16,31))/100;             /* Debit Amount*/
            oTotalsArray[i].m_lCreditCount=Integer.parseInt(stotalsDE124.substring(31,41));         /* Credit Count*/
            oTotalsArray[i].m_dCreditAmount=Double.parseDouble(stotalsDE124.substring(41,56))/100;          /* Credit Amount*/
            oTotalsArray[i].m_dCashBackAmount=Double.parseDouble(stotalsDE124.substring(56,71))/100;        /* Cash Back Amount*/
            oTotalsArray[i].m_dCashAdvanceAmount=Double.parseDouble(stotalsDE124.substring(71,86))/100;     /* Cash Advance Amount*/
            oTotalsArray[i].m_lAuthorisationCount=Integer.parseInt(stotalsDE124.substring(86,96));    /* Authorisation Count*/

            if(stotalsDE124.length()>96)
                stotalsDE124=stotalsDE124.substring(96,stotalsDE124.length());
        }

        return oTotalsArray;
    }
    private int Parse_reversal_Response(byte[] receivedPacket) {
        Log.d(TAG, "Parse_reversal_Response: Started");
        int iRetres=-1;

        Process_Rece_Packet( receivedPacket);
        Log.i(TAG, "Process_Rece_Packet: ");
        if(!ValidateHostMAC())
        {
            //todo go to invalid macing process
        }

        if(!CheckHostActionCode(BCDASCII.bytesToHexString(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(39))))
        {
            //todo declined transaction check response code and print message or not for reversal declined
            return 0;
        }

        return iRetres;


    }
}



