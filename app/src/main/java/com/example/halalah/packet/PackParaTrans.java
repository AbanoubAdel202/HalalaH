package com.example.halalah.packet;

import android.util.Log;

import com.example.halalah.Utils;
import com.example.halalah.iso8583.ISO8583;

public class PackParaTrans {
	private static final String TAG = PackParaTrans.class.getSimpleName();

	private ISO8583 mISO8583;
	private byte[] mPacketMsg = null;
	
	public PackParaTrans(String termId, String merId) {
		Log.i(TAG, "PackParaTrans()");
		mISO8583   = new ISO8583();
        mISO8583.ClearFields();

		//todo mostafa ADD TMS download message
        byte[] isobyte = mISO8583.isotostr();
        mPacketMsg = PackUtils.getPacketHeader(isobyte, termId, merId);
	}
	
	public byte[] get()
	{
		return mPacketMsg;
	}

	private byte[] getField46(String f46) {
		if(f46.length() > 0) {
			byte[] tmpb = new byte[3 + f46.length()];
			tmpb[0] = (byte) 0x8F;
			tmpb[1] = 0x09;
			tmpb[2] = (byte) f46.length();

			System.arraycopy(f46.getBytes(), 0, tmpb, 3, f46.length());
			return tmpb;
		}
		return null;
	}
}
