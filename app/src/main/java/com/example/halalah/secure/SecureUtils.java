package com.example.halalah.secure;
/**
 * 加解密辅助的功能
 */

import android.util.Log;

import com.example.halalah.iso8583.BCDASCII;

import java.security.NoSuchAlgorithmException;

public class SecureUtils {
	private static final String TAG = SecureUtils.class.getSimpleName();
	
	
	public static String getString(byte[] b) {
		int offset= 0;
		int  len  = b.length;
        if  (b.length % 2 != 0)
            len++;

        StringBuffer sb = new  StringBuffer();
        for  (int  i = 0; i < len; i++) {
            sb.append(Integer.toHexString((b[i + offset] & 0xf0) >> 4));
            sb.append(Integer.toHexString(b[i + offset] & 0xf));
        }
        return  sb.toString().toUpperCase();
	}
	
	private  byte [] StrToBCD(String str, int  numlen) {
        if  (numlen % 2 != 0)
            numlen++;

        while  (str.length() < numlen) {
            str = "0" + str;  //鍓嶅琛モ�00鈥�
        }

        byte [] bStr = new  byte [str.length() / 2];
        char [] cs = str.toCharArray();
        int  i     = 0;
        int  iNum  = 0;
        for  (i = 0; i < cs.length; i += 2) {

            int  iTemp = 0;
            if  (cs[i] >= '0' && cs[i] <= '9') {
                iTemp = (cs[i] - '0') << 4;
            } else  {
                //  鍒ゆ柇鏄惁涓篴~f 
                if  (cs[i] >= 'a' && cs[i] <= 'f') {
                    cs[i] -= 32;
                }
                iTemp = (cs[i] - '0' - 7) << 4;
            }
            //  澶勭悊浣庝綅 
            if  (cs[i + 1] >= '0' && cs[i + 1] <= '9') {
                iTemp += cs[i + 1] - '0';
            } else  {
                //  鍒ゆ柇鏄惁涓篴~f 
                if  (cs[i + 1] >= 'a' && cs[i + 1] <= 'f') {
                    cs[i + 1] -= 32;
                }
                iTemp += cs[i + 1] - '0' - 7;
            }
            bStr[iNum] = (byte ) iTemp;
            iNum++;
        }
        return  bStr;
    }
	
	////////////////////////////////////////////////
	// XOR 
	////////////////////////////////////////////////
	//两个串进行异或
	public static byte[] XOR(byte[] src1, int offset1, byte[] src2, int offset2, int len){
		byte [] result=new byte[len];
		int i=0;
		int src1len = src1.length, src2len = src2.length;
		for (i=0; i<len; i++) {
			if (i+offset1>=src1len || i+offset2>=src2len) break;
			result[i] = (byte) (src1[i+offset1] ^ src2[i+offset2]);
		}
		return result;
	}
	
	//两个串进行异或
	public static byte[] XOR(byte[] src1, int offset1, byte[] src2, int len){
		return XOR(src1, offset1, src2, 0, len);
	}

	//两个串进行异或
	public static byte[] XOR(byte[] src1, byte[] src2, int offset2, int len){
		return XOR(src1, src2, offset2, len);
	}
	
	/**
     * 异或二串数据
     * @param src1
     * @param src2
     * @param len 参与异或的内容字节数
     * @return
     */
	public static byte[] XOR(byte[] src1, byte[] src2, int len){
		return XOR(src1, 0, src2, 0, len);
	}

	
	//两个串进行异或
	public static String XOR(String src1, String src2){
		return getString(XOR(BCDASCII.hexStringToBytes(src1), 0, BCDASCII.hexStringToBytes(src2), 0, src1.length()/2));
	}
	
	
	////////////////////////////////////////////////////////////
	/**
	 * 通联应用，取第一个参与XOR运算的串,BCD
	 * @return
	 */
	public static byte[] GetXOR1Val()
	{
		return BCDASCII.hexStringToBytes("CDA8C1AAD0C2D0CB");
	}
	
