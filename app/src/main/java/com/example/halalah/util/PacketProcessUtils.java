package com.example.halalah.util;

public class PacketProcessUtils {
    public static final String PACKET_PROCESS_TYPE = "process_type";
    public static final int PACKET_PROCESS_PURCHASE = 1;
    public static final int PACKET_PROCESS_PURCHASE_WITH_NAQD = 2;
    public static final int PACKET_PROCESS_REFUND = 3;
    public static final int PACKET_PROCESS_AUTHORISATION = 4;
    public static final int PACKET_PROCESS_AUTHORISATION_EXTENSION = 5;
    public static final int PACKET_PROCESS_PURCHASE_ADVICE = 6;
    public static final int PACKET_PROCESS_AUTHORISATION_VOID = 7;
    public static final int PACKET_PROCESS_CASH_ADVANCE = 8;
    public static final int PACKET_PROCESS_REVERSAL = 9;
    public static final int PACKET_PROCESS_SADAD_BILL = 10;
    public static final int PACKET_PROCESS_RECONCILIATION = 11;
    public static final int PACKET_PROCESS_TMS_FILE_DOWNLOAD = 13;
    public static final int PACKET_PROCESS_TERMINAL_REGISTRATION = 14;
    public static final int PACKET_PROCESS_ADMIN = 17;
    public static final int PACKET_PROCESS_AUTHORISATION_ADVICE = 18;
    public static final int PACKET_PROCESS_ONLINE_INIT = 19;


    public static final int SOCKET_PROC_ERROR_REASON_IP_PORT = -1;
    public static final int SOCKET_PROC_ERROR_REASON_CONNE = -2;
    public static final int SOCKET_PROC_ERROR_REASON_SEND = -3;
    public static final int SOCKET_PROC_ERROR_REASON_RECE = -4;
    public static final int SOCKET_PROC_ERROR_REASON_RECE_TIME_OUT = -5;

    public static final int SUCCESS = 0;
}