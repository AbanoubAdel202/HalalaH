package com.example.halalah.util;

public class PacketProcessUtils {
    public static final String PACKET_PROCESS_TYPE = "process_type";
    public static final int PACKET_PROCESS_PURCHASE = 1;
    public static final int PACKET_PROCESS_REFUND = 2;
    public static final int PACKET_PROCESS_REVERSAL = 3;
    public static final int PACKET_PROCESS_CONSUME_REVOKE_POSITIVE = 4;
    public static final int PACKET_PROCESS_BALANCE_INQUIRY = 5;
    public static final int PACKET_PROCESS_RETURN_GOODS = 6;
    public static final int PACKET_PROCESS_SCAN = 7;
    public static final int PACKET_PROCESS_SCAN_REVOKE = 8;

    public static final int PACKET_PROCESS_PARAM_TRANS = 9;
    public static final int PACKET_PROCESS_STATUS_UPLOAD = 10;
    public static final int PACKET_PROCESS_ECHO_TEST = 11;
    public static final int PACKET_PROCESS_IC_CAPK_DOWNLOAD = 13;
    public static final int PACKET_PROCESS_IC_PARA_DOWNLOAD = 14;

    public static final int PACKET_PROCESS_SIGN_UP = 17;
    public static final int PACKET_PROCESS_SIGN_OFF = 18;
    public static final int PACKET_PROCESS_ONLINE_INIT = 19;

    public static final int SOCKET_PROC_ERROR_REASON_IP_PORT = 1;
    public static final int SOCKET_PROC_ERROR_REASON_CONNE = 2;
    public static final int SOCKET_PROC_ERROR_REASON_SEND = 3;
    public static final int SOCKET_PROC_ERROR_REASON_RECE = 4;
    public static final int SOCKET_PROC_ERROR_REASON_RECE_TIME_OUT = 5;
}