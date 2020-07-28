package com.example.halalah.sqlite.database;

import android.content.Context;


import com.example.halalah.sqlite.database.dao.AidDaoImpl;
import com.example.halalah.sqlite.database.dao.AidListDaoImpl;
import com.example.halalah.sqlite.database.dao.CardSchemeDaoImpl;
import com.example.halalah.sqlite.database.dao.ConnectionParametersDaoImpl;
import com.example.halalah.sqlite.database.dao.DeviceSpecificDaoImpl;
import com.example.halalah.sqlite.database.dao.KeyDaoImpl;
import com.example.halalah.sqlite.database.dao.MessageDaoImpl;
import com.example.halalah.sqlite.database.dao.RetailerDataDaoImpl;
import com.example.halalah.sqlite.database.dao.RevokedCertificateDaoImpl;


public final class DBManager {
	private static DBManager instance = new DBManager();
	private AidListDaoImpl aidListDao = null;
	private AidDaoImpl aidDao = null;
	private CardSchemeDaoImpl cardSchemeDao = null;
	private ConnectionParametersDaoImpl connectionParametersDao = null;
	private DeviceSpecificDaoImpl deviceSpecificDao = null;
	private MessageDaoImpl messageDao = null;
	private KeyDaoImpl publicKeyDao = null;
	private RetailerDataDaoImpl retailerDataDao = null;
	private RevokedCertificateDaoImpl revokedCertificateDao = null;

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
		setAidListDao(new AidListDaoImpl(context));
		setAidDao(new AidDaoImpl(context));
		setCardSchemeDao(new CardSchemeDaoImpl(context));
		setConnectionParametersDao(new ConnectionParametersDaoImpl(context));
		setDeviceSpecificDao(new DeviceSpecificDaoImpl(context));
		setMessageDao(new MessageDaoImpl(context));
		setPublicKeyDao(new KeyDaoImpl(context));
		setRetailerDataDao(new RetailerDataDaoImpl(context));
		setRevokedCertificateDao(new RevokedCertificateDaoImpl(context));
	}

	public AidDaoImpl getAidDao() {
		return aidDao;
	}

	private void setAidDao(AidDaoImpl aidDao) {
		this.aidDao = aidDao;
	}

	public KeyDaoImpl getPublicKeyDao() {
		return publicKeyDao;
	}

	private void setPublicKeyDao(KeyDaoImpl capkDao) {
		this.publicKeyDao = capkDao;
	}

	public ConnectionParametersDaoImpl getConnectionParametersDao() {
		return connectionParametersDao;
	}

	private void setConnectionParametersDao(ConnectionParametersDaoImpl connectionParametersDao) {
		this.connectionParametersDao = connectionParametersDao;
	}

	public AidListDaoImpl getAidListDao() {
		return aidListDao;
	}

	private void setAidListDao(AidListDaoImpl aidListDao) {
		this.aidListDao = aidListDao;
	}

	public CardSchemeDaoImpl getCardSchemeDao() {
		return cardSchemeDao;
	}

	private void setCardSchemeDao(CardSchemeDaoImpl cardSchemeDao) {
		this.cardSchemeDao = cardSchemeDao;
	}

	public DeviceSpecificDaoImpl getDeviceSpecificDao() {
		return deviceSpecificDao;
	}

	private void setDeviceSpecificDao(DeviceSpecificDaoImpl deviceSpecificDao) {
		this.deviceSpecificDao = deviceSpecificDao;
	}

	public MessageDaoImpl getMessageDao() {
		return messageDao;
	}

	private void setMessageDao(MessageDaoImpl messageDao) {
		this.messageDao = messageDao;
	}

	public RetailerDataDaoImpl getRetailerDataDao() {
		return retailerDataDao;
	}

	private void setRetailerDataDao(RetailerDataDaoImpl retailerDataDao) {
		this.retailerDataDao = retailerDataDao;
	}

	public RevokedCertificateDaoImpl getRevokedCertificateDao() {
		return revokedCertificateDao;
	}

	private void setRevokedCertificateDao(RevokedCertificateDaoImpl revokedCertificateDao) {
		this.revokedCertificateDao = revokedCertificateDao;
	}
}
