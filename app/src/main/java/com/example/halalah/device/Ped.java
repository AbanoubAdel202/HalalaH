package com.example.halalah.device;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.Utils;
import com.example.halalah.iso8583.BCDASCII;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;

/**
 * Created by topwise on 17-6-20.
 */

public class Ped {
    private static final String TAG = Utils.TAGPUBLIC +Ped.class.getSimpleName();
    //主密钥，工作密钥索引的范围都是0-9
    final int MAIN_KEY_INDEX = 0;
    final int USER_KEY_INDEX = 0;


    private static class SingleHolder {
        private static Ped instance = new Ped();
    }
    public static Ped getInstance() {
        return Ped.SingleHolder.instance;
    }

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
        boolean result;

        result = mPOSDeviceManager.loadWorkKey(Key_Mode,0,0,BCDASCII.hexStringToBytes(key),BCDASCII.hexStringToBytes(CheckValue));

        if (!result) {
            Log.i(TAG, "return -2");
            return -2;
        }

        return 0;
    }

    //磁道加密
    public int SwipeDes(byte[] outkey,byte[] inputdata) throws RemoteException {
        Log.i(TAG,"SwipeDes()");
        int size = 16;// 长度
        mPOSDeviceManager = DeviceTopUsdkServiceManager.getInstance().getPinpadManager(0);
        if (mPOSDeviceManager == null) {
            Log.i(TAG,"DZSwipeDes result  -1");
            return -1;
        }

        Log.i(TAG, BCDASCII.bytesToHexString(inputdata));
        byte mode = 0;
        byte [] crydat = new byte[mPOSDeviceManager.encryptByTdk(0,mode,mPOSDeviceManager.getRandom(),inputdata,outkey)];
        if(crydat == null){
            Log.i(TAG,"DZSwipeDes result  -2");
            return -2;
        }
        System.arraycopy(crydat, 0, outkey, 0, size);
        return 0;

    }

    public int CalculateEncryptByTDK(byte[] inputdata, byte[] outputdata) {
        int result = -1;
        mPOSDeviceManager = DeviceTopUsdkServiceManager.getInstance().getPinpadManager(0);
        if (mPOSDeviceManager == null) {
            Log.i(TAG, "return -1");
            return -1;
        }

        byte[] aaa = BCDASCII.hexStringToBytes("19621799651000356010700000000000");

        Log.i(TAG, "aaa : "+BCDASCII.bytesToHexString(aaa));
        byte mode = 0;
        try {
            result = mPOSDeviceManager.encryptByTdk(0, mode, null, aaa, outputdata);
            if(result != 0){
                Log.i(TAG,"DZSwipeDes result  -2");
                return -2;
            }

            Log.i(TAG, "outputdata : "+BCDASCII.bytesToHexString(outputdata));
            //System.arraycopy(crydat, 0, outputdata, 0, 16);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //计算MACkey
    public int CalculateMac(byte[] inputdata, byte[] outkey) throws RemoteException {
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
        result = mPOSDeviceManager.getMac(mbundle,outkey);

        Log.i(TAG, "outkey : "+BCDASCII.bytesToHexString(outkey));
        return result;
    }
}
