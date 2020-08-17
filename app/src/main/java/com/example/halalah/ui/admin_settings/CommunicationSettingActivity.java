package com.example.halalah.ui.admin_settings;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.example.halalah.R;
import com.example.halalah.TMS.Connection;
import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.TMS.Dialup;
import com.example.halalah.TMS.Gprs;
import com.example.halalah.TMS.Gsm;
import com.example.halalah.TMS.TMSManager;
import com.example.halalah.TMS.Tcp_IP;
import com.example.halalah.TMS.Wifi;
import com.example.halalah.storage.CommunicationInfo;

import static com.example.halalah.Utils.CONNECTION_TYPE_DIALUP;
import static com.example.halalah.Utils.CONNECTION_TYPE_GPRS;
import static com.example.halalah.Utils.CONNECTION_TYPE_GSM;
import static com.example.halalah.Utils.CONNECTION_TYPE_TCPIP;
import static com.example.halalah.Utils.CONNECTION_TYPE_WIFI;

public class CommunicationSettingActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_communication_setting);
        initViews();

        TMSManager tmsManager = TMSManager.getInstance();

        mConnectionParameters = tmsManager.getConnectionParameters();

        if (mConnectionParameters == null) {
            initConnectionForFirstTime();
        } else {
            setConnectionData(primaryDetailsLayout, mConnectionParameters.getConn_primary());
            setConnectionData(secondaryDetailsLayout, mConnectionParameters.getConn_secondary());
        }
    }

    private void initViews() {
        primaryLayout = findViewById(R.id.primary_connection_layout);
        primaryDetailsLayout = findViewById(R.id.primary_connection_details);
        primaryTV = findViewById(R.id.primary_connection_tv);
        primaryCheckBox = findViewById(R.id.primary_checkbox);

        secondaryLayout = findViewById(R.id.secondary_connection_layout);
        secondaryDetailsLayout = findViewById(R.id.secondary_connection_details);
        secondaryTV = findViewById(R.id.secondary_connection_tv);
        secondaryCheckBox = findViewById(R.id.secondary_checkbox);

        saveBtn = findViewById(R.id.commu_para_save);
        cancelBtn = findViewById(R.id.commu_para_cancle);

        primaryLayout.setOnClickListener(this);
        primaryDetailsLayout.setOnClickListener(this);
        secondaryLayout.setOnClickListener(this);
        secondaryDetailsLayout.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        primaryCheckBox.setOnCheckedChangeListener(this);
        secondaryCheckBox.setOnCheckedChangeListener(this);
    }

    private void initConnectionForFirstTime() {
        secondaryLayout.setVisibility(View.GONE);
        secondaryDetailsLayout.setVisibility(View.GONE);
        setConnectionData(primaryLayout, null);

//        Patterns.IP_ADDRESS.matcher("url").matches();
    }

    private void setConnectionData(ViewGroup root, Connection connection) {

        if (connection != null) {

            String connectionName = getConnectionName(connection.Communication_Type);
            if (connection.Priority.equals("1")) {
                primaryTV.setText(primaryTV.getText().toString() + "(" + connectionName + ")");
            } else if (connection.Priority.equals("2")) {
                secondaryTV.setText(secondaryTV.getText().toString() + "(" + connectionName + ")");
            }

            switch (connection.Communication_Type) {
                case CONNECTION_TYPE_DIALUP:
                    setDialupData((Dialup) connection, root);
                    break;
                case CONNECTION_TYPE_TCPIP:
                    setTCPIPData((Tcp_IP) connection, root);
                    break;
                case CONNECTION_TYPE_GPRS:
                    setGPRSData((Gprs) connection, root);
                    break;
                case CONNECTION_TYPE_GSM:
                    setGSMData((Gsm) connection, root);
                    break;
                default:
                case CONNECTION_TYPE_WIFI:
                    setWIFIData((Wifi) connection, root);
                    break;
            }
        }
    }

    private String getConnectionName(String communication_type) {
        switch (communication_type){
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

        EditText et_network_ip_address = root.findViewById(R.id.et_network_ip_address);
        EditText et_network_tcp_support = root.findViewById(R.id.et_network_tcp_support);
        EditText et_count_access_retries = root.findViewById(R.id.et_count_access_retries);
        EditText et_response_timeout = root.findViewById(R.id.et_response_timeout);
        EditText et_ssl_certificate_file = root.findViewById(R.id.et_ssl_certificate_file);

        et_network_ip_address.setText(connection.Network_IP_Address);
        et_network_tcp_support.setText(connection.Network_TCP_Support);
        et_count_access_retries.setText(connection.Count_Access_Retries);
        et_response_timeout.setText(connection.Response_Time_Out);
        et_ssl_certificate_file.setText(connection.SSL_Certificate_File);

    }

    private void setGSMData(Gsm connection, ViewGroup root) {

        final View addedView = getLayoutInflater().inflate(R.layout.communication_setting_gsm,
                null);
        root.addView(addedView);
        root.setVisibility(View.GONE);

    }

    private void setGPRSData(Gprs connection, ViewGroup root) {

        final View addedView = getLayoutInflater().inflate(R.layout.communication_setting_gprs,
                null);
        root.addView(addedView);
        root.setVisibility(View.GONE);

        EditText et_dial_Number = root.findViewById(R.id.et_dial_Number);
        EditText et_access_point_name = root.findViewById(R.id.et_access_point_name);
        EditText et_connect_time = root.findViewById(R.id.et_connect_time);
        EditText et_network_ip_address = root.findViewById(R.id.et_network_ip_address);
        EditText et_network_tcp_port = root.findViewById(R.id.et_network_tcp_port);
        EditText et_dial_attempts = root.findViewById(R.id.et_dial_attempts);
        EditText et_response_timeout = root.findViewById(R.id.et_response_timeout);
        EditText et_ssl_certificate_file = root.findViewById(R.id.et_ssl_certificate_file);

        et_dial_Number.setText(connection.GPRS_dial_Number);
        et_access_point_name.setText(connection.GPRS_access_point_name);
        et_connect_time.setText(connection.Connect__Time_for_GPRS_phone);
        et_network_ip_address.setText(connection.Network_IP_address);
        et_network_tcp_port.setText(connection.Network_TCP_port);
        et_dial_attempts.setText(connection.Dial_attempts_to_network);
        et_response_timeout.setText(connection.Response_time_out);
        et_ssl_certificate_file.setText(connection.SSL_Certificate_file);

    }

    private void setTCPIPData(Tcp_IP connection, ViewGroup root) {
        final View addedView = getLayoutInflater().inflate(R.layout.communication_setting_tcpip,
                null);
        root.addView(addedView);
        root.setVisibility(View.GONE);
    }

    private void setDialupData(Dialup connection, ViewGroup root) {

        final View addedView = getLayoutInflater().inflate(R.layout.communication_setting_dialup,
                null);
        root.addView(addedView);
        root.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.primary_connection_layout:
                if (primaryDetailsLayout.getVisibility() == View.VISIBLE){
                    primaryDetailsLayout.setVisibility(View.GONE);
                } else {
                    primaryDetailsLayout.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.secondary_connection_layout:
                if (secondaryDetailsLayout.getVisibility() == View.VISIBLE){
                    secondaryDetailsLayout.setVisibility(View.GONE);
                } else {
                    secondaryDetailsLayout.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.commu_para_cancle:
                finish();
                break;
            case R.id.commu_para_save:
//                mCommunicationInfo.setHostIP(mHostIp1.getText().toString());
//                mCommunicationInfo.setHostPort(mHostPort1.getText().toString());
//                mCommunicationInfo.setSpareHostIP(mHostIp2.getText().toString());
//                mCommunicationInfo.setSpareHostPort(mHostPort2.getText().toString());
//                mCommunicationInfo.setTPDU(mTPDU.getText().toString());
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked){
            if (compoundButton.getId() == R.id.primary_checkbox){
                secondaryCheckBox.setChecked(false);
            } else if (compoundButton.getId() == R.id.secondary_checkbox){
                primaryCheckBox.setChecked(false);
            }
        } else {
            if (compoundButton.getId() == R.id.primary_checkbox){
                secondaryCheckBox.setChecked(true);
            } else if (compoundButton.getId() == R.id.secondary_checkbox){
                primaryCheckBox.setChecked(true);
            }
        }
    }
}
