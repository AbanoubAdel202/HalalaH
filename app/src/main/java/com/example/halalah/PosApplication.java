package com.example.halalah;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.halalah.TMS.SAMA_TMS;
import com.example.halalah.database.table.DBManager;
import com.topwise.cloudpos.aidl.emv.level2.EmvTerminalInfo;

/** Header PosApplication
 \Class Name: POSTransaction
 \Param  :
 \Return :
 \Pre    :
 \Post   :
 \Author	: Mostafa Hussiny
 \DT		: 4/27/2020
 \Des    : initiate POS_main class and POSTransaction class for global calling during application start.
 */
public class PosApplication extends Application{
    private static final String TAG = Utils.TAGPUBLIC + PosApplication.class.getSimpleName();
    public static boolean testapp=false;

    private Context mContext;
    private static PosApplication mPosApplication=new PosApplication();

    /*******************
     *   Message types *
     *******************/
    public static String MTI_Financial_Request="1200";
    public static String MTI_Financial_Reponse="1210";
    public static String MTI_Financial_Transaction_Advice="1220";
    public static String MTI_Financial_Transaction_Advice_repeat="1221";
    public static String MTI_Financial_Transaction_Advice_response="1230";
    public static String MTI_Reversal_Advice="1420";
    public static String MTI_Reversal_Advice_Repeat="1421";
    public static String MTI_Reversal_Advice_Reponse="1430";
    public static String MTI_Authorisation_Request="1100";
    public static String MTI_Authorisation_Response="1110";
    public static String MTI_Authorisation_Advice="1120";
    public static String MTI_Authorisation_Advice_Repeat="1121";
    public static String MTI_Authorisation_Advice_Response="1130";
    public static String MTI_File_Action_Request="1304";
    public static String MTI_File_Action_Request_Repeat="1305";
    public static String MTI_File_Action_Request_Response="1314";
    public static String MTI_Terminal_Reconciliation_Advice="1524";
    public static String MTI_Terminal_Reconciliation_Advice_Repeat="1525";
    public static String MTI_Terminal_Reconciliation_Advice_Response="1534";
    public static String MTI_Administrative_Notification="1644";
    public static String MTI_Network_Management_Request="1804";
    public static String MTI_Network_Management_Response="1814";


    /****************
     * DukptKeyType *
     ***************/
    public final static int DUKPT_BDK = 0;
    public final static int DUKPT_IPEK = 1;
    public final static int DUKPT_MAK = 12;
    public final static int DUKPT_PEK = 13;

    /**********************
     * Global Transaction *
     **********************/
    public POS_MAIN oGPOS_MAIN = new POS_MAIN();

    /**********************
     * Global Transaction *
     **********************/
    public POSTransaction oGPosTransaction = new POSTransaction();

    /**********************
     * TMS SAMA *
     **********************/
    public SAMA_TMS oGSama_TMS = new SAMA_TMS();

    /**************************
     * Terminal opertation DATA *
     **************************/
   public Terminal_Operation_Data oGTerminal_Operation_Data =new Terminal_Operation_Data();

    /**********************
     * Terminal Regestration  *
     **********************/
    public Terminal_Registeration oGTerminal_Registeration = new Terminal_Registeration();





    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
		DBManager.getInstance().init(this);
        mContext = getApplicationContext();
        mPosApplication = this;

    }

    public static PosApplication getApp() {
       return mPosApplication;
    }

    public void getDeviceManager() {
        DeviceTopUsdkServiceManager.getInstance();
    }




    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(TAG, "onTerminate");
        System.exit(0);
    }


}
