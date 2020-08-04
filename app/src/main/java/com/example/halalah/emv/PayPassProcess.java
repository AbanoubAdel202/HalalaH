package com.example.halalah.emv;

import android.os.Bundle;
import android.os.RemoteException;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.qrcode.utils.SDKLog;
import com.topwise.cloudpos.aidl.emv.level2.AidlPaypass;
import com.topwise.cloudpos.aidl.emv.level2.ClssTornLogRecord;
import com.topwise.cloudpos.aidl.emv.level2.EmvCapk;
import com.topwise.cloudpos.aidl.emv.level2.EmvTerminalInfo;
import com.topwise.cloudpos.struct.BytesUtil;
import com.topwise.cloudpos.struct.TlvList;

import java.util.Arrays;

/**
 * @author xukun
 * @version 1.0.0
 * @date 19-9-17
 */

class PayPassProcess extends BasePayProcess {

    private static final String TAG = PayPassProcess.class.getSimpleName();
    private AidlPaypass paypass = DeviceTopUsdkServiceManager.getInstance().getL2Paypass();
    private int mSaveLogNum = 0;
    private ClssTornLogRecord[] mTornLogs;

    @Override
    void startPay(Bundle param, TransResultListener listener) {
        SDKLog.d(TAG, "startPaypass");
        try {
            paypass.initialize(1);
            byte[] data = param.getByteArray(PayDataUtil.CardCode.FINAL_SELECT_DATA);
            int len = param.getInt(PayDataUtil.CardCode.FINAL_SELECT_LEN);
            int res = paypass.setFinalSelectData(data, len);
            SDKLog.d(TAG, "setFinalSelectData res: " + res);

            if (!handleResult(res, listener)) {
                return;
            }
            String rid = getCurrentRid();  //getting RID from kernel
            byte[] aidData = getCurrentAidData(rid); // Getting Data from DB
            listener.setKernalData(PayDataUtil.KERNTYPE_MC, aidData);

            EmvTerminalInfo terminalInfo = EmvManager.getInstance().getEmvTerminalInfo();
            if (terminalInfo != null) {
                TlvList list = new TlvList();
                if (terminalInfo.getAucIFDSerialNumber().length() == 8) {
                    list.addTlv("9F1E", terminalInfo.getAucIFDSerialNumber().getBytes());
                }
                if (terminalInfo.getUcTerminalType() != -1) {
                    list.addTlv("9F35", new byte[]{terminalInfo.getUcTerminalType()});
                }
                if (terminalInfo.getAucTerminalCountryCode().length == 2) {
                    list.addTlv("9F1A", terminalInfo.getAucTerminalCountryCode());
                }
                if (terminalInfo.getAucTransCurrencyCode().length == 2) {
                    list.addTlv("5F2A", terminalInfo.getAucTransCurrencyCode());
                }
                if (terminalInfo.getAucTerminalCapabilities().length == 3) {
                    list.addTlv("9F33", terminalInfo.getAucTerminalCapabilities());
                }
                if (terminalInfo.getAucAddtionalTerminalCapabilities().length == 5) {
                    list.addTlv("9F40", terminalInfo.getAucAddtionalTerminalCapabilities());
                }
                if (!list.getList().isEmpty()) {
                    byte[] tlvData = list.getBytes();
                    paypass.setTLVDataList(tlvData, tlvData.length);
                }
            }
            listener.nextTransStep(PayDataUtil.CallbackSort.REQUEST_FINAL_AID_SELECT, null);

            byte[] dataBuf = new byte[1];
            res = paypass.gpoProc(dataBuf);
            SDKLog.d(TAG, "gpoProc res: " + res);
            if (!handleResult(res, listener)) {
                return;
            }

            res = paypass.readData();
            SDKLog.d(TAG, "readData res: " + res);
            if (!handleResult(res, listener)) {
                return;
            }

            byte[] ucAcType = new byte[1];
            SDKLog.d(TAG, "ucType: " + dataBuf[0]);
            if (dataBuf[0] == PayDataUtil.CLSS_TRANSPATH_EMV) {
                addCapk(rid);
                SDKLog.d(TAG, "start transProcMChip");
                int[] tornUpdateFlag = {0};
                int tornLogNum[] = {0};
                SDKLog.d(TAG, "mSaveLogNum: " + mSaveLogNum);
                if (mSaveLogNum > 0) {
                    paypass.setTornLogMChip(mTornLogs, mSaveLogNum);
                }
                res = paypass.transProcMChip(ucAcType);
                SDKLog.d(TAG, "end transProcMChip");
                Arrays.fill(tornUpdateFlag, 0);
                Arrays.fill(tornLogNum, 0);
                mTornLogs = new ClssTornLogRecord[5];
                paypass.getTornLogMChip(mTornLogs, tornLogNum, tornUpdateFlag);
                SDKLog.d(TAG, "getTornLogMChip tornUpdateFlag: " + tornUpdateFlag[0]);
                if (tornUpdateFlag[0] == 1) {
                    if (tornLogNum[0] > mSaveLogNum) {
                        listener.onProcessResult(true, true,
                                PayDataUtil.CardCode.TRANS_SECOND_READ);
                    }
                    mSaveLogNum = tornLogNum[0];
                    return;
                }

            } else if (dataBuf[0] == PayDataUtil.CLSS_TRANSPATH_MAG) {
                res = paypass.transProcMag(ucAcType);
            } else {
                res = PayDataUtil.CLSS_TERMINATE;
            }
            SDKLog.d(TAG, "trans proc res: " + res + ";ucAcType: " + ucAcType[0]);

            if (res != PayDataUtil.EMV_OK) {
                SDKLog.d(TAG, "trans fail!!");
                listener.onProcessResult(true, true, PayDataUtil.CardCode.TRANS_REFUSE);
                return;
            }
            //read card info
            byte[] cardData = new byte[32];
            int[] dataLen = new int[1];
            int res1 = paypass.getTLVDataList(BytesUtil.hexString2Bytes("57"),1, cardData.length, cardData, dataLen);
            if (res1 == PayDataUtil.EMV_OK) {
                byte[] track2 = new byte[dataLen[0]];
                System.arraycopy(cardData, 0, track2, 0, track2.length);
                SDKLog.d(TAG, "track2 data len: " + dataLen[0]);
                String track2Data = BytesUtil.bytes2HexString(track2);
                SDKLog.d(TAG, "track2 data: " + track2Data);
                String cardNo = track2Data.split("D")[0];
                if (cardNo.length() % 2 != 0) {
                    cardNo = cardNo + "F";
                }
                listener.setCardNo(BytesUtil.hexString2Bytes(cardNo));
            }
            //DF8129 tag  --is need pwd
            byte[] outComeBuffer = new byte[17];
            int[] bufLen = new int[1];
            res = paypass.getTLVDataList(new byte[]{(byte) 0xDF, (byte) 0x81, 0x29},
                    3, outComeBuffer.length, outComeBuffer, bufLen);
            if (res == PayDataUtil.EMV_OK) {
                SDKLog.d(TAG, "bufLen: " + bufLen[0]);
                byte[] outData = new byte[bufLen[0]];
                System.arraycopy(outComeBuffer, 0, outData, 0, bufLen[0]);
                SDKLog.d(TAG, "real outcome data: " + BytesUtil.bytes2HexString(outData));
                //judge import pin
                if ((outData[3] & 0xF0) == PayDataUtil.CLSS_OC_ONLINE_PIN) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(PayDataUtil.CardCode.IMPORT_PIN_TYPE, PayDataUtil.PINTYPE_ONLINE);
                    listener.nextTransStep(PayDataUtil.CallbackSort.REQUEST_IMPORT_PIN, bundle);
                }
            }
            switch (ucAcType[0]) {
                case PayDataUtil.AC_TC:
                    //offline success
                    SDKLog.d(TAG, "TC offline success");
                    listener.onProcessResult(false, true, PayDataUtil.CardCode.TRANS_APPROVAL);
                    break;
                case PayDataUtil.AC_ARQC:
                    //online success
                    SDKLog.d(TAG, "ARQC online success");
                    listener.onProcessResult(true, false, PayDataUtil.CardCode.TRANS_APPROVAL);
                    break;
                case PayDataUtil.AC_AAC:
                    //transaction reject
                    SDKLog.d(TAG, "AAC transaction reject");
                    listener.onProcessResult(true, true, PayDataUtil.CardCode.TRANS_REFUSE);
                    break;
                default:
                    break;
            }
        }catch(RemoteException e){
            listener.onFail(e.hashCode());
        }
    }

    @Override
    boolean scriptProcess(boolean onlineRes, String respCode, String icc55, TransResultListener listener ) {
        // 没有脚本处理逻辑
        return false;
    }

    /**
     * 获取当前rid
     *
     * @return 当前应用rid
     */
    private String getCurrentRid() {
        String aid = null;
        try {
            paypass.delAllRevocList();
            paypass.delAllCAPK();
            int[] realLen = new int[1];
            byte[] aucAid = new byte[17];
            int res = paypass.getTLVDataList(new byte[]{0x4F}, 1,
                    aucAid.length, aucAid, realLen);
            SDKLog.d(TAG, "getTLVDataList capk aid res: " + res);
            if (res == PayDataUtil.EMV_OK) {
                if (realLen[0] > 0) {
                    byte[] aidData = new byte[realLen[0]];
                    System.arraycopy(aucAid, 0, aidData, 0, realLen[0]);
                    aid = BytesUtil.bytes2HexString(aidData);
                    SDKLog.d(TAG, "aid len: " + realLen[0] + ";aid: " + aid);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return aid;
    }

    /**
     * 添加CAPK到交易库
     */
    private void addCapk(String aid) {
        try {
            paypass.delAllRevocList();
            paypass.delAllCAPK();
            byte[] index = new byte[1];
            int[] realLen = new int[1];
            SDKLog.d(TAG, "aid: " + aid);
            int res = paypass.getTLVDataList(new byte[]{(byte) 0x8F},
                    1, 1, index, realLen);
            if (res == PayDataUtil.EMV_OK) {
                SDKLog.d(TAG, "capk index: " + index[0]);
                //import capk
                EmvCapk emvCapk = getCurrentCapk(BytesUtil.hexString2Bytes(aid), index);
                SDKLog.d(TAG, "add capk: " + emvCapk);
                if (emvCapk != null) {
                    res = paypass.addCAPK(emvCapk);
                    SDKLog.d(TAG, "add capk res: " + res);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
