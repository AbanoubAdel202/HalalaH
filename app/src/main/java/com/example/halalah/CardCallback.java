package com.example.halalah;

import com.topwise.cloudpos.aidl.magcard.TrackData;

public interface CardCallback {
    public void onFindMagCard(TrackData data);
}
