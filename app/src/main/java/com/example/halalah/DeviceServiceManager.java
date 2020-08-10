package com.example.halalah;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.topwise.cloudpos.aidl.AidlDeviceService;
import com.topwise.cloudpos.aidl.camera.AidlCameraScanCode;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;
import com.topwise.cloudpos.aidl.system.AidlSystem;

public class DeviceServiceManager {
    private static final String TAG = Utils.TAGPUBLIC + DeviceServiceManager.class.getSimpleName();

    private static String DEVICE_SERVICE_PACKAGE_NAME = "com.android.topwise.lklusdkservice";
    private static String DEVICE_SERVICE_CLASS_NAME = "com.android.topwise.lklusdkservice.service.DeviceService";
    private static String ACTION_DEVICE_SERVICE = "lkl_cloudpos_device_service";

    private static DeviceServiceManager mDeviceServiceManager;

    private Context mContext;
    private AidlDeviceService mDeviceService;
    private boolean mBindResult = false;

    public static void get(Context context) {
        synchronized (DeviceServiceManager.class) {
            mDeviceServiceManager = new DeviceServiceManager();
            mDeviceServiceManager.mContext = context;
            mDeviceServiceManager.mBindResult = mDeviceServiceManager.bindDeviceService();
        }
    }

    public static DeviceServiceManager getInstance() {
        return mDeviceServiceManager;
    }

    private boolean bindDeviceService() {
        Log.i(TAG, "bindDeviceService()");

        Intent intent = new Intent();
        intent.setAction(ACTION_DEVICE_SERVICE);
        intent.setClassName(DEVICE_SERVICE_PACKAGE_NAME, DEVICE_SERVICE_CLASS_NAME);

        try {
            boolean bindResult = mContext.bindService(intent, mConnection, mContext.BIND_AUTO_CREATE);
            Log.i(TAG, "bindResult = "+bindResult);
            return bindResult;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void unBindDeviceService() {
        Log.i(TAG, "unBindDeviceService()");

        try {
            mContext.unbindService(mConnection);
        } catch (Exception e) {
            Log.i(TAG, "unbind DeviceService service failed : " + e);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mDeviceService = AidlDeviceService.Stub.asInterface(service);
            Log.i(TAG,"onServiceConnected  :  "+mDeviceService);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG,"onServiceDisconnected  :  "+mDeviceService);
            mDeviceService = null;
        }
    };

    public IBinder getSystemService() {
        try {
            if(mDeviceService != null) {
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
            if(mDeviceService != null) {
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

    public IBinder getPrinter() {
        try {
            if(mDeviceService != null) {
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

    public IBinder getInsertCardReader() {
        try {
            if(mDeviceService != null) {
                return mDeviceService.getInsertCardReader();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
    public IBinder getRFIDReader() {
        try {
            if(mDeviceService != null) {
                return mDeviceService.getRFIDReader();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IBinder getPSAMReader(int devid) {
        try {
            if(mDeviceService != null) {
                return mDeviceService.getPSAMReader(devid);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IBinder getSerialPort(int port) {
        try {
            if(mDeviceService != null) {
                return mDeviceService.getSerialPort(port);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IBinder getShellMonitor() {
        try {
            if(mDeviceService != null) {
                return mDeviceService.getShellMonitor();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
    public IBinder getCPUCard() {
        try {
            if(mDeviceService != null) {
                return mDeviceService.getCPUCard();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*public IBinder getExternalRFCardReader() {
        try {
            if(mDeviceService != null) {
                return mDeviceService.getExternalRFCardReader();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }*/
    public IBinder getPedestal() {
        try {
            if(mDeviceService != null) {
                return mDeviceService.getPedestal();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AidlCameraScanCode getCameraManager() {
        try {
            if(mDeviceService != null) {
                Log.i(TAG, "mDeviceService.getCameraManager()");
                return AidlCameraScanCode.Stub.asInterface(mDeviceService.getCameraManager());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public AidlFacingbackCamera getScanManager() {

        try {
            if (mDeviceService != null) {
                Log.i(TAG, "(getCameraManager().getFacingbackCamera() == null) : "+(getCameraManager().getFacingbackCamera() == null));
                Log.i(TAG, "(AidlFacingbackCamera.Stub.asInterface(getCameraManager().getFacingbackCamera())) : "+(AidlFacingbackCamera.Stub.asInterface(getCameraManager().getFacingbackCamera())));
                return AidlFacingbackCamera.Stub.asInterface(getCameraManager().getFacingbackCamera());
            }
        }catch(RemoteException e){
            e.printStackTrace();
        }
        return null;
    }*/
}