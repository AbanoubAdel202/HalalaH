package com.example.halalah;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.topwise.cloudpos.aidl.AidlDeviceService;
import com.topwise.cloudpos.aidl.buzzer.AidlBuzzer;
import com.topwise.cloudpos.aidl.emv.level2.AidlAmex;
import com.topwise.cloudpos.aidl.emv.level2.AidlEmvL2;
import com.topwise.cloudpos.aidl.emv.level2.AidlEntry;
import com.topwise.cloudpos.aidl.emv.level2.AidlPaypass;
import com.topwise.cloudpos.aidl.emv.level2.AidlPaywave;
import com.topwise.cloudpos.aidl.emv.level2.AidlPure;
import com.topwise.cloudpos.aidl.iccard.AidlICCard;
import com.topwise.cloudpos.aidl.led.AidlLed;
import com.topwise.cloudpos.aidl.magcard.AidlMagCard;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;
import com.topwise.cloudpos.aidl.rfcard.AidlRFCard;
import com.topwise.cloudpos.aidl.shellmonitor.AidlShellMonitor;
import com.topwise.cloudpos.aidl.system.AidlSystem;
import com.topwise.cloudpos.aidl.emv.level2.AidlQpboc;


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
            Log.d("topwise", "onServiceConnected: "+ mDeviceService);
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



    public AidlBuzzer getBeepManager() {

        AidlBuzzer aidlBuzzer = AidlBuzzer.Stub.asInterface(getBeep());

        return aidlBuzzer;
    }
    public IBinder getBeep() {
        try {
            if (mDeviceService != null) {
                return mDeviceService.getBuzzer();
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

    public AidlMagCard getMagCardMoniter() {
        try {
            if (mDeviceService != null) {
                return AidlMagCard.Stub.asInterface(mDeviceService.getMagCardReader());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlEmvL2 getEmvL2() {
        try {
            if (mDeviceService != null) {
                return AidlEmvL2.Stub.asInterface(mDeviceService.getL2Emv());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlPure getL2Pure() {
        try {
            if (mDeviceService != null) {
                return AidlPure.Stub.asInterface(mDeviceService.getL2Pure());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlPaypass getL2Paypass() {
        try {
            if (mDeviceService != null) {
                return AidlPaypass.Stub.asInterface(mDeviceService.getL2Paypass());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlPaywave getL2Paywave() {
        try {
            if (mDeviceService != null) {
                return AidlPaywave.Stub.asInterface(mDeviceService.getL2Paywave());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlEntry getL2Entry() {
        try {
            if (mDeviceService != null) {
                return AidlEntry.Stub.asInterface(mDeviceService.getL2Entry());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlAmex getL2Amex() {
        try {
            if (mDeviceService != null) {
                return AidlAmex.Stub.asInterface(mDeviceService.getL2Amex());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
    public AidlQpboc getL2Qpboc() {
        try {
            if (mDeviceService != null) {
                return AidlQpboc.Stub.asInterface(mDeviceService.getL2Qpboc());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

}
