package com.example.halalah.ui.admin_settings.communication;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.halalah.R;
import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.TMS.TMSManager;
import com.example.halalah.TMS.Wifi;
import com.example.halalah.storage.SharedPreferencesManager;

import org.parceler.Parcels;

public class WifiFragment extends Fragment implements ISaveConnection {

    // the fragment initialization parameter, wifi key
    private static final String ARG_WIFI_OBJ = "wifi";

    private Wifi mConnection;
    private SharedPreferencesManager sharedPreferencesManager;

    private EditText et_tpdu;
    private EditText et_network_ip_address;
    private EditText et_network_tcp_port;
    private EditText et_count_access_retries;
    private EditText et_response_timeout;
    private EditText et_ssl_certificate_file;
    private String tpdu;
    private String ip;
    private String port;
    private String retries_count;
    private String response_timeout;

    public WifiFragment() {
        // Required empty public constructor
    }

    public static WifiFragment newInstance(Wifi connection) {
        WifiFragment fragment = new WifiFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_WIFI_OBJ, Parcels.wrap(connection));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mConnection = Parcels.unwrap(getArguments().getParcelable(ARG_WIFI_OBJ));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.communication_setting_wifi, container, false);
        et_tpdu = rootView.findViewById(R.id.et_tpdu);
        et_network_ip_address = rootView.findViewById(R.id.et_network_ip_address);
        et_network_tcp_port = rootView.findViewById(R.id.et_network_tcp_port);
        et_count_access_retries = rootView.findViewById(R.id.et_count_access_retries);
        et_response_timeout = rootView.findViewById(R.id.et_response_timeout);
        et_ssl_certificate_file = rootView.findViewById(R.id.et_ssl_certificate_file);

        sharedPreferencesManager = new SharedPreferencesManager(getActivity());

        if (mConnection != null) {
            et_network_ip_address.setText(mConnection.Network_IP_Address);
            et_network_tcp_port.setText(mConnection.Network_TCP_Support);
            et_count_access_retries.setText(mConnection.Count_Access_Retries);
            et_response_timeout.setText(mConnection.Response_Time_Out);
            et_ssl_certificate_file.setText(mConnection.SSL_Certificate_File);
            et_tpdu.setText(sharedPreferencesManager.getTPDU());
        }

        return rootView;
    }

    @Override
    public void onSaveClicked(int saveMode) {

        if (!validateInput()) {
            return;
        }

        Wifi wifi = new Wifi();
        wifi.Network_IP_Address = ip;
        wifi.Network_TCP_Support = port;
        wifi.Count_Access_Retries = retries_count;
        wifi.Response_Time_Out = response_timeout;

        if (saveMode == SAVE_MODE_DEFAULT) {

            sharedPreferencesManager.saveConnection(wifi);
            sharedPreferencesManager.setTPDU(tpdu);
        } else {
            TMSManager tmsManager = TMSManager.getInstance();
            Connection_Parameters connection_parameters = tmsManager.getConnectionParameters();
            if (connection_parameters == null) {
                connection_parameters = new Connection_Parameters();
            }

            if (mConnection.Priority.equals("1")) {
                connection_parameters.setConn_primary(wifi);
                connection_parameters.setPrimaryConnectionType("04");
            } else {
                connection_parameters.setConn_secondary(wifi);
                connection_parameters.setSecondaryConnectionType("04");
            }
        }
        Toast.makeText(getActivity(), "Connection Saved Successfully", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    private boolean validateInput() {

        tpdu = et_tpdu.getText().toString();
        if (TextUtils.isEmpty(tpdu)) {
            et_tpdu.setError("tpdu error");
            et_tpdu.requestFocus();
            return false;
        }

        ip = et_network_ip_address.getText().toString();
        if (!Patterns.IP_ADDRESS.matcher(ip).matches()) {
            et_network_ip_address.setError("ip error");
            et_network_ip_address.requestFocus();
            return false;
        }

        port = et_network_tcp_port.getText().toString();
        if (TextUtils.isEmpty(port)) {
            et_network_tcp_port.setError("port error");
            et_network_tcp_port.requestFocus();
            return false;
        }

        retries_count = et_count_access_retries.getText().toString();
        if (TextUtils.isEmpty(retries_count)) {
            et_count_access_retries.setError("retries count error");
            et_count_access_retries.requestFocus();
            return false;
        }

        response_timeout = et_response_timeout.getText().toString();
        if (TextUtils.isEmpty(response_timeout)) {
            et_response_timeout.setError("response timeout error");
            et_response_timeout.requestFocus();
            return false;
        }

        return true;
    }
}