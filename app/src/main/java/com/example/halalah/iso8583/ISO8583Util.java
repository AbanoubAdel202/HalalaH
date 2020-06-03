package com.example.halalah.iso8583;

public class ISO8583Util {
    private static final String TAG = ISO8583Util.class.getSimpleName();
    
    //应用类别定义
    public static final int APP_CATEGORY_MAGCARD_FINANCE_PAYMENT = 0x60; //磁条卡金融支付类应用
    public static final int APP_CATEGORY_ICCARD_FINANCE_PAYMENT = 0x61;  //IC卡金融支付类应用
    public static final int APP_CATEGORY_MAGCARD_VALUE_ADDED = 0x62;     //磁条卡增值业务类支付
    public static final int APP_CATEGORY_ICCARD_VALUE_ADDED = 0x63;      //IC卡增值业务类支付 

    //处理要求 process requests
    public static final int PROCESS_REQUEST_0 = 0x00; //无处理要求
    public static final int PROCESS_REQUEST_1 = 0x01; //下传终端磁条卡参数
    public static final int PROCESS_REQUEST_2 = 0x02; //上传终端磁条卡状态信息
    public static final int PROCESS_REQUEST_3 = 0x03; //重新签到
    public static final int PROCESS_REQUEST_4 = 0x04; //通知终端发起更新公钥信息操作
    public static final int PROCESS_REQUEST_5 = 0x05; //下载终端IC卡参数
    public static final int PROCESS_REQUEST_6 = 0x06; //TMS参数下载
    public static final int PROCESS_REQUEST_7 = 0x07; //卡BIN黑名单下载
    public static final int PROCESS_REQUEST_8 = 0x08; //币种汇率下载（仅在境外使用）/助弄取款手续费比率下载（仅在境内使用）

    public static byte[] byteArrayAdd(byte[] bytesa, byte[] bytesb) {
        int length1 = bytesa.length;
        int length2 = bytesb.length;
        int length = length1 + length2;
        if (length > 0) {
            byte[] bytes = new byte[length];
            System.arraycopy(bytesa, 0, bytes, 0, length1);
            System.arraycopy(bytesb, 0, bytes, length1, length2);
            return bytes;
        } else {
            byte[] bytes = new byte[0];
            return bytes;
        }//BCD
    }

    public static byte[] byteArrayAdd(byte[] bytesa, byte[] bytesb, byte[] bytesc) {
        return byteArrayAdd(byteArrayAdd(bytesa, bytesb), bytesc);
    }
}
