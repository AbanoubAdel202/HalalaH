package com.example.halalah.ui.operation_setting;

import androidx.lifecycle.ViewModelProviders;

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
import com.example.halalah.storage.CommunicationInfo;

import java.io.InputStream;

public class Setting extends Fragment implements View.OnClickListener {

    private SettingViewModel mViewModel;

    public static Setting newInstance() {
        return new Setting();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        Button TMSdownload = (Button) root.findViewById(R.id.TMS_btn);
       TMSdownload.setOnClickListener(this);
        CommunicationInfo communicationInfo = new CommunicationInfo(getContext());
        InputStream caInputStream = getResources().openRawResource(R.raw.bks);
        CommunicationsHandler.getInstance(communicationInfo, caInputStream).connect();
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

                PosApplication.getApp().oGPOS_MAIN.mcontext = getContext();
                PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.TMS_FILE_DOWNLOAD;
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction,POSTransaction.TranscationType.TMS_FILE_DOWNLOAD);


        }
    }
}
