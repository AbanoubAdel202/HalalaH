package com.example.halalah.emv;

public interface OnEmvProcessListener {
    /**请求设置EMV aid参数*/
    void finalAidSelect() throws android.os.RemoteException;
    /**请求输入金额 ，简易流程时不回调此方法*/
    void requestImportAmount(int type) throws android.os.RemoteException;
    /**请求提示信息*/
    void requestTipsConfirm(java.lang.String msg) throws android.os.RemoteException;
    /**请求多应用选择*/
    void requestAidSelect(int times, java.lang.String[] aids) throws android.os.RemoteException;
    /**请求确认是否使用电子现金*/
    void requestEcashTipsConfirm() throws android.os.RemoteException;
    /**请求确认卡信息*/
    void onConfirmCardInfo(String cardNo) throws android.os.RemoteException;
    /** 请求导入PIN */
    void requestImportPin(int type, boolean lasttimeFlag, java.lang.String amt) throws android.os.RemoteException;
    /** 请求身份认证 */
    void requestUserAuth(int certype, java.lang.String certnumber) throws android.os.RemoteException;
    /**请求联机*/
    void onRequestOnline() throws android.os.RemoteException;
    /**返回读取卡片脱机余额结果*/
    void onReadCardOffLineBalance(java.lang.String moneyCode, java.lang.String balance, java.lang.String secondMoneyCode, java.lang.String secondBalance) throws android.os.RemoteException;
    /**返回读取卡片交易日志结果*/
    void onReadCardTransLog(com.topwise.cloudpos.aidl.emv.PCardTransLog[] log) throws android.os.RemoteException;
    /**返回读取卡片圈存日志结果*/
    void onReadCardLoadLog(java.lang.String atc, java.lang.String checkCode, com.topwise.cloudpos.aidl.emv.PCardLoadLog[] logs) throws android.os.RemoteException;
    /**交易结果
     批准: 0x01
     拒绝: 0x02
     终止: 0x03
     FALLBACK: 0x04
     采用其他界面: 0x05
     其他：0x06
     EMV简易流程不回调此方法
     */
    void onTransResult(int result) throws android.os.RemoteException;
    /**出错*/
    void onError(int erroCode) throws android.os.RemoteException;
}
