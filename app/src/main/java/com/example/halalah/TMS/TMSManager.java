package com.example.halalah.TMS;

import com.example.halalah.Utils;
import com.example.halalah.sqlite.database.DBManager;

import java.util.List;

import static com.example.halalah.Utils.CONNECTION_TYPE_DIALUP;
import static com.example.halalah.Utils.CONNECTION_TYPE_GPRS;
import static com.example.halalah.Utils.CONNECTION_TYPE_GSM;
import static com.example.halalah.Utils.CONNECTION_TYPE_TCPIP;
import static com.example.halalah.Utils.CONNECTION_TYPE_WIFI;

public class TMSManager {
    private static final String TAG = Utils.TAGPUBLIC + TMSManager.class.getSimpleName();
    private static TMSManager mInstance;
    private static DBManager dbManager;

    public static TMSManager getInstance() {
        if (mInstance == null) {
            mInstance = new TMSManager();
            dbManager = DBManager.getInstance();
        }
        return mInstance;
    }

    public void insert(Aid aid) {
        dbManager.getAidListDao().insert(aid);
    }

    public void insert(AID_Data aidData) {
        dbManager.getAidDao().insert(aidData);
    }

    public void insert(Card_Scheme cardScheme) {
//        Log.d(TAG, cardScheme.toString());
        dbManager.getCardSchemeDao().insertOrUpdate(cardScheme);
    }

    public void insert(Connection_Parameters connectionParameters) {
        insertConnection(connectionParameters.getPrimaryConnectionType(), connectionParameters.getConn_primary());
        insertConnection(connectionParameters.getSecondaryConnectionType(), connectionParameters.getConn_secondary());
        dbManager.getConnectionParametersDao().insert(connectionParameters);
    }

    public void insert(Device_Specific deviceSpecific) {
        dbManager.getDeviceSpecificDao().insert(deviceSpecific);
    }

    public void insert(Message_Text message) {
        dbManager.getMessageDao().insert(message);
    }

    public void insert(Public_Key publicKey) {
        dbManager.getPublicKeyDao().insert(publicKey);
    }

    public void insert(Retailer_Data retailerData) {
        dbManager.getRetailerDataDao().insert(retailerData);
    }

    public void insert(Revoked_Certificates revokedCertificate) {
        dbManager.getRevokedCertificateDao().insert(revokedCertificate);
    }

    public Aid getAid(String aid) {
        return dbManager.getAidListDao().get(aid);
    }

    public List<Aid> getAidList() {
        return dbManager.getAidListDao().getAll();
    }

    public AID_Data getAidData(String aid) {
        return dbManager.getAidDao().get(aid);
    }

    public List<AID_Data> getAidDataList() {
        return dbManager.getAidDao().getAll();
    }

    public List<Card_Scheme> getAllCardScheme(){
        return dbManager.getCardSchemeDao().getAll();
    }

    public Card_Scheme getCardSchemeByAID(String aid){
        return dbManager.getCardSchemeDao().getByAid(aid);
    }

    public Card_Scheme getCardSchemeByPAN(String pan){
        return dbManager.getCardSchemeDao().getByPAN(pan);
    }

    public Card_Scheme getCardSchemeByID(String id){
        return dbManager.getCardSchemeDao().getById(id);
    }

    public Connection_Parameters getConnectionParameters(){

        Connection_Parameters connectionParameters = dbManager.getConnectionParametersDao().get();
        if (connectionParameters  == null) {
            return null;
        }
        connectionParameters.setConn_primary(getConnection(connectionParameters.getPrimaryConnectionType(), "1"));
        connectionParameters.setConn_secondary(getConnection(connectionParameters.getSecondaryConnectionType(), "2"));
        return connectionParameters;
    }


    public Connection getPrimaryConnection(String primaryConnectionType) {
        return dbManager.getConnectionParametersDao().getPrimaryConnection();
    }

    public Connection getSecondaryConnection(){
        return dbManager.getConnectionParametersDao().getSecondaryConnection();
    }

    public List<Device_Specific> getAllDeviceSpecifics(){
        return dbManager.getDeviceSpecificDao().getAll();
    }

    public Limits getCTLSLimitsByCardScheme(String cardSchemeID){
        return dbManager.getDeviceSpecificDao().getLimits(cardSchemeID);
    }

    public Device_Specific getDeviceSpecific(){
        return dbManager.getDeviceSpecificDao().get();
    }

    public Message_Text getMessage(String actionCode, String displayCode){
        return dbManager.getMessageDao().get(actionCode, displayCode);
    }

    public List<Message_Text> getAllMessages(){
        return dbManager.getMessageDao().getAll();
    }

    public Public_Key getPublicKeyByRID(String rid){
        return dbManager.getPublicKeyDao().getByRid(rid);
    }

    public List<Public_Key> getAllPublicKeys(){
        return dbManager.getPublicKeyDao().getAll();
    }

    public Retailer_Data getRetailerData(){
        return dbManager.getRetailerDataDao().get();
    }

    public List<Revoked_Certificates> getAllRevokedCertificates(){
        return dbManager.getRevokedCertificateDao().getAll();
    }

    private void insertConnection(String connectionType, Connection connection) {
        switch (connectionType) {
            case CONNECTION_TYPE_DIALUP:
                dbManager.getDialUpDao().insert((Dialup) connection);
                break;
            case CONNECTION_TYPE_TCPIP:
                dbManager.getTcpIPDao().insert((Tcp_IP) connection);
                break;
            case CONNECTION_TYPE_GPRS:
                dbManager.getGprsDao().insert((Gprs) connection);
                break;
            case CONNECTION_TYPE_GSM:
                dbManager.getGsmDao().insert((Gsm) connection);
                break;
            case CONNECTION_TYPE_WIFI:
                dbManager.getWifiDao().insert((Wifi) connection);
                break;


        }
    }

    private Connection getConnection(String connectionType, String priority) {
        switch (connectionType) {
            case CONNECTION_TYPE_DIALUP:
                return dbManager.getDialUpDao().findByPriority(priority);
            case CONNECTION_TYPE_TCPIP:
                return dbManager.getTcpIPDao().findByPriority(priority);
            case CONNECTION_TYPE_GPRS:
                return dbManager.getGprsDao().findByPriority(priority);
            case CONNECTION_TYPE_GSM:
                return dbManager.getGsmDao().findByPriority(priority);
            case CONNECTION_TYPE_WIFI:
                return dbManager.getWifiDao().findByPriority(priority);
        }
        return null;
    }
}
