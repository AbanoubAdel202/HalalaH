package com.example.halalah.emv;

import android.os.RemoteException;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.card.CardType;
import com.example.halalah.card.EmvTransData;
import com.example.halalah.database.table.DBManager;
import com.example.halalah.qrcode.utils.SDKLog;
import com.example.halalah.util.CommonFunction;
import com.topwise.cloudpos.aidl.emv.level2.AidlEmvL2;
import com.topwise.cloudpos.aidl.emv.level2.EmvTerminalInfo;
import com.topwise.cloudpos.struct.BytesUtil;

public class EmvManager {
    private static final String TAG = "EmvManager";
    private static EmvManager instance = new EmvManager();

    private static final int PARAM_ERROR = -1;

    private AidlEmvL2 emv = DeviceTopUsdkServiceManager.getInstance().getEmvL2();
    private EmvTransData emvTransData;
    private OnEmvProcessListener listener;
    private EmvTerminalInfo emvTerminalInfo;
    private volatile EmvProcessThread emvProcessThread = null;
    private DBManager db = DBManager.getInstance();
    private int cardType = CardType.NONE;
    private boolean isProcessEmv = false;

    /**
     * process PBOC thread
     */
    private class EmvProcessThread extends Thread {

        private volatile OnEmvProcessListener emvProcessListener = null;
        private EmvProcessThread() {
        }

        @Override
        public void run() {
            if(cardType == CardType.IC){
                try {
                    ContactCardProcess.getInstance().EmvProcess(emvTransData, listener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if(cardType==CardType.RF){
                int entryLibRes = 0;
                try {
                    entryLibRes = ClsCardProcess.getInstance().processEntryLib(emvTransData, listener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                SDKLog.d(TAG, "processEntryLib res: " + entryLibRes);
            }
        }
    }

    public static EmvManager getInstance() {
        return instance;
    }

    public synchronized void startEmvProcess(EmvTransData emvTransData, OnEmvProcessListener listener){
        if (emvTransData == null || listener == null) {
            SDKLog.d(TAG, "input param is null");
            return;
        }
        SDKLog.d(TAG, emvTransData.toString());
        this.emvTransData = emvTransData;
        this.listener = listener;
        cardType = emvTransData.getCardType();
        if (emvTransData == null) {
            SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "emvTransData == null");
            return;
        }
        if (listener == null) {
            SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "listener == null");
            return;
        }

        if (emvProcessThread != null && emvProcessThread.isAlive()) {
            SDKLog.e(TAG, CommonFunction._FILE_LINE_FUN_() + "pbocThread is alive");
            try {
                abortEMV();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            //return;
        }
        isProcessEmv = true;
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "EmvTransData: " + emvTransData.toString());
        emvProcessThread = new EmvProcessThread();
        emvProcessThread.start();
    }

    public void setEmvTerminalInfo(EmvTerminalInfo info){
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "info="+info.toString());
        emvTerminalInfo = info;
    }

    public EmvTerminalInfo getEmvTerminalInfo() {
        return emvTerminalInfo;
    }

    /**
     * End EMV process
     *
     * @throws RemoteException RemoteException
     */
    public void endEMV() throws RemoteException {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "endPBOC");

        if (cardType == CardType.RF) {
            ClsCardProcess.getInstance().endEmv();
        } else {
            ContactCardProcess.getInstance().endEmv();
        }

        if (emvProcessThread != null) {
            emvProcessThread.interrupt();
        }
    }

    /**
     * abort EMV process, equivalent to the endPBOC method
     *
     * @throws RemoteException RemoteException
     */
    public void abortEMV() throws RemoteException {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "abortPBOC");
        endEMV();
    }

