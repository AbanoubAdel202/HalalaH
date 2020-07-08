package com.example.halalah.connect;

public interface SendReceiveListener {
    void onSocketProcessEnd(byte[] recePacket, int errReason);
    void showConnectionStatus(int connectionStatus);
}
