package com.example.halalah.connect;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class CommunicationsHandlerTest {

    @Test
    public void addToBeginningOfArray() {
        byte[] originalArray = {(byte)'C', (byte)'D', (byte)'E'};
        byte[] newArray = {(byte) 'A', (byte)'B'};
        addToBeginningOfArray(originalArray, newArray);
    }

    public static byte[] addToBeginningOfArray(byte[] elements, byte[] newElements) {
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
}