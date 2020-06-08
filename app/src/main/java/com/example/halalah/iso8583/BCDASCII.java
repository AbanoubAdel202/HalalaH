package com.example.halalah.iso8583;


import com.example.halalah.Utils;

public class BCDASCII {
    private static final String TAG = Utils.TAGPUBLIC + BCDASCII.class.getSimpleName();

    /** 
     * 字母'A'的ASCII编码值 
     */  
    public final static byte ALPHA_A_ASCII_VALUE = 0x41;  
  
    /** 
     * 字母'a'的ASCII编码值 
     */  
    public final static byte ALPHA_a_ASCII_VALUE = 0x61;  
  
    /** 
     * 数字'0'的ASCII编码值 
     */  
    public final static byte DIGITAL_0_ASCII_VALUE = 0x30;  
  
    private BCDASCII() {}

    /**
     * Converts a hex String to a byte array.
     *
     * @param s A string of hexadecimal characters, must be an even number of
     *          chars long
     *
     * @return byte array representation
     *
     * @throws RuntimeException on invalid format
     */
    public static byte[] hexStringToBytes(String s) {
        byte[] ret;

        if (s == null) return null;

        int sz = s.length();

        char c;
        for (int i = 0; i < sz; ++i) {
            c = s.charAt(i);
            if (!((c >= '0') && (c <= '9'))
                    && !((c >= 'A') && (c <= 'F'))
                    && !((c >= 'a') && (c <= 'f'))) {
                s = s.replaceAll("[^[0-9][A-F][a-f]]", "");
                sz = s.length();
                break;
            }
        }

        ret = new byte[sz/2];

        for (int i=0 ; i <sz ; i+=2) {
            ret[i/2] = (byte) ((hexCharToInt(s.charAt(i)) << 4)
                    | hexCharToInt(s.charAt(i+1)));
        }

        return ret;
    }


