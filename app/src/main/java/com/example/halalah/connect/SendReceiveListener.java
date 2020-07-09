package com.example.halalah.connect;

public interface SendReceiveListener {
    void showConnectionStatus(int connectionStatus);
    void onSuccess(byte[] receivedPacket);
    void onFailure(int errReason);
}
