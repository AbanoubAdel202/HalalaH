package com.example.halalah.packet;

import android.util.Log;

import com.example.halalah.iso8583.ISO8583;

//UnpackParaDownload
//UnpackSignoff
//UnpackStatusUpload
public class UnpackDefault {
    private static final String TAG = UnpackDefault.class.getSimpleName();

    private String resMsg = null;
    private String resDetail = null;

    public UnpackDefault(byte[] srcData, int srcDataLen) {
        Log.d(TAG, "UnpackSignoff ... ");

        UnpackUtils unpack = new UnpackUtils();
        ISO8583 mIso = unpack.UnpackFront(srcData, srcDataLen);
        if(mIso == null)
            return;
        resMsg = new String(mIso.getDataElement(39));
        Log.d(TAG,"field 39 is：" + resMsg);
        resDetail = unpack.processField46(mIso.getDataElement(46), resMsg);
    }

    public String getResponse() {
        return resMsg;
    }

    public String getResponseDetail() {
        return resDetail;
    }
}
