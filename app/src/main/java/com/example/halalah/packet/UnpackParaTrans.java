package com.example.halalah.packet;

import android.content.Context;
import android.util.Log;

import com.example.halalah.Utils;
import com.example.halalah.iso8583.ISO8583;
import com.example.halalah.storage.MerchantInfo;

import java.io.UnsupportedEncodingException;

public class UnpackParaTrans {
    private static final String TAG = Utils.TAGPUBLIC + UnpackParaTrans.class.getSimpleName();

    private String resMsg = null;
    private String resDetail = null;

    public UnpackParaTrans(Context context, byte[] srcData, int srcDataLen) {
        Log.d(TAG, "UnPackParamTrans ... ");

        UnpackUtils unpack = new UnpackUtils();
        ISO8583 mIso = unpack.UnpackFront(srcData, srcDataLen);
        if(mIso == null)
            return;

        //todo: mostafa unpack DE72 for TMS
    }

    public String getResponse() {
        return resMsg;
    }

    public String getResponseDetail() {
        return resDetail;
    }
}
