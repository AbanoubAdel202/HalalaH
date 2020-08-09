package com.example.halalah.connect;

public interface iConnect {

    int CONNECTION_STATUS_CONNECTED = 1;
    int CONNECTION_STATUS_IN_PROGRESS = 0;
    int CONNECTION_STATUS_DISCONNECTED = -1;

    boolean connect(String host, String port);

    int send(byte[] message);

    byte[] receive();

    int getConnectionStatus();

    void disconnect();
}
