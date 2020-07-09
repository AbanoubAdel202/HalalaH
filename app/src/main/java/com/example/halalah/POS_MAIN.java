package com.example.halalah;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.appcompat.widget.AlertDialogLayout;

import com.example.halalah.TMS.AID_Data;
import com.example.halalah.TMS.Public_Key;
import com.example.halalah.TMS.SAMA_TMS;

import com.example.halalah.card.CardManager;
import com.example.halalah.card.CheckCardListenerSub;
import com.example.halalah.ui.AmountInputActivity;
import com.example.halalah.ui.P_NAQD_InputActivity;
import com.example.halalah.ui.PacketProcessActivity;
import com.example.halalah.ui.PinpadActivity;
import com.example.halalah.ui.Refund_InputActivity;
import com.example.halalah.util.PacketProcessUtils;

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
public class POS_MAIN {
    private static final String TAG = POS_MAIN.class.getSimpleName();
    public static boolean isforced;



    public static Context mcontext;
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
                 AmountACT = new Intent(mcontext, P_NAQD_InputActivity.class);
                 AmountACT.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(AmountACT);

                break;
            case REFUND://REFUND

                // TODO check supervisour password
                // TODO get original transaction Type
                AmountACT = new Intent(mcontext, Refund_InputActivity.class);
                AmountACT.putExtra("transaction Type",Trxtype);
                mcontext.startActivity(AmountACT);

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
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type",Trxtype);
                break;
            case AUTHORISATION_EXTENSION://AUTHORISATION_EXTENSION
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type",Trxtype);
                break;
            case PURCHASE_ADVICE://PURCHASE_ADVICE:
                //todo PURCHASE_ADVICE
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type",Trxtype);

                break;

