package com.example.halalah;
import com.topwise.cloudpos.aidl.serialport.AidlSerialport;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.halalah.TMS.AID_Data;
import com.example.halalah.TMS.Public_Key;
import com.example.halalah.TMS.SAMA_TMS;
import com.example.halalah.iso8583.BCDASCII;

import com.example.halalah.secure.DUKPT_KEY;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.topwise.cloudpos.aidl.emv.AidlPboc;
import com.topwise.cloudpos.aidl.emv.TerminalParam;
import com.topwise.cloudpos.aidl.led.AidlLed;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;
import com.topwise.cloudpos.data.PinpadConstant;

import androidx.activity.OnBackPressedDispatcher;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Context context;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView date = findViewById(R.id.Date);
        TextView time =findViewById(R.id.TIME);
        TextView status = findViewById(R.id.Status);
        context = getApplicationContext();
        // Mostafa 21/4/2020 added to time and date for action bar
        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
        date.setText(s);
        CharSequence t = DateFormat.format("HH:MM",d.getTime());
        time.setText(t);

        PosApplication.getApp().getDeviceManager();

        // Mostafa 21/4/2020 added to remove bars for full screen application
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        */

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_transaction, R.id.nav_Merchant, R.id.nav_totals,R.id.nav_reports,R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Terminal Initializing please wait...");
        mProgressDialog.show();


        Terminal_Initialization();

       // StartMADA_APP();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        PosApplication.getApp().oGPosTransaction.Reset();
       /* Intent returnhome = new Intent(this,MainActivity.class);

        startActivity(returnhome);*/
       Toast.makeText(this,"This is Home Screen",Toast.LENGTH_LONG).show();




    }
    @Override
    public void onUserInteraction(){

        // if we need to do someting if user interacting
        //listen for user action
        //Start_transaction(pos_transaction,Purchase)
    }

    @Override
    protected void onResume() {
        super.onResume();
        PosApplication.getApp().oGPosTransaction.Reset();
        AidlLed mAidlLed = DeviceTopUsdkServiceManager.getInstance().getLedManager();
        try {
            if(mAidlLed != null){
                mAidlLed.setLed(0 , false);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean Terminal_Initialization(){

        // todo Terminal initialization
                //check hardware();
                // load Terminal configuration file(); //TMS parameter

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                downLoadParM();
                downLoadKeys();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mProgressDialog!= null && mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }

                    }
                });
            }
        }).start();

