package com.example.halalah.util;

import java.util.HashMap;
import java.util.Map;

public class AidToCardSchemeMapper {

    private static final Map<String, String> aidMap = new HashMap<String, String>() {{
        put("A0000000041010", "MC");
        put("A0000000043060", "DM");
        put("A0000002281010", "P1");
        put("A0000002282010", "P1");
        put("A0000002283010", "P1");
        put("A0000000031010", "VC");
        put("A0000000032010", "VD");
        put("A000000333010101", "UP");
        put("A000000333010102", "UP");
        put("A0000001523010", "DC");
        put("A0000001524010", "DC");
        put("A00000006510", "JC");
        put("A0000000651010", "JC");
        put("A000000025", "AX");
    }};


    public static String getCardSchemeByAID(String aid){
        return aidMap.get(aid);
    }

}
