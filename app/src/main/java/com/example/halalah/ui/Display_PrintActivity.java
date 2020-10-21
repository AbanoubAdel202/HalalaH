package com.example.halalah.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.Utils;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.print.Purchase_Print;

import com.example.halalah.secure.DUKPT_KEY;
import com.topwise.cloudpos.aidl.led.AidlLed;
import com.topwise.cloudpos.data.PrinterConstant;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Display_PrintActivity extends Activity implements View.OnClickListener {
    private static final String TAG = Utils.TAGPUBLIC + Display_PrintActivity.class.getSimpleName();

    private static final int MSG_TIME_UPDATE = 100;
    private Intent intent;
    private TextView mAmount;
    private TextView mTransactionType;
    private TextView mPANno;
    private TextView mRecieptno;
    private TextView mRRN;
    private TextView mOperatorNum;
    private TextView mTransactiontime;
    private TextView mPinBlock;
    private TextView mKsnValue;

    private Bundle mBundle;


    private String mShowMsg;
    private String mPrintHolder;
    private String mPrintMerchant;
    private String mPrintBank;

    private Purchase_Print mPurchasePrint;
    private AlertDialog.Builder mAlertDialog;

    private int mTime = 31;

    private AidlLed mAidlLed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_consume_result);

         mAmount = (TextView) findViewById(R.id.consume_amount);
        mTransactionType = (TextView) findViewById(R.id.consume_type);
        mPANno = (TextView) findViewById(R.id.consume_cardnum);
        mRecieptno = (TextView) findViewById(R.id.consume_venchor_num);
        mRRN = (TextView) findViewById(R.id.consume_reference_num);
        mOperatorNum = (TextView) findViewById(R.id.operator_num);
        mTransactiontime = (TextView) findViewById(R.id.consume_time);
        mPinBlock = (TextView) findViewById(R.id.pin_block);
        mKsnValue = (TextView) findViewById(R.id.ksn_value);


        byte[] pin = new byte[0];
        if (PosApplication.getApp().oGPosTransaction.m_sTrxPIN!=null) {
            pin = PosApplication.getApp().oGPosTransaction.m_sTrxPIN.getBytes();
        }
        byte[] ksnValue = DUKPT_KEY.getKSN();
        Log.i(TAG, "pin: " + BCDASCII.bytesToHexString(pin));
        Log.d(TAG, "ksnValue: " + BCDASCII.bytesToHexString(ksnValue));
        if (pin != null && pin.length > 0) {
            mPinBlock.setText(BCDASCII.bytesToHexString(pin));
        } else {
            mPinBlock.setText("null");
        }
        if (ksnValue != null && ksnValue.length > 0) {
            mKsnValue.setText(BCDASCII.bytesToHexString(ksnValue));
        } else {
            mKsnValue.setText("null");
        }

        mPurchasePrint = new Purchase_Print(this);
        Log.d(TAG, "Printing: Purchase_Print ");
        mPurchasePrint.setCurTime(getCurTime());
        mBundle = this.getIntent().getExtras();


        ActionBar actionBar = this.getActionBar();
//        actionBar.setTitle(R.string.title_consume);

