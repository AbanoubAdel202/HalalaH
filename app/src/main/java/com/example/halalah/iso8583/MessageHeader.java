package com.example.halalah.iso8583;

public class MessageHeader {
    private static final String TAG = MessageHeader.class.getSimpleName();
    
    protected static final byte[]  AppType   = {(byte) 0x90};
    protected static final byte[]  Standard  = {(byte) 0x20};
    protected static final byte[]  TermStat  = {(byte) 0x01};
    protected static final byte[]  ProcCode  = {(byte) 0x00};
    protected static final byte[]  Reserve   = {(byte) 0x00, 0x00, 0x00};
    
    public static byte[] Merge(byte b1, byte b2)
    {
    	String cs = String.format("%01X%01X", b1, b2);
    	return BCDASCII.hexStringToBytes(cs);
    }
    
    public static byte[] get()
    {
    	return ISO8583Util.byteArrayAdd(AppType, Standard, ISO8583Util.byteArrayAdd(MessageHeader.Merge((byte)TermStat[0], (byte)ProcCode[0]),Reserve));
    }
    
    public static void setAppType(byte apptype)
    {
    	AppType[0] = apptype;
    }
    public static void setAppType(byte[] apptype)
    {
    	AppType[0] = apptype[0];
    }
    
    public static void setStandard(byte standard)
    {
    	Standard[0] = standard;
    }
    public static void setStandard(byte[] standard)
    {
    	Standard[0] = standard[0];
    }
    
    public static void setTermStat(byte termStat)
    {
    	TermStat[0] = termStat;
    }
    public static void setTermStat(byte[] termStat)
    {
    	TermStat[0] = termStat[0];
    }
    
    public static void setProcCode(byte procCode)
    {
    	ProcCode[0] = procCode;
    }
    public static void setProcCode(byte[] procCode)
    {
    	ProcCode[0] = procCode[0];
    }
    
    public static void setReserve(byte[] reserve)
    {
    	System.arraycopy(reserve, 0, Reserve, 0, 3);
    }
}
