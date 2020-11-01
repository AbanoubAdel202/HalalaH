package com.example.halalah.util;

import android.os.RemoteException;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.topwise.cloudpos.aidl.buzzer.AidlBuzzer;
import com.topwise.cloudpos.aidl.led.AidlLed;
/** Header POS Main
 \Class Name: LEDnbuzzmanager
 \Param  :
 \Return :
 \Pre    :
 \Post   :
 \Author	: Mostafa Hussiny
 \DT		: 25/10/2020
 \Des    : main control of led and buzzer
 */
public class LEDnbuzzManager {

    private static LEDnbuzzManager mLEDnbuzzManager;
    private static AidlLed mAidlLed = null;
    private static AidlBuzzer mbeepManager = null;



    LEDnbuzzManager()

    {
        mAidlLed = DeviceTopUsdkServiceManager.getInstance().getLedManager();
        mbeepManager = DeviceTopUsdkServiceManager.getInstance().getBeepManager();
    }
    public static LEDnbuzzManager getinstence()
    {
        if(mLEDnbuzzManager == null)
        {
            mLEDnbuzzManager = new LEDnbuzzManager();
        }

        return mLEDnbuzzManager;
    }

/************
    Constant name Constant value description
    ALL                  0(int) All bits
    RED                 1(int) Red
    GREEN                2(int) Green
    YELLOW              3(int) Yellow
     BLUE                4(int) blue
 *******************************/
    public void setled(int LEDid,boolean onflag){
        try {
            if(mAidlLed != null){
                mAidlLed.setLed(LEDid , onflag);
            }
        } catch (
                RemoteException e) {
            e.printStackTrace();
        }
    }


    /***
    Constant name Constant value description
    NORAML               0 (int)      Beep  once
    SUCCESS              1(int)     Success beep
    FAIL                2(int) Failed beep
    INTERVAL                3(int) Intermittent
                                 beep
    ERROR                4(int) Error beep


     (int mode，int ms)


     **/
    public void setbuzzer(int value,int ms)
    {
        try {
            mbeepManager.beep(value,ms);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stopbuzzer()
    {
        try {
            mbeepManager.stopBeep();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
