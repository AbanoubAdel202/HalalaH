package com.example.halalah.card;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.POSTransaction;
import com.example.halalah.POS_MAIN;
import com.example.halalah.PosApplication;
import com.example.halalah.Utils;

import com.example.halalah.ui.PacketProcessActivity;
import com.example.halalah.ui.PinpadActivity;
import com.example.halalah.emv.EmvManager;
import com.example.halalah.emv.OnEmvProcessListener;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.util.PacketProcessUtils;

import com.topwise.cloudpos.aidl.emv.CardInfo;
import com.topwise.cloudpos.aidl.emv.PCardLoadLog;
import com.topwise.cloudpos.aidl.emv.PCardTransLog;

public class ICPbocStartListenerSub implements OnEmvProcessListener {
    private static final String TAG = Utils.TAGPUBLIC + ICPbocStartListenerSub.class.getSimpleName();

    private Context mContext;
    private EmvManager emvManager;
    private boolean isOnline = false;

    private boolean isGetPin = false;

    public ICPbocStartListenerSub(Context context) {
        mContext = context;
        emvManager = EmvManager.getInstance();
        CardManager.getInstance().initCardResultCallBack(callBack);
    }

    @Override
    public void finalAidSelect() throws RemoteException {

        emvManager.setTlv("9F1A", BCDASCII.hexStringToBytes("0682"));
	emvManager.setTlv("5F2A", BCDASCII.hexStringToBytes("0682"));
        emvManager.setTlv("9f3c", BCDASCII.hexStringToBytes("0682"));

        //todo AID supported or not

        byte[] bAIDs;
        String[] AIDs = new String[]{"9F06"};
        bAIDs= getTlv(AIDs);
        PosApplication.getApp().oGPosTransaction.m_sAID=BCDASCII.bytesToHexString(bAIDs);
        //todo success validation
        if (POS_MAIN.Recognise_card()!=0)
            //todo do activity error CArd not recognised

            if(!POS_MAIN.Check_transaction_allowed(PosApplication.getApp().oGPosTransaction.m_enmTrxType)) {
                //todo do transaction not allowed Activity
                return;
            }

                if(POS_MAIN.Check_transaction_limits(PosApplication.getApp().oGPosTransaction.m_enmTrxType)==0)
                {
                    //todo alert dialog for limit exeeded
                }
        POS_MAIN.supervisor_pass_required();


        emvManager.importFinalAidSelectRes(true);
    }

    /**
     * 请求输入金额 ，简易流程时不回调此方法
     */
    @Override
    public void requestImportAmount(int type) throws RemoteException {
        Log.d(TAG, "requestImportAmount(), type: " + type);

        boolean isSuccess = emvManager.importAmount(PosApplication.getApp().oGPosTransaction.m_sTrxAmount);
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

        boolean isSuccess = emvManager.importAidSelectRes(iAID_Index);
        Log.d(TAG, "isSuccess() : " + isSuccess);
    }

    /**
     * Request confirmation to use e-cash
     */
    @Override
    public void requestEcashTipsConfirm() throws RemoteException {
        Log.d(TAG, "requestEcashTipsConfirm()");

        boolean isSuccess = emvManager.importECashTipConfirmRes(false);
        Log.d(TAG, "isSuccess() : " + isSuccess);
    }

    /**
     *
     * Request confirmation card information
     */
    @Override
    public void onConfirmCardInfo(String cardNo) throws RemoteException {
        Log.d(TAG, "onConfirmCardInfo(), cardNo: " + cardNo);

       // isEcCard();





        PosApplication.getApp().oGPosTransaction.m_sPAN=cardNo;
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

        boolean isSuccess = emvManager.importUserAuthRes(true);
        Log.d(TAG, "isSuccess() : " + isSuccess);
    }

