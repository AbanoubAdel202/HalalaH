package com.example.halalah.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.halalah.Utils;

public class MerchantInfo {
    private static final String TAG = Utils.TAGPUBLIC + MerchantInfo.class.getSimpleName();

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public MerchantInfo(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences("merchant_info", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void setMerchantName(String merchantName) {
        Log.i(TAG, "setMerchantName  "+merchantName);
        mEditor.putString(MerchantUtils.MERCHANT_NAME, merchantName);
        mEditor.commit();
    }

    public String getMerchantName() {
        Log.i(TAG, "getMerchantName  "+mSharedPreferences.getString(MerchantUtils.MERCHANT_NAME, null));
        return mSharedPreferences.getString(MerchantUtils.MERCHANT_NAME, MerchantUtils.merName_default);
    }

    public void setMerchantId(String merchantId) {
        Log.i(TAG, "setMerchantId  "+merchantId);
        mEditor.putString(MerchantUtils.MERCHANT_ID, merchantId);
        mEditor.commit();
    }

    public String getMerchantId() {
        Log.i(TAG, "getMerchantId  "+mSharedPreferences.getString(MerchantUtils.MERCHANT_ID, null));
        return mSharedPreferences.getString(MerchantUtils.MERCHANT_ID, MerchantUtils.merId_default);
    }

    public void setTermId(String termId) {
        Log.i(TAG, "setTermId  "+termId);
        mEditor.putString(MerchantUtils.TERM_ID, termId);
        mEditor.commit();
    }

    public String getTermId() {
        Log.i(TAG, "getTermId  "+mSharedPreferences.getString(MerchantUtils.TERM_ID, null));
        return mSharedPreferences.getString(MerchantUtils.TERM_ID, MerchantUtils.termId_default);
    }


    public void setTradNum(String tradNum) {
        Log.i(TAG, "setTradNum  "+tradNum);
        mEditor.putString(MerchantUtils.TRAD_NUMBER, tradNum);
        mEditor.commit();
    }

    public String getTradNum(boolean isGrowth) {
        Log.i(TAG, "getTradNum");
        String tradNum = mSharedPreferences.getString(MerchantUtils.TRAD_NUMBER, MerchantUtils.TEST_begin_value);
        Log.i(TAG, "tradNum: "+tradNum);

        if (isGrowth) {
            int snNum = Integer.parseInt(tradNum);
            Log.d(TAG, "snNum: " + snNum);
            String temp;
            if (snNum >= 999999){
                temp = String.valueOf(1);
            }else {
                temp = String.valueOf(snNum + 1);
            }
            StringBuilder str = new StringBuilder();
            if (temp.length() < 6) {
                for (int i = 0; i < (6 - temp.length()); i++) {
                    str.append("0");
                }
            }
            str.append(temp);
            Log.d(TAG, "str.toString(): " + str.toString());
            setTradNum(str.toString());
        }
        return tradNum;
    }
}