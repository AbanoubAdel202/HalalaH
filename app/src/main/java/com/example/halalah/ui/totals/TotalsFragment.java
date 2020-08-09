package com.example.halalah.ui.totals;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.halalah.MainActivity;
import com.example.halalah.R;

public class TotalsFragment extends Fragment implements View.OnClickListener {

    private TotalsViewModel totalsViewModel;


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

        return root;
    }
    public void onClick()
    {

    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.recon_btn:
                Intent reconciliation = new Intent(getActivity(), reconciliation.class);

                startActivity(reconciliation);
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


}
