package com.example.halalah.ui.admin_settings.communication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.halalah.R;
import com.example.halalah.TMS.Gprs;

import org.parceler.Parcels;


public class GPRSFragment extends Fragment implements ISaveConnection {

    // the fragment initialization parameter, wifi key
    private static final String ARG_GPRS_OBJ = "gprs";

    private Gprs mConnection;

    public GPRSFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param connection Gprs connection object.
     * @return A new instance of fragment WifiFragment.
     */

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

        EditText et_dial_Number = rootView.findViewById(R.id.et_dial_Number);
        EditText et_access_point_name = rootView.findViewById(R.id.et_access_point_name);
        EditText et_connect_time = rootView.findViewById(R.id.et_connect_time);
        EditText et_network_ip_address = rootView.findViewById(R.id.et_network_ip_address);
        EditText et_network_tcp_port = rootView.findViewById(R.id.et_network_tcp_port);
        EditText et_dial_attempts = rootView.findViewById(R.id.et_dial_attempts);
        EditText et_response_timeout = rootView.findViewById(R.id.et_response_timeout);
        EditText et_ssl_certificate_file = rootView.findViewById(R.id.et_ssl_certificate_file);

        if (mConnection != null) {
            et_dial_Number.setText(mConnection.GPRS_dial_Number);
            et_access_point_name.setText(mConnection.GPRS_access_point_name);
            et_connect_time.setText(mConnection.Connect__Time_for_GPRS_phone);
            et_network_ip_address.setText(mConnection.Network_IP_address);
            et_network_tcp_port.setText(mConnection.Network_TCP_port);
            et_dial_attempts.setText(mConnection.Dial_attempts_to_network);
            et_response_timeout.setText(mConnection.Response_time_out);
            et_ssl_certificate_file.setText(mConnection.SSL_Certificate_file);
        }

        return rootView;
    }

    @Override
    public void onSaveClicked() {
//        validate();
//        save();
    }
}