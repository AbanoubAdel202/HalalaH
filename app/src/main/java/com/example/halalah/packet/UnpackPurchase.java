package com.example.halalah.packet;

import android.util.Log;

import com.example.halalah.PosApplication;
import com.example.halalah.Utils;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.iso8583.ISO8583;

public class    UnpackPurchase {
    private static final String TAG = Utils.TAGPUBLIC + UnpackPurchase.class.getSimpleName();

    private byte[] mField47 = null;
    private String resMsg = null;
    private String resDetail = null;

    public UnpackPurchase(byte[] srcData, int srcDataLen) {
        Log.d(TAG, "UnpackPurchase ... ");

        UnpackUtils unpack = new UnpackUtils();
        ISO8583 mIso = unpack.UnpackFront(srcData, srcDataLen); //todo remove TPDU
        if (mIso == null)
            return;
        else
            {
                PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg = mIso;
                resMsg = new String(mIso.getDataElement(39));
        }
        /*resMsg = new String(mIso.getDataElement(39));
        if(PosApplication.testapp)
        resMsg = new String(mIso.getDataElement_original(39));
        Log.d(TAG,"field 39 is：" + resMsg);
        resDetail = unpack.processField46(mIso.getDataElement_original(46), resMsg);

        if(resMsg.equals("00")) {  ////todo add more approved response codes other than 00


            Log.d(TAG, "#########37####################");
            byte[] field37 = mIso.getDataElement(37);
            Log.d(TAG, "37:" + field37.length);
            Log.d(TAG, "37  ASCII: " + new String(field37));
            PosApplication.getApp().oGPosTransaction.m_sRRNumber = (new String(field37));

            Log.d(TAG, "###########38###################");
            byte[] field38 = mIso.getDataElement(38);
            if (field38 != null) {
                Log.d(TAG, "38:" + field38.length);
                Log.d(TAG, "38 ASCII: " + new String(field38));
                if (field38.length > 0) {
                    PosApplication.getApp().oGPosTransaction.m_sApprovalCode = (new String(field38));
                }
            }


            Log.d(TAG, "###########47####################");
            mField47 = mIso.getDataElement(47);
            Log.i(TAG, "field47 m_sCardSchemeSponsorID: " + BCDASCII.bytesToHexString(mField47));

//

            Log.d(TAG, "###########60####################");
            byte[] field60 = mIso.getDataElement(60);
            Log.d(TAG, "60 ASCII: " + new String(field60));
            ParseDE60();
            Log.d(TAG, "###########64####################");
            byte[] field64 = mIso.getDataElement(64);
            String Balance = new String(field64);
            Log.d(TAG, "64" + Balance);
            //todo MAC validation

            }*/
            ///////////////////test//////////////////////////////

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
