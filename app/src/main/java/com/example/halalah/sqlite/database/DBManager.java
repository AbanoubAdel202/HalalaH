package com.example.halalah.sqlite.database;

import android.content.Context;

import com.example.halalah.sqlite.database.dao.AidDaoImpl;
import com.example.halalah.sqlite.database.dao.ConnectionParametersDaoImpl;
import com.example.halalah.sqlite.database.dao.KeyDaoImpl;


public final class DBManager {
	private static DBManager instance = new DBManager();
	private AidDaoImpl aidDao = null;
	private KeyDaoImpl capkDao = null;
	private ConnectionParametersDaoImpl connectionParametersDao = null;
	private boolean hasinit = false;
	
	private DBManager() {
	}
	
	public static DBManager getInstance() {
		return instance;
	}
	
	public void init(Context context) {
		if (hasinit)
			return;
		hasinit = true;
		initDaoImpl(context);
	}
	
	public void initDaoImpl(Context context){
		setAidDao(new AidDaoImpl(context));
		setCapkDao(new KeyDaoImpl(context));
		setConnectionParametersDao(new ConnectionParametersDaoImpl(context));
	}

	public AidDaoImpl getAidDao() {
		return aidDao;
	}

	private void setAidDao(AidDaoImpl aidDao) {
		this.aidDao = aidDao;
	}

	public KeyDaoImpl getCapkDao() {
		return capkDao;
	}

	private void setCapkDao(KeyDaoImpl capkDao) {
		this.capkDao = capkDao;
	}

	public ConnectionParametersDaoImpl getConnectionParametersDao() {
		return connectionParametersDao;
	}

	private void setConnectionParametersDao(ConnectionParametersDaoImpl connectionParametersDao) {
		this.connectionParametersDao = connectionParametersDao;
	}
}
