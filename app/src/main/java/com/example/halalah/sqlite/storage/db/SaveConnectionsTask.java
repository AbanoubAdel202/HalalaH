package com.example.halalah.sqlite.storage.db;

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
import com.example.halalah.sqlite.storage.dao.BaseDao;

public class SaveConnectionsTask<T> extends AsyncTask<T, Integer, Long> {

    private static final String TAG = Utils.TAGPUBLIC + SaveConnectionsTask.class.getSimpleName();

    private BaseDao<T> dao;
    private Connection_Parameters mConnectionParameters;

    private Listener mListener;

    public SaveConnectionsTask(Class<? extends BaseDao> baseDao, Connection_Parameters connection_parameters) {
        dao = baseDao.cast(baseDao);
        mConnectionParameters = connection_parameters;
    }

    public interface Listener {
        void afterPost(Long id);
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
    protected Long doInBackground(T... obj) {
        Log.i(TAG, "doInBackground()");
        Long id = dao.insert(obj[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long id) {
        Log.i(TAG, "onPostExecute()");
        super.onPostExecute(id);
        if (mListener != null) {
            if (id != -1) {
                mListener.afterPost(id);
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