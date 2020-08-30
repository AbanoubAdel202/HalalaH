package com.example.halalah.ui.admin_settings.communication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.halalah.R;
import com.example.halalah.TMS.Connection;
import com.example.halalah.TMS.Gprs;
import com.example.halalah.TMS.Wifi;
import com.example.halalah.storage.SharedPreferencesManager;

import static com.example.halalah.ui.admin_settings.communication.ISaveConnection.SAVE_MODE_DEFAULT;

public class DefaultCommunicationSettingsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ISaveConnection iSaveConnection;

    private Button cancelBtn;
    private Button saveBtn;
    private Spinner connectionTypeSp;
    private GPRSFragment gprsFragment;
    private WifiFragment wifiFragment;
    private SharedPreferencesManager sharedPreferencesManager;
    private Connection connection;

    public DefaultCommunicationSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.communication_setting_default, container, false);
        connectionTypeSp = rootView.findViewById(R.id.connection_type_sp);
        connectionTypeSp.setOnItemSelectedListener(this);

        sharedPreferencesManager = new SharedPreferencesManager(getActivity());

        if (rootView.findViewById(R.id.container) != null) {
            // if we are being restored from a previous state, then we dont need to do anything and should
            // return or else we could end up with overlapping fragments.
            if (savedInstanceState == null) {

                connection = sharedPreferencesManager.getConnection();
                if (connection instanceof Gprs) {
                    connectionTypeSp.setSelection(1);
                } else {
                    connectionTypeSp.setSelection(0);
                }

                getChildFragmentManager().beginTransaction()
                        .add(R.id.container, getFragment()).commit();
            }
        }

        saveBtn = rootView.findViewById(R.id.commu_para_save);
        saveBtn.setOnClickListener(this);
        cancelBtn = rootView.findViewById(R.id.commu_para_cancle);
        cancelBtn.setOnClickListener(this);

        return rootView;
    }

    private Fragment getFragment() {

        String connectionType = connectionTypeSp.getSelectedItem().toString().trim();

        if (connectionType.equalsIgnoreCase("gprs")) {

            if (gprsFragment != null) {
                iSaveConnection = gprsFragment;
                return gprsFragment;
            }

            if (connection instanceof Gprs) {
                gprsFragment = GPRSFragment.newInstance((Gprs) connection);
            } else {
                gprsFragment = GPRSFragment.newInstance(null);
            }

            iSaveConnection = gprsFragment;
            return gprsFragment;
        } else {

            if (wifiFragment != null) {
                iSaveConnection = wifiFragment;
                return wifiFragment;
            }

            if (connection instanceof Wifi) {
                wifiFragment = WifiFragment.newInstance((Wifi) connection);
            } else {
                wifiFragment = WifiFragment.newInstance(null);
            }

            iSaveConnection = wifiFragment;
            return wifiFragment;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commu_para_cancle:
                getActivity().finish();
                break;
            case R.id.commu_para_save:
                if (iSaveConnection != null) {
                    iSaveConnection.onSaveClicked(SAVE_MODE_DEFAULT);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        getChildFragmentManager().beginTransaction()
                .replace(R.id.container, getFragment()).commit();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}