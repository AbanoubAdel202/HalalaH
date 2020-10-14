package com.example.halalah.packet;

import android.util.Log;

import com.example.halalah.Utils;


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
