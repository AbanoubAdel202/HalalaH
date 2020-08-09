package com.example.halalah.connect.connectivity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * ConnectionManager
 */
public class ConnectionManager {
    private Context context;
    private Activity activity;
    private static final String WPA = "WPA";
    private static final String WEP = "WEP";
    private static final String OPEN = "Open";
    private final static String TAG = "WiFiConnector";

    public ConnectionManager(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void enableWifi() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            Toast.makeText(activity, "Wifi Turned On", Toast.LENGTH_SHORT).show();
        }
    }

    public void requestWIFIConnection(String networkSSID, String networkPass) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            //Check ssid exists
            scanWifi(wifiManager, networkSSID, found -> {
                if (found){
                    if (getCurrentSSID(wifiManager) != null && getCurrentSSID(wifiManager).equals("\"" + networkSSID + "\"")) {
                        Toast.makeText(activity, "Already Connected With ", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Already Connected With " + networkSSID);
                    }
                    //Security type detection
                    String SECURE_TYPE = checkSecurity(wifiManager, networkSSID);
                    if (SECURE_TYPE == null) {
                        Log.e(TAG, "Unable to find Security type for " + networkSSID);
                    }
                    if (SECURE_TYPE.equals(WPA)) {
                        WPA(networkSSID, networkPass, wifiManager);
                    } else if (SECURE_TYPE.equals(WEP)) {
                        WEP(networkSSID, networkPass);
                    } else {
                        OPEN(wifiManager, networkSSID);
                    }
                }
            });
            /*connectME();*/
        } catch (Exception e) {
            Log.e(TAG, "Error Connecting WIFI" + networkSSID);
        }
    }

    private void WPA(String networkSSID, String networkPass, WifiManager wifiManager) {
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = networkSSID;
        wc.preSharedKey = "\"" + networkPass + "\"";
        wc.status = WifiConfiguration.Status.ENABLED;
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        int id = wifiManager.addNetwork(wc);
        wifiManager.disconnect();
        wifiManager.enableNetwork(id, true);
        wifiManager.reconnect();
    }

    private void WEP(String networkSSID, String networkPass) {
    }

    private void OPEN(WifiManager wifiManager, String networkSSID) {
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\"" + networkSSID + "\"";
        wc.hiddenSSID = true;
        wc.priority = 0xBADBAD;
        wc.status = WifiConfiguration.Status.ENABLED;
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        int id = wifiManager.addNetwork(wc);
        wifiManager.disconnect();
        wifiManager.enableNetwork(id, true);
        wifiManager.reconnect();
    }

    void scanWifi(WifiManager wifiManager, String networkSSID, final ScanListener<Boolean> listener) {

        List<ScanResult> scanList = getScanResults(wifiManager);
        new Handler().postDelayed(() -> {
            Log.e(TAG, "scanList # " + scanList.size());
            for (ScanResult i : scanList) {
                if (i.SSID != null) {
                    Log.e(TAG, "SSID: " + i.SSID);
                }

                if (i.SSID != null && i.SSID.equals(networkSSID)) {
                    Log.e(TAG, "Found SSID: " + i.SSID);
                    listener.onComplete(true);
                    break;
                }
            }

            listener.onComplete(false);
        }, 500);
    }

    List<ScanResult> getScanResults(WifiManager wifiManager) {
        Log.e(TAG, "scanWifi starts");
        return wifiManager.getScanResults();
    }


    public String getCurrentSSID(WifiManager wifiManager) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }

    private String checkSecurity(WifiManager wifiManager, String ssid) {
        List<ScanResult> networkList = wifiManager.getScanResults();
        for (ScanResult network : networkList) {
            if (network.SSID.equals(ssid)) {
                String Capabilities = network.capabilities;
                if (Capabilities.contains("WPA")) {
                    return WPA;
                } else if (Capabilities.contains("WEP")) {
                    return WEP;
                } else {
                    return OPEN;
                }

            }
        }
        return null;
    }

    class Constants{

        public static final int ALREADY_CONNECTED = 1;
        public static final int UNABLE_TO_FIND_SECURITY_TYPE = 2;
        public static final int CONNECTION_REQUESTED = 3;
        public static final int SSID_NOT_FOUND = 4;
    }


    public interface ScanListener<T> {
        void onComplete(T arg);
    }

}
