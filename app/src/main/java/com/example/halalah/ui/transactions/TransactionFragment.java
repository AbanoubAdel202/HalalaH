package com.example.halalah.ui.transactions;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.halalah.POSTransaction;

import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.secure.DUKPT_KEY;
import com.example.halalah.storage.CommunicationInfo;

import java.io.InputStream;


public class TransactionFragment extends Fragment implements View.OnClickListener, View.OnKeyListener {

    private TransactionViewModel transactionViewModel;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transaction, container, false);
        Button mada_btn = (Button) root.findViewById(R.id.Mada_btn);
        Button authorization = (Button) root.findViewById(R.id.auth_btn);
        Button preauthorization = (Button) root.findViewById(R.id.Reversal_btn);
        Button naqd = (Button) root.findViewById(R.id.Naqd_btn);
        Button purchaseadvice =(Button)root.findViewById(R.id.Purchase_advice_btn);
        Button cashadvance =(Button)root.findViewById(R.id.cash_advance_btn);
        Button sadad =(Button)root.findViewById(R.id.sadad_btn);
        Button authadvice =(Button)root.findViewById(R.id.authorization_advice_btn);
        Button refund=(Button)root.findViewById(R.id.Refund_btn);
        mada_btn.setOnClickListener(this);
        authorization.setOnClickListener(this);
        preauthorization.setOnClickListener(this);
        naqd.setOnClickListener(this);
        purchaseadvice.setOnClickListener(this);
        refund.setOnClickListener(this);
        sadad.setOnClickListener(this);
        authadvice.setOnClickListener(this);

        //test keys
        DUKPT_KEY.InitilizeDUKPT("0123456789ABCDEFFEDCBA9876543210", BCDASCII.bytesToHexString(PosApplication.getApp().oGTerminal_Operation_Data.m_CurrentKSN));

        // context is handed to posmain
        PosApplication.getApp().oGPOS_MAIN.mcontext = getContext();
        transactionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });


        return root;
    }


    @Override
    public void onClick(View v) {

        //for testing we are dowing preconnect here //mada already doing preconnection on amount confirm
        preConnect();

        switch (v.getId())
        {
            case R.id.Mada_btn:
                Toast.makeText(getContext(),R.string.Purchase,Toast.LENGTH_LONG).show();
                PosApplication.getApp().oGPosTransaction.m_enmTrxType=POSTransaction.TranscationType.PURCHASE;
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction, POSTransaction.TranscationType.PURCHASE);
                break;
            case R.id.Naqd_btn:
                Toast.makeText(getContext(),R.string.NaQD,Toast.LENGTH_LONG).show();
                PosApplication.getApp().oGPosTransaction.m_enmTrxType=POSTransaction.TranscationType.PURCHASE_WITH_NAQD;
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction,POSTransaction.TranscationType.PURCHASE_WITH_NAQD);
                break;
            case R.id.auth_btn: // authorization transaction
                //todo open Authorization menu for other (auth void , Auth ext , auth advice)
                PosApplication.getApp().oGPosTransaction.m_enmTrxType=POSTransaction.TranscationType.AUTHORISATION;
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction,POSTransaction.TranscationType.AUTHORISATION);
                break;
            case R.id.Reversal_btn:
                PosApplication.getApp().oGPosTransaction.m_enmTrxType=POSTransaction.TranscationType.REVERSAL;
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction,POSTransaction.TranscationType.REVERSAL);


                break;
            case R.id.Purchase_advice_btn:

                PosApplication.getApp().oGPosTransaction.m_enmTrxType=POSTransaction.TranscationType.PURCHASE_ADVICE;
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction,POSTransaction.TranscationType.PURCHASE_ADVICE);
                break;
            case R.id.cash_advance_btn:
                //POS_transaction(cash_advance)
                PosApplication.getApp().oGPosTransaction.m_enmTrxType=POSTransaction.TranscationType.CASH_ADVANCE;
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction,POSTransaction.TranscationType.CASH_ADVANCE);
                break;
            case R.id.Refund_btn:
                PosApplication.getApp().oGPosTransaction.m_enmTrxType=POSTransaction.TranscationType.REFUND;
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction,POSTransaction.TranscationType.REFUND);
                break;
            case R.id.authorization_advice_btn:
                PosApplication.getApp().oGPosTransaction.m_enmTrxType=POSTransaction.TranscationType.AUTHORISATION_ADVICE;
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction,POSTransaction.TranscationType.AUTHORISATION_ADVICE);
                break;


        }

    }

    private void preConnect() {
        // open socket to be ready to sending/receiving financial messages
        CommunicationInfo communicationInfo = new CommunicationInfo(PosApplication.getApp().oGPOS_MAIN.mcontext);
        InputStream caInputStream = getResources().openRawResource(R.raw.bks);
        CommunicationsHandler.getInstance(communicationInfo, caInputStream).preConnect();
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            Toast.makeText(getContext(),"default is main",Toast.LENGTH_LONG).show();
        }
        return true;
    }

}
