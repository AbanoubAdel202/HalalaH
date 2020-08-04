package com.example.halalah.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class CommonFunction {
    public static String _FILE_LINE_FUN_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuffer sb = new StringBuffer(traceElement.getFileName()).append(" | ").append(
                traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName() + " ");
        return sb.toString();
    }

    public static String _FILE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getFileName();
    }

    public static String _FUNC_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getMethodName();
    }

    public static int _LINE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getLineNumber();
    }

    public static String _TIME_() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(now);
    }

    public static String getVersion(Context cxt) {
        String version = "1.0.0";
        try {
            PackageManager manager = cxt.getPackageManager();
            PackageInfo info = manager.getPackageInfo(cxt.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return version;
        }
    }

    public static boolean hasElement(String[] elements, String target) {
        if (elements == null || elements.length == 0 || target == null) {
            return false;
        }
        for (String element : elements) {
            if (target.equals(element)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasElement(int[] elements, int target) {
        if (elements == null || elements.length == 0) {
            return false;
        }
        for (int element : elements) {
            if (element == target) {
                return true;
            }
        }
        return false;
    }

}
