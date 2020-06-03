package com.example.halalah.packet;

import android.util.Log;

import com.example.halalah.Utils;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.iso8583.FrontHeader;
import com.example.halalah.iso8583.ISO8583;
import com.example.halalah.iso8583.ISO8583Util;
import com.example.halalah.iso8583.MessageHeader;
import com.example.halalah.iso8583.TPDU;
import com.example.halalah.secure.EncryptControl;

import java.util.Arrays;

import static com.example.halalah.iso8583.ISO8583.LLLVAR_LEN;
import static com.example.halalah.iso8583.ISO8583.LLVAR_LEN;

public class PackUtils {
    private static final String TAG = Utils.TAGPUBLIC + PackUtils.class.getSimpleName();

    //MOSTAFA : 10/5/2020 ADDED MADA MSGID
    public static final String  MSGTYPEID_Authorisation_Request = "1100";
    public static final String  MSGTYPEID_Authorisation_Advice ="1120";
    public static final String  MSGTYPEID_Authorisation_Advice_Repeat="1121";
    public static final String  MSGTYPEID_Financial_Request="1200";
    public static final String  MSGTYPEID_Financial_Advice="1220";
    public static final String  MSGTYPEID_Financial_Advice_Repeat="1221";
    public static final String  MSGTYPEID_File_Action_Request="1304";
    public static final String  MSGTYPEID_File_Action_Request_Repeat="1305";
    public static final String  MSGTYPEID_Reversal_Advice="1420";
    public static final String  MSGTYPEID_Reversal_Advice_Repeat="1421";
    public static final String  MSGTYPEID_Terminal_Reconciliation_Advice="1524";
    public static final String  MSGTYPEID_Terminal_Reconciliation_Advice_Repeat="1525";
    public static final String MSGTYPEID_Network_Management_Request="1804";



//mostafa : todo packet header

    protected static byte[] getPacketHeader(byte[] isobyte, String termId, String merId) {
        //TPDU
        Log.d(TAG, "TPDU=" + BCDASCII.bytesToHexString(TPDU.get()));
        //Header
        MessageHeader.setAppType((byte) 0x80);
        MessageHeader.setTermStat((byte) 0x00);
        MessageHeader.setProcCode((byte) 0x00);
        Log.d(TAG, "MessageHeader=" + BCDASCII.bytesToHexString(MessageHeader.get()));
        //Front
        FrontHeader.setLen(isobyte.length);
        Log.d(TAG, "FrontHeader=" + BCDASCII.bytesToHexString(FrontHeader.get(termId, merId)) + " , ascii(" + new String(FrontHeader.get(termId, merId)) + ")");

        String cs = String.format("%04X", ISO8583Util.byteArrayAdd(isobyte, ISO8583Util.byteArrayAdd(TPDU.get(), MessageHeader.get(), FrontHeader.get(termId, merId))).length);
        byte[] bcdlen = BCDASCII.hexStringToBytes(cs);

        return ISO8583Util.byteArrayAdd(bcdlen, ISO8583Util.byteArrayAdd(TPDU.get(), MessageHeader.get(), FrontHeader.get(termId, merId)), isobyte);
    }

    public static String getField4(String amountStr) {
        Log.i(TAG, "begin amount: " + amountStr);
        int index = amountStr.indexOf(".");
        if (amountStr.substring(index + 1, amountStr.length()).length() < 2) {
            amountStr = amountStr + "0";
        }
        amountStr = amountStr.replace(".", "");
        int amtlen = amountStr.length();
        StringBuilder amtBuilder = new StringBuilder();
        if (amtlen < 12) {
            for (int i = 0; i < (12 - amtlen); i++) {
                amtBuilder.append("0");
            }
        }
        amtBuilder.append(amountStr);
        amountStr = amtBuilder.toString();
        Log.i(TAG, "begin amount: " + amountStr);
        return amountStr;
    }

    public static byte[] getTrackField(String data, int lenType) {
        Log.i(TAG, "getTrackField(), data = " + data + ", lenType = " + lenType);
        byte[] lenData = null;
        byte[] byteData = null;

        byte[] inputData = null;
        byte[] outputDate = null;

        int lenReal;

        if (lenType == LLVAR_LEN) {
            lenData = BCDASCII.hexStringToBytes(String.format("%2d", data.length()));
        } else if (lenType == LLLVAR_LEN) {
            lenData = BCDASCII.hexStringToBytes(String.format("%4d", data.length()));
        }
        Log.i(TAG, "lenData = "+BCDASCII.bytesToHexString(lenData));

        if (data.length() % 2 != 0) {
            byteData = BCDASCII.hexStringToBytes(data+"0");
        } else {
            byteData = BCDASCII.hexStringToBytes(data);
        }
        Log.i(TAG, "byteData = "+BCDASCII.bytesToHexString(byteData));

        lenReal = (lenData.length + byteData.length) + 8 - (lenData.length + byteData.length) % 8 ;
        inputData = new byte[lenReal];
        Arrays.fill(inputData, (byte) 0x00);
        System.arraycopy(lenData, 0, inputData, 0, lenData.length);
        System.arraycopy(byteData, 0, inputData, lenData.length, byteData.length);
        Log.i(TAG, "inputData = "+BCDASCII.bytesToHexString(inputData));

        outputDate = new byte[lenReal];
        try {
            EncryptControl encryptControl = new EncryptControl();
            int result = encryptControl.CalculateEncryptByTDK(inputData, outputDate);
            if (result != 0) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "outputDate = "+BCDASCII.bytesToHexString(outputDate));

        return outputDate;
    }

	protected static byte[] getField64(ISO8583 iso8583) {
		byte[] nulldata = new byte[8];
        Arrays.fill(nulldata, (byte) 0x00);
		iso8583.SetDataElement(64, nulldata, nulldata.length);
		byte[] mac = new byte[8];
        EncryptControl encryptControl = new EncryptControl();
        encryptControl.CalculateMac(iso8583.isotostr(), iso8583.isotostr().length, mac);
        return mac;
	}

    public static String getfixedNumber(int number , int len, String fixChar) {
        String fixedNum = "";
        String num = Integer.toString(number);
        int numLen = num.length();
        while (numLen < len) {
            fixedNum += fixChar;
            numLen++;
        }
        fixedNum = fixedNum + number;
        return fixedNum;
    }
}
