package com.example.halalah.util;

import android.annotation.SuppressLint;

@SuppressLint({"DefaultLocale"})
public class BytesUtil {
    public BytesUtil() {
    }

    public static String byte2HexString(byte data) {
        StringBuilder buffer = new StringBuilder();
        String hex = Integer.toHexString(data & 255);
        if (hex.length() == 1) {
            buffer.append('0');
        }

        buffer.append(hex);
        return buffer.toString().toUpperCase();
    }

    public static String bytes2HexString(byte[] data) {
        if (data != null && data.length != 0) {
            StringBuilder buffer = new StringBuilder();
            byte[] var2 = data;
            int var3 = data.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte b = var2[var4];
                String hex = Integer.toHexString(b & 255);
                if (hex.length() == 1) {
                    buffer.append('0');
                }

                buffer.append(hex);
            }

            return buffer.toString().toUpperCase();
        } else {
            return "";
        }
    }

    public static byte[] hexString2Bytes(String data) {
        if (data != null && data.length() != 0) {
            byte[] result = new byte[(data.length() + 1) / 2];
            if ((data.length() & 1) == 1) {
                data = data + "0";
            }

            for(int i = 0; i < result.length; ++i) {
                result[i] = (byte)(hex2byte(data.charAt(i * 2 + 1)) | hex2byte(data.charAt(i * 2)) << 4);
            }

            return result;
        } else {
            return new byte[0];
        }
    }

    public static byte hex2byte(char hex) {
        if (hex <= 'f' && hex >= 'a') {
            return (byte)(hex - 97 + 10);
        } else if (hex <= 'F' && hex >= 'A') {
            return (byte)(hex - 65 + 10);
        } else {
            return hex <= '9' && hex >= '0' ? (byte)(hex - 48) : 0;
        }
    }

    public static byte[] subBytes(byte[] data, int offset, int len) {
        if (offset >= 0 && data.length > offset) {
            if (len < 0 || data.length < offset + len) {
                len = data.length - offset;
            }

            byte[] ret = new byte[len];
            System.arraycopy(data, offset, ret, 0, len);
            return ret;
        } else {
            return null;
        }
    }

    public static byte[] int2Bytes(int n, boolean highFirst) {
        byte[] data = new byte[4];

        for(int i = 0; i < 4; ++i) {
            if (highFirst) {
                data[i] = (byte)(n >> 24 - i * 8 & 255);
            } else {
                data[3 - i] = (byte)(n >> 24 - i * 8 & 255);
            }
        }

        return data;
    }

    public static int bytes2Int(byte[] b, boolean highFirst) {
        int value = 0;

        for(int i = 0; i < 4; ++i) {
            int shift;
            if (highFirst) {
                shift = (3 - i) * 8;
            } else {
                shift = i * 8;
            }

            value += (b[i] & 255) << shift;
        }

        return value;
    }

    public static byte[] add(byte[] byteArray, byte b) {
        if (byteArray == null) {
            return new byte[]{b};
        } else {
            byte[] data = new byte[byteArray.length + 1];
            System.arraycopy(byteArray, 0, data, 0, byteArray.length);
            data[byteArray.length] = b;
            return data;
        }
    }

    public static byte[] mergeBytes(byte[] bytesA, byte[] bytesB) {
        if (bytesA != null && bytesA.length != 0) {
            if (bytesB != null && bytesB.length != 0) {
                byte[] bytes = new byte[bytesA.length + bytesB.length];
                System.arraycopy(bytesA, 0, bytes, 0, bytesA.length);
                System.arraycopy(bytesB, 0, bytes, bytesA.length, bytesB.length);
                return bytes;
            } else {
                return bytesA;
            }
        } else {
            return bytesB;
        }
    }

    public static byte[] merge(byte[]... data) {
        if (data == null) {
            return null;
        } else {
            byte[] bytes = null;
            byte[][] var2 = data;
            int var3 = data.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte[] aData = var2[var4];
                bytes = mergeBytes(bytes, aData);
            }

            return bytes;
        }
    }
}
