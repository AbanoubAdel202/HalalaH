package com.example.halalah.ui.operation_setting;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.connect.TCPCommunicator;
import com.example.halalah.network_settings;
import com.example.halalah.registration.view.ITransaction;
import com.example.halalah.storage.CommunicationInfo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.InputStream;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Setting extends Fragment implements View.OnClickListener , ITransaction.View  {

    private SettingViewModel mViewModel;
    private TCPCommunicator tcpClient;
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder mAlertdialog;
    private ITransaction.View mView;
    public static Setting newInstance() {
        return new Setting();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        Button TMSdownload = (Button) root.findViewById(R.id.TMS_btn);
        Button networksettingbtn = root.findViewById(R.id.Networksettingsbtn);
       TMSdownload.setOnClickListener(this);
        networksettingbtn.setOnClickListener(this);
       /* CommunicationInfo communicationInfo = new CommunicationInfo(getContext());
        InputStream caInputStream = getResources().openRawResource(R.raw.bks);
        CommunicationsHandler.getInstance(communicationInfo, caInputStream).connect();*/
        mView = this;
                return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        // TODO: Use the ViewModel

    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.TMS_btn:
                mAlertdialog = new AlertDialog.Builder(getContext());
                mAlertdialog.setTitle("select TMS Download Type");
                mAlertdialog.setMessage("Partial or Full");
                mAlertdialog.setPositiveButton("FULL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PosApplication.getApp().oGTerminal_Operation_Data.m_sTMSHeader="3060000";

                       DownloadTMS();

                    }
                });
                mAlertdialog.setNegativeButton("Partial", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PosApplication.getApp().oGTerminal_Operation_Data.m_sTMSHeader="3040000";
                        DownloadTMS();
                    }
                });
                mAlertdialog.show();

               /* PosApplication.getApp().oGPOS_MAIN.mcontext = getContext();
                PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.TMS_FILE_DOWNLOAD;
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction,POSTransaction.TranscationType.TMS_FILE_DOWNLOAD,null);
                */


                break;
            case R.id.Networksettingsbtn:
                Intent networksetting = new Intent(getActivity(), network_settings.class);
                startActivity(networksetting);
                break;
        }
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


    private void showLoading(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
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


    @Override
    public void showRegistrationScreen() {

    }

    @Override
    public void showTMSupdateScreen() {



            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mProgressDialog.setMessage("TMS Downloading : "+ Integer.toString(PosApplication.getApp().oGTerminal_Operation_Data.TMS_currentcount) + " OF " + Integer.toString(PosApplication.getApp().oGTerminal_Operation_Data.TMS_endcount));

                    if (PosApplication.getApp().oGTerminal_Operation_Data.TMS_currentcount == PosApplication.getApp().oGTerminal_Operation_Data.TMS_endcount)
                        hideLoading();
                }
            });








    }

    @Override
    public void showError(int errorMessageId) {

    }

    @Override
    public void showError(String errorMessageString) {

    }

    @Override
    public void showConnectionStatus(int connectionStatus) {

    }

    @Override
    public void showRegistrationSuccess() {

    }

    public void DownloadTMS()
    {
        preConnect();
        PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.TMS_FILE_DOWNLOAD;
        PosApplication.getApp().oGTerminal_Operation_Data.TMS_currentcount=0;
        //  PosApplication.getApp().oGTerminal_Operation_Data.m_sTMSHeader ="3060000";
        if(PosApplication.getApp().oGTerminal_Operation_Data.TMS_currentcount==0)
            showLoading("Terminal Downloading  TMS please wait...");
        PosApplication.getApp().oGPOS_MAIN.StartTMSDownload(false,mView);
    }
}