    /**
     * 请求联机
     */
    @Override
    public void onRequestOnline() throws RemoteException {
        Log.d(TAG, "onRequestOnline()");

        // getting CVM Results
        byte[] bCVMR;
        String[] sCVMR_Tag = new String[]{"9F34"};
        bCVMR= getTlv(sCVMR_Tag);
        switch(bCVMR[3]) {
            case 0x01:
            case 0x41:
                PosApplication.getApp().oGPosTransaction.m_enmTrxCVM = POSTransaction.CVM.OFFLINE_PIN;
                break;
            case 0x02:
            case 0x42:
                PosApplication.getApp().oGPosTransaction.m_enmTrxCVM = POSTransaction.CVM.ONLINE_PIN;
                break;
            case 0x03:
            case 0x43:
            case 0x05:
            case 0x45:
                PosApplication.getApp().oGPosTransaction.m_enmTrxCVM = POSTransaction.CVM.OFFLINE_PIN_SIGNATURE;
                break;
            case 0x1E:
            case 0x5E:
                PosApplication.getApp().oGPosTransaction.m_enmTrxCVM = POSTransaction.CVM.SIGNATURE;
                break;
            case 0x1F:
            case 0x5F:
                PosApplication.getApp().oGPosTransaction.m_enmTrxCVM = POSTransaction.CVM.NO_CVM;
                break;




        }


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
          //todo will be enhanced communication
            Bundle bundle = new Bundle();
            switch(PosApplication.getApp().oGPosTransaction.m_enmTrxType) {
                case PURCHASE:
                    bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE);
                CardManager.getInstance().startActivity(mContext, bundle, PacketProcessActivity.class);
                break;
                case REFUND:
                    bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_REFUND);
                    CardManager.getInstance().startActivity(mContext, bundle, PacketProcessActivity.class);
                case AUTHORISATION:
                    bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_AUTHORISATION);
                    CardManager.getInstance().startActivity(mContext, bundle, PacketProcessActivity.class);

                case AUTHORISATION_VOID:
                    bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_AUTHORISATION_VOID);
                    CardManager.getInstance().startActivity(mContext, bundle, PacketProcessActivity.class);
                case AUTHORISATION_ADVICE:
                    bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_AUTHORISATION_ADVICE);
                    CardManager.getInstance().startActivity(mContext, bundle, PacketProcessActivity.class);

                case CASH_ADVANCE:
                    bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_CASH_ADVANCE);
                    CardManager.getInstance().startActivity(mContext, bundle, PacketProcessActivity.class);
                case SADAD_BILL:
                    bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_SADAD_BILL);
                    CardManager.getInstance().startActivity(mContext, bundle, PacketProcessActivity.class);
                case AUTHORISATION_EXTENSION:
                    bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_AUTHORISATION_EXTENSION);
                    CardManager.getInstance().startActivity(mContext, bundle, PacketProcessActivity.class);

                case PURCHASE_WITH_NAQD:
                    bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE_WITH_NAQD);
                    CardManager.getInstance().startActivity(mContext, bundle, PacketProcessActivity.class);
                case PURCHASE_ADVICE:
                    bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE_ADVICE);
                    CardManager.getInstance().startActivity(mContext, bundle, PacketProcessActivity.class);
            }

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
            if (null != emvManager) {
                try {
                    emvManager.importAmount(amount);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void aidSelect(int index) {
            Log.d(TAG, "aidSelect index : " + index);
            if (null != emvManager) {
                try {
                    emvManager.importAidSelectRes(index);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void eCashTipsConfirm(boolean confirm) {
            Log.d(TAG, "eCashTipsConfirm confirm : " + confirm);
            if (null != emvManager) {
                try {
                    emvManager.importECashTipConfirmRes(confirm);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void confirmCardInfo(boolean confirm) {
            Log.d(TAG, "confirmCardInfo confirm : " + confirm);
            if (null != emvManager) {
                try {
                    emvManager.importConfirmCardInfoRes(confirm);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void importPin(String pin) {
            Log.d(TAG, "importPin pin : " + pin);
            if (null != emvManager) {
                try {
                    emvManager.importPin(pin);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void userAuth(boolean auth) {
            Log.d(TAG, "userAuth auth : " + auth);
            if (null != emvManager) {
                try {
                    emvManager.importUserAuthRes(auth);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void requestOnline(boolean online, String respCode, String icc55) {
            Log.d(TAG, "requestOnline online : " + online + " respCode : " + respCode + " icc55 : " + icc55);
            if (null != emvManager) {
                try {
                    emvManager.importOnlineResp(online, respCode, icc55);
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
            cardSeqNum = cardSeqNum.substring(4);
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

        String[] DE55Tag = new String[]{"82","9F02","9F03","4F","50","9F12","9F36","9F6C","9F26","9F27","9F34",
                                        "84","9F6E","9F10","9F1E","5A","9F24","57","9F33","9F66","9F35","95",
                                        "9F1A","5F2A","9A","9C","9F37","9F19","9F25"};
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
                int len = emvManager.readKernelData(tempStr, tempByte);
                Log.d(TAG, "temp: " + BCDASCII.bytesToHexString(tempByte, len));
            }

            int result = emvManager.readKernelData(tags, tempList);

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