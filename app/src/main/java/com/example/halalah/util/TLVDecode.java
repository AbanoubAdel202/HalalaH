package com.example.halalah.util;

import android.util.Log;

import com.example.halalah.iso8583.BCDASCII;

import java.util.LinkedHashMap;

public class TLVDecode {
    private static final String TAG = TLVDecode.class.getSimpleName();

    private static LinkedHashMap<byte[], byte[]> mList;

    public static LinkedHashMap<byte[], byte[]> getDecodeTLV(byte[] tlv) {
        Log.d(TAG,"getDecodeTLV tlv:  " + BCDASCII.bytesToHexString(tlv));

        mList = new LinkedHashMap<byte[], byte[]>();
        decodeTLV(tlv);

        for (byte[] item : mList.keySet()) {
            Log.i(TAG, "getDecodeTLV item: "+ BCDASCII.bytesToHexString(item)+"  mList.get(item):"+ BCDASCII.bytesToHexString(mList.get(item)));
        }

        return mList;
    }

    private static void decodeTLV (byte[] tlv) {
        if (tlv ==null || tlv.length < 1) {
            return;
        }

        byte[] tag;
        byte[] value;
        byte[] nextTlv;
        int valueLen;
        int lenLen;

        if ((tlv[0] & 0x20) != 0x20) { // 基本数据对象

            if ((tlv[0] & 0x1f) != 0X1F) { // Tag为一个字节

                valueLen = (tlv[1] & 0x81) != 0x81 ? (tlv[1] & 0xFF) : (tlv[2] & 0xFF);
                lenLen = (tlv[1] & 0x81) != 0x81 ? 1 : 2;

                tag = new byte[1];
                value = new byte[valueLen];
                System.arraycopy(tlv, 0, tag, 0, 1);
                System.arraycopy(tlv, 1+lenLen, value, 0, valueLen);

                mList.put(tag, value);
                Log.i(TAG,"mList_1 tag: "+ BCDASCII.bytesToHexString(tag)+"  tag: "+ BCDASCII.bytesToHexString(value));

                if (tlv.length > 1+lenLen+value.length) {
                    nextTlv = new byte[tlv.length-1-lenLen-valueLen];
                    System.arraycopy(tlv, 1+lenLen+valueLen, nextTlv, 0, tlv.length-1-lenLen-valueLen);
                    decodeTLV(nextTlv);
                }
            } else { // Tag为两个字节

                valueLen = (tlv[2] & 0x81) != 0x81 ? (tlv[2] & 0xFF) : (tlv[3] & 0xFF);
                lenLen = (tlv[2] & 0x81) != 0x81 ? 1 : 2;

                tag = new byte[2];
                value = new byte[valueLen];
                System.arraycopy(tlv, 0, tag, 0, 2);
                System.arraycopy(tlv, 2+lenLen, value, 0, valueLen);

                mList.put(tag, value);
                Log.i(TAG,"mList_2 tag: "+ BCDASCII.bytesToHexString(tag)+"  tag: "+ BCDASCII.bytesToHexString(value));

                if (tlv.length > 2+lenLen+value.length) {
                    nextTlv = new byte[tlv.length-2-lenLen-valueLen];
                    System.arraycopy(tlv, 2+lenLen+valueLen, nextTlv, 0, tlv.length-2-lenLen-valueLen);
                    decodeTLV(nextTlv);
                }
            }
        } else { // 结构数据对象
            if ((tlv[0] & 0x1f) != 0X1F) { // Tag为一个字节

                valueLen = (tlv[2] & 0x81) != 0x81 ? (tlv[1] & 0xFF) : (tlv[2] & 0xFF);
                lenLen = (tlv[2] & 0x81) != 0x81 ? 1 : 2;

                tag = new byte[1];
                value = new byte[valueLen];
                System.arraycopy(tlv, 0, tag, 0, 1);
                System.arraycopy(tlv, 1+lenLen, value, 0, valueLen);//

                mList.put(tag, value);
                Log.i(TAG,"mList_3 tag: "+ BCDASCII.bytesToHexString(tag)+"  tag: "+ BCDASCII.bytesToHexString(value));
                decodeTLV(value);

                if (tlv.length > 1+lenLen+valueLen) {
                    nextTlv = new byte[tlv.length-1-lenLen-valueLen];
                    System.arraycopy(tlv, 1+lenLen+valueLen, nextTlv, 0, tlv.length-1-lenLen-valueLen);
                    decodeTLV(nextTlv);
                }
            } else { // Tag为两个字节

                valueLen = (tlv[2] & 0x81) != 0x81 ? (tlv[2] & 0xFF) : (tlv[3] & 0xFF);
                lenLen = (tlv[2] & 0x81) != 0x81 ? 1 : 2;

                tag = new byte[2];
                value = new byte[valueLen];
                System.arraycopy(tlv, 0, tag, 0, 2);
                System.arraycopy(tlv, 2+lenLen, value, 0, valueLen);

                mList.put(tag, value);
                Log.i(TAG,"mList_4 tag: "+ BCDASCII.bytesToHexString(tag)+"  tag: "+ BCDASCII.bytesToHexString(value));
                decodeTLV(value);

                if (tlv.length > 1+lenLen+valueLen) {
                    nextTlv = new byte[tlv.length-2-lenLen-valueLen];
                    System.arraycopy(tlv, 2+lenLen+valueLen, nextTlv, 0, tlv.length-2-lenLen-valueLen);
                    decodeTLV(nextTlv);
                }
            }
        }
    }
}