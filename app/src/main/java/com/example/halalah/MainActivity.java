package com.example.halalah;

import com.example.halalah.TMS.Card_Scheme;
import com.example.halalah.TMS.Public_Key;
import com.example.halalah.TMS.TMSManager;
import com.example.halalah.cloudpos.data.AidlErrorCode;
import com.example.halalah.connect.TCPCommunicator;
import com.example.halalah.emv.EmvManager;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.storage.SaveLoadFile;
import com.example.halalah.util.BytesUtil;
import com.topwise.cloudpos.aidl.emv.level2.EmvTerminalInfo;

import android.Manifest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.halalah.TMS.SAMA_TMS;
import com.example.halalah.cloudpos.data.PinpadConstant;
import com.example.halalah.emv.EmvManager;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.registration.RegistrationData;
import com.example.halalah.registration.view.ITransaction;
import com.example.halalah.registration.view.RegistrationActivity;
import com.example.halalah.secure.DUKPT_KEY;
import com.google.android.material.navigation.NavigationView;
import com.topwise.cloudpos.aidl.emv.level2.EmvTerminalInfo;
import com.topwise.cloudpos.aidl.led.AidlLed;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;

import org.parceler.Parcels;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity implements ITransaction.View {

    private static final int REGISTRATION_REQUEST_CODE = 1000;
    private static final int REGISTRATION_REQUEST_BYPASS = 2000;
    private AppBarConfiguration mAppBarConfiguration;
    Context context;
    ProgressDialog mProgressDialog;
    ProgressDialog mProgressDialog2;
    AlertDialog.Builder builder;
    private TCPCommunicator tcpClient;

    private boolean isRegistrationInProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        CommunicationInfo communicationInfo = new CommunicationInfo(this);
 //       communicationInfo.setHostIP("192.168.8.138");
//        communicationInfo.setHostPort("1001");
//        communicationInfo.setTPDU("6000230000");
        builder = new AlertDialog.Builder(this);


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

        //showLoading("Terminal Initializing please wait...");

        //Terminal_Initialization();
        //StartMADA_APP();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.i("wxz", ">" + DeviceTopUsdkServiceManager.getInstance().getDeviceService());
                    if (DeviceTopUsdkServiceManager.getInstance().getDeviceService() != null) {
                        break;
                    }
                    SystemClock.sleep(1000);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Terminal_Initialization();
                        StartMADA_APP();
                    }
                });
            }
        }).start();
