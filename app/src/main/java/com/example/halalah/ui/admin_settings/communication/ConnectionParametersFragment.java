package com.example.halalah.ui.admin_settings.communication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

import com.example.halalah.R;
import com.example.halalah.TMS.Connection;
import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.TMS.Gprs;
import com.example.halalah.TMS.TMSManager;
import com.example.halalah.TMS.Wifi;

import static com.example.halalah.Utils.CONNECTION_TYPE_DIALUP;
import static com.example.halalah.Utils.CONNECTION_TYPE_GPRS;
import static com.example.halalah.Utils.CONNECTION_TYPE_GSM;
import static com.example.halalah.Utils.CONNECTION_TYPE_TCPIP;
import static com.example.halalah.Utils.CONNECTION_TYPE_WIFI;


public class ConnectionParametersFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ViewGroup primaryLayout;
    private ViewGroup primaryDetailsLayout;
    private ViewGroup secondaryLayout;
    private ViewGroup secondaryDetailsLayout;
    private Button saveBtn;
    private Button cancelBtn;
    private TextView primaryTV;
    private TextView secondaryTV;
    private CheckBox primaryCheckBox;
    private CheckBox secondaryCheckBox;

    private Connection_Parameters mConnectionParameters;

    ISaveConnection iSaveConnection1;
    ISaveConnection iSaveConnection2;

    public ConnectionParametersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_communication_setting, container, false);
        initViews(rootView);

        mConnectionParameters = TMSManager.getInstance().getConnectionParameters();
        setConnectionData(R.id.primary_connection_details, mConnectionParameters.getConn_primary());
        setConnectionData(R.id.secondary_connection_details, mConnectionParameters.getConn_secondary());
        return rootView;
    }

    private void initViews(View rootView) {
        primaryLayout = rootView.findViewById(R.id.primary_connection_layout);
        primaryDetailsLayout = rootView.findViewById(R.id.primary_connection_details);
        primaryTV = rootView.findViewById(R.id.primary_connection_tv);
        primaryCheckBox = rootView.findViewById(R.id.primary_checkbox);

        secondaryLayout = rootView.findViewById(R.id.secondary_connection_layout);
        secondaryDetailsLayout = rootView.findViewById(R.id.secondary_connection_details);
        secondaryTV = rootView.findViewById(R.id.secondary_connection_tv);
        secondaryCheckBox = rootView.findViewById(R.id.secondary_checkbox);

        saveBtn = rootView.findViewById(R.id.commu_para_save);
        cancelBtn = rootView.findViewById(R.id.commu_para_cancle);

        primaryLayout.setOnClickListener(this);
        primaryDetailsLayout.setOnClickListener(this);
        secondaryLayout.setOnClickListener(this);
        secondaryDetailsLayout.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        primaryCheckBox.setOnCheckedChangeListener(this);
        secondaryCheckBox.setOnCheckedChangeListener(this);
    }

    private void saveConnectionParameters() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.primary_connection_layout:
                if (primaryDetailsLayout.getVisibility() == View.VISIBLE) {
                    primaryDetailsLayout.setVisibility(View.GONE);
                } else {
                    primaryDetailsLayout.setVisibility(View.VISIBLE);
                    secondaryDetailsLayout.setVisibility(View.GONE);
                }
                break;

            case R.id.secondary_connection_layout:
                if (secondaryDetailsLayout.getVisibility() == View.VISIBLE) {
                    secondaryDetailsLayout.setVisibility(View.GONE);
                } else {
                    secondaryDetailsLayout.setVisibility(View.VISIBLE);
                    primaryDetailsLayout.setVisibility(View.GONE);
                }
                break;

            case R.id.commu_para_cancle:
                getActivity().finish();
                break;
            case R.id.commu_para_save:

                saveConnectionParameters();

                getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked) {
            if (compoundButton.getId() == R.id.primary_checkbox) {
                secondaryCheckBox.setChecked(false);
            } else if (compoundButton.getId() == R.id.secondary_checkbox) {
                primaryCheckBox.setChecked(false);
            }
        } else {
            if (compoundButton.getId() == R.id.primary_checkbox) {
                secondaryCheckBox.setChecked(true);
            } else if (compoundButton.getId() == R.id.secondary_checkbox) {
                primaryCheckBox.setChecked(true);
            }
        }
    }

    private void setConnectionData(@IdRes int containerId, Connection connection) {

        if (connection != null) {

            String connectionName = getConnectionName(connection.Communication_Type);
            if (connection.Priority.equals("1")) {
                primaryTV.setText(primaryTV.getText().toString() + "(" + connectionName + ")");
            } else if (connection.Priority.equals("2")) {
                secondaryTV.setText(secondaryTV.getText().toString() + "(" + connectionName + ")");
            }

            switch (connection.Communication_Type) {
                case CONNECTION_TYPE_DIALUP:
//                    setDialupData((Dialup) connection, root);
                    break;
                case CONNECTION_TYPE_TCPIP:
//                    setTCPIPData((Tcp_IP) connection, root);
                    break;
                case CONNECTION_TYPE_GPRS:
                    GPRSFragment gprsFragment = GPRSFragment.newInstance((Gprs) connection);
                    iSaveConnection1 = gprsFragment;
                    getChildFragmentManager().beginTransaction()
                            .add(containerId, gprsFragment).commit();
                    break;
                case CONNECTION_TYPE_GSM:
//                    setGSMData((Gsm) connection, root);
                    break;
                default:
                case CONNECTION_TYPE_WIFI:
                    WifiFragment wifiFragment = WifiFragment.newInstance((Wifi) connection);
                    iSaveConnection2 = wifiFragment;

                    getChildFragmentManager().beginTransaction()
                            .add(containerId, wifiFragment).commit();

                    break;
            }
        }
    }

    private String getConnectionName(String communication_type) {
        switch (communication_type) {
            case "01":
                return "Dialup";
            case "02":
                return "TCP/IP";
            case "03":
                return "GPRS";
            case "04":
                return "wifi";
            case "05":
                return "GSM";
            default:
                return "";
        }
    }

    private void setWIFIData(Wifi connection, ViewGroup root) {

        final View addedView = getLayoutInflater().inflate(R.layout.communication_setting_wifi,
                null);
        root.addView(addedView);
        root.setVisibility(View.GONE);

    }


}