    /**
     * Read the kernel log
     *
     * @param taglist Needed to output the taglist data such as {“9F26”,“5A”}
     * @param buffer  Read data [output param]
     * @return <0 means read failed, >0 means the number of bytes read
     * @throws RemoteException RemoteException
     */
    public synchronized int readKernelData(String[] taglist, byte[] buffer) throws RemoteException {
        if (taglist == null) {
            SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "readKernelData taglist == null");
            return PARAM_ERROR;
        }
        if (buffer == null) {
            SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "readKernelData buffer == null");
            return PARAM_ERROR;
        }
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "readKernelData start");
        int bufferLen = 0;
        int bufferInLen = buffer.length;
        for (String tag : taglist) {
            int tagInt = Integer.parseInt(tag, 16);
            byte[] value = new byte[500];
            int retTlv = 0;
            if (cardType == CardType.IC) {
                value = ContactCardProcess.getInstance().getTlvData(tag);
                if (value != null) {
                    retTlv = value.length;
                }
            } else {
                value = ClsCardProcess.getInstance().getTlvData(tag);
                if (value != null) {
                    retTlv = value.length;
                }
            }
            SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "readKernelData retTlv: " + retTlv + "; tag: " + tag + "; tagInt: " + tagInt);
            if (retTlv > 0) {
                if (bufferLen <= bufferInLen) {
                    System.arraycopy(value, 0, buffer, bufferLen, retTlv);
                    bufferLen += retTlv;
                }
            }
        }
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "readKernelData end bufferLen: " + bufferLen + "; buffer: " + BytesUtil.bytes2HexString(buffer));
        return bufferLen;
    }

    /**
     * Set the TLV data
     *
     * @param tag   tlv data
     * @param value tlv result [output param]
     * @throws RemoteException RemoteException
     */
    public void setTlv(String tag, byte[] value) throws RemoteException {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "setTlv tag: " + tag + "; value: " + BytesUtil.bytes2HexString(value));
        ContactCardProcess.getInstance().setTlvData(tag, value);
        ClsCardProcess.getInstance().setTlvData(tag, value);
    }

    /**
     * Get the TLV data
     *
     * @param tag   tlv data
     * @param value tlv result [output param]
     * @throws RemoteException RemoteException
     */
    public int getTlv(int tag, byte[] value) throws RemoteException {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "tag: " + tag + ";value: " + value);
        int ret = -1;
        if (value == null) {
            return -1;
        }
        if (cardType == CardType.IC) {
            //ret = EmvJNI.getTlv(tag, value);
        } else {
            String tagStr = BytesUtil.bytes2HexString(BytesUtil.int2Bytes(tag, true));
            byte[] valueBuf = ClsCardProcess.getInstance().getTlvData(tagStr);
            if (valueBuf != null) {
                ret = Math.min(value.length, valueBuf.length);
                System.arraycopy(valueBuf, 0, value, 0, ret);
            }
        }
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "getTlv ret: " + ret);
        if (ret > 0) {
            SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "getTlv value: " + BytesUtil.bytes2HexString(value));
        }
        return ret;
    }

    /**
     * import amount
     *
     * @param amt amount
     * @return whether succeed
     * @throws RemoteException RemoteException
     */
    public boolean importAmount(String amt) throws RemoteException {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "importAmount amt: " + amt);
        boolean isImportAmount;
        if (cardType == CardType.RF) {
            isImportAmount = ClsCardProcess.getInstance().importAmount(amt);
        } else {
            isImportAmount = ContactCardProcess.getInstance().importAmount(amt);
        }
        return isImportAmount;
    }

    /**
     * Import the application selection results
     *
     * @param index app index
     * @return whether succeed
     * @throws RemoteException RemoteException
     */
    public boolean importAidSelectRes(int index) throws RemoteException {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "importAidSelectRes");
        boolean isImportAidRes;
        if (cardType == CardType.RF) {
            isImportAidRes = ClsCardProcess.getInstance().importAidSelectRes(index);
        } else {
            isImportAidRes = ContactCardProcess.getInstance().importAidSelectRes(index);
        }
        return isImportAidRes;
    }

    /**
     * import result of select aid res
     *
     * @param res true:confirm success, false: confirm failed
     * @return whether succeed
     * @throws RemoteException
     */
    public boolean importFinalAidSelectRes(boolean res) throws RemoteException {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "importFinalAidSelectRes res: " + res);
        boolean isimportFinalAidSelectRes;
        if (cardType == CardType.RF) {
            isimportFinalAidSelectRes = ClsCardProcess.getInstance().importFinalAidSelectRes(res);
        } else {
            isimportFinalAidSelectRes = ContactCardProcess.getInstance().importFinalAidSelectRes(res);
        }
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "importFinalAidSelectRes: " + isimportFinalAidSelectRes);
        return isimportFinalAidSelectRes;
    }

    /**
     * import result of confirm card info
     *
     * @param res true: confirm success, false: confirm failed
     * @return whether succeed
     * @throws RemoteException RemoteException
     */
    public boolean importConfirmCardInfoRes(boolean res) throws RemoteException  {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "importMsgConfirmRes res: " + res);
        boolean isImportMsgConfirmRes;
        if (cardType == CardType.RF) {
            isImportMsgConfirmRes = ClsCardProcess.getInstance().importConfirmCardInfoRes(res);
        } else {
            isImportMsgConfirmRes = ContactCardProcess.getInstance().importConfirmCardInfoRes(res);
        }
        return isImportMsgConfirmRes;
    }

    /**
     * import pin
     *
     * @param pin pin
     * @return whether succeed
     * @throws RemoteException RemoteException
     */
    public boolean importPin(String pin) throws RemoteException {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "importPin pin: " + pin);
        boolean isImportPin;
        if (cardType == CardType.RF) {
            isImportPin = ClsCardProcess.getInstance().importPin(pin);
        } else {
            isImportPin = ContactCardProcess.getInstance().importPin(pin);
        }
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "isimportPin: " + isImportPin);
        return isImportPin;
    }

    /**
     * import certification result
     *
     * @param res Authentication result, true: authentication successful, false: authentication failed
     * @return whether succeed
     * @throws RemoteException RemoteException
     */
    public boolean importUserAuthRes(boolean res) throws RemoteException {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "importUserAuthRes");
        boolean isImportAuthRes;
        if (cardType == CardType.RF) {
            isImportAuthRes = ClsCardProcess.getInstance().importUserAuthRes(res);
        } else {
            isImportAuthRes = ContactCardProcess.getInstance().importUserAuthRes(res);
        }
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "importUserAuthRes: " + isImportAuthRes);
        return isImportAuthRes;
    }

    /**
     * Import the message of confirm the results
     *
     * @param confirm Confirm the result, true: confirm, false: cancel
     * @return whether succeed
     * @throws RemoteException RemoteException
     */
    public boolean importMsgConfirmRes(boolean confirm) throws RemoteException {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "importMsgConfirmRes confirm: " + confirm);
        boolean isImportMsgConfirmRes;
        if (cardType == CardType.RF) {
            isImportMsgConfirmRes = ClsCardProcess.getInstance().importMsgConfirmRes(confirm);
        } else {
            isImportMsgConfirmRes = ContactCardProcess.getInstance().importMsgConfirmRes(confirm);
        }
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "isImportMsgConfirmRes: " + isImportMsgConfirmRes);
        return isImportMsgConfirmRes;
    }

    /**
     * import electronic cash prompt information confirm result
     *
     * @param confirm Confirm the result, true: confirm, false: cancel
     * @return whether succeed
     * @throws RemoteException RemoteException
     */
    public boolean importECashTipConfirmRes(boolean confirm) throws RemoteException {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "importECashTipConfirmRes confirm: " + confirm);
        boolean isImportECashTipRes;
        if (cardType == CardType.RF) {
            isImportECashTipRes = ClsCardProcess.getInstance().importECashTipConfirmRes(confirm);
        } else {
            isImportECashTipRes = ContactCardProcess.getInstance().importECashTipConfirmRes(confirm);
        }
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "isimportECashTipConfirmRes: " + isImportECashTipRes);
        return isImportECashTipRes;
    }

    /**
     * import online result
     *
     * @param onlineRes whether online success
     * @param respCode  Background response code
     * @param icc55     The 55 field data returned by the card
     * @return whether succeed
     * @throws RemoteException RemoteException
     */
    public boolean importOnlineResp(boolean onlineRes, String respCode, String icc55) throws RemoteException {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "importOnlineResp onlineRes: " + onlineRes + "; respCode: " + respCode + "; icc55: " + icc55);
        boolean isImportOnlineResp;
        if (cardType == CardType.RF) {
            isImportOnlineResp = ClsCardProcess.getInstance().importOnlineResp(onlineRes, respCode, icc55);
        } else {
            isImportOnlineResp = ContactCardProcess.getInstance().importOnlineResp(onlineRes, respCode, icc55);
        }
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "isImportOnlineResp: " + isImportOnlineResp);
        return isImportOnlineResp;
    }
}
