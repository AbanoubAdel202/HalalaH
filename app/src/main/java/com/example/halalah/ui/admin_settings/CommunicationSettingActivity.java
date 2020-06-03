package com.example.halalah.ui.admin_settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.halalah.R;
import com.example.halalah.storage.CommunicationInfo;

public class CommunicationSettingActivity extends Activity implements View.OnClickListener{

    private EditText mHostIp1;
    private EditText mHostPort1;
    private EditText mHostIp2;
    private EditText mHostPort2;
    private EditText mTPDU;
    private CommunicationInfo mCommunicationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_setting);

        mCommunicationInfo = new CommunicationInfo(this);

        mHostIp1 = (EditText) findViewById(R.id.host_ip_1);
        mHostIp1.setText(mCommunicationInfo.getHostIP());
        mHostPort1 = (EditText) findViewById(R.id.host_port_1);
        mHostPort1.setText(mCommunicationInfo.getHostPort());
        mHostIp2 = (EditText) findViewById(R.id.host_ip_2);
        mHostIp2.setText(mCommunicationInfo.getSpareHostIP());
        mHostPort2 = (EditText) findViewById(R.id.host_port_2);
        mHostPort2.setText(mCommunicationInfo.getSpareHostPort());
        mTPDU = (EditText) findViewById(R.id.tpdu_value);
        mTPDU.setText(mCommunicationInfo.getTPDU());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commu_para_cancle:
                finish();
                break;
            case R.id.commu_para_save:
                mCommunicationInfo.setHostIP(mHostIp1.getText().toString());
                mCommunicationInfo.setHostPort(mHostPort1.getText().toString());
                mCommunicationInfo.setSpareHostIP(mHostIp2.getText().toString());
                mCommunicationInfo.setSpareHostPort(mHostPort2.getText().toString());
                mCommunicationInfo.setTPDU(mTPDU.getText().toString());
                finish();
                break;
            default:
                break;
        }
    }
}
