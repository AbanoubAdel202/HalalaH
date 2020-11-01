package com.example.halalah.connect;

import com.example.halalah.iso8583.BCDASCII;

import java.util.Arrays;
import java.util.LinkedList;

public class HeadersInterceptor {

    private static final int HEADERS_LENGTH = 7;
    private static final int TPDU_LENGTH = 5;

    public byte[] addHeaders(byte[] sendPacket, String tpdu) {
        sendPacket = addToBeginningOfArray(sendPacket, BCDASCII.hexStringToBytes(tpdu));
        sendPacket = addLength(sendPacket);
        return sendPacket;
    }

    private byte[] addLength(byte[] packet) {
        String packetLengthInHex = Integer.toHexString(packet.length);
        packetLengthInHex = String.format("%0" + (4 - packetLengthInHex.length()) + "d%s", 0, packetLengthInHex);
        byte[] lengthInBytes = BCDASCII.hexStringToBytes(packetLengthInHex);
        return addToBeginningOfArray(packet, lengthInBytes);
    }

    private byte[] addToBeginningOfArray(byte[] elements, byte[] newElements) {
        LinkedList<Byte> tmpList = new LinkedList();
        for (int i = 0; i < newElements.length; i++) {
            tmpList.add(newElements[i]);
        }
        for (int i = 0; i < elements.length; i++) {
            tmpList.add(elements[i]);
        }
        byte[] bytesArray = new byte[tmpList.size()];
        for (int i = 0; i < bytesArray.length; i++) {
            bytesArray[i] = tmpList.get(i).byteValue();
        }
        return bytesArray;
    }

    public boolean checkPacketIntegrity(byte[] rawResponse) {
        byte[] lengthPortion = Arrays.copyOfRange(rawResponse, 0, 2);
        String lengthHex = BCDASCII.bytesToHexString(lengthPortion);
        int allegedLength = Integer.parseInt(lengthHex, 16) - TPDU_LENGTH;
        return allegedLength == rawResponse.length - HEADERS_LENGTH;
    }

    public byte[] getResponseBody(byte[] rawResponse){
        return Arrays.copyOfRange(rawResponse, 7, rawResponse.length);
    }
}
