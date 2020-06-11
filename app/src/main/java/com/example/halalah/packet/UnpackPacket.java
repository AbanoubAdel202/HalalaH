package com.example.halalah.packet;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.ui.PacketProcessActivity;
import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.R;
import com.example.halalah.Utils;

import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.iso8583.ISO8583Util;
import com.example.halalah.storage.MerchantInfo;
import com.example.halalah.util.PacketProcessUtils;
import com.example.halalah.util.TLVDecode;
import com.topwise.cloudpos.aidl.emv.AidlPboc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UnpackPacket {
    private static final String TAG = Utils.TAGPUBLIC + UnpackPacket.class.getSimpleName();

    private MerchantInfo mMerchantInfo;
    private String mTermId;
    private byte[] mRecePacket = null;
    private int mProcType;
    private String mResponse;
    private String mResponseDetail;
    private byte[] mField47;


    private AidlPboc mPobcManager;
    private List<LinkedHashMap<byte[], byte[]>> mQueryEmvList;
    private ArrayList<byte[]> mDownload62List;

    private byte[] mFieldRece62 = null;
    private int mField62FirstByte;
    private int mDownloacEmvNum = 0;
    private int mSetEmvNum = 0;

    public UnpackPacket(Context context, int procType) {
        mProcType = procType;

        mMerchantInfo = new MerchantInfo(context);
        mTermId = mMerchantInfo.getTermId();
       if (mProcType == PacketProcessUtils.PACKET_PROCESS_IC_CAPK_DOWNLOAD ||
                mProcType == PacketProcessUtils.PACKET_PROCESS_IC_PARA_DOWNLOAD) {
            mQueryEmvList = new ArrayList<LinkedHashMap<byte[], byte[]>>();
            mPobcManager = DeviceTopUsdkServiceManager.getInstance().getPbocManager();
        }
    }

    public void procRecePacket(Context context, byte[] recePacket, Bundle data) {
        Log.i(TAG, "procRecePacket(), mProcType" + mProcType);
        mRecePacket = recePacket;

 if (mProcType == PacketProcessUtils.PACKET_PROCESS_PARAM_TRANS) {
            UnpackParaTrans unpackParaTrans = new UnpackParaTrans(context, mRecePacket, mRecePacket.length);
            mResponse = unpackParaTrans.getResponse();
            mResponseDetail = unpackParaTrans.getResponseDetail();
        } else if (mProcType == PacketProcessUtils.PACKET_PROCESS_STATUS_UPLOAD ||
                mProcType == PacketProcessUtils.PACKET_PROCESS_ECHO_TEST ) {
            UnpackDefault unpackDefault = new UnpackDefault(mRecePacket, mRecePacket.length);
            mResponse = unpackDefault.getResponse();
            mResponseDetail = unpackDefault.getResponseDetail();

        } else if (mProcType == PacketProcessUtils.PACKET_PROCESS_PURCHASE) {
            UnpackPurchase unpackpurchase = new UnpackPurchase(mRecePacket, mRecePacket.length);
            mResponse = unpackpurchase.getResponse();
            mResponseDetail = unpackpurchase.getResponseDetail();

        }
    }

    public String getResponse() {
        return mResponse;
    }

    public String getResponseDetail() {
        return mResponseDetail;
    }

    public byte[] getField47() {
        return mField47;
    }



    private boolean setEmvCAPK(byte[] f62) {
        if (mPobcManager == null) {
            return false;
        }

        boolean isSetCapkSuccess = false;
        try {
            isSetCapkSuccess = mPobcManager.updateCAPK(0x01, BCDASCII.bytesToHexString(f62));
            Log.i(TAG, "setEmvCAPK: " + isSetCapkSuccess + ", f62: " + BCDASCII.bytesToHexString(f62));
            if (isSetCapkSuccess) {
                mSetEmvNum++;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return isSetCapkSuccess;
    }

    public boolean clearEmvCapk() {
        Log.i(TAG, "clearEmvCapk()");
        if (mPobcManager == null) {
            return false;
        }
        boolean isClearCapkSuccess = false;
        try {
            isClearCapkSuccess = mPobcManager.updateCAPK(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return isClearCapkSuccess;
    }

    private boolean setEmvPARA(byte[] f62) {
        if (mPobcManager == null) {
            return false;
        }
        boolean isSetParaSuccess = false;
        try {
            isSetParaSuccess = mPobcManager.updateAID(0x01, BCDASCII.bytesToHexString(f62));
            Log.i(TAG, "setEmvPARA: " + isSetParaSuccess + ", f62: " + BCDASCII.bytesToHexString(f62));
            if (isSetParaSuccess) {
                mSetEmvNum++;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return isSetParaSuccess;
    }

    public boolean clearEmvPara() {
        Log.i(TAG, "clearEmvPara()");
        if (mPobcManager == null) {
            return false;
        }

        boolean isClearAidSuccess = false;
        try {
            isClearAidSuccess = mPobcManager.updateAID(0x03, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return isClearAidSuccess;
    }

    private void getDownloadCapkTLV62(Map<byte[], byte[]> queryCapkList) {
        if (queryCapkList.size() < 1) {
            return;
        }

        byte[] tlv;
        int tlvLen = 0;

        byte[] rid = null;
        byte[] ind = null;
        for (byte[] key : queryCapkList.keySet()) {
            Log.i(TAG, "getDownloadCapkTLV62, key:" + BCDASCII.bytesToHexString(key));

            String tag = BCDASCII.bytesToHexString(key);
            byte[] value;

            value = queryCapkList.get(key);
            int len = PackUtils.getfixedNumber(value.length, 2, "0").length() + value.length;
            switch (tag) {
                case "9F06": // RID
                    rid = new byte[len + 2];
                    rid = ISO8583Util.byteArrayAdd(new byte[]{(byte) 0x9F, 0x06}, BCDASCII.hexStringToBytes(PackUtils.getfixedNumber(value.length, 2, "0")), value);
                    tlvLen += (len + 2);
                    Log.i(TAG, "rid: " + BCDASCII.bytesToHexString(rid));
                    break;
                case "9F22": // IDX
                    ind = new byte[len + 2];
                    ind = ISO8583Util.byteArrayAdd(new byte[]{(byte) 0x9F, 0x22}, BCDASCII.hexStringToBytes(PackUtils.getfixedNumber(value.length, 2, "0")), value);
                    tlvLen += (len + 2);
                    Log.i(TAG, "ind: " + BCDASCII.bytesToHexString(ind));
                    break;
                case "DF05": // EXP DATE
                    break;
                case "DF02": // 公钥模
                    break;
                case "DF04": // 公钥指数
                    break;
                case "DF03": // SHA
                    break;
                default:
                    break;
            }

            if ((rid != null) && (ind != null)) {
                tlv = new byte[tlvLen];
                tlv = ISO8583Util.byteArrayAdd(rid, ind);
                Log.i(TAG, "tlv: " + BCDASCII.bytesToHexString(tlv));
                mDownload62List.add(tlv);
                rid = null;
                ind = null;
            }
        }
    }

    private void getDownloadParaTLV62(Map<byte[], byte[]> queryParaList) {
        if (queryParaList.size() < 1) {
            return;
        }

        byte[] aid = null;
        for (byte[] key : queryParaList.keySet()) {
            Log.i(TAG, "getDownloadParaTLV62, key:" + BCDASCII.bytesToHexString(key));

            String tag = BCDASCII.bytesToHexString(key);
            byte[] value;

            value = queryParaList.get(key);
            int len = PackUtils.getfixedNumber(value.length, 2, "0").length() + value.length;
            switch (tag) {
                case "9F06": // AID
                    aid = new byte[len + 2];
                    aid = ISO8583Util.byteArrayAdd(new byte[]{(byte) 0x9F, 0x06}, BCDASCII.hexStringToBytes(PackUtils.getfixedNumber(value.length, 2, "0")), value);
                    Log.i(TAG, "aid: " + BCDASCII.bytesToHexString(aid));
                    break;
                case "9F1B": // minimum amount
                    break;
                case "9F08": // Application version number (card), useless? ?? UnionPay POST sent this is wrong, it should be changed to 9F09
                case "9F09": // Application version number (terminal), UnionPay POST send did not send this
                    break;
                case "DF01": // ASI, whether to allow AID partial match
                    break;
                case "DF11": // TAC default
                    break;
                case "DF12": // TAC online
                    break;
                case "DF13": // TAC refused
                    break;
                case "DF15": // Bias random selection threshold
                    break;
                case "DF16": // Randomly choose max%
                    break;
                case "DF17": // Select target%
                    break;
                case "DF14": // defaultDDOL
                    break;
                case "DF19":
                    break;
                case "DF20":
                    break;
                case "DF21":
                    break;
                case "DF18": // Online PIN support capability 1: support; 0 does not support
                    break;
                case "9F7B":
                    break;
                default:
                    break;
            }
            if (aid != null) {
                mDownload62List.add(aid);
                aid = null;
            }
        }
    }

    public byte[] getField62(PacketProcessActivity activity) {
        if (mDownload62List == null) {
            return null;
        }
        if (mProcType == PacketProcessUtils.PACKET_PROCESS_IC_CAPK_DOWNLOAD) {
            activity.mTextProcDetail.setText(String.format(activity.getResources().getString(R.string.socket_proc_detail_emv_capk_download), mDownloacEmvNum + 1));
        } else {
            activity.mTextProcDetail.setText(String.format(activity.getResources().getString(R.string.socket_proc_detail_emv_para_download), mDownloacEmvNum + 1));
        }
        byte[] field62 = mDownload62List.get(mDownloacEmvNum++);
        Log.i(TAG, "field62 = " + BCDASCII.bytesToHexString(field62));
        return field62;
    }
}