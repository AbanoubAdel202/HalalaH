package com.example.halalah.ui.admin_settings.communication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.halalah.R;
import com.example.halalah.TMS.Wifi;

import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WifiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WifiFragment extends Fragment implements ISaveConnection {

    // the fragment initialization parameter, wifi key
    private static final String ARG_WIFI_OBJ = "wifi";

    // TODO: Rename and change types of parameters
    private Wifi mConnection;

    public WifiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param connection wifi connection object.
     * @return A new instance of fragment WifiFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        EditText et_network_ip_address = rootView.findViewById(R.id.et_network_ip_address);
        EditText et_network_tcp_support = rootView.findViewById(R.id.et_network_tcp_support);
        EditText et_count_access_retries = rootView.findViewById(R.id.et_count_access_retries);
        EditText et_response_timeout = rootView.findViewById(R.id.et_response_timeout);
        EditText et_ssl_certificate_file = rootView.findViewById(R.id.et_ssl_certificate_file);

        if (mConnection != null) {
            et_network_ip_address.setText(mConnection.Network_IP_Address);
            et_network_tcp_support.setText(mConnection.Network_TCP_Support);
            et_count_access_retries.setText(mConnection.Count_Access_Retries);
            et_response_timeout.setText(mConnection.Response_Time_Out);
            et_ssl_certificate_file.setText(mConnection.SSL_Certificate_File);
        }

        return rootView;
    }

    @Override
    public void onSaveClicked() {
//        validate();
//        save();
    }
}