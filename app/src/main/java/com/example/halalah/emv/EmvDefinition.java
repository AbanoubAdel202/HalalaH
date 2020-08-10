package com.example.halalah.emv;

public class EmvDefinition {
    private static final String TAG = PayDataUtil.class.getSimpleName();

    //Kernel Type
    public static final int KERNTYPE_DEF = 0x00;
    public static final int KERNTYPE_EMV = 0x00;
    public static final int KERNTYPE_VISAAP = 0x01;
    public static final int KERNTYPE_MC = 0x02; //PayPass
    public static final int KERNTYPE_VISA = 0x03; //qVSDC
    public static final int KERNTYPE_AMEX = 0x04;
    public static final int KERNTYPE_JCB = 0x05; //J/Speedy
    public static final int KERNTYPE_ZIP = 0x06; //Discover ZIP or 16
    public static final int KERNTYPE_DPAS = 0x06; //Discover DPAS
    public static final int KERNTYPE_QPBOC = 0x07;
    public static final int KERNTYPE_QUICS = 0x17;// with qPBOC
    public static final int KERNTYPE_RUPAY = 0x0D;
    public static final int KERNTYPE_FLASH = 0x10;
    public static final int KERNTYPE_EFT = 0x11;
    public static final int KERNTYPE_PURE = 0x12;
    public static final int KERNTYPE_PAGO = 0x13;
    public static final int KERNTYPE_MIR = 0x14;
    public static final int KERNTYPE_PBOC = 0xE1; //Contact PBOC
    public static final int KERNTYPE_NSICC = 0xE2;
    public static final int KERNTYPE_RFU = 0xFF;

    //Transaction type
    public static final int EMV_TRANS_TYPE_GOODS = 0x00;
    public static final int EMV_TRANS_TYPE_SERVICE = 0x00;//The same with Goods 00
    public static final int EMV_TRANS_TYPE_CASH = 0x01;
    public static final int EMV_TRANS_TYPE_CASHBACK = 0x09;
    public static final int EMV_TRANS_TYPE_REFUND = 0x20;
    public static final int EMV_TRANS_TYPE_INQUIRY = 0x31;
    public static final int EMV_TRANS_TYPE_TRANSFER = 0x40;
    public static final int EMV_TRANS_TYPE_PAYMENT = 0x50;
    public static final int EMV_TRANS_TYPE_ADMIN = 0x60;
    public static final int EMV_TRANS_TYPE_DEPOSIT = 0x21;

    //CDA mode
    public static final int EMV_CDA_MODE1 = 0x00; //CDA on ARQC _ Req, CDA on 2nd GenAC TC after approved Online authorization
    public static final int EMV_CDA_MODE2 = 0x01; //CDA on ARQC _ No Req CDA on 2 2nd Gen Ac TC after appproved online authorization
    public static final int EMV_CDA_MODE3 = 0x02; //No Req. CDA on ARQC + No Req CDA on 2nd GenAc after approved Online authorization
    public static final int EMV_CDA_MODE4 = 0x03; //No Req CDA on ARQC + req CDA on 2nd GenAcC after approved Online Authorization

    //
    public static final int EMV_DEFAULT_ACTION_CODE_BEFOR_GAC1 = 0x00;    //Default Action before 1st AC
    public static final int EMV_DEFAULT_ACTION_CODE_AFTER_GAC1 = 0x01;    //Default Action after  1st AC

    //Terminal defined Authorisation Response Code
    public static final String OFFLINE_APPROVED = "Y1";
    public static final String OFFLINE_DECLINE = "Z1";
    public static final String ONLINE_APPROVED = "Y2";
    public static final String ONLINE_DECLINE = "Z2";
    public static final String ONLINE_ERROR_OFFLINE_APPROVED = "Y3";
    public static final String ONLINE_ERROR_OFFLINE_DECLINE = "Z3";

    //Online processing results
    public static final int EMV_ONLINE_APPROVED = 0x00;
    public static final int EMV_ONLINE_REJECT = 0x01;
    public static final int EMV_ONLINE_VOICE_PREFER = 0x02;
    public static final int EMV_ONLINE_ERROR = 0x03;
    public static final int EMV_ONLINE_TIME_OUT = 0x04;
    public static final int EMV_ONLINE_CONNECT_FAILED = 0x05;

    //EMV error code definition
    public static final int EMV_OK = 0;
    public static final int EMV_APPROVED = 1;
    public static final int EMV_FORCE_APPROVED = 2;
    public static final int EMV_DECLINED = 3;
    public static final int EMV_NOT_ALLOWED = 5;
    public static final int EMV_NO_ACCEPTED = 6;
    public static final int EMV_TERMINATED = 7;
    public static final int EMV_CARD_BLOCKED = 8;
    public static final int EMV_APP_BLOCKED = 9;
    public static final int EMV_NO_APP = 10;
    public static final int EMV_FALLBACK = 11;
    public static final int EMV_CAPK_EXPIRED = 12;
    public static final int EMV_CAPK_CHECKSUM_ERROR = 13;
    public static final int EMV_AID_DUPLICATE = 14;
    public static final int EMV_CERTIFICATE_RECOVER_FAILED = 15;
    public static final int EMV_DATA_AUTH_FAILED = 16;
    public static final int EMV_UN_RECOGNIZED_TAG = 17;
    public static final int EMV_DATA_NOT_EXISTS = 18;
    public static final int EMV_DATA_LENGTH_ERROR = 19;
    public static final int EMV_INVALID_TLV = 20;
    public static final int EMV_INVALID_RESPONSE = 21;
    public static final int EMV_DATA_DUPLICATE = 22;
    public static final int EMV_MEMORY_NOT_ENOUGH = 23;
    public static final int EMV_MEMORY_OVERFLOW = 24;
    public static final int EMV_PARAMETER_ERROR = 25;
    public static final int EMV_ICC_ERROR = 26;
    public static final int EMV_NO_MORE_DATA = 27;
    public static final int EMV_CAPK_NO_FOUND = 28;
    public static final int EMV_AID_NO_FOUND = 29;
    public static final int EMV_FORMAT_ERROR = 30;
    public static final int EMV_ONLINE_REQUEST = 31;//online request -by wfh20190805
    public static final int EMV_SELECT_NEXT_AID = 32;//Select next AID
    public static final int EMV_TRY_AGAIN = 33;//Try Again. ICC read failed.
    public static final int EMV_SEE_PHONE = 34;//Status Code returned by IC card is 6986, please see phone. GPO 6986 CDCVM.
    public static final int EMV_TRY_OTHER_INTERFACE = 35;//Try other interface -by wfh20190805
    public static final int EMV_ICC_ERR_LAST_RECORD = 36;
    public static final int EMV_OTHER_ERROR = 255;
}
