package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.AID_Data;
import com.example.halalah.TMS.AID_List;
import com.example.halalah.TMS.Card_Scheme;
import com.example.halalah.TMS.Conn_Primary;
import com.example.halalah.TMS.Conn_ٍSecondary;
import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.TMS.Device_Specific;
import com.example.halalah.TMS.Dialup;
import com.example.halalah.TMS.Gprs;
import com.example.halalah.TMS.Gsm;
import com.example.halalah.TMS.Message_Text;
import com.example.halalah.TMS.Public_Key;
import com.example.halalah.TMS.Retailer_Data;
import com.example.halalah.TMS.Revoked_Certificates;
import com.example.halalah.TMS.Tcp_IP;
import com.example.halalah.TMS.Wifi;

import java.util.List;

public class TMSManager {

    private static TMSManager tmsManager;

    private static AIDListRepo aidListRepo;
    private static AIDRepo aidRepo;
    private static CardSchemeRepo cardSchemeRepo;
    private static ConnectionParametersRepo connectionParametersRepo;
    private static ConnPrimaryRepo connPrimaryRepo;
    private static ConnSecondaryRepo connSecondaryRepo;
    private static DeviceSpecificRepo deviceSpecificRepo;
    private static DialupRepo dialupRepo;
    private static GprsRepo gprsRepo;
    private static GsmRepo gsmRepo;
    private static MessagesRepo messagesRepo;
    private static PublicKeyRepo publicKeyRepo;
    private static RetailerDataRepo retailerDataRepo;
    private static RevokedCertificatesRepo revokedCertificatesRepo;
    private static TcpipRepo tcpipRepo;
    private static WifiRepo wifiRepo;
    private List<AID_List> aidList;
    private List<AID_Data> aidDataList;
    private List<Card_Scheme> cardSchemeList;
    private List<Connection_Parameters> connectionParametersList;
    private List<Conn_Primary> connPrimaryList;
    private List<Conn_ٍSecondary> connSecondaryList;
    private List<Device_Specific> deviceSpecificList;
    private List<Dialup> dialupList;
    private List<Gprs> gprsList;
    private List<Gsm> gsmList;
    private List<Message_Text> messagesList;
    private List<Public_Key> publicKeyList;
    private List<Retailer_Data> retailerDataList;
    private List<Revoked_Certificates> revokedCertificatesList;
    private List<Tcp_IP> tcpipList;
    private List<Wifi> wifiList;


    public static TMSManager getInstance(Context context) {
        if (tmsManager == null) {
            tmsManager = new TMSManager();

            aidListRepo = new AIDListRepo(context);
            aidRepo = new AIDRepo(context);
            cardSchemeRepo = new CardSchemeRepo(context);
            connectionParametersRepo = new ConnectionParametersRepo(context);
            connPrimaryRepo = new ConnPrimaryRepo(context);
            connSecondaryRepo = new ConnSecondaryRepo(context);
            deviceSpecificRepo = new DeviceSpecificRepo(context);
            dialupRepo = new DialupRepo(context);
            gprsRepo = new GprsRepo(context);
            gsmRepo = new GsmRepo(context);
            messagesRepo = new MessagesRepo(context);
            publicKeyRepo = new PublicKeyRepo(context);
            retailerDataRepo = new RetailerDataRepo(context);
            revokedCertificatesRepo = new RevokedCertificatesRepo(context);
            tcpipRepo = new TcpipRepo(context);
            wifiRepo = new WifiRepo(context);
        }
        return tmsManager;
    }

    public void loadData() {
        aidList = aidListRepo.getAll();
        aidDataList = aidRepo.getAll();
        cardSchemeList = cardSchemeRepo.getAll();
        connectionParametersList = connectionParametersRepo.getAll();
        connPrimaryList = connPrimaryRepo.getAll();
        connSecondaryList = connSecondaryRepo.getAll();
        deviceSpecificList = deviceSpecificRepo.getAll();
        deviceSpecificList = deviceSpecificRepo.getAll();
        dialupList = dialupRepo.getAll();
        gprsList = gprsRepo.getAll();
        gsmList = gsmRepo.getAll();
        messagesList = messagesRepo.getAll();
        publicKeyList = publicKeyRepo.getAll();
        retailerDataList = retailerDataRepo.getAll();
        revokedCertificatesList = revokedCertificatesRepo.getAll();
        tcpipList = tcpipRepo.getAll();
        wifiList = wifiRepo.getAll();
    }

    public AID_List getAidListById(String id) {
        return aidListRepo.getById(id);
    }

    public List<AID_List> getAllAIDList() {
        if (aidList == null || aidList.isEmpty()) {
            return aidListRepo.getAll();
        }
        return aidList;
    }

    public AID_Data getAidById(String id) {
        return aidRepo.getById(id);
    }

    public List<AID_Data> getAllAIDData() {
        if (aidDataList == null || aidDataList.isEmpty()) {
            return aidRepo.getAll();
        }
        return aidDataList;
    }

    public Card_Scheme getCardScheme(String id) {
        return cardSchemeRepo.getById(id);
    }

    public List<Card_Scheme> getAllCardSchemes() {
        if (cardSchemeList == null || cardSchemeList.isEmpty()) {
            return cardSchemeRepo.getAll();
        }
        return cardSchemeList;
    }

