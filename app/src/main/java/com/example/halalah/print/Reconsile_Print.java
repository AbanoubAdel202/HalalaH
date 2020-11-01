package com.example.halalah.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.RemoteException;
import android.util.Log;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.HostTotals;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.Utils;
import com.example.halalah.ui.Display_PrintActivity;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;
import com.topwise.cloudpos.aidl.printer.AidlPrinterListener;
import com.topwise.cloudpos.aidl.printer.PrintItemObj;
import com.topwise.cloudpos.data.PrinterConstant;
import com.topwise.template.Align;
import com.topwise.template.PrintTemplate;
import com.topwise.template.TextUnit;

import java.util.ArrayList;

public class Reconsile_Print {
    private static final String TAG = Utils.TAGPUBLIC + Reconsile_Print.class.getSimpleName();

    public static final int MSG_TASK_SHOW_RESULT = 101;
    public static final int MSG_TASK_PRINT = 103;

    public AidlPrinter mPrinterManager;
    private ArrayList<PrintItemObj> mPrintObjs,mPrintObjs2,mPrintObjs3;
    private PrintItemObj mPrintItem1;
    private PrintItemObj mPrintItem2;
    private PrintItemObj mPrintItem3;


    private boolean isHolder = false;


    private Context mContext;

    public String curTime;

    public Reconsile_Print(/*Display_PrintActivity display_PrintActivity*/) {
        mPrinterManager = DeviceTopUsdkServiceManager.getInstance().getPrintManager();
        mPrintObjs = new ArrayList<PrintItemObj>();
        mPrintObjs2 = new ArrayList<PrintItemObj>();
        mPrintObjs3 = new ArrayList<PrintItemObj>();
       // mDisplay_PrintActivity = display_PrintActivity;

    }

