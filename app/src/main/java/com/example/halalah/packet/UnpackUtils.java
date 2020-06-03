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
        
        //TPDU
        srcDataOffset = 2;
        String tmpstr = BCDASCII.fromBCDToASCIIString(srcData, srcDataOffset, 10, false);
        Log.d(TAG, "TPDU = "+tmpstr);
        
        
        //Header
        srcDataOffset = 2+5;
        tmpstr = BCDASCII.fromBCDToASCIIString(srcData, srcDataOffset, 12, false);
        Log.d(TAG, "Header = "+tmpstr);

        //front data
        srcDataOffset = 2+5+6;
        tmpstr = new String(srcData, srcDataOffset, 15);
        Log.d(TAG, "Mid = "+tmpstr);
        
        srcDataOffset = 2+5+6+15;
        tmpstr = new String(srcData, srcDataOffset, 8);
        Log.d(TAG, "Tid = "+tmpstr);
        
        srcDataOffset = 2+5+6+15+8;
        tmpstr = new String(srcData, srcDataOffset, 15);
        Log.d(TAG, "Mname = "+tmpstr);
        
        srcDataOffset = 2+5+6+15+8+15;
        tmpstr = new String(srcData, srcDataOffset, 8);
        Log.d(TAG, "BType = "+tmpstr);
        
        srcDataOffset = 2+5+6+15+8+15+8;
        tmpstr = new String(srcData, srcDataOffset, 15);
        Log.d(TAG, "Mid = "+tmpstr);

        srcDataOffset = 2+5+6+15+8+15+8+15;
        
        tmpstr = new String(srcData, srcDataOffset, 8);
        Log.d(TAG, "8583Len = "+tmpstr);

        // unpack 8583 packet
        srcDataOffset = 2 + 5 + 6 + 15 + 8 + 15 + 8 + 15 + 8;
        int length = srcDataLen - srcDataOffset;
        byte[] data8583 = new byte[length];
        System.arraycopy(srcData, srcDataOffset, data8583, 0, length);
        ISO8583 mIso = new ISO8583();
        mIso.strtoiso(data8583);
		
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
