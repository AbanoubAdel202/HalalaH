package com.example.halalah.iso8583;

public class FrontHeader {
	private static final String TAG = FrontHeader.class.getSimpleName();

    protected static final byte[] ID = new byte[23];
    protected static final byte[] MNo = new byte[15];
    protected static final byte[] Type = new byte[8];
    protected static final byte[] MName= new byte[15];
    protected static final byte[] Len = new byte[8];
    
    public static byte[] get(String termId, String merId) {

    	System.arraycopy(merId.getBytes(), 0, ID, 0, 15);
    	System.arraycopy(termId.getBytes(), 0, ID, 15, 8);
    	System.arraycopy("300000000000003".getBytes(), 0, MNo, 0, 15);
    	System.arraycopy("00000000".getBytes(), 0, Type, 0, 8);
    	System.arraycopy(merId.getBytes(), 0, MName, 0, 15);
    	return ISO8583Util.byteArrayAdd(ID, MNo, ISO8583Util.byteArrayAdd(Type, MName, Len));
    }
    
    public static void set(byte[] btype, byte[] blen){
    	setType(btype);
    	setLen(blen);
    }
    
    public static void setType(byte[] btype){
    	System.arraycopy(btype, 0, Type, 0, 8);
    }
    
    public static void setLen(byte[] blen){
    	System.arraycopy(blen, 0, Len, 0, 8);
    }
    public static void setLen(int len){
    	String slen = String.format("%08d", len);
    	System.arraycopy(slen.getBytes(), 0, Len, 0, 8);
    }
}
