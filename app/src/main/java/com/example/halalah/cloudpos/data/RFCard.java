package com.example.halalah.cloudpos.data;

/**
 * Created by kafier on 5/23/16.
 */
public class RFCard {
    public static final class RFCardType {
        public static final int UNSUPPORTED = 0x00;
        public static final int TYPEA = 0x01;
        public static final int TYPEB = 0x02;
        public static final int MIFARE_ONE = 0x10;
        public static final int MIFARE_S50 = 0x20;
        public static final int MIFARE_ONE_S70 = 0x40;
        public static final int MIFARE_ULTRALIGHT = 0x50;
        public static final int MIFARE_ULTRALIGHT_C = 0x51;
        public static final int MIFARE_PLUS = 0x60;
        public static final int MIFARE_DESFIRE = 0x70;
        public static final int MIFARE_CPU = 0x80;
        public static final int MIFARE_PRO = 0x81;
        public static final int MIFARE_S50_PRO = 0x82;
        public static final int MIFARE_S70_PRO = 0x83;
    }
}
