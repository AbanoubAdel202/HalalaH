package com.example.halalah.card;

import android.os.RemoteException;

import com.topwise.cloudpos.aidl.magcard.MagCardListener;
import com.topwise.cloudpos.aidl.magcard.TrackData;


public class MagCardListenerSub extends MagCardListener.Stub {

    @Override
    public void onSuccess(TrackData trackData) throws RemoteException {

    }

    @Override
    public void onGetTrackFail() throws RemoteException {

    }

    @Override
    public void onError(int errorCode) throws RemoteException {

    }

    @Override
    public void onTimeout() throws RemoteException {

    }

    @Override
    public void onCanceled() throws RemoteException {

    }
}