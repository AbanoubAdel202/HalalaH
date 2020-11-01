package com.example.halalah.emv;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.qrcode.utils.SDKLog;
import com.example.halalah.util.CommonFunction;
import com.topwise.cloudpos.aidl.emv.level2.AidlEntry;
import com.topwise.cloudpos.aidl.emv.level2.AidlQpboc;
import com.topwise.cloudpos.aidl.emv.level2.EmvCapk;
import com.topwise.cloudpos.aidl.emv.level2.EmvTerminalInfo;
import com.topwise.cloudpos.aidl.emv.level2.PreProcResult;
import com.topwise.cloudpos.aidl.emv.level2.QpbocCallback;
import com.topwise.cloudpos.struct.BytesUtil;
import com.topwise.cloudpos.struct.Tlv;
import com.topwise.cloudpos.struct.TlvList;

import java.util.Map;

public class QpbocProcess extends BasePayProcess {

    private static final String TAG = QpbocProcess.class.getSimpleName();
    private AidlQpboc aidlQpboc = DeviceTopUsdkServiceManager.getInstance().getL2Qpboc();
    private AidlEntry entryL2 = DeviceTopUsdkServiceManager.getInstance().getL2Entry();

    private QpbocCallback qpbocCallback = new QpbocCallback.Stub() {

        @Override
        public int cCheckExceptionFile(byte[] bytes, int i, byte b) throws RemoteException {
            SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "cCheckExceptionFile");
            SDKLog.d(TAG, "PAN: " + BytesUtil.bytes2HexString(bytes));
            SDKLog.d(TAG, "PAN length: " + i);
            SDKLog.d(TAG, "PAN sequence no: " + b);

            return 0;
        }

        @Override
        public int cRFU1() throws RemoteException {
            return 0;
        }

