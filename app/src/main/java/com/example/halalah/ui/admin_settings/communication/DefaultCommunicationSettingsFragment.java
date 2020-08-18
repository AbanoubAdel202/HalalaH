package com.example.halalah.ui.admin_settings.communication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.halalah.R;
import com.example.halalah.storage.CommunicationInfo;

public class DefaultCommunicationSettingsFragment extends Fragment implements View.OnClickListener {

    private EditText mHostIp1;
    private EditText mHostPort1;
    private EditText mHostIp2;
    private EditText mHostPort2;
    private EditText mTPDU;
    private Button cancelBtn;
    private Button saveBtn;
    private CommunicationInfo mCommunicationInfo;

    public DefaultCommunicationSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.communication_setting_default, container, false);
        mCommunicationInfo = new CommunicationInfo(getActivity().getApplicationContext());

        mHostIp1 = rootView.findViewById(R.id.host_ip_1);
        mHostIp1.setText(mCommunicationInfo.getHostIP());
        mHostPort1 = rootView.findViewById(R.id.host_port_1);
        mHostPort1.setText(mCommunicationInfo.getHostPort());
        mHostIp2 = rootView.findViewById(R.id.host_ip_2);
        mHostIp2.setText(mCommunicationInfo.getSpareHostIP());
        mHostPort2 = rootView.findViewById(R.id.host_port_2);
        mHostPort2.setText(mCommunicationInfo.getSpareHostPort());
        mTPDU = rootView.findViewById(R.id.tpdu_value);
        mTPDU.setText(mCommunicationInfo.getTPDU());

        saveBtn = rootView.findViewById(R.id.commu_para_save);
        saveBtn.setOnClickListener(this);
        cancelBtn = rootView.findViewById(R.id.commu_para_cancle);
        cancelBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commu_para_cancle:
                getActivity().finish();
                break;
            case R.id.commu_para_save:
                mCommunicationInfo.setHostIP(mHostIp1.getText().toString());
                mCommunicationInfo.setHostPort(mHostPort1.getText().toString());
                mCommunicationInfo.setSpareHostIP(mHostIp2.getText().toString());
                mCommunicationInfo.setSpareHostPort(mHostPort2.getText().toString());
                mCommunicationInfo.setTPDU(mTPDU.getText().toString());
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}