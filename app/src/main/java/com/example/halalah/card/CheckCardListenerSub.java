package com.example.halalah.card;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.CardCallback;
import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.POSTransaction;
import com.example.halalah.POS_MAIN;
import com.example.halalah.PosApplication;
import com.example.halalah.Utils;
//import com.example.halalah.activity.CardConfirmActivity;
import com.example.halalah.ui.PinpadActivity;
import com.example.halalah.emv.EmvManager;
import com.example.halalah.iso8583.BCDASCII;
import com.topwise.cloudpos.aidl.emv.AidlCheckCardListener;
import com.topwise.cloudpos.aidl.emv.level2.AidlEmvL2;
import com.topwise.cloudpos.aidl.magcard.TrackData;

import static com.example.halalah.util.CardSearchErrorUtil.CARD_SEARCH_ERROR_REASON_MAG_EMV;
import static com.example.halalah.util.CardSearchErrorUtil.CARD_SEARCH_ERROR_REASON_MAG_READ;

public class CheckCardListenerSub extends AidlCheckCardListener.Stub {
    private static final String TAG = Utils.TAGPUBLIC + CheckCardListenerSub.class.getSimpleName();

    private Context mContext;
    private EmvManager mEmvManager;
    private EmvTransData mEmvTransData;
    private AidlEmvL2 emv = DeviceTopUsdkServiceManager.getInstance().getEmvL2();
    private CardCallback mCardCallBack;

    public CheckCardListenerSub(Context context) {
        mEmvManager = EmvManager.getInstance();
        mContext = context;
    }

    @Override
    public void onFindMagCard(TrackData data) throws RemoteException {
        Log.i(TAG, "onFindMagCard()");
        if (mCardCallBack != null){
            mCardCallBack.onFindMagCard(data);
        }
        String scardNo = data.getCardno();
        String strack2 = data.getSecondTrackData();
       // String track3 = data.getThirdTrackData();

        Log.d(TAG, "onFindMagCard cardNo : " + scardNo + " track2 : " + strack2);
        if (scardNo == null || isTrack2Error(strack2)) {
            cancelCheckCard();
            CardManager.getInstance().callBackError(CARD_SEARCH_ERROR_REASON_MAG_READ);
        } else if (isEmvCard(strack2)) {
            cancelCheckCard();
            CardManager.getInstance().callBackError(CARD_SEARCH_ERROR_REASON_MAG_EMV);
        } else {
            PosApplication.getApp().oGPosTransaction.m_enmTrxCardType = POSTransaction.CardType.MAG;

            PosApplication.getApp().oGPosTransaction.m_sPAN = scardNo;
            PosApplication.getApp().oGPosTransaction.m_sCardExpDate = data.getExpiryDate();



            /*if (track3 != null) {
                track3 = track3.replace("=", "D");
                PosApplication.getApp().mConsumeData.setThirdTrackData(track3);
            }*/
            //CardManager.getInstance().startActivity(mContext, null, CardConfirmActivity.class);
            POS_MAIN.Recognise_card();
            if (!POS_MAIN.Check_transaction_allowed(PosApplication.getApp().oGPosTransaction.m_enmTrxType)) {
                //todo transaction not allowed

            }
            else {

                if (POS_MAIN.Check_transaction_limits(PosApplication.getApp().oGPosTransaction.m_enmTrxType) == 0) {
                    //todo alert dialog for limit exeeded
                }
                POS_MAIN.supervisor_pass_required();
                CardManager.getInstance().setConfirmCardInfo(true);
                Intent intent = new Intent(mContext, PinpadActivity.class);
                if (PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCheck_Service_Code == "1")  //if service code check is enabled so it's higher priority than TMS cardholder Authentication
                {
                    POS_MAIN.Check_Servicecode(strack2);
                } else {
                    switch (POS_MAIN.Check_CVM(PosApplication.getApp().oGPosTransaction.m_enmTrxType)) {
                        case 0: //none
                            PosApplication.getApp().oGPosTransaction.m_enmTrxCVM = POSTransaction.CVM.NO_CVM;
                        case 1: //signature
                            PosApplication.getApp().oGPosTransaction.m_enmTrxCVM = POSTransaction.CVM.SIGNATURE;
                            break;
                        case 2: //pin
                            PosApplication.getApp().oGPosTransaction.m_enmTrxCVM = POSTransaction.CVM.ONLINE_PIN;
                            mContext.startActivity(intent);
                            break;
                        case 3: //both pin and signature
                            PosApplication.getApp().oGPosTransaction.m_enmTrxCVM = POSTransaction.CVM.ONLINE_PIN_SIGNATURE;

                            mContext.startActivity(intent);
                            break;
                    }
                }

                if (strack2.contains("="))
                    PosApplication.getApp().oGPosTransaction.m_sTrack2 = strack2.replace("=", "D");
            }
        }
    }