    public Connection_Parameters getConnectionParameter(String id) {
        return connectionParametersRepo.getById(id);
    }

    public List<Connection_Parameters> getAllConnectionParameter() {
        if (connectionParametersList == null || connectionParametersList.isEmpty()) {
            return connectionParametersRepo.getAll();
        }
        return connectionParametersList;
    }

    public Conn_Primary getConnectionPrimary(String id) {
        return connPrimaryRepo.getById(id);
    }

    public List<Conn_Primary> getAllConnectionPrimary() {
        if (connPrimaryList == null || connPrimaryList.isEmpty()) {
            return connPrimaryRepo.getAll();
        }
        return connPrimaryList;
    }

    public Conn_ٍSecondary getConnectionSecondary(String id) {
        return connSecondaryRepo.getById(id);
    }

    public List<Conn_ٍSecondary> getAllConnectionSecondary() {
        if (connSecondaryList == null || connSecondaryList.isEmpty()) {
            return connSecondaryRepo.getAll();
        }
        return connSecondaryList;
    }

    public Device_Specific getDeviceSpecific(String id) {
        return deviceSpecificRepo.getById(id);
    }

    public List<Device_Specific> getAllDeviceSpecifics() {
        if (deviceSpecificList == null || deviceSpecificList.isEmpty()) {
            return deviceSpecificRepo.getAll();
        }
        return deviceSpecificList;
    }

    public Dialup getDialup(String id) {
        return dialupRepo.getById(id);
    }

    public List<Dialup> getAllDialups() {
        if (dialupList == null || dialupList.isEmpty()) {
            return dialupRepo.getAll();
        }
        return dialupList;
    }

    public Gprs getGprs(String id) {
        return gprsRepo.getById(id);
    }

    public List<Gprs> getAllGprs() {
        if (gprsList == null || gprsList.isEmpty()) {
            return gprsRepo.getAll();
        }
        return gprsList;
    }

    public Gsm getGsm(String id) {
        return gsmRepo.getById(id);
    }

    public List<Gsm> getAllGsm() {
        if (gsmList == null || gsmList.isEmpty()) {
            return gsmRepo.getAll();
        }
        return gsmList;
    }

    public Message_Text getMessage(String id) {
        return messagesRepo.getById(id);
    }

    public List<Message_Text> getAllMessages() {
        if (messagesList == null || messagesList.isEmpty()) {
            return messagesRepo.getAll();
        }
        return messagesList;
    }

    public Public_Key getPublicKey(String id) {
        return publicKeyRepo.getById(id);
    }

    public List<Public_Key> getAllPublicKeys() {
        if (publicKeyList == null || publicKeyList.isEmpty()) {
            return publicKeyRepo.getAll();
        }
        return publicKeyList;
    }

    public Retailer_Data getRetailerData(String id) {
        return retailerDataRepo.getById(id);
    }

    public List<Retailer_Data> getAllRetailerData() {
        if (retailerDataList == null || retailerDataList.isEmpty()) {
            return retailerDataRepo.getAll();
        }
        return retailerDataList;
    }

    public Wifi getWifi(String id) {
        return wifiRepo.getById(id);
    }

    public List<Wifi> getAllWifi() {
        if (wifiList == null || wifiList.isEmpty()) {
            return wifiRepo.getAll();
        }
        return wifiList;
    }

    public boolean insert(AID_List aid_list) {
        return aidListRepo.insert(aid_list);
    }

    public boolean insert(AID_Data aid_data) {
        return aidRepo.insert(aid_data);
    }

    public boolean insert(Card_Scheme card_scheme) {
        return cardSchemeRepo.insert(card_scheme);
    }

    public boolean insert(Connection_Parameters connection_parameters) {
        return connectionParametersRepo.insert(connection_parameters);
    }

    public boolean insert(Conn_Primary conn_primary) {
        return connPrimaryRepo.insert(conn_primary);
    }

    public boolean insert(Conn_ٍSecondary conn_ٍSecondary) {
        return connSecondaryRepo.insert(conn_ٍSecondary);
    }

    public boolean insert(Device_Specific device_specific) {
        return deviceSpecificRepo.insert(device_specific);
    }

    public boolean insert(Dialup dialup) {
        return dialupRepo.insert(dialup);
    }

    public boolean insert(Gprs gprs) {
        return gprsRepo.insert(gprs);
    }

    public boolean insert(Gsm gsm) {
        return gsmRepo.insert(gsm);
    }

    public boolean insert(Message_Text message) {
        return messagesRepo.insert(message);
    }

    public boolean insert(Public_Key public_key) {
        return publicKeyRepo.insert(public_key);
    }

    public boolean insert(Retailer_Data retailer_data) {
        return retailerDataRepo.insert(retailer_data);
    }

    public boolean insert(Revoked_Certificates revoked_certificates) {
        return revokedCertificatesRepo.insert(revoked_certificates);
    }

    public boolean insert(Tcp_IP tcp_ip) {
        return tcpipRepo.insert(tcp_ip);
    }

    public boolean insert(Wifi wifi) {
        return wifiRepo.insert(wifi);
    }
}
