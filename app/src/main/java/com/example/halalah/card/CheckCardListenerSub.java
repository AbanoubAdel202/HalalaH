package com.example.halalah.card;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.PosApplication;
import com.example.halalah.Utils;
//import com.example.halalah.activity.CardConfirmActivity;
import com.example.halalah.cache.ConsumeData;
import com.topwise.cloudpos.aidl.emv.AidlCheckCardListener;
import com.topwise.cloudpos.aidl.emv.AidlPboc;
import com.topwise.cloudpos.aidl.emv.EmvTransData;
import com.topwise.cloudpos.aidl.magcard.TrackData;

import static com.example.halalah.util.CardSearchErrorUtil.CARD_SEARCH_ERROR_REASON_MAG_EMV;
import static com.example.halalah.util.CardSearchErrorUtil.CARD_SEARCH_ERROR_REASON_MAG_READ;

public class CheckCardListenerSub extends AidlCheckCardListener.Stub {
    private static final String TAG = Utils.TAGPUBLIC + CheckCardListenerSub.class.getSimpleName();

    private Context mContext;
    private AidlPboc mPbocManager;
    private EmvTransData mEmvTransData;

    public CheckCardListenerSub(Context context) {
        mPbocManager = DeviceTopUsdkServiceManager.getInstance().getPbocManager();
        mContext = context;
    }

    @Override
    public void onFindMagCard(TrackData data) throws RemoteException {
        Log.i(TAG, "onFindMagCard()");

        String cardNo = data.getCardno();
        String track2 = data.getSecondTrackData();
        String track3 = data.getThirdTrackData();

        Log.d(TAG, "onFindMagCard cardNo : " + cardNo + " track2 : " + track2);
        if (cardNo == null || isTrack2Error(track2)) {
            cancelCheckCard();
            CardManager.getInstance().callBackError(CARD_SEARCH_ERROR_REASON_MAG_READ);
        } else if (isEmvCard(track2)) {
            cancelCheckCard();
            CardManager.getInstance().callBackError(CARD_SEARCH_ERROR_REASON_MAG_EMV);
        } else {
            PosApplication.getApp().oGPosTransaction.m_iCardType=ConsumeData.CARD_TYPE_MAG;
            PosApplication.getApp().oGPosTransaction.m_sPAN=cardNo;
            PosApplication.getApp().oGPosTransaction.m_sCardExpDate=data.getExpiryDate();
            track2 = track2.replace("=", "D");
            PosApplication.getApp().oGPosTransaction.m_sTrack2=track2;
            //todo service code check
            /*if (track3 != null) {
                track3 = track3.replace("=", "D");
                PosApplication.getApp().mConsumeData.setThirdTrackData(track3);
            }*/
          //  CardManager.getInstance().startActivity(mContext, null, CardConfirmActivity.class);

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

        boolean result = mPbocManager.setEmvKernelType(1);
        Log.d(TAG, "setEmvKernelType: " + result);
        EmvTransDataSub emvTransDataSub = new EmvTransDataSub();
        mEmvTransData = emvTransDataSub.getEmvTransData(true);
        mPbocManager.processPBOC(mEmvTransData, new ICPbocStartListenerSub(mContext));
    }

    @Override
    public void onFindRFCard() throws RemoteException {
        Log.i(TAG, "onFindRFCard()");

        PosApplication.getApp().oGPosTransaction.m_iCardType=ConsumeData.CARD_TYPE_RF;
        boolean result =  mPbocManager.setEmvKernelType(2);
        Log.d(TAG, "setEmvKernelType: " + result);
        EmvTransDataSub emvTransDataSub = new EmvTransDataSub();
        mEmvTransData = emvTransDataSub.getEmvTransData(false);
        mPbocManager.processPBOC(mEmvTransData, new RFPbocStartListenerSub(mContext));
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
            mPbocManager.cancelCheckCard();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}