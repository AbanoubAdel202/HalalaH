package com.example.halalah.ui.transactions;

import android.content.Intent;
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

import com.example.halalah.POS_MAIN;

import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.ui.AmountInputActivity;


public class TransactionFragment extends Fragment implements View.OnClickListener, View.OnKeyListener {

    private TransactionViewModel transactionViewModel;
    POS_MAIN oPos_main =new POS_MAIN();
    POSTransaction oPostransaction =new POSTransaction();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transaction, container, false);
        Button mada_btn = (Button) root.findViewById(R.id.Mada_btn);
        Button authorization = (Button) root.findViewById(R.id.authorization_btn);
        Button preauthorization = (Button) root.findViewById(R.id.preauthorization_btn);
        Button naqd = (Button) root.findViewById(R.id.Naqd_btn);
        Button purchaseadvice =(Button)root.findViewById(R.id.Purchase_advice_btn);
        Button cashadvance =(Button)root.findViewById(R.id.cash_advance_btn);
        mada_btn.setOnClickListener(this);
        authorization.setOnClickListener(this);
        preauthorization.setOnClickListener(this);
        naqd.setOnClickListener(this);
        purchaseadvice.setOnClickListener(this);

        // context is handed to posmain
        oPos_main.mcontext = getContext();
        transactionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });


        return root;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.Mada_btn:
                Toast.makeText(getContext(),R.string.Purchase,Toast.LENGTH_LONG).show();

                oPos_main.Start_Transaction(oPostransaction, POSTransaction.TranscationType.PURCHASE);
                break;
            case R.id.Naqd_btn:
                Toast.makeText(getContext(),R.string.NaQD,Toast.LENGTH_LONG).show();
                oPos_main.Start_Transaction(oPostransaction,POSTransaction.TranscationType.PURCHASE_WITH_NAQD);
                break;
            case R.id.authorization_btn: // authorization transaction

                oPos_main.Start_Transaction(oPostransaction,POSTransaction.TranscationType.AUTHORISATION);
                break;
            case R.id.preauthorization_btn:

                oPos_main.Start_Transaction(oPostransaction,POSTransaction.TranscationType.AUTHORISATION_ADVICE);
                break;
            case R.id.Purchase_advice_btn:
                //POS_transaction(purchase_advice)
                oPos_main.Start_Transaction(oPostransaction,POSTransaction.TranscationType.PURCHASE_ADVICE);
                break;
            case R.id.cash_advance_btn:
                //POS_transaction(cash_advance)
                oPos_main.Start_Transaction(oPostransaction,POSTransaction.TranscationType.CASH_ADVANCE);
                break;

        }

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