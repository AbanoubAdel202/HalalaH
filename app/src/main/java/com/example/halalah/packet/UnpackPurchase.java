package com.example.halalah.packet;

import android.util.Log;

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
        ISO8583 mIso = unpack.UnpackFront(srcData, srcDataLen);
        if(mIso == null)
            return;

        //resMsg = new String(mIso.getDataElement(39));
        resMsg = new String(mIso.getDataElement_original(39));
        Log.d(TAG,"field 39 is：" + resMsg);
        resDetail = unpack.processField46(mIso.getDataElement_original(46), resMsg);

        if(resMsg.equals("00")) {
            Log.d(TAG, "###########4###################");
            byte[] field4 = mIso.getDataElement_original(4);
            Log.d(TAG, "4" + field4.length);
            Log.d(TAG, "4 ASCII: " + new String(field4));
            Log.d(TAG, "4 BCD: " + BCDASCII.bytesToHexString(BCDASCII.fromASCIIToBCD(field4, 0, field4.length, false)));

            Log.d(TAG, "###########11###################");
            byte[] field11 = mIso.getDataElement_original(11);
            Log.d(TAG, "11:" + field11.length);
            Log.d(TAG, "11  ASCII: " + new String(field11));
            Log.d(TAG, "11  BCD: " + BCDASCII.bytesToHexString(BCDASCII.fromASCIIToBCD(field11, 0, field11.length, false)));
            if(field11 != null && field11.length != 0) {
            }
            Log.d(TAG, "###########12###################");
            byte[] field12 = mIso.getDataElement_original(12);
            Log.d(TAG, "12:" + field12.length);
            Log.d(TAG, "12 ASCII: " + new String(field12));

            Log.d(TAG, "###########13##################");
            byte[] field13 = mIso.getDataElement_original(13);
            Log.d(TAG, "13:" + field13.length);
            Log.d(TAG, "13 ASCII: " + new String(field13));

            Log.d(TAG, "###########14####################");
            byte[] field14 = mIso.getDataElement_original(14);

            Log.d(TAG, "#########37####################");
            byte[] field37 = mIso.getDataElement_original(37);
            Log.d(TAG, "37:" + field37.length);
            Log.d(TAG, "37  ASCII: " + new String(field37));
//            app.mCurrTran.setReferenceCode(new String(field37));

            Log.d(TAG, "###########38###################");
            byte[] field38 = mIso.getDataElement_original(38);
            if (field38 != null) {
                Log.d(TAG, "38:" + field38.length);
                Log.d(TAG, "38 ASCII: " + new String(field38));
                if (field38.length > 0) {
//                    app.mCurrTran.setAuthorizationCode(new String(field38));
                }
            }

            Log.d(TAG, "###########41####################");
            byte[] field41 = mIso.getDataElement_original(41);
            Log.d(TAG, "41:" + field41.length);
            Log.d(TAG, "41  ASCII: " + new String(field41));

            Log.d(TAG, "###########42####################");
            byte[] field42 = mIso.getDataElement_original(42);
            Log.d(TAG, "42:" + field42.length);
            Log.d(TAG, "42  ASCII: " + new String(field42));

            Log.d(TAG, "###########49###################");
            byte[] field49 = mIso.getDataElement_original(49);
            Log.d(TAG, "49:" + field49.length);
            Log.d(TAG, "49  ASCII: " + new String(field49));

            Log.d(TAG, "###########46####################");
            byte[] field46 = mIso.getDataElement_original(46);
            Log.d(TAG, "46:" + field46.length);
            Log.d(TAG, "46 HEX: " + BCDASCII.bytesToHexString(field46));
            String bashnum = null;
            String serialnum = null;
            String consumedate = null;
            byte[] bash = new byte[6];
            byte[] serial = new byte[6];
            byte[] date = new byte[4];
            System.arraycopy(field46, 15, bash, 0, 6);
            System.arraycopy(field46, 22, serial, 0, 6);
            System.arraycopy(field46, 29, date, 0, 4);
            bashnum = new String(bash);
            serialnum = new String(serial);
            consumedate = new String(date);

//            app.mCurrTran.setSerialnum(serialnum);
//            app.mCurrTran.setBashnum(bashnum);

            Log.d(TAG, "###########47####################");
            mField47 = mIso.getDataElement_original(47);
            Log.i(TAG,"field47: "+BCDASCII.bytesToHexString(mField47));

//            byte[] printByte = new byte[field47.length - 5];
//            int printLen = printByte.length;
//            System.arraycopy(field47, 5, printByte, 0, printLen);
//            Log.i(TAG,"printByte: "+BCDASCII.bytesToHexString(printByte));
//            for (int i = 0; i < printLen; i++) {
//                if (printByte[i] == 0x10 || printByte[i] == 0x11
//                        || printByte[i] == 0x12 || printByte[i] == 0x13) {
//                    printByte[i] = 0x0A;
//                }
//                if (printByte[i] == 0x02) {
//                    printByte[i] = 0x00;
//                }
//                if (printByte[i] == 0xA1) {
//
//                }
//            }
//            Log.d(TAG, "printByte:" + printByte.length);
//            try {
//                mPrintMsg = new String(printByte, "GBK");
//                Log.d(TAG, "47 ASCII: " + mPrintMsg);
//            } catch (UnsupportedEncodingException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

            Log.d(TAG, "###########60####################");
            byte[] field60 = mIso.getDataElement_original(60);
            Log.d(TAG, "60 ASCII: " + new String(field60));
            int offset60 = 0;

            byte[] field60_type = new byte[2];
            System.arraycopy(field60, offset60, field60_type, 0, 2);
            Log.d(TAG, "60ASCII: " + new String(field60_type));
            offset60 += 2;

            byte[] bashid = new byte[6];
            System.arraycopy(field60, offset60, bashid, 0, 6);
            Log.d(TAG, "60ASCII: " + new String(bashid));
            offset60 += 6;

            byte[] netcode = new byte[3];
            System.arraycopy(field60, offset60, netcode, 0, 3);
            Log.d(TAG, "60ASCII: " + new String(netcode));
            offset60 += 3;

            byte[] bility = new byte[1];
            System.arraycopy(field60, offset60, bility, 0, 1);
            Log.d(TAG, "60ASCII: " + new String(bility));
            offset60 += 1;

            byte[] condition = new byte[1];
            System.arraycopy(field60, offset60, condition, 0, 1);
            Log.d(TAG, "60ASCII: " + new String(condition));
            offset60 += 1;

            byte[] productcode = new byte[4];
            System.arraycopy(field60, offset60, productcode, 0, 4);
            Log.d(TAG, "60ASCII: " + new String(productcode));
            offset60 += 4;

            byte[] buscode = new byte[2];
            System.arraycopy(field60, offset60, buscode, 0, 2);
            Log.d(TAG, "60 ASCII: " + new String(buscode));
            offset60 += 2;

            Log.d(TAG, "###########64####################");
            byte[] field64 = mIso.getDataElement_original(64);
            String Balance = new String(field64);
            Log.d(TAG, "64" + Balance);
        }
    }

    public String getResponse() {
        return resMsg;
    }

    public String getResponseDetail() {
        return resDetail;
    }

    public byte[] getField47() {
        return mField47;
    }
}
