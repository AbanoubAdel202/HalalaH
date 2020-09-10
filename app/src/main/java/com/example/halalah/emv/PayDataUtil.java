package com.example.halalah.emv;

import android.util.Log;

import com.example.halalah.database.table.Capk;
import com.example.halalah.qrcode.utils.SDKLog;
import com.topwise.cloudpos.struct.BytesUtil;
import com.topwise.cloudpos.struct.Tlv;
import com.topwise.cloudpos.struct.TlvList;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 交易全局数据存储类
 *
 * @author xukun
 * @version 1.0.0
 * @date 19-9-17
 */

class PayDataUtil {

    private static final String TAG = PayDataUtil.class.getSimpleName();

    static final String KERNAL_FILE = "kernal_data.txt";

    static final int ORIGINAL_EMV_PROCESS = 100;
    static final int WILDCARD_PROCESS = 101;
    static final int DEFAULT_RETURN_CODE = -100;

    static final int CLSS_TAG_NOT_EXIST = 0;
    static final int CLSS_TAG_EXIST_WITHVAL = 1;
    static final int CLSS_TAG_EXIST_NOVAL = 2;

    static final int EMV_OK = 0;
    static final int CLSS_USE_CONTACT = -23;
    static final int CLSS_REFER_CONSUMER_DEVICE = -40;  //caixh added
    static final int ENTRY_KERNEL_6A82_ERR = -105;
    static final int CLSS_RESELECT_APP = -35;
    static final int CLSS_TERMINATE = -25;
    static final int ICC_CMD_ERR = -2;
    static final byte KERNTYPE_MC = 0x02;
    static final byte KERNTYPE_VISA = 0x03;
    static final byte KERNTYPE_AMEX = 0x04;
    static final byte KERNTYPE_JCB = 0x05;
    static final byte KERNTYPE_ZIP = 0x06; //Discover ZIP or 16
    static final byte KERNTYPE_DPAS = 0x06;//Discover DPAS
    static final byte KERNTYPE_RUPAY = 0x0D;
    static final byte  KERNTYPE_PURE = 0x2D;
    /**
     * auth handle response code
     */
    static final String AUTH_RES_CODE = "00";

    /**
     * Transaction Path
     */
    static final int CLSS_TRANSPATH_EMV = 0;
    static final int CLSS_TRANSPATH_MAG = 0x10;
    static final int CLSS_TRANSPATH_LEGACY = 0x20;

    /**
     * AC Type
     */
    static final int AC_AAC = 0x00;
    static final int AC_TC = 0x01;
    static final int AC_ARQC = 0x02;

    /**
     * Byte 4 bit8-5 CVM
     */
    static final byte CLSS_OC_NO_CVM = 0x00;
    static final byte CLSS_OC_OBTAIN_SIGNATURE = 0x10;
    static final byte CLSS_OC_ONLINE_PIN = 0x20;
    /**
     * Byte 1 bit8-5 Status
     */
    static final byte CLSS_OC_APPROVED = 0x10;
    static final byte CLSS_OC_DECLINED = 0x20;
    static final byte CLSS_OC_ONLINE_REQUEST = 0x30;
    static final byte CLSS_OC_END_APPLICATION = 0x40;
    static final byte CLSS_OC_SELECT_NEXT = 0x50;
    static final byte CLSS_OC_TRY_ANOTHER_INTERFACE = 0x60;
    static final byte CLSS_OC_TRY_AGAIN = 0x70;
    static final byte CLSS_OC_NA = (byte) 0xF0;

    static final byte PINTYPE_OFFLINE = 0x01;
    static final byte PINTYPE_OFFLINE_LASTTIME = 0x02;
    static final byte PINTYPE_ONLINE = 0x03;

    /**
     * date or time
     */
    static final int TRANS_DATE_YYMMDD = 0;
    static final int TRANS_TIME_HHMMSS = 1;

