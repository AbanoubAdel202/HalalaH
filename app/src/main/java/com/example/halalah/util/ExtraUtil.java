package com.example.halalah.util;

import android.text.format.DateFormat;
import android.util.Log;

//import com.example.halalah.PosApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Locale;

/**
 * @author xukun
 * @version 1.0.0
 * @date 18-6-12
 */

public class ExtraUtil {

    public static String GetDate_Time(){
        Date d = new Date();
        CharSequence s = DateFormat.format("MMddHHmmss", d.getTime());
        return s.toString();

    }

    public static String Get_Local_Date_Time(){
        Date d = new Date();
        CharSequence s = DateFormat.format("yyMMddHHmmss", d.getTime());

        return s.toString();

    }


    public static String getCustomVersionMsg(String originalMsg) {
        StringBuilder version = new StringBuilder();
        if (originalMsg != null) {
            version.append(originalMsg);
            version.append("-");
        }
        InputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
      //      in = PosApplication.getApp().getAssets().open("version.ver");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            if ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        version.append(content.toString());
        Log.i("topwise","getCustomVersionMsg: " + version.toString());
        return version.toString();
    }

    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
