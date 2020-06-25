package com.example.halalah.packet;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;
import com.example.halalah.Utils;
import com.example.halalah.iso8583.ISO8583;
import com.example.halalah.storage.MerchantInfo;
import com.example.halalah.util.ExtraUtil;
import com.example.halalah.util.PacketProcessUtils;

public class PackPacket {
    private static final String TAG = Utils.TAGPUBLIC + PackPacket.class.getSimpleName();

    private Context mContext;
    private String mTermId;
    private String mMerId;
    private String mTradNum;
    private String mTPDU;
    private MerchantInfo mMerchantInfo;


    private byte[] mField62 = null;
    private int mQueryEmvNum = 0;

    public PackPacket(Context context, String tpdu) {
        mContext = context;
        mTPDU = tpdu;

        mMerchantInfo = new MerchantInfo(context);
        mTermId = mMerchantInfo.getTermId();
        mMerId = mMerchantInfo.getMerchantId();
    }

    public byte[] getSendPacket(int procType, Bundle data) {
        Log.i(TAG, "getSendPacket()");
        if (procType == PacketProcessUtils.PACKET_PROCESS_TMS_FILE_DOWNLOAD) {
              PosApplication.getApp().oGPosTransaction.ComposeFileDownloadMessage();
            return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
        }
         else if (PosApplication.getApp().oGPosTransaction.m_enmTrxType==POSTransaction.TranscationType.PURCHASE) {
            PosApplication.getApp().oGPosTransaction.card_scheme.m_sCard_Scheme_ID="P1";
            PosApplication.getApp().oGPosTransaction.GetDE03();
            PosApplication.getApp().oGPosTransaction.m_sTrxDateTime= ExtraUtil.GetDate_Time();
            PosApplication.getApp().oGPosTransaction.m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
            PosApplication.getApp().oGPosTransaction.m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
            PosApplication.getApp().oGPosTransaction.GetDE22_POSEntryMode();
            PosApplication.getApp().oGPosTransaction.GetDE24_FunctionCode();
            PosApplication.getApp().oGPosTransaction.GetDE25_Messagereasoncode();
            PosApplication.getApp().oGPosTransaction.m_sCardAcceptorBusinessCode=PosApplication.getApp().oGTerminal_Operation_Data.sMerchant_Category_Code;
            PosApplication.getApp().oGPosTransaction.m_sAquirerInsIDCode=PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID;
            PosApplication.getApp().oGPosTransaction.GetDE_37_RRN();
            PosApplication.getApp().oGPosTransaction.m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
            PosApplication.getApp().oGPosTransaction.m_sMerchantID=PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID;
            PosApplication.getApp().oGPosTransaction.GetDE47_CardSchemeSponsorID();
            PosApplication.getApp().oGPosTransaction.m_sCurrencyCode="0628";
            PosApplication.getApp().oGPosTransaction.m_sTrxSecurityControl="09000090";
            PosApplication.getApp().oGPosTransaction.m_sAdditionalAmount="0000000";
            PosApplication.getApp().oGPosTransaction.m_sTransportData="tdata";
            PosApplication.getApp().oGPosTransaction.card_scheme.m_sCard_Scheme_Name_English="mada";
           // mTradNum = mMerchantInfo.getTradNum(true);
           // PackPurchase packpurchase = new PackPurchase(PosApplication.getApp().oGPosTransaction.m_RequestISOMsg);
            PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(POSTransaction.TranscationType.PURCHASE);
            return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

        }
         else if(PosApplication.getApp().oGPosTransaction.m_enmTrxType==POSTransaction.TranscationType.AUTHORISATION)
        {
            PosApplication.getApp().oGPosTransaction.GetDE03();
            PosApplication.getApp().oGPosTransaction.m_sTrxDateTime= ExtraUtil.GetDate_Time();
            PosApplication.getApp().oGPosTransaction.m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
            PosApplication.getApp().oGPosTransaction.m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
            PosApplication.getApp().oGPosTransaction.GetDE22_POSEntryMode();
            PosApplication.getApp().oGPosTransaction.GetDE24_FunctionCode();
            PosApplication.getApp().oGPosTransaction.GetDE25_Messagereasoncode();
            PosApplication.getApp().oGPosTransaction.m_sCardAcceptorBusinessCode=PosApplication.getApp().oGTerminal_Operation_Data.sMerchant_Category_Code;
            PosApplication.getApp().oGPosTransaction.m_sAquirerInsIDCode=PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID;
            PosApplication.getApp().oGPosTransaction.GetDE_37_RRN();
            PosApplication.getApp().oGPosTransaction.m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
            PosApplication.getApp().oGPosTransaction.m_sMerchantID=PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID;
            PosApplication.getApp().oGPosTransaction.GetDE47_CardSchemeSponsorID();
            PosApplication.getApp().oGPosTransaction.m_sCurrencyCode="0628";
            PosApplication.getApp().oGPosTransaction.m_sTrxSecurityControl="09000090";
            PosApplication.getApp().oGPosTransaction.m_sAdditionalAmount="0000000";
            PosApplication.getApp().oGPosTransaction.m_sTransportData="tdata";
            PosApplication.getApp().oGPosTransaction.card_scheme.m_sCard_Scheme_Name_English="mada";
            // mTradNum = mMerchantInfo.getTradNum(true);
            // PackPurchase packpurchase = new PackPurchase(PosApplication.getApp().oGPosTransaction.m_RequestISOMsg);

            PosApplication.getApp().oGPosTransaction.ComposeAuthoriszationMessage(POSTransaction.TranscationType.AUTHORISATION);
            return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
        }
        else if(PosApplication.getApp().oGPosTransaction.m_enmTrxType==POSTransaction.TranscationType.PURCHASE_ADVICE)
        {
            PosApplication.getApp().oGPosTransaction.GetDE03();
            PosApplication.getApp().oGPosTransaction.m_sTrxDateTime= ExtraUtil.GetDate_Time();
            PosApplication.getApp().oGPosTransaction.m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
            PosApplication.getApp().oGPosTransaction.m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
            PosApplication.getApp().oGPosTransaction.GetDE22_POSEntryMode();
            PosApplication.getApp().oGPosTransaction.GetDE24_FunctionCode();
            PosApplication.getApp().oGPosTransaction.GetDE25_Messagereasoncode();
            PosApplication.getApp().oGPosTransaction.m_sCardAcceptorBusinessCode=PosApplication.getApp().oGTerminal_Operation_Data.sMerchant_Category_Code;
            PosApplication.getApp().oGPosTransaction.m_sAquirerInsIDCode=PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID;
            PosApplication.getApp().oGPosTransaction.GetDE_37_RRN();
            PosApplication.getApp().oGPosTransaction.m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
            PosApplication.getApp().oGPosTransaction.m_sMerchantID=PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID;
            PosApplication.getApp().oGPosTransaction.GetDE47_CardSchemeSponsorID();
            PosApplication.getApp().oGPosTransaction.m_sCurrencyCode="0628";
            PosApplication.getApp().oGPosTransaction.m_sTrxSecurityControl="09000090";
            PosApplication.getApp().oGPosTransaction.m_sAdditionalAmount="0000000";
            PosApplication.getApp().oGPosTransaction.m_sTransportData="tdata";
            PosApplication.getApp().oGPosTransaction.card_scheme.m_sCard_Scheme_Name_English="mada";
            // mTradNum = mMerchantInfo.getTradNum(true);
            // PackPurchase packpurchase = new PackPurchase(PosApplication.getApp().oGPosTransaction.m_RequestISOMsg);

            PosApplication.getApp().oGPosTransaction.ComposePurchaseAdviseMessage(POSTransaction.TranscationType.PURCHASE_ADVICE);
            return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
        }
        else if(PosApplication.getApp().oGPosTransaction.m_enmTrxType==POSTransaction.TranscationType.AUTHORISATION_ADVICE)
        {
            PosApplication.getApp().oGPosTransaction.GetDE03();
            PosApplication.getApp().oGPosTransaction.m_sTrxDateTime= ExtraUtil.GetDate_Time();
            PosApplication.getApp().oGPosTransaction.m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
            PosApplication.getApp().oGPosTransaction.m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
            PosApplication.getApp().oGPosTransaction.GetDE22_POSEntryMode();
            PosApplication.getApp().oGPosTransaction.GetDE24_FunctionCode();
            PosApplication.getApp().oGPosTransaction.GetDE25_Messagereasoncode();
            PosApplication.getApp().oGPosTransaction.m_sCardAcceptorBusinessCode=PosApplication.getApp().oGTerminal_Operation_Data.sMerchant_Category_Code;
            PosApplication.getApp().oGPosTransaction.m_sAquirerInsIDCode=PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID;
            PosApplication.getApp().oGPosTransaction.GetDE_37_RRN();
            PosApplication.getApp().oGPosTransaction.m_sTerminalID=PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID;
            PosApplication.getApp().oGPosTransaction.m_sMerchantID=PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID;
            PosApplication.getApp().oGPosTransaction.GetDE47_CardSchemeSponsorID();
            PosApplication.getApp().oGPosTransaction.m_sCurrencyCode="0628";
            PosApplication.getApp().oGPosTransaction.m_sTrxSecurityControl="09000090";
            PosApplication.getApp().oGPosTransaction.m_sAdditionalAmount="0000000";
            PosApplication.getApp().oGPosTransaction.m_sTransportData="tdata";
            PosApplication.getApp().oGPosTransaction.card_scheme.m_sCard_Scheme_Name_English="mada";
            // mTradNum = mMerchantInfo.getTradNum(true);
            // PackPurchase packpurchase = new PackPurchase(PosApplication.getApp().oGPosTransaction.m_RequestISOMsg);

            PosApplication.getApp().oGPosTransaction.ComposeAuthorisationAdviseMessage(POSTransaction.TranscationType.AUTHORISATION_ADVICE);
            return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();
        }


        return null;
    }
}