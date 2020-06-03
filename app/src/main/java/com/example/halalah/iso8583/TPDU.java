package com.example.halalah.iso8583;

public class TPDU {
    private static final String TAG = TPDU.class.getSimpleName();

    protected static final byte[] ID = {0x60};
    protected static final byte[] DestAddr = {0x06, 0x03};
    protected static final byte[] SrcAddr = {0x00, 0x00};
    protected static byte[] mTpdu = ISO8583Util.byteArrayAdd(ID, DestAddr, SrcAddr);

    public static byte[] get() {
        return mTpdu;
    }

    public static void set(byte id, byte[] dstaddr, byte[] srcaddr) {
        ID[0] = id;
        System.arraycopy(dstaddr, 0, DestAddr, 0, 2);
        System.arraycopy(srcaddr, 0, SrcAddr,  0, 2);
        mTpdu = ISO8583Util.byteArrayAdd(ID, DestAddr, SrcAddr);
    }
    public static void set(byte[] tpdu) {
        mTpdu = tpdu;
    }
    
}
