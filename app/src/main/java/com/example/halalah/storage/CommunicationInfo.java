package com.example.halalah.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.halalah.Utils;

public class CommunicationInfo {
    private static final String TAG = Utils.TAGPUBLIC + CommunicationInfo.class.getSimpleName();

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private boolean mIsSSLEnabled = false;

    public CommunicationInfo(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences("communication_info", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void setHostIP(String hostIP) {
        Log.i(TAG, "setHostIP  "+hostIP);
        mEditor.putString(CommunicationUtils.HOST_IP, hostIP);
        mEditor.commit();
    }

    public String getHostIP() {
        Log.i(TAG, "getHostIP  "+mSharedPreferences.getString(CommunicationUtils.HOST_IP, null));
        return mSharedPreferences.getString(CommunicationUtils.HOST_IP, CommunicationUtils.host_ip_default);
    }

    public void setHostPort(String hostPort) {
        Log.i(TAG, "setHostPort  "+hostPort);
        mEditor.putString(CommunicationUtils.HOST_PORT, hostPort);
        mEditor.commit();
    }

    public String getHostPort() {
        Log.i(TAG, "getHostPort  "+mSharedPreferences.getString(CommunicationUtils.HOST_PORT, null));
        return mSharedPreferences.getString(CommunicationUtils.HOST_PORT, CommunicationUtils.host_port_default);
    }

    public void setSpareHostIP(String spareHostIP) {
        Log.i(TAG, "setSpareHostIP  "+spareHostIP);
        mEditor.putString(CommunicationUtils.HOST_SPARE_IP, spareHostIP);
        mEditor.commit();
    }

    public String getSpareHostIP() {
        Log.i(TAG, "getSpareHostIP  "+mSharedPreferences.getString(CommunicationUtils.HOST_SPARE_IP, null));
        return mSharedPreferences.getString(CommunicationUtils.HOST_SPARE_IP, CommunicationUtils.host_spare_ip_default);
    }

    public void setSpareHostPort(String hostPort) {
        Log.i(TAG, "setHostPort  "+hostPort);
        mEditor.putString(CommunicationUtils.HOST_SPARE_PORT, hostPort);
        mEditor.commit();
    }

    public String getSpareHostPort() {
        Log.i(TAG, "getHostPort  "+mSharedPreferences.getString(CommunicationUtils.HOST_SPARE_PORT, null));
        return mSharedPreferences.getString(CommunicationUtils.HOST_SPARE_PORT, CommunicationUtils.host_spare_port_default);
    }

    public void setTPDU(String tpdu) {
        Log.i(TAG, "setTPDU  "+tpdu);
        mEditor.putString(CommunicationUtils.TPDU, CommunicationUtils.tpdu_default_1);
        mEditor.commit();
    }

    public String getTPDU() {
        Log.i(TAG, "getTPDU  "+mSharedPreferences.getString(CommunicationUtils.TPDU, null));
        return mSharedPreferences.getString(CommunicationUtils.TPDU, CommunicationUtils.tpdu_default_1);
    }

    public void setIsSSLEnabled(boolean isSSLEnabled){
         mIsSSLEnabled = isSSLEnabled;
    }

    public boolean isSSLEnabled(){
        return mIsSSLEnabled;
    }
}