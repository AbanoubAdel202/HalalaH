package com.example.halalah.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.Utils;
import com.example.halalah.ui.Declined_Display_Print;
import com.example.halalah.ui.Display_PrintActivity;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;
import com.topwise.cloudpos.aidl.printer.AidlPrinterListener;
import com.topwise.cloudpos.aidl.printer.PrintItemObj;
import com.topwise.cloudpos.data.PrinterConstant;
import com.topwise.template.Align;
import com.topwise.template.PrintTemplate;
import com.topwise.template.TextUnit;

import java.util.ArrayList;

public class Declined_Print {
    private static final String TAG = Utils.TAGPUBLIC + Declined_Print.class.getSimpleName();

    public static final int MSG_TASK_SHOW_RESULT = 101;
    public static final int MSG_TASK_PRINT = 103;

    public AidlPrinter mPrinterManager;
    private ArrayList<PrintItemObj> mPrintObjs;
    private PrintItemObj mPrintItem1;
    private PrintItemObj mPrintItem2;
    private PrintItemObj mPrintItem3;


    private boolean isHolder = false;

    private Declined_Display_Print mDeclinedActivity;
    private Context mContext;

    public String curTime;

    public Declined_Print(Declined_Display_Print DeclinedActivity) {
        mPrinterManager = DeviceTopUsdkServiceManager.getInstance().getPrintManager();
        mPrintObjs = new ArrayList<PrintItemObj>();

        mDeclinedActivity = DeclinedActivity;

    }

    public void printDetail(String messagetxt) {
        Log.i(TAG, "printDetail, printMsg Started ");

        if (mDeclinedActivity != null) {
            mContext = mDeclinedActivity;
            getPurchase_PrintString(messagetxt);

        }



        Log.i(TAG, "startPrint ");
        try {
            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "hwzs.ttf");
            curTime = getCurTime();

            PrintTemplate template = new PrintTemplate(mContext,typeface);
            template.setStrokeWidth(0.1f);
            int textSize = TextUnit.TextSize.NORMAL;

            template.add(new TextUnit("هلا للمدفوعات",24, Align.CENTER).setLineSpacing(10));

            try {
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.halalah);


                mPrinterManager.addRuiImage(bitmap, 0);


            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mPrinterManager.addRuiImage(template.getPrintBitmap(),0);
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
//                        if (mDeclinedActivity != null) {
//                            mDeclinedActivity.mHandle.sendEmptyMessage(MSG_TASK_PRINT);
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
//                        if (mDeclinedActivity != null) {
//                            mDeclinedActivity.mHandle.sendMessage(message);
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

    private void getPurchase_PrintString(String messagetxt) {
        Log.i(TAG, "getPurchase_PrintString()");


        mPrintObjs.add(getPrintItemObjs("PURCHASE Transaction", PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("Hala Merchant", PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("time:"+curTime, PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("TID:"+PosApplication.getApp().oGPosTransaction.m_sTerminalID,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));
        mPrintObjs.add(getPrintItemObjs("MID:"+PosApplication.getApp().oGPosTransaction.m_sMerchantID,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));
  //      mPrintObjs.add(getPrintItemObjs("هلا للمدفوعات", PrinterConstant.FontSize.LARGE,false, PrintItemObj.ALIGN.RIGHT));
        mPrintObjs.add(getPrintItemObjs("Declined", PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("Action code : "+ PosApplication.getApp().oGPosTransaction.m_sActionCode, PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs(messagetxt , PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));

        mPrintObjs.add(getPrintItemObjs("Amount:"+PosApplication.getApp().oGPosTransaction.m_sTrxAmount,PrinterConstant.FontSize.LARGE,true,PrintItemObj.ALIGN.LEFT));
        mPrintObjs.add(getPrintItemObjs(PosApplication.getApp().oGPosTransaction.m_sPAN,PrinterConstant.FontSize.LARGE,true,PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("RRN:"+PosApplication.getApp().oGPosTransaction.m_sRRNumber,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));
        mPrintObjs.add(getPrintItemObjs(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sCard_Scheme_Name_English+"      CVM:"+PosApplication.getApp().oGPosTransaction.m_enmTrxCVM.toString() ,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));

        mPrintObjs.add(getPrintItemObjs("------------------------------",PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("thank you for using Hala",PrinterConstant.FontSize.NORMAL,false,PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("------------------------------",PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs(mContext.getString(R.string.print_confirm_transaction), 8, true, PrintItemObj.ALIGN.LEFT));
        mPrintObjs.add(getPrintItemObjs("\n\n\n\n",PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("going ICC DATA:"+PosApplication.getApp().oGPosTransaction.m_sICCRelatedTags,PrinterConstant.FontSize.NORMAL,false,PrintItemObj.ALIGN.LEFT));

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