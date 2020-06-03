package com.example.halalah.card;

import com.example.halalah.Utils;
import com.topwise.cloudpos.aidl.emv.EmvTransData;

public class EmvTransDataSub{
    private static final String TAG = Utils.TAGPUBLIC + EmvTransDataSub.class.getSimpleName();

    private byte mTranstype;// Consumption 0x00 Query 0x31 Pre-authorization 0x03 Return 0x20 Consumption Revocation 0x20 ...
    private byte mRequestAmtPosition;// Request input amount position 0x01: before displaying card number 0x02: after displaying card number
    private byte mEmvFlow;// 0x01 – PBOC process 0x02 – qPBOC process
    private byte mSlotType;// 0x00-contact 0x01-non-contact
    private byte[] mReserv;// Reserved for extended use. When the transaction type is 0xF4-card loading log query, Resv [0] value 0x00-read one by one 0x01-read once

    private EmvTransData mEmvTransData;

    public EmvTransData getEmvTransData(boolean isIc) {
        mTranstype = 0x03;
        mRequestAmtPosition = 0x01;
        mReserv = new byte[3];

        if (isIc) {
            mEmvFlow = 0x01;
            mSlotType = 0x00;
        } else {
            mEmvFlow = 0x02;
            mSlotType = 0x01;
        }

        mEmvTransData = new EmvTransData(mTranstype, mRequestAmtPosition, false, true, true, mEmvFlow, mSlotType, mReserv);
        return mEmvTransData;
    }
}