package com.example.halalah.emv;

import android.os.Bundle;
import android.os.RemoteException;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.qrcode.utils.SDKLog;
import com.topwise.cloudpos.aidl.emv.TerminalParam;
import com.topwise.cloudpos.aidl.emv.level2.AidlPure;
import com.topwise.cloudpos.aidl.emv.level2.EmvCapk;
import com.topwise.cloudpos.aidl.emv.level2.EmvTerminalInfo;
import com.topwise.cloudpos.aidl.emv.level2.PreProcResult;
import com.topwise.cloudpos.aidl.emv.level2.TransParam;
import com.topwise.cloudpos.struct.BytesUtil;
import com.topwise.cloudpos.struct.TlvList;

/**
 * Pure Process
 * Created by topwise on 20-07-09.
 */
class PureProcess extends BasePayProcess {

    private static final String TAG = PureProcess.class.getSimpleName();
    private AidlPure pure = DeviceTopUsdkServiceManager.getInstance().getL2Pure();
    @Override
    void startPay(Bundle param, TransResultListener listener) {
        SDKLog.d(TAG, "startPay");
        try {
            pure.initialize();
            byte[] data = param.getByteArray(PayDataUtil.CardCode.FINAL_SELECT_DATA);
            int len = param.getInt(PayDataUtil.CardCode.FINAL_SELECT_LEN);
            int res = pure.setFinalSelectData(data, len);
            SDKLog.d(TAG, "setFinalSelectData res: " + res);
            SDKLog.d(TAG, "setFinalSelectData data: " + BytesUtil.bytes2HexString(data));
            if (!handleResult(res, listener)) {
                return;
            }

            String rid = getCurrentRid();
            byte[] aidData = getCurrentAidData(rid);
            listener.setKernalData(PayDataUtil.KERNTYPE_PURE, aidData);

            EmvTerminalInfo terminalInfo = EmvManager.getInstance().getEmvTerminalInfo();
            if(terminalInfo!=null){
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
                    pure.setTLVDataList(tlvData, tlvData.length);
                }
            }
            listener.nextTransStep(PayDataUtil.CallbackSort.REQUEST_FINAL_AID_SELECT, null);

            //PURE_Config_01
            byte[] transTypeAuthAppli = {(byte)0xFF, (byte)0x81, 0x35, 0x01, (byte)0x90};
            pure.setTLVDataList(transTypeAuthAppli, transTypeAuthAppli.length);

            byte[] ATOL = {(byte)0xFF, (byte)0x81, 0x30, 0x28,
                    (byte)0x9F, 0x02, (byte)0x9F, 0x03, (byte)0x9F, 0x26, (byte)0x82, (byte)0x9F, 0x36, (byte)0x9F,
                    0x27, (byte)0x9F, 0x10, (byte)0x9F, 0x1A, (byte)0x95, 0x5F, 0x2A, (byte)0x9A, (byte)0x9C,
                    (byte)0x9F, 0x37, (byte)0x9F, 0x35, 0x57, (byte)0x9F, 0x34, (byte)0x84, 0x5F, 0x34,
                    0x5A, (byte)0xC7, (byte)0x9F, 0x33, (byte)0x9F, 0x73, (byte)0x9F, 0x77, (byte)0x9F, 0x45};
            pure.setTLVDataList(ATOL, ATOL.length);

            byte[] MTOL = {(byte)0xFF, (byte)0x81, 0x31, 0x02,
                    (byte)0x8C, 0x57};
            pure.setTLVDataList(MTOL, MTOL.length);

            byte[] ATDTOL = {(byte)0xFF, (byte)0x81, 0x32, 0x05,
                    (byte)0x82, (byte)0x95, (byte)0x9F, (byte)0x77, (byte)0x84};
            pure.setTLVDataList(ATDTOL, ATDTOL.length);

            byte[] appCapabilities = {(byte)0xFF, (byte)0x81, 0x33, 0x05,
                    0x36, 0x00, 0x60, 0x43, (byte)0xF9};
            pure.setTLVDataList(appCapabilities, appCapabilities.length);

            byte[] implOption = {(byte)0xFF, (byte)0x81, 0x34, 0x01, (byte)0xFF};
            pure.setTLVDataList(implOption, implOption.length);

            byte[] defaultDDOL = {(byte)0xFF, (byte)0x81, 0x36, 0x03,
                    (byte)0x9F, 0x37, 0x04};
            pure.setTLVDataList(defaultDDOL, defaultDDOL.length);

            byte[] posTimeout = {(byte)0xDF, (byte)0x81, 0x27, 0x02,
                    0x10, 0x00};
            pure.setTLVDataList(posTimeout, posTimeout.length);

            byte[] envelope2 = {(byte)0x9F, 0x76, 0x09,
                    0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
            pure.setTLVDataList(envelope2, envelope2.length);

            PreProcResult preProcResult = param.getParcelable(PayDataUtil.CardCode.PREPROC_RESULT);
            if (preProcResult != null) {
                String buffer = BytesUtil.bytes2HexString(preProcResult.getAucReaderTTQ());
                SDKLog.d(TAG, "preProcResult.getAucReaderTTQ: " + buffer);
                if (buffer.contains("00000000")) {
                    preProcResult.setAucReaderTTQ(BytesUtil.hexString2Bytes("3600C000"));
                }
            }

            byte[] C7Tlv = new byte[7];
            byte[] C7Value = new byte[5];
            int[] C7Len = new int[1];
            byte[] Tlv95 = new byte[7];
            byte[] Value95 = new byte[5];
            int[] Len95 = new int[1];
            if ((preProcResult.getUcRdCLFLmtExceed() == 1) ||
                    (preProcResult.getUcTermFLmtExceed() == 1)) {
                pure.getTLVDataList(new byte[]{(byte)0xC7}, 1, C7Value.length, C7Value, C7Len);
                //Byte2 Bit8
                C7Value[1] |= (byte)0x80;
                C7Tlv[0] = (byte)0xC7;
                C7Tlv[1] = 0x05;
                System.arraycopy(C7Value, 0, C7Tlv, 2, C7Value.length);
                pure.setTLVDataList(C7Tlv, C7Tlv.length);

                pure.getTLVDataList(new byte[]{(byte)0x95}, 1, Value95.length, Value95, Len95);
                //Byte4 Bit8
                Value95[3] |= (byte)0x80;
                Tlv95[0] = (byte)0x95;
                Tlv95[1] = 0x05;
                System.arraycopy(Value95, 0, Tlv95, 2, Value95.length);
                pure.setTLVDataList(Tlv95, Tlv95.length);
            }
            if (preProcResult.getUcRdCVMLmtExceed() == 1) {
                pure.getTLVDataList(new byte[]{(byte)0xC7}, 1, C7Value.length, C7Value, C7Len);

                //Byte2 Bit7
                C7Value[1] |= (byte)0x40;
                C7Tlv[0] = (byte)0xC7;
                C7Tlv[1] = 0x05;
                System.arraycopy(C7Value, 0, C7Tlv, 2, C7Value.length);
                pure.setTLVDataList(C7Tlv, C7Tlv.length);
            }
            if (preProcResult.getUcStatusCheckFlg() == 1) {
                pure.getTLVDataList(new byte[]{(byte)0xC7}, 1, C7Value.length, C7Value, C7Len);

                //Byte2 Bit6
                C7Value[1] |= (byte)0x20;
                C7Tlv[0] = (byte)0xC7;
                C7Tlv[1] = 0x05;
                System.arraycopy(C7Value, 0, C7Tlv, 2, C7Value.length);
                pure.setTLVDataList(C7Tlv, C7Tlv.length);
            }
            if (preProcResult.getUcZeroAmtFlg() == 1) {
                pure.getTLVDataList(new byte[]{(byte)0xC7}, 1, C7Value.length, C7Value, C7Len);

                //Byte2 Bit5
                C7Value[1] |= (byte)0x10;
                C7Tlv[0] = (byte)0xC7;
                C7Tlv[1] = 0x05;
                System.arraycopy(C7Value, 0, C7Tlv, 2, C7Value.length);
                pure.setTLVDataList(C7Tlv, C7Tlv.length);
            }

            res = pure.gpoProc();
            SDKLog.d(TAG, "gpoProc res: " + res);
            if (!handleResult(res, listener)) {
                return;
            }

            res = pure.readData();
            SDKLog.d(TAG, "readData res: " + res);
            if (!handleResult(res, listener)) {
                return;
            }

            //read card info
            byte[] cardData = new byte[32];
            int[] dataLen = new int[1];
            int res1 = pure.getTLVDataList(BytesUtil.hexString2Bytes("57"),
                    1, cardData.length, cardData, dataLen);
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

            //Add Capk
            addCapk(rid);

            res = pure.startTrans((byte)0);
            SDKLog.d(TAG, "startTrans res: " + res);
            if (!handleResult(res, listener)) {
                return;
            }

            res = pure.cardAuth();
            SDKLog.d(TAG, "pure.cardAuth: res=" + res);
            if (!handleResult(res, listener)) {
                return;
            }

//DF8129 tag  --is need pwd
            byte[] outComeBuffer = new byte[17];
            int[] bufLen = new int[1];
            res = pure.getTLVDataList(new byte[]{(byte) 0xDF, (byte) 0x81, 0x29},
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

            switch (outComeBuffer[0]) {
                case 0x10:
                    //offline success
                    SDKLog.d(TAG, "TC offline success");
                    listener.onProcessResult(false, true, PayDataUtil.CardCode.TRANS_APPROVAL);
                    break;
                case 0x30:
                    //online success
                    SDKLog.d(TAG, "ARQC online success");
                    listener.onProcessResult(true, false, PayDataUtil.CardCode.TRANS_APPROVAL);
                    break;
                case 0x20:
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
    boolean scriptProcess(boolean onlineRes, String respCode, String icc55, TransResultListener listener) {
        // AE¿¨Ã»ÓÐ½Å±¾´¦Àí
        return false;
    }

    /**
     * »ñÈ¡µ±Ç°rid
     *
     * @return µ±Ç°Ó¦ÓÃrid
     */
    private String getCurrentRid() {
        String aid = null;
        try {
            pure.delAllRevocList();
            pure.delAllCAPK();
            int[] realLen = new int[1];
            byte[] aucAid = new byte[17];
            int res = pure.getTLVDataList(new byte[]{0x4F}, 1,
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
        }catch(RemoteException e){
            e.printStackTrace();
        }
        return aid;
    }

    /**
     * Ìí¼ÓCAPKµ½½»Ò×¿â
     */
    private void addCapk(String aid) {
        try {
            pure.delAllRevocList();
            pure.delAllCAPK();
            byte[] index = new byte[1];
            int[] realLen = new int[1];
            SDKLog.d(TAG, "aid: " + aid);
            int res = pure.getTLVDataList(new byte[]{(byte) 0x8F},
                    1, 1, index, realLen);
            if (res == PayDataUtil.EMV_OK) {
                SDKLog.d(TAG, "capk index: " + index[0]);
                //import capk
                EmvCapk emvCapk = getCurrentCapk(BytesUtil.hexString2Bytes(aid), index);
                SDKLog.d(TAG, "add capk: " + emvCapk);
                if (emvCapk != null) {
                    res = pure.addCAPK(emvCapk);
                    SDKLog.d(TAG, "add capk res: " + res);
                }
            }
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }
}
