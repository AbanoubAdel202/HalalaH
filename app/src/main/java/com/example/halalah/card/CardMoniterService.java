package com.example.halalah.card;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.Utils;
import com.example.halalah.emv.EmvManager;
import com.topwise.cloudpos.aidl.emv.level2.AidlEmvL2;

public class CardMoniterService extends Service {
    private final String TAG = Utils.TAGPUBLIC + CardMoniterService.class.getSimpleName();
    private static final int SEARCH_CARD_TIME = 30000;

    private AidlEmvL2 emv;
    private EmvManager emvManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
       /* emv = DeviceTopUsdkServiceManager.getInstance().getEmvL2();
        emvManager = EmvManager.getInstance();*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        emv = DeviceTopUsdkServiceManager.getInstance().getEmvL2();
        emvManager = EmvManager.getInstance();

        try {
            if(emv != null){
                emvManager.endEMV();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        checkCard(true, true, true);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    private void checkCard(boolean isMag, boolean isIc, boolean isRf) {
        Log.i(TAG, "searchCard()");
        synchronized (this) {
            try {
                if(emv != null){
                    emv.checkCard(isMag, isIc, isRf, SEARCH_CARD_TIME, new CheckCardListenerSub(this));
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void cancelCheckCard() {
        Log.i(TAG, "cancelCheckCard()");
        synchronized (this) {
            try {
                if(emv != null){
                    emv.cancelCheckCard();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        cancelCheckCard();
    }
}

