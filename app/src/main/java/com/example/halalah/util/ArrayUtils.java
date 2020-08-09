package com.example.halalah.util;

import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    public static String convertArrayToString(String[] array) {
        if (array == null) {
            return null;
        }
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + array[i];
            // Do not append comma at the end of last element
            if (i < array.length - 1) {
                str = str + ",";
            }
        }
        return str;
    }

    public static String convertListToString(List<String> list) {
        if (list == null) {
            return null;
        }
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str = str + list.get(i);
            // Do not append comma at the end of last element
            if (i < list.size() - 1) {
                str = str + ",";
            }
        }
        return str;
    }


    public static String[] convertStringToArray(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String[] arr = str.split(",");
        return arr;
    }

    public static List<String> convertStringToList(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String[] arr = str.split(",");
        return Arrays.asList(arr);
    }
}
