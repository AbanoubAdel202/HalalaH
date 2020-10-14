package com.example.halalah.ui.totals;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.halalah.MainActivity;
import com.example.halalah.POSTransaction;
import com.example.halalah.POS_MAIN;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.connect.TCPCommunicator;
import com.example.halalah.connect.TCPListener;
import com.example.halalah.storage.CommunicationInfo;

import java.io.InputStream;

public class TotalsFragment extends Fragment implements View.OnClickListener  {

    private TotalsViewModel totalsViewModel;
    private TCPCommunicator tcpClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        totalsViewModel =
                ViewModelProviders.of(this).get(TotalsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_totals, container, false);
        Button recon_btn = (Button)root.findViewById(R.id.recon_btn);
        recon_btn.setOnClickListener(this);
        Button run_tot_btn = (Button)root.findViewById(R.id.run_tot_btn);
        run_tot_btn.setOnClickListener(this);
        Button snapshot_btn = (Button)root.findViewById(R.id.Snapshot_btn);
        snapshot_btn.setOnClickListener(this);
        Button back_btn = (Button)root.findViewById(R.id.back_btn);
        snapshot_btn.setOnClickListener(this);
        preConnect();
        return root;
    }


    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.recon_btn:
              //  preConnect();

                PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.RECONCILIATION;
                 PosApplication.getApp().oGPOS_MAIN.StartReconciliation(true);

                    Intent reconciliation = new Intent(getActivity(), reconciliation.class);
                    startActivity(reconciliation);


              //  disconnect();





            break;
            case R.id.run_tot_btn:
                Intent running = new Intent(getActivity(), running_totals.class);

                startActivity(running);
                break;
            case R.id.Snapshot_btn:
                Intent snapshot = new Intent(getActivity(), snapshot.class);
                startActivity(snapshot);
                break;
            case R.id.back_btn:
                Intent home = new Intent(getActivity(), MainActivity.class);
                startActivity(home);
                break;

        }

    }

    private void disconnect() {
        TCPCommunicator.closeStreams();
    }

    private void preConnect() {
        // open socket to be ready to sending/receiving financial messages
     /*  CommunicationInfo communicationInfo = new CommunicationInfo(getContext());
        InputStream caInputStream = getResources().openRawResource(R.raw.bks);
       CommunicationsHandler.getInstance(communicationInfo, caInputStream).connect();*/

       tcpClient = TCPCommunicator.getInstance();
        tcpClient.init("192.168.8.151", 2030);
        TCPCommunicator.closeStreams();
    }


}
