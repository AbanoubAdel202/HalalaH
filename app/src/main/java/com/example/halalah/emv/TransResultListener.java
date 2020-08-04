package com.example.halalah.emv;

import android.os.Bundle;

/**
 * @author xukun
 * @version 1.0.0
 * @date 19-9-17
 */

interface TransResultListener {
    /**
     * 交互失败返回
     *
     * @param errorCode 错误码
     */
    void onFail(int errorCode);

    /**
     * 交易结果
     *
     * @param isOnline     是否联机
     * @param isTransResult 是否进入交易结果
     * @param resultValue 交易结果值
     */
    void onProcessResult(boolean isOnline, boolean isTransResult, int resultValue);

    /**
     * 进行app上层交互
     *
     * @param sort 交互类型
     * @param data 返回上层的数据
     */
    void nextTransStep(PayDataUtil.CallbackSort sort, Bundle data);

    /**
     * 再次进行应用选择
     */
    void finalSelectAgain();

    /**
     * 设置内核参数
     *
     * @param aucType 用卡类型
     */
    void setKernalData(int aucType, byte[] aidData);

    /**
     * 设置卡号
     * @param cardNo 卡号
     */
    void setCardNo(byte[] cardNo);
}
