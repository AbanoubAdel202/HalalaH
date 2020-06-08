package com.example.halalah.card;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.POS_MAIN;
import com.example.halalah.PosApplication;
import com.example.halalah.Utils;

import com.example.halalah.ui.CardConfirmActivity;
import com.example.halalah.ui.PacketProcessActivity;
import com.example.halalah.ui.PinpadActivity;
import com.example.halalah.cache.ConsumeData;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.util.PacketProcessUtils;
import com.topwise.cloudpos.aidl.emv.AidlPboc;
import com.topwise.cloudpos.aidl.emv.AidlPbocStartListener;
import com.topwise.cloudpos.aidl.emv.CardInfo;
import com.topwise.cloudpos.aidl.emv.PCardLoadLog;
import com.topwise.cloudpos.aidl.emv.PCardTransLog;

public class ICPbocStartListenerSub extends AidlPbocStartListener.Stub {
    private static final String TAG = Utils.TAGPUBLIC + ICPbocStartListenerSub.class.getSimpleName();

    private Context mContext;
    private AidlPboc mPbocManager;
    private boolean isOnline = false;

    private boolean isGetPin = false;

    public ICPbocStartListenerSub(Context context) {
        mContext = context;
        mPbocManager = DeviceTopUsdkServiceManager.getInstance().getPbocManager();
        CardManager.getInstance().initCardResultCallBack(callBack);
    }

    @Override
    public void finalAidSelect() throws RemoteException {

        mPbocManager.setTlv("9F1A", BCDASCII.hexStringToBytes("0682"));
        mPbocManager.setTlv("5F2A", BCDASCII.hexStringToBytes("0682"));
        mPbocManager.setTlv("9f3c", BCDASCII.hexStringToBytes("0682"));

        //todo AID supported or not

        byte[] bAIDs;
        String[] AIDs = new String[]{"9F06"};
        bAIDs= getTlv(AIDs);

        POS_MAIN.Recognise_card();
        POS_MAIN.Check_transaction_allowed(PosApplication.getApp().oGPosTransaction.m_enmTrxType);
        POS_MAIN.Check_transaction_limits();



        mPbocManager.importFinalAidSelectRes(true);
    }

    /**
     * 请求输入金额 ，简易流程时不回调此方法
     */
    @Override
    public void requestImportAmount(int type) throws RemoteException {
        Log.d(TAG, "requestImportAmount(), type: " + type);

        boolean isSuccess = mPbocManager.importAmount(PosApplication.getApp().oGPosTransaction.m_sTrxAmount);
        Log.d(TAG, "isSuccess() : " + isSuccess);
    }

    /**
     * 请求提示信息
     */
    @Override
    public void requestTipsConfirm(String msg) throws RemoteException {
        Log.d(TAG, "requestTipsConfirm(), msg: " + msg);
    }

    /**
     * Request multiple application choices
     */
    @Override
    public void requestAidSelect(int times, String[] aids) throws RemoteException {
        Log.d(TAG, "requestAidSelect(), times: " + times + ", aids.length = " + aids.length);
     int iAID_Index=0;

       //auto selection for mada
        if(PosApplication.getApp().oGTerminal_Operation_Data.Mada_Auto_Selection==1)
        {
            for(int index=0 ;index<aids.length;index++)
            {
                if (aids[index].contains("mada"))
                    iAID_Index=index+1;
            }

        }
        else
        {
               // show selection
                //todo AID ACtivity selection and return AID selected index or it's value
                iAID_Index=1;
        }

        boolean isSuccess = mPbocManager.importAidSelectRes(iAID_Index);
        Log.d(TAG, "isSuccess() : " + isSuccess);
    }

    /**
     * Request confirmation to use e-cash
     */
    @Override
    public void requestEcashTipsConfirm() throws RemoteException {
        Log.d(TAG, "requestEcashTipsConfirm()");

        boolean isSuccess = mPbocManager.importECashTipConfirmRes(false);
        Log.d(TAG, "isSuccess() : " + isSuccess);
    }

    /**
     *
     * Request confirmation card information
     */
    @Override
    public void onConfirmCardInfo(CardInfo cardInfo) throws RemoteException {
        String cardno = cardInfo.getCardno();
        Log.d(TAG, "onConfirmCardInfo(), cardno: " + cardno);

       // isEcCard();




        PosApplication.getApp().oGPosTransaction.m_iCardType=ConsumeData.CARD_TYPE_IC;
        PosApplication.getApp().oGPosTransaction.m_sPAN=cardno;
        //CardManager.getInstance().startActivity(mContext, null, CardConfirmActivity.class);
        CardManager.getInstance().setConfirmCardInfo(true);
    }