/*
        Terminal_Initialization();

        StartMADA_APP();*/



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

       //Toast.makeText(this,"This is Home Screen",Toast.LENGTH_LONG).show();


    }
    @Override
    public void onUserInteraction(){

        // if we need to do someting if user interacting
        //listen for user action

    }

    @Override
    protected void onResume() {
        super.onResume();
        PosApplication.getApp().oGPosTransaction.Reset();
        POS_MAIN.load_TermData();
        checkRegistration();
        if(PosApplication.getApp().oGTerminal_Operation_Data.bDeSAF_flag) {
            if (PosApplication.getApp().oGTerminal_Operation_Data.saf_info.CheckSAFLimits()) {
                //todo "make Temp screen service unavailable"

                PosApplication.getApp().oGPOS_MAIN.DeSAF(SAF_Info.DESAFtype.FULL);
            }
        }

    }

    private boolean Terminal_Initialization() {

        // todo Terminal initialization
        POS_MAIN.check_hardware();
        POS_MAIN.load_Terminal_configuration_file(); //TMS initial parameter
        //add default terminal data
        //POS_MAIN.load_TermData();
        Getlocation();

        /*new Thread(() -> {
            SystemClock.sleep(1000);
            if(!PosApplication.getApp().oGTerminal_Operation_Data.m_TMS_Downloaded)
              //  DownLoadParM();//Dummy TMS download
            //downLoadKeys();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                }
            });
        }).start();*/

        return true;


    }

    private void Getlocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {

                Toast.makeText(
                        getBaseContext(),
                        "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                                + loc.getLongitude(), Toast.LENGTH_SHORT).show();
                String longitude = "Longitude: " + loc.getLongitude();
                Log.d("longitude", longitude);
                String latitude = "Latitude: " + loc.getLatitude();
                Log.v("latitude", latitude);

                //todo format as needed for longand lat
                //PosApplication.getApp().oGTerminal_Operation_Data.GPS_Location_Coordinates=
                /*------- To get city name from coordinates -------- */
                String cityName = null;
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.ENGLISH);
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        System.out.println(addresses.get(0).getLocality());
                        cityName = addresses.get(0).getLocality();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                        + cityName;

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });



    }

    private void DownloadTMS()
    {
        preConnect();
        PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.TMS_FILE_DOWNLOAD;
        PosApplication.getApp().oGPOS_MAIN.StartTMSDownload(false ,this);
    }
    private void preConnect() {
        // open socket to be ready to sending/receiving financial messages
      /*  CommunicationInfo communicationInfo = new CommunicationInfo(this);
        InputStream caInputStream = getResources().openRawResource(R.raw.bks);
        CommunicationsHandler.getInstance(communicationInfo, caInputStream).connect();*/

        tcpClient = TCPCommunicator.getInstance();
        tcpClient.init( PosApplication.getApp().oGTerminal_Operation_Data.Hostip, PosApplication.getApp().oGTerminal_Operation_Data.Hostport);
       // TCPCommunicator.closeStreams();
    }


    private void StartMADA_APP()
    {

        //Load_Terminal_operation_data();
        Initialize_Security();
       // checkRegistration();
        if(PosApplication.getApp().oGTerminal_Operation_Data.m_TMS_Downloaded)
          POS_MAIN.Get_Terminal_Transaction_limits();

    }

    private void checkRegistration() {
        if (isRegistrationInProgress) {
            showLoading("Registering terminal. Please wait ...");
            return;
        }
        boolean bRegistered = PosApplication.getApp().oGTerminal_Operation_Data.m_bregistered;

        if (bRegistered==true) {
        //if (true) {
            //Initialize_EMV_Configuration();
            Initialize_CTLS_configuration();
           // if(!PosApplication.getApp().oGTerminal_Operation_Data.m_TMS_Downloaded)
                //DownloadTMS();
            hideLoading();
        } else {
            showRegistrationScreen();
        }
    }

    private void showLoading(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    private void Initialize_Security()
    {
        //DUKPT_KEY.InitilizeDUKPT("0123456789ABCDEFFEDCBA9876543210", BCDASCII.bytesToHexString(PosApplication.getApp().oGTerminal_Operation_Data.m_CurrentKSN));
        DUKPT_KEY.InitilizeDUKPT(PosApplication.getApp().oGTerminal_Operation_Data.m_szBDK, BCDASCII.bytesToHexString(PosApplication.getApp().oGTerminal_Operation_Data.m_CurrentKSN));

        //todo initialize security parameter
    }
    private void Initialize_EMV_Configuration()
    {
        EmvManager mEMVmanager = EmvManager.getInstance();

        EmvTerminalInfo EMVterminalParam = new EmvTerminalInfo();
        EMVterminalParam.setUcTerminalType(Byte.parseByte(PosApplication.getApp().oGSama_TMS.retailer_data.m_sEMV_Terminal_Type));
        EMVterminalParam.setAucTerminalCountryCode(PosApplication.getApp().oGSama_TMS.retailer_data.m_sTerminal_Country_Code.getBytes());
        EMVterminalParam.setAucTransCurrencyCode(PosApplication.getApp().oGSama_TMS.retailer_data.m_sTerminal_Currency_Code.getBytes());
        //EMVterminalParam.setAucTerminalCapabilities(PosApplication.getApp().oGSama_TMS.retailer_data.m_sTerminal_Capability.getBytes());

        mEMVmanager.setEmvTerminalInfo(EMVterminalParam);


    }
    private void Initialize_CTLS_configuration()
    {
        //todo initialize contactless parameters
    }
    private void Load_Terminal_operation_data()
    {
        PosApplication.getApp().oGTerminal_Operation_Data=SaveLoadFile.loadTeminal_operation_Data();
    }
    @Override
    public void showRegistrationScreen() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, REGISTRATION_REQUEST_CODE);
    }

    @Override
    public void showTMSupdateScreen() {

        isRegistrationInProgress = false;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                String currentcount = Integer.toString(PosApplication.getApp().oGTerminal_Operation_Data.TMS_currentcount);
                String endCount = Integer.toString(PosApplication.getApp().oGTerminal_Operation_Data.TMS_endcount);
                mProgressDialog.setMessage("Downloading: " + currentcount + " OF" + endCount);
                if(currentcount == endCount) {
                    hideLoading();
                }
            }
        });

    }


    @Override
    public void showError(int errorMessageId) {
        hideLoading();
        isRegistrationInProgress = false;
        //Toast.makeText(context, getString(errorMessageId), Toast.LENGTH_SHORT).show();

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, getString(errorMessageId), Toast.LENGTH_SHORT).show();
                showalert(getString(errorMessageId));

            }
        });
        checkRegistration();

    }

    @Override
    public void showError(String errorMessageString) {
        hideLoading();
        Toast.makeText(context, errorMessageString, Toast.LENGTH_SHORT).show();

        isRegistrationInProgress = false;

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, errorMessageString, Toast.LENGTH_SHORT).show();
                showalert(errorMessageString);

            }
        });
        checkRegistration();

    }

    @Override
    public void showConnectionStatus(int connectionStatus) {
        Toast.makeText(context, "Connection code = " + connectionStatus, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRegistrationSuccess() {
        hideLoading();
        isRegistrationInProgress = false;
        Toast.makeText(context, "Terminal registered successfully !! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "onActivityResult");
        switch (requestCode) {
            case REGISTRATION_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null && data.hasExtra("registrationData")) {
                    RegistrationData registrationData = Parcels.unwrap(data.getParcelableExtra("registrationData"));
                    PosApplication.getApp().oGPosTransaction.setTerminalRegistrationData(registrationData);
                    // based on Moamen Ahmed Registeration file , Terminal_Registeration.java also
                    isRegistrationInProgress = true;
                    PosApplication.getApp().oGTerminal_Registeration.StartRegistrationProcess(
                            PosApplication.getApp().oGPosTransaction, this);
                } else {
                    if(resultCode == RESULT_CANCELED)
                    {
                        PosApplication.getApp().oGTerminal_Operation_Data.m_bregistered=true;
                    }
                    showError(R.string.registration_error);
                }
                break;




        }
    }

    public void showalert(String message){

                //Uncomment the below code to Set the message and title from the strings.xml file
                builder.setMessage(message) .setTitle("error registeration");

                //Setting message manually and performing action on button click
                builder.setMessage("not registered ")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Error registeration");
                alert.show();
            }


}
