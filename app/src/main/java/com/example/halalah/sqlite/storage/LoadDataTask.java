package com.example.halalah.sqlite.storage;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.halalah.TMS.Connection;
import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.TMS.Dialup;
import com.example.halalah.TMS.Gprs;
import com.example.halalah.TMS.Gsm;
import com.example.halalah.TMS.Tcp_IP;
import com.example.halalah.TMS.Wifi;
import com.example.halalah.Utils;
import com.example.halalah.sqlite.repository.component.AppDatabase;

public class LoadDataTask extends AsyncTask<byte[], Integer, String> {
    private static final String TAG = Utils.TAGPUBLIC + LoadDataTask.class.getSimpleName();

    private AppDatabase mDatabase;

    private Listener mListener;

    public LoadDataTask(AppDatabase database) {
        mDatabase = database;
    }

    public interface Listener {
        void afterPost(String t);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        Log.i(TAG, "onPreExecute()");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(byte[]... params) {
        Log.i(TAG, "doInBackground()");
        return testRoomDB(mDatabase);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i(TAG, "onPostExecute()");
        super.onPostExecute(s);
        if (mListener != null) {
            if (s != null) {
                mListener.afterPost(s);
            }
        }
    }

    private String testRoomDB(AppDatabase database) {
        // retrieving
        String primaryConnectionType = database.getConnectionParametersDao().getPrimaryConnectionType();
        String secondaryConnectionType = database.getConnectionParametersDao().getSecondaryConnectionType();
        try {
            Connection_Parameters connectionParameters = new Connection_Parameters();
            connectionParameters.setConn_primary(getConnection(database, primaryConnectionType, "1"));
            connectionParameters.setConn_secondary(getConnection(database, secondaryConnectionType, "2"));
            return "Success";
        } catch (Exception e){
            return "Error";
        }
    }

    private Connection getConnection(AppDatabase database, String connectionType, String priority) {
        switch (connectionType){
            case Connection.DIALUP:
                return database.getDialUpDao().getConnection(priority);
            case Connection.GPRS:
                return database.getGprsDao().getConnection(priority);
            case Connection.GSM:
                return database.getGSMDao().getConnection(priority);
            case Connection.WIFI:
                return database.getWifiDao().getConnection(priority);
            case Connection.TCPIP:
                return database.getTcpIPDao().getConnection(priority);
        }
        return null;
    }
}