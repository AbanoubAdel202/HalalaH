package com.example.halalah.packet;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.halalah.POSTransaction;
import com.example.halalah.POS_MAIN;
import com.example.halalah.PosApplication;
import com.example.halalah.SAF_Info;
import com.example.halalah.Utils;
import com.example.halalah.iso8583.ISO8583;
import com.example.halalah.secure.DUKPT_KEY;
import com.example.halalah.storage.MerchantInfo;
import com.example.halalah.util.ExtraUtil;
import com.example.halalah.util.PacketProcessUtils;

public class PackPacket {
    private static final String TAG = Utils.TAGPUBLIC + PackPacket.class.getSimpleName();

    private Context mContext;
    private String mTPDU;






    public PackPacket(Context context, String tpdu) {
        mContext = context;
        mTPDU = tpdu;


    }

    public byte[] getSendPacket() {
        Log.i(TAG, "getSendPacket()");
        // test dummy data
        //PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCard_Scheme_ID="P1";
        //PosApplication.getApp().oGPosTransaction.m_sTransportData="tdata";
        //PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCard_Scheme_Name_English="mada";
        //PosApplication.getApp().oGPosTransaction.m_sAdditionalAmount = "000005500";
        //////////////////////////////////////////////
        if(PosApplication.getApp().oGPosTransaction.m_enmTrxType!= POSTransaction.TranscationType.TMS_FILE_DOWNLOAD)
        PosApplication.getApp().oGPosTransaction.m_sTrxAmount= PosApplication.getApp().oGPosTransaction.m_sTrxAmount.replace(".","");
        if(PosApplication.getApp().oGPosTransaction.m_is_mada)
        {
            switch (PosApplication.getApp().oGPosTransaction.m_enmTrxType) {


                case PURCHASE:
                    PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(POSTransaction.TranscationType.PURCHASE);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
                case PURCHASE_ADVICE:
                    PosApplication.getApp().oGPosTransaction.ComposeFinancialAdviseMessage(POSTransaction.TranscationType.PURCHASE_ADVICE);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case PURCHASE_WITH_NAQD:

                    PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(POSTransaction.TranscationType.PURCHASE_WITH_NAQD);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
                case AUTHORISATION:

                    PosApplication.getApp().oGPosTransaction.ComposeAuthoriszationMessage(POSTransaction.TranscationType.AUTHORISATION);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case AUTHORISATION_ADVICE:

                    PosApplication.getApp().oGPosTransaction.ComposeAuthorisationAdviseMessage(POSTransaction.TranscationType.AUTHORISATION_ADVICE);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case AUTHORISATION_EXTENSION:

                    PosApplication.getApp().oGPosTransaction.ComposeAuthorisationAdviseMessage(POSTransaction.TranscationType.AUTHORISATION_EXTENSION);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case AUTHORISATION_VOID:

                    PosApplication.getApp().oGPosTransaction.ComposeAuthorisationAdviseMessage(POSTransaction.TranscationType.AUTHORISATION_VOID);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case REFUND:

                    PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(POSTransaction.TranscationType.REFUND);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case SADAD_BILL:

                    PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(POSTransaction.TranscationType.SADAD_BILL);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case CASH_ADVANCE:

                    PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(POSTransaction.TranscationType.CASH_ADVANCE);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

               /* case RECONCILIATION:

                    PosApplication.getApp().oGPosTransaction.CompoaseReconciliationMessage();
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();*/

               /* case TERMINAL_REGISTRATION:

                    PosApplication.getApp().oGPosTransaction.ComposeNetworkMessage();
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();*/

                case REVERSAL:

                    PosApplication.getApp().oGPosTransaction.ComposeReversalMessage();
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

               case TMS_FILE_DOWNLOAD:
                    PosApplication.getApp().oGPosTransaction.ComposeFileDownloadMessage();
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case ADMIN:

                    PosApplication.getApp().oGPosTransaction.ComposeAdministrativeMessage();
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

            }

        }
        else //ICS
        {
            switch (PosApplication.getApp().oGPosTransaction.m_enmTrxType) {

                /*****\
                 * we can use BuildISO8583Message(TranscationType TrxType) for automatic compose function selection
                 */
                case PURCHASE:
                    if(PosApplication.getApp().oGPosTransaction.m_enmTrxCardType== POSTransaction.CardType.ICC || PosApplication.getApp().oGPosTransaction.m_enmTrxCardType== POSTransaction.CardType.CTLS)
                    PosApplication.getApp().oGPosTransaction.ComposeAuthoriszationMessage(POSTransaction.TranscationType.PURCHASE);
                    else if(PosApplication.getApp().oGPosTransaction.m_enmTrxCardType== POSTransaction.CardType.MAG ||PosApplication.getApp().oGPosTransaction.m_enmTrxCardType== POSTransaction.CardType.MANUAL)
                    PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(POSTransaction.TranscationType.PURCHASE);

                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
                case AUTHORISATION:

                    PosApplication.getApp().oGPosTransaction.ComposeAuthoriszationMessage(POSTransaction.TranscationType.AUTHORISATION);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
                case PURCHASE_ADVICE:
                    PosApplication.getApp().oGPosTransaction.ComposeFinancialAdviseMessage(POSTransaction.TranscationType.PURCHASE_ADVICE);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case PURCHASE_WITH_NAQD:

                    PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(POSTransaction.TranscationType.PURCHASE_WITH_NAQD);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case AUTHORISATION_ADVICE:

                    PosApplication.getApp().oGPosTransaction.ComposeAuthorisationAdviseMessage(POSTransaction.TranscationType.AUTHORISATION_ADVICE);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case AUTHORISATION_EXTENSION:

                    PosApplication.getApp().oGPosTransaction.ComposeAuthorisationAdviseMessage(POSTransaction.TranscationType.AUTHORISATION_EXTENSION);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case AUTHORISATION_VOID:

                    PosApplication.getApp().oGPosTransaction.ComposeAuthorisationAdviseMessage(POSTransaction.TranscationType.AUTHORISATION_VOID);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case REFUND:
                        //1220-1230   online not supported for visa and master

                    PosApplication.getApp().oGPosTransaction.ComposeFinancialAdviseMessage(POSTransaction.TranscationType.REFUND);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case SADAD_BILL:

                    PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(POSTransaction.TranscationType.SADAD_BILL);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case CASH_ADVANCE:

                    PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(POSTransaction.TranscationType.CASH_ADVANCE);
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case RECONCILIATION:

                    PosApplication.getApp().oGPosTransaction.CompoaseReconciliationMessage();
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                /*case TERMINAL_REGISTRATION:

                    PosApplication.getApp().oGPosTransaction.ComposeNetworkMessage();
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
*/
                case REVERSAL:

                    PosApplication.getApp().oGPosTransaction.ComposeReversalMessage();
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case TMS_FILE_DOWNLOAD:
                    PosApplication.getApp().oGPosTransaction.ComposeFileDownloadMessage();
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

                case ADMIN:

                    PosApplication.getApp().oGPosTransaction.ComposeAdministrativeMessage();
                    return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

            }

        }


        return null;
    }
}