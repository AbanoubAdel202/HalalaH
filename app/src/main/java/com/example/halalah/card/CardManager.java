package com.example.halalah.card;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.halalah.Utils;
import com.example.halalah.card.CardMoniterService;

/**
 * Created by zhoulin on 17-9-19.
 */

public class CardManager {
    private static String TAG = Utils.TAGPUBLIC + CardManager.class.getSimpleName();
    private static CardManager sInstance = new CardManager();
    private CardResultCallBack resultCallBack;
    private CardExceptionCallBack exceptionCallBack;

    public static CardManager getInstance() {
        return sInstance;
    }

    public void initCardResultCallBack(CardResultCallBack callBack) {
        resultCallBack = callBack;
    }

    public void initCardExceptionCallBack(CardExceptionCallBack callBack) {
        exceptionCallBack = callBack;
    }

    /**
     * 消费金额
     * @param amount
     */
    public void setConsumeAmount(String amount) {
        if (null == resultCallBack) {
            Log.d(TAG, "setConsumeAmount callBack is null");
            return;
        }
        resultCallBack.consumeAmount(amount);
    }

    /**
     * 多应用选择
     * @param index
     */
    public void setAidSelect(int index) {
        if (null == resultCallBack) {
            Log.d(TAG, "setAidSelect callBack is null");
            return;
        }
        resultCallBack.aidSelect(index);
    }

    /**
     * 确认是否使用电子现金
     * @param confirm
     */
    public void setEcashTipsConfirm(boolean confirm) {
        if (null == resultCallBack) {
            Log.d(TAG, "setEcashTipsConfirm callBack is null");
            return;
        }
        resultCallBack.eCashTipsConfirm(confirm);
    }

    /**
     * 确认卡信息
     * @param confirm
     */
    public void setConfirmCardInfo(boolean confirm) {
        if (null == resultCallBack) {
            Log.d(TAG, "setConfirmCardInfo callBack is null");
            return;
        }
        resultCallBack.confirmCardInfo(confirm);
    }

    public void setImportPin(String pin) {
        if (null == resultCallBack) {
            Log.d(TAG, "setConfirmCardInfo callBack is null");
            return;
        }
        resultCallBack.importPin(pin);
    }

    /**
     * 身份认证
     * @param auth
     */
    public void setUserAuth(boolean auth) {
        if (null == resultCallBack) {
            Log.d(TAG, "setUserAuth callBack is null");
            return;
        }
        resultCallBack.userAuth(auth);
    }

    /**
     * 联机
     * @param online
     * @param respCode
     * @param icc55
     */
    public void setRequestOnline(boolean online, String respCode, String icc55) {
        if (null == resultCallBack) {
            Log.d(TAG, "setRequestOnline callBack is null");
            return;
        }
        resultCallBack.requestOnline(online, respCode, icc55);
    }

    public interface CardResultCallBack {
        void consumeAmount(String amount);      //消费金额
        void aidSelect(int index);              //多应用选择
        void eCashTipsConfirm(boolean confirm); //否使用电子现金
        void confirmCardInfo(boolean confirm);  //确认卡信息
        void importPin(String pin);             //密码
        void userAuth(boolean auth);            //身份认证
        void requestOnline(boolean online, String respCode, String icc55); //request online
    }

    /**
     * 回调超时
     */
    public void callBackTimeOut() {
        if (null == exceptionCallBack) {
            Log.d(TAG, "callBackTimeOut callBack is null");
            return;
        }
        exceptionCallBack.callBackTimeOut();
    }

    /**
     * 回调出错
     * @param errorCode
     */
    public void callBackError(int errorCode) {
        if (null == exceptionCallBack) {
            Log.d(TAG, "callBackError callBack is null");
            return;
        }
        exceptionCallBack.callBackError(errorCode);
    }

    /**
     * 回调取消
     */
    public void callBackCanceled() {
        if (null == exceptionCallBack) {
            Log.d(TAG, "callBackCanceled callBack is null");
            return;
        }
        exceptionCallBack.callBackCanceled();
    }

    /**
     * 交易结果
     * @param result
     */
    public void callBackTransResult (int result) {
        if (null == exceptionCallBack) {
            Log.d(TAG, "callBackTransResult callBack is null");
            return;
        }
        exceptionCallBack.callBackTransResult(result);
    }

    /**
     * 关闭上一个界面
     */
    public void finishPreActivity () {
        if (null == exceptionCallBack) {
            Log.d(TAG, "finishPreActivity callBack is null");
            return;
        }
        exceptionCallBack.finishPreActivity();
    }

    public interface CardExceptionCallBack {
        void callBackTimeOut();             //回调超时
        void callBackError(int errorCode);  //回调出错
        void callBackCanceled();            //回调取消
        void callBackTransResult(int result);
        void finishPreActivity();
    }

    public void startCardDealService(Context mContext) {
        Log.d(TAG, "startCardDealService");
        Intent intent = new Intent(mContext, CardMoniterService.class);
        mContext.startService(intent);
    }

    public void stopCardDealService(Context mContext) {
        Intent intent = new Intent(mContext, CardMoniterService.class);
        mContext.stopService(intent);
    }

    public void startActivity(Context mContext, Bundle bundle, Class<?> cls) {
        try {
            Intent intent = new Intent();
            intent.setClass(mContext, cls);
            if (null != bundle)
                intent.putExtras(bundle);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
