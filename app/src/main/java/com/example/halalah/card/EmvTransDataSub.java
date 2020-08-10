package com.example.halalah.card;

import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;
import com.example.halalah.Utils;


public class EmvTransDataSub{
    private static final String TAG = Utils.TAGPUBLIC + EmvTransDataSub.class.getSimpleName();

    private byte mTranstype;// Consumption 0x00 Query 0x31 Pre-authorization 0x03 Return 0x20 Consumption Revocation 0x20 ...
    private byte mCardType;//CardType.RF或CardType.IC或RF或CardType.MAG

    private EmvTransData mEmvTransData;

    public EmvTransData getEmvTransData(boolean isIc) {
        int amount = Integer.parseInt(PosApplication.getApp().oGPosTransaction.m_sTrxAmount.replace(".",""));
        if(PosApplication.getApp().oGPosTransaction.m_enmTrxType== POSTransaction.TranscationType.PURCHASE_WITH_NAQD)
            mTranstype = 0x09;
        else
            mTranstype = 0x00;
        if (isIc) {
            mCardType = CardType.IC;
        } else {
            mCardType = CardType.RF;
        }
        mEmvTransData = new EmvTransData(mCardType,mTranstype,amount,true);
        return mEmvTransData;
    }
}