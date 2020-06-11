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
        if (procType == PacketProcessUtils.PACKET_PROCESS_PARAM_TRANS) {
            PackParaTrans packParaTrans = new PackParaTrans(mTermId, mMerId);
            return packParaTrans.get();
        }
         else if (procType == PacketProcessUtils.PACKET_PROCESS_PURCHASE) {

            PosApplication.getApp().oGPosTransaction.GetDE03();
            PosApplication.getApp().oGPosTransaction.m_sTrxDateTime= ExtraUtil.GetDate_Time();
            PosApplication.getApp().oGPosTransaction.m_sSTAN=String.valueOf(PosApplication.getApp().oGTerminal_Operation_Data.m_iSTAN++);
            PosApplication.getApp().oGPosTransaction.m_sLocalTrxDateTime=ExtraUtil.GetDate_Time();
            PosApplication.getApp().oGPosTransaction.GetDE22();
            PosApplication.getApp().oGPosTransaction.GetDE24FunctionCode();
            PosApplication.getApp().oGPosTransaction.m_sMsgReasonCode="12";
            PosApplication.getApp().oGPosTransaction.m_sCardAcceptorBusinessCode="11";
            PosApplication.getApp().oGPosTransaction.m_sAquirerInsIDCode="12";
            PosApplication.getApp().oGPosTransaction.m_sRRNumber="1234567890";
            PosApplication.getApp().oGPosTransaction.m_sTerminalID="12345678";
            PosApplication.getApp().oGPosTransaction.m_sMerchantID="010002913";
            PosApplication.getApp().oGPosTransaction.m_sCardSchemeSponsorID="50";
            PosApplication.getApp().oGPosTransaction.m_sCurrencyCode="0628";
            PosApplication.getApp().oGPosTransaction.m_sTrxSecurityControl="09000090";
            PosApplication.getApp().oGPosTransaction.m_sAdditionalAmount="123456789";
            PosApplication.getApp().oGPosTransaction.m_sTransportData="tdata";
            PosApplication.getApp().oGPosTransaction.m_sTrxMACBlock="macblock";
           // mTradNum = mMerchantInfo.getTradNum(true);
           // PackPurchase packpurchase = new PackPurchase(PosApplication.getApp().oGPosTransaction.m_RequestISOMsg);
            PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(POSTransaction.TranscationType.PURCHASE);
            return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

        }
         //todo mostafa Add all other transactions (refund , purchase with cache back , preauthorizaiton , .....)
        return null;
    }
}