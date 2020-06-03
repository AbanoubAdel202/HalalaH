package com.example.halalah;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.topwise.cloudpos.aidl.AidlDeviceService;
import com.topwise.cloudpos.aidl.emv.AidlPboc;
import com.topwise.cloudpos.aidl.iccard.AidlICCard;
import com.topwise.cloudpos.aidl.led.AidlLed;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;
import com.topwise.cloudpos.aidl.rfcard.AidlRFCard;
import com.topwise.cloudpos.aidl.shellmonitor.AidlShellMonitor;
import com.topwise.cloudpos.aidl.system.AidlSystem;

/**
 * @author xukun
 * @version 1.0.0
 * @date 18-6-8
 */

public class DeviceTopUsdkServiceManager {

    private static String DEVICE_SERVICE_PACKAGE_NAME = "com.android.topwise.topusdkservice";
    private static String DEVICE_SERVICE_CLASS_NAME = "com.android.topwise.topusdkservice.service.DeviceService";
    private static String ACTION_DEVICE_SERVICE = "topwise_cloudpos_device_service";

    private static DeviceTopUsdkServiceManager mDeviceServiceManager;


    private Context mContext;
    private AidlDeviceService mDeviceService;

    public AidlDeviceService getDeviceService() {
        return mDeviceService;
    }
    
    private boolean mBindResult = false;

    public static void getmDeviceServiceManager() {
        synchronized (DeviceTopUsdkServiceManager.class) {
            mDeviceServiceManager = new DeviceTopUsdkServiceManager();
            Log.d("topwise","gz mDeviceServiceManager: " + mDeviceServiceManager);
            mDeviceServiceManager.mContext = PosApplication.getApp();
            mDeviceServiceManager.mBindResult = mDeviceServiceManager.bindDeviceService();
        }
    }

    public static DeviceTopUsdkServiceManager getInstance() {
        Log.d("topwise","mDeviceServiceManager: " + mDeviceServiceManager);
        if (null == mDeviceServiceManager) {
            synchronized (DeviceTopUsdkServiceManager.class) {
                if (null == mDeviceServiceManager) {
                    getmDeviceServiceManager();
                }
            }
        }
        return mDeviceServiceManager;
    }

    private boolean bindDeviceService() {
        Log.i("topwise","");

        Intent intent = new Intent();
        intent.setAction(ACTION_DEVICE_SERVICE);
        intent.setClassName(DEVICE_SERVICE_PACKAGE_NAME, DEVICE_SERVICE_CLASS_NAME);

        try {
            boolean bindResult = mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            Log.i("topwise","bindResult = " + bindResult);
            return bindResult;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void unBindDeviceService() {
        Log.i("topwise","");

        try {
            mContext.unbindService(mConnection);
        } catch (Exception e) {
            Log.i("topwise","unbind DeviceService service failed : " + e);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mDeviceService = AidlDeviceService.Stub.asInterface(service);
            Log.i("topwise","onServiceConnected  :  " + mDeviceService);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("topwise","onServiceDisconnected  :  " + mDeviceService);
            mDeviceService = null;
        }
    };

    public IBinder getSystemService() {
        try {
            if (mDeviceService != null) {
                return mDeviceService.getSystemService();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlSystem getSystemManager() {

        AidlSystem aidlSystem = AidlSystem.Stub.asInterface(getSystemService());
        return aidlSystem;
    }

    public IBinder getPinPad(int devid) {
        try {
            if (mDeviceService != null) {
                return mDeviceService.getPinPad(devid);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlPinpad getPinpadManager(int type) {

        AidlPinpad aidlPinpad = AidlPinpad.Stub.asInterface(getPinPad(type));
        return aidlPinpad;
    }

    public AidlLed getLedManager() {

        AidlLed aidlLed = AidlLed.Stub.asInterface(getLed());

        return aidlLed;
    }

    public IBinder getLed() {
        try {
            if (mDeviceService != null) {
                return mDeviceService.getLed();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public IBinder getPrinter() {
        try {
            if (mDeviceService != null) {
                return mDeviceService.getPrinter();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlPrinter getPrintManager() {

        AidlPrinter aidlPrinter = AidlPrinter.Stub.asInterface(getPrinter());
        return aidlPrinter;
    }

    public IBinder getEMVL2() {
        try {
            if (mDeviceService != null) {
                return mDeviceService.getEMVL2();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlPboc getPbocManager() {

        AidlPboc aidlPboc = AidlPboc.Stub.asInterface(getEMVL2());
        return aidlPboc;
    }


    public IBinder getRFIDReader() {
        try {
            if (mDeviceService != null) {
                return mDeviceService.getRFIDReader();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IBinder getPSAMReader(int devid) {
        try {
            if (mDeviceService != null) {
                return mDeviceService.getPSAMReader(devid);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IBinder getSerialPort(int port) {
        try {
            if (mDeviceService != null) {
                return mDeviceService.getSerialPort(port);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlShellMonitor getShellMonitor() {
        try {
            if (mDeviceService != null) {
                return AidlShellMonitor.Stub.asInterface(mDeviceService.getShellMonitor());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IBinder getCPUCard() {
        try {
            if (mDeviceService != null) {
                return mDeviceService.getCPUCard();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IBinder getPedestal() {
        try {
            if (mDeviceService != null) {
                return mDeviceService.getPedestal();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlICCard getICCardMoniter() {
        try {
            if (mDeviceService != null) {
                return AidlICCard.Stub.asInterface(mDeviceService.getInsertCardReader());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlRFCard getRFCardMoniter() {
        try {
            if (mDeviceService != null) {
                return AidlRFCard.Stub.asInterface(mDeviceService.getRFIDReader());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