//        mTransaction = TransactionSub.getInstance();

        mAlertDialog = new AlertDialog.Builder(this);


        filldisplaydata();

        //add end

        mHandle.sendEmptyMessage(MSG_TIME_UPDATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                /*AidlPboc mPboc = AidlPboc.Stub.
                        asInterface(DeviceTopUsdkServiceManager.getInstance().getEMVL2());
                if (mPboc != null) {
                    try {
                        mPboc.endPBOC();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }*/
                finish();
                break;
            case R.id.btn_print:
                try {
                    int printState = mPurchasePrint.mPrinterManager.getPrinterState();
                    Log.i(TAG, "printState = " + printState);

                    if (printState == PrinterConstant.PrinterState.PRINTER_STATE_NOPAPER) {
                        showDialog(null, getString(R.string.result_need_paper));
                    } else {
                        mPurchasePrint.printDetail();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            default:
                break;
        }
    }

    public Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIME_UPDATE:
                    mTime--;
                    if (mTime == 0) {
                        finish();
                    } else if (mTime == 29) {
//                        boolean isRfCardExit = mTransaction.mRFCardDevice.isCardExist();
//                        boolean isSmartCardExit = mTransaction.mSmartCardDevice.isCardExist();
//
//                        Log.i(TAG, "isRfCardExit = "+isRfCardExit);
//                        Log.i(TAG, "isSmartCardExit = "+isSmartCardExit);
//
//                        if (isRfCardExit || isSmartCardExit) {
//                            Log.i(TAG, "dialog.show");
//                            showDialog(null, getString(R.string.result_remove_ic_card));
//                        }
                        mHandle.sendEmptyMessageDelayed(MSG_TIME_UPDATE, 1000);
                    } else {
                        mHandle.sendEmptyMessageDelayed(MSG_TIME_UPDATE, 1000);
                    }
                    break;
                case Purchase_Print.MSG_TASK_SHOW_RESULT:
                    showDialog(null, msg.getData().getString("message"));
                    break;
                case Purchase_Print.MSG_TASK_PRINT:
                    showPrintDialog();
                    break;
                default:
                    break;
            }
        }
    };

  /*  private void getPrintMessage(byte[] field47) {
        Log.i(TAG, "getPrintMessage, field47 " + BCDASCII.bytesToHexString(field47));

        byte[] printData = null;
        byte[] printConfirmHolder = null;
        byte[] printConfirmMerchant = null;
        byte[] printConfirmBank = null;

        int startIndex = 0;
        int endIndex = 0;
        int index = 0;

        int printLen = field47.length - 5;
        byte[] printByte = new byte[printLen];
        System.arraycopy(field47, 5, printByte, 0, printLen);
        Log.i(TAG, "printByte: " + BCDASCII.bytesToHexString(printByte));
        try {
            Log.i(TAG, "printByte: " + (new String(printByte, "GBK")));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (int i = 0; i < printLen; i++) {
            if (printByte[i] == 0x10 || printByte[i] == 0x11
                    || printByte[i] == 0x12 || printByte[i] == 0x13) {
                Log.i(TAG, "1__i = " + i + ", printByte[i] = " + printByte[i]);
                startIndex = i + 1;
            }
            if (printByte[i] == 0x02) {
                Log.i(TAG, "2__i = " + i + ", printByte[i] = " + printByte[i]);
                endIndex = i;
                if (index == 0) {
                    Log.i(TAG, "0 = " + (endIndex - startIndex));
                    printData = new byte[endIndex - startIndex];
                    System.arraycopy(printByte, startIndex, printData, 0, endIndex - startIndex);
                    index++;
                } else if (index == 1) {
                    Log.i(TAG, "1 = " + (endIndex - startIndex));
                    printConfirmHolder = new byte[endIndex - startIndex];
                    System.arraycopy(printByte, startIndex, printConfirmHolder, 0, endIndex - startIndex);
                    index++;
                } else if (index == 2) {
                    Log.i(TAG, "2 = " + (endIndex - startIndex));
                    printConfirmMerchant = new byte[endIndex - startIndex];
                    System.arraycopy(printByte, startIndex, printConfirmMerchant, 0, endIndex - startIndex);
                    index++;
                } else if (index == 3) {
                    Log.i(TAG, "3 = " + (endIndex - startIndex));
                    printConfirmBank = new byte[endIndex - startIndex];
                    System.arraycopy(printByte, startIndex, printConfirmBank, 0, endIndex - startIndex);
                    index++;
                }
            }
        }

        try {
            mShowMsg = new String(printData, "GBK");
//            if (mTransaction.mIsNoSign) {
//                mPrintHolder = "POS签购单\n==交易凭证(持卡人联)==\n" + mShowMsg + getString(R.string.print_no_sign)+"\n\n\n\n\n";
//                mPrintMerchant = "POS签购单\n===交易凭证(商户联)===\n" + mShowMsg + getString(R.string.print_no_sign)+"\n\n\n\n\n";
//                mPrintBank = "POS签购单\n===交易凭证(银行联)===\n" + mShowMsg + getString(R.string.print_no_sign)+"\n\n\n\n\n";
//            } else {
            mPrintHolder = "POS HALA.com\n== محمد:هلا==\n" + mShowMsg + new String(printConfirmHolder, "GBK") + "\n\n\n\n\n";
            mPrintMerchant = "POS签购单\n===交易凭证(商户联)===\n" + mShowMsg + new String(printConfirmMerchant, "GBK") + "\n\n\n\n\n";
            mPrintBank = "POS签购单\n===hala هلا===\n" + mShowMsg + new String(printConfirmBank, "GBK") + "\n\n\n\n\n";
//            }
            Log.d(TAG, "mShowMsg = " + mShowMsg);
            Log.d(TAG, "mPrintHolder = " + mPrintHolder);
            Log.d(TAG, "mPrintMerchant = " + mPrintMerchant);
            Log.d(TAG, "mPrintBank = " + mPrintBank);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
     }*/
    private void filldisplaydata() {

                mAmount.setText(PosApplication.getApp().oGPosTransaction.m_sTrxAmount);
                mTransactionType.setText(PosApplication.getApp().oGPosTransaction.m_enmTrxType.toString());

                mPANno.setText(PosApplication.getApp().oGPosTransaction.m_sPAN);

                mRecieptno.setText(PosApplication.getApp().oGPosTransaction.m_sSTAN);

                mOperatorNum.setText(PosApplication.getApp().oGPosTransaction.m_sTerminalID);

                mRRN.setText(PosApplication.getApp().oGPosTransaction.m_sRRNumber);
                mTransactiontime.setText(PosApplication.getApp().oGPosTransaction.m_sTrxDateTime);


    }




    private String getCurTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    /**
     * add end
     */

    private void showDialog(String title, String message) {
        if (mAlertDialog != null) {
            mAlertDialog = new AlertDialog.Builder(this);
            mAlertDialog.setMessage(message);
            mAlertDialog.setCancelable(false);
            mAlertDialog.setPositiveButton(R.string.result_dialog_ok, null);
            mAlertDialog.show();
        }
    }

    private void showPrintDialog() {
        if (mAlertDialog != null) {
            mAlertDialog = new AlertDialog.Builder(this);
            mAlertDialog.setMessage(getString(R.string.result_print_more));
            mAlertDialog.setCancelable(false);
            mAlertDialog.setNegativeButton(R.string.result_dialog_cancle, null);
            mAlertDialog.setPositiveButton(R.string.result_dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        int printState = mPurchasePrint.mPrinterManager.getPrinterState();
                        Log.i(TAG, "printState = " + printState);

                        if (printState == PrinterConstant.PrinterState.PRINTER_STATE_NOPAPER) {
                            showDialog(null, getString(R.string.result_need_paper));
                        } else {
                            mPurchasePrint.printDetail();
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
            mAlertDialog.show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mTime = 30;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed()");
    }


    @Override
    protected void onPause() {
        super.onPause();
        mAidlLed = DeviceTopUsdkServiceManager.getInstance().getLedManager();
        try {
            if (mAidlLed != null) {
                mAidlLed.setLed(0, false);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        mHandle.removeMessages(MSG_TIME_UPDATE);
        mHandle.removeMessages(Purchase_Print.MSG_TASK_SHOW_RESULT);
        mHandle.removeMessages(Purchase_Print.MSG_TASK_PRINT);

        if (mAlertDialog != null) {
            mAlertDialog = null;
        }
    }
}
