package com.example.halalah.sqlite.storage.db;

import android.os.AsyncTask;
import android.util.Log;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.halalah.TMS.Connection;
import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.Utils;
import com.example.halalah.sqlite.repository.component.AppDatabase;
import com.example.halalah.sqlite.storage.dao.BaseDao;

import java.util.List;

public class LoadConnectionsTask<T> extends AsyncTask<String, Integer, List<T>> {
    private static final String TAG = Utils.TAGPUBLIC + LoadConnectionsTask.class.getSimpleName();

    private BaseDao<T> dao;

    private Listener mListener;

    public LoadConnectionsTask(Class<? extends BaseDao> baseDao) {
        dao = baseDao.cast(baseDao);
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
    protected List<T> doInBackground(String... params) {
        Log.i(TAG, "doInBackground()");
        return dao.getList(new SimpleSQLiteQuery(params[0]));
    }

    @Override
    protected void onPostExecute(List<T> queryResults) {
        Log.i(TAG, "onPostExecute()");
        super.onPostExecute(queryResults);
        if (mListener != null) {
            if (queryResults != null && !queryResults.isEmpty()) {
                mListener.afterPost(queryResults);
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


    public interface Listener {
        void afterPost(List results);
    }
}