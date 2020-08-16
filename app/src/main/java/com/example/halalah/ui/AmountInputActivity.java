package com.example.halalah.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.storage.CommunicationInfo;
import com.topwise.cloudpos.aidl.AidlDeviceService;
import com.topwise.cloudpos.aidl.buzzer.AidlBuzzer;
import com.topwise.cloudpos.aidl.led.AidlLed;

import java.io.InputStream;
import java.util.List;

public class AmountInputActivity extends Activity implements View.OnClickListener {
    private static final String TAG = AmountInputActivity.class.getSimpleName();

    private static final int MSG_TIME_UPDATE = 100;

    private TextView mTextAmount;
    private TextView mTextAmount2;
    private Button mBtnConfirm;
    private StringBuilder mAmountBuilder;
    private StringBuilder mAmount;
    private StringBuilder mNAQD_Amount;
    private AidlDeviceService serviceManager;
    private AidlLed mAidlLed = DeviceTopUsdkServiceManager.getInstance().getLedManager();
    private AidlBuzzer iBeeper;
    public static final String TOPWISE_SERVICE_ACTION = "topwise_cloudpos_device_service";

    private int mTime = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_amount_input);

        ServiceConnection conn = new ServiceConnection(){

            @Override
            public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
                Log.d(TAG,"aidlService服务连接成功");
                if(serviceBinder != null){	//绑定成功
                    serviceManager = AidlDeviceService.Stub.asInterface(serviceBinder);
                    try {
                        iBeeper = AidlBuzzer.Stub.asInterface(serviceManager.getBuzzer());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG,"AidlService服务断开了");
            }
        };
        Intent intent = new Intent();
        intent.setAction(TOPWISE_SERVICE_ACTION);
        final Intent eintent = new Intent(createExplicitFromImplicitIntent(this,intent));
        boolean flag =bindService(eintent, conn, Context.BIND_AUTO_CREATE);





        /*ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(R.string.title_consume);*/
        mBtnConfirm = (Button) findViewById(R.id.btn_search_card);
        mTextAmount = (TextView) findViewById(R.id.edit_amount);
      //  mTextAmount2 = (TextView) findViewById(R.id.edit_naqd_amount);

        mAmountBuilder = new StringBuilder("R");

        mBtnConfirm.setText(R.string.amount_input_search_card);
        mHandle.sendEmptyMessage(MSG_TIME_UPDATE);

        /*if( PosApplication.getApp().oGPosTransaction.m_enmTrxType== POSTransaction.TranscationType.PURCHASE
                ||PosApplication.getApp().oGPosTransaction.m_enmTrxType== POSTransaction.TranscationType.AUTHORISATION
                ||PosApplication.getApp().oGPosTransaction.m_enmTrxType==POSTransaction.TranscationType.AUTHORISATION_ADVICE
                ||PosApplication.getApp().oGPosTransaction.m_enmTrxType==POSTransaction.TranscationType.PURCHASE_ADVICE) {


        }
         else if (PosApplication.getApp().oGPosTransaction.m_enmTrxType== POSTransaction.TranscationType.PURCHASE_WITH_NAQD)
        {
           // todo show naqd amount


        }
        else if (PosApplication.getApp().oGPosTransaction.m_enmTrxType== POSTransaction.TranscationType.REFUND) {

            //TODO show RRN and other DATA activity
        }
        else if(PosApplication.getApp().oGPosTransaction.m_enmTrxType== POSTransaction.TranscationType.AUTHORISATION_EXTENSION) {

        }*/
    }

    private void preConnect() {
        // open socket to be ready to sending/receiving financial messages
        CommunicationInfo communicationInfo = new CommunicationInfo(this);
        InputStream caInputStream = getResources().openRawResource(R.raw.bks);
        CommunicationsHandler.getInstance(communicationInfo, caInputStream).preConnect();
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIME_UPDATE:
                    if (mTime == 0) {
                        finish();
                    } else {
                        mHandle.sendEmptyMessageDelayed(MSG_TIME_UPDATE, 1000);
                    }
                    mTime--;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {



        switch (v.getId()) {
            case R.id.btn_0:
                setText("0");

                try {
                    mAidlLed.setLed(0,true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_1:
                setText("1");

                try {
                    mAidlLed.setLed(1,true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_2:
                setText("2");
                try {
                    mAidlLed.setLed(2,true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_3:
                setText("3");
                try {
                    mAidlLed.setLed(3,true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_4:
                setText("4");
                try {
                    mAidlLed.setLed(4,true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_5:
                setText("5");
                try {
                    iBeeper.beep(0,100);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_6:
                setText("6");
                try {
                    iBeeper.beep(1,100);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_7:
                setText("7");
                try {
                    iBeeper.beep(2,100);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_8:
                setText("8");
                try {
                    iBeeper.beep(3,100);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_9:
                setText("9");
                try {
                    iBeeper.beep(4,100);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_back:
                if (mAmountBuilder.length() > 1) {
                    mAmountBuilder.delete(mAmountBuilder.length() - 1, mAmountBuilder.length());
                }

                try {
                    mAidlLed.setLed(0,false);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                setText(null);
                break;
            case R.id.btn_clear:
                mAmountBuilder.delete(1, mAmountBuilder.length());
                setText(null);
                break;
            case R.id.btn_cancle:
                finish();
                break;
            case R.id.btn_search_card:
                preConnect();
                String sAmount = mAmount.substring(1);


                PosApplication.getApp().oGPosTransaction.m_sTrxAmount= sAmount;

                switch (PosApplication.getApp().oGPosTransaction.m_enmTrxType) {
                    case PURCHASE:
                    case PURCHASE_ADVICE:
                    case AUTHORISATION:
                        try {
                            Intent intent = new Intent(this, SearchCardActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.i(TAG, e.toString());
                        }
                        break;
                case AUTHORISATION_ADVICE:

                    // todo authorization data filling in activity
                    try {
                        Intent intent = new Intent(this, SearchCardActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.i(TAG, e.toString());
                    }


                    /////////////////note : it's not decided yet if all data detail will be input after amount or screens based on transaction type
                    case REFUND:
                        try {
                            Intent intent = new Intent(this, Refund_InputActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.i(TAG, e.toString());
                        }
                        break;
                    case PURCHASE_WITH_NAQD:
                        try {
                            Intent intent = new Intent(this, P_NAQD_InputActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.i(TAG, e.toString());
                        }
                        break;
                    case SADAD_BILL:
                        //todo check SADAD flow
                    case CASH_ADVANCE:
                        //todo check Cash advance flow
                    case AUTHORISATION_EXTENSION:
                        PosApplication.getApp().oGPosTransaction.m_sTrxAmount="0";


                }


                finish();

            default:
                break;
        }
    }

    private void setText(String charNum) {
        String temp = mAmountBuilder.toString();
        Log.i(TAG, "temp = " + temp);

        if (temp.length() > 12) {
            return;
        }

        if (charNum != null) {
            mAmountBuilder.append(charNum);
        }

        temp = mAmountBuilder.toString();
        Log.i(TAG, "temp = " + temp);

        if (temp.equals("R 0")) {
            temp = "R";
            mAmountBuilder.delete(1, 2);
        }
        mAmount = new StringBuilder(temp);
        Log.i(TAG, "mAmount before = " + mAmount);
        for (int i = 0; i < 4 - mAmountBuilder.length(); i++) {
            mAmount.insert(1, "0");
        }
        Log.i(TAG, "mAmount = " + mAmount);
        mAmount.insert(mAmount.length() - 2, ".");
        mTextAmount.setText(mAmount);

        temp = temp.substring(1);
        Log.i(TAG, "temp.isEmpty() = " + temp.isEmpty());
        if (temp.isEmpty() || Long.parseLong(temp) == 0) {
            Log.i(TAG, "false");
            mBtnConfirm.setEnabled(false);
        } else {
            Log.i(TAG, "true");
            mBtnConfirm.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        mHandle.removeMessages(MSG_TIME_UPDATE);
    }

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

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
}