        @Override
        public int cRFU2() throws RemoteException {
            return 0;
        }
    };

    @Override
    void startPay(Bundle param, TransResultListener listener) {
        SDKLog.d(TAG, "startQpboc()");
        int res = 0;

        try {
            PreProcResult preProcResult = param.getParcelable(PayDataUtil.CardCode.PREPROC_RESULT);
            if (preProcResult == null) {
                SDKLog.d(TAG, "preProcResult = null");
                listener.onProcessResult(false, true, PayDataUtil.CardCode.TRANS_STOP);
                return;
            }

            res = aidlQpboc.initialize((byte)EmvDefinition.KERNTYPE_QUICS, (byte)0);
            SDKLog.d(TAG, "aidlQpboc.initialize res: " + res);
            if (res != EmvDefinition.EMV_OK) {
                listener.onProcessResult(false, true, PayDataUtil.CardCode.TRANS_STOP);
                return;
            }

            res = aidlQpboc.setCallback(qpbocCallback);
            SDKLog.d(TAG, "aidlQpboc.setCallback res: " + res);
            if (res != EmvDefinition.EMV_OK) {
                listener.onProcessResult(false, true, PayDataUtil.CardCode.TRANS_STOP);
                return;
            }

            byte[] data = param.getByteArray(PayDataUtil.CardCode.FINAL_SELECT_DATA);
            int len = param.getInt(PayDataUtil.CardCode.FINAL_SELECT_LEN);
            res = aidlQpboc.setFinalSelectData(data, len);
            SDKLog.d(TAG, "aidlQpboc.setFinalSelectData res: " + res);
            if (res == EmvDefinition.EMV_SELECT_NEXT_AID) {
                int ret = entryL2.delCandListCurApp();
                SDKLog.d(TAG, "entryL2.delCandListCurApp ret: " + ret);
                if (ret == EmvDefinition.EMV_OK) {
                    listener.finalSelectAgain();
                } else {
                    listener.onProcessResult(true, true, PayDataUtil.CardCode.TRANS_STOP);
                }
                return;
            } else if (res != EmvDefinition.EMV_OK) {
                listener.onProcessResult(false, true, PayDataUtil.CardCode.TRANS_STOP);
                return;
            }

            String rid = getCurrentAid();
            if (rid == null) {
                SDKLog.d(TAG, "getCurrentAid = null");
                listener.onProcessResult(false, true, PayDataUtil.CardCode.TRANS_STOP);
                return;
            }
            byte[] aidData = getCurrentAidData(rid);
            if (aidData != null && aidData.length > 0) {
                listener.setKernalData(PayDataUtil.KERNTYPE_QPBOC, aidData);
            }

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
                    setTLVDataList(tlvData, tlvData.length);
                }
            }
            listener.nextTransStep(PayDataUtil.CallbackSort.REQUEST_FINAL_AID_SELECT, null);

            //Set TTQ
            byte[] TTQ = new byte[4];
            byte[] getTTQ = preProcResult.getAucReaderTTQ();
            if (getTTQ == null) {
                SDKLog.d(TAG, "getAucReaderTTQ = null");
                listener.onProcessResult(false, true, PayDataUtil.CardCode.TRANS_STOP);
                return;
            }
            Log.d(TAG, "getAucReaderTTQ: " + BytesUtil.bytes2HexString(getTTQ));
            System.arraycopy(getTTQ, 0, TTQ, 0, 4);
            //Topwise qPBOC Terminal Transaction Qualifiers
            //Byte1 Bit7 : 0 – Full transaction flow in Contactless interface Not Support
            //Byte1 Bit1 : 0 – Offline Data Authentication for Online Authorisation Not Supported
            TTQ[0] &= (byte)0x3E; //0011 1110
            //Byte3 Bit7 : 1- Consumer Device CVM Supported
            TTQ[2] = 0x40; //0100 0000
            //Byte4 Bit8 : 1 - fDDA v1.0 Supported
            TTQ[3] = (byte)0x80; //1000 0000
            Log.d(TAG, "Set TTQ: " + BytesUtil.bytes2HexString(TTQ));
            aidlQpboc.setTLVData(0x9F66, TTQ);

            //Application Initialization (GPO)
            byte[] transPath = new byte[1];
            res = aidlQpboc.gpoProc(transPath);
            SDKLog.d(TAG, "aidlQpboc.gpoProc res: " + res);
            if (res == EmvDefinition.EMV_SELECT_NEXT_AID) {
                int ret = entryL2.delCandListCurApp();
                SDKLog.d(TAG, "entryL2.delCandListCurApp ret: " + ret);
                if (ret == EmvDefinition.EMV_OK) {
                    listener.finalSelectAgain();
                } else {
                    listener.onProcessResult(true, true, PayDataUtil.CardCode.TRANS_STOP);
                }
                return;
            } else if (res == EmvDefinition.EMV_SEE_PHONE) {
                listener.onProcessResult(false, true, PayDataUtil.CardCode.CDCVM_SECOND_READ_CARD);
                return;
            } else if (res == EmvDefinition.EMV_TRY_AGAIN) {
                listener.onProcessResult(false, true, PayDataUtil.CardCode.TRANS_AGAIN_CHECK_CARD);
                return;
            } else if (res != EmvDefinition.EMV_APPROVED && res != EmvDefinition.EMV_DECLINED && res != EmvDefinition.EMV_ONLINE_REQUEST) {
                listener.onProcessResult(true, true, PayDataUtil.CardCode.TRANS_STOP);
                return;
            }

            if (transPath[0] != PayDataUtil.CLSS_TRANSPATH_EMV) {
                listener.onProcessResult(true, true, PayDataUtil.CardCode.TRANS_STOP);
                return;
            }

            //Read Application Data
            res = aidlQpboc.readData();
            SDKLog.d(TAG, "aidlQpboc.readData res: " + res);
            if (res != EmvDefinition.EMV_APPROVED && res != EmvDefinition.EMV_DECLINED && res != EmvDefinition.EMV_ONLINE_REQUEST) {
                listener.onProcessResult(true, true, PayDataUtil.CardCode.TRANS_STOP);
                return;
            }

            //Offline Data Authentication
            if (res == EmvDefinition.EMV_APPROVED) {
                addCapk(rid);
                res = aidlQpboc.cardAuth();
                SDKLog.d(TAG, "aidlQpboc.readData res: " + res);
            }

            //Cardholder Verification
            byte[] cvmResBuf = aidlQpboc.getTLVData(0x9F34);
            if (cvmResBuf != null && cvmResBuf.length > 0) {
                SDKLog.d(TAG, "cvmResBuf: " + BytesUtil.bytes2HexString(cvmResBuf));
                if (cvmResBuf[0] == 0x02) {
                    //Online enciphered PIN
                    Bundle bundle = new Bundle();
                    bundle.putInt(PayDataUtil.CardCode.IMPORT_PIN_TYPE, PayDataUtil.PINTYPE_ONLINE);
                    listener.nextTransStep(PayDataUtil.CallbackSort.REQUEST_IMPORT_PIN, bundle);
                }
            }

            switch (res) {
                case EmvDefinition.EMV_APPROVED:
                    //offline success
                    SDKLog.d(TAG, "TC offline success");
                    listener.onProcessResult(false, true, PayDataUtil.CardCode.TRANS_APPROVAL);
                    break;
                case EmvDefinition.EMV_ONLINE_REQUEST:
                    //online success
                    SDKLog.d(TAG, "ARQC online success");
                    listener.onProcessResult(true, false, PayDataUtil.CardCode.TRANS_APPROVAL);
                    break;
                case EmvDefinition.EMV_DECLINED:
                    //transaction reject
                    SDKLog.d(TAG, "AAC transaction reject");
                    listener.onProcessResult(true, true, PayDataUtil.CardCode.TRANS_REFUSE);
                    break;
                default:
                    break;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    boolean scriptProcess(boolean onlineRes, String respCode, String icc55, TransResultListener listener) throws RemoteException {
        return false;
    }

    public static int setTLVDataList(byte[] pTLVDatas, int iDataLen) {
        SDKLog.d(TAG, "setTLVDataList()");
        AidlQpboc aidlQpboc = DeviceTopUsdkServiceManager.getInstance().getL2Qpboc();
        TlvList tlvlist = new TlvList();
        byte[] tlvsBuf = new byte[iDataLen];

        System.arraycopy(pTLVDatas, 0, tlvsBuf, 0, iDataLen);
        SDKLog.d(TAG, "pTLVDatas: " + BytesUtil.bytes2HexString(tlvsBuf));

        tlvlist.fromBytes(tlvsBuf);
        if (tlvlist.getList() != null && tlvlist.getList().size() > 0) {
            for (Map.Entry<String, Tlv> entry : tlvlist.getList().entrySet()) {

                byte[] bTag = BytesUtil.hexString2Bytes(entry.getValue().getTag());
                Log.d(TAG, "bTag: " + BytesUtil.bytes2HexString(bTag));

                byte[] bTag4Bytes = new byte[4];
                java.util.Arrays.fill(bTag4Bytes, (byte)0);
                System.arraycopy(bTag, 0, bTag4Bytes, bTag4Bytes.length - bTag.length, bTag.length);
                Log.d(TAG, "bTag4Bytes: " + BytesUtil.bytes2HexString(bTag4Bytes));

                //The first parameter of 'BytesUtil.bytes2Int' must be 4 bytes
                int iTag = BytesUtil.bytes2Int(bTag4Bytes, true);
                Log.d(TAG, "iTag: " + iTag);

                SDKLog.d(TAG, "Value: " + BytesUtil.bytes2HexString(entry.getValue().getValue()));

                try {
                    aidlQpboc.setTLVData(iTag, entry.getValue().getValue());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }

    private String getCurrentAid() {
        SDKLog.d(TAG, "getCurrentAid()");
        String aid = null;
        try {
            byte[] aucAid = aidlQpboc.getTLVData(0x9F06);
            if (aucAid != null) {
                aid = BytesUtil.bytes2HexString(aucAid);
                SDKLog.d(TAG,"aid: " + aid);
            } else {
                SDKLog.d(TAG, "aidlQpboc.getTLVData aucAid == null" );
                return null;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return aid;
    }

    private void addCapk(String aid) {
        SDKLog.d(TAG, "addCapk(),  aid: " + aid);
        try {
            aidlQpboc.delAllRevoIPK();
            aidlQpboc.delAllCAPK();
            byte[] index = aidlQpboc.getTLVData(0x8F);
            if (index == null) {
                SDKLog.d(TAG, "aidlQpboc.getTLVData capk index == null ");
                index = new byte[1];
                index[0] = 0x00;
            }

            SDKLog.d(TAG, "capk index: " + index[0]);
            //import CAPK
            EmvCapk emvCapk = getCurrentCapk(BytesUtil.hexString2Bytes(aid), index);
            SDKLog.d(TAG, "add capk: " + emvCapk);
            if (emvCapk != null) {
                int res = aidlQpboc.addCAPK(emvCapk);
                SDKLog.d(TAG, "aidlQpboc.addCAPK res: " + res);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
