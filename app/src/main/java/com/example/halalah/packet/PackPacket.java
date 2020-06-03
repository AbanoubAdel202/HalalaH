package com.example.halalah.packet;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;
import com.example.halalah.Utils;
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
             PosApplication.getApp().oGPosTransaction.m_sProcessCode="000000";
            PosApplication.getApp().oGPosTransaction.m_sTrxDateTime= ExtraUtil.GetDate_Time();
           // mTradNum = mMerchantInfo.getTradNum(true);
           // PackPurchase packpurchase = new PackPurchase(PosApplication.getApp().oGPosTransaction.m_RequestISOMsg);
            PosApplication.getApp().oGPosTransaction.ComposeFinancialMessage(POSTransaction.TranscationType.PURCHASE);
            return PosApplication.getApp().oGPosTransaction.m_RequestISOMsg.isotostr();

        }
         //todo mostafa Add all other transactions (refund , purchase with cache back , preauthorizaiton , .....)
        return null;
    }
}