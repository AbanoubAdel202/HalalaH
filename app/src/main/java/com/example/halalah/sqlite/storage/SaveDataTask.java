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

public class SaveDataTask extends AsyncTask<byte[], Integer, String> {
    private static final String TAG = Utils.TAGPUBLIC + SaveDataTask.class.getSimpleName();

    private AppDatabase mDatabase;
    private Connection_Parameters mConnectionParameters;

    private Listener mListener;

    public SaveDataTask(AppDatabase database, Connection_Parameters connection_parameters) {
        mDatabase = database;
        mConnectionParameters = connection_parameters;
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
        try {
            database.getConnectionParametersDao().insert(mConnectionParameters);
        } catch (SQLiteConstraintException e) {
            database.getConnectionParametersDao().update(mConnectionParameters);
        }

        long primaryConnectionId = saveConnection(database, mConnectionParameters.getConn_primary());
        long secondaryConnectionID = saveConnection(database, mConnectionParameters.getConn_secondary());
        if(primaryConnectionId > 0 && secondaryConnectionID > 0){
            return "Success";
        } else {
            return "Error";
        }
    }

    private long saveConnection(AppDatabase database, Connection connection) {
        long id = -1;
        if (connection instanceof Wifi) {
            try {
                id = database.getWifiDao().insert((Wifi) connection);
            } catch (SQLiteConstraintException e) {
                id = database.getWifiDao().update((Wifi) connection);
            }
        } else if (connection instanceof Gprs) {
            try {
                id = database.getGprsDao().insert((Gprs) connection);
            } catch (SQLiteConstraintException e) {
                id = database.getGprsDao().update((Gprs) connection);
            }
        } else if (connection instanceof Gsm) {
            try {
                id = database.getGSMDao().insert((Gsm) connection);
            } catch (SQLiteConstraintException e) {
                id = database.getGSMDao().update((Gsm) connection);
            }
        } else if (connection instanceof Tcp_IP) {
            try {
                id = database.getTcpIPDao().insert((Tcp_IP) connection);
            } catch (SQLiteConstraintException e) {
                id = database.getTcpIPDao().update((Tcp_IP) connection);
            }
        } else if (connection instanceof Dialup) {
            try {
                id = database.getDialUpDao().insert((Dialup) connection);
            } catch (SQLiteConstraintException e){
                id = database.getDialUpDao().update((Dialup) connection);
            }
        }
        return id;
    }
}