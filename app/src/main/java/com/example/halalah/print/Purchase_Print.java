package com.example.halalah.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.Utils;
import com.example.halalah.ui.Display_PrintActivity;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;
import com.topwise.cloudpos.aidl.printer.AidlPrinterListener;
import com.topwise.cloudpos.aidl.printer.PrintItemObj;
import com.topwise.cloudpos.data.PrinterConstant;

import java.util.ArrayList;

public class Purchase_Print {
    private static final String TAG = Utils.TAGPUBLIC + Purchase_Print.class.getSimpleName();

    public static final int MSG_TASK_SHOW_RESULT = 101;
    public static final int MSG_TASK_PRINT = 103;

    public AidlPrinter mPrinterManager;
    private ArrayList<PrintItemObj> mPrintObjs;
    private PrintItemObj mPrintItem1;
    private PrintItemObj mPrintItem2;
    private PrintItemObj mPrintItem3;


    private boolean isHolder = false;

    private Display_PrintActivity mDisplay_PrintActivity;
    private Context mContext;

    public String curTime;

    public Purchase_Print(Display_PrintActivity display_PrintActivity) {
        mPrinterManager = DeviceTopUsdkServiceManager.getInstance().getPrintManager();
        mPrintObjs = new ArrayList<PrintItemObj>();

        mDisplay_PrintActivity = display_PrintActivity;
        /*mScanSuccessActivity = scanSuccessActivity;*/
    }

    public void printDetail(String printMsg) {
        Log.i(TAG, "printDetail, printMsg = "+printMsg);

        if (mDisplay_PrintActivity != null) {
            mContext = mDisplay_PrintActivity;
            getPurchase_PrintString(printMsg);

        }



        Log.i(TAG, "startPrint ");
        try {
            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "hwzs.ttf");
            String startTime = getCurTime();

//            PrintTemplate template = new PrintTemplate(mContext,typeface);
//            template.setStrokeWidth(0.1f);
//            int textSize = TextUnit.TextSize.NORMAL;
//
//          template.add(new TextUnit("هلا للمدفوعات",24, Align.CENTER).setLineSpacing(10));

            try {
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.halalah);


                mPrinterManager.addRuiImage(bitmap, 0);


            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
          //  mPrinterManager.addRuiImage(template.getPrintBitmap(),0);
            mPrinterManager.printRuiQueue(new AidlPrinterListener.Stub() {
                @Override
                public void onError(int i) throws RemoteException {

                }

                @Override
                public void onPrintFinish() throws RemoteException {

                }
            });
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {


            mPrinterManager.printText(mPrintObjs, new AidlPrinterListener.Stub(){
                @Override
                public void onError(int i) throws RemoteException {

                }

                @Override
                public void onPrintFinish() throws RemoteException {
                    Log.i(TAG,"onPrintFinish");

//                    if (isHolder) {
//                        if (mDisplay_PrintActivity != null) {
//                            mDisplay_PrintActivity.mHandle.sendEmptyMessage(MSG_TASK_PRINT);
//                        }
//                        if (mScanSuccessActivity != null) {
//                            mScanSuccessActivity.mHandle.sendEmptyMessage(MSG_TASK_PRINT);
//                        }
//                    } else {
//                        Message message = new Message();
//                        message.what = MSG_TASK_SHOW_RESULT;
//                        Bundle data = new Bundle();
//                        data.putString("message", mContext.getString(R.string.result_print_success));
//                        message.setData(data);
//                        if (mDisplay_PrintActivity != null) {
//                            mDisplay_PrintActivity.mHandle.sendMessage(message);
//                        }
//                        if (mScanSuccessActivity != null) {
//                            mScanSuccessActivity.mHandle.sendMessage(message);
//                        }
//                    }
                }


            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "startPrint end");
    }

    private void getPurchase_PrintString(String printMsg) {
        Log.i(TAG, "getPurchase_PrintString()");


        mPrintObjs.add(getPrintItemObjs("PURCHASE Transaction", PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("Hala Merchant", PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("time:"+curTime, PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("TID:"+PosApplication.getApp().oGPosTransaction.m_sTerminalID,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));
        mPrintObjs.add(getPrintItemObjs("MID:"+PosApplication.getApp().oGPosTransaction.m_sMerchantID,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));
        mPrintObjs.add(getPrintItemObjs("هلا للمدفوعات", PrinterConstant.FontSize.LARGE,false, PrintItemObj.ALIGN.RIGHT));
        mPrintObjs.add(getPrintItemObjs("Amount:"+PosApplication.getApp().oGPosTransaction.m_sTrxAmount,PrinterConstant.FontSize.LARGE,true,PrintItemObj.ALIGN.LEFT));
        mPrintObjs.add(getPrintItemObjs(PosApplication.getApp().oGPosTransaction.m_sPAN,PrinterConstant.FontSize.LARGE,true,PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("RRN:"+PosApplication.getApp().oGPosTransaction.m_sRRNumber,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));
        mPrintObjs.add(getPrintItemObjs(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCard_Scheme_Name_English+"      "+PosApplication.getApp().oGPosTransaction.m_enmTrxCVM.toString() ,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));

        mPrintObjs.add(getPrintItemObjs("------------------------------",PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("thank you for using Hala",PrinterConstant.FontSize.NORMAL,false,PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("------------------------------",PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs(mContext.getString(R.string.print_confirm_transaction), 8, true, PrintItemObj.ALIGN.LEFT));
        mPrintObjs.add(getPrintItemObjs("\n\n\n\n",PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.CENTER));

    }


    private PrintItemObj getPrintItemObjs(String printText, int fontSize,boolean bold, PrintItemObj.ALIGN align) {
        PrintItemObj printItemObj = new PrintItemObj(printText, fontSize, bold, align);
        return printItemObj;
    }
    /**add by zongli for fake print data */
    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public String getCurTime(){
        return curTime;
    }
    /**add end */
}