    /**
     * 请求导入PIN
     */
    @Override
    public void requestImportPin(int type, boolean lasttimeFlag, String amt) throws RemoteException {
        Log.d(TAG, "requestImportPin(), type: " + type + "; lasttimeFlag: " + lasttimeFlag + "; amt: " + amt);
        isGetPin = true;
        Bundle param = new Bundle();
        param.putInt("type", type);
        CardManager.getInstance().startActivity(mContext, param, PinpadActivity.class);
    }

    /**
     * 请求身份认证
     */
    @Override
    public void requestUserAuth(int certype, String certnumber) throws RemoteException {
        Log.d(TAG, "requestUserAuth(), certype: " + certype + "; certnumber: " + certnumber);

        boolean isSuccess = mPbocManager.importUserAuthRes(true);
        Log.d(TAG, "isSuccess() : " + isSuccess);
    }

    /**
     * 请求联机
     */
    @Override
    public void onRequestOnline() throws RemoteException {
        Log.d(TAG, "onRequestOnline()");

        setExpired();
        setSeqNum();
        setTrack2();
        setDE55();


        isOnline = true;
        if (!isGetPin) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("online", true);
            CardManager.getInstance().startActivity(mContext, bundle, PinpadActivity.class);
        } else {
            //socket通信
            Bundle bundle = new Bundle();
            bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE);
            CardManager.getInstance().startActivity(mContext, bundle, PacketProcessActivity.class);
        /*byte[] sendData = PosApplication.getApp().mConsumeData.getICData();
        Log.d(TAG, BCDASCII.bytesToHexString(sendData));
        JsonAndHttpsUtils.sendJsonData(mContext, BCDASCII.bytesToHexString(sendData));*/
        }
    }

    /**
     * 返回读取卡片脱机余额结果
     */
    @Override
    public void onReadCardOffLineBalance(String moneyCode, String balance, String secondMoneyCode, String secondBalance) throws RemoteException {
        Log.d(TAG, "onReadCardOffLineBalance(), moneyCode: " + moneyCode + "; balance"
                + "; secondMoneyCode: " + secondMoneyCode + "; secondBalance: " + secondBalance);
    }

    /**
     * 返回读取卡片交易日志结果
     */
    @Override
    public void onReadCardTransLog(PCardTransLog[] log) throws RemoteException {
        Log.d(TAG, "onReadCardTransLog()");
        if (log == null) {
            return;
        }
        Log.d(TAG, "onReadCardTransLog log.length: " + log.length);
    }

    /**
     * 返回读取卡片圈存日志结果
     */
    @Override
    public void onReadCardLoadLog(String atc, String checkCode, PCardLoadLog[] logs) throws RemoteException {
        Log.d(TAG, "onReadCardLoadLog(), atc: " + atc + "; checkCode: " + checkCode + "logs.length: " + logs.length);
        if (logs == null) {
            return;
        }
    }

    /**
     * 交易结果
     * 批准: 0x01
     * 拒绝: 0x02
     * 终止: 0x03
     * FALLBACK: 0x04
     * Use other interface: 0x05
     * 其他：0x06
     * EMV simple process does not call back this method
     */
    @Override
    public void onTransResult(int result) throws RemoteException {
        Log.d(TAG, "onTransResult result: " + result + isOnline);
        if (!isOnline) {
            CardManager.getInstance().callBackTransResult(result);
        }
    }

    @Override
    public void onError(int errorCode) throws RemoteException {
        Log.d(TAG, "onError errorCode: " + errorCode);
        CardManager.getInstance().callBackError(errorCode);
    }

    CardManager.CardResultCallBack callBack = new CardManager.CardResultCallBack() {
        @Override
        public void consumeAmount(String amount) {
            Log.d(TAG, "consumeAmount amount : " + amount);
            if (null != mPbocManager) {
                try {
                    mPbocManager.importAmount(amount);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void aidSelect(int index) {
            Log.d(TAG, "aidSelect index : " + index);
            if (null != mPbocManager) {
                try {
                    mPbocManager.importAidSelectRes(index);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void eCashTipsConfirm(boolean confirm) {
            Log.d(TAG, "eCashTipsConfirm confirm : " + confirm);
            if (null != mPbocManager) {
                try {
                    mPbocManager.importECashTipConfirmRes(confirm);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void confirmCardInfo(boolean confirm) {
            Log.d(TAG, "confirmCardInfo confirm : " + confirm);
            if (null != mPbocManager) {
                try {
                    mPbocManager.importConfirmCardInfoRes(confirm);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void importPin(String pin) {
            Log.d(TAG, "importPin pin : " + pin);
            if (null != mPbocManager) {
                try {
                    mPbocManager.importPin(pin);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void userAuth(boolean auth) {
            Log.d(TAG, "userAuth auth : " + auth);
            if (null != mPbocManager) {
                try {
                    mPbocManager.importUserAuthRes(auth);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void requestOnline(boolean online, String respCode, String icc55) {
            Log.d(TAG, "requestOnline online : " + online + " respCode : " + respCode + " icc55 : " + icc55);
            if (null != mPbocManager) {
                try {
                    mPbocManager.importOnlineResp(online, respCode, icc55);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private boolean isEcCard() {
        Log.i(TAG, "isEcCard()");
        String ecCard = "A000000333010106";

        String[] aidTag = new String[]{"84"};
        String cardAid = BCDASCII.bytesToHexString(getTlv(aidTag));

        Log.i(TAG, "cardAid: " + cardAid);
        return cardAid.contains(ecCard);
    }

    private void setExpired() {
        Log.i(TAG, "getExpired()");
        String[] dataTag = new String[]{"5F24"};
        byte[] dataTlvList = getTlv(dataTag);
        String expired = null;

        if (dataTlvList != null) {
            expired = BCDASCII.bytesToHexString(dataTlvList);
            expired = expired.substring(expired.length() - 6, expired.length() - 2);
        }
        Log.d(TAG, "setExpired : " + expired);
        PosApplication.getApp().oGPosTransaction.m_sCardExpDate=expired;
    }

    private void setSeqNum() {
        Log.i(TAG, "getSeqNum()");
        String[] seqNumTag = new String[]{"5F34"};
        byte[] seqNumTlvList = getTlv(seqNumTag);
        String cardSeqNum = null;

        if (seqNumTlvList != null) {
            cardSeqNum = BCDASCII.bytesToHexString(seqNumTlvList);
            cardSeqNum = cardSeqNum.substring(cardSeqNum.length() - 2, cardSeqNum.length());
        }
        Log.d(TAG, "setSeqNum : " + cardSeqNum);
        PosApplication.getApp().oGPosTransaction.m_sCardSeqNum=cardSeqNum;
    }

    private void setTrack2() {
        Log.i(TAG, "getTrack2()");
        String[] track2Tag = new String[]{"57"};
        byte[] track2TlvList = getTlv(track2Tag);

        byte[] temp = new byte[track2TlvList.length - 2];
        System.arraycopy(track2TlvList, 2, temp, 0, temp.length);
        String track2 = processTrack2(BCDASCII.bytesToHexString(temp));
        PosApplication.getApp().oGPosTransaction.m_sTrack2=track2;
    }

    private static String processTrack2(String track) {
        Log.i(TAG, "processTrack2()");
        StringBuilder builder = new StringBuilder();
        String subStr = null;
        String resultStr = null;
        for (int i = 0; i < track.length(); i++) {
            subStr = track.substring(i, i + 1);
            if (!subStr.endsWith("F")) {
                /*if(subStr.endsWith("D")) {
                    builder.append("=");
                } else {*/
                builder.append(subStr);
                /*}*/
            }
        }
        resultStr = builder.toString();
        return resultStr;
    }

    private void setDE55() {
        Log.i(TAG, "getDE55()");
        /*String[] consume55Tag = new String[]{"9F26", "9F27", "9F10", "9F37", "9F36", "95", "9A", "9C", "9F02", "5F2A",
                "82", "9F1A", "9F03", "9F33", "9F34", "9F35", "9F1E", "84", "9F09",
                "91", "71", "72", "DF32", "DF33", "DF34"};*/
        String[] DE55Tag = new String[]{"4F", "82", "95", "9A", "9B", "9C", "5F24",
                "5F2A", "9F02", "9F03", "9F06", "9F10", "9F12", "9F1A", "9F1C", "9F26",
                "9F27", "9F33", "9F34", "9F36", "9F37", "C2", "CD", "CE", "C0", "C4",
                "C7", "C8"};
        byte[] DE55TlvList = getTlv(DE55Tag);
        Log.d(TAG, "setDE55 DE55TlvList : " + BCDASCII.bytesToHexString(DE55TlvList));
        PosApplication.getApp().oGPosTransaction.m_sICCRelatedTags=BCDASCII.bytesToHexString(DE55TlvList);
    }



    private byte[] getTlv(String[] tags) {
        byte[] tempList = new byte[500];
        byte[] tlvList = null;
        try {
            for (String tag : tags) {
                String[] tempStr = {tag};
                byte[] tempByte = new byte[500];
                int len = mPbocManager.readKernelData(tempStr, tempByte);
                Log.d(TAG, "temp: " + BCDASCII.bytesToHexString(tempByte, len));
            }

            int result = mPbocManager.readKernelData(tags, tempList);

            if (result < 0) {
                return null;
            } else {
                tlvList = new byte[result];
                System.arraycopy(tempList, 0, tlvList, 0, result);
                Log.i(TAG, "tlvList: " + BCDASCII.bytesToHexString(tlvList));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return tlvList;
    }
}