package com.example.halalah;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.halalah.sqlite.database.DBManager;
import com.example.halalah.TMS.SAMA_TMS;
import com.example.halalah.storage.CommunicationInfo;
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


    /*******************
     * Terminal Status *
     *******************/
    //Terminal Dial Indicator
    public static String terminal_dial_indicator;// '1'-9', 'A' - 'F' (i.e. total range is '1'-'F').
   //Printer Status
   public static String Printer_Status;  //'0' = No printer. '1' = Out of paper. '2' = Plain paper receipt.
    //Idle Time
    public static String Idle_Time; //hhmmss
    //Magnetic Reader Status
    public static String Magnetic_Reader_Status;// '0'=Okay. '1' = Out of order.
    //Chip Card Reader Status
    public static String Chip_Card_Reader_Status;// '0'=Okay. '1' = Out of order.
    //GPS Location Coordinates
    public static String GPS_Location_Coordinates;// ANNNNNNANNNNNNN e.g. N402646W0795856 Which represents, N 40° 26′ 46″ W 079° 58′ 56″
    //Contactless Reader Status
    public static String Contactless_Reader_Status;// '0'=Okay. '1' = Out of order. '9' = Not supported.
    //Connection Start Time
    public static String Connection_Start_Time; //HHMMSSsss As 24 hour clock
    //Connection End Time
    public static String Connection_End_Time; //HHMMSSsss As 24 hour clock
    //Request Sent Time
    public static String Request_Sent_Time; //HHMMSSsss As 24 hour clock
    //Response Received Time
    public static String Response_Received_Time; //HHMMSSsss As 24 hour clock
    //Performance Timers Reference
    public static String Performance_Timers_Reference; //original RRN for the online authorization or financial message for which the timers refer to.
    //mada EFTPOS specification release version
    public static String mada_EFTPOS_specification_release_version; //The POS should send the version number without dots, and with 2 digits each with a leading zero, if applicable, for the Major, Minor and Patch specification version numbers i.e. Version 6.0.3 should be expressed as 060003 and Version 10.2.0 should be expressed as 100200
    //Connection Details
    public static String Connection_Details; //Connection Priority ‘01’ Primary ‘02’ Secondary,   Network Service Provider (NSP) ‘01’ iNET ‘02’ Mobily ‘03’ Zain ‘04’ Sky Band ‘05’ Geidea ,   Provider ‘01’ STC ‘02’ Mobily ‘03’ Zain ‘04’ Sky Band  , Connection Method ‘01’ Dial-up ‘02’ SIM ‘03’ TCP/IP ‘04’ VSAT ‘05’ DSL ‘06’ WiFi
    //Terminal Online Flag
    public static String Terminal_Online_Flag; // '0'=No action. '1' = Go on-line.
    //Force Reconciliation Flag
    public static String Force_Reconciliation_Flag;// '0'=No action. '1' = Go on-line.




    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
		DBManager.getInstance().init(this);

        mContext = getApplicationContext();
        oGTerminal_Operation_Data.communicationInfo=new CommunicationInfo(this);
        oGTerminal_Operation_Data.communicationInfo.setHostIP("192.168.8.137");
        oGTerminal_Operation_Data.communicationInfo.setHostPort("1000");
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
