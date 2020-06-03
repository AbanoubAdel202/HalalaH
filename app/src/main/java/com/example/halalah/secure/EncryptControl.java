package com.example.halalah.secure;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.Utils;
import com.example.halalah.iso8583.BCDASCII;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;

/**
 * Created by topwise on 17-6-20.
 */

public class EncryptControl {
    private static final String TAG = Utils.TAGPUBLIC +EncryptControl.class.getSimpleName();
    //主密钥，工作密钥索引的范围都是0-9
    final int MAIN_KEY_INDEX = 0;
    final int USER_KEY_INDEX = 0;

    private AidlPinpad mPOSDeviceManager;

    public int UpdateMasterKey(String key) throws RemoteException{
        Log.i(TAG,"UpdateMasterKey()");
        mPOSDeviceManager = DeviceTopUsdkServiceManager.getInstance().getPinpadManager(0);

        if (mPOSDeviceManager == null) {
            return -1;
        }
        Log.i(TAG, "key: " + key);
        boolean result = mPOSDeviceManager.loadMainkey(0,BCDASCII.hexStringToBytes(key),null);
        Log.i(TAG, "result: " + result);

        if (!result) {
            Log.i(TAG,"result -2");
            return -2;
        }

        return 0;
    }

    public int UpdateWorkKey(int Key_Mode,String key,String CheckValue) throws RemoteException {
        Log.i(TAG, "UpdateWorkKey, Key_Mode = "+Key_Mode+", key = "+key+", CheckValue = "+CheckValue);
        mPOSDeviceManager = DeviceTopUsdkServiceManager.getInstance().getPinpadManager(0);
        if (mPOSDeviceManager == null) {
            Log.i(TAG, "return -1");
            return -1;
        }

        boolean result = mPOSDeviceManager.loadWorkKey(Key_Mode,0,0,BCDASCII.hexStringToBytes(key),BCDASCII.hexStringToBytes(CheckValue));
        if (!result) {
            Log.i(TAG, "return -2");
            return -2;
        }

        return 0;
    }

    public int CalculateEncryptByTDK(byte[] inputdata, byte[] outputdata) {
        Log.i(TAG,"CalculateEncryptByTDK()");
        int result = -1;
        int size = 16;// 长度
        mPOSDeviceManager = DeviceTopUsdkServiceManager.getInstance().getPinpadManager(0);
        if (mPOSDeviceManager == null) {
            Log.i(TAG, "return -1");
            return -1;
        }

        Log.i(TAG, "inputdata : "+BCDASCII.bytesToHexString(inputdata));
        byte mode = 0;
        try {
            result = mPOSDeviceManager.encryptByTdk(0, mode, null, inputdata, outputdata);
            if(result != 0){
                Log.i(TAG,"DZSwipeDes result  -2");
                return -2;
            }

            Log.i(TAG, "outputdata : "+BCDASCII.bytesToHexString(outputdata));
            System.arraycopy(outputdata, 0, outputdata, 0, size);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //计算MACkey
    public int CalculateMac(byte[] inputdata, int len_inputdata, byte[] outkey) {
        Log.i(TAG,"CalculateMac()");
        int result = -1;
        mPOSDeviceManager = DeviceTopUsdkServiceManager.getInstance().getPinpadManager(0);
        if (mPOSDeviceManager == null) {
            Log.i(TAG, "return -1");
            return -1;
        }

        Log.i(TAG, "inputdata : "+BCDASCII.bytesToHexString(inputdata));
        Bundle mbundle = new Bundle();
        mbundle.putString("TAG","Mac计算");
        mbundle.putInt("wkeyid",0);
        mbundle.putByteArray("data",inputdata);
        mbundle.putInt("type", 0x01);

        try {
            result = mPOSDeviceManager.getMac(mbundle,outkey);
            if(result != 0){
                Log.i(TAG,"CalculateMac result  -2");
                return -2;
            }

            Log.i(TAG, "outkey : "+BCDASCII.bytesToHexString(outkey));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
