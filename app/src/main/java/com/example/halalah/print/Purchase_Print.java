package com.example.halalah.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.Utils;
import com.example.halalah.ui.ConsumeSuccessActivity;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;
import com.topwise.cloudpos.aidl.printer.AidlPrinterListener;
import com.topwise.cloudpos.aidl.printer.Align;
import com.topwise.cloudpos.aidl.printer.ImageUnit;
import com.topwise.cloudpos.aidl.printer.PrintItemObj;
import com.topwise.cloudpos.aidl.printer.PrintTemplate;
import com.topwise.cloudpos.aidl.printer.TextUnit;
import com.topwise.cloudpos.data.PrinterConstant;

import java.util.ArrayList;
import java.util.List;

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

    private ConsumeSuccessActivity mConsumeSuccessActivity;
    private Context mContext;

    public String curTime;

    public Purchase_Print(ConsumeSuccessActivity consumeSuccessActivity) {
        mPrinterManager = DeviceTopUsdkServiceManager.getInstance().getPrintManager();
        mPrintObjs = new ArrayList<PrintItemObj>();

        mConsumeSuccessActivity = consumeSuccessActivity;
        /*mScanSuccessActivity = scanSuccessActivity;*/
    }

    public void printDetail(String printMsg) {
        Log.i(TAG, "printDetail, printMsg = "+printMsg);

        if (mConsumeSuccessActivity != null) {
            mContext = mConsumeSuccessActivity;
            getPurchase_PrintString(printMsg);
            //getPurchase_PrintFakeString();
        }

       /* if (mScanSuccessActivity != null) {
            mContext = mScanSuccessActivity;
            //getScanPrintString(printMsg);
            getScanPrintFakeString();
        }*/

        Log.i(TAG, "startPrint ");
        try {
            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),"hwzs.ttf");
            String startTime = getCurTime();

            PrintTemplate template = new PrintTemplate(mContext,typeface);
            //template.setStrokeWidth(0.1f);
            int textSize = TextUnit.TextSize.NORMAL;

          template.add(new TextUnit("هلا للمدفوعات",24,Align.CENTER).setLineSpacing(10));

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
//                        if (mConsumeSuccessActivity != null) {
//                            mConsumeSuccessActivity.mHandle.sendEmptyMessage(MSG_TASK_PRINT);
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
//                        if (mConsumeSuccessActivity != null) {
//                            mConsumeSuccessActivity.mHandle.sendMessage(message);
//                        }
//                        if (mScanSuccessActivity != null) {
//                            mScanSuccessActivity.mHandle.sendMessage(message);
//                        }
//                    }
                }

               /* @Override
                public void onError(int errorCode) throws RemoteException {
                    Log.i(TAG,"onError:"+errorCode);

                    if (errorCode == PrinterConstant.PrinterState.PRINTER_STATE_NOPAPER) {
                        Message message = new Message();
                        message.what = MSG_TASK_SHOW_RESULT;
                        Bundle data = new Bundle();
                        data.putString("message", mContext.getString(R.string.result_check_paper));
                        message.setData(data);
                        if (mConsumeSuccessActivity != null) {
                            mConsumeSuccessActivity.mHandle.sendMessage(message);
                        }
                        if (mScanSuccessActivity != null) {
                            mScanSuccessActivity.mHandle.sendMessage(message);
                        }
                    } else {
                        Message message = new Message();
                        message.what = MSG_TASK_SHOW_RESULT;
                        Bundle data = new Bundle();
                        data.putString("message", String.valueOf(errorCode));
                        message.setData(data);
                        if (mConsumeSuccessActivity != null) {
                            mConsumeSuccessActivity.mHandle.sendMessage(message);
                        }
                        if (mScanSuccessActivity != null) {
                            mScanSuccessActivity.mHandle.sendMessage(message);
                        }
                    }
                }*/
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "startPrint end");
    }

    private void getPurchase_PrintString(String printMsg) {
        Log.i(TAG, "getPurchase_PrintString()");
       /* *//**add by zongli for fake print data *//*
        String amount = PosApplication.getApp().oGPosTransaction.m_sTrxAmount;
        String cardNo = PosApplication.getApp().oGPosTransaction.m_sPAN;
        String firCardNo = null;
        String mid = null;
        String lastCardNo = null;
        if(cardNo != null){
            int cardLength = cardNo.length();
            firCardNo = cardNo.substring(0,6);
            lastCardNo = cardNo.substring(cardLength - 4);
            mid = "******";
            cardNo = firCardNo + mid + lastCardNo;
        }
        *//**add end *//*
        String[] print = printMsg.split("\\n");
        for (int i = 0; i < print.length; i++) {
            if (print[i].indexOf("~") != -1) {
                if (print[i].indexOf("Cardholder's signature") != -1) {
                    mPrintObjs.add(getPrintItemObjs("Cardholder's signature:", PrinterConstant.FontSize.LARGE, PrintItemObj.ALIGN.LEFT));
                }
                *//**add by zongli for fake print data *//*
                else if(print[i].indexOf("card number") != -1){
                    if(cardNo != null){
                        mPrintObjs.add(getPrintItemObjs("card number:"+cardNo, PrinterConstant.FontSize.NORMAL, PrintItemObj.ALIGN.LEFT));
                    }
                } else if(print[i].indexOf("Amount") != -1){
                    if(amount != null){
                        mPrintObjs.add(getPrintItemObjs("Amount:RMB "+amount, PrinterConstant.FontSize.LARGE, PrintItemObj.ALIGN.LEFT));
                    }
                }
                *//**add end *//*
                else {
                    mPrintObjs.add(getPrintItemObjs(print[i].replace("~", ""), PrinterConstant.FontSize.LARGE, PrintItemObj.ALIGN.LEFT));
                }
            } else if (print[i].indexOf("Signing order") != -1 ||
                    print[i].indexOf("Transaction voucher") != -1) {
                if (print[i].indexOf("Transaction voucher") != -1 && print[i].indexOf("Cardholder Union") != -1) {
                    isHolder = true;
                } else {
                    isHolder = false;
                }
                mPrintObjs.add(getPrintItemObjs(print[i], PrinterConstant.FontSize.LARGE, PrintItemObj.ALIGN.CENTER));
            }
            *//**add by zongli for fake print data *//*
            else if(print[i].indexOf("time") != -1){
                if(curTime != null){
                    mPrintObjs.add(getPrintItemObjs("time:"+curTime, PrinterConstant.FontSize.NORMAL, PrintItemObj.ALIGN.LEFT));
                }
            }
            *//**add end *//*
            else {
                mPrintObjs.add(getPrintItemObjs(print[i], PrinterConstant.FontSize.NORMAL, PrintItemObj.ALIGN.LEFT));
            }
        }
        mPrintObjs.add(getPrintItemObjs("\n\n\n\n", PrinterConstant.FontSize.NORMAL, PrintItemObj.ALIGN.LEFT));

*/

        mPrintObjs.add(getPrintItemObjs("PURCHASE Transaction", PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("Hala Merchant", PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("time:"+curTime, PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("TID:"+PosApplication.getApp().oGPosTransaction.m_sTerminalID,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));
        mPrintObjs.add(getPrintItemObjs("MID:"+PosApplication.getApp().oGPosTransaction.m_sMerchantID,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));
        //mPrintObjs.add(getPrintItemObjs("هلا للمدفوعات", PrinterConstant.FontSize.LARGE, PrintItemObj.ALIGN.RIGHT));
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

    private void getScanPrintString(String printMsg) {

        /**add by zongli for fake print data */
        String amount ="1000";// PosApplication.getApp().mConsumeData.getAmount();
        /**add end */

        String[] print = printMsg.split("\\n");
        for (int i = 0; i < print.length; i++) {
            if (print[i].indexOf("~") != -1) {
                if (print[i].indexOf("交易类型") != -1) {
                    mPrintObjs.add(getPrintItemObjs(print[i].replace("~", ""), PrinterConstant.FontSize.LARGE, true,PrintItemObj.ALIGN.LEFT));
                }
                /**add by zongli for fake print data */
                else if(print[i].indexOf("金额") != -1){
                    if(amount != null){
                        mPrintObjs.add(getPrintItemObjs("金额:RMB "+amount, PrinterConstant.FontSize.LARGE, true,PrintItemObj.ALIGN.LEFT));
                    }
                }
                /**add end */
                else {
                    mPrintObjs.add(getPrintItemObjs(print[i].replace("~", ""), PrinterConstant.FontSize.NORMAL,true, PrintItemObj.ALIGN.LEFT));
                }
            } else if (print[i].indexOf("签购单") != -1 ||
                    print[i].indexOf("交易凭证") != -1) {
                if (print[i].indexOf("交易凭证") != -1 && print[i].indexOf("持卡人联") != -1) {
                    isHolder = true;
                } else {
                    isHolder = false;
                }
                mPrintObjs.add(getPrintItemObjs(print[i], PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
            } /**add by zongli for fake print data */
            else if(print[i].indexOf("时间") != -1){
                if(curTime != null){
                    mPrintObjs.add(getPrintItemObjs("交易时间:"+curTime, PrinterConstant.FontSize.NORMAL,true, PrintItemObj.ALIGN.LEFT));
                }
            }
            /**add end */
            else {
                mPrintObjs.add(getPrintItemObjs(print[i], PrinterConstant.FontSize.NORMAL,true, PrintItemObj.ALIGN.LEFT));
            }
        }
        mPrintObjs.add(getPrintItemObjs("\n\n\n\n", PrinterConstant.FontSize.NORMAL,true, PrintItemObj.ALIGN.LEFT));
    }

    private void getPurchase_PrintFakeString() {
        Log.i(TAG, "getPurchase_PrintString()");
        /**add by zongli for fake print data */
        final String amount ="100"; // PosApplication.getApp().mConsumeData.getAmount();
        String cardNo = "15000077";//PosApplication.getApp().mConsumeData.getCardno();
        String firCardNo = null;
        String mid = null;
        String lastCardNo = null;
        if(cardNo != null){
            int cardLength = cardNo.length();
            firCardNo = cardNo.substring(0,6);
            lastCardNo = cardNo.substring(cardLength - 4);
            mid = "******";
            cardNo = firCardNo + mid + lastCardNo;
        }
        /**add end */
        final String finalCardNo = cardNo;
        mPrintObjs = new ArrayList<PrintItemObj>() {
            {
                add(new PrintItemObj(mContext.getString(R.string.print_purchase_order), 16, true, PrintItemObj.ALIGN.CENTER));
                add(new PrintItemObj(mContext.getString(R.string.print_transaction_documents), 16, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_version), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_business_name), 8, true, PrintItemObj.ALIGN.LEFT));

                add(new PrintItemObj(mContext.getString(R.string.print_terminal_number), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_acquirer), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_card_number)+ finalCardNo, 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_transaction_type), 16, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_validity), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_document_number), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_reference_no), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_time)+curTime, 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_amount)+amount, 16, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_dotted_line), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_ticket_number), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_dotted_line), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_cardholder_signature), 16, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj("\n"));
                add(new PrintItemObj(mContext.getString(R.string.print_confirm_transaction), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj("\n\n"));
            }
        };
    }

    private void getScanPrintFakeString() {
        /*final String amount = PosApplication.getApp().mConsumeData.getAmount();

        mPrintObjs = new ArrayList<PrintItemObj>() {
            {
                add(new PrintItemObj(mContext.getString(R.string.print_purchase_order), 16, true, PrintItemObj.ALIGN.CENTER));
                add(new PrintItemObj(mContext.getString(R.string.print_transaction_documents), 16, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_version), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_business_name), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_business_number), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_terminal_number), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_ticket_number_scan), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_reference_no_scan), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_transaction_type_scan), 16, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_amount)+amount, 16, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_time)+curTime, 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_no_password_scan), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj(mContext.getString(R.string.print_notice_scan), 8, true, PrintItemObj.ALIGN.LEFT));
                add(new PrintItemObj("\n\n"));
            }
        };*/

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