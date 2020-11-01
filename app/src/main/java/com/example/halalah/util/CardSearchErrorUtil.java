package com.example.halalah.util;

public class CardSearchErrorUtil {
    public static final int CARD_SEARCH_ERROR_REASON_MAG_READ = 100;

    public static final int CARD_SEARCH_ERROR_REASON_MAG_EMV = 101;

    public static final int CARD_SEARCH_ERROR_REASON_MAG_EMV_s = -16;

    /**交易结果
     批准: 0x01
     拒绝: 0x02
     终止: 0x03
     FALLBACK: 0x04
     采用其他界面: 0x05
     其他：0x06
     EMV简易流程不回调此方法
     */
    public static final int TRANS_APPROVE = 0x01;
    public static final int TRANS_REASON_REJECT = 0x02;
    public static final int TRANS_REASON_STOP = 0x03;
    public static final int TRANS_REASON_FALLBACK = 0x04;
    public static final int TRANS_REASON_OTHER_UI = 0x05;
    public static final int TRANS_REASON_STOP_OTHERS = 0x06;

}