package com.example.halalah.sqlite.storage;

import android.util.Log;

import com.example.halalah.PosApplication;
import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.sqlite.repository.component.AppDatabase;

public class TMSManager {

    private static TMSManager tmsManager;
    private static AppDatabase mAppDatabase;
    private static AIDListDao aidListDao;
    private static ConnectionParametersDao connectionParametersDao;
    private static DialUpDao dialUpDao;
    private static GprsDao gprsDao;
    private static GSMDao gsmDao;
    private static TcpIPDao tcpIPDao;
    private static WifiDao wifiDao;

    public static TMSManager getInstance() {
        if (tmsManager == null) {
            tmsManager = new TMSManager();
            mAppDatabase = PosApplication.getAppDatabase();
        }
        return tmsManager;
    }

    public void loadData() {
        LoadDataTask loadDataTask = new LoadDataTask(mAppDatabase);
        loadDataTask.setListener(new LoadDataTask.Listener() {
            @Override
            public void afterPost(String s) {
                Log.d("SAMA_TMS", s);
            }
        });
        loadDataTask.execute();
    }

    public void saveConnectionParameters(Connection_Parameters connectionParameters){
        SaveDataTask saveDataTask = new SaveDataTask(mAppDatabase, connectionParameters);
        saveDataTask.setListener(new SaveDataTask.Listener() {
            @Override
            public void afterPost(String s) {
                Log.d("SAMA_TMS", s);
            }
        });
        saveDataTask.execute();
    }
}
