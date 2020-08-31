package com.example.halalah.packet;

import android.util.Log;

import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.iso8583.ISO8583;
import com.example.halalah.util.ResultsBean;
import com.example.halalah.util.ResultsQuery;
import com.example.halalah.util.TLVDecode;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;

public class UnpackUtils {
	private static String TAG = UnpackUtils.class.getSimpleName();
	/*
	 * 解析TPDU Header以及Front data
	 */
	public ISO8583 UnpackFront(byte[] srcData, int srcDataLen){
		
		int srcDataOffset = 0;
	
		if (srcData==null || srcDataLen<=0) {
			return null;
		}
		
		//packet len
		int packetlen = 0;
		String lenSrc = BCDASCII.fromBCDToASCIIString(srcData, srcDataOffset, 4, false);
		Log.d(TAG, "lenSrc = "+lenSrc);
        packetlen = Integer.parseInt(lenSrc, 16);
        Log.d(TAG, "Packet Len = "+packetlen);
        

        int length = srcDataLen - srcDataOffset;
        byte[] data8583 = new byte[length];
        System.arraycopy(srcData, srcDataOffset, data8583, 0, length);
       // data8583="313131303732333030303131304543323838303131363438343738333530313034373439313230303030303030303030303039303030383031323133303433383433303030323632313831323133303733383433313831323133303635383838343930373338343330303032363234373439313230303034373030303132333031343930313233303130313132333435363738202020303036534149425031363832313347FFF00111100000000536303904C3C1".getBytes();
        ISO8583 mIso = new ISO8583();

        mIso.strtoiso(data8583);
      //  mIso.strtoiso_original(data8583);

		return mIso;
	}

    protected String processField46(byte[] field46, String response) {

        String showInformation = null;

        ResultsBean resultsBean = ResultsQuery.getInstance().getResultsBean(String.valueOf(response));
        if(field46 == null) {
            if (!response.equals("00")) {
                showInformation = resultsBean.getShow();
            }
            return showInformation;
        }

        Log.i(TAG, "processField46, field46: "+BCDASCII.bytesToHexString(field46));

        boolean isTag8F01 = false;
        boolean isTag5F51 = false;

        byte[] value8F01 = null;
        byte[] value5F51 = null;

        LinkedHashMap<byte[], byte[]> tlv46 = TLVDecode.getDecodeTLV(field46);

        for(byte[] key : tlv46.keySet()) {
            String tag = BCDASCII.bytesToHexString(key);
            Log.i(TAG,"processField46, tag:"+tag);

            switch (tag) {
                case "8F01":
                    isTag8F01 = true;
                    value8F01 = tlv46.get(key);
                    break;
                case "5F51":
                    isTag5F51 = true;
                    value5F51 = tlv46.get(key);
                    break;
                default:
                    break;
            }
        }

        try {
            if (isTag8F01) {
                showInformation = new String(value8F01, "GBK");
            } else {
                if (response.equals("00")) {
                    showInformation = new String(field46, "GBK");
                } else {
                    if (isTag5F51 && (value5F51.length > 0 && value5F51.length < 48)) {
                        showInformation = new String(value5F51, "GBK");
                        Log.i(TAG, "showInformation: "+showInformation);
                    } else {
                        resultsBean = ResultsQuery.getInstance().getResultsBean(String.valueOf(response));
                        showInformation = resultsBean.getShow();
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "isTag8F01 : "+isTag8F01);
        Log.i(TAG, "isTag5F51 : "+isTag5F51);
        Log.i(TAG, "information : "+showInformation);

        return showInformation;
    }
}
