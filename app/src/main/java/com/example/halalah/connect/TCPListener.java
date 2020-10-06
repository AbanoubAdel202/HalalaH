package com.example.halalah.connect;

public interface TCPListener {
    void onTCPMessageRecieved(byte[] message);
    void onTCPConnectionStatusChanged(boolean isConnectedNow);
}
