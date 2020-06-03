package com.example.halalah.iso8583;

public class MessageBase {
    private static final String TAG = MessageBase.class.getSimpleName();

    protected static final byte[] ID = {0x60};
    protected static final byte[] DestAddr = {0x00, 0x04};
    protected static final byte[] SrcAddr = {0x00, 0x00};
    protected static final byte[] TotalSWVersion = {0x31};
    protected static final byte[] SWVersion = {0x31, 0x13, 0x66};
    protected static final byte[] TPDU = new byte[5];
    protected static final byte[] MessageHeader = new byte[6];

    
    protected static byte[] getTPDU() {
        return ISO8583Util.byteArrayAdd(ID, DestAddr, SrcAddr);
    }
}