    /**
     * 交易过程请求APP交互类型
     */
    enum CallbackSort {
        REQUEST_IMPORT_AMT,
        REQUEST_TIPS_CONFIRM,
        REQUEST_AID_SELECT,
        REQUEST_FINAL_AID_SELECT,
        REQUEST_ECASHTIPS_CONFIRM,
        REQUEST_CARDINFO_CONFIRM,
        REQUEST_IMPORT_PIN,
        REQUEST_USER_AUTH,
        REQUEST_ONLINE,
        ON_OFFLINE_BALANCE,
        ON_CARD_TRANSLOG,
        ON_CARD_LOADLOG,
        ON_TRANS_RESULT,
        ON_ERROR,
        DEFAULT_MENU
    }

    class CardCode {
        /**
         * error code
         */
        static final int NO_AID_ERROR = 0;

        /**
         * callback param
         */
        static final String IMPORT_AMT_TIMES = "times";
        static final String IMPORT_AMT_AIDS = "aids";
        static final String CARDINFO_CARDNO = "card_no";
        static final String IMPORT_PIN_AMOUNT = "amount";
        static final String IMPORT_PIN_TYPE = "pin_type";
        static final String TRANS_RESULT = "trans_result";
        static final String ERROR_CODE = "error_code";

        /**
         * trans data
         */
        static final String FINAL_SELECT_LEN = "select_len";
        static final String FINAL_SELECT_DATA = "select_data";
        static final String PREPROC_RESULT = "preproc_result";
        static final String TRANS_PARAM = "trans_param";

        /**
         * trans type
         */
        static final byte LKL_CONSUME = 0x00;
        static final byte LKL_BALANCE = 0x31;
        static final byte LKL_PREAUTH = 0x03;
        static final byte LKL_REVOKE = 0x20;
        static final byte LKL_ACCOUNT_DEPOSIT = 0x60;
        static final byte LKL_UNACCOUNT_DEPOSIT = 0x62;
        static final byte LKL_CASH_DEPOSIT = 0x63;
        static final byte LKL_CASH_REVOKE = 0x17;
        static final byte LKL_UNLOOP_CARD = (byte) 0xF1;
        static final byte LKL_OFF_BALANCE = (byte) 0xF2;
        static final byte LKL_TRANS_LOG = (byte) 0xF3;
        static final byte LKL_TRAP_LOG = (byte) 0xF4;

        /**
         * trans result
         */
        static final byte TRANS_APPROVAL = 0x01;
        static final byte TRANS_REFUSE = 0x02;
        static final byte TRANS_STOP = 0x03;
        static final byte TRANS_FALLBACK = 0x04;
        static final byte TRANS_USE_OTHER_INTERFACE = 0x05;
        static final byte TRANS_SECOND_READ = 0x18;
        static final byte TRANS_OTHER = 0x06;
        static final byte CDCVM_SECOND_READ_CARD = 0x19;
    }

    /**
     * 金额填充 eg:1.2 -> 000000000012
     *
     * @param amt 交易金额
     * @return 填充好的金额数据
     */
    String getFixedAmount(String amt) {
        StringBuilder buffer = new StringBuilder();
        if (amt.contains(".")) {
            String[] buf = amt.split("\\.");
            SDKLog.d(TAG, "buf len: " + buf.length);
            if (buf.length == 2) {
                if (buf[1].length() >= 2) {
                    buffer.append(buf[0]).append(buf[1].substring(0, 2));
                } else if (buf[1].length() == 1) {
                    buffer.append(buf[0]).append(buf[1]).append("0");
                } else {
                    buffer.append(buf[0]).append("00");
                }
            } else {
                return null;
            }
        } else {
            buffer.append(amt).append("00");
        }
        SDKLog.d(TAG, "amount buffer: " + buffer);
        String bufStr = buffer.toString().length() % 2 != 0 ? 0 + buffer.toString() : buffer.toString();
        byte[] amount = new byte[6];
        Arrays.fill(amount, (byte) 0x00);
        System.arraycopy(BytesUtil.hexString2Bytes(bufStr), 0, amount, 6 - bufStr.length() / 2, bufStr.length() / 2);
        return BytesUtil.bytes2HexString(amount);
    }