    public void printDetail(HostTotals hostTotals) {
        Log.i(TAG, "printDetail, printMsg Started ");
        mContext = PosApplication.getApp().getApplicationContext();
        if (mContext != null) {

            getreconsile_PrintString_terminal(hostTotals);
            getreconsile_PrintString_Host(hostTotals);
            getreconsile_PrintString_footer();
        }



        Log.i(TAG, "startPrint ");
        try {
            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),"hwzs.ttf");
            String startTime = getCurTime();

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
                    Log.d(TAG, "onError: printing error for detail");

                }

                @Override
                public void onPrintFinish() throws RemoteException {
                    Log.i(TAG,"onPrintFinish");


                }


            });

            mPrinterManager.printText(mPrintObjs2, new AidlPrinterListener.Stub(){
                @Override
                public void onError(int i) throws RemoteException {
                    Log.d(TAG, "onError: printing error for detail");

                }

                @Override
                public void onPrintFinish() throws RemoteException {
                    Log.i(TAG,"onPrintFinish");


                }


            });
            mPrinterManager.printText(mPrintObjs3, new AidlPrinterListener.Stub(){
                @Override
                public void onError(int i) throws RemoteException {
                    Log.d(TAG, "onError: printing error for detail");

                }

                @Override
                public void onPrintFinish() throws RemoteException {
                    Log.i(TAG,"onPrintFinish");


                }


            });
        } catch (RemoteException e) {
            e.printStackTrace();
                }

        Log.i(TAG, "startPrint end");
    }

    private void getreconsile_PrintString_terminal(HostTotals hostTotals) {
        Log.i(TAG, "getPurchase_PrintString()");


        mPrintObjs.add(getPrintItemObjs("Reconcile", PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("Hala Merchant", PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("time:"+curTime, PrinterConstant.FontSize.LARGE,true, PrintItemObj.ALIGN.CENTER));
        mPrintObjs.add(getPrintItemObjs("TID:"+PosApplication.getApp().oGPosTransaction.m_sTerminalID,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));
        mPrintObjs.add(getPrintItemObjs("MID:"+PosApplication.getApp().oGPosTransaction.m_sMerchantID,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));
        mPrintObjs.add(getPrintItemObjs("STAN:"+PosApplication.getApp().oGPosTransaction.m_sSTAN,PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.LEFT));
        if(!hostTotals.inBalance) {
            mPrintObjs.add(getPrintItemObjs("OUT OF BALANCE", PrinterConstant.FontSize.LARGE, true, PrintItemObj.ALIGN.CENTER));
        }else
        {
            mPrintObjs.add(getPrintItemObjs("MATCHED BALANCE", PrinterConstant.FontSize.LARGE, true, PrintItemObj.ALIGN.CENTER));
        }
  //      mPrintObjs.add(getPrintItemObjs("هلا للمدفوعات", PrinterConstant.FontSize.LARGE,false, PrintItemObj.ALIGN.RIGHT));

        for(int i=0 ;i<PosApplication.getApp().oGTerminal_Operation_Data.g_NumberOfCardSchemes;i++) {
            mPrintObjs.add(getPrintItemObjs("Cardscheme:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_szCardSchmID, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs.add(getPrintItemObjs("ACQID:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_szCardSchemeAcqID, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs.add(getPrintItemObjs("CreditCount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_lCreditCount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs.add(getPrintItemObjs("CreditAmount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_dCreditAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs.add(getPrintItemObjs("DebitCount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_lDebitCount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs.add(getPrintItemObjs("DebitAmount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_dDebitAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs.add(getPrintItemObjs("AuthorisationCount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_lAuthorisationCount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs.add(getPrintItemObjs("Cash Advance:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_dCashAdvanceAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs.add(getPrintItemObjs("Cash back:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_dCashBackAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));



            // Card Scheme Trx totals Details
            mPrintObjs.add(getPrintItemObjs("OfflinePurchaseCount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_lOfflinePurchaseCount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));

            mPrintObjs.add(getPrintItemObjs("OfflinePurchaseAmount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_dOfflinePurchaseAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));

            mPrintObjs.add(getPrintItemObjs("m_lOnlinePurchaseCount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_lOnlinePurchaseCount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));

            mPrintObjs.add(getPrintItemObjs("m_dOnlinePurchaseAmount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_dOnlinePurchaseAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));

            mPrintObjs.add(getPrintItemObjs("m_lReversalCount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_lReversalCount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));


            mPrintObjs.add(getPrintItemObjs("m_dReversalAmount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_dReversalAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));

            mPrintObjs.add(getPrintItemObjs("m_lRefundCount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_lRefundCount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));

            mPrintObjs.add(getPrintItemObjs("m_dRefundAmount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_dRefundAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));

            mPrintObjs.add(getPrintItemObjs("m_lPurcAdvCompCount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_lPurcAdvCompCount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));

            mPrintObjs.add(getPrintItemObjs("m_dPurAdvCompAmount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_dPurAdvCompAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));

            mPrintObjs.add(getPrintItemObjs("m_lPurWCashBackCount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_lPurWCashBackCount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));

            mPrintObjs.add(getPrintItemObjs("m_dPurWCashBackAmount:" + PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_dPurWCashBackAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));




            mPrintObjs.add(getPrintItemObjs("\n", PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.CENTER));
        }



    }

    private void getreconsile_PrintString_Host(HostTotals hostTotals)
    {

        //printing Host TOTALS

        for(int i=0 ;i<hostTotals.cardSchemeTotals.length;i++) {
            mPrintObjs2.add(getPrintItemObjs("Cardscheme:" + hostTotals.cardSchemeTotals[i].m_szCardSchmID, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs2.add(getPrintItemObjs("ACQID:" + hostTotals.cardSchemeTotals[i].m_szCardSchemeAcqID, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs2.add(getPrintItemObjs("CreditCount:" + hostTotals.cardSchemeTotals[i].m_lCreditCount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs2.add(getPrintItemObjs("CreditAmount:" + hostTotals.cardSchemeTotals[i].m_dCreditAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs2.add(getPrintItemObjs("DebitCount:" + hostTotals.cardSchemeTotals[i].m_lDebitCount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs2.add(getPrintItemObjs("DebitAmount:" + hostTotals.cardSchemeTotals[i].m_dDebitAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs2.add(getPrintItemObjs("AuthorisationCount:" + hostTotals.cardSchemeTotals[i].m_lAuthorisationCount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs2.add(getPrintItemObjs("Cash Advance:" + hostTotals.cardSchemeTotals[i].m_dCashAdvanceAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));
            mPrintObjs2.add(getPrintItemObjs("Cash back:" + hostTotals.cardSchemeTotals[i].m_dCashBackAmount, PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.LEFT));



            mPrintObjs2.add(getPrintItemObjs("\n", PrinterConstant.FontSize.NORMAL, true, PrintItemObj.ALIGN.CENTER));
        }
    }

    private void getreconsile_PrintString_footer()
    {


        mPrintObjs3.add(getPrintItemObjs("------------------------------",PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.CENTER));
        mPrintObjs3.add(getPrintItemObjs("thank you for using Hala",PrinterConstant.FontSize.NORMAL,false,PrintItemObj.ALIGN.CENTER));
        mPrintObjs3.add(getPrintItemObjs("------------------------------",PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.CENTER));
        mPrintObjs3.add(getPrintItemObjs("\n\n\n\n",PrinterConstant.FontSize.NORMAL,true,PrintItemObj.ALIGN.CENTER));

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