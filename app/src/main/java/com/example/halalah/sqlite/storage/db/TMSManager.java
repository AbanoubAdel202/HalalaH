package com.example.halalah.sqlite.storage.db;

import android.util.Log;

import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.Utils;
import com.example.halalah.sqlite.database.dao.AidListDaoImpl;
import com.example.halalah.sqlite.storage.ConnectionParametersDao;
import com.example.halalah.sqlite.storage.DialUpDao;
import com.example.halalah.sqlite.storage.GSMDao;
import com.example.halalah.sqlite.storage.GprsDao;
import com.example.halalah.sqlite.storage.TcpIPDao;
import com.example.halalah.sqlite.storage.WifiDao;

public class TMSManager {

    private static final String TAG = Utils.TAGPUBLIC + TMSManager.class.getSimpleName();

    private static TMSManager tmsManager;
//    private static AppDatabase mAppDatabase;
//    private static AIDListDao aidListDao;
//    private static ConnectionParametersDao connectionParametersDao;
//    private static DialUpDao dialUpDao;
//    private static GprsDao gprsDao;
//    private static GSMDao gsmDao;
//    private static TcpIPDao tcpIPDao;
//    private static WifiDao wifiDao;



    private static AidListDaoImpl aidListDao;
    private static ConnectionParametersDao connectionParametersDao;
    private static DialUpDao dialUpDao;
    private static GprsDao gprsDao;
    private static GSMDao gsmDao;
    private static TcpIPDao tcpIPDao;
    private static WifiDao wifiDao;

    public static TMSManager getInstance() {
        if (tmsManager == null) {
            tmsManager = new TMSManager();
        }
        return tmsManager;
    }

    public void loadData() {



//        LoadConnectionsTask loadDataTask = new LoadConnectionsTask(mAppDatabase.getConnectionParametersDao().getClass());
//        loadDataTask.setListener(s -> Log.d("SAMA_TMS", s.toString()));
//        loadDataTask.execute();
    }

    public void saveConnectionParameters(Connection_Parameters connectionParameters) {
        Long id = connectionParametersDao.insert(connectionParameters);
        Log.d(TAG, "connectionParameters id: " + id);

//        SaveConnectionsTask saveConnectionsTask =
//                new SaveConnectionsTask(mAppDatabase.getConnectionParametersDao().getClass(), connectionParameters);
//        saveConnectionsTask.setListener(s -> Log.d("SAMA_TMS", s.toString()));
//        saveConnectionsTask.execute();
    }
}