    /**
     * Converts a byte array into a String of hexadecimal characters.
     *
     * @param bytes an array of bytes
     *
     * @return hex string representation of bytes array
     */
    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) return null;

        StringBuilder ret = new StringBuilder(2*bytes.length);

        for (int i = 0 ; i < bytes.length ; i++) {
            int b;

            b = 0x0f & (bytes[i] >> 4);
            ret.append("0123456789abcdef".charAt(b));

            b = 0x0f & bytes[i];
            ret.append("0123456789abcdef".charAt(b));
        }

        return ret.toString().toUpperCase();
    }

    public static String bytesToHexString(byte[] bytes, int len) {
        if (bytes == null) return null;

        StringBuilder ret = new StringBuilder(len*2);	//2*bytes.length);

        for (int i = 0 ; i < len; i++) {
            int b;

            b = 0x0f & (bytes[i] >> 4);
            ret.append("0123456789abcdef".charAt(b));

            b = 0x0f & bytes[i];
            ret.append("0123456789abcdef".charAt(b));
        }

        return ret.toString().toUpperCase();
    }

    static int hexCharToInt(char c) {
        if (c >= '0' && c <= '9') return (c - '0');
        if (c >= 'A' && c <= 'F') return (c - 'A' + 10);
        if (c >= 'a' && c <= 'f') return (c - 'a' + 10);

        throw new RuntimeException ("invalid hex char '" + c + "'");
    }

    //byte值转成INT值
    public static int byte2int(byte val){
        return (val >= 0 ? val : (val + 256));
    }

    /**
     * ASCII码byte数组转为字符串
     * 方法名：byteArray2String
     * 描 述：aaa
     *
     * @param data
     * @return String 日 期：2014年10月13日 by lijinniu
     */
    public static String asciiByteArray2String(byte[] data) {
        if (data == null) return "";
        StringBuffer tStringBuf = new StringBuffer();
        char[] tChars = new char[data.length];

        int end = 0;
        for (int i = 0; i < data.length; i++) {
            end = data.length;
            tChars[i] = (char) data[i];
        }

        tStringBuf.append(tChars, 0, end);

        return tStringBuf.toString().trim();
    }

    public static String fromBCDToASCIIString(byte[] bcdBuf, int bcdOffset, int asciiLen, boolean rightAlignFlag) {
        try {
            return new String(fromBCDToASCII(bcdBuf, bcdOffset, asciiLen, rightAlignFlag), "GBK");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
  
    /** 
     * 从BCD编码转换成ASCII编码. 
     * @param bcdBuf, BCD编码缓冲区 
     * @param asciiLen, 统一采用ASCII编码时的信息长度 
     * @param rightAlignFlag, 奇数个ASCII码时采用的右对齐方式标志 
     * @return, ASCII编码缓冲区 
     */  
    public static byte[] fromBCDToASCII(byte[] bcdBuf, int bcdOffset, int asciiLen, boolean rightAlignFlag) {  
        byte[] asciiBuf = new byte[asciiLen];  
        fromBCDToASCII(bcdBuf, bcdOffset, asciiBuf, 0, asciiLen, rightAlignFlag);  
  
        return asciiBuf;  
    }

    /**
     * 从BCD编码转换成ASCII编码.
     * @param bcdBuf, BCD编码缓冲区
     * @param bcdOffset, BCD编码缓冲区起始偏移
     * @param asciiBuf, ASCII编码缓冲区
     * @param asciiOffset, ASCII编码缓冲区的起始偏移
     * @param asciiLen, 采用ASCII编码时的信息长度
     * @param rightAlignFlag, 奇数个ASCII码时采用的右对齐方式标志
     * @return, ASCII编码缓冲区
     */
    public static void fromBCDToASCII(byte[] bcdBuf, int bcdOffset, byte[] asciiBuf, int asciiOffset, int asciiLen,
                                      boolean rightAlignFlag) {
        int cnt;

        if (((asciiLen & 1) == 1) && rightAlignFlag) {
            cnt = 1;
            asciiLen++;
        } else {
            cnt = 0;
        }

        for (; cnt < asciiLen; cnt++, asciiOffset++) {
            asciiBuf[asciiOffset] = (byte) ((((cnt) & 1) == 1) ? (bcdBuf[bcdOffset++] & 0x0f)
                    : ((bcdBuf[bcdOffset] >> 4) & 0x0f));
            asciiBuf[asciiOffset] = (byte) (asciiBuf[asciiOffset] + ((asciiBuf[asciiOffset] > 9) ? (ALPHA_A_ASCII_VALUE - 10)
                    : DIGITAL_0_ASCII_VALUE));
        }

        //Log.d(TAG, "BCD  ="+BCDHelper.bcdToString(bcdBuf, 0, asciiLen/2));
        //Log.d(TAG, "ASCII="+new String(asciiBuf));
    }

    public static void fromASCIIToBCD(String asciiStr, int asciiOffset, int asciiLen, byte[] bcdBuf, int bcdOffset,
                                      boolean rightAlignFlag) {
        try {
            byte[] asciiBuf = asciiStr.getBytes("GBK");
            fromASCIIToBCD(asciiBuf, asciiOffset, asciiLen, bcdBuf, bcdOffset, rightAlignFlag);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
  
    /** 
     * 从ASCII编码转换成BCD编码. 
     * @param asciiBuf, ASCII编码缓冲区 
     * @param asciiOffset, ASCII编码缓冲区的起始偏移 
     * @param asciiLen, 采用ASCII编码时的信息长度 
     * @param bcdBuf, BCD编码缓冲区 
     * @param bcdOffset, BCD编码缓冲区起始偏移 
     * @param rightAlignFlag, 奇数个ASCII码时采用的右对齐方式标志 
     */  
    public static void fromASCIIToBCD(byte[] asciiBuf, int asciiOffset, int asciiLen, byte[] bcdBuf, int bcdOffset,  
            boolean rightAlignFlag) {  
        int cnt;  
        byte ch, ch1;  
  
        if (((asciiLen & 1) == 1) && rightAlignFlag) {  
            ch1 = 0;  
        } else {  
            ch1 = 0x55;  
        }  
  
        for (cnt = 0; cnt < asciiLen; cnt++, asciiOffset++) {  
            if (asciiBuf[asciiOffset] >= ALPHA_a_ASCII_VALUE)  
                ch = (byte) (asciiBuf[asciiOffset] - ALPHA_a_ASCII_VALUE + 10);  
            else if (asciiBuf[asciiOffset] >= ALPHA_A_ASCII_VALUE)  
                ch = (byte) (asciiBuf[asciiOffset] - ALPHA_A_ASCII_VALUE + 10);  
            else if (asciiBuf[asciiOffset] >= DIGITAL_0_ASCII_VALUE)  
                ch = (byte) (asciiBuf[asciiOffset] - DIGITAL_0_ASCII_VALUE);  
            else  
                ch = 0x00;  
  
            if (ch1 == 0x55)  
                ch1 = ch;  
            else {  
                bcdBuf[bcdOffset] = (byte) (ch1 << 4 | ch);  
                bcdOffset++;  
                ch1 = 0x55;  
            }  
        }  
  
        if (ch1 != 0x55)  
            bcdBuf[bcdOffset] = (byte) (ch1 << 4);  
    }

    public static byte[] fromASCIIToBCD(String asciiStr, int asciiOffset, int asciiLen, boolean rightAlignFlag) {
        try {
            byte[] asciiBuf = asciiStr.getBytes("GBK");
            return fromASCIIToBCD(asciiBuf, asciiOffset, asciiLen, rightAlignFlag);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
      
    /** 
     * 从ASCII编码转换成BCD编码. 
     * @param asciiBuf, ASCII编码缓冲区 
     * @param asciiOffset, ASCII编码缓冲区的起始偏移 
     * @param asciiLen, 统一采用ASCII编码时的信息长度 
     * @param rightAlignFlag, 奇数个ASCII码时采用的右对齐方式标志 
     * @return, BCD编码缓冲区 
     */  
    public static byte[] fromASCIIToBCD(byte[] asciiBuf, int asciiOffset, int asciiLen, boolean rightAlignFlag) {  
        byte[] bcdBuf = new byte[(asciiLen + 1) / 2];  
        fromASCIIToBCD(asciiBuf, asciiOffset, asciiLen, bcdBuf, 0, rightAlignFlag);  
  
        return bcdBuf;  
    }
}