return true;



    }
    private boolean downLoadParM(){
      AidlPboc mPbocManager = DeviceTopUsdkServiceManager.getInstance().getPbocManager();
        try {
            //Read the IC card parameter configuration file under assert and load the relevant parameters into the EMV kernel
            try {
                boolean updateResult = false;
                boolean flag = true;
                int i = 0;
                String success = "";
                String fail = "";
                // 获取IC卡参数信息
                  mPbocManager.updateAID(0x03, null);
                  mPbocManager.updateCAPK(0x03, null);

                InputStream ins = this.getAssets().open("icparam/ic_param.txt");
                if (ins != null && ins.available() != 0x00) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(ins));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        // 未到达文件末尾
                        if (null != line) {
                            if (line.startsWith("AID")) {
                                // 更新AID
                                updateResult = mPbocManager.updateAID(0x01, line.split("=")[1]);
                                Log.d("update AID:",String.valueOf(updateResult)+"AID data:"+line);

                            } else { // 更新RID
                                updateResult = mPbocManager.updateCAPK(0x01, line.split("=")[1]);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return true;
    }


    private boolean downLoadKeys(){

        final AidlPinpad pinpadManager = DeviceTopUsdkServiceManager.getInstance().getPinpadManager(0);
        final byte[] tmk = BCDASCII.hexStringToBytes("89F8B0FDA2F2896B9801F131D32F986D89F8B0FDA2F2896B");
        final byte[] tak = BCDASCII.hexStringToBytes("92B1754D6634EB22");
        final byte[] tpk = BCDASCII.hexStringToBytes("B5E175AC5FD8DD8A03AD23A35C5BAB6B");
        final byte[] trk = BCDASCII.hexStringToBytes("744185122EEC284830694CAD383B4F7A");
        boolean mIsSuccess =false;

        try {
            mIsSuccess = pinpadManager.loadMainkey(0, tmk, null);

            mIsSuccess = pinpadManager.loadWorkKey(PinpadConstant.WKeyType.WKEY_TYPE_MAK, 0, 0, tak, null);

            mIsSuccess = pinpadManager.loadWorkKey(PinpadConstant.WKeyType.WKEY_TYPE_PIK, 0, 0, tpk, null);

            mIsSuccess = pinpadManager.loadWorkKey(PinpadConstant.WKeyType.WKEY_TYPE_TDK, 0, 0, trk, null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    return true;
    }

    private void StartMADA_APP()
    {   Load_Terminal_operation_data();
        boolean bRegistered=PosApplication.getApp().oGTerminal_Operation_Data.m_bregistered;
        Initialize_Security();
        if(bRegistered==true) {
            Initialize_EMV_Configuration();
            Initialize_CTLS_configuration();
        }
        else{

            while (!PosApplication.getApp().oGTerminal_Operation_Data.m_bregistered)  //todo add tries counter due to connection failures
            {
                POS_MAIN oPos_Main = new POS_MAIN();
                oPos_Main.Start_Transaction(PosApplication.getApp().oGPosTransaction,POSTransaction.TranscationType.TERMINAL_REGISTRATION);

            }
        }
    }
    private void Initialize_Security()
    {
        DUKPT_KEY.InitilizeDUKPT("0123456789ABCDEF0123456789ABCDEF","47FFF00111100000016D");

        //todo initialize security parameter
    }
    private void Initialize_EMV_Configuration()
    {
        AidlPboc mPbocManager = DeviceTopUsdkServiceManager.getInstance().getPbocManager();

        TerminalParam terminalParam = new TerminalParam();
        terminalParam.setTerminalType(Byte.parseByte(PosApplication.getApp().oGSama_TMS.retailer_data.m_sEMV_Terminal_Type));
        terminalParam.setCountryCode(PosApplication.getApp().oGSama_TMS.retailer_data.m_sTerminal_Country_Code.getBytes());
        terminalParam.setCurrencyCode(PosApplication.getApp().oGSama_TMS.retailer_data.m_sTerminal_Currency_Code.getBytes());
        terminalParam.setCapability(PosApplication.getApp().oGSama_TMS.retailer_data.m_sTerminal_Capability.getBytes());
            try {
                 mPbocManager.setTerminalParam(terminalParam);
            }
            catch (RemoteException e)
            {
               e.printStackTrace();
            }


        try {
            boolean updateResult = false;
            boolean flag = true;

            String success = "";
            String fail = "";
            // Get IC card parameter information
            mPbocManager.updateAID(0x03, null);
            mPbocManager.updateCAPK(0x03, null);

            AID_Data AIDdata[] = PosApplication.getApp().oGSama_TMS.GET_AID_Data_PARAM();


            for(int index=0;index<AIDdata.length;index++)
            {
                String sAIDdata = POS_MAIN.FormatAIDData(AIDdata[index]);
                updateResult = mPbocManager.updateAID(0x01, sAIDdata);

            }

            Public_Key CAPK[] = PosApplication.getApp().oGSama_TMS.Get_all_CAPK();

            for (int index = 0; index<CAPK.length; index++)
            {
                String sCAPK=POS_MAIN.FormatCAKeys(CAPK[index]);
                updateResult = mPbocManager.updateCAPK(0x01,sCAPK);
            }



        } catch (RemoteException e) {
            e.printStackTrace();
        }


        ///////////////////////////


    }
    private void Initialize_CTLS_configuration()
    {
        //todo initialize contactless parameters
    }
    private void Load_Terminal_operation_data()
    {
        //todo Load Terminal operation data from database
    }






}