    /**
     * 获取交易序列计数器
     *
     * @return 交易计数器(4个字节)
     */
    String getSequenceCounter() {

        long counter = getSerialNumber();
        String buffer = String.valueOf(counter);
        if (buffer.length() % 2 != 0) {
            buffer = "0" + buffer;
        }
        byte[] data = new byte[4];
        Arrays.fill(data, (byte) 0);
        byte[] buff = BytesUtil.hexString2Bytes(buffer);
        System.arraycopy(buff, 0, data, 4 - buff.length, buff.length);
        buffer = BytesUtil.bytes2HexString(data);
        SDKLog.d(TAG, "sequence counter: " + buffer);
        return buffer;
    }

    private static long sn = 0;
    public static long getSerialNumber() {
        if(sn > 999999) {
            sn = 0;
        }
        return ++sn;
    }

    public static byte[] getHexRandom(int len) {
        if(len <= 0) {
            return null;
        }
        byte[] random = new byte[len];
        Random random1 = new Random();
        random1.nextBytes(random);
        return random;
    }

    /**
     * 获取交易时间
     *
     * @param type
     * @return
     */
    String getTransDateTime(int type) {
        Date date = new Date();
        String result = "";
        SimpleDateFormat format = null;
        switch (type) {
            case TRANS_DATE_YYMMDD:
                format = new SimpleDateFormat("yyMMdd", Locale.CHINA);
                result = format.format(date);
                break;
            case TRANS_TIME_HHMMSS:
                format = new SimpleDateFormat("hhmmss", Locale.CHINA);
                result = format.format(date);
                break;
            default:
                break;
        }
        SDKLog.d(TAG, "getTransDateTime: " + result);

        return result;
    }

    /**
     * 填充金额
     *
     * @param amount 分
     * @return 1  -> 000000000001
     */
    String getFixedAmount(int amount) {
        byte[] amtByte = new byte[6];
        Arrays.fill(amtByte, (byte) 0);
        String amtBuf = String.valueOf(amount);
        if (amtBuf.length() % 2 != 0) {
            amtBuf = "0" + amtBuf;
        }
        System.arraycopy(BytesUtil.hexString2Bytes(amtBuf), 0, amtByte, 6 - amtBuf.length() / 2, amtBuf.length() / 2);
        return BytesUtil.bytes2HexString(amtByte);
    }

    /**
     * 获取默认的kernal数据
     *
     * @return
     */
    TlvList getDefaultKernal() {
        TlvList list = new TlvList();
        list.addTlv("9F53","01");
        list.addTlv("5F36","02");
        list.addTlv("9F03","000000000000");
        list.addTlv("9F1E","3030303030303031");
        list.addTlv("9F15","0001");
        list.addTlv("9F09","0002");
        list.addTlv("9F40","0000000000");
        list.addTlv("DF8117","00");
        list.addTlv("DF8118","40");
        list.addTlv("DF8119","08");
        list.addTlv("DF811F","08");
        list.addTlv("DF811A","9F6A04");
        list.addTlv("9F6D","0001");
        list.addTlv("DF811E","10");
        list.addTlv("DF812C","00");
        list.addTlv("9F35","22");
        list.addTlv("DF81","0102");
        list.addTlv("5F2A","0682");
        list.addTlv("9F1A","0682");

        //list.addTlv("DF8104","");
        //list.addTlv("DF8105","");
        return list;
    }

    public static byte[] getSHA1(byte[] buf, int offset, int len) {
        if (buf == null) {
            return null;
        }
        if (offset < 0) {
            return null;
        }
        if (len == 0) {
            return null;
        }

        byte[] SHA1 = null;
        byte[] data = new byte[len];
        System.arraycopy(buf, offset, data, 0, len);

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            SHA1 = messageDigest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return SHA1;
    }

    public static byte[] getCAPKChecksum(Capk capk) {
        byte[] data = new byte[5 + 1 + capk.getModul().length + capk.getExponent().length];
        System.arraycopy(BytesUtil.hexString2Bytes(capk.getRid()), 0, data, 0, 5);
        data[5] = capk.getIndex();
        System.arraycopy(capk.getModul(), 0, data, 6, capk.getModul().length);
        System.arraycopy(capk.getExponent(), 0, data, 6 + capk.getModul().length, capk.getExponent().length);
        byte[] sha1 = PayDataUtil.getSHA1(data, 0, data.length);
        Log.d(TAG, "getCAPKChecksum, sha1: " + BytesUtil.bytes2HexString(sha1));
        return sha1;
    }
}