	/**
	 * 通联应用，取第二个参与XOR运算的串,BCD
	 * @return
	 */
	public static byte[] GetXOR2Val()
	{
		return BCDASCII.hexStringToBytes("D6A7B8B6CEDED3C7");
	}
	/**
	 * 进行TID转换,通联项目
	 * @param stid,8个字节的终端号
	 * @param ttek
	 * @param md5pwd
	 * @param xorvalue
	 * @return
	 */
	public static byte[] getXTID(String stid, byte[] xorvalue) {
		byte[] result = new byte[8];
		byte[] tid_1  = new byte[8];
		byte[] tid_2  = new byte[8];
		int i = 0;
		
		System.arraycopy(stid.getBytes(), 4, tid_1, 0, 4);
		System.arraycopy(stid.getBytes(), 0, tid_1, 4, 4);
		tid_2 = XOR(tid_1, xorvalue, 8);
		
		//Log.d(TAG, "TID_1 = "+BCDHelper.bcdToString(tid_1, 0, 8));
		//Log.d(TAG, "TID_1 xor Val1 = " + BCDHelper.bcdToString(tid_2, 0, 8));
		
		for (i=0; i<8; i++){
			byte val  = (byte) tid_2[i];
			int  ival = BCDASCII.byte2int(val);//转成无符号的值
			byte vmod = (byte) (ival % 9);
			byte vdiv = (byte) (ival / 9);
			
			result[i] = (byte) (vmod ^ vdiv);
		}
		Log.d(TAG, "XTid = "+BCDASCII.bytesToHexString(result));
		
		return (result);
	}
	public static byte[] getXTID(byte[] btid, byte[] xorvalue) {
		byte[] result = new byte[8];
		byte[] tid_1  = new byte[8];
		byte[] tid_2  = new byte[8];
		int i = 0;
		
		System.arraycopy(btid, 4, tid_1, 0, 4);
		System.arraycopy(btid, 0, tid_1, 4, 4);
		tid_2 = XOR(tid_1, xorvalue, 8);
		
		//Log.d(TAG, "TID_1 = "+BCDHelper.bcdToString(tid_1, 0, 8));
		//Log.d(TAG, "TID_1 xor Val1 = " + BCDHelper.bcdToString(tid_2, 0, 8));
		
		for (i=0; i<8; i++){
			byte val  = (byte) tid_2[i];
			int  ival = BCDASCII.byte2int(val);//转成无符号的值
			byte vmod = (byte) (ival % 9);
			byte vdiv = (byte) (ival / 9);
			
			result[i] = (byte) (vmod ^ vdiv);
		}
		Log.d(TAG, "XTid = "+BCDASCII.bytesToHexString(result));
		
		return (result);
	}
	
	
	//计算TTEK
	public static byte[] getTTEK(String stid, String pwd) {
		byte[] result   = new byte[16];
		byte[] xtid     = new byte[16];
		byte[] xor1value= new byte[8];
		byte[] xor2value= new byte[8];
		byte[] md5pwd   = new byte[16];
		
		//xor1value = BCDHelper.stringToBcd("CDA8C1AAD0C2D0CB", 16);
		//xor2value = BCDHelper.stringToBcd("D6A7B8B6CEDED3C7", 16);
		System.arraycopy(GetXOR1Val(), 0, xor1value, 0, 8);
		System.arraycopy(GetXOR2Val(), 0, xor2value, 0, 8);
		try {
        	String md5result = Md5.gen(pwd);
        	md5pwd = BCDASCII.hexStringToBytes(md5result);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		System.arraycopy(getXTID(stid, xor1value), 0, xtid, 0, 8);
		System.arraycopy(xor2value, 0, xtid, 8, 8);
		result = XOR(xtid, md5pwd, 16);
		
		//Log.d(TAG, "xtid xor Val2 = "+BCDHelper.bcdToString(xtid, 0, 16));
		//Log.d(TAG, "md5pwd= "+BCDHelper.bcdToString(md5pwd, 0, 16));
		Log.d(TAG, "TTEK  = "+BCDASCII.bytesToHexString(result));
		
		return result;
	}
}
