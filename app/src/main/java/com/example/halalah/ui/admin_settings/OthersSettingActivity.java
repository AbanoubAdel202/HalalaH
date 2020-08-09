package com.example.halalah.ui.admin_settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.halalah.R;
import com.example.halalah.Utils;
import com.example.halalah.ui.PacketProcessActivity;
import com.example.halalah.util.PacketProcessUtils;

public class OthersSettingActivity extends Activity implements View.OnClickListener{
    private static final String TAG = Utils.TAGPUBLIC + OthersSettingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_setting);
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick()");
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.image_param_trans:
                intent.setClass(this, PacketProcessActivity.class);
              //  intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PARAM_TRANS);
                break;
            case R.id.image_status_upload:
                intent.setClass(this, PacketProcessActivity.class);
              //  intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_STATUS_UPLOAD);
                break;
            case R.id.image_sign_up:
                intent.setClass(this, PacketProcessActivity.class);
             //   intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_SIGN_UP);
                break;
            case R.id.image_ic_capk_download:
                intent.setClass(this, PacketProcessActivity.class);
              //  intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_IC_CAPK_DOWNLOAD);
                break;
            case R.id.image_ic_para_download:
                intent.setClass(this, PacketProcessActivity.class);
             //   intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_IC_PARA_DOWNLOAD);
                break;
            case R.id.image_echo_test:
                intent.setClass(this, PacketProcessActivity.class);
              //  intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_ECHO_TEST);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
