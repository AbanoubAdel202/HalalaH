package com.example.halalah.packet;

import android.util.Log;

import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;
import com.example.halalah.Utils;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.iso8583.ISO8583;

public class UnpackResponse {
    private static final String TAG = Utils.TAGPUBLIC + UnpackResponse.class.getSimpleName();


    private String resMsg = null;
    private String resDetail = null;

    public UnpackResponse(byte[] srcData, int srcDataLen) {
        Log.d(TAG, "UnpackPurchase ... ");

        UnpackUtils unpack = new UnpackUtils();
        ISO8583 mIso = unpack.UnpackFront(srcData, srcDataLen); //todo remove TPDU
        if (mIso == null)
            return;
        else
            {
                PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg = mIso;
                resMsg = new String(mIso.getDataElement(39));
                if(PosApplication.getApp().oGPosTransaction.m_enmTrxType== POSTransaction.TranscationType.RECONCILIATION)
                {

                }
                else {
                    PosApplication.getApp().oGPosTransaction.m_sApprovalCode = new String(mIso.getDataElement(38));
                    resDetail = PosApplication.getApp().oGPosTransaction.m_sApprovalCode;
                }
            }

    }

    private void ParseDE60() {
        //todo parse DE60
    }

    public String getResponse() {
        return resMsg;
    }

    public String getResponseDetail() {
        return resDetail;
    }


}
