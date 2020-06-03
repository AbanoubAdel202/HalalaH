package com.example.halalah.secure;

import android.util.Log;

import com.example.halalah.Utils;
//import com.example.halalah.device.Ped;
import com.example.halalah.iso8583.BCDASCII;

/**
 * @author zhanghw
 *
 */
public class GenMac {
	private String TAG = Utils.TAGPUBLIC + GenMac.class.getSimpleName();

	/**for test
	private String inputdata = "0200202004C020C098113100000000010220001224CF3EB03BE436D90E4F57B515043BF79963425C5A27CA2ACB3130303535343737333030343430313339393830303231313536363837303738393626000000000000000019010000010005000000000000000000000000";
	byte[] bcddata = BCDHelper.stringToBcd(inputdata, inputdata.length());
	private String inputkey = "029EF2B06B3BF88F";
	byte[] bcdkey = BCDHelper.stringToBcd(inputkey, inputkey.length());
	//Mac = 4134354545363345
	**/
	public GenMac(byte[] mac, byte[] data, int datalen, byte[] key) {

		byte[] output= new byte[8];
		try {
	//		Ped.getInstance().CalculateMac(data, output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i(TAG, "mactemp : "+BCDASCII.bytesToHexString(output));

		System.arraycopy(output, 0, mac, 0, 8);
	}
}
