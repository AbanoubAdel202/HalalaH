package com.example.halalah.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.halalah.TMS.Connection;
import com.example.halalah.TMS.Gprs;
import com.example.halalah.TMS.Wifi;
import com.example.halalah.Utils;
import com.google.gson.Gson;

public class SharedPreferencesManager {

    private static final String TAG = Utils.TAGPUBLIC + SharedPreferencesManager.class.getSimpleName();

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private boolean mIsSSLEnabled = false;
    private Gson gson;

    public SharedPreferencesManager(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences("communication_info", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        gson = new Gson();
    }

    public void saveConnection(Gprs gprs) {

        gson = new Gson();
        String json = gson.toJson(gprs);
        mEditor.putString("default_connection", json);
        mEditor.commit();
    }

    public void saveConnection(Wifi wifi) {

        gson = new Gson();
        String json = gson.toJson(wifi);
        mEditor.putString("default_connection", json);
        mEditor.commit();
    }

    public Connection getConnection() {
        String json = mSharedPreferences.getString("default_connection", "");

        if (json.contains("GPRS_dial_Number")) {
            return gson.fromJson(json, Gprs.class);
        } else if (json.contains("Count_Access_Retries")) {
            return gson.fromJson(json, Wifi.class);
        } else {
            return null;
        }
    }

    public String getTPDU() {
        Log.i(TAG, "getTPDU  " + mSharedPreferences.getString(CommunicationUtils.TPDU, null));
        return mSharedPreferences.getString(CommunicationUtils.TPDU, CommunicationUtils.tpdu_default_1);
    }

    public void setTPDU(String tpdu) {
        Log.i(TAG, "setTPDU  " + tpdu);
        mEditor.putString(CommunicationUtils.TPDU, CommunicationUtils.tpdu_default_1);
        mEditor.commit();
    }

    public void setIsSSLEnabled(boolean isSSLEnabled) {
        mIsSSLEnabled = isSSLEnabled;
    }

    public boolean isSSLEnabled() {
        return mIsSSLEnabled;
    }

    public void saveIsRegistered(boolean isRegistered) {
        Log.i(TAG, "saveIsRegistered  " + isRegistered);
        mEditor.putBoolean(CommunicationUtils.IS_REGISTERED, isRegistered);
        mEditor.commit();
    }

    public boolean isRegistered() {
        return mSharedPreferences.getBoolean(CommunicationUtils.IS_REGISTERED, false);
    }
}