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
import com.example.halalah.TMS.Gprs;
import com.example.halalah.TMS.TMSManager;
import com.example.halalah.storage.SharedPreferencesManager;

import org.parceler.Parcels;


public class GPRSFragment extends Fragment implements ISaveConnection {

    // the fragment initialization parameter, gprs key
    private static final String ARG_GPRS_OBJ = "gprs";

    private Gprs mConnection;
    private SharedPreferencesManager sharedPreferencesManager;
    private EditText et_tpdu;
    private EditText et_dial_Number;
    private EditText et_access_point_name;
    private EditText et_connect_time;
    private EditText et_network_ip_address;
    private EditText et_network_tcp_port;
    private EditText et_dial_attempts;
    private EditText et_response_timeout;
    private EditText et_ssl_certificate_file;

    private String tpdu;
    private String dial_Number;
    private String access_point_name;
    private String connect_time;
    private String ip;
    private String port;
    private String dial_attempts;
    private String response_timeout;

    public GPRSFragment() {
        // Required empty public constructor
    }

    public static GPRSFragment newInstance(Gprs connection) {
        GPRSFragment fragment = new GPRSFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GPRS_OBJ, Parcels.wrap(connection));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mConnection = Parcels.unwrap(getArguments().getParcelable(ARG_GPRS_OBJ));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.communication_setting_gprs, container, false);

        et_tpdu = rootView.findViewById(R.id.et_tpdu);
        et_dial_Number = rootView.findViewById(R.id.et_dial_Number);
        et_access_point_name = rootView.findViewById(R.id.et_access_point_name);
        et_connect_time = rootView.findViewById(R.id.et_connect_time);
        et_network_ip_address = rootView.findViewById(R.id.et_network_ip_address);
        et_network_tcp_port = rootView.findViewById(R.id.et_network_tcp_port);
        et_dial_attempts = rootView.findViewById(R.id.et_dial_attempts);
        et_response_timeout = rootView.findViewById(R.id.et_response_timeout);
        et_ssl_certificate_file = rootView.findViewById(R.id.et_ssl_certificate_file);

        sharedPreferencesManager = new SharedPreferencesManager(getActivity());

        if (mConnection != null) {
            et_dial_Number.setText(mConnection.GPRS_dial_Number);
            et_access_point_name.setText(mConnection.GPRS_access_point_name);
            et_connect_time.setText(mConnection.Connect__Time_for_GPRS_phone);
            et_network_ip_address.setText(mConnection.Network_IP_address);
            et_network_tcp_port.setText(mConnection.Network_TCP_port);
            et_dial_attempts.setText(mConnection.Dial_attempts_to_network);
            et_response_timeout.setText(mConnection.Response_time_out);
            et_ssl_certificate_file.setText(mConnection.SSL_Certificate_file);
            et_tpdu.setText(sharedPreferencesManager.getTPDU());
        }

        return rootView;
    }

    @Override
    public void onSaveClicked(int saveMode) {

        if (!validateInput()) {
            return;
        }

        Gprs gprs = new Gprs();
        gprs.GPRS_dial_Number = dial_Number;
        gprs.GPRS_access_point_name = access_point_name;
        gprs.Connect__Time_for_GPRS_phone = connect_time;
        gprs.Network_IP_address = ip;
        gprs.Network_TCP_port = port;
        gprs.Dial_attempts_to_network = dial_attempts;
        gprs.Response_time_out = response_timeout;

        if (saveMode == SAVE_MODE_DEFAULT) {
            sharedPreferencesManager.saveConnection(gprs);
            sharedPreferencesManager.setTPDU(tpdu);
        } else {
            Connection_Parameters connection_parameters = TMSManager.getInstance().getConnectionParameters();
            if (connection_parameters != null) {
                connection_parameters = new Connection_Parameters();
            }

            if (mConnection.Priority.equals("1")) {
                connection_parameters.setConn_primary(gprs);
                connection_parameters.setPrimaryConnectionType("03");
            } else {
                connection_parameters.setConn_secondary(gprs);
                connection_parameters.setSecondaryConnectionType("03");
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

        dial_Number = et_dial_Number.getText().toString();
        if (TextUtils.isEmpty(dial_Number)) {
            et_dial_Number.setError("Dial Number error");
            et_dial_Number.requestFocus();
            return false;
        }

        access_point_name = et_access_point_name.getText().toString();
        if (TextUtils.isEmpty(access_point_name)) {
            et_access_point_name.setError("Access point_name error");
            et_access_point_name.requestFocus();
            return false;
        }

        connect_time = et_connect_time.getText().toString();
        if (TextUtils.isEmpty(connect_time)) {
            et_connect_time.setError("Connect Time error");
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

        dial_attempts = et_dial_attempts.getText().toString();
        if (TextUtils.isEmpty(dial_attempts)) {
            et_dial_attempts.setError("dial attempts error");
            et_dial_attempts.requestFocus();
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