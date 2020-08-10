package com.example.halalah.emv;

import android.os.Bundle;
import android.os.RemoteException;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.database.table.Aid;
import com.example.halalah.database.table.Capk;
import com.example.halalah.database.table.DBManager;
import com.example.halalah.qrcode.utils.SDKLog;
import com.topwise.cloudpos.aidl.emv.level2.AidlEntry;
import com.topwise.cloudpos.aidl.emv.level2.EmvCapk;
import com.topwise.cloudpos.struct.BytesUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 交易的基础进程
 *
 * @author xukun
 * @version 1.0.0
 * @date 19-9-17
 */

abstract class BasePayProcess {

    private static final String TAG = BasePayProcess.class.getSimpleName();
    private AidlEntry entryL2 = DeviceTopUsdkServiceManager.getInstance().getL2Entry();
    private DBManager db = DBManager.getInstance();

    /**
     * 开始交易
     *
     * @param param    需要交易的数据
     * @param listener 交易监听
     */
    abstract void startPay(Bundle param, final TransResultListener listener);

    /**
     * 脚本处理
     *
     * @param onlineRes 是否联机
     * @param respCode  联机结果
     * @param icc55     脚本信息
     * @param listener  交易监听
     * @return 脚本处理结果
     */
    abstract boolean scriptProcess(boolean onlineRes, String respCode, String icc55, final TransResultListener listener) throws RemoteException;

    /**
     * 处理每一个库函数的返回值
     *
     * @param ret      库函数返回值
     * @param listener 监听处理
     * @return 是否成功
     */
    boolean handleResult(int ret, TransResultListener listener) {
        try {
            if (ret == PayDataUtil.EMV_OK) {
                return true;
            } else {
                if (ret == PayDataUtil.CLSS_RESELECT_APP) {
                    ret = entryL2.delCandListCurApp();
                    SDKLog.d(TAG, "delCandListCurApp ret: " + ret);
                    if (ret == PayDataUtil.EMV_OK) {
                        listener.finalSelectAgain();
                    } else {
                        listener.onProcessResult(true, true, PayDataUtil.CardCode.TRANS_USE_OTHER_INTERFACE);
                    }
                } else if (ret == PayDataUtil.CLSS_USE_CONTACT) {
                    listener.onProcessResult(true, true, PayDataUtil.CardCode.TRANS_USE_OTHER_INTERFACE);
                } else if (ret == PayDataUtil.CLSS_REFER_CONSUMER_DEVICE) {
                    listener.onProcessResult(false, true, PayDataUtil.CardCode.CDCVM_SECOND_READ_CARD);
                } else {
                    listener.onFail(ret);
                }
            }
        }catch(RemoteException e){
            e.printStackTrace();
            listener.onProcessResult(true, true, PayDataUtil.CardCode.TRANS_USE_OTHER_INTERFACE);
        }
        return false;
    }

    /**
     * 查询对应capk
     *
     * @param rid
     * @param index   索引
     * @return capk
     */
    EmvCapk getCurrentCapk(byte[] rid, byte[] index) {
        SDKLog.d(TAG, "getCurrentCapk rid: " + BytesUtil.bytes2HexString(rid));
        SDKLog.d(TAG, "getCurrentCapk index: " + BytesUtil.bytes2HexString(index));

        Capk capk = db.getCapkDao().findByRidIndex(BytesUtil.bytes2HexString(rid),index[0]);

        if (capk != null) {
            SDKLog.d(TAG, "capk: " + capk.toString());
            EmvCapk emvCapk = new EmvCapk();
            emvCapk.setRID(rid);
            emvCapk.setKeyID(index[0]);
            byte[] orgData = capk.getExpDate();
            if (orgData != null && orgData.length>0) {
                byte[] date = new byte[3];
                if (orgData.length > 3) {
                    System.arraycopy(orgData, orgData.length - 3, date, 0, 3);
                } else {
                    System.arraycopy(orgData, 0, date, 0, orgData.length);
                }
                emvCapk.setExpDate(date);
            }
            emvCapk.setHashInd(capk.getHashInd());
            emvCapk.setArithInd(capk.getArithInd());
            emvCapk.setCheckSum(capk.getCheckSum());
            orgData = capk.getModul();
            if (orgData != null) {
                emvCapk.setModul(orgData);
            }
            orgData = capk.getExponent();
            if (orgData != null) {
                emvCapk.setExponent(orgData);
            }
            SDKLog.d(TAG, "return emvCapk: " + emvCapk.toString());
            return emvCapk;
        }
        return null;
    }

    /**
     * 获取当前AID参数
     *
     * @param aidHex 9F26TAG的16进制字符串值
     * @return AID参数
     */
    byte[] getCurrentAidData(String aidHex) {
        SDKLog.d(TAG, "getCurrentAidData aidHex: " + aidHex);
        Aid aid = db.getAidDao().findByAid(aidHex);
        SDKLog.d(TAG, "aid:" + aid.toString());
        byte[] aidData = null;
        if (aid != null) {
            aidData = aid.getTlvList().getBytes();
        }
        return aidData;
    }
}