            case CASH_ADVANCE://CASH_ADVANCE:
                //todo start cash advance
                AmountACT = new Intent(mcontext, AmountInputActivity.class);
                AmountACT.putExtra("transaction Type",Trxtype);
                break;
            case REVERSAL://REVERSAL
                //Do_Reversal();


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
            {
                istate= SAMA_TMS.Get_card_scheme_BY_PAN(PosApplication.getApp().oGPosTransaction.m_sPAN);
                            if (Check_MADA_Card())
                              PosApplication.getApp().oGPosTransaction.is_mada=true;
                            else
                                PosApplication.getApp().oGPosTransaction.is_mada=false;



            }
            else
                istate=SAMA_TMS.Get_card_scheme_BY_AID(PosApplication.getApp().oGPosTransaction.m_sAID);

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
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(7))) {
                    return true;
                }else if(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCard_Scheme_ID=="P1")
                {

                    return true;
                }
                break;
            case AUTHORISATION_VOID://AUTHORISATION_VOID:                                offset 8
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(8)))
                return true;
                else if(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCard_Scheme_ID=="P1")
                {

                    return true;
                }
                break;
            case SADAD_BILL://SADAD_BILL:                                                offset 9
                if ("1".equals(PosApplication.getApp().oGPosTransaction.card_scheme.m_sTransactions_Allowed.charAt(9)))
                return true;
                else if(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCard_Scheme_ID=="P1")
                {

                    return true;
                }
                break;


        }
        return false;  // transaction not allowed
    }
    public static int Check_transaction_limits()
    {
            // todo get transaction limits
        //todo  issues limit cases
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
        //todo get max cash back

        return 0;
    }

    public static boolean Check_MADA_Card()
    {
        if(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCard_Scheme_ID=="P1")
        return true;

        return false;
    }
    public static void supervisor_pass_required(){

      if(PosApplication.getApp().oGPosTransaction.card_scheme.m_sSupervisor_Functions=="1") {//todo dialog which ask for password}

          mcontext = PosApplication.getApp().getApplicationContext();
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
    public static int Check_CVM(POSTransaction.TranscationType Trxtype) {

        switch (Trxtype) {
            case PURCHASE://purchase  offset 0

                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCardholder_Authentication.charAt(0)));

            case PURCHASE_WITH_NAQD://PURCHASE_WITH_NAQD                                 offset 1
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCardholder_Authentication.charAt(1)));

            case PURCHASE_ADVICE://PURCHASE_ADVICE:                                      offset 2
            case AUTHORISATION_ADVICE://AUTHORISATION_ADVICE:
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCardholder_Authentication.charAt(2)));

            case REFUND://REFUND                                                         offset 3
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCardholder_Authentication.charAt(3)));

            case AUTHORISATION://AUTHORISATION:                                          offset 4
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCardholder_Authentication.charAt(4)));
            case CASH_ADVANCE://CASH_ADVANCE:                                            offset 5
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCardholder_Authentication.charAt(5)));
            case REVERSAL://REVERSAL:                                                    offset 6
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCardholder_Authentication.charAt(6)));
            case AUTHORISATION_EXTENSION://AUTHORISATION_EXTENSION                       offset 7
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCardholder_Authentication.charAt(7)));
            case AUTHORISATION_VOID://AUTHORISATION_VOID:                                offset 8
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCardholder_Authentication.charAt(8)));
            case SADAD_BILL://SADAD_BILL:                                                offset 9
                return Integer.parseInt(String.valueOf(PosApplication.getApp().oGPosTransaction.card_scheme.m_sCardholder_Authentication.charAt(9)));

        }
        return -1;
    }

    public  int Do_Reversal(int iErrorreason){

        SAF_Info.SAVE_IN_REV(PosApplication.getApp().oGPosTransaction);
       /* Intent intent = new Intent(mcontext, PacketProcessActivity.class);
        intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_REVERSAL);
        mcontext.startActivity(intent);*/

        return 0;
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
            strFormatedAIDData.append("9f06" +String.format(Locale.ENGLISH,"%02d",AIDObj.AID.length()/2)+AIDObj.AID.toString());

        // Adding ApplicationSelectionIndicator(0:PartMatch,1:ExactMatch)
        strFormatedAIDData.append("df010100");

        // Adding ApplicationVersionNumber
        if (AIDObj.Terminal_AID_version_numbers.length() > 0)
            strFormatedAIDData.append("9f09" +"02"+AIDObj.Terminal_AID_version_numbers.substring(0,4));

        // Adding Default_action_code
        if (AIDObj.Denial_action_code.length() > 0)
            strFormatedAIDData.append("df11" +String.format(Locale.ENGLISH,"%02d",AIDObj.Default_action_code.length()/2)+AIDObj.Default_action_code.toString());


        // Adding Online_action_code
        if (AIDObj.Denial_action_code.length() > 0)
            strFormatedAIDData.append("df12" +String.format(Locale.ENGLISH,"%02d",AIDObj.Online_action_code.length()/2)+AIDObj.Online_action_code.toString());


        // Adding Denial_action_code
        if (AIDObj.Denial_action_code.length() > 0)
            strFormatedAIDData.append("df13" +String.format(Locale.ENGLISH,"%02d",AIDObj.Denial_action_code.length()/2)+AIDObj.Denial_action_code.toString());

            strFormatedAIDData.append("9f1b"+String.format(Locale.ENGLISH,"%02d","00000000".length()/2)+"00000000");

        // Adding ThresholdValueforBiasedRandomSelection
        if (AIDObj.Threshold_Value_for_Biased_Random_Selection.length() > 0)
            strFormatedAIDData.append("df15" +String.format(Locale.ENGLISH,"%02d",AIDObj.Threshold_Value_for_Biased_Random_Selection.length()/2)+AIDObj.Threshold_Value_for_Biased_Random_Selection.toString());

        // Adding Maximum_Target_Percentage_for_Biased_Random_Selection
        if (AIDObj.Maximum_Target_Percentage_for_Biased_Random_Selection.length() > 0)
            strFormatedAIDData.append("df16" +String.format(Locale.ENGLISH,"%02d",AIDObj.Maximum_Target_Percentage_for_Biased_Random_Selection.length()/2)+AIDObj.Maximum_Target_Percentage_for_Biased_Random_Selection.toString());

        // Adding Target_Percentage
        if (AIDObj.Target_Percentage.length() > 0)
            strFormatedAIDData.append("df17" +String.format(Locale.ENGLISH,"%02d",AIDObj.Target_Percentage.length()/2)+AIDObj.Target_Percentage.toString());

        // Adding Default_TDOL
        if (AIDObj.Default_DDOL.length() > 0)
            strFormatedAIDData.append("df14" +String.format(Locale.ENGLISH,"%02d",AIDObj.Default_DDOL.length()/2)+AIDObj.Default_DDOL.toString());

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


    public static int ValidateHostResponse()
    {
        //todo get recieved buffer and do validation using specific DE on business logic
        return 0;
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