    @Override
    public void onSwipeCardFail() throws RemoteException {
        Log.i(TAG, "onSwipeCardFail()");
        cancelCheckCard();
        CardManager.getInstance().callBackError(CARD_SEARCH_ERROR_REASON_MAG_READ);
    }

    @Override
    public void onFindICCard() throws RemoteException {
        Log.i(TAG, "onFindICCard()");

//        boolean result = mEmvManager.setEmvKernelType(1);
//        Log.d(TAG, "setEmvKernelType: " + result);
        EmvTransDataSub emvTransDataSub = new EmvTransDataSub();
        mEmvTransData = emvTransDataSub.getEmvTransData(true);
        PosApplication.getApp().oGPosTransaction.m_enmTrxCardType= POSTransaction.CardType.ICC;
        mEmvManager.startEmvProcess(mEmvTransData, new ICPbocStartListenerSub(mContext));
    }

    @Override
    public void onFindRFCard() throws RemoteException {
        Log.i(TAG, "onFindRFCard()");


//        boolean result =  mPbocManager.setEmvKernelType(2);
//        Log.d(TAG, "setEmvKernelType: " + result);
        EmvTransDataSub emvTransDataSub = new EmvTransDataSub();
        mEmvTransData = emvTransDataSub.getEmvTransData(false);
        PosApplication.getApp().oGPosTransaction.m_enmTrxCardType= POSTransaction.CardType.CTLS;
        mEmvManager.startEmvProcess(mEmvTransData, new RFPbocStartListenerSub(mContext));
    }

    @Override
    public void onError(int errorCode) throws RemoteException {
        Log.i(TAG, "onError(), errorCode: " + errorCode);
        cancelCheckCard();
        CardManager.getInstance().callBackError(errorCode);
    }

    @Override
    public void onTimeout() throws RemoteException {
        Log.i(TAG, "onTimeout()");
        CardManager.getInstance().callBackTimeOut();

    }

    @Override
    public void onCanceled() throws RemoteException {
        Log.i(TAG, "onCanceled()");
        CardManager.getInstance().callBackCanceled();
    }

    private boolean isTrack2Error(String track2) {
        Log.i(TAG, "isTrack2Error = " + track2);
        //Log.i(TAG, "isTrack2Error length = " + track2.length());
        if (track2 == null ||
                track2.length() < 21 ||
                track2.length() > 37 ||
                track2.indexOf("=") < 12) {
            return true;
        }

        return false;
    }

    private boolean isEmvCard(String track2) {
        Log.i(TAG, "isEmvCard: track2: " + track2);
        if ((track2 != null) && (track2.length() > 0)) {
            int index = track2.indexOf("=");
            String subTrack2 = track2.substring(index);

            if (subTrack2.charAt(5) == '2' || subTrack2.charAt(5) == '6') {
                Log.i(TAG, "isEmvCard: true");
                return true;
            }
        }
        Log.i(TAG, "isEmvCard: false");
        return false;
    }

    private void cancelCheckCard() {
        Log.i(TAG, "cancelCheckCard()");
        try {
            emv.cancelCheckCard();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}