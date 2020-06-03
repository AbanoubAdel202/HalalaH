package com.example.halalah.secure;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {
	private final static String TAG = Md5.class.getSimpleName();
	
	public static String gen(byte[] bcdval) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(bcdval);
		byte[] m = md5.digest();
		try {
			String sb= new String(m, "utf-8");
			Log.e(TAG, "digest bcd="+sb);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return SecureUtils.getString(m);
	}
	
	public static String gen(String val) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(val.getBytes());
		byte[] m = md5.digest();
		try {
			String sb= new String(m, "utf-8");
			Log.e(TAG, "digest asc="+SecureUtils.getString(m));	//BCDHelper.hex2DebugHexString(m, m.length));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return (SecureUtils.getString(m));
	}
}
