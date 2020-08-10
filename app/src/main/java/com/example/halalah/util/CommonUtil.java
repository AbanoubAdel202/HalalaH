package com.example.halalah.util;

import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.example.halalah.qrcode.utils.SDKLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

/**
 * Created by on 2016/7/14.
 */
public class CommonUtil {

    private static final String TAG = "CommonUtil";
    private static final int SYSTEM_UID = 1000;

    /**
     * @param tag
     * @param path 文件对应的路径
     * @return
     */
    public static String getValueByTag(String tag, String path) {
        String value = "0.0.0";
        String content = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            File f = new File(path);
            if (f.exists()) {
                fr = new FileReader(path);
                br = new BufferedReader(fr);
                String line = br.readLine();
                while (line != null) {
                    if (line.startsWith(tag + "=")) {
                        content = line;
                        break;
                    }
                    line = br.readLine();
                }

                if (content != null) {
                    value = content.substring(content.indexOf(tag) + tag.length() + 1, content.length());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {

                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fr != null) {

                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        SDKLog.d(TAG, "path=" + path + ",tag=" + tag + ",value=" + value.trim());
        return value.trim();
    }


    public static void setSystemTime(Context context, String timeStr) {
        if (timeStr == null) {
            Log.e(TAG, "timeStr == null");
            return;
        }
        if (timeStr.length() != 14) {
            Log.e(TAG, "timeStr.length() != 14");
            return;
        }
        try {
            int year = Integer.valueOf(timeStr.substring(0, 4));

            int month = Integer.valueOf(timeStr.substring(4, 6)) - 1;
            if (month > 12) {
                Log.e(TAG, "month > 12");
                return;
            }
            int day = Integer.valueOf(timeStr.substring(6, 8));
            if (day > 31) {
                Log.e(TAG, "day > 31");
                return;
            }
            int hour = Integer.valueOf(timeStr.substring(8, 10));
            if (hour > 24) {
                Log.e(TAG, "hour > 24");
                return;
            }
            int minute = Integer.valueOf(timeStr.substring(10, 12));
            if (minute > 60) {
                Log.e(TAG, "minute > 60");
                return;
            }
            int second = Integer.valueOf(timeStr.substring(12, 14));
            if (second > 60) {
                Log.e(TAG, "second > 60");
                return;
            }
            Calendar c = Calendar.getInstance();
            c.set(year, month, day, hour, minute, second);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.setTime(c.getTimeInMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param appFilePath
     * @param runActivityName
     * @param appName
     * @return
     */
    public static int installSilent(String appFilePath, String runActivityName, String appName) {
        int ret = -1;
        if (appFilePath == null || "".equals(appFilePath)) {
            Log.d(TAG, "appFilePath is null");
            return ret;
        }
        if (runActivityName == null || "".equals(runActivityName)) {
            Log.d(TAG, "runActivityName is null");
            return ret;
        }
        if (appName == null || "".equals(appName)) {
            Log.d(TAG, "appName is null");
            return ret;
        }

        String[] args = {"pm", "install", "-r", appFilePath};
        ProcessBuilder processBuilder = new ProcessBuilder(args);

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder resultMsg = new StringBuilder();
        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                resultMsg.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ret;
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        if (resultMsg.toString().contains("Success") || resultMsg.toString().contains("success")) {
            return 0;
        }
        return ret;

    }

    public static void doStartApplicationWithPackageName(Context context, String packagename) {

        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            String packageName = resolveinfo.activityInfo.packageName;
            String className = resolveinfo.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    public static Intent createExplictIntent(Context context,
                                             Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent,
                0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

    public static boolean checkPermissionSuc(Context context, int uid, String permission) {
        SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "checkPermissionSuc uid: " + uid + "; permission: " + permission);
        if (uid == SYSTEM_UID) {
            return true;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = packageManager.getNameForUid(uid);
            int permissionResult = packageManager.checkPermission(permission, packageName);
            SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "checkPermissionSuc packageName: " + packageName + "; permissionResult: " + permissionResult);
            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            PackageInfo pack = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            String[] permissionStrings = pack.requestedPermissions;
            boolean flag = false;
            for (String s : permissionStrings) {
                if (permission.equals(s)) {
                    SDKLog.d(TAG, CommonFunction._FILE_LINE_FUN_() + "checkPermissionSuc success");
                    flag = true;
                    break;
                }
            }
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
