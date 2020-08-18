package com.example.halalah.ui.admin_settings.communication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.halalah.R;
import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.TMS.TMSManager;

public class CommunicationSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_communication_setting);

        if (findViewById(R.id.container) != null) {
            // if we are being restored from a previous state, then we dont need to do anything and should
            // return or else we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            TMSManager tmsManager = TMSManager.getInstance();
            Connection_Parameters mConnectionParameters = tmsManager.getConnectionParameters();
//            mConnectionParameters = new Connection_Parameters();

            if (mConnectionParameters == null) {
                // add fragment to the fragment container layout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new DefaultCommunicationSettingsFragment()).commit();
            } else {
                // add fragment to the fragment container layout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new ConnectionParametersFragment()).commit();
            }
        }
    